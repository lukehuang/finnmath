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

package com.github.ltennstedt.finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @param <E>
 *            The type of the elements of the vector
 * @param <V>
 *            The type of the vector
 * @param <N>
 *            The type of the taxicab and max norm of the vector
 * @param <P>
 *            The type of the inner product
 * @author Lars Tennstedt
 * @see ImmutableMap
 * @since 1
 */
@Beta
abstract class AbstractVector<E, V, N, P> {
    /**
     * The map holding the elements of this {@link AbstractVector}
     *
     * @author Lars Tennstedt
     * @since 1
     */
    protected final ImmutableMap<Integer, E> map;

    protected AbstractVector(final ImmutableMap<Integer, E> map) {
        this.map = map;
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
     * @author Lars Tennstedt
     * @see Map#containsKey
     * @since 1
     */
    public E element(final Integer index) {
        requireNonNull(index, "index");
        checkArgument(map.containsKey(index), "expected index in [1, %s] but actual %s", map.size(), index);
        return map.get(index);
    }

    /**
     * Returns all elements of the underlying {@link Map} of this {@link AbstractVector}
     *
     * @return The elements
     * @author Lars Tennstedt
     * @see Map#entrySet
     * @since 1
     */
    public ImmutableSet<Entry<Integer, E>> entries() {
        return map.entrySet();
    }

    /**
     * Returns all elements of this {@link AbstractVector}
     *
     * @return The elements
     * @author Lars Tennstedt
     * @see Map#values
     * @since 1
     */
    public ImmutableCollection<E> elements() {
        return map.values();
    }

    protected abstract V add(V summand);

    protected abstract V subtract(V subtrahend);

    protected abstract E dotProduct(V other);

    protected abstract V scalarMultiply(E scalar);

    protected abstract V negate();

    protected abstract N taxicabNorm();

    protected abstract N taxicabDistance(V other);

    protected abstract P euclideanNormPow2();

    protected abstract BigDecimal euclideanNorm();

    protected abstract BigDecimal euclideanNorm(BigDecimal precision);

    protected abstract BigDecimal euclideanNorm(int scale, RoundingMode roundingMode);

    protected abstract BigDecimal euclideanNorm(BigDecimal precision, int scale, RoundingMode roundingMode);

    protected abstract P euclideanDistancePow2(V other);

    protected abstract BigDecimal euclideanDistance(V other);

    protected abstract BigDecimal euclideanDistance(V other, BigDecimal precision);

    protected abstract BigDecimal euclideanDistance(V other, int scale, RoundingMode roundingMode);

    protected abstract BigDecimal euclideanDistance(V other, BigDecimal precision, int scale,
        RoundingMode roundingMode);

    protected abstract N maxNorm();

    protected abstract N maxDistance(V other);

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @author Lars Tennstedt
     * @since 1
     */
    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).toString();
    }

    public ImmutableMap<Integer, E> getMap() {
        return map;
    }
}
