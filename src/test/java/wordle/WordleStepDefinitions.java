package wordle;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import wordle.model.CellColor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WordleStepDefinitions {

    WordleGame wordleGame;

    @Given("the target word is:")
    public void the_target_word_is(DataTable targetWord) {
        String target = String.join("", targetWord.asLists().get(0));
        this.wordleGame = new WordleGame(target);
    }

    @When("the player enters the following letters:")
    public void the_player_enters_the_following_letters(List<String> playerAttempts) {
        playerAttempts.forEach(
                attempt -> wordleGame.play(attempt)
        );
    }

    Throwable exceptionThrownAfterAnInvalidAttempt;
    String attemptedWord;

    @When("the player attempts to enter the following letters:")
    // We are expecting an exception to be thrown here.
    public void the_player_attempts_to_enters_the_following_letters(List<String> playerAttempts) {
        attemptedWord = playerAttempts.get(0);
        try {
            wordleGame.play(attemptedWord);
        } catch(Throwable e) {
            exceptionThrownAfterAnInvalidAttempt = e;
        }
    }

    @Then("the attempt should be rejected")
    public void attemptShouldBeRejected() {
        assertThat(exceptionThrownAfterAnInvalidAttempt)
                .isNotNull()
                .hasMessageContaining("'" + attemptedWord + "' is not a valid word");
    }

    @Then("the squares should be colored as follows:")
    public void the_squares_should_be_colored_as_follows(List<List<CellColor>> renderedRows) {
        assertThat(wordleGame.getRenderedCells()).isEqualTo(renderedRows);
    }
}
