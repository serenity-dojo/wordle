package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationContext;
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

    PlayerService playerService;

    @Autowired
    public InMemoryGameService(GameHistoryService gameHistoryService, PlayerService playerService) {
        this.gameHistoryService = gameHistoryService;
        this.playerService = playerService;
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
        GameResult result = gameWithId(gameId).play(word);
        if (userIsAuthenticated()) {
            playerService.findPlayerByUsername(AuthenticationContext.getCurrentUser().getUsername()).ifPresent(
                    player -> {
                        if (result != GameResult.IN_PROGRESS) {
                            gameHistoryService.recordGameHistory(player, games.get(gameId));
                        }
                    }
            );
        }
        return result;
    }

    private boolean userIsAuthenticated() {
        return AuthenticationContext.getCurrentUser() != null && AuthenticationContext.getCurrentUser().getUsername() != null;
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
