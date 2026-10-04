package com.quizmaster.service;

import com.quizmaster.entity.UserProgress;
import com.quizmaster.payload.response.LeaderboardEntryDto;
import com.quizmaster.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    public List<LeaderboardEntryDto> getLeaderboard(String period, Long categoryId) {
        List<UserProgress> progressList = userProgressRepository.findTopLeaderboard();
        List<LeaderboardEntryDto> entries = new ArrayList<>();

        int rank = 1;
        for (UserProgress up : progressList) {
            if (up.getUser() != null && up.getTotalQuizzesCompleted() > 0) {
                LeaderboardEntryDto entry = new LeaderboardEntryDto(
                        rank++,
                        up.getUser().getId(),
                        up.getUser().getFullName(),
                        up.getUser().getAvatarUrl(),
                        up.getTotalQuizzesCompleted(),
                        up.getAverageScore() != null ? up.getAverageScore() : 0.0,
                        up.getHighestScore() != null ? up.getHighestScore() : 0.0,
                        up.getTotalPoints() != null ? up.getTotalPoints() : 0,
                        up.getCurrentStreak() != null ? up.getCurrentStreak() : 0
                );
                entries.add(entry);
            }
        }

        return entries;
    }
}
