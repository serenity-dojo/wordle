package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.GameHistoryDTO;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationContext;
import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
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
@Tag(name = "Game History Controller", description = "End points used to report on game history")
@CrossOrigin(origins = {"http://127.0.0.1:5173", "http://localhost:5173"})
public class GameHistoryController {

    private final GameHistoryService gameHistoryService;
    private final PlayerService playerService;

    @Autowired
    public GameHistoryController(GameHistoryService gameHistoryService, PlayerService playerService) {
        this.gameHistoryService = gameHistoryService;
        this.playerService = playerService;
    }

    @PreAuthorize("hasRole('PLAYER')")
    @RequestMapping(value = "/api/game/history", method = GET)
    @Operation(description = "Get the game history for the current player")
    public List<GameHistoryDTO> playerHistory() {
        if (AuthenticationContext.getCurrentUser() != null) {
            String username = AuthenticationContext.getCurrentUser().getUsername();
            if (playerService.findPlayerByUsername(username).isPresent()) {
                Player player = playerService.findPlayerByUsername(username).get();
                return gameHistoryService.findGameHistory(player).stream()
                        .map(historyEntry -> new GameHistoryDTO(historyEntry.getDateTimePlayed(), historyEntry.getWon(), historyEntry.getNumberOfGuesses()))
                        .toList();
            }
        }
        throw new PlayerNotFoundException("No player found for the current session");
    }

    @PreAuthorize("hasRole('PLAYER')")
    @RequestMapping(value = "/api/game/statistics", method = GET)
    @Operation(description = "Get the game statistics for the current player")
    public GameHistoryStatistics playerStatistics() {
        if (AuthenticationContext.getCurrentUser() != null) {
            String username = AuthenticationContext.getCurrentUser().getUsername();
            if (playerService.findPlayerByUsername(username).isPresent()) {
                Player player = playerService.findPlayerByUsername(username).get();
                return gameHistoryService.calculateStatistics(player);
            }
        }
        throw new PlayerNotFoundException("No player found for the current session");
    }
}
