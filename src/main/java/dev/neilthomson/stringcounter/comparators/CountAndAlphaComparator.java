package dev.neilthomson.stringcounter.comparators;

import java.util.Comparator;
import java.util.Map;

/**
 * A Comparator implementation for comparing Map entries by their associated values (count) and keys alphabetically.
 * This comparator sorts entries primarily by their values in ascending or descending order, and in case of tie,
 * by their keys alphabetically in a case-insensitive manner.
 */
public class CountAndAlphaComparator implements Comparator<Map.Entry<String, Long>> {

    /**
     * Indicates whether the comparison should be in ascending or descending order.
     */
    private final boolean ascending;

    /**
     * Constructs a CountAndAlphaComparator with the specified ordering direction.
     *
     * @param ascending {@code true} for ascending order, {@code false} for descending order.
     */
    public CountAndAlphaComparator(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Compares two Map entries by their associated values (count) and keys alphabetically.
     *
     * @param e1 the first Map entry to be compared
     * @param e2 the second Map entry to be compared
     * @return a negative integer, zero, or a positive integer as the first entry's value is less than, equal to,
     *         or greater than the second entry's value. If the values are equal, returns a comparison result based
     *         on the alphabetical order of the keys.
     */
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

