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
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
     * Default abort criterion
     *
     * @since 1
     */
    public static final BigDecimal DEFAULT_ABORT_CRITERION = BigDecimal.valueOf(0.0000000001);

    /**
     * Default rounding mode
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * The map holding the elements of this {@link AbstractVector}
     *
     * @since 1
     */
    protected final ImmutableMap<Integer, E> map;

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

    protected abstract V add(V summand);

    protected abstract V subtract(V subtrahend);

    protected abstract E dotProduct(V other);

    protected abstract V scalarMultiply(E scalar);

    protected abstract V negate();

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

    protected abstract P euclideanNormPow2();

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @return The euclidean norm
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanNorm() {
        return euclideanNorm(DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param abortCriterion
     *            abort criterion
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanNorm(final BigDecimal abortCriterion) {
        requireNonNull(abortCriterion, "abortCriterion");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return euclideanNorm(abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param roundingMode
     *            rounding mode
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanNorm(final RoundingMode roundingMode) {
        requireNonNull(roundingMode, "roundingMode");
        return euclideanNorm(DEFAULT_ABORT_CRITERION, roundingMode);
    }

    protected abstract BigDecimal euclideanNorm(BigDecimal abortCriterion, RoundingMode roundingMode);

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param mathContext
     *            math context
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanNorm(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return euclideanNorm(DEFAULT_ABORT_CRITERION, mathContext);
    }

    protected abstract BigDecimal euclideanNorm(BigDecimal abortCriterion, MathContext mathContext);

    /**
     * Returns the square of the euclidean distance from this
     * {@link SimpleComplexNumberVector} to the given one
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
        return euclideanDistance(other, DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the euclidean distance of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param abortCriterion
     *            abort criterion
     * @return euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other, final BigDecimal abortCriterion) {
        requireNonNull(other, "other");
        requireNonNull(abortCriterion, "abortCriterion");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return euclideanDistance(other, abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the euclidean distance of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param roundingMode
     *            rounding mode
     * @return euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other, final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return euclideanDistance(other, DEFAULT_ABORT_CRITERION, roundingMode);
    }

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param abortCriterion
     *            abort criterion
     * @param roundingMode
     *            rounding mode
     * @return Euclidean norm
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other, final BigDecimal abortCriterion,
        final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return subtract(other).euclideanNorm(abortCriterion, roundingMode);
    }

    /**
     * Returns the euclidean distance of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param mathContext
     *            math context
     * @return euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other, final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return euclideanDistance(other, DEFAULT_ABORT_CRITERION, mathContext);
    }

    /**
     * Returns the euclidean norm of this {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param abortCriterion
     *            abort criterion
     * @param mathContext
     *            math context
     * @return Euclidean norm
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @since 1
     */
    public final BigDecimal euclideanDistance(final V other, final BigDecimal abortCriterion,
        final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return subtract(other).euclideanNorm(abortCriterion, mathContext);
    }

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

    @Override
    public final int hashCode() {
        return Objects.hash(map);
    }

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

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).toString();
    }

    public final ImmutableMap<Integer, E> getMap() {
        return map;
    }
}
