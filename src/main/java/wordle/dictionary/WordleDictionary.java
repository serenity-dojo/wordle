package wordle.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class WordleDictionary {

    private final Set<String> words;

    public WordleDictionary() {
        InputStream dictionaryFile = getClass().getResourceAsStream("/dictionary/dictionary.txt");
        assert dictionaryFile != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dictionaryFile))) {
            words = reader.lines().map(String::toUpperCase).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException("Dictionary could not be loaded: " + e.getMessage(), e);
        }
    }

    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }
}
