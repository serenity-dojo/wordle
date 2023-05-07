package com.serenitydojo.wordle.api;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.serenitydojo.wordle.model.GameResult;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Wordle API Specs")
class PlayingTheGameViaTheAPIIT {

    String id;

    @Steps
    GameAPIFacade gameAPI;

    @BeforeEach
    void newGame() {
        RestAssured.baseURI = "http://localhost:9000";
        id = gameAPI.newGame();
    }

    @Test
    @DisplayName("We can check the status of the Wordle service hy sending a GET to /api/service")
    public void checkStatus() {
        SerenityRest.get("/api/status")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("We create a new game with the /api/game end-point")
    void creatingANewGame() {
        String id = gameAPI.newGame();
        assertThat(id).isNotEmpty();
    }

    @Test
    @DisplayName("We make a move by posting a word to the with the /api/game/{id}/word end-point")
    void makingAMove() {
        assertThat(gameAPI.playWord(id, "FEAST").statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("At any time we can check the current state of the game by sending a GET to /api/game/{id}/history")
    void checkingTheStateOfTheGame() {
        gameAPI.playWord(id, "FEAST");
        gameAPI.playWord(id, "BEAST");

        assertThat(gameAPI.gameHistory(id))
                .hasSize(2)
                .allMatch(row -> row.stream().allMatch(this::isAValidCellValue));
    }

    @Test
    @DisplayName("We can play a complete game via the API")
    void playingAGame() {

        id = gameAPI.newGameWith("BLAND");
        gameAPI.playWord(id, "BEAST");
        gameAPI.playWord(id,  "BRAIN");
        gameAPI.playWord(id,  "BLAND");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(gameAPI.gameHistory(id).get(0)).contains("GREEN","GRAY","GREEN","GRAY","GRAY");
        softly.assertThat(gameAPI.gameHistory(id).get(1)).contains("GREEN","GRAY","GREEN","GRAY","YELLOW");
        softly.assertThat(gameAPI.gameHistory(id).get(2)).contains("GREEN","GREEN","GREEN","GREEN","GREEN");
        softly.assertThat(gameAPI.resultFor(id)).isEqualTo(GameResult.WIN);

        softly.assertAll();

    }

    private boolean isAValidCellValue(String cell) {
        return cell.equals("GRAY") || cell.equals("GREEN") || cell.equals("YELLOW");
    }

    @Test
    @DisplayName("We can get the current game result by sending a GET to /api/game/{id}/result")
    void fetchingTheGameState() {
        assertThat(gameAPI.resultFor(id)).isEqualTo(GameResult.IN_PROGRESS);
    }

    @Test
    @DisplayName("We can try to ask for the answer by sending a GET to /api/game/{id}/answer")
    void tryingToRevealTheAnswerTooSoon() {
        assertThat(gameAPI.requestAnswer(id).statusCode()).isEqualTo(403);
    }
}
