package com.quizmaster.repository;

import com.quizmaster.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserId(Long userId);

    @Query("SELECT up FROM UserProgress up JOIN FETCH up.user u ORDER BY up.totalPoints DESC, up.averageScore DESC")
    List<UserProgress> findTopLeaderboard();
}
