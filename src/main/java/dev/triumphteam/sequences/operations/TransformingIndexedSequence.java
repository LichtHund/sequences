package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.AbstractSequence;
import dev.triumphteam.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;

import static dev.triumphteam.sequences.util.SequenceUtils.checkIndexOverflow;

public final class TransformingIndexedSequence<T, R> extends AbstractSequence<R, Sequence<R>> {
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

}
