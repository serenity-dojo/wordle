package com.serenitydojo.wordle.dictionary;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("When checking whether a word is allowed or not")
public class WhenCheckingWordsAgainstTheDictionary {

    WordleDictionary wordleDictionary = new WordleDictionary();

    @DisplayName("5 letter UK words should be allowed")
    @ParameterizedTest
    @ValueSource(strings = {"bloat","plane","faint","feast","wordy"})
    void shouldAllowValid5LetterWords(String word) {
        assertThat(wordleDictionary.contains(word)).isTrue();
    }

    @DisplayName("The check should be case insensitive")
    @ParameterizedTest
    @ValueSource(strings = {"Bloat","PLANE"})
    void shouldBeCaseInsensitive(String word) {
        assertThat(wordleDictionary.contains(word)).isTrue();
    }

    @DisplayName("Words with different lengths should not be allowed")
    @ParameterizedTest
    @ValueSource(strings = {"to","aardvark","painting"})
    void shouldRefuseValidWordsThatDontHave5Letters(String word) {
        assertThat(wordleDictionary.contains(word)).isFalse();
    }

    @DisplayName("5-letter combinations that are not words should not be allowed")
    @ParameterizedTest
    @ValueSource(strings = {"abcde","last!","avion"})
    void shouldRefuseNonWords(String word) {
        assertThat(wordleDictionary.contains(word)).isFalse();
    }

}
