package com.serenitydojo.wordle.model;

import org.junit.jupiter.api.Test;
import com.serenitydojo.wordle.domain.CheckersGame;
import com.serenitydojo.wordle.domain.ChessGame;
import com.serenitydojo.wordle.domain.Game;
import com.serenitydojo.wordle.domain.GoGame;

import static org.assertj.core.api.Assertions.assertThat;

public class WhenUsingJavaOOFeatures {

    @Test
    void creatingAnObject() {
        Game game = new ChessGame();

        assertThat(game.getName()).isEqualTo("Chess");
    }

    @Test
    void aGameCanHaveANumberOfPieces() {
        Game game = new ChessGame();

        assertThat(game.getInitialNumberOfPieces()).isEqualTo(16);
    }

    @Test
    void checkersUses20piecesPerSide() {
        Game game = new CheckersGame();

        assertThat(game.getInitialNumberOfPieces()).isEqualTo(20);
    }

    @Test
    void goDoesNotHaveALimitOnNumberOfPieces() {
        Game gameOfGo = new GoGame();

        assertThat(gameOfGo.getInitialNumberOfPieces()).isEqualTo(0);
    }

    @Test
    void shouldReportNumberOfPiecesInTheGame() {
        Game checkersGame = new CheckersGame();
        Game chessGame = new ChessGame();
        Game goGame = new GoGame();

        String checkers = checkersGame.describe();
        String chess = chessGame.describe();
        String go = goGame.describe();

        assertThat(checkers).isEqualTo("Checkers starts with 20 pieces");
        assertThat(chess).isEqualTo("Chess starts with 16 pieces");
        assertThat(go).isEqualTo("Go starts with 0 pieces and has no limit on the number of pieces");
    }
}
