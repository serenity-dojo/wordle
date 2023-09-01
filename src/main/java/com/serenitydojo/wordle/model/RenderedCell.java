package com.serenitydojo.wordle.model;

public class RenderedCell {
    private final String targetWord;

    public RenderedCell(String targetWord) {
        this.targetWord = targetWord;
    }

    public static RenderedCell forTargetWord(String targetWord) {
        return new RenderedCell(targetWord);
    }

    public CellColor forEntry(String proposedWord, int position) {
        char letter = proposedWord.charAt(position);
        int numberOfMatches = countMatches(letter, targetWord);

        if (Character.toUpperCase(targetWord.charAt(position)) == Character.toUpperCase(letter)) {
            return CellColor.GREEN;
        } else if (targetWord.contains(Character.toString(letter))) {

            int proposedLetterCount = 0;
            for(int i = 0; i <= position; i++) {
                if (proposedWord.charAt(i) == letter) {
                    proposedLetterCount++;
                }
            }
            if (proposedLetterCount <= numberOfMatches) {
                return CellColor.YELLOW;
            } else {
                return CellColor.GRAY;
            }
        } else if (!targetWord.contains(Character.toString(letter))) {
            return CellColor.GRAY;
        }
        return null;
    }

    private int countMatches(char letter, String word) {
        int numberOfMatches = 0;
        for(int pos = 0; pos < word.length(); pos++) {
            if (word.charAt(pos) == letter) {
                numberOfMatches++;
            }
        }
        return numberOfMatches;
    }

}
