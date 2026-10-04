package com.quizmaster.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmaster.entity.*;
import com.quizmaster.payload.request.AnswerSubmissionDto;
import com.quizmaster.payload.request.SubmitQuizRequest;
import com.quizmaster.payload.response.*;
import com.quizmaster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizAttemptService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private QuizService quizService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Map<String, Object> startQuizAttempt(Long userId, Long quizId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));

        // Create new attempt
        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus("IN_PROGRESS");
        Long qCount = questionRepository.countByQuizId(quizId);
        attempt.setTotalQuestions(qCount != null ? qCount.intValue() : 0);

        QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

        List<QuizQuestionDto> questions = quizService.getQuestionsForAttempt(quizId);

        Map<String, Object> response = new HashMap<>();
        response.put("attemptId", savedAttempt.getId());
        response.put("quiz", quizService.mapToDto(quiz));
        response.put("questions", questions);
        response.put("startTime", savedAttempt.getStartTime());

        return response;
    }

    @Transactional
    public QuizResultDto submitQuizAttempt(Long userId, SubmitQuizRequest request) {
        QuizAttempt attempt = quizAttemptRepository.findById(request.getAttemptId())
                .orElseThrow(() -> new RuntimeException("Quiz attempt not found with id: " + request.getAttemptId()));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized attempt submission!");
        }

        // Prevent duplicate submission
        if ("COMPLETED".equalsIgnoreCase(attempt.getStatus())) {
            return getAttemptResult(userId, attempt.getId());
        }

        Quiz quiz = attempt.getQuiz();
        List<Question> questions = questionRepository.findByQuizIdOrderBySortOrderAsc(quiz.getId());

        // Map submitted answers by questionId
        Map<Long, AnswerSubmissionDto> submissionMap = new HashMap<>();
        if (request.getAnswers() != null) {
            for (AnswerSubmissionDto ans : request.getAnswers()) {
                submissionMap.put(ans.getQuestionId(), ans);
            }
        }

        int correctCount = 0;
        int incorrectCount = 0;
        int unansweredCount = 0;
        int totalScore = 0;
        int maxPossibleMarks = 0;

        List<UserAnswer> userAnswersToSave = new ArrayList<>();

        for (Question q : questions) {
            int qMarks = q.getMarks() != null ? q.getMarks() : 5;
            maxPossibleMarks += qMarks;

            Set<Long> correctOptionIds = q.getOptions().stream()
                    .filter(o -> Boolean.TRUE.equals(o.getIsCorrect()))
                    .map(Option::getId)
                    .collect(Collectors.toSet());

            AnswerSubmissionDto userSub = submissionMap.get(q.getId());
            List<Long> selectedIds = (userSub != null && userSub.getSelectedOptionIds() != null)
                    ? userSub.getSelectedOptionIds()
                    : new ArrayList<>();

            boolean isUnanswered = selectedIds.isEmpty();
            boolean isCorrect = false;

            if (isUnanswered) {
                unansweredCount++;
            } else {
                Set<Long> userSelectedSet = new HashSet<>(selectedIds);
                if (userSelectedSet.equals(correctOptionIds)) {
                    isCorrect = true;
                    correctCount++;
                    totalScore += qMarks;
                } else {
                    incorrectCount++;
                }
            }

            UserAnswer ua = new UserAnswer();
            ua.setAttempt(attempt);
            ua.setQuestion(q);
            try {
                ua.setSelectedOptionIdsJson(objectMapper.writeValueAsString(selectedIds));
            } catch (Exception e) {
                ua.setSelectedOptionIdsJson("[]");
            }
            ua.setIsCorrect(isCorrect);
            ua.setMarksAwarded(isCorrect ? qMarks : 0);
            ua.setTimeSpentSeconds(userSub != null && userSub.getTimeSpentSeconds() != null ? userSub.getTimeSpentSeconds() : 0);

            userAnswersToSave.add(ua);
        }

        userAnswerRepository.saveAll(userAnswersToSave);

        double percentage = (maxPossibleMarks > 0) ? (totalScore * 100.0 / maxPossibleMarks) : 0.0;
        double accuracy = (!questions.isEmpty()) ? (correctCount * 100.0 / questions.size()) : 0.0;
        boolean passed = percentage >= (quiz.getPassPercentage() != null ? quiz.getPassPercentage() : 50);

        String performanceLevel;
        if (percentage >= 85.0) {
            performanceLevel = "OUTSTANDING";
        } else if (percentage >= 70.0) {
            performanceLevel = "PROFICIENT";
        } else if (percentage >= 50.0) {
            performanceLevel = "AVERAGE";
        } else {
            performanceLevel = "NEEDS_IMPROVEMENT";
        }

        attempt.setEndTime(LocalDateTime.now());
        attempt.setTotalQuestions(questions.size());
        attempt.setCorrectAnswers(correctCount);
        attempt.setIncorrectAnswers(incorrectCount);
        attempt.setUnansweredCount(unansweredCount);
        attempt.setScore(totalScore);
        attempt.setPercentage(Math.round(percentage * 10.0) / 10.0);
        attempt.setPassed(passed);
        attempt.setTimeTakenSeconds(request.getTimeTakenSeconds() != null ? request.getTimeTakenSeconds() : 0);
        attempt.setStatus("COMPLETED");
        attempt.setPerformanceLevel(performanceLevel);

        quizAttemptRepository.save(attempt);

        // Save Result record
        Result result = new Result();
        result.setAttempt(attempt);
        result.setUser(attempt.getUser());
        result.setQuiz(quiz);
        result.setTotalMarks(maxPossibleMarks);
        result.setScoreObtained(totalScore);
        result.setPercentage(attempt.getPercentage());
        result.setAccuracy(Math.round(accuracy * 10.0) / 10.0);
        result.setPassed(passed);
        result.setPerformanceLevel(performanceLevel);
        result.setCompletedAt(attempt.getEndTime());
        resultRepository.save(result);

        // Update User Progress & Streak
        updateUserProgress(userId, attempt, totalScore, questions.size(), correctCount);

        // Check & Award Gamification Achievements
        achievementService.evaluateAndAwardAchievements(userId, attempt);

        return buildResultDto(attempt, result, maxPossibleMarks, questions, userAnswersToSave);
    }

    private void updateUserProgress(Long userId, QuizAttempt attempt, int score, int totalQ, int correctQ) {
        UserProgress progress = userProgressRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User u = userRepository.findById(userId).orElseThrow();
                    return new UserProgress(u);
                });

        progress.setTotalQuizzesAttempted(progress.getTotalQuizzesAttempted() + 1);
        progress.setTotalQuizzesCompleted(progress.getTotalQuizzesCompleted() + 1);
        progress.setTotalQuestionsAnswered(progress.getTotalQuestionsAnswered() + totalQ);
        progress.setTotalCorrectAnswers(progress.getTotalCorrectAnswers() + correctQ);
        progress.setTotalPoints(progress.getTotalPoints() + score);

        // Recalculate average score
        Double avg = quizAttemptRepository.getAverageScoreByUserId(userId);
        progress.setAverageScore(avg != null ? Math.round(avg * 10.0) / 10.0 : attempt.getPercentage());

        // Highest score
        Double highest = quizAttemptRepository.getHighestScoreByUserId(userId);
        progress.setHighestScore(highest != null ? Math.round(highest * 10.0) / 10.0 : attempt.getPercentage());

        // Streak calculation
        LocalDate today = LocalDate.now();
        LocalDate lastDate = progress.getLastQuizDate();
        if (lastDate == null) {
            progress.setCurrentStreak(1);
        } else if (lastDate.equals(today.minusDays(1))) {
            progress.setCurrentStreak(progress.getCurrentStreak() + 1);
        } else if (!lastDate.equals(today)) {
            progress.setCurrentStreak(1);
        }
        progress.setLastQuizDate(today);

        userProgressRepository.save(progress);
    }

    public QuizResultDto getAttemptResult(Long userId, Long attemptId) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + attemptId));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied for this attempt result!");
        }

        Result result = resultRepository.findByAttemptId(attemptId)
                .orElseThrow(() -> new RuntimeException("Result not generated yet for attempt: " + attemptId));

        List<Question> questions = questionRepository.findByQuizIdOrderBySortOrderAsc(attempt.getQuiz().getId());
        List<UserAnswer> answers = userAnswerRepository.findByAttemptId(attemptId);

        int maxMarks = questions.stream().mapToInt(q -> q.getMarks() != null ? q.getMarks() : 5).sum();

        return buildResultDto(attempt, result, maxMarks, questions, answers);
    }

    public List<QuestionReviewDto> getAttemptReview(Long userId, Long attemptId) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + attemptId));

        if (!attempt.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied for this review!");
        }

        List<Question> questions = questionRepository.findByQuizIdOrderBySortOrderAsc(attempt.getQuiz().getId());
        List<UserAnswer> answers = userAnswerRepository.findByAttemptId(attemptId);
        Map<Long, UserAnswer> answerMap = answers.stream().collect(Collectors.toMap(ua -> ua.getQuestion().getId(), ua -> ua, (a, b) -> a));

        List<QuestionReviewDto> reviewList = new ArrayList<>();
        for (Question q : questions) {
            UserAnswer ua = answerMap.get(q.getId());
            Set<Long> selectedIds = new HashSet<>();
            if (ua != null && ua.getSelectedOptionIdsJson() != null) {
                try {
                    List<Long> parsed = objectMapper.readValue(ua.getSelectedOptionIdsJson(), new TypeReference<List<Long>>() {});
                    selectedIds.addAll(parsed);
                } catch (Exception ignored) {}
            }

            QuestionReviewDto qDto = new QuestionReviewDto();
            qDto.setQuestionId(q.getId());
            qDto.setQuestionText(q.getQuestionText());
            qDto.setQuestionType(q.getQuestionType());
            qDto.setTopic(q.getTopic());
            qDto.setMaxMarks(q.getMarks() != null ? q.getMarks() : 5);
            qDto.setMarksAwarded(ua != null ? ua.getMarksAwarded() : 0);
            qDto.setIsCorrect(ua != null && Boolean.TRUE.equals(ua.getIsCorrect()));
            qDto.setExplanation(q.getExplanation());

            List<OptionReviewDto> optReviews = q.getOptions().stream().map(opt -> {
                boolean isSel = selectedIds.contains(opt.getId());
                boolean isCor = Boolean.TRUE.equals(opt.getIsCorrect());
                return new OptionReviewDto(opt.getId(), opt.getOptionText(), isCor, isSel);
            }).collect(Collectors.toList());

            qDto.setOptions(optReviews);
            reviewList.add(qDto);
        }

        return reviewList;
    }

    private QuizResultDto buildResultDto(QuizAttempt attempt, Result result, int maxMarks,
                                         List<Question> questions, List<UserAnswer> answers) {
        QuizResultDto dto = new QuizResultDto();
        dto.setAttemptId(attempt.getId());
        dto.setQuizId(attempt.getQuiz().getId());
        dto.setQuizTitle(attempt.getQuiz().getTitle());
        dto.setCategoryName(attempt.getQuiz().getCategory().getName());
        dto.setTotalQuestions(attempt.getTotalQuestions());
        dto.setCorrectAnswers(attempt.getCorrectAnswers());
        dto.setIncorrectAnswers(attempt.getIncorrectAnswers());
        dto.setUnansweredCount(attempt.getUnansweredCount());
        dto.setScore(attempt.getScore());
        dto.setTotalMarks(maxMarks);
        dto.setPercentage(attempt.getPercentage());
        dto.setTimeTakenSeconds(attempt.getTimeTakenSeconds());
        dto.setAccuracy(result.getAccuracy());
        dto.setPassed(attempt.getPassed());
        dto.setPerformanceLevel(attempt.getPerformanceLevel());
        dto.setCompletedAt(result.getCompletedAt());

        return dto;
    }
}
