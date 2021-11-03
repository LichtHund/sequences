package dev.triumphteam.sequences.range;

import org.jetbrains.annotations.NotNull;

public interface ClosedRange<T extends Comparable<T>> {

    /**
     * The minimum value in the range.
     */
    T getStart();

    /**
     * The maximum value in the range (inclusive).
     */
    T getEndInclusive();

    /**
     * Checks whether the specified [value] belongs to the range.
     */
   default boolean contains(@NotNull final T value) {
       return value.compareTo(getStart()) >= 0 && value.compareTo(getEndInclusive()) <= 0;
   }

    /**
     * Checks whether the range is empty.
     *
     * The range is empty if its start value is greater than the end value.
     */
    default boolean isEmpty() {
        return getStart().compareTo(getEndInclusive()) > 0;
    }

}
