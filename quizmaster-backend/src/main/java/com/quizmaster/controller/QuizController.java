package com.quizmaster.controller;

import com.quizmaster.payload.request.SubmitQuizRequest;
import com.quizmaster.payload.response.QuizDto;
import com.quizmaster.payload.response.QuizResultDto;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.QuizAttemptService;
import com.quizmaster.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizAttemptService quizAttemptService;

    @GetMapping
    public ResponseEntity<List<QuizDto>> getQuizzes(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(quizService.getQuizzes(categoryId, difficulty, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDto> getQuizDetail(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizDetail(id));
    }

    @GetMapping("/{id}/instructions")
    public ResponseEntity<QuizDto> getQuizInstructions(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.getQuizDetail(id));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> startQuizAttempt(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> attemptData = quizAttemptService.startQuizAttempt(userDetails.getId(), id);
        return ResponseEntity.ok(attemptData);
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResultDto> submitQuiz(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody SubmitQuizRequest request) {
        QuizResultDto result = quizAttemptService.submitQuizAttempt(userDetails.getId(), request);
        return ResponseEntity.ok(result);
    }
}
