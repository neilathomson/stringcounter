package dev.neilthomson.stringcounter.comparators;

import java.util.Comparator;
import java.util.Map;

public class CountAndAlphaComparator implements Comparator<Map.Entry<String, Long>> {

    private final boolean ascending;

    public CountAndAlphaComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(Map.Entry<String, Long> e1, Map.Entry<String, Long> e2) {
        // Compare counts first
        int comparison = Long.compare(e1.getValue(), e2.getValue());
        if (comparison != 0) {
            // If counts differ, return the count comparison
            return ascending ? comparison : -comparison;
        } else {
            // If counts are equal, compare alphabetically (case-insensitive)
            return e1.getKey().compareToIgnoreCase(e2.getKey());
        }
    }
}
