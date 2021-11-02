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

    // TODO: 10/30/2021 Check what this does in kotlin because it's a `expect` function
    public static int checkIndexOverflow(final int index) {
        return index;
    }

}
