package dev.triumphteam.sequences.iterator;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<T> implements Iterator<T> {

    private State state = State.NOT_READY;
    private T nextValue = null;

    @Override
    public boolean hasNext() {
        if (state == State.FAILED) {
            throw new IllegalArgumentException("Failed requirement.");
        }

        switch (state) {
            case DONE:
                return false;
            case READY:
                return true;
            default:
                return tryToComputeNext();
        }
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        state = State.NOT_READY;
        return nextValue;
    }

    protected void setNext(@Nullable final T value) {
        nextValue = value;
        state = State.READY;
    }

    protected void done() {
        state = State.DONE;
    }

    abstract protected void computeNext();

    private boolean tryToComputeNext() {
        state = State.FAILED;
        computeNext();
        return state == State.READY;
    }

    private enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

}
