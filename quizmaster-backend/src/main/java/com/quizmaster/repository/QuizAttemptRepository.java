package com.quizmaster.repository;

import com.quizmaster.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserIdOrderByStartTimeDesc(Long userId);
    List<QuizAttempt> findTop5ByUserIdOrderByStartTimeDesc(Long userId);
    List<QuizAttempt> findByUserIdAndStatus(Long userId, String status);
    Long countByUserId(Long userId);

    @Query("SELECT AVG(a.percentage) FROM QuizAttempt a WHERE a.user.id = :userId AND a.status = 'COMPLETED'")
    Double getAverageScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT MAX(a.percentage) FROM QuizAttempt a WHERE a.user.id = :userId AND a.status = 'COMPLETED'")
    Double getHighestScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(a) FROM QuizAttempt a WHERE a.status = 'COMPLETED'")
    Long countCompletedAttempts();

    @Query("SELECT AVG(a.percentage) FROM QuizAttempt a WHERE a.status = 'COMPLETED'")
    Double getGlobalAverageScore();
}
