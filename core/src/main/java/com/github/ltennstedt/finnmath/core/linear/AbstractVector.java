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
public abstract class AbstractVector<E, V extends AbstractVector<E, V, N, P>, N, P> {
    /**
     * Default {@link SquareRootCalculator}
     *
     * @since 1
     */
    public static final SquareRootContext DEFAULT_SQUARE_ROOT_CONTEXT =
        SquareRootCalculator.DEFAULT_SQUARE_ROOT_CONTEXT;

    /**
     * The map holding the elements of this {@link AbstractVector}
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
     */
    protected AbstractVector(final ImmutableMap<Integer, E> map) {
        requireNonNull(map, "map");
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
     * @see Map#containsKey
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
     * @return The elements
     * @see Map#entrySet
     * @since 1
     */
    public final ImmutableSet<Entry<Integer, E>> entries() {
        return map.entrySet();
    }

    /**
     * Returns all elements of this {@link AbstractVector}
     *
     * @return The elements
     * @see Map#values
     * @since 1
     */
    public final ImmutableCollection<E> elements() {
        return map.values();
    }

    /**
     * Returns the sum of this {@link AbstractVector} and the given one
     *
     * @param summand
     *            summand
     * @return Sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     * @since 1
     */
    protected abstract V add(V summand);

    /**
     * Returns the difference of this {@link AbstractVector} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code size != subtrahend.size}
     * @since 1
     */
    protected abstract V subtract(V subtrahend);

    /**
     * Returns the dot product of this {@link AbstractVector} and the given one
     *
     * @param other
     *            The other {@link AbstractVector}
     * @return The dot product
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    protected abstract E dotProduct(V other);

    /**
     * Returns the scalar product of the given scalar and this
     * {@link AbstractVector}
     *
     * @param scalar
     *            the scalar
     * @return The scalar product
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @since 1
     */
    protected abstract V scalarMultiply(E scalar);

    /**
     * Returns the negated {@link AbstractVector} of this one
     *
     * @return The negated
     * @see #scalarMultiply
     * @since 1
     */
    protected abstract V negate();

    /**
     * Returns the taxicab norm of this {@link AbstractVector}
     *
     * @return The taxicab norm
     * @since 1
     */
    protected abstract N taxicabNorm();

    /**
     * Returns the taxicab distance from this {@link AbstractVector} to the given
     * one
     *
     * @param other
     *            The other {@link AbstractVector}
     * @return The taxicab distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @see #subtract
     * @see #taxicabNorm
     * @since 1
     */
    public final N taxicabDistance(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean norm of this {@link AbstractVector}
     *
     * @return The square of the euclidean norm
     * @see #dotProduct
     * @since 1
     */
    protected abstract P euclideanNormPow2();

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return Euclidean norm
     * @since 1
     */
    protected abstract BigDecimal euclideanNorm(SquareRootContext squareRootContext);

    /**
     * Returns the square of the euclidean distance from this {@link AbstractVector}
     * to the given one
     *
     * @param other
     *            The other {@link AbstractVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @see #subtract
     * @see #euclideanNormPow2
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
     * @see SquareRootCalculator#sqrt(BigDecimal)
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
     * @return Euclidean norm
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     * @see #subtract
     * @see #euclideanNorm(SquareRootContext)
     */
    public final BigDecimal euclideanDistance(final V other, final SquareRootContext squareRootContext) {
        requireNonNull(other, "other");
        requireNonNull(squareRootContext, "squareRootContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).euclideanNorm(squareRootContext);
    }

    /**
     * Returns the max norm of this {@link AbstractVector}
     *
     * @return The max norm
     * @since 1
     */
    protected abstract N maxNorm();

    /**
     * Returns the max distance from this {@link AbstractVector} to the given one
     *
     * @param other
     *            The other {@link AbstractVector}
     * @return The max distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @see #subtract
     * @see #maxNorm
     * @since 1
     */
    public final N maxDistance(final V other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).maxNorm();
    }

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @since 1
     */
    public final int size() {
        return map.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return Objects.hash(map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractVector)) {
            return false;
        }
        final AbstractVector<?, ?, ?, ?> other = (AbstractVector<?, ?, ?, ?>) object;
        return Objects.deepEquals(map, other.getMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).toString();
    }

    public final ImmutableMap<Integer, E> getMap() {
        return map;
    }
}
