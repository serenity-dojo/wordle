package wordle.microservice.game;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wordle.WordleGame;
import wordle.dictionary.WordleDictionary;
import wordle.model.CellColor;
import wordle.model.GameResult;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/api/game", method = POST)
    @Operation(description = "Start a new Wordle game with a random word")
    public Long newGame() {
        return gameService.newGame();
    }

    @RequestMapping(value = "/api/game/{id}/word", method = POST)
    @Operation(description = "Attempt a new word")
    public List<List<CellColor>> play(@PathVariable long id, @RequestBody String word) {
        gameService.play(id, word);
        return gameService.getHistory(id);
    }

    @RequestMapping(value = "/api/game/{id}/history", method = GET)
    @Operation(description = "Find the current history of moves of the game")
    public List<List<CellColor>> get(@PathVariable long id) {
        return gameService.getHistory(id);
    }

    @RequestMapping(value = "/api/game/{id}/result", method = GET)
    @Operation(description = "Find the current result of the game")
    public GameResult getResult(@PathVariable long id) {
        return gameService.getResult(id);
    }

    @RequestMapping(value = "/api/game/{id}/answer", method = GET)
    @Operation(description = "Find the answer (only available after the game is lost)")
    public String getAnswer(@PathVariable long id) {
        return gameService.revealAnswer(id);
    }
}
