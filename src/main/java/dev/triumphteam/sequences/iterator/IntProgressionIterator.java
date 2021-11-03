package dev.triumphteam.sequences.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IntProgressionIterator implements Iterator<Integer> {
    private final int step;
    private final int finalElement;
    private boolean hasNext;
    private int next;

    public IntProgressionIterator(final int first, final int last, final int step) {
        this.step = step;

        this.finalElement = last;
        this.hasNext = step > 0 ? first <= last : first >= last;
        this.next = hasNext ? first : this.finalElement;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Integer next() {
        final int value = next;
        if (value == finalElement) {
            if (!hasNext) throw new NoSuchElementException();
            hasNext = false;
            return value;
        }

        next += step;
        return value;
    }

}
