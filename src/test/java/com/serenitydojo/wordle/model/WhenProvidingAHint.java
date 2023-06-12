package com.serenitydojo.wordle.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("When providing a hint")
class WhenProvidingAHint {

    @Nested
    class AboutFirstAndLastLetters {
        Hints hints = new Hints("WEARY");

        @Test
        void firstLetterHint() {
            assertThat(hints.allHints()).contains("The word starts with the letter W");
        }

        @Test
        void lastLetterHint() {
            assertThat(hints.allHints()).contains("The word ends with the letter Y");
        }
    }

    @ParameterizedTest
    @CsvSource({
            "WEARY, The word contains 2 vowels",
            "SLOTH, The word contains 1 vowel",
            "GLYPH, The word contains no vowels"
    })
    void aboutTheNumberOfVowels(String word, String hint) {
        Hints hints = new Hints(word);
        assertThat(hints.allHints()).contains(hint);
    }

}
