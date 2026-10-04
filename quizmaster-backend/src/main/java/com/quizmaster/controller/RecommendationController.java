package com.quizmaster.controller;

import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyRecommendations(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userDetails.getId()));
    }
}
