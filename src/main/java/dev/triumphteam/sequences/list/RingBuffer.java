package dev.triumphteam.sequences.list;

import dev.triumphteam.sequences.iterator.AbstractIterator;
import dev.triumphteam.sequences.util.SequenceUtils;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;

// TODO: 10/30/2021 This class also needs working, because KT's usage of coroutines
public class RingBuffer<T> extends AbstractList<T> implements RandomAccess {
    private final Object[] buffer;
    private final int capacity;
    private int startIndex = 0;
    private int size;

    public RingBuffer(@NotNull final Object[] buffer, final int filledSize) {
        if (filledSize < 0) {
            throw new IllegalArgumentException("ring buffer filled size should not be negative but it is " + filledSize);
        }
        if (filledSize > buffer.length) {
            throw new IllegalArgumentException("ring buffer filled size: " + filledSize + " cannot be larger than the buffer size: " + buffer.length);
        }
        this.buffer = buffer;
        this.capacity = buffer.length;
        this.size = filledSize;
    }

    public RingBuffer(final int capacity) {
        this(SequenceUtils.arrayOfNulls(capacity), 0);
    }

    @Override
    public T get(final int index) {
        return (T) buffer[SequenceUtils.forward(startIndex, index, capacity)];
    }

    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {
            private int count = size;
            private int index = startIndex;

            @Override
            protected void computeNext() {
                if (count == 0) {
                    done();
                    return;
                }

                setNext((T) buffer[index]);
                index = SequenceUtils.forward(index, 1, capacity);
                count--;
            }
        };
    }

    @NotNull
    @Override
    public <A> A[] toArray(@NotNull final A[] array) {
        final A[] result;
        if (array.length < size) result = Arrays.copyOf(array, size());
        else result = array;

        int widx = 0;
        int idx = startIndex;

        while (widx < size && idx < capacity) {
            result[widx] = (A) buffer[idx];
            widx++;
            idx++;
        }

        idx = 0;
        while (widx < size) {
            result[widx] = (A) buffer[idx];
            widx++;
            idx++;
        }
        if (result.length > size) result[size] = null;

        return result;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return toArray(SequenceUtils.arrayOfNulls(size()));
    }

    public RingBuffer<T> expanded(final int maxCapacity) {
        final int newCapacity = Math.min(capacity + (capacity >> 1) + 1, maxCapacity);
        final Object[] newBuffer;
        if (startIndex == 0) newBuffer = Arrays.copyOf(buffer, newCapacity);
        else newBuffer = toArray(SequenceUtils.arrayOfNulls(newCapacity));
        return new RingBuffer<>(newBuffer, size);
    }

    public void addToBuffer(@NotNull final T element) {
        if (isFull()) {
            throw new IllegalStateException("ring buffer is full");
        }

        buffer[SequenceUtils.forward(startIndex, size, capacity)] = element;
        size++;
    }

    public void removeFirst(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n shouldn't be negative but it is " + n);
        }
        if (n > size) {
            throw new IllegalArgumentException("n shouldn't be greater than the buffer size: n = " + n + ", size = " + size);
        }

        if (n > 0) {
            final int start = startIndex;
            final int end = SequenceUtils.forward(start, n, capacity);

            if (start > end) {
                Arrays.fill(buffer, start, capacity, null);
                Arrays.fill(buffer, 0, end, null);
            } else {
                Arrays.fill(buffer, start, end, null);
            }

            startIndex = end;
            size -= n;
        }
    }

    @Override
    public int size() {
        return size;
    }


}
