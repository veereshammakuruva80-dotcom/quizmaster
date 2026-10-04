package com.quizmaster.payload.request;

import java.util.ArrayList;
import java.util.List;

public class AnswerSubmissionDto {
    private Long questionId;
    private List<Long> selectedOptionIds = new ArrayList<>();
    private Integer timeSpentSeconds = 0;

    public AnswerSubmissionDto() {}

    public AnswerSubmissionDto(Long questionId, List<Long> selectedOptionIds) {
        this.questionId = questionId;
        this.selectedOptionIds = selectedOptionIds;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public List<Long> getSelectedOptionIds() { return selectedOptionIds; }
    public void setSelectedOptionIds(List<Long> selectedOptionIds) { this.selectedOptionIds = selectedOptionIds; }

    public Integer getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(Integer timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
}
