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

public class Application {

    private static final int BUFFER_SIZE = 512;
    private static boolean DEBUG = false;

    private final WordCounter wordCounter;

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

    private Application(WordCounter wordCounter) {
        this.wordCounter = wordCounter;
    }

    public List<Map.Entry<String, Long>> runFromFile(String filename) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filename))) {
            return wordCounter.count(inputStream, new CountAndAlphaComparator(false));
        }
    }
}

