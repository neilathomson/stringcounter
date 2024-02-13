package dev.neilthomson.stringcounter.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implements the {@link WordCounter} interface to provide parallel word counting functionality.
 * <p>
 * This class efficiently counts word occurrences within a text stream, utilizing parallel
 * streams for improved performance. It leverages regular expressions for word splitting and
 * punctuation removal, and performs case-insensitive counting. Additionally, it allows customizing
 * the sorting of results based on different criteria.
 */
public class SimpleParallelWordCounter implements WordCounter {

    private static final Pattern WORD_PATTERN = Pattern.compile("\\s+");
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\p{Punct}&&[^'-]]+");

    @Override
    public List<Map.Entry<String, Long>> count(InputStream inputStream, Comparator<Map.Entry<String, Long>> comparator) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }

        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .parallel()
                    .flatMap(WORD_PATTERN::splitAsStream) // Split directly using regex
                    .map(word -> PUNCTUATION_PATTERN.matcher(word).replaceAll("").toLowerCase(Locale.ENGLISH))
                    .filter(word -> !word.isEmpty())
                    .collect(Collectors.groupingByConcurrent(Function.identity(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
    }
}
