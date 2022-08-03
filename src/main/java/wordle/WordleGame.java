package wordle;

import wordle.dictionary.WordleDictionary;
import wordle.model.CellColor;
import wordle.model.RenderedCell;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private final String targetWord;
    private final WordleDictionary dictionary = new WordleDictionary();

    List<List<CellColor>> stateOfPlay = new ArrayList<>();

    public WordleGame(String targetWord) {
        this.targetWord = targetWord;
    }

    public void play(String attempt) {
        if (!dictionary.contains(attempt)) {
            throw new InvalidWordleWordException("'" + attempt + "' is not a valid word");
        }

        List<CellColor> renderedAttempt = new ArrayList<>();
        for(int pos = 0; pos < attempt.length(); pos++) {
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
