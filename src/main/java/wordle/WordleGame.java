package wordle;

import wordle.dictionary.WordleDictionary;
import wordle.model.*;

import java.util.ArrayList;
import java.util.List;

import static wordle.model.GameResult.*;

public class WordleGame {
    private final String targetWord;
    private final WordleDictionary dictionary;

    List<List<CellColor>> stateOfPlay = new ArrayList<>();

    public static WordleGame withRandomWord(WordleDictionary dictionary) {
        return new WordleGame(dictionary.random(), dictionary);
    }

    public WordleGame(String targetWord, WordleDictionary dictionary) {
        this.targetWord = targetWord;
        this.dictionary = dictionary;
    }

    public GameResult play(String attemptedWord) {
        ensureGameIsNotOver();
        ensureWordIsValidFor(attemptedWord);

        String normalizedWord = attemptedWord.toUpperCase();

        List<CellColor> renderedAttempt = new ArrayList<>();
        for(int pos = 0; pos < normalizedWord.length(); pos++) {
            CellColor cellColor = RenderedCell.forTargetWord(targetWord)
                                              .forEntry(normalizedWord, pos);
            renderedAttempt.add(cellColor);
        }

        stateOfPlay.add(renderedAttempt);
        return resultFrom(stateOfPlay);
    }

    private void ensureWordIsValidFor(String attemptedWord) {
        if (!dictionary.contains(attemptedWord)) {
            throw new IllegalPlayException("'" + attemptedWord + "' is not a valid word");
        }
    }

    private void ensureGameIsNotOver() {
        if (getResult() != IN_PROGRESS) {
            throw new IllegalPlayException("Game over");
        }
    }

    private GameResult resultFrom(List<List<CellColor>> stateOfPlay) {
        if (stateOfPlay.isEmpty()) {
            return IN_PROGRESS;
        } else if (isCorrect(stateOfPlay.get(stateOfPlay.size() - 1))) {
            return WIN;
        } else if (stateOfPlay.size() >= 6) {
            return LOSE;
        } else {
            return IN_PROGRESS;
        }
    }

    private boolean isCorrect(List<CellColor> cellColors) {
        return cellColors.stream().allMatch(cell -> cell == CellColor.GREEN);
    }

    public List<List<CellColor>> getRenderedCells() {
        return stateOfPlay;
    }

    public GameResult getResult() {
        return resultFrom(stateOfPlay);
    }

    public String revealAnswer() {
        if (resultFrom(stateOfPlay) == IN_PROGRESS) {
            throw new IllegalAttemptToShowAnswerException("Don't cheat!");
        }
        return targetWord;
    }
}
