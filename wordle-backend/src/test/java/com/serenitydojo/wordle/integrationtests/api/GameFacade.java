package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.microservices.domain.GameHistoryDTO;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.domain.PlayerScore;
import com.serenitydojo.wordle.model.GameResult;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import java.util.List;

public class GameFacade {

    String token;

    @Step("Create a new game")
    public String newGame() {
        return SerenityRest
                .given().header("Authorization", "Bearer " + token)
                .when().post("/wordle/api/game")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    @Step("Create a new game with the word {0}")
    public String newGameWith(String initialWord) {
        return RestAssured
                .given()
                .given().header("Authorization", "Bearer " + token)
                .body(initialWord)
                .when().post("/wordle/api/game/seed")
                .then()
                .statusCode(200)
                .extract().asString();
    }


    @Step("Play the word '{1}'")
    public Response playWord(String id, String word) {
        return SerenityRest.given()
                .header("Authorization", "Bearer " + token)
                .body(word)
                .when()
                .post("/wordle/api/game/{id}/word", id);
    }

    @Step("Play the words '{1}'")
    public Response playWords(String id, String... words) {
        Response lastResponse = null;
        for(String word : words) {
            lastResponse = SerenityRest.given()
                    .header("Authorization", "Bearer " + token)
                    .body(word)
                    .when()
                    .post("/wordle/api/game/{id}/word", id);
        }
        return lastResponse;
    }

    @Step("Request the answer")
    public Response requestAnswer(String id) {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/{id}/answer", id);
    }

    @Step("Request a hint")
    public Response requestHint(String id) {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/{id}/hint", id);
    }

    @Step("Get the game history")
    public List<List<String>> gameHistory(String id) {
        return getTheGameHistory(id);
    }

    public List<List<String>> getTheGameHistory(String id) {
        return (List<List<String>>) SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/{id}/guesses", id)
                .then()
                .statusCode(200)
                .extract().as(List.class);
    }

    @Step("Get the game result")
    public GameResult resultFor(String id) {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/{id}/result", id).body().as(GameResult.class);
    }

    public String registerPlayer(String name, String password, String email) {
        return registerPlayer(name, password, email,"GB");
    }

    public String registerPlayer(String name, String password, String email, String country) {
        Player player = new Player(name, password, email,country,false);
        SerenityRest
                .given()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/register")
                .then().statusCode(201);

        token = SerenityRest
                .given()
                .body(new Credentials(player.getUsername(), player.getPassword()))
                .contentType(ContentType.JSON)
                .post("/wordle/api/auth/login")
                .jsonPath().getString("accessToken");

        return token;
    }

    @Step("Get the game history for the current player")
    public List<GameHistoryDTO> getTheGameHistoryForTheCurrentPlayer() {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/history")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", GameHistoryDTO.class);
    }

    @Step("Get the game statistics for the current player")
    public GameHistoryStatistics getTheGameStatisticsForTheCurrentPlayer() {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/statistics")
                .then()
                .statusCode(200)
                .extract().jsonPath().getObject("", GameHistoryStatistics.class);
    }

    public List<PlayerScore> getLeaderboardScores() {
        return SerenityRest
                .given()
                .header("Authorization", "Bearer " + token)
                .get("/wordle/api/game/leaderboard")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("", PlayerScore.class);
    }
}
