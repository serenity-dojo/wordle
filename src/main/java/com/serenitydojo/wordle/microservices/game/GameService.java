package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GameService {
    /**
     * Create a new game
     */
    Long newGame();

    /**
     * Create a new game with a specified word
     */
    Long newGame(String initialWord);

    /**
     * Make a move
     */
    GameResult play(long gameId, String word);

    /**
     * Get the game history
     */
    List<List<CellColor>> getHistory(long gameId);
    /**
     * Get the results
     */
    GameResult getResult(long gameId);

    /**
     * Reveal the answer
     */
    String revealAnswer(long gameId);
}
