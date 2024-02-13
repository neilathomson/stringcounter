package dev.neilthomson.stringcounter;

import dev.neilthomson.stringcounter.comparators.CountAndAlphaComparator;
import dev.neilthomson.stringcounter.services.WordCounter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ApplicationTest {

    @Mock
    private WordCounter wordCounterMock;

    @InjectMocks
    private Application application;

    @Test
    public void shouldOpenFileStream() throws IOException {
        List<Map.Entry<String, Long>> mockCounts = new ArrayList<>();
        mockCounts.add(new AbstractMap.SimpleEntry<>("word", 2L));
        Mockito.when(wordCounterMock.count(any(InputStream.class), any(CountAndAlphaComparator.class))).thenReturn(mockCounts);
        application.runFromFile("src/test/resources/test.txt");
        Mockito.verify(wordCounterMock).count(any(InputStream.class), any(CountAndAlphaComparator.class));
    }

    @Test
    public void shouldThrowIOExceptionIfInvalidFile() throws IOException {
        assertThrows(IOException.class, () -> application.runFromFile("invalid_file.txt"));
    }

}
