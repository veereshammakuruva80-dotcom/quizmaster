package com.quizmaster.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Integer totalQuizzesAttempted = 0;

    private Integer totalQuizzesCompleted = 0;

    private Integer totalQuestionsAnswered = 0;

    private Integer totalCorrectAnswers = 0;

    private Integer totalPoints = 0;

    private Double averageScore = 0.0;

    private Double highestScore = 0.0;

    private Integer currentStreak = 0;

    private LocalDate lastQuizDate;

    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserProgress() {}

    public UserProgress(User user) {
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getTotalQuizzesAttempted() { return totalQuizzesAttempted; }
    public void setTotalQuizzesAttempted(Integer totalQuizzesAttempted) { this.totalQuizzesAttempted = totalQuizzesAttempted; }

    public Integer getTotalQuizzesCompleted() { return totalQuizzesCompleted; }
    public void setTotalQuizzesCompleted(Integer totalQuizzesCompleted) { this.totalQuizzesCompleted = totalQuizzesCompleted; }

    public Integer getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
    public void setTotalQuestionsAnswered(Integer totalQuestionsAnswered) { this.totalQuestionsAnswered = totalQuestionsAnswered; }

    public Integer getTotalCorrectAnswers() { return totalCorrectAnswers; }
    public void setTotalCorrectAnswers(Integer totalCorrectAnswers) { this.totalCorrectAnswers = totalCorrectAnswers; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }

    public Double getHighestScore() { return highestScore; }
    public void setHighestScore(Double highestScore) { this.highestScore = highestScore; }

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public LocalDate getLastQuizDate() { return lastQuizDate; }
    public void setLastQuizDate(LocalDate lastQuizDate) { this.lastQuizDate = lastQuizDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
