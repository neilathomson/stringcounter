package dev.neilthomson.stringcounter.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Defines an interface for counting words in an input stream.
 */
public interface WordCounter {

    /**
     * Counts the occurrences of words in the provided input stream and returns a sorted list of word-count entries.
     *
     * @param inputStream The input stream containing the text to be analyzed in UTF-8 character encoding.
     * @param comparator A comparator used to sort the word-count entries. The comparator accepts two
     *                   {@link java.util.Map.Entry} objects with keys of type {@link String} (word) and values of type
     *                   {@link Long} (count). It should return a negative value if the first entry should come before
     *                   the second, 0 if they are equal, and a positive value if the first entry should come after the
     *                   second. If null is provided, an IllegalArgumentException will be thrown.
     * @return A sorted list of {@link java.util.Map.Entry} objects, where each entry has a key of type
     *         {@link String} (word) and a value of type {@link Long} (count). The list is sorted according to the provided
     *         comparator, or by count in descending order and then alphabetically by word if no comparator is provided.
     * @throws IOException If an error occurs while reading from the input stream.
     */
    List<Map.Entry<String, Long>> count(InputStream inputStream, Comparator<Map.Entry<String, Long>> comparator) throws IOException, IllegalArgumentException;
}
