package com.quizmaster.service;

import com.quizmaster.entity.Category;
import com.quizmaster.entity.Option;
import com.quizmaster.entity.Question;
import com.quizmaster.entity.Quiz;
import com.quizmaster.payload.request.QuizRequest;
import com.quizmaster.payload.response.OptionPublicDto;
import com.quizmaster.payload.response.QuizDto;
import com.quizmaster.payload.response.QuizQuestionDto;
import com.quizmaster.repository.CategoryRepository;
import com.quizmaster.repository.QuestionRepository;
import com.quizmaster.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuizDto> getQuizzes(Long categoryId, String difficulty, String keyword) {
        List<Quiz> quizzes;

        if (categoryId != null && difficulty != null && !difficulty.isBlank()) {
            quizzes = quizRepository.findByCategoryIdAndDifficultyAndActiveTrue(categoryId, difficulty);
        } else if (categoryId != null) {
            quizzes = quizRepository.findByCategoryIdAndActiveTrue(categoryId);
        } else if (difficulty != null && !difficulty.isBlank()) {
            quizzes = quizRepository.findByDifficultyAndActiveTrue(difficulty);
        } else if (keyword != null && !keyword.isBlank()) {
            quizzes = quizRepository.findByTitleContainingIgnoreCaseAndActiveTrue(keyword);
        } else {
            quizzes = quizRepository.findByActiveTrue();
        }

        return quizzes.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<QuizDto> getAllQuizzesAdmin() {
        return quizRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

    public QuizDto getQuizDetail(Long id) {
        Quiz quiz = getQuizById(id);
        return mapToDto(quiz);
    }

    public List<QuizQuestionDto> getQuestionsForAttempt(Long quizId) {
        List<Question> questions = questionRepository.findByQuizIdOrderBySortOrderAsc(quizId);
        List<QuizQuestionDto> dtos = new ArrayList<>();

        for (Question q : questions) {
            QuizQuestionDto dto = new QuizQuestionDto();
            dto.setId(q.getId());
            dto.setQuestionText(q.getQuestionText());
            dto.setQuestionType(q.getQuestionType());
            dto.setDifficulty(q.getDifficulty());
            dto.setTopic(q.getTopic());
            dto.setMarks(q.getMarks());
            dto.setSortOrder(q.getSortOrder());

            List<OptionPublicDto> optionDtos = q.getOptions().stream()
                    .map(o -> new OptionPublicDto(o.getId(), o.getOptionText(), o.getSortOrder()))
                    .collect(Collectors.toList());
            dto.setOptions(optionDtos);

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public Quiz createQuiz(QuizRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCategory(category);
        quiz.setDifficulty(request.getDifficulty());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setTotalMarks(request.getTotalMarks());
        quiz.setPassPercentage(request.getPassPercentage());
        quiz.setActive(request.getActive() != null ? request.getActive() : true);

        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz updateQuiz(Long id, QuizRequest request) {
        Quiz quiz = getQuizById(id);
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            quiz.setCategory(category);
        }

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDifficulty(request.getDifficulty());
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setTotalMarks(request.getTotalMarks());
        quiz.setPassPercentage(request.getPassPercentage());
        if (request.getActive() != null) {
            quiz.setActive(request.getActive());
        }

        return quizRepository.save(quiz);
    }

    @Transactional
    public void deleteQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quizRepository.delete(quiz);
    }

    public QuizDto mapToDto(Quiz quiz) {
        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setDifficulty(quiz.getDifficulty());
        dto.setTimeLimitMinutes(quiz.getTimeLimitMinutes());
        dto.setTotalMarks(quiz.getTotalMarks());
        dto.setPassPercentage(quiz.getPassPercentage());
        dto.setActive(quiz.getActive());

        if (quiz.getCategory() != null) {
            dto.setCategoryId(quiz.getCategory().getId());
            dto.setCategoryName(quiz.getCategory().getName());
            dto.setCategoryIcon(quiz.getCategory().getIcon());
            dto.setCategoryColor(quiz.getCategory().getColorCode());
        }

        Long qCount = questionRepository.countByQuizId(quiz.getId());
        dto.setQuestionCount(qCount != null ? qCount.intValue() : 0);

        return dto;
    }
}
