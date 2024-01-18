package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.GameHistory;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.persistance.GameHistoryRepository;
import com.serenitydojo.wordle.model.GameResult;
import com.serenitydojo.wordle.model.WordleGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameHistoryService {

    @Autowired
    public GameHistoryService(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    GameHistoryRepository gameHistoryRepository;
    public void recordGameHistory(Player player, WordleGame wordleGame) {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setPlayer(player);
        gameHistory.setDateTimePlayed(LocalDateTime.now());
        gameHistory.setOutcome(wordleGame.getResult() == GameResult.WIN);
        gameHistory.setNumberOfGuesses(wordleGame.getRenderedCells().size());
        gameHistoryRepository.save(gameHistory);
    }

    public List<GameHistory> findGameHistory(Player player) {
        return gameHistoryRepository.findByPlayer(player);
    }
}
