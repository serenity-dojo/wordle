package com.serenitydojo.wordle.integrationtests.api;

import io.restassured.RestAssured;
import net.serenitybdd.annotations.Description;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import com.serenitydojo.wordle.model.GameResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Wordle API Specs")
@Description("The Wordle API allows us to create a new game, play a word, and check the result of the game.")
@TestMethodOrder(OrderAnnotation.class)
@Tag("integration")
class APIExamples {

    String id;

    @Steps
    GameAPIFacade gameAPI;

    @BeforeEach
    void newGame() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    @Test
    @Order(1)
    @DisplayName("We can check the status of the Wordle service by sending a GET to /api/service")
    public void checkStatus() {
        SerenityRest.get("/api/status")
                .then()
                .statusCode(200);
    }

    @DisplayName("Creating a new game")
    @Nested
    class CreatingANewGame {

        @BeforeEach
        void newGame() {
            id = gameAPI.newGame();
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
            gameAPI.gameHistory(id).isEmpty();
        }
    }

    @DisplayName("Playing the game")
    @Nested
    class PlayingTheGame {

        @BeforeEach
        void newGame() {
            id = gameAPI.newGame();
        }

        @Test
        @DisplayName("We make a move by posting a word to the with the /api/game/{id}/word end-point")
        @Order(3)
        void makingAMove() {
            assertThat(gameAPI.playWord(id, "FEAST").statusCode()).isEqualTo(201);
        }

        @Test
        @DisplayName("Invalid words should be rejected with a 403 error")
        @Order(3)
        void makingAMoveWithAnInvalidWord() {
            assertThat(gameAPI.playWord(id, "ABCDE").statusCode()).isEqualTo(403);
        }

        @Test
        @DisplayName("When we make a move, the move is recorded in the game history")
        @Order(4)
        void movesAreRecordedInGameHistory() {
            gameAPI.playWord(id, "FEAST");
            assertThat(gameAPI.gameHistory(id)).hasSize(1);
        }

        @Test
        @DisplayName("We can check the current state of the game by sending a GET to /api/game/{id}/history")
        @Order(5)
        void checkingTheStateOfTheGame() {
            gameAPI.playWord(id, "FEAST");
            gameAPI.playWord(id, "BEAST");

            assertThat(gameAPI.gameHistory(id))
                    .hasSize(2)
                    .allMatch(row -> row.stream().allMatch(this::isAValidCellValue));
        }

        @Test
        @DisplayName("We can get the current game result by sending a GET to /api/game/{id}/result")
        @Order(6)
        void fetchingTheGameState() {
            assertThat(gameAPI.resultFor(id)).isEqualTo(GameResult.IN_PROGRESS);
        }

        @Test
        @DisplayName("If we try to ask for the answer by sending a GET to /api/game/{id}/answer")
        @Order(7)
        void tryingToRevealTheAnswerTooSoon() {
            assertThat(gameAPI.requestAnswer(id).statusCode()).isEqualTo(403);
        }


        @Test
        @Order(8)
        @DisplayName("This is an example of a complete game played via the API")
        void playingAGame() {

            id = gameAPI.newGameWith("BLAND");
            gameAPI.playWord(id, "BEAST");
            gameAPI.playWord(id, "BRAIN");
            gameAPI.playWord(id, "BLAND");

            Serenity.reportThat("The game history should be correctly recorded",
                    () -> {
                        SoftAssertions softly = new SoftAssertions();
                        softly.assertThat(gameAPI.getTheGameHistory(id).get(0)).contains("GREEN", "GRAY", "GREEN", "GRAY", "GRAY");
                        softly.assertThat(gameAPI.getTheGameHistory(id).get(1)).contains("GREEN", "GRAY", "GREEN", "GRAY", "YELLOW");
                        softly.assertThat(gameAPI.getTheGameHistory(id).get(2)).contains("GREEN", "GREEN", "GREEN", "GREEN", "GREEN");
                        softly.assertAll();
                    }
            );
            Serenity.reportThat("The player wins",
                    () -> assertThat(gameAPI.resultFor(id)).isEqualTo(GameResult.WIN)
            );
        }

        private boolean isAValidCellValue(String cell) {
            return cell.equals("GRAY") || cell.equals("GREEN") || cell.equals("YELLOW");
        }
    }

    @DisplayName("Showing game history")
    @Nested
    class ShowingGameHistory {

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
            id = gameAPI.newGameWith(word);
            gameAPI.playWord(id, guess);
            List<List<String>> gameHistory = gameAPI.gameHistory(id);
            List<String> row = gameHistory.get(0);
            assertThat(row).containsExactly(cell1, cell2, cell3, cell4, cell5);
        }
    }

    @DisplayName("Asking for hints")
    @Nested
    @Tag("hints")
    class WhenAskingForHints {
        @Test
        @DisplayName("We can request a hint for the current game via GET /api/game/{id}/hint")
        @Order(8)
        void requestingAHint() {
            id = gameAPI.newGameWith("BLAND");
            gameAPI.requestHint(id).then()
                    .statusCode(200)
                    .body("size()", equalTo(1));
        }

        @ParameterizedTest(name = "Hints should include \"{0}\"")
        @Order(9)
        @ValueSource(strings = {
                "The word starts with the letter B",
                "The word ends with the letter D",
                "The word contains 1 vowel"})
        void requestingAnotherHint(String expectedHint) {
            id = gameAPI.newGameWith("BLAND");
            gameAPI.requestHint(id);
            List<String> hints = gameAPI.requestHint(id).then().extract().as(ArrayList.class);
            assertThat(hints).contains(expectedHint);
        }
    }
}
