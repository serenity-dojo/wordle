package com.serenitydojo.wordle.integrationtests.api;

import com.github.javafaker.Faker;
import com.serenitydojo.wordle.model.GameResult;
import io.restassured.RestAssured;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Creating a new game")
@Tag("integration")
@Tag("creation")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class CreatingANewGameTest {

    @LocalServerPort
    private int port;

    Faker fake = Faker.instance();

    String name = fake.name().username();
    String password = fake.bothify("????####");
    String email = fake.bothify("????##@gmail.com");

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        gameFacade.registerPlayer(name, password, email);
        id = gameFacade.newGame();
    }

    String id;

    @Steps
    GameFacade gameFacade;

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
