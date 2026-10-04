package com.quizmaster.payload.response;

import java.util.ArrayList;
import java.util.List;

public class QuestionReviewDto {
    private Long questionId;
    private String questionText;
    private String questionType;
    private String topic;
    private Integer marksAwarded;
    private Integer maxMarks;
    private Boolean isCorrect;
    private String explanation;
    private List<OptionReviewDto> options = new ArrayList<>();

    public QuestionReviewDto() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Integer getMarksAwarded() { return marksAwarded; }
    public void setMarksAwarded(Integer marksAwarded) { this.marksAwarded = marksAwarded; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public List<OptionReviewDto> getOptions() { return options; }
    public void setOptions(List<OptionReviewDto> options) { this.options = options; }
}
