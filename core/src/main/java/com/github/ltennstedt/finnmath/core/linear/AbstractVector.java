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
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Base class for vectors
 *
 * @param <E>
 *            type of the elements of the vector
 * @param <V>
 *            type of the vector
 * @param <M>
 *            type of the related matrix
 * @param <N>
 *            type of the taxicab and max norm of the vector
 * @param <P>
 *            type of the inner product
 * @author Lars Tennstedt
 * @see ImmutableMap
 * @since 1
 */
@Beta
public abstract class AbstractVector<E, V extends AbstractVector<E, V, M, N, P>,
    M extends AbstractMatrix<E, V, M, N, P>, N, P> {
    /**
     * Default {@link SquareRootCalculator}
     *
     * @since 1
     */
    public static final SquareRootContext DEFAULT_SQUARE_ROOT_CONTEXT =
        SquareRootCalculator.DEFAULT_SQUARE_ROOT_CONTEXT;

    /**
     * {@link ImmutableMap} holding the elements of this {@link AbstractVector}
     *
     * @since 1
     */
    protected final ImmutableMap<Integer, E> map;

    /**
     * Required arguments constructor
     *
     * @param map
     *            {@link ImmutableMap}
     * @throws NullPointerException
     *             if {@code map == null}
     * @since 1
     */
    protected AbstractVector(final ImmutableMap<Integer, E> map) {
        this.map = requireNonNull(map, "map");
    }

    /**
     * Returns the sum of this {@link AbstractVector} and the given one
     *
     * @param summand
     *            summand
     * @return sum
     * @since 1
     */
    protected abstract V add(V summand);

    /**
     * Returns the difference of this {@link AbstractVector} and the given one
     *
     * @param subtrahend
     *            subtrahend
     * @return difference
     * @since 1
     */
    protected abstract V subtract(V subtrahend);

    /**
     * Returns the dot product of this {@link AbstractVector} and the given one
     *
     * @param other
     *            other {@link AbstractVector}
     * @return dot product
     * @since 1
     */
    protected abstract E dotProduct(V other);

    /**
     * Returns the scalar product of the given scalar and this
     * {@link AbstractVector}
     *
     * @param scalar
     *            scalar
     * @return scalar product
     * @since 1
     */
    protected abstract V scalarMultiply(E scalar);

    /**
     * Returns the negated {@link AbstractVector} of this one
     *
     * @return negated {@link AbstractVector}
     * @since 1
     */
    protected abstract V negate();

    /**
     * Returns if {@code this} is orthogonal to the other {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @return {@code true} if {@code this} is orthogonal to the other
     *         {@link AbstractVector}, {@code false} otherwise
     * @since 1
     */
    protected abstract boolean orthogonalTo(V other);

    /**
     * Returns the taxicab norm of this {@link AbstractVector}
     *
     * @return taxicab norm
     * @since 1
     */
    protected abstract N taxicabNorm();

    /**
     * Returns the square of the euclidean norm of this {@link AbstractVector}
     *
     * @return square of the euclidean norm
     * @since 1
     */
    protected abstract P euclideanNormPow2();

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @return euclidean norm
     * @since 1
     */
    protected abstract BigDecimal euclideanNorm();

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return euclidean norm
     * @since 1
     */
    protected abstract BigDecimal euclideanNorm(SquareRootContext squareRootContext);

    /**
     * Returns the maximum norm of this {@link AbstractVector}
     *
     * @return maximum norm
     * @since 1
     */
    protected abstract N maxNorm();

    /**
     * Returns the taxicab distance from this {@link AbstractVector} to the given
     * one
     *
     * @param other
     *            other {@link AbstractVector}
     * @return taxicab distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final N taxicabDistance(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean distance from this {@link AbstractVector}
     * to the given one
     *
     * @param other
     *            other {@link AbstractVector}
     * @return square of the euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final P euclideanDistancePow2(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).euclideanNormPow2();
    }

    /**
     * Returns the euclidean distance of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @return euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return euclideanDistance(other, DEFAULT_SQUARE_ROOT_CONTEXT);
    }

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return euclidean norm
     * @since 1
     */
    protected abstract BigDecimal euclideanDistance(V other, SquareRootContext squareRootContext);

    /**
     * Returns the maximum distance from this {@link AbstractVector} to the given
     * one
     *
     * @param other
     *            other {@link AbstractVector}
     * @return maximum distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final N maxDistance(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).maxNorm();
    }

    /**
     * Returns the dyadic product of {@code this} {@link AbstractVector} and the
     * other one
     *
     * @param other
     *            other {@link AbstractVector}
     * @return dyadic product
     * @since 1
     */
    protected abstract M dyadicProduct(V other);

    /**
     * Returns the element dependent on the given index
     *
     * @param index
     *            index
     * @return element
     * @throws NullPointerException
     *             if {@code index == null}
     * @throws IllegalArgumentException
     *             if {@code index < 1 || size < index}
     * @since 1
     */
    public final E element(final Integer index) {
        requireNonNull(index, "index");
        checkArgument(map.containsKey(index), "expected index in [1, %s] but actual %s", map.size(), index);
        return map.get(index);
    }

    /**
     * Returns all elements of the underlying {@link Map} of this
     * {@link AbstractVector}
     *
     * @return {@link Entry Entries}
     * @since 1
     */
    public final ImmutableSet<Entry<Integer, E>> entries() {
        return map.entrySet();
    }

    /**
     * Returns all elements of this {@link AbstractVector}
     *
     * @return elements
     * @since 1
     */
    public final ImmutableCollection<E> elements() {
        return map.values();
    }

    /**
     * Returns the size of this {@link AbstractVector}
     *
     * @return size
     * @since 1
     */
    public final int size() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public final int hashCode() {
        return Objects.hash(map);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractVector)) {
            return false;
        }
        final AbstractVector<?, ?, ?, ?, ?> other = (AbstractVector<?, ?, ?, ?, ?>) object;
        return Objects.deepEquals(map, other.getMap());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).toString();
    }

    public final ImmutableMap<Integer, E> getMap() {
        return map;
    }
}
