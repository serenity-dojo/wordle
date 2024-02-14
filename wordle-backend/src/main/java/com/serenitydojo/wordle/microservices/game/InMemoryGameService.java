package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationContext;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationService;
import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import com.serenitydojo.wordle.model.WordleGame;
import com.serenitydojo.wordle.dictionary.WordleDictionary;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryGameService implements GameService {

    private Map<Long, WordleGame> games = new HashMap<>();
    private WordleDictionary dictionary = new WordleDictionary();
    private AtomicLong idCounter = new AtomicLong(1);

    GameHistoryService gameHistoryService;
    AuthenticationService authenticationService;

    @Autowired
    public InMemoryGameService(GameHistoryService gameHistoryService,
                               AuthenticationService authenticationService) {
        this.gameHistoryService = gameHistoryService;
        this.authenticationService = authenticationService;
    }

    @Override
    public Long newGame() {
        long id = idCounter.getAndIncrement();
        games.put(id, WordleGame.withRandomWord(dictionary));
        return id;
    }

    @Override
    public Long newGame(String initialWord) {
        long id = idCounter.getAndIncrement();
        games.put(id, WordleGame.withSpecifiedWord(initialWord, dictionary));
        return id;
    }

    @Override
    public GameResult play(long gameId, String word) {
        WordleGame game = gameWithId(gameId);
        GameResult result = game.play(word);
        if (authenticationService.userIsAuthenticated() && (result != GameResult.IN_PROGRESS)) {
            Player player = authenticationService.getCurrentPlayer();
            gameHistoryService.recordGameHistory(player, game);
        }
        return result;
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

    @Override
    public List<String> getHint(long gameId) {
        return gameWithId(gameId).requestHint();
    }

    private WordleGame gameWithId(long id) {
        if (!games.containsKey(id)) {
            throw new NoSuchGameException("No game found with id " + id);
        }
        return games.get(id);
    }
}
