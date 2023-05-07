package com.serenitydojo.wordle.acceptancetests.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import com.serenitydojo.wordle.WordleGame;
import com.serenitydojo.wordle.dictionary.WordleDictionary;
import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WordleStepDefinitions {

    WordleGame wordleGame;
    WordleDictionary dictionary = new WordleDictionary();

    @Given("the target word is:")
    public void the_target_word_is(DataTable targetWord) {
        String target = String.join("", targetWord.asLists().get(0));
        this.wordleGame = new WordleGame(target, dictionary);
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

    @Then("the player should {gameResult} the game")
    public void playerShould(GameResult gameResult) {
        assertThat(wordleGame.getResult()).isEqualTo(gameResult);
    }
}
