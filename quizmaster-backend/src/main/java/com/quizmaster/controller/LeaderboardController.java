package com.quizmaster.controller;

import com.quizmaster.payload.response.LeaderboardEntryDto;
import com.quizmaster.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboard(
            @RequestParam(required = false, defaultValue = "all_time") String period,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(period, categoryId));
    }
}
