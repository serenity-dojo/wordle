package com.serenitydojo.wordle.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NumberOfVowelsHint implements Hint {

    private static final List<Character> VOWELS = Arrays.asList('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U');

    @Override
    public String getHint(String word) {
        long numberOfVowels = countVowelsIn(word);
        if (numberOfVowels == 0) {
            return "The word contains no vowels";
        } else if (numberOfVowels == 1) {
            return "The word contains 1 vowel";
        } else {
            return "The word contains " + numberOfVowels + " vowels";
        }
    }

    private long countVowelsIn(String word) {
        return word.chars()
                .mapToObj(i -> (char) i)
                .filter(VOWELS::contains)
                .count();
    }
}
