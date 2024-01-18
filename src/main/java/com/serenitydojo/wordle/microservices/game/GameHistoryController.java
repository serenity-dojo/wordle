package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.GameHistory;
import com.serenitydojo.wordle.microservices.domain.GameHistoryDTO;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationContext;
import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
                        .map(historyEntry -> new GameHistoryDTO(historyEntry.getDateTimePlayed(), historyEntry.getOutcome(), historyEntry.getNumberOfGuesses()))
                        .toList();
            }
        }
        return new ArrayList<>();
    }
}
