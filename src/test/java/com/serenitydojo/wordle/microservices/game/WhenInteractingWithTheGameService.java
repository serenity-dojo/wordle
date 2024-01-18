package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import com.serenitydojo.wordle.model.IllegalAttemptToShowAnswerException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class WhenInteractingWithTheGameService {
    private InMemoryGameService gameService;

    @Mock
    private GameHistoryService gameHistoryService;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        gameService = new InMemoryGameService(gameHistoryService, playerService);
    }

    @Test
    @DisplayName("When creating a new game, it should provide a unique id")
    void shouldProvideUniqueGameIdOnNewGame() {
        Long gameId1 = gameService.newGame();
        Long gameId2 = gameService.newGame();

        assertThat(gameId1).isNotNull();
        assertThat(gameId2).isNotNull();
        assertThat(gameId1).isNotEqualTo(gameId2);
    }


    @Test
    @DisplayName("When creating a game with a specified word, it should use that word for the game")
    void shouldUseSpecifiedWordOnNewGameWithWord() {
        String initialWord = "buddy";

        Long gameId = gameService.newGame(initialWord);
        GameResult result = gameService.play(gameId, initialWord);
        assertThat(result).isEqualTo(GameResult.WIN);
    }


    @Test
    @DisplayName("When creating a game with a specified word, it should use that word for the game")
    void shouldGetTheCurrentGameResult() {
        String initialWord = "buddy";

        Long gameId = gameService.newGame(initialWord);

        gameService.play(gameId, initialWord);

        assertThat(gameService.getResult(gameId)).isEqualTo(GameResult.WIN);
    }


    @Test
    @DisplayName("When fetching game history, it should retrieve history from the game instance")
    void shouldRetrieveGameHistoryFromGameInstance() {
        String initialWord = "CHAIN";

        Long gameId = gameService.newGame(initialWord);
        gameService.play(gameId, "CHURN");

        var playHistory = gameService.getHistory(1);
        assertThat(playHistory).hasSize(1);
        assertThat(playHistory.get(0)).containsExactly(CellColor.GREEN, CellColor.GREEN, CellColor.GRAY, CellColor.GRAY, CellColor.GREEN);

    }

    @Test
    @DisplayName("When revealing the answer, it should retrieve the answer from the game instance")
    void shouldRevealAnswerFromGameInstance() {
        String initialWord = "CHAIN";

        Long gameId = gameService.newGame(initialWord);

        gameService.play(gameId, "CHAIN");

        String answer = gameService.revealAnswer(gameId);

        assertThat(answer).isEqualTo(initialWord);
    }


    @Test
    @DisplayName("When revealing the answer, it should not reveal the answer before the end of the game")
    void shouldNotRevealAnswerFromGameInstanceBeforeTheEndOfTheName() {
        String initialWord = "CHAIN";

        Long gameId = gameService.newGame(initialWord);

        Assertions.assertThrows(IllegalAttemptToShowAnswerException.class, () -> gameService.revealAnswer(gameId));
    }

    @Test
    @DisplayName("When requesting a hint, it should delegate the request to the game instance")
    void shouldDelegateHintRequestToGameInstance() {
        Long gameId = gameService.newGame();

        List<String> hints = gameService.getHint(gameId);

        assertThat(hints).isNotEmpty();
    }

    @Test
    @DisplayName("When fetching an invalid game, it should throw a NoSuchGameException")
    void shouldThrowExceptionOnInvalidGame() {
        Assertions.assertThrows(NoSuchGameException.class, () -> gameService.play(99, "WRONG"));
    }
}
