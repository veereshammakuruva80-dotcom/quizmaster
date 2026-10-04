package com.quizmaster.controller;

import com.quizmaster.payload.response.AnalyticsDto;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/performance")
    public ResponseEntity<AnalyticsDto> getPerformanceAnalytics(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(analyticsService.getUserAnalytics(userDetails.getId()));
    }
}
