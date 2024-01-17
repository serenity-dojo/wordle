package com.serenitydojo.wordle.model;

import com.serenitydojo.wordle.dictionary.WordleDictionary;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private final String targetWord;
    private final WordleDictionary dictionary;

    List<List<CellColor>> stateOfPlay = new ArrayList<>();
    private boolean firstHint = true;

    public static WordleGame withRandomWord(WordleDictionary dictionary) {
        return new WordleGame(dictionary.random(), dictionary);
    }

    public static WordleGame withSpecifiedWord(String targetWord, WordleDictionary dictionary) {
        return new WordleGame(targetWord, dictionary);
    }

    public WordleGame(String targetWord, WordleDictionary dictionary) {
        this.targetWord = targetWord;
        this.dictionary = dictionary;
    }

    public GameResult play(String attemptedWord) {
        ensureGameIsNotOver();
        ensureWordIsValidFor(attemptedWord);

        List<CellColor> renderedAttempt = new ArrayList<>();
        for (int pos = 0; pos < attemptedWord.length(); pos++) {
            CellColor cellColor = RenderedCell.forTargetWord(targetWord).forEntry(attemptedWord, pos);
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
        if (getResult() != GameResult.IN_PROGRESS) {
            throw new IllegalPlayException("Game over");
        }
    }

    private GameResult resultFrom(List<List<CellColor>> stateOfPlay) {
        if (stateOfPlay.isEmpty()) {
            return GameResult.IN_PROGRESS;
        } else if (isCorrect(stateOfPlay.get(stateOfPlay.size() - 1))) {
            return GameResult.WIN;
        } else if (stateOfPlay.size() >= 6) {
            return GameResult.LOSE;
        } else {
            return GameResult.IN_PROGRESS;
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
        if (resultFrom(stateOfPlay) == GameResult.IN_PROGRESS) {
            throw new IllegalAttemptToShowAnswerException("Don't cheat!");
        }
        return targetWord;
    }

    public List<String> requestHint() {
        if (firstHint) {
            firstHint = false;
            return new Hints(targetWord).randomHint();
        } else {
            return allHints();
        }
    }

    public List<String> allHints() {
        return new Hints(targetWord).allHints();
    }

}
