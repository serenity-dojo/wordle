package com.serenitydojo.wordle.acceptancetests;

import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {
    @Before
    public void setTheBaseURI() {
        RestAssured.baseURI = "http://localhost:9000";
    }

    String gameId;
    @Given("the player is playing Wordle")
    public void the_player_is_playing_wordle() {
        // Create a new game via the /api/game endpoint and store the id
        gameId = RestAssured.post("/api/game")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    ValidatableResponse response;

    @When("the player proposes {string}")
    public void the_player_proposes(String proposedWord) {
        // POST the proposed word to the /api/game/{id}/word endpoint
        response = RestAssured.given()
                .body(proposedWord)
                .when()
                .post("/api/game/{id}/word", gameId)
                .then();
    }

    @ParameterType("(YES|NO|Yes|No)")
    public boolean yesNo(String value) {
        return value.equalsIgnoreCase("Yes");
    }

    @Then("the word should or should not be allowed: {yesNo}")
    public void the_word_should_should_not_be_allowed(boolean isAllowed) {
        // Check that the response status code is 201 or 403 depending on whether the word is allowed
        if (isAllowed) {
            response.statusCode(201);
        } else {
            response.statusCode(403);
        }
    }

    @Given("the target word is {string}")
    public void theTargetWordIs(String targetWord) {
        // Seed a new game with the target word using /api/game/seed
        gameId = RestAssured.given()
                .body(targetWord)
                .when()
                .post("/api/game/seed")
                .then()
                .statusCode(200)
                .extract().asString();
    }

    @Then("the feedback should be:")
    public void theFeedbackShouldBe(List<List<CellColor>> rendered) {
        List<CellColor> expectedColors = rendered.get(0);

        List<CellColor> letterColors = Optional.ofNullable((List<?>) response.extract().jsonPath().get("[0]"))
                .orElseGet(Collections::emptyList) // This will ensure an empty list is used if the result is null
                .stream()
                .map(letterColor -> CellColor.valueOf(letterColor.toString()))
                .toList();

        assertThat(letterColors).isEqualTo(expectedColors);
    }

    @DataTableType
    public CellColor cellColor(String cellColor) {
        return CellColor.valueOf(cellColor);
    }

    @When("the player proposes")
    public void thePlayerProposes(List<String> proposedWords) {
        // TODO: play each word one by one
    }


    @Then("the result should be: {}")
    public void theResultShouldBe(GameResult expectedResult) {
        // Fetch the current game result from /api/game/{id}/result and convert it to a GameResult
        GameResult actualResult = RestAssured.get("/api/game/{id}/result", gameId)
                .then()
                .statusCode(200)
                .extract()
                .as(GameResult.class);

        // Compare the actual result with the expected result
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
