package wordle.microservice.game;

import org.springframework.stereotype.Service;
import wordle.model.CellColor;
import wordle.model.GameResult;

import java.util.List;

@Service
public interface GameService {
    /**
     * Create a new game
     */
    Long newGame();

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
