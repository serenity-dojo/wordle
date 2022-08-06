package wordle.microservice;

import org.junit.jupiter.api.*;
import wordle.microservice.game.GameService;
import wordle.microservice.game.InMemoryGameService;
import wordle.microservice.game.NoSuchGameException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("When interacting with the Game service")
class WhenInteractingWithTheGameService {

    GameService gameService;

    @BeforeEach
    void newGameService() {
        gameService = new InMemoryGameService();
    }

    @Test
    void shouldCreateANewIdForANewGame() {
        assertThat(gameService.newGame()).isNotZero();
    }

    @Test
    void eachGameIdShouldBeDifferent() {
        assertThat(gameService.newGame()).isNotEqualTo(gameService.newGame());
    }

    @Test
    void anExceptionShouldBeThrownIfTheIdDoesNotExist() {
        NoSuchGameException thrown = Assertions.assertThrows(NoSuchGameException.class, () -> gameService.getResult(999));
        assertThat(thrown.getMessage()).isEqualTo("No game found with id 999");
    }

    @Nested
    @DisplayName("When making a move")
    class WhenMakingAMove {

        long id;
        @BeforeEach
        void newGame() {
            id = gameService.newGame();
        }

        @DisplayName("we need to provide the id of the game to make each move")
        @Test
        void makeAMove() {
            gameService.play(id,"BEAST");
            assertThat(gameService.getHistory(id)).hasSize(1);
        }
    }

}
