package com.quizmaster.payload.request;

import java.util.ArrayList;
import java.util.List;

public class SubmitQuizRequest {
    private Long attemptId;
    private Integer timeTakenSeconds;
    private List<AnswerSubmissionDto> answers = new ArrayList<>();

    public SubmitQuizRequest() {}

    public Long getAttemptId() { return attemptId; }
    public void setAttemptId(Long attemptId) { this.attemptId = attemptId; }

    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    public List<AnswerSubmissionDto> getAnswers() { return answers; }
    public void setAnswers(List<AnswerSubmissionDto> answers) { this.answers = answers; }
}
