package wordle.api;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wordle.model.GameResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class WhenPlayingTheGameViaTheAPI {

    String id;

    @Before
    public void newGame() {
        RestAssured.reset();
        RestAssured.baseURI = "http://localhost:9000";
        id = RestAssured.when().post("/api/game").body().asString();
    }

    @Test
    public void creatingANewGameShouldReturnAUniqueId() {
        String id = SerenityRest.when()
                .post("/api/game")
                .then()
                .statusCode(200)
                .extract().asString();

        assertThat(id).isNotEmpty();
    }

    @Test
    public void playingAGameWithOneTry() {
        SerenityRest.given()
                .pathParam("id", id)
                .body("FEAST")
                .when()
                .post("/api/game/{id}/word");


        List<List<String>> gameState = (List<List<String>>) SerenityRest.get("/api/game/{id}/history", id)
                .then()
                .statusCode(200)
                .extract().as(List.class);

        assertThat(gameState).hasSize(1);
        assertThat(gameState.get(0))
                .hasSize(5)
                .allMatch(
                        cell -> cell.equals("GRAY") || cell.equals("GREEN") || cell.equals("YELLOW")
                );
    }

    @Test
    public void fetchingGameHistory() {

        RestAssured.given().pathParam("id", id).body("FEAST").post("/api/game/{id}/word");
        RestAssured.given().pathParam("id", id).body("BEAST").post("/api/game/{id}/word");

        List<List<String>> gameState = (List<List<String>>) SerenityRest
                .given().pathParam("id", id)
                .get("/api/game/{id}/history")
                .then()
                .statusCode(200)
                .extract().as(List.class);

        assertThat(gameState).hasSize(2);
    }

    @Test
    public void fetchingTheGameState() {
        GameResult result = SerenityRest
                .get("/api/game/{id}/result", id)
                .then()
                .extract().as(GameResult.class);

        assertThat(result).isEqualTo(GameResult.IN_PROGRESS);
    }

    @Test
    public void tryingToRevealTheAnswerTooSoon() {
        SerenityRest
                .get("/api/game/{id}/answer", id)
                .then()
                .statusCode(403);
    }

}
