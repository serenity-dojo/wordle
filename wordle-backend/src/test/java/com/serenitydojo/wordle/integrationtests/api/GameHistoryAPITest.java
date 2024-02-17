package com.serenitydojo.wordle.integrationtests.api;

import com.serenitydojo.wordle.microservices.domain.GameHistoryDTO;
import com.serenitydojo.wordle.microservices.domain.GameHistoryStatistics;
import com.serenitydojo.wordle.microservices.domain.PlayerScore;
import io.restassured.RestAssured;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Game history and statistics")
@Tag("integration")
@Tag("history")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
public class GameHistoryAPITest {

    @LocalServerPort
    private int port;

    @Steps
    GameFacade gameFacade;

    String token;

    TestPlayer testPlayer;

    @BeforeEach
    void newGame() {
        RestAssured.baseURI = "http://localhost:" + port;

        testPlayer = TestPlayer.randomPlayer();

        token = gameFacade.registerPlayer(testPlayer.name(),
                testPlayer.password(),
                testPlayer.email(),
                "GB");
    }

    @Test
    @DisplayName("Game results are recorded in the game history")
    void gameHistory() {

        String game1 = gameFacade.newGameWith("BLAND");
        gameFacade.playWords(game1, "BEAST", "BRAIN", "BLAND");

        String game2 = gameFacade.newGameWith("QUEST");
        gameFacade.playWords(game2, "CLEAN", "CLEAR", "QUEEN", "QUEST");

        String game3 = gameFacade.newGameWith("BEAST");
        gameFacade.playWords(game3, "CLEAN", "QUEEN", "QUEST", "CRONE", "CREST", "CROWN");

        List<GameHistoryDTO> gameHistory = gameFacade.getTheGameHistoryForTheCurrentPlayer();

        SoftAssertions softly = new SoftAssertions();
        List<Integer> numberOfGuesses = gameHistory.stream().map(GameHistoryDTO::numberOfGuesses).toList();
        softly.assertThat(numberOfGuesses).contains(3, 4, 6);

        List<Boolean> gameOutcomes = gameHistory.stream().map(GameHistoryDTO::outcome).toList();
        softly.assertThat(gameOutcomes).contains(true, true, false);

        softly.assertAll();
    }

    @Test
    @DisplayName("Game statistics can be retrieved for the current player")
    void gameStatistics() {

        String game1 = gameFacade.newGameWith("BLAND");
        gameFacade.playWords(game1, "BEAST", "BRAIN", "BLAND");

        String game2 = gameFacade.newGameWith("QUEST");
        gameFacade.playWords(game2, "CLEAN", "CLEAR", "QUEEN", "QUEST");

        String game3 = gameFacade.newGameWith("BEAST");
        gameFacade.playWords(game3, "CLEAN", "CLEAR", "QUEEN", "BEAST");

        String game4 = gameFacade.newGameWith("BEAST");
        gameFacade.playWords(game4, "CLEAN", "QUEEN", "QUEST", "CRONE", "CREST", "CROWN");

        GameHistoryStatistics statistics = gameFacade.getTheGameStatisticsForTheCurrentPlayer();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(statistics.totalTries()).isEqualTo(4);
        softly.assertThat(statistics.totalWins()).isEqualTo(3);
        softly.assertThat(statistics.percentageSuccessRate()).isEqualTo(75);
        softly.assertThat(statistics.currentWinningStreak()).isEqualTo(0);
        softly.assertThat(statistics.bestWinningStreak()).isEqualTo(3);
        softly.assertThat(statistics.guessDistribution().get(1)).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution().get(2)).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution().get(3)).isEqualTo(1);
        softly.assertThat(statistics.guessDistribution().get(4)).isEqualTo(2);
        softly.assertThat(statistics.guessDistribution().get(5)).isEqualTo(0);
        softly.assertThat(statistics.guessDistribution().get(6)).isEqualTo(0);

        softly.assertAll();
    }

    @Test
    @DisplayName("Player statistics should appear in the leaderboard")
    void leaderboard() {
        String game1 = gameFacade.newGameWith("BLAND");
        gameFacade.playWords(game1, "BEAST", "BRAIN", "BLAND");

        String game2 = gameFacade.newGameWith("QUEST");
        gameFacade.playWords(game2, "CLEAN", "CLEAR", "QUEEN", "QUEST");

        String game3 = gameFacade.newGameWith("BEAST");
        gameFacade.playWords(game3, "CLEAN", "CLEAR", "QUEEN", "BEAST");

        String game4 = gameFacade.newGameWith("BEAST");
        gameFacade.playWords(game4, "CLEAN", "QUEEN", "QUEST", "CRONE", "CREST", "CROWN");

        List<PlayerScore> scores = gameFacade.getLeaderboardScores();

        assertThat(scores).isNotEmpty();

        PlayerScore score = scores.stream().filter(s -> s.name().equals(testPlayer.name())).findFirst().get();
        assertThat(score.name()).isEqualTo(testPlayer.name());
        assertThat(score.countryName()).isEqualTo("United Kingdom");
        assertThat(score.successRate()).isEqualTo(75);
        assertThat(score.totalTries()).isEqualTo(4);
    }
}
