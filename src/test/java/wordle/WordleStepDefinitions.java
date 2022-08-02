package wordle;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import wordle.model.CellColor;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class WordleStepDefinitions {

    List<String> attempts;
    List<List<CellColor>> renderedRows;

    WordleGame wordleGame;

    @Given("the target word is:")
    public void the_target_word_is(DataTable targetWord) {
        String target = String.join("", targetWord.asLists().get(0));
        this.wordleGame = new WordleGame(target);

    }

    @When("the player enters the following letters:")
    public void the_player_enters_the_following_letters(DataTable playerAttempts) {
        this.attempts = playerAttempts.asLists().stream()
                .map(cellsInRow -> String.join("", cellsInRow))
                .collect(Collectors.toList());

        this.attempts.forEach(
                attempt -> wordleGame.play(attempt)
        );

    }

    @Then("the squares should be colored as follows:")
    public void the_squares_should_be_colored_as_follows(DataTable dataTable) {
        this.renderedRows = dataTable.asLists().stream()
                .map(listOfCellSymbols -> colored(listOfCellSymbols))
                .collect(Collectors.toList());

        assertThat(wordleGame.getRenderedCells()).isEqualTo(this.renderedRows);
    }

    private List<CellColor> colored(List<String> listOfCellSymbols) {
        return listOfCellSymbols.stream()
                .map(symbol -> CellColor.withSymbol(symbol))
                .collect(Collectors.toList());
    }

}
