package com.quizmaster.payload.response;

import java.util.ArrayList;
import java.util.List;

public class QuizQuestionDto {
    private Long id;
    private String questionText;
    private String questionType;
    private String difficulty;
    private String topic;
    private Integer marks;
    private Integer sortOrder;
    private List<OptionPublicDto> options = new ArrayList<>();

    public QuizQuestionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public List<OptionPublicDto> getOptions() { return options; }
    public void setOptions(List<OptionPublicDto> options) { this.options = options; }
}
