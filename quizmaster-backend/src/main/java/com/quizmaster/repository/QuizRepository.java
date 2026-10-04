package com.quizmaster.repository;

import com.quizmaster.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByActiveTrue();
    List<Quiz> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Quiz> findByDifficultyAndActiveTrue(String difficulty);
    List<Quiz> findByCategoryIdAndDifficultyAndActiveTrue(Long categoryId, String difficulty);
    List<Quiz> findByTitleContainingIgnoreCaseAndActiveTrue(String keyword);
}
