package com.quizmaster.controller;

import com.quizmaster.payload.response.AchievementDto;
import com.quizmaster.security.UserDetailsImpl;
import com.quizmaster.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping("/my")
    public ResponseEntity<List<AchievementDto>> getMyAchievements(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(achievementService.getUserAchievements(userDetails.getId()));
    }
}
