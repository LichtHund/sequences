package dev.triumphteam.sequences.iterator;

import dev.triumphteam.sequences.scope.SequenceScope;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class SequenceBuilderIterator<T> extends SequenceScope<T> {

    private final List<T> buffer = new LinkedList<>();

    @Override
    public void yield(@NotNull final T value) {
        buffer.add(value);
    }

    @Override
    public void yieldAll(final @NotNull Iterator<T> iterator) {
        if (!iterator.hasNext()) return;
    }

    public Iterator<T> getIterator() {
        return buffer.iterator();
    }

}
