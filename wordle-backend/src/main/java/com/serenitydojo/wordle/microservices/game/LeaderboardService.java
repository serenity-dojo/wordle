package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.PlayerScore;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LeaderboardService {
    /**
     * Return the list of players and their scores from highest to lowest
     */
    List<PlayerScore> getLeaderboard();
}
