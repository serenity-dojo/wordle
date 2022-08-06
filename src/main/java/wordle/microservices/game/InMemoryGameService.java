package wordle.microservices.game;

import org.springframework.stereotype.Service;
import wordle.WordleGame;
import wordle.dictionary.WordleDictionary;
import wordle.model.CellColor;
import wordle.model.GameResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryGameService implements GameService {

    private Map<Long, WordleGame> games = new HashMap<>();
    private WordleDictionary dictionary = new WordleDictionary();
    private AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Long newGame() {
        long id = idCounter.getAndIncrement();
        games.put(id, WordleGame.withRandomWord(dictionary));
        return id;
    }

    @Override
    public GameResult play(long gameId, String word) {
        return gameWithId(gameId).play(word);
    }

    @Override
    public List<List<CellColor>> getHistory(long gameId) {
        return gameWithId(gameId).getRenderedCells();
    }

    @Override
    public GameResult getResult(long gameId) {
        return gameWithId(gameId).getResult();
    }

    @Override
    public String revealAnswer(long gameId) {
        return gameWithId(gameId).revealAnswer();
    }

    private WordleGame gameWithId(long id) {
        if (!games.containsKey(id)) {
            throw new NoSuchGameException("No game found with id " + id);
        }
        return games.get(id);
    }
}
