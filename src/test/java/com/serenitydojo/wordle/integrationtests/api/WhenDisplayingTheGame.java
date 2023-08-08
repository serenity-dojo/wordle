package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.WordleGame;
import com.serenitydojo.wordle.dictionary.WordleDictionary;
import com.serenitydojo.wordle.model.CellColor;
import net.serenitybdd.annotations.Description;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Displaying the results of a game")
@Description("Game results are represented in different colors")
@Tag("integration")
@TestMethodOrder(OrderAnnotation.class)
class WhenDisplayingTheGame {

    @Test
    void shouldShowIncorrectCellsAsGray() {
        assertThat(CellColor.GRAY).isIn(CellColor.values());
    }

    @Test
    void shouldShowMisplacedCellsAsYellow() {
        assertThat(CellColor.YELLOW).isIn(CellColor.values());
    }

    @Nested
    @Order(1)
    @DisplayName("When rendering cells")
    class WhenRenderingCells {

        @Test
        void shouldShowCorrectCellsAsGreen() {
            assertThat(CellColor.GREEN).isIn(CellColor.values());
        }
        @DisplayName("Cell color codes")
        @ParameterizedTest(name = "{0} cells are {1}")
        @CsvSource({
                "GREEN, correct and in the correct position",
                "YELLOW, correct but in the wrong position",
                "GRAY, not in the word"})
        void shouldShowCellsWithColor(String color, String meaning) {
            assertThat(CellColor.valueOf(color)).isIn(CellColor.values());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("A row of cells contains cells of different colors")
    class ARowOfCellsContainsCellsOfDifferentColors {

        @ParameterizedTest(name = "When the word is {0} and the guess is {1}, the row is {2}, {3}, {4}, {5}, {6}")
        @DisplayName("For example")
        @CsvSource({
                "CRYPT, ORGAN, GRAY, GREEN, GRAY, GRAY, GRAY",
                "CRYPT, BRACE, GRAY, GREEN, GRAY, YELLOW, GRAY",
                "CRYPT, CRYPT, GREEN, GREEN, GREEN, GREEN, GREEN",})
        void shouldShowRowOfColoredCells(String word,
                                         String guess,
                                         String cell1,
                                         String cell2,
                                         String cell3,
                                         String cell4,
                                         String cell5) {
            WordleDictionary dictionary = new WordleDictionary();
            WordleGame game = new WordleGame(word, dictionary);
            game.play(guess);

            List<CellColor> row = game.getRenderedCells().get(0);

            assertThat(row).containsExactly(
                    CellColor.valueOf(cell1),
                    CellColor.valueOf(cell2),
                    CellColor.valueOf(cell3),
                    CellColor.valueOf(cell4),
                    CellColor.valueOf(cell5)
            );
        }
    }
}
