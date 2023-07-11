package com.serenitydojo.wordle.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("When rendering a cell")
class WhenRenderingACell {
    @Test
    @DisplayName("Matching letters in the right spot are Green")
    void matchingLettersInTheRightSpot() {
        Assertions.assertThat(RenderedCell.forTargetWord("weary").forEntry("weary",0)).isEqualTo(CellColor.GREEN);
    }

    @Test
    @DisplayName("Matching letters in the wrong spot are Yellow")
    void matchingLettersInTheWrongSpot() {
        assertThat(RenderedCell.forTargetWord("weary").forEntry("karat",1)).isEqualTo(CellColor.YELLOW);
    }

    @Test
    @DisplayName("Matching letters in the wrong spot are Yellow")
    void nonMatchingLetters() {
        assertThat(RenderedCell.forTargetWord("weary").forEntry("karat",0)).isEqualTo(CellColor.GRAY);
    }

    @DisplayName("When the same letter is tried twice")
    @Nested
    class SameLetterTwice {

        @Nested
        class AMatchingLetter {

            @Test
            void oneMatchInTheWrongSpot() {
                assertThat(RenderedCell.forTargetWord("quart").forEntry("karat",1)).isEqualTo(CellColor.YELLOW);
            }
            @Test
            void secondMatch() {
                assertThat(RenderedCell.forTargetWord("quart").forEntry("karat",3)).isEqualTo(CellColor.GRAY);
            }
            @Test
            void thridMatch() {
                assertThat(RenderedCell.forTargetWord("quart").forEntry("karaa",4)).isEqualTo(CellColor.GRAY);
            }
        }
    }
}
