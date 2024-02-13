package dev.neilthomson.stringcounter.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BufferedWordCounter implements WordCounter {

    private final int bufferSize;

    public BufferedWordCounter(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public List<Map.Entry<String, Long>> count(InputStream inputStream, Comparator<Map.Entry<String, Long>> comparator) throws IOException {
        if (inputStream == null)
            throw new IllegalArgumentException("InputStream cannot be null");

        if (comparator == null)
            throw new IllegalArgumentException("Comparator cannot be null");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            char[] buffer = new char[bufferSize];
            Map<String, Long> wordCounts = new HashMap<>();
            int numRead;
            StringBuilder currentWord = new StringBuilder();
            boolean buildingWord = false; // Track if currently building a word

            while ((numRead = reader.read(buffer)) != -1) {
                for (int i = 0; i < numRead; i++) {
                    char c = buffer[i];
                    if (Character.isAlphabetic(c) || c == '\'' || c == '-') { // Character is part of a word
                        currentWord.append(Character.toLowerCase(c));
                        buildingWord = true;
                    } else if (buildingWord) { // End of word encountered
                        String word = currentWord.toString();
                        currentWord.setLength(0); // Reset for next word
                        buildingWord = false;
                        wordCounts.merge(word, 1L, Long::sum); // Update word count in the map
                    }
                }
            }

            if (buildingWord) // flush currentWord if stream end reached
                wordCounts.merge(currentWord.toString(), 1L, Long::sum);

            return wordCounts.entrySet().stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
    }
}
