package dev.neilthomson.stringcounter.services;

import dev.neilthomson.stringcounter.comparators.CountAndAlphaComparator;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public interface WordCounterTest<T extends WordCounter> {

    T createWordCounter();

    @Test
    default void processStreamShouldSucceed() {
        final WordCounter wordCounter = createWordCounter();
        final ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("test.txt")) {
            assertNotNull(inputStream, "test.txt not found in resources");
            final List<Map.Entry<String, Long>> counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
            assertNotNull(counts, "The returned counts should not be null");
            assertEquals(97L, counts.size(), "The counts should be correct");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    default void punctuationShouldBeRemoved() {
        final WordCounter wordCounter = createWordCounter();
        final String myString = "Word.";
        final byte[] bytes = myString.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            try {
                final List<Map.Entry<String, Long>> counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
                assertEquals(1L, counts.size(), "The counts should have a single instance");
                assertEquals("word", counts.get(0).getKey(), "It did not remove punctuation");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    default void shouldIncludeApostrophesInWords() {
        final WordCounter wordCounter = createWordCounter();
        final String myString = "peoples peoples' people's";
        final byte[] bytes = myString.getBytes(StandardCharsets.UTF_8); // Encode string to bytes
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            try {
                final List<Map.Entry<String, Long>> counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
                assertEquals(3L, counts.size(), "The counts should contain the correct items for apostrophes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    default void shouldIncludeHyphensInWords() {
        final WordCounter wordCounter = createWordCounter();
        final String myString = "check-mark check mark";
        final byte[] bytes = myString.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            List<Map.Entry<String, Long>> counts = null;
            try {
                counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
                assertEquals(3L, counts.size(), "The counts should contain the correct items for hyphens");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            fail("Unexpected IOException occurred while closing the stream: " + e.getMessage());
        }
    }

    @Test
    default void shouldUseUTF8Characters() {
        final WordCounter wordCounter = createWordCounter();
        final ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("german.txt")) {
            List<Map.Entry<String, Long>> counts = null;
            try {
                counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
                assertNotNull(counts, "map is null");
                assertEquals(5L, counts.size(), "The counts should contain the correct items for all UTF-8 characters");
                assertEquals("gr\u00fc\u00dfen", counts.get(2).getKey(), "UTF-8 character should be considered");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            fail("Unexpected IOException occurred while closing the stream: " + e.getMessage());
        }
    }

    @Test
    default void shouldThrowExceptionForNullInputStream() {
        final WordCounter wordCounter = createWordCounter();
        assertThrows(IllegalArgumentException.class, () -> wordCounter.count(null, new CountAndAlphaComparator(false)));
    }

    @Test
    default void shouldThrowExceptionForNullComparator() {
        final WordCounter wordCounter = createWordCounter();
        final String myString = "";
        final byte[] bytes = myString.getBytes(StandardCharsets.UTF_8);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            assertThrows(IllegalArgumentException.class, () -> wordCounter.count(inputStream, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    default void shouldWorkConcurrently() {
        final WordCounter wordCounter = createWordCounter();
        final String myString = "word word word";
        final byte[] bytes = myString.getBytes(StandardCharsets.UTF_8);

        ConcurrentHashMap<Integer, Long> map = new ConcurrentHashMap<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
                    List<Map.Entry<String, Long>> counts;
                    counts = wordCounter.count(inputStream, new CountAndAlphaComparator(false));
                    map.put(finalI, counts.get(0).getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            threads.get(i).start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Unexpected InterruptedException occurred: " + e.getMessage());
            }
        }
        assertEquals(5, map.size(), "It did not run the correct amount of times");
        map.values().forEach((v) -> assertEquals(3L, v, "It did not count the correct number of words"));
    }
}
