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

    public static void checkWindowSizeStep(final int size, final int step) {
        if (size <= 0 || step <= 0) {
            if (size != step) throw new IllegalArgumentException("Both size " + size + " and step " + step + " must be greater than zero.");
            throw new IllegalArgumentException("size " + size + " must be greater than zero.");
        }
    }

    public static int getProgressionLastElement(final int start, final int end, final int step) {
        if (step > 0) {
            if (start >= end) return end;
            return end - differenceModulo(end, start, step);
        }

        if (step < 0) {
            if (start <= end) return end;
            return end - differenceModulo(start, end, -step);
        }

        throw new IllegalArgumentException("Step is zero.");
    }

    private static int differenceModulo(final int a, final int b, final int c) {
        return mod(mod(a, c) - mod(b, c), c);
    }

    private static int mod(final int a, final int b) {
        final int mod = a % b;
        if (mod >= 0) return mod;
        return mod + b;
    }

}
