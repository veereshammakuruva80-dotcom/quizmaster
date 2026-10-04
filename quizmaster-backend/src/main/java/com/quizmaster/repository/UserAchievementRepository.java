package com.quizmaster.repository;

import com.quizmaster.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserIdOrderByEarnedAtDesc(Long userId);
    Boolean existsByUserIdAndAchievementId(Long userId, Long achievementId);
}
