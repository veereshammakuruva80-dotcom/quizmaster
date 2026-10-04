package com.quizmaster.payload.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardStatsDto {
    private Integer totalQuizzesAttempted = 0;
    private Integer totalQuizzesCompleted = 0;
    private Integer totalQuestionsAnswered = 0;
    private Double averageScore = 0.0;
    private Double highestScore = 0.0;
    private Integer currentStreak = 0;
    private Integer totalPoints = 0;

    private List<Map<String, Object>> strongSubjects = new ArrayList<>();
    private List<Map<String, Object>> weakSubjects = new ArrayList<>();
    private List<Map<String, Object>> recentAttempts = new ArrayList<>();
    private List<Map<String, Object>> recommendedQuizzes = new ArrayList<>();

    public DashboardStatsDto() {}

    public Integer getTotalQuizzesAttempted() { return totalQuizzesAttempted; }
    public void setTotalQuizzesAttempted(Integer totalQuizzesAttempted) { this.totalQuizzesAttempted = totalQuizzesAttempted; }

    public Integer getTotalQuizzesCompleted() { return totalQuizzesCompleted; }
    public void setTotalQuizzesCompleted(Integer totalQuizzesCompleted) { this.totalQuizzesCompleted = totalQuizzesCompleted; }

    public Integer getTotalQuestionsAnswered() { return totalQuestionsAnswered; }
    public void setTotalQuestionsAnswered(Integer totalQuestionsAnswered) { this.totalQuestionsAnswered = totalQuestionsAnswered; }

    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }

    public Double getHighestScore() { return highestScore; }
    public void setHighestScore(Double highestScore) { this.highestScore = highestScore; }

    public Integer getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(Integer currentStreak) { this.currentStreak = currentStreak; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }

    public List<Map<String, Object>> getStrongSubjects() { return strongSubjects; }
    public void setStrongSubjects(List<Map<String, Object>> strongSubjects) { this.strongSubjects = strongSubjects; }

    public List<Map<String, Object>> getWeakSubjects() { return weakSubjects; }
    public void setWeakSubjects(List<Map<String, Object>> weakSubjects) { this.weakSubjects = weakSubjects; }

    public List<Map<String, Object>> getRecentAttempts() { return recentAttempts; }
    public void setRecentAttempts(List<Map<String, Object>> recentAttempts) { this.recentAttempts = recentAttempts; }

    public List<Map<String, Object>> getRecommendedQuizzes() { return recommendedQuizzes; }
    public void setRecommendedQuizzes(List<Map<String, Object>> recommendedQuizzes) { this.recommendedQuizzes = recommendedQuizzes; }
}
