package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import io.restassured.RestAssured;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Description;
import net.thucydides.core.annotations.Steps;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Displaying the results of a game")
@Description("Game results are represented in different colors")
class WhenDisplayingTheGame {

    @ParameterizedTest(name = "Should render {0} cells as \"{1}\"")
    @CsvSource({
            "GREEN,  +",
            "YELLOW, *",
            "GRAY,   -"
    })
    @DisplayName("Each cell color has a specific symbol")
    void shouldDisplayCellsInTheRightColor(CellColor color, String renderedValue) {
        assertThat(color.getSymbol()).isEqualTo(renderedValue);
    }
}
