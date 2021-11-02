package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.BaseSequence;
import dev.triumphteam.sequences.IndexedValue;
import dev.triumphteam.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.triumphteam.sequences.util.SequenceUtils.checkIndexOverflow;

public final class IndexingSequence<T> extends BaseSequence<IndexedValue<T>> {

    private final Sequence<T> sequence;

    public IndexingSequence(@NotNull final Sequence<T> sequence) {
        this.sequence = sequence;
    }

    @NotNull
    @Override
    public  Iterator<IndexedValue<T>> iterator() {
        return new Iterator<IndexedValue<T>>() {
            private final Iterator<T> iterator = sequence.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public IndexedValue<T> next() {
                return new IndexedValue<>(checkIndexOverflow(++index), iterator.next());
            }
        };
    }

}
