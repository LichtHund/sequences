package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.BaseSequence;
import dev.triumphteam.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public final class TransformingSequence<T, R> extends BaseSequence<R> {

    private final Sequence<T> sequence;
    private final Function<T, R> transformer;

    public TransformingSequence(@NotNull final Sequence<T> sequence, @NotNull final Function<T, R> transformer) {
        this.sequence = sequence;
        this.transformer = transformer;
    }

    @NotNull
    @Override
    public Iterator<R> getIterator() {
        return new Iterator<R>() {
            private final Iterator<T> iterator = sequence.getIterator();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return transformer.apply(iterator.next());
            }
        };
    }
}
