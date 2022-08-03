package wordle.dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class WordleDictionary {

    private final Set<String> words;

    public WordleDictionary() {
        String dictionaryPath = getClass().getClassLoader().getResource("dictionary.txt").getPath();
        try {
            words = Files.lines(Paths.get(dictionaryPath)).map(String::toUpperCase).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException("No dictionary found: " + e.getMessage());
        }
    }

    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }
}
