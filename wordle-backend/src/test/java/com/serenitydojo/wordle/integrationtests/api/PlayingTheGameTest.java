package com.serenitydojo.wordle.integrationtests.api;

import com.github.javafaker.Faker;
import com.serenitydojo.wordle.microservices.domain.GameHistoryDTO;
import com.serenitydojo.wordle.model.GameResult;
import io.restassured.RestAssured;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Playing the game")
@Tag("gameplay")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class PlayingTheGameTest {

    String id;

    @LocalServerPort
    private int port;

    @Steps
    GameFacade gameFacade;

    String token;

    @BeforeEach
    void newGame() {
        RestAssured.baseURI = "http://localhost:" + port;

        TestPlayer testPlayer = TestPlayer.randomPlayer();
        token = gameFacade.registerPlayer(testPlayer.name(), testPlayer.password(), testPlayer.email());
        id = gameFacade.newGame();
    }

    @Test
    @DisplayName("We make a move by posting a word to the with the /api/game/{id}/word end-point")
    @Order(3)
    void makingAMove() {
        assertThat(gameFacade.playWord(id, "FEAST").statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("Invalid words should be rejected with a 403 error")
    @Order(4)
    void makingAMoveWithAnInvalidWord() {
        assertThat(gameFacade.playWord(id, "ABCDE").statusCode()).isEqualTo(403);
    }

    @Test
    @DisplayName("When we make a move, the move is recorded in the game history")
    @Order(5)
    void movesAreRecordedInGameHistory() {
        gameFacade.playWord(id, "FEAST");
        assertThat(gameFacade.gameHistory(id)).hasSize(1);
    }

    @Test
    @DisplayName("We can check the current state of the game by sending a GET to /api/game/{id}/guesses")
    @Order(6)
    void checkingTheStateOfTheGame() {
        gameFacade.playWords(id, "FEAST", "BEAST");

        assertThat(gameFacade.gameHistory(id))
                .hasSize(2)
                .allMatch(row -> row.stream().allMatch(this::isAValidCellValue));
    }

    @Test
    @DisplayName("We can get the current game result by sending a GET to /api/game/{id}/result")
    @Order(7)
    void fetchingTheGameState() {
        assertThat(gameFacade.resultFor(id)).isEqualTo(GameResult.IN_PROGRESS);
    }

    @Test
    @DisplayName("If we try to ask for the answer by sending a GET to /api/game/{id}/answer")
    @Order(8)
    void tryingToRevealTheAnswerTooSoon() {
        assertThat(gameFacade.requestAnswer(id).statusCode()).isEqualTo(403);
    }


    @Test
    @Order(9)
    @DisplayName("This is an example of a complete game played via the API")
    void playingAGame() {

        id = gameFacade.newGameWith("BLAND");
        gameFacade.playWords(id, "BEAST", "BRAIN", "BLAND");

        Serenity.reportThat("The game history should be correctly recorded",
                () -> {
                    SoftAssertions softly = new SoftAssertions();
                    softly.assertThat(gameFacade.getTheGameHistory(id).get(0)).contains("GREEN", "GRAY", "GREEN", "GRAY", "GRAY");
                    softly.assertThat(gameFacade.getTheGameHistory(id).get(1)).contains("GREEN", "GRAY", "GREEN", "GRAY", "YELLOW");
                    softly.assertThat(gameFacade.getTheGameHistory(id).get(2)).contains("GREEN", "GREEN", "GREEN", "GREEN", "GREEN");
                    softly.assertAll();
                }
        );
        Serenity.reportThat("The player wins",
                () -> assertThat(gameFacade.resultFor(id)).isEqualTo(GameResult.WIN)
        );
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/examples/rendered_words.csv", numLinesToSkip = 1)
    @DisplayName("Each cell should be rendered in the correct color")
    void should_render_each_letter_with_the_appropriate_color(String word,
                                                              String guess,
                                                              String cell1,
                                                              String cell2,
                                                              String cell3,
                                                              String cell4,
                                                              String cell5) {
        id = gameFacade.newGameWith(word);

        gameFacade.playWord(id, guess);

        List<List<String>> moves = gameFacade.gameHistory(id);
        assertThat(moves.get(0)).containsExactly(cell1, cell2, cell3, cell4, cell5);
    }

    private boolean isAValidCellValue(String cell) {
        return cell.equals("GRAY") || cell.equals("GREEN") || cell.equals("YELLOW");
    }

    @ParameterizedTest(name = "When the word is {0} and the guess is {1}, the row is {2}, {3}, {4}, {5}, {6}")
    @CsvSource({
            "CRYPT, ORGAN, GRAY, GREEN, GRAY, GRAY, GRAY",
            "CRYPT, BRACE, GRAY, GREEN, GRAY, YELLOW, GRAY",
            "CRYPT, CRYPT, GREEN, GREEN, GREEN, GREEN, GREEN",})
    void shouldShowRowOfColoredCells(String word,
                                     String guess,
                                     String cell1,
                                     String cell2,
                                     String cell3,
                                     String cell4,
                                     String cell5) {
        id = gameFacade.newGameWith(word);
        gameFacade.playWord(id, guess);
        List<List<String>> gameHistory = gameFacade.gameHistory(id);
        List<String> row = gameHistory.get(0);
        assertThat(row).containsExactly(cell1, cell2, cell3, cell4, cell5);
    }

    @Test
    @DisplayName("After the game has finished the answer can be revealed via via GET /api/game/{id}/answer")
    @Order(10)
    void gettingTheGameResult() {
        id = gameFacade.newGameWith("BLAND");
        gameFacade.playWord(id, "BLAND");
        String answer = gameFacade.requestAnswer(id).then()
                .statusCode(200)
                .extract().asString();

        assertThat(answer).isEqualTo("BLAND");
    }
}
