package com.quizmaster.service;

import com.quizmaster.entity.QuizAttempt;
import com.quizmaster.entity.UserAnswer;
import com.quizmaster.payload.response.AnalyticsDto;
import com.quizmaster.repository.QuizAttemptRepository;
import com.quizmaster.repository.UserAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public AnalyticsDto getUserAnalytics(Long userId) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdOrderByStartTimeDesc(userId);
        List<QuizAttempt> completedAttempts = attempts.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        AnalyticsDto dto = new AnalyticsDto();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // 1. Score History (Chronological for chart)
        List<QuizAttempt> chronological = new ArrayList<>(completedAttempts);
        Collections.reverse(chronological);

        List<Map<String, Object>> scoreHistory = new ArrayList<>();
        for (QuizAttempt att : chronological) {
            Map<String, Object> item = new HashMap<>();
            item.put("attemptId", att.getId());
            item.put("quizTitle", att.getQuiz().getTitle());
            item.put("category", att.getQuiz().getCategory().getName());
            item.put("percentage", att.getPercentage());
            item.put("score", att.getScore());
            item.put("date", att.getEndTime() != null ? att.getEndTime().format(dtf) : "");
            scoreHistory.add(item);
        }
        dto.setScoreHistory(scoreHistory);

        // 2. Subject-wise performance
        Map<String, List<Double>> catPercentages = new HashMap<>();
        Map<String, Integer> catAttempts = new HashMap<>();
        for (QuizAttempt att : completedAttempts) {
            String cat = att.getQuiz().getCategory().getName();
            catPercentages.computeIfAbsent(cat, k -> new ArrayList<>()).add(att.getPercentage());
            catAttempts.put(cat, catAttempts.getOrDefault(cat, 0) + 1);
        }

        List<Map<String, Object>> subjectPerf = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : catPercentages.entrySet()) {
            Map<String, Object> sp = new HashMap<>();
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            sp.put("subject", entry.getKey());
            sp.put("averageScore", Math.round(avg * 10.0) / 10.0);
            sp.put("attemptsCount", catAttempts.get(entry.getKey()));
            subjectPerf.add(sp);
        }
        dto.setSubjectPerformance(subjectPerf);

        // 3. Accuracy breakdown
        int totalCorrect = completedAttempts.stream().mapToInt(QuizAttempt::getCorrectAnswers).sum();
        int totalIncorrect = completedAttempts.stream().mapToInt(QuizAttempt::getIncorrectAnswers).sum();
        int totalUnanswered = completedAttempts.stream().mapToInt(QuizAttempt::getUnansweredCount).sum();
        int totalQuestions = totalCorrect + totalIncorrect + totalUnanswered;

        Map<String, Object> accBreakdown = new HashMap<>();
        accBreakdown.put("totalQuestions", totalQuestions);
        accBreakdown.put("correctAnswers", totalCorrect);
        accBreakdown.put("incorrectAnswers", totalIncorrect);
        accBreakdown.put("unanswered", totalUnanswered);
        accBreakdown.put("overallAccuracy", totalQuestions > 0 ? Math.round((totalCorrect * 100.0 / totalQuestions) * 10.0) / 10.0 : 0.0);
        dto.setAccuracyBreakdown(accBreakdown);

        // 4. Completion stats
        long passedCount = completedAttempts.stream().filter(a -> Boolean.TRUE.equals(a.getPassed())).count();
        Map<String, Object> compStats = new HashMap<>();
        compStats.put("totalAttempted", attempts.size());
        compStats.put("completed", completedAttempts.size());
        compStats.put("passed", passedCount);
        compStats.put("passRate", completedAttempts.size() > 0 ? Math.round((passedCount * 100.0 / completedAttempts.size()) * 10.0) / 10.0 : 0.0);
        dto.setCompletionStats(compStats);

        // 5. Strong & Weak Topics from UserAnswers
        Map<String, int[]> topicCounts = new HashMap<>(); // topic -> [correct, incorrect]
        for (QuizAttempt att : completedAttempts) {
            List<UserAnswer> answers = userAnswerRepository.findByAttemptId(att.getId());
            for (UserAnswer ans : answers) {
                String topic = ans.getQuestion().getTopic();
                if (topic != null && !topic.isBlank()) {
                    topicCounts.computeIfAbsent(topic, k -> new int[2]);
                    if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                        topicCounts.get(topic)[0]++;
                    } else {
                        topicCounts.get(topic)[1]++;
                    }
                }
            }
        }

        List<Map<String, Object>> strongTopics = new ArrayList<>();
        List<Map<String, Object>> weakTopics = new ArrayList<>();

        for (Map.Entry<String, int[]> entry : topicCounts.entrySet()) {
            int cor = entry.getValue()[0];
            int inc = entry.getValue()[1];
            int tot = cor + inc;
            double acc = tot > 0 ? (cor * 100.0 / tot) : 0.0;

            Map<String, Object> tMap = new HashMap<>();
            tMap.put("topic", entry.getKey());
            tMap.put("totalAttempted", tot);
            tMap.put("accuracy", Math.round(acc * 10.0) / 10.0);

            if (acc >= 75.0 && tot >= 1) {
                strongTopics.add(tMap);
            } else if (acc < 65.0 || inc > cor) {
                weakTopics.add(tMap);
            }
        }

        dto.setStrongTopics(strongTopics);
        dto.setWeakTopics(weakTopics);

        return dto;
    }
}
