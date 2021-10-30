package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.BaseSequence;
import dev.triumphteam.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;

public final class TransformingIndexedSequence<T, R> extends BaseSequence<R> {
    private final Sequence<T> sequence;
    private final BiFunction<Integer, T, R> transformer;

    public TransformingIndexedSequence(@NotNull final Sequence<T> sequence, @NotNull final BiFunction<Integer, T, R> transformer) {
        this.sequence = sequence;
        this.transformer = transformer;
    }

    @NotNull
    @Override
    public Iterator<R> iterator() {
        return new Iterator<R>() {
            private final Iterator<T> iterator = sequence.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return transformer.apply(checkIndexOverflow(index++), iterator.next());
            }
        };
    }

    // TODO: 10/30/2021 Check what this does in kotlin because it's a `expect` function
    private int checkIndexOverflow(final int index) {
        return index;
    }
}
