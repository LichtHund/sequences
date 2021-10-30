package dev.triumphteam.sequences.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public final class EmptyIterator<T> implements ListIterator<T> {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't remove from empty iterator.");
    }

    @Override
    public void forEachRemaining(final Consumer<? super T> action) {
        ListIterator.super.forEachRemaining(action);
    }

    @Override
    public void set(@NotNull final Object o) {
        throw new UnsupportedOperationException("Can't set to empty iterator.");
    }

    @Override
    public void add(final Object o) {
        throw new UnsupportedOperationException("Can't add to empty iterator.");
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public T previous() {
        throw new NoSuchElementException();
    }


}
