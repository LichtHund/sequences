package dev.triumphteam.sequences.operations;

import dev.triumphteam.sequences.AbstractSequence;
import dev.triumphteam.sequences.Sequence;
import dev.triumphteam.sequences.iterator.EmptyIterator;
import dev.triumphteam.sequences.list.RingBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class WindowedSequence<T> extends AbstractSequence<List<T>, Sequence<List<T>>> {

    private final Iterator<T> iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;
    private final boolean reuseBuffer;

    public WindowedSequence(
            @NotNull final Iterator<T> iterator,
            final int size,
            final int step,
            final boolean partialWindows,
            final boolean reuseBuffer
    ) {
        checkWindowSizeStep(size, step);

        this.iterator = iterator;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
        this.reuseBuffer = reuseBuffer;
    }

    @NotNull
    @Override
    public Iterator<List<T>> iterator() {
        if (!iterator.hasNext()) return new EmptyIterator<>();

        final List<List<T>> builder = new ArrayList<>();

        final int bufferInitialCapacity = Math.min(size, 1024);
        final int gap = step - size;

        if (gap >= 0) {
            List<T> buffer = new ArrayList<>(bufferInitialCapacity);
            int skip = 0;

            while (iterator.hasNext()) {
                final T element = iterator.next();
                if (skip > 0) {
                    skip -= 1;
                    continue;
                }

                buffer.add(element);
                if (buffer.size() == size) {
                    builder.add(buffer);
                    if (reuseBuffer) buffer.clear();
                    else buffer = new ArrayList<>(size);
                    skip = gap;
                }
            }

            if (!buffer.isEmpty()) {
                if (partialWindows || buffer.size() == size) builder.add(buffer);
            }

            return builder.iterator();
        }

        RingBuffer<T> buffer = new RingBuffer<>(bufferInitialCapacity);
        while (iterator.hasNext()) {
            buffer.addToBuffer(iterator.next());
            if (buffer.isFull()) {
                if (buffer.size() < size) {
                    buffer = buffer.expanded(size);
                    continue;
                }

                if (reuseBuffer) builder.add(buffer);
                else builder.add(new ArrayList<T>(buffer));
                buffer.removeFirst(step);
            }
        }

        if (partialWindows) {
            while (buffer.size() > step) {
                if (reuseBuffer) builder.add(buffer);
                else builder.add(new ArrayList<T>(buffer));
                buffer.removeFirst(step);
            }

            if (!buffer.isEmpty()) builder.add(buffer);
        }

        return builder.iterator();
    }

    private void checkWindowSizeStep(final int size, final int step) {
        if (size <= 0 && step <= 0) {
            if (size != step) {
                throw new IllegalArgumentException("Both size $size and step $step must be greater than zero.");
            }

            throw new IllegalArgumentException("size $size must be greater than zero.");
        }
    }
}
