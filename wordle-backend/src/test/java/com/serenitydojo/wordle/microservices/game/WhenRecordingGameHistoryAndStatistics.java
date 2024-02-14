package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.integrationtests.api.TestPlayer;
import com.serenitydojo.wordle.microservices.domain.GameHistory;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
import com.serenitydojo.wordle.microservices.domain.Player;
import com.serenitydojo.wordle.microservices.registration.service.AuthenticationService;
import com.serenitydojo.wordle.microservices.registration.service.PlayerService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Calculating game statistics")
@SpringBootTest(classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class WhenRecordingGameHistoryAndStatistics {

    @Mock
    AuthenticationService authenticationService;

    @Autowired
    GameHistoryService gameHistoryService;

    @Autowired
    PlayerService playerService;

    InMemoryGameService gameService;

    Player player;

    @BeforeEach
    void newGame() {
        gameService = new InMemoryGameService(gameHistoryService, authenticationService);
        TestPlayer testPlayer = TestPlayer.randomPlayer();
        player = playerService.registerPlayer(testPlayer.asPlayer());

        when(authenticationService.userIsAuthenticated()).thenReturn(true);
        when(authenticationService.getCurrentPlayer()).thenReturn(player);
    }

    @Test
    @DisplayName("Game statistics should be initially empty")
    void gameStatisticsShouldBeInitiallyEmpty() {

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(statistics.totalTries()).isEqualTo(0);
        softly.assertThat(statistics.totalWins()).isEqualTo(0);
        softly.assertThat(statistics.percentageSuccessRate()).isEqualTo(0);
        softly.assertThat(statistics.currentWinningStreak()).isEqualTo(0);
        softly.assertThat(statistics.bestWinningStreak()).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution()).hasSize(6);
        softly.assertThat(statistics.guessDistribution().keySet()).contains(1, 2, 3, 4, 5, 6);
        softly.assertThat(statistics.guessDistribution().values()).contains(0, 0, 0, 0, 0, 0);
        softly.assertAll();
    }

    @Test
    @DisplayName("Should record the games played by the player")
    void gameHistoryShouldRecordAllGames() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");

        List<GameHistory> gameHistory = gameHistoryService.findGameHistory(player);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(gameHistory).hasSize(2);
        softly.assertThat(gameHistory).allMatch(history -> history.getPlayer().getId().equals(player.getId()));
        softly.assertThat(gameHistory).allMatch(GameHistory::getWon);
        softly.assertThat(gameHistory.stream().map(GameHistory::getNumberOfGuesses)).contains(1,2);
        softly.assertAll();
    }

    @Test
    @DisplayName("Should calculate the number of games played")
    void gameStatisticsShouldTrackNumberOfTries() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.totalTries()).isEqualTo(2);
    }


    @Test
    @DisplayName("Should calculate the number of games won")
    void gameStatisticsShouldTrackNumberOfGamesWon() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.totalWins()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should calculate the percentage of games won")
    void gameStatisticsShouldTrackTheSuccessRate() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.percentageSuccessRate()).isEqualTo(67);
    }


    @Test
    @DisplayName("Should calculate the current winning streak when winning")
    void gameStatisticsShouldTrackOfTheCurrentWinningStreak() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.currentWinningStreak()).isEqualTo(3);
    }


    @Test
    @DisplayName("Should calculate the best winning streak when winning")
    void gameStatisticsShouldTrackOfBestWinningStreak() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.bestWinningStreak()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should calculate the current winning streak after having lost")
    void gameStatisticsShouldTrackOfTheCurrentWinningStreakAfterLose() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.currentWinningStreak()).isEqualTo(0);
    }


    @Test
    @DisplayName("Should calculate the best winning streak after having lost once")
    void gameStatisticsShouldTrackOfBestWinningStreakAfterHavingLostOnce() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.bestWinningStreak()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should calculate the current winning streak after two winning streaks")
    void gameStatisticsShouldTrackOfTheCurrentWinningStreakAfterPreviousWinningStreak() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");
        playGame("QUEST", "QUEEN", "QUEST");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.currentWinningStreak()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should calculate the Calculating the best winning streak after two winning streaks when the first is better")
    void gameStatisticsShouldTrackOfTheBestWinningStreakAfterPreviousWinningStreak() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");
        playGame("QUEST", "QUEEN", "QUEST");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.bestWinningStreak()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should calculate the best winning streak after two winning streaks when the second is better")
    void gameStatisticsShouldTrackOfTheBestWinningStreakAfterAWorsePreviousWinningStreak() {

        playGame("BLAND", "BLAND");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");
        playGame("BEAST", "BLAND", "BEAST");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("QUEST", "QUEEN", "QUEST");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);

        assertThat(statistics.bestWinningStreak()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should calculate the guess distribution values")
    void calculateGuessDistribution() {
        // Wins in 1 guess
        playGame("BLAND", "BLAND");
        // Wins in 2 guesses
        playGame("BEAST", "BLAND", "BEAST");
        playGame("BEAST", "BLAND", "BEAST");
        // Wins in 3 guesses
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("COCKY", "ROCKS", "ROCKY", "COCKY");
        playGame("COCKY", "BEAST", "LEAST", "LEARN", "ROCKS", "ROCKY", "COCKY");
        // Loses
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");
        playGame("BEAST", "LUCKY", "CLEAN", "FORCE", "QUEST", "QUEEN", "COCKY");

        GameHistoryStatistics statistics = gameHistoryService.calculateStatistics(player);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(statistics.guessDistribution()).hasSize(6);
        softly.assertThat(statistics.guessDistribution().get(1)).isEqualTo(1);
        softly.assertThat(statistics.guessDistribution().get(2)).isEqualTo(2);
        softly.assertThat(statistics.guessDistribution().get(3)).isEqualTo(5);
        softly.assertThat(statistics.guessDistribution().get(4)).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution().get(4)).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution().get(6)).isEqualTo(1);
        softly.assertAll();
    }

    private void playGame(String target, String... words) {
        Long gameId = gameService.newGame(target);
        for (String word : words) {
            gameService.play(gameId, word);
        }
    }

}
