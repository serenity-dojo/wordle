package com.serenitydojo.wordle.acceptancetests;

import com.serenitydojo.wordle.model.CellColor;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StepDefinitions {
    @Given("the player is playing Wordle")
    public void the_player_is_playing_wordle() {
    }

    @When("the player proposes {string}")
    public void the_player_proposes(String proposedWord) {
    }

    @ParameterType("(YES|NO|Yes|No)")
    public boolean yesNo(String value) {
        return value.equalsIgnoreCase("Yes");
    }

    @Then("the word should or should not be allowed: {yesNo}")
    public void the_word_should_should_not_be_allowed(boolean isAllowed) {
//        throw new PendingException();
    }

    @Given("the target word is {string}")
    public void theTargetWordIs(String targetWord) {
    }

    @DataTableType
    public CellColor cellColor(String cellColor) {
        return CellColor.valueOf(cellColor);
    }

    @Then("the feedback should be:")
    public void theFeedbackShouldBe(List<List<CellColor>> rendered) {
        List<CellColor> renderedWord = rendered.get(0);
        // TODO: Compare the rendered with the current state of the game
    }

}
