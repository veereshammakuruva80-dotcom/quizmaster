package com.quizmaster.controller;

import com.quizmaster.entity.QuizAttempt;
import com.quizmaster.entity.UserProgress;
import com.quizmaster.payload.response.DashboardStatsDto;
import com.quizmaster.repository.QuizAttemptRepository;
import com.quizmaster.repository.UserProgressRepository;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        UserProgress progress = userProgressRepository.findByUserId(userId).orElse(new UserProgress());
        List<QuizAttempt> recentAttemptsList = quizAttemptRepository.findTop5ByUserIdOrderByStartTimeDesc(userId);

        DashboardStatsDto dto = new DashboardStatsDto();
        dto.setTotalQuizzesAttempted(progress.getTotalQuizzesAttempted());
        dto.setTotalQuizzesCompleted(progress.getTotalQuizzesCompleted());
        dto.setTotalQuestionsAnswered(progress.getTotalQuestionsAnswered());
        dto.setAverageScore(progress.getAverageScore() != null ? progress.getAverageScore() : 0.0);
        dto.setHighestScore(progress.getHighestScore() != null ? progress.getHighestScore() : 0.0);
        dto.setCurrentStreak(progress.getCurrentStreak() != null ? progress.getCurrentStreak() : 0);
        dto.setTotalPoints(progress.getTotalPoints() != null ? progress.getTotalPoints() : 0);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        List<Map<String, Object>> recentAttempts = new ArrayList<>();
        for (QuizAttempt att : recentAttemptsList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", att.getId());
            map.put("quizTitle", att.getQuiz().getTitle());
            map.put("category", att.getQuiz().getCategory().getName());
            map.put("score", att.getScore());
            map.put("percentage", att.getPercentage());
            map.put("passed", att.getPassed());
            map.put("status", att.getStatus());
            map.put("date", att.getEndTime() != null ? att.getEndTime().format(dtf) : "In Progress");
            recentAttempts.add(map);
        }
        dto.setRecentAttempts(recentAttempts);

        // Fetch AI recommendations and strengths/weaknesses
        try {
            Map<String, Object> recData = recommendationService.getUserRecommendations(userId);
            if (recData != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> strong = (List<Map<String, Object>>) recData.get("strongSubjects");
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> weak = (List<Map<String, Object>>) recData.get("weakSubjects");
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> recQuizzes = (List<Map<String, Object>>) recData.get("recommendedQuizzes");

                if (strong != null) dto.setStrongSubjects(strong);
                if (weak != null) dto.setWeakSubjects(weak);
                if (recQuizzes != null) dto.setRecommendedQuizzes(recQuizzes);
            }
        } catch (Exception e) {
            // graceful fallback
        }

        return ResponseEntity.ok(dto);
    }
}
