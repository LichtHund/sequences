package dev.triumphteam.sequences.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class CharProgressionIterator implements Iterator<Character> {
    private final int step;
    private final char finalElement;
    private boolean hasNext;
    private char next;

    public CharProgressionIterator(final char first, final char last, final int step) {
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
    public Character next() {
        final char value = next;
        if (value == finalElement) {
            if (!hasNext) throw new NoSuchElementException();
            hasNext = false;
            return value;
        }

        next += step;
        return value;
    }

}
