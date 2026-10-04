package com.quizmaster.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuizRequest {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    private Long categoryId;
    private String difficulty = "INTERMEDIATE";
    private Integer timeLimitMinutes = 15;
    private Integer totalMarks = 50;
    private Integer passPercentage = 50;
    private Boolean active = true;

    public QuizRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Integer getTimeLimitMinutes() { return timeLimitMinutes; }
    public void setTimeLimitMinutes(Integer timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }

    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }

    public Integer getPassPercentage() { return passPercentage; }
    public void setPassPercentage(Integer passPercentage) { this.passPercentage = passPercentage; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
