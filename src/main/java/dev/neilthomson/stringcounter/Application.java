package dev.neilthomson.stringcounter;

import dev.neilthomson.stringcounter.comparators.CountAndAlphaComparator;
import dev.neilthomson.stringcounter.services.BufferedWordCounter;
import dev.neilthomson.stringcounter.services.WordCounter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Main application class for word counting program.
 * <p>
 * This class takes a filename and optionally a debug flag as arguments. It utilizes a provided
 * {@link WordCounter} implementation to count word occurrences in the specified file and prints
 * the results.
 */
public class Application {

    private static final int BUFFER_SIZE = 512;
    private static boolean DEBUG = false;

    private final WordCounter wordCounter;

    /**
     * Main entry point for the application.
     *
     * @param args Command-line arguments:
     *             - <filename>: The file containing the text to be processed.
     *             - [--debug]: Optional flag to enable debug output.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Application <filename> [--debug]");
            return;
        }

        for (String arg : args) {
            if ("--debug".equals(arg)) {
                DEBUG = true;
                break;
            }
        }

        Application app = new Application(new BufferedWordCounter(BUFFER_SIZE));
        String filename = args[0];
        try {
            List<Map.Entry<String, Long>> counts = app.runFromFile(filename);
            counts.forEach((c) -> System.out.printf("%s: %d\n", c.getKey(), c.getValue()));
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        } catch (Exception t) {
            if (DEBUG) {
                t.printStackTrace();
            } else {
                System.out.println("Encountered an error and had to exit");
            }
        }
    }

    /**
     * Creates an instance of the Application class with the given WordCounter implementation.
     *
     * @param wordCounter The WordCounter instance to use for counting words.
     */
    private Application(WordCounter wordCounter) {
        this.wordCounter = wordCounter;
    }

    /**
     * Reads text from the specified file, counts word occurrences using the wordCounter, and returns the results.
     *
     * @param filename The path to the file to read.
     * @return A List of Map.Entry objects where each entry represents a word and its count.
     * @throws IOException If an error occurs while reading the file.
     */
    public List<Map.Entry<String, Long>> runFromFile(String filename) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename))) {
            return wordCounter.count(inputStream, new CountAndAlphaComparator(false));
        }
    }
}

