package com.quizmaster.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmaster.entity.*;
import com.quizmaster.payload.response.QuizDto;
import com.quizmaster.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    @Value("${quizmaster.ai.serviceUrl}")
    private String aiServiceUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private QuizService quizService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getUserRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<QuizAttempt> attempts = quizAttemptRepository.findByUserIdOrderByStartTimeDesc(userId);
        List<QuizDto> availableQuizzes = quizService.getQuizzes(null, null, null);

        // Prepare attempt history payload
        List<Map<String, Object>> attemptHistory = new ArrayList<>();
        for (QuizAttempt att : attempts) {
            if ("COMPLETED".equalsIgnoreCase(att.getStatus())) {
                Map<String, Object> attMap = new HashMap<>();
                attMap.put("quizId", att.getQuiz().getId());
                attMap.put("category", att.getQuiz().getCategory().getName());
                attMap.put("percentage", att.getPercentage());
                attMap.put("accuracy", (att.getTotalQuestions() > 0) ? (att.getCorrectAnswers() * 100.0 / att.getTotalQuestions()) : 0.0);
                attMap.put("timeTakenSeconds", att.getTimeTakenSeconds());
                attMap.put("difficulty", att.getQuiz().getDifficulty());

                List<UserAnswer> answers = userAnswerRepository.findByAttemptId(att.getId());
                List<String> correctTopics = new ArrayList<>();
                List<String> incorrectTopics = new ArrayList<>();

                for (UserAnswer ans : answers) {
                    String topic = ans.getQuestion().getTopic();
                    if (topic != null && !topic.isBlank()) {
                        if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                            correctTopics.add(topic);
                        } else {
                            incorrectTopics.add(topic);
                        }
                    }
                }

                attMap.put("correctTopics", correctTopics);
                attMap.put("incorrectTopics", incorrectTopics);
                attemptHistory.add(attMap);
            }
        }

        // Parse user interests & skills
        List<String> interests = new ArrayList<>();
        if (user.getInterests() != null && !user.getInterests().isBlank()) {
            interests.addAll(Arrays.asList(user.getInterests().split("[,;|]+")));
        }

        List<String> skills = new ArrayList<>();
        if (user.getSkills() != null && !user.getSkills().isBlank()) {
            skills.addAll(Arrays.asList(user.getSkills().split("[,;|]+")));
        }

        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("userId", userId);
        requestPayload.put("userInterests", interests);
        requestPayload.put("userSkills", skills);
        requestPayload.put("attemptHistory", attemptHistory);
        requestPayload.put("availableQuizzes", availableQuizzes);

        // Try calling Python AI service
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(aiServiceUrl, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> aiResult = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
                aiResult.put("aiEngineActive", true);
                return aiResult;
            }
        } catch (Exception e) {
            logger.warn("Python AI service not responding on {}. Using built-in adaptive recommendation fallback. Details: {}", aiServiceUrl, e.getMessage());
        }

        // Built-in intelligent fallback
        return generateFallbackRecommendations(user, attemptHistory, availableQuizzes);
    }

    private Map<String, Object> generateFallbackRecommendations(User user, List<Map<String, Object>> attemptHistory, List<QuizDto> availableQuizzes) {
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("aiEngineActive", false);

        // Calculate subject performance
        Map<String, List<Double>> subjectScores = new HashMap<>();
        Map<String, Integer> topicMistakes = new HashMap<>();

        for (Map<String, Object> att : attemptHistory) {
            String cat = (String) att.get("category");
            Double pct = (Double) att.get("percentage");
            subjectScores.computeIfAbsent(cat, k -> new ArrayList<>()).add(pct);

            @SuppressWarnings("unchecked")
            List<String> incTopics = (List<String>) att.get("incorrectTopics");
            if (incTopics != null) {
                for (String t : incTopics) {
                    topicMistakes.put(t, topicMistakes.getOrDefault(t, 0) + 1);
                }
            }
        }

        List<Map<String, Object>> strongSubjects = new ArrayList<>();
        List<Map<String, Object>> weakSubjects = new ArrayList<>();

        for (Map.Entry<String, List<Double>> entry : subjectScores.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            Map<String, Object> subMap = new HashMap<>();
            subMap.put("subject", entry.getKey());
            subMap.put("avgScore", Math.round(avg * 10.0) / 10.0);

            if (avg >= 70.0) {
                subMap.put("status", avg >= 85.0 ? "Mastered" : "Strong");
                strongSubjects.add(subMap);
            } else {
                subMap.put("status", avg < 50.0 ? "Critical" : "Needs Improvement");
                weakSubjects.add(subMap);
            }
        }

        // Topics to revise
        List<Map<String, Object>> topicsToRevise = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : topicMistakes.entrySet()) {
            Map<String, Object> topMap = new HashMap<>();
            topMap.put("topic", entry.getKey());
            topMap.put("incorrectCount", entry.getValue());
            topMap.put("tip", "Review core rules and syntax of " + entry.getKey() + " to improve problem-solving accuracy.");
            topicsToRevise.add(topMap);
        }

        // Recommend quizzes
        List<Map<String, Object>> recommendedQuizzes = new ArrayList<>();
        for (QuizDto q : availableQuizzes) {
            Map<String, Object> item = new HashMap<>();
            item.put("quiz", q);

            boolean isWeak = weakSubjects.stream().anyMatch(w -> w.get("subject").toString().equalsIgnoreCase(q.getCategoryName()));
            boolean isStrong = strongSubjects.stream().anyMatch(s -> s.get("subject").toString().equalsIgnoreCase(q.getCategoryName()));

            if (isWeak) {
                item.put("score", 95.0);
                item.put("recommendationType", "WEAK_TOPIC_REVISION");
                item.put("reason", "Tailored practice to improve your understanding of " + q.getCategoryName() + ".");
                recommendedQuizzes.add(item);
            } else if (isStrong && "ADVANCED".equalsIgnoreCase(q.getDifficulty())) {
                item.put("score", 88.0);
                item.put("recommendationType", "ADVANCED_CHALLENGE");
                item.put("reason", "Push your mastery further with advanced questions in " + q.getCategoryName() + ".");
                recommendedQuizzes.add(item);
            } else if (recommendedQuizzes.size() < 4) {
                item.put("score", 70.0);
                item.put("recommendationType", "GENERAL_RECOMMENDED");
                item.put("reason", "Recommended quiz to expand your fundamental knowledge.");
                recommendedQuizzes.add(item);
            }
        }

        result.put("strongSubjects", strongSubjects);
        result.put("weakSubjects", weakSubjects);
        result.put("topicsToRevise", topicsToRevise);
        result.put("recommendedQuizzes", recommendedQuizzes);
        result.put("easierPractice", recommendedQuizzes.stream().filter(r -> "BEGINNER".equalsIgnoreCase(((QuizDto) r.get("quiz")).getDifficulty())).collect(Collectors.toList()));
        result.put("advancedChallenges", recommendedQuizzes.stream().filter(r -> "ADVANCED".equalsIgnoreCase(((QuizDto) r.get("quiz")).getDifficulty())).collect(Collectors.toList()));

        List<Map<String, Object>> roadmap = new ArrayList<>();
        int step = 1;
        for (Map<String, Object> w : weakSubjects) {
            Map<String, Object> r = new HashMap<>();
            r.put("step", step++);
            r.put("title", "Review Foundational Concepts in " + w.get("subject"));
            r.put("description", "Current average score: " + w.get("avgScore") + "%. Revisit core textbook principles.");
            r.put("priority", "HIGH");
            roadmap.add(r);
        }
        Map<String, Object> rFinal = new HashMap<>();
        rFinal.put("step", step);
        rFinal.put("title", "Complete 1 Daily Practice Quiz");
        rFinal.put("description", "Maintain your learning streak and earn gamified badges.");
        rFinal.put("priority", "NORMAL");
        roadmap.add(rFinal);
        result.put("learningRoadmap", roadmap);

        return result;
    }
}
