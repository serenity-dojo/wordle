package com.serenitydojo.wordle.integrationtests.api;

import com.google.common.collect.ImmutableMap;
import com.serenitydojo.wordle.microservices.authentication.Player;
import com.serenitydojo.wordle.model.GameResult;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFacade {

    @Step("Create a new game")
    public String newGame() {
        return SerenityRest.when().post("/api/game")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    @Step("Create a new game with the word {0}")
    public String newGameWith(String initialWord) {
        return RestAssured
                .given().body(initialWord)
                .when().post("/api/game/seed")
                .then()
                .statusCode(200)
                .extract().asString();
    }


    @Step("Play the word '{1}'")
    public Response playWord(String id, String word) {
        return SerenityRest.given()
                .body(word)
                .when()
                .post("/api/game/{id}/word", id);
    }

    @Step("Request the answer")
    public Response requestAnswer(String id) {
        return SerenityRest
                .get("/api/game/{id}/answer", id);
    }

    @Step("Request a hint")
    public Response requestHint(String id) {
        return SerenityRest
                .get("/api/game/{id}/hint", id);
    }

    @Step("Get the game history")
    public List<List<String>> gameHistory(String id) {
        return getTheGameHistory(id);
    }

    public List<List<String>> getTheGameHistory(String id) {
        return (List<List<String>>) SerenityRest.get("/api/game/{id}/history", id)
                .then()
                .statusCode(200)
                .extract().as(List.class);
    }

    @Step("Get the game result")
    public GameResult resultFor(String id) {
        return SerenityRest.get("/api/game/{id}/result", id).body().as(GameResult.class);
    }

    public Long registerPlayer(String name, String email, String password) {
        Player player = new Player(email, password, name);
        return SerenityRest
                .with()
                .body(player)
                .contentType(ContentType.JSON)
                .post("/api/players/register")
                .getBody().as(Long.class);
    }

}
