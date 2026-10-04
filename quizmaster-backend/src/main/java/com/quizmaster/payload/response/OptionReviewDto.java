package com.quizmaster.payload.response;

public class OptionReviewDto {
    private Long id;
    private String optionText;
    private Boolean isCorrect;
    private Boolean isSelected;

    public OptionReviewDto() {}

    public OptionReviewDto(Long id, String optionText, Boolean isCorrect, Boolean isSelected) {
        this.id = id;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.isSelected = isSelected;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOptionText() { return optionText; }
    public void setOptionText(String optionText) { this.optionText = optionText; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

    public Boolean getIsSelected() { return isSelected; }
    public void setIsSelected(Boolean isSelected) { this.isSelected = isSelected; }
}
