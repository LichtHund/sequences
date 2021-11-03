package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.AbstractSequence;
import dev.triumphteam.sequences.BaseSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiFunction;

public final class MergingSequence<T1, T2, V> extends AbstractSequence<V> {

    private final BaseSequence<T1> sequence1;
    private final BaseSequence<T2> sequence2;
    private final BiFunction<T1, T2, V> transform;

    public MergingSequence(
            @NotNull final BaseSequence<T1> sequence1,
            @NotNull final BaseSequence<T2> sequence2,
            @NotNull final BiFunction<T1, T2, V> transform
    ) {
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.transform = transform;
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return new Iterator<V>() {
            private final Iterator<T1> iterator1 = sequence1.iterator();
            private final Iterator<T2> iterator2 = sequence2.iterator();

            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public V next() {
                return transform.apply(iterator1.next(), iterator2.next());
            }
        };
    }
}
