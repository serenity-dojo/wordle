package com.serenitydojo.wordle.model;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.serenitydojo.wordle.WordleGame;
import com.serenitydojo.wordle.dictionary.WordleDictionary;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("When playing the Wordle game")
@ExtendWith(SerenityJUnit5Extension.class)
class WhenPlayingTheGame {

    @Nested
    class WhenTheGameStarts {

        WordleDictionary dictionary = new WordleDictionary();
        WordleGame game = WordleGame.withRandomWord(dictionary);

        @Test
        @DisplayName("A game starts as in progress")
        void theGameIsInProgress() {
            assertThat(game.getResult()).isEqualTo(GameResult.IN_PROGRESS);
        }

        @Test
        @DisplayName("A game starts with no entries")
        void theGameHasNoTries() {
            assertThat(game.getRenderedCells()).isEmpty();
        }
    }

    @Nested
    class WhenAPlayerPlaysTheGame {
        WordleDictionary dictionary = new WordleDictionary();
        WordleGame game = new WordleGame("BEAST", dictionary);


        @DisplayName("Each entry is recorded")
        @Test
        void entriesAreRecorded() {
            assertThat(game.play("FORTH")).isEqualTo(GameResult.IN_PROGRESS);
        }

        @DisplayName("An unsuccessful try means the game is still in progress")
        @Test
        void anUnsuccessfulTry() {
            play(game,"FIRST","BRAIN","BEAST");

            assertThat(game.getRenderedCells()).hasSize(3);
        }

        @DisplayName("A successful tries means the game is won")
        @Test
        void aSuccessfulTry() {
            play(game,"FIRST","AGAIN");
            assertThat(game.play("BEAST")).isEqualTo(GameResult.WIN);
        }

        @DisplayName("Six unsuccessful tries means the game is lost")
        @Test
        void sixUnsuccessfulTris() {
            play(game,"WRONG","WRONG","WRONG","WRONG","WRONG");

            assertThat(game.play("AGAIN")).isEqualTo(GameResult.LOSE);
        }

        @DisplayName("Once the game is lost no new tries are accepted")
        @Test
        void playingAfterLosingIsNotAllowed() {
            play(game,"WRONG","WRONG","WRONG","WRONG","WRONG","WRONG");

            assertThatThrownBy( () -> game.play("AGAIN"))
                    .isInstanceOf(IllegalPlayException.class)
                    .hasMessageContaining("Game over");

        }

        @DisplayName("Non-existant words are rejected")
        @Test
        void cantPlayNonexistantWords() {
            assertThatThrownBy( () -> game.play("ABCDE"))
                    .isInstanceOf(IllegalPlayException.class)
                    .hasMessageContaining("'ABCDE' is not a valid word");

            assertThat(game.getRenderedCells()).isEmpty();
        }

        @DisplayName("The target word")
        @Nested
        class TheTargetWord {
            @DisplayName("can be revealed at the end of the game")
            @Test
            void whenThePlayerLooses() {
                play(game,"WRONG","WRONG","WRONG","WRONG","WRONG","WRONG");

                assertThat(game.revealAnswer()).isEqualTo("BEAST");
            }

            @DisplayName("cannot be revealed during the game")
            @Test
            void cannotBeRevealedDuringTheGame() {
                assertThatThrownBy( () -> game.revealAnswer())
                        .isInstanceOf(IllegalAttemptToShowAnswerException.class)
                        .hasMessageContaining("Don't cheat!");
            }

        }
    }

    private void play(WordleGame game, String... words) {
        for(String word : words) {
            game.play(word);
        }
    }
}
