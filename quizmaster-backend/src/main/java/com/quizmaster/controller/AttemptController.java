package com.quizmaster.controller;

import com.quizmaster.entity.QuizAttempt;
import com.quizmaster.payload.response.QuestionReviewDto;
import com.quizmaster.payload.response.QuizResultDto;
import com.quizmaster.repository.QuizAttemptRepository;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.PdfReportService;
import com.quizmaster.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    @Autowired
    private QuizAttemptService quizAttemptService;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping("/my")
    public ResponseEntity<List<QuizAttempt>> getMyAttempts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(quizAttemptRepository.findByUserIdOrderByStartTimeDesc(userDetails.getId()));
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<QuizResultDto> getAttemptResult(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(quizAttemptService.getAttemptResult(userDetails.getId(), id));
    }

    @GetMapping("/{id}/review")
    public ResponseEntity<List<QuestionReviewDto>> getAttemptReview(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(quizAttemptService.getAttemptReview(userDetails.getId(), id));
    }

    @GetMapping(value = "/{id}/report", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getAttemptReport(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        QuizResultDto result = quizAttemptService.getAttemptResult(userDetails.getId(), id);
        List<QuestionReviewDto> reviews = quizAttemptService.getAttemptReview(userDetails.getId(), id);
        String html = pdfReportService.generateHtmlReport(result, userDetails.getFullName(), userDetails.getUsername(), reviews);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"quiz-report-" + id + ".html\"")
                .body(html);
    }
}
