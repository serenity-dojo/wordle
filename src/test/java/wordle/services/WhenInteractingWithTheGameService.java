package wordle.services;

import org.junit.Before;
import org.junit.Test;
import wordle.microservices.game.GameService;
import wordle.microservices.game.InMemoryGameService;
import wordle.microservices.game.NoSuchGameException;
import wordle.model.GameResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WhenInteractingWithTheGameService {

    GameService gameService;

    @Before
    public void newGameService() {
        gameService = new InMemoryGameService();
    }

    @Test
    public void shouldCreateANewIdForANewGame() {
        assertThat(gameService.newGame()).isNotZero();
    }

    @Test
    public void eachGameIdShouldBeDifferent() {
        assertThat(gameService.newGame()).isNotEqualTo(gameService.newGame());
    }

    @Test
    public void anExceptionShouldBeThrownIfTheIdDoesNotExist() {
        assertThatThrownBy(() -> gameService.getResult(999))
                .isInstanceOf(NoSuchGameException.class)
                .hasMessageContaining("No game found with id 999");
    }

    @Test
    public void makeAMove() {
        long id = gameService.newGame();
        gameService.play(id, "BEAST");
        assertThat(gameService.getHistory(id)).hasSize(1);
    }

    @Test
    public void makeAMoveAndSeeTheResult() {
        long id = gameService.newGame();
        GameResult result = gameService.play(id, "BEAST");
        assertThat(result).isNotNull();
    }
}

