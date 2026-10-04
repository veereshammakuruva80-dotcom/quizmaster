package com.quizmaster.service;

import com.quizmaster.entity.Option;
import com.quizmaster.entity.Question;
import com.quizmaster.entity.Quiz;
import com.quizmaster.payload.request.OptionDto;
import com.quizmaster.payload.request.QuestionRequest;
import com.quizmaster.repository.OptionRepository;
import com.quizmaster.repository.QuestionRepository;
import com.quizmaster.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private OptionRepository optionRepository;

    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizIdOrderBySortOrderAsc(quizId);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }

    @Transactional
    public Question createQuestion(QuestionRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + request.getQuizId()));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(request.getQuestionText());
        question.setQuestionType(request.getQuestionType() != null ? request.getQuestionType() : "MCQ");
        question.setDifficulty(request.getDifficulty() != null ? request.getDifficulty() : "INTERMEDIATE");
        question.setTopic(request.getTopic());
        question.setMarks(request.getMarks() != null ? request.getMarks() : 5);
        question.setExplanation(request.getExplanation());
        question.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 1);

        Question savedQuestion = questionRepository.save(question);

        if (request.getOptions() != null) {
            int order = 1;
            for (OptionDto optDto : request.getOptions()) {
                Option option = new Option(savedQuestion, optDto.getOptionText(),
                        optDto.getIsCorrect() != null ? optDto.getIsCorrect() : false,
                        optDto.getSortOrder() != null ? optDto.getSortOrder() : order++);
                optionRepository.save(option);
            }
        }

        return questionRepository.findById(savedQuestion.getId()).orElse(savedQuestion);
    }

    @Transactional
    public Question updateQuestion(Long id, QuestionRequest request) {
        Question question = getQuestionById(id);
        question.setQuestionText(request.getQuestionText());
        question.setQuestionType(request.getQuestionType());
        question.setDifficulty(request.getDifficulty());
        question.setTopic(request.getTopic());
        question.setMarks(request.getMarks());
        question.setExplanation(request.getExplanation());
        question.setSortOrder(request.getSortOrder());

        // Update options: delete old options and insert updated ones
        optionRepository.deleteAll(question.getOptions());
        question.getOptions().clear();

        if (request.getOptions() != null) {
            int order = 1;
            for (OptionDto optDto : request.getOptions()) {
                Option option = new Option(question, optDto.getOptionText(),
                        optDto.getIsCorrect() != null ? optDto.getIsCorrect() : false,
                        optDto.getSortOrder() != null ? optDto.getSortOrder() : order++);
                optionRepository.save(option);
                question.getOptions().add(option);
            }
        }

        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }
}
