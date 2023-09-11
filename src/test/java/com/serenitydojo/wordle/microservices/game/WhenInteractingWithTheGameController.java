package com.serenitydojo.wordle.microservices.game;

import com.serenitydojo.wordle.model.CellColor;
import com.serenitydojo.wordle.model.GameResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WhenInteractingWithTheGameController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @Test
    @DisplayName("Start a new Wordle game with a random word")
    void shouldStartNewRandomWordGame() throws Exception {
        when(gameService.newGame()).thenReturn(1L);

        mockMvc.perform(post("/api/game"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    private static final List<CellColor> A_ROW_WITH_SOME_CORRECT_GUESSES
            = Arrays.asList(CellColor.GRAY, CellColor.GREEN, CellColor.GREEN, CellColor.GREEN, CellColor.YELLOW);

    @Test
    @DisplayName("Attempt a new word")
    void shouldAttemptNewWord() throws Exception {
        long gameId = 1L;
        String word = "hello";
        List<List<CellColor>> mockHistory = Arrays.asList(A_ROW_WITH_SOME_CORRECT_GUESSES);

        when(gameService.play(gameId, word)).thenReturn(GameResult.IN_PROGRESS);
        when(gameService.getHistory(gameId)).thenReturn(mockHistory);

        mockMvc.perform(post("/api/game/{id}/word", gameId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(word))
                .andExpect(status().isCreated());
    }


}
