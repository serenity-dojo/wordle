package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.model.GameResult;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Creating a new game")
@Tag("integration")
@SpringBootTest(classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
@AutoConfigureMockMvc
public class CreatingANewGame {

    @Autowired
    private MockMvc mockMvc;

    String id;

    @Steps
    GameFacade gameFacade;

    @BeforeEach
    void newGame() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        id = gameFacade.newGame();
    }

    @Test
    @DisplayName("Each new game should be assigned a unique id")
    @Order(1)
    void creatingANewGame() {
        assertThat(id).isNotEmpty();
    }

    @Test
    @DisplayName("The new game should contain no moves")
    @Order(2)
    void aNewGameShouldHaveAnEmptyHistory() {
        assertThat(gameFacade.gameHistory(id)).isEmpty();
    }

    @Test
    @DisplayName("The new game should initially be In Progress")
    @Order(2)
    void aNewGameShouldBeInProgress() {
        assertThat(gameFacade.resultFor(id)).isEqualTo(GameResult.IN_PROGRESS);
    }

}
