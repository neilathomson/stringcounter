package dev.neilthomson.stringcounter;

import dev.neilthomson.stringcounter.comparators.CountAndAlphaComparator;
import dev.neilthomson.stringcounter.services.SimpleParallelWordCounter;
import dev.neilthomson.stringcounter.services.WordCounter;

import java.io.IOException;
import java.io.InputStream;

public class Profiler {

    public static void main(String[] args) {
        Profiler app = new Profiler();
        app.run();
    }

    private void run() {
        for (int i = 0; i < 20; i++) {
//            profileWordCounter(new BufferedWordCounter(64));
//            profileWordCounter(new SimpleWordCounter());
            profileWordCounter(new SimpleParallelWordCounter(), i >= 10);
        }
    }

    private void profileWordCounter(WordCounter wordCounter, boolean skip) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream("test.txt")) {
            long startTime = System.nanoTime();
            wordCounter.count(inputStream, new CountAndAlphaComparator(false));
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            if (!skip)
                System.out.printf("%s: %d nanoseconds\n", wordCounter.getClass().getSimpleName(), duration);
        } catch (IOException e) {
            System.err.println("Error processing input: " + e.getMessage());
        }
    }

}
