package com.quizmaster.payload.request;

import java.util.ArrayList;
import java.util.List;

public class OptionDto {
    private Long id;
    private String optionText;
    private Boolean isCorrect = false;
    private Integer sortOrder = 1;

    public OptionDto() {}

    public OptionDto(String optionText, Boolean isCorrect, Integer sortOrder) {
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}
