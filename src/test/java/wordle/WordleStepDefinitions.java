package wordle;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import wordle.model.CellColor;

import java.util.List;
import java.util.stream.Collectors;

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

    @Then("the squares should be colored as follows:")
    public void the_squares_should_be_colored_as_follows(List<List<CellColor>> renderedRows) {
        assertThat(wordleGame.getRenderedCells()).isEqualTo(renderedRows);
    }
}
