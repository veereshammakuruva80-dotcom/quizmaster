package com.quizmaster.controller;

import com.quizmaster.entity.Category;
import com.quizmaster.entity.Question;
import com.quizmaster.entity.Quiz;
import com.quizmaster.entity.QuizAttempt;
import com.quizmaster.payload.request.CategoryRequest;
import com.quizmaster.payload.request.QuestionRequest;
import com.quizmaster.payload.request.QuizRequest;
import com.quizmaster.payload.response.MessageResponse;
import com.quizmaster.payload.response.QuizDto;
import com.quizmaster.payload.response.UserProfileDto;
import com.quizmaster.repository.*;
import com.quizmaster.service.CategoryService;
import com.quizmaster.service.QuestionService;
import com.quizmaster.service.QuizService;
import com.quizmaster.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAdminDashboard() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userRepository.count());
        data.put("totalQuizzes", quizRepository.count());
        data.put("totalQuestions", questionRepository.count());
        data.put("totalCategories", categoryRepository.count());

        Long completed = quizAttemptRepository.countCompletedAttempts();
        data.put("totalAttempts", completed != null ? completed : 0);

        Double avgScore = quizAttemptRepository.getGlobalAverageScore();
        data.put("averagePlatformScore", avgScore != null ? Math.round(avgScore * 10.0) / 10.0 : 0.0);

        return ResponseEntity.ok(data);
    }

    // --- User Management ---
    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<UserProfileDto> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserStatus(id));
    }

    @PutMapping("/users/{id}/toggle-admin")
    public ResponseEntity<UserProfileDto> toggleAdminRole(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleAdminRole(id));
    }

    // --- Category Management ---
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }

    // --- Quiz Management ---
    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizDto>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzesAdmin());
    }

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(quizService.createQuiz(request));
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(quizService.updateQuiz(id, request));
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<MessageResponse> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(new MessageResponse("Quiz deleted successfully"));
    }

    // --- Question Management ---
    @GetMapping("/questions/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuiz(quizId));
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.createQuestion(request));
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return ResponseEntity.ok(questionService.updateQuestion(id, request));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok(new MessageResponse("Question deleted successfully"));
    }

    // --- Attempts & Platform Reports ---
    @GetMapping("/attempts")
    public ResponseEntity<List<QuizAttempt>> getAllAttempts() {
        return ResponseEntity.ok(quizAttemptRepository.findAll());
    }
}
