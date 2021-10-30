package dev.triumphteam.sequences.util;

public final class SequenceUtils {

    public static int coerceAtMost(final int initialValue, final int maximumValue) {
        return Math.min(initialValue, maximumValue);
    }

    public static Object[] arrayOfNulls(final int capacity) {
        final Object[] array = new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = null;
        }
        return array;
    }

    public static int forward(final int start, final int n, final int capacity) {
        return (start + n) % capacity;
    }

}
