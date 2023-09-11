package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.model.GameResult;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import net.serenitybdd.annotations.Step;

import java.util.List;

public class GameFacade {

    @Step("Create a new game")
    public String newGame() {
        return RestAssuredMockMvc.when().post("/api/game")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    @Step("Create a new game with the word {0}")
    public String newGameWith(String initialWord) {
        return RestAssuredMockMvc
                .given().body(initialWord)
                .when().post("/api/game/seed")
                .then()
                .statusCode(200)
                .extract().asString();
    }


    @Step("Play the word '{1}'")
    public MockMvcResponse playWord(String id, String word) {
        return RestAssuredMockMvc.given()
                .body(word)
                .when()
                .post("/api/game/{id}/word", id);
    }

    @Step("Request the answer")
    public MockMvcResponse requestAnswer(String id) {
        return RestAssuredMockMvc
                .get("/api/game/{id}/answer", id);
    }

    @Step("Request a hint")
    public MockMvcResponse requestHint(String id) {
        return RestAssuredMockMvc
                .get("/api/game/{id}/hint", id);
    }

    @Step("Get the game history")
    public List<List<String>> gameHistory(String id) {
        return getTheGameHistory(id);
    }

    public List<List<String>> getTheGameHistory(String id) {
        return (List<List<String>>) RestAssuredMockMvc.get("/api/game/{id}/history", id)
                .then()
                .statusCode(200)
                .extract().as(List.class);
    }

    @Step("Get the game result")
    public GameResult resultFor(String id) {
        return RestAssuredMockMvc.get("/api/game/{id}/result", id).body().as(GameResult.class);
    }

}
