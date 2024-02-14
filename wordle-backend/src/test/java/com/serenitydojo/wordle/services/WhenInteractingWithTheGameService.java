package com.serenitydojo.wordle.services;

import com.serenitydojo.wordle.microservices.game.GameHistoryService;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationService;
import com.serenitydojo.wordle.microservices.game.GameService;
import com.serenitydojo.wordle.microservices.game.InMemoryGameService;
import com.serenitydojo.wordle.microservices.game.NoSuchGameException;
import com.serenitydojo.wordle.model.GameResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class WhenInteractingWithTheGameService {

    GameService gameService;

    @Mock
    private GameHistoryService gameHistoryService;

    @Mock
    private AuthenticationService authenticationService;

    @BeforeEach
    public void newGameService() {
        gameService = new InMemoryGameService(gameHistoryService, authenticationService);
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

