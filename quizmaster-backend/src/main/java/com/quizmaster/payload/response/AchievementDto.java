package com.quizmaster.payload.response;

import java.time.LocalDateTime;

public class AchievementDto {
    private Long id;
    private String code;
    private String title;
    private String description;
    private String icon;
    private String badgeColor;
    private String criteriaType;
    private Integer criteriaThreshold;
    private Boolean isUnlocked = false;
    private LocalDateTime earnedAt;

    public AchievementDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getBadgeColor() { return badgeColor; }
    public void setBadgeColor(String badgeColor) { this.badgeColor = badgeColor; }

    public String getCriteriaType() { return criteriaType; }
    public void setCriteriaType(String criteriaType) { this.criteriaType = criteriaType; }

    public Integer getCriteriaThreshold() { return criteriaThreshold; }
    public void setCriteriaThreshold(Integer criteriaThreshold) { this.criteriaThreshold = criteriaThreshold; }

    public Boolean getIsUnlocked() { return isUnlocked; }
    public void setIsUnlocked(Boolean isUnlocked) { this.isUnlocked = isUnlocked; }

    public LocalDateTime getEarnedAt() { return earnedAt; }
    public void setEarnedAt(LocalDateTime earnedAt) { this.earnedAt = earnedAt; }
}
