package com.quizmaster.service;

import com.quizmaster.entity.*;
import com.quizmaster.payload.response.AchievementDto;
import com.quizmaster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    public List<AchievementDto> getUserAchievements(Long userId) {
        List<Achievement> allAchievements = achievementRepository.findAll();
        List<UserAchievement> earnedList = userAchievementRepository.findByUserIdOrderByEarnedAtDesc(userId);

        Set<Long> earnedIds = earnedList.stream()
                .map(ua -> ua.getAchievement().getId())
                .collect(Collectors.toSet());

        List<AchievementDto> dtos = new ArrayList<>();
        for (Achievement ach : allAchievements) {
            AchievementDto dto = new AchievementDto();
            dto.setId(ach.getId());
            dto.setCode(ach.getCode());
            dto.setTitle(ach.getTitle());
            dto.setDescription(ach.getDescription());
            dto.setIcon(ach.getIcon());
            dto.setBadgeColor(ach.getBadgeColor());
            dto.setCriteriaType(ach.getCriteriaType());
            dto.setCriteriaThreshold(ach.getCriteriaThreshold());

            boolean unlocked = earnedIds.contains(ach.getId());
            dto.setIsUnlocked(unlocked);
            if (unlocked) {
                earnedList.stream()
                        .filter(ua -> ua.getAchievement().getId().equals(ach.getId()))
                        .findFirst()
                        .ifPresent(ua -> dto.setEarnedAt(ua.getEarnedAt()));
            }
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    public List<Achievement> evaluateAndAwardAchievements(Long userId, QuizAttempt attempt) {
        List<Achievement> newlyAwarded = new ArrayList<>();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return newlyAwarded;

        UserProgress progress = userProgressRepository.findByUserId(userId).orElse(null);
        int totalCompleted = progress != null ? progress.getTotalQuizzesCompleted() : 1;
        int streak = progress != null ? progress.getCurrentStreak() : 1;
        double pct = attempt.getPercentage() != null ? attempt.getPercentage() : 0.0;

        // 1. FIRST_QUIZ
        if (totalCompleted >= 1) {
            awardIfEligible(user, "FIRST_QUIZ", newlyAwarded);
        }

        // 2. TEN_COMPLETED
        if (totalCompleted >= 10) {
            awardIfEligible(user, "TEN_COMPLETED", newlyAwarded);
        }

        // 3. QUIZ_MASTER
        if (totalCompleted >= 25) {
            awardIfEligible(user, "QUIZ_MASTER", newlyAwarded);
        }

        // 4. HIGH_ACCURACY
        if (pct >= 90.0) {
            awardIfEligible(user, "HIGH_ACCURACY", newlyAwarded);
        }

        // 5. PERFECT_SCORE
        if (pct >= 100.0) {
            awardIfEligible(user, "PERFECT_SCORE", newlyAwarded);
        }

        // 6. SEVEN_DAY_STREAK
        if (streak >= 7) {
            awardIfEligible(user, "SEVEN_DAY_STREAK", newlyAwarded);
        }

        // 7. Category specific badges
        String categoryName = attempt.getQuiz().getCategory().getName().toLowerCase();
        if ((categoryName.contains("java") || categoryName.contains("python") || categoryName.contains("programming") || categoryName.contains("c/c++")) && pct >= 80.0) {
            awardIfEligible(user, "PROGRAMMING_EXPERT", newlyAwarded);
        }

        if (categoryName.contains("database") && pct >= 80.0) {
            awardIfEligible(user, "DATABASE_EXPERT", newlyAwarded);
        }

        return newlyAwarded;
    }

    private void awardIfEligible(User user, String code, List<Achievement> newlyAwarded) {
        achievementRepository.findByCode(code).ifPresent(achievement -> {
            if (!userAchievementRepository.existsByUserIdAndAchievementId(user.getId(), achievement.getId())) {
                UserAchievement ua = new UserAchievement(user, achievement);
                ua.setEarnedAt(LocalDateTime.now());
                userAchievementRepository.save(ua);
                newlyAwarded.add(achievement);
            }
        });
    }
}
