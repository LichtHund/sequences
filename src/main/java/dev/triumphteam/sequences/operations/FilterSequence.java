package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.AbstractSequence;
import dev.triumphteam.sequences.BaseSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public final class FilterSequence<T> extends AbstractSequence<T> {

    private final BaseSequence<T> sequence;
    private final boolean sendWhen;
    private final Predicate<T> predicate;

    public FilterSequence(@NotNull final BaseSequence<T> sequence, final boolean sendWhen, @NotNull final Predicate<T> predicate) {
        this.sequence = sequence;
        this.sendWhen = sendWhen;
        this.predicate = predicate;
    }

    public FilterSequence(@NotNull final BaseSequence<T> sequence, @NotNull final Predicate<T> predicate) {
        this(sequence, true, predicate);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iterator = sequence.iterator();
            private int nextState = -1; // -1 for unknown, 0 for done, 1 for continue
            private T nextItem = null;

            @Override
            public boolean hasNext() {
                if (nextState == -1) calcNext();
                return nextState == 1;
            }

            @Override
            public T next() {
                if (nextState == -1) calcNext();
                if (nextState == 0) throw new NoSuchElementException();

                final T result = nextItem;
                nextItem = null;
                nextState = -1;
                return result;
            }

            private void calcNext() {
                while (iterator.hasNext()) {
                    final T item = iterator.next();
                    if (predicate.test(item) == sendWhen) {
                        nextItem = item;
                        nextState = 1;
                        return;
                    }
                }

                nextState = 0;
            }
        };
    }

}
