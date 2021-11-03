package dev.triumphteam.sequences;

import dev.triumphteam.sequences.operations.FlatteningSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class NestedSequence<T> extends AbstractSequence<Sequence<T>, NestedSequence<T>> {

    private final List<Sequence<T>> elements;

    @SafeVarargs
    public NestedSequence(@NotNull final Sequence<T> ... elements) {
        this.elements = Arrays.asList(elements);
    }

    @Override
    public @NotNull Iterator<Sequence<T>> iterator() {
        return elements.iterator();
    }

    @NotNull
    public Sequence<T> flatten() {
        return new FlatteningSequence<>(this, it -> it, BaseSequence::iterator);
    }

}
