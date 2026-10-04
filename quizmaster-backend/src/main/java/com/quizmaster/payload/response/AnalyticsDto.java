package com.quizmaster.payload.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalyticsDto {
    private List<Map<String, Object>> scoreHistory = new ArrayList<>();
    private List<Map<String, Object>> subjectPerformance = new ArrayList<>();
    private Map<String, Object> accuracyBreakdown;
    private Map<String, Object> completionStats;
    private List<Map<String, Object>> weeklyPerformance = new ArrayList<>();
    private List<Map<String, Object>> monthlyPerformance = new ArrayList<>();
    private List<Map<String, Object>> strongTopics = new ArrayList<>();
    private List<Map<String, Object>> weakTopics = new ArrayList<>();

    public AnalyticsDto() {}

    public List<Map<String, Object>> getScoreHistory() { return scoreHistory; }
    public void setScoreHistory(List<Map<String, Object>> scoreHistory) { this.scoreHistory = scoreHistory; }

    public List<Map<String, Object>> getSubjectPerformance() { return subjectPerformance; }
    public void setSubjectPerformance(List<Map<String, Object>> subjectPerformance) { this.subjectPerformance = subjectPerformance; }

    public Map<String, Object> getAccuracyBreakdown() { return accuracyBreakdown; }
    public void setAccuracyBreakdown(Map<String, Object> accuracyBreakdown) { this.accuracyBreakdown = accuracyBreakdown; }

    public Map<String, Object> getCompletionStats() { return completionStats; }
    public void setCompletionStats(Map<String, Object> completionStats) { this.completionStats = completionStats; }

    public List<Map<String, Object>> getWeeklyPerformance() { return weeklyPerformance; }
    public void setWeeklyPerformance(List<Map<String, Object>> weeklyPerformance) { this.weeklyPerformance = weeklyPerformance; }

    public List<Map<String, Object>> getMonthlyPerformance() { return monthlyPerformance; }
    public void setMonthlyPerformance(List<Map<String, Object>> monthlyPerformance) { this.monthlyPerformance = monthlyPerformance; }

    public List<Map<String, Object>> getStrongTopics() { return strongTopics; }
    public void setStrongTopics(List<Map<String, Object>> strongTopics) { this.strongTopics = strongTopics; }

    public List<Map<String, Object>> getWeakTopics() { return weakTopics; }
    public void setWeakTopics(List<Map<String, Object>> weakTopics) { this.weakTopics = weakTopics; }
}
