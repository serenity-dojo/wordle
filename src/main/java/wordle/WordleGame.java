package wordle;

import wordle.model.CellColor;
import wordle.model.RenderedCell;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private final String targetWord;
    List<List<CellColor>> stateOfPlay = new ArrayList<>();

    public WordleGame(String targetWord) {
        this.targetWord = targetWord;
    }

    public void play(String attempt) {
        List<CellColor> renderedAttempt = new ArrayList<>();
        for(int pos = 0; pos < attempt.length(); pos++) {
            char letter = attempt.charAt(pos);
            CellColor cellColor = RenderedCell.forTargetWord(targetWord)
                                              .forEntry(attempt, pos);
            renderedAttempt.add(cellColor);
        }

        stateOfPlay.add(renderedAttempt);
    }

    public List<List<CellColor>> getRenderedCells() {
        return stateOfPlay;
    }
}
