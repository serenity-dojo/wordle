package wordle.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The collection of all valid words
 */
public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary() {
        InputStream dictionaryFile = getClass().getResourceAsStream("/dictionary/dictionary.txt");
        assert dictionaryFile != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dictionaryFile))) {
            words = reader.lines().map(String::toUpperCase).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Dictionary could not be loaded: " + e.getMessage(), e);
        }
    }

    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }

    public String random() {
        int wordNumber = new Random().nextInt(words.size());
        return words.get(wordNumber);
    }
}
