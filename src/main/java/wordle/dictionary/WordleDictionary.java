package wordle.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class WordleDictionary {

    private final Set<String> words;

    public WordleDictionary() {

        words = new HashSet<>();
        InputStream dictionaryFile = getClass().getResourceAsStream("/dictionary/dictionary.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dictionaryFile))) {
            while(reader.ready()) {
                words.add(reader.readLine().toUpperCase());
            }
        } catch (IOException e) {
            throw new IllegalStateException("No dictionary found: " + e.getMessage(), e);
        }
    }

    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }
}
