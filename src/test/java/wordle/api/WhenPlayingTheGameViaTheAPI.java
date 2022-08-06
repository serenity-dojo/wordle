package wordle.api;

import io.restassured.RestAssured;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import wordle.model.GameResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SerenityRunner.class)
public class WhenPlayingTheGameViaTheAPI {

    String id;

    @Steps
    GameAPIFacade gameAPI;

    @Before
    public void newGame() {
        RestAssured.baseURI = "http://localhost:9000";
        id = gameAPI.newGame();
    }

    @Test
    @Title("We create a new game with the /api/game end-point")
    public void creatingANewGame() {
        String id = gameAPI.newGame();
        assertThat(id).isNotEmpty();
    }

    @Test
    @Title("We make a move by posting a word to the with the /api/game/{id}/word end-point")
    public void makingAMove() {
        assertThat(gameAPI.playWord(id, "FEAST").statusCode()).isEqualTo(201);
    }

    @Test
    @Title("At any time we can check the current state of the game by sending a GET to /api/game/{id}/history")
    public void checkingTheStateOfTheGame() {
        gameAPI.playWord(id, "FEAST");
        gameAPI.playWord(id, "BEAST");

        assertThat(gameAPI.gameHistory(id))
                .hasSize(2)
                .allMatch(row -> row.stream().allMatch(this::isAValidCellValue));
    }

    private boolean isAValidCellValue(String cell) {
        return cell.equals("GRAY") || cell.equals("GREEN") || cell.equals("YELLOW");
    }

    @Test
    @Title("We can get the current game result by sending a GET to /api/game/{id}/result")
    public void fetchingTheGameState() {
        assertThat(gameAPI.resultFor(id)).isEqualTo(GameResult.IN_PROGRESS);
    }

    @Test
    @Title("We can try to ask for the answer by sending a GET to /api/game/{id}/answer")
    public void tryingToRevealTheAnswerTooSoon() {
        assertThat(gameAPI.requestAnswer(id).statusCode()).isEqualTo(403);
    }
}
