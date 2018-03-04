/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ltennstedt.finnmath.core.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.addExact;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.Builder;

/**
 * Base class for vector builders
 *
 * @param <E>
 *            The type of the elements of the vector
 * @param <V>
 *            The type of the vector
 * @param <B>
 *            The type of the builder
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public abstract class AbstractVectorBuilder<E, V, B> implements Builder<V> {
    /**
     * The map holding the elements of this {@link AbstractVectorBuilder}
     */
    protected final Map<Integer, E> map = new HashMap<>();

    /**
     * The size of the map
     */
    protected final int size;

    protected AbstractVectorBuilder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        this.size = size;
    }

    /**
     * Returns the element dependent on the given index
     *
     * @param index
     *            the index of the element
     * @return The element
     * @throws NullPointerException
     *             if {@code index == null}
     * @throws IllegalArgumentException
     *             if {@code !map.containsKey(index)}
     * @see Map#containsKey
     * @since 1
     */
    public final E element(final Integer index) {
        requireNonNull(index, "index");
        checkArgument(0 < index && index <= size, "expected index in [1, %s] but actual %s", size, index);
        return map.get(index);
    }

    /**
     * Puts the given element on the first free index and returns {@code this}
     *
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code element == null}
     * @throws ArithmeticException
     *             if ({@code size + 1} overflows
     * @throws IllegalStateException
     *             if {@code size == size}
     * @since 1
     */
    public final B put(final E element) {
        requireNonNull(element, "element");
        final int index = addExact(map.size(), 1);
        checkState(map.size() < size, "expected index in [1, %s] but actual %s", size, index);
        map.put(index, element);

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    /**
     * Puts the given element on the given index and returns {@code this}
     *
     * @param index
     *            the index
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code index == null}
     * @throws NullPointerException
     *             if {@code element == null}
     * @throws IllegalArgumentException
     *             if {@code index <= 0 || size < index}
     * @since 1
     */
    public final B put(final Integer index, final E element) {
        requireNonNull(index, "index");
        requireNonNull(element, "element");
        checkArgument(0 < index && index <= size, "expected index in [1, %s] but actual %s", size, index);
        map.put(index, element);

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    /**
     * Puts the given element on all indices and returns {@code this}
     *
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code element == null}
     * @since 1
     */
    public final B putAll(final E element) {
        requireNonNull(element, "element");
        IntStream.rangeClosed(1, size).forEach(index -> map.put(index, element));

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    /**
     * Puts the given element on all indices which are {@code null} and returns
     * {@code this}
     *
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code element == null}
     * @since 1
     */
    public final B nullsToElement(final E element) {
        requireNonNull(element, "element");
        map.entrySet().forEach(entry -> {
            if (entry.getValue() == null) {
                map.put(entry.getKey(), element);
            }
        });

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).add("size", size).toString();
    }

    public final Map<Integer, E> getMap() {
        return map;
    }

    public final int getSize() {
        return size;
    }
}
