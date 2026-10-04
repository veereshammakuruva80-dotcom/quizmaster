package com.quizmaster.payload.response;

public class LeaderboardEntryDto {
    private Integer rank;
    private Long userId;
    private String fullName;
    private String avatarUrl;
    private Integer totalQuizzes;
    private Double averageScore;
    private Double highestScore;
    private Integer points;
    private Integer streak;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(Integer rank, Long userId, String fullName, String avatarUrl,
                               Integer totalQuizzes, Double averageScore, Double highestScore,
                               Integer points, Integer streak) {
        this.rank = rank;
        this.userId = userId;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.totalQuizzes = totalQuizzes;
        this.averageScore = averageScore;
        this.highestScore = highestScore;
        this.points = points;
        this.streak = streak;
    }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Integer getTotalQuizzes() { return totalQuizzes; }
    public void setTotalQuizzes(Integer totalQuizzes) { this.totalQuizzes = totalQuizzes; }

    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }

    public Double getHighestScore() { return highestScore; }
    public void setHighestScore(Double highestScore) { this.highestScore = highestScore; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public Integer getStreak() { return streak; }
    public void setStreak(Integer streak) { this.streak = streak; }
}
