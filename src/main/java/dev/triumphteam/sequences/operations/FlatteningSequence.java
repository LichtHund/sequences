package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.AbstractSequence;
import dev.triumphteam.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class FlatteningSequence<T, R, E> extends AbstractSequence<E, Sequence<E>> {

    private final Sequence<T> sequence;
    private final Function<T, R> transformer;
    private final Function<R, Iterator<E>> iteratorFunction;

    public FlatteningSequence(
            @NotNull final Sequence<T> sequence,
            @NotNull final Function<T, R> transformer,
            @NotNull final Function<R, Iterator<E>> iteratorFunction
    ) {
        this.sequence = sequence;
        this.transformer = transformer;
        this.iteratorFunction = iteratorFunction;
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<T> iterator = sequence.iterator();
            Iterator<E> itemIterator = null;

            @Override
            public E next() {
                if (!ensureItemIterator()) throw new NoSuchElementException();
                return itemIterator.next();
            }

            @Override
            public boolean hasNext() {
                return ensureItemIterator();
            }

            private boolean ensureItemIterator() {
                if (itemIterator != null && !itemIterator.hasNext()) itemIterator = null;

                while (itemIterator == null) {
                    if (!iterator.hasNext()) {
                        return false;
                    } else {
                        final T element = iterator.next();
                        final Iterator<E> nextItemIterator = iteratorFunction.apply(transformer.apply(element));
                        if (nextItemIterator.hasNext()) {
                            itemIterator = nextItemIterator;
                            return true;
                        }
                    }
                }
                return true;
            }
        };
    }
}
