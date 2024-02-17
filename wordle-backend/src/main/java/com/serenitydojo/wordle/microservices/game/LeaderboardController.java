package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.PlayerScore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Tag(name = "Leaderboard Controller", description = "Provides the list of players ordered by score")
@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PreAuthorize("hasRole('PLAYER')")
    @RequestMapping(value = "/api/game/leaderboard", method = GET)
    @Operation(description = "Return the names and scores of players from highest to lowest")
    public List<PlayerScore> newGame() {
        return leaderboardService.getLeaderboard();
    }
}
