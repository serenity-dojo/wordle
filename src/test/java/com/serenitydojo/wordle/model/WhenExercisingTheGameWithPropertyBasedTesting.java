package com.serenitydojo.wordle.model;

import com.serenitydojo.wordle.WordleGame;
import com.serenitydojo.wordle.dictionary.WordleDictionary;
import io.cucumber.java.sl.Ce;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenExercisingTheGameWithPropertyBasedTesting {

    @Test
    void nonMatchingCharsWithNonLatinCharactersShouldNotWinTheGame() {

        String word = "CRYPT";
        String guess = "ᾓ" + "RYPT";

        WordleDictionary dictionary = new LenientDictionary();
        WordleGame game = WordleGame.withSpecifiedWord(word, dictionary);

        GameResult result = game.play(guess);
        assertThat(result).isNotEqualTo(GameResult.WIN);
    }

    @Property
    void anyLetterThatIsNotPresentInTheWordWillBeRenderedAsGray(@ForAll("incorrectLetters") Character letter) {
        String word = "CRYPT";
        String guess = letter + "RYPT";

        WordleDictionary dictionary = new LenientDictionary();
        WordleGame game = WordleGame.withSpecifiedWord(word, dictionary);

        GameResult result = game.play(guess);
        assertThat(result).isNotEqualTo(GameResult.WIN);
    }

    // Any non-matching word should not win the game
    @Property
    void anyNonMatchingWordShouldNotWin(
            @ForAll("5 letter words") String word,
            @ForAll("5 letter words") String guess) {
        WordleDictionary dictionary = new LenientDictionary();
        WordleGame game = WordleGame.withSpecifiedWord(word, dictionary);

        GameResult result = game.play(guess);

        if (guess.equals(word)) {
            assertThat(result).isEqualTo(GameResult.WIN);
        } else {
            assertThat(result).isNotEqualTo(GameResult.WIN);
        }
    }

    @Test
    void lettersShouldBeRenderedInTheCorrectColor() {
        String guess = "HZYZY";
        String word = "CRYPT";
        WordleDictionary dictionary = new LenientDictionary();
        WordleGame game = WordleGame.withSpecifiedWord(word, dictionary);

        game.play(guess);
        List<CellColor> cells = game.getRenderedCells().get(0);
        assertThat(cells).contains(CellColor.GRAY, CellColor.GRAY, CellColor.GREEN, CellColor.GRAY, CellColor.GRAY);
    }

    @Property
    void lettersShouldBeRenderedInTheCorrectColor(@ForAll("5 letter alpha words") String guess) {
        String word = "CRYPT";
        WordleGame game = WordleGame.withSpecifiedWord(word, new LenientDictionary());

        // The colors are determined by the number of matches. So if the letter Y appears twice in the guessed word,
        // it will be shown as yellow or green the first time, and grey the second time.

        // Let's play the game with the proposed word and see what the colors are
        game.play(guess);
        List<CellColor> cells = game.getRenderedCells().get(0);
        List<CellColor> expectedColors = expectedColorsFor(guess, word);

        assertThat(cells).withFailMessage("Target word: " + word + ", guess: " + guess).isEqualTo(expectedColors);
    }

    /**
     * This method calculates the expected colors for a given guess and target word.
     */
    private List<CellColor> expectedColorsFor(String guess, String word) {
        CellColor[] expectedColors = new CellColor[5];
        Set<Character> remainingLetters = new HashSet<>();
        for (int i = 0; i < word.length(); i++) {
            remainingLetters.add(word.charAt(i));
        }

        for (int i = 0; i < word.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                // Any matching letters in the right place should be green
                expectedColors[i] = CellColor.GREEN;
                remainingLetters.remove(word.charAt(i));
            } else if (guess.charAt(i) != word.charAt(i) && remainingLetters.contains(guess.charAt(i))) {
                // Any matching letters in the wrong place should be yellow
                expectedColors[i] = CellColor.YELLOW;
                remainingLetters.remove(guess.charAt(i));
            } else {
                // Any other letters should be grey
                expectedColors[i] = CellColor.GRAY;
            }
        }
        return Arrays.asList(expectedColors);
    }


    @Provide("5 letter words")
    Arbitrary<String> fiveLetterWords() {
        return Arbitraries.strings().ofLength(5);
    }

    @Provide("5 letter alpha words")
    Arbitrary<String> fiveLetterAlphaWords() {
        return Arbitraries.strings().withCharRange('A', 'Z').ofLength(5);
    }

    @Provide
    Arbitrary<Character> incorrectLetters() {
        return Arbitraries.chars().filter(letter -> !(letter == 'C' || letter == 'c'));
    }
}
