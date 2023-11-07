package com.serenitydojo.wordle.model;

public class RenderedCell {
    private final String targetWord;

    public RenderedCell(String targetWord) {
        this.targetWord = targetWord.toLowerCase(); // Convert to lower-case once
    }

    public static RenderedCell forTargetWord(String targetWord) {
        return new RenderedCell(targetWord);
    }

    public CellColor forEntry(String proposedWord, int position) {
        char letter = proposedWord.charAt(position);
        String letterStr = Character.toString(letter).toLowerCase(); // Convert to string once

        if (isExactMatch(letterStr, position)) {
            return CellColor.GREEN;
        } else if (targetWord.contains(letterStr)) {
            return determineColorForContainedLetter(proposedWord, letterStr, position);
        } else {
            return CellColor.BLACK;
        }
    }

    private boolean isExactMatch(String letter, int position) {
        return targetWord.charAt(position) == letter.charAt(0);
    }

    private CellColor determineColorForContainedLetter(String proposedWord, String letter, int position) {
        long proposedLetterCount = proposedWord.toLowerCase().substring(0, position + 1)
                .chars()
                .filter(c -> c == letter.charAt(0))
                .count();
        long numberOfMatches = targetWord.chars()
                .filter(c -> c == letter.charAt(0))
                .count();

        return proposedLetterCount <= numberOfMatches ? CellColor.YELLOW : CellColor.BLACK;
    }

}
