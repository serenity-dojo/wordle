package com.serenitydojo.wordle.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Hints {
    private final String targetWord;

    List<Hint> hints = Arrays.asList(
            (word) -> "The word starts with the letter " + word.charAt(0),
            (word) -> "The word ends with the letter " + word.charAt(word.length() - 1),
            new NumberOfVowelsHint()
    );

    public Hints(String targetWord) {
        this.targetWord = targetWord;
    }

    public List<String> allHints() {
        return hints.stream().map(hint -> hint.getHint(targetWord)).toList();
    }

    public List<String> randomHint() {
        List<String> allHints = allHints();
        Random rand = new Random();
        return Collections.singletonList(allHints.get(rand.nextInt(allHints.size())));
    }
}
