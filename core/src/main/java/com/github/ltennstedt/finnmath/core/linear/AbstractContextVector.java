/*
 * Copyright 2018 Lars Tennstedt
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

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.MathContext;

/**
 *
 * Base class for vectors
 *
 * @author Lars Tennstedt
 *
 * @param <E>
 *            type of the elements
 * @param <V>
 *            type of the {@link AbstractVector}
 * @param <M>
 *            type of the {@link AbstractMatrix}
 * @param <N>
 *            type of the norm
 * @param <P>
 *            type of the inner product
 * @param <C>
 *            type of the context
 * @since 1
 */
@Beta
public abstract class AbstractContextVector<E, V extends AbstractVector<E, V, M, N, P>,
    M extends AbstractContextMatrix<E, V, M, N, P, C>, N, P, C> extends AbstractVector<E, V, M, N, P> {
    /**
     * Required arguments constructor
     *
     * @param map
     *            {@link ImmutableMap}
     * @since 1
     */
    protected AbstractContextVector(final ImmutableMap<Integer, E> map) {
        super(map);
    }

    /**
     * Returns the sum of this {@link AbstractVector} and the given one
     *
     * @param summand
     *            summand
     * @param mathContext
     *            {@link MathContext}
     * @return sum
     * @since 1
     */
    protected abstract V add(V summand, MathContext mathContext);

    /**
     * Returns the difference of this {@link AbstractVector} and the given one
     *
     * @param subtrahend
     *            subtrahend
     * @param mathContext
     *            {@link MathContext}
     * @return difference
     * @since 1
     */
    protected abstract V subtract(V subtrahend, MathContext mathContext);

    /**
     * Returns the dot product of this {@link AbstractVector} and the given one
     *
     * @param other
     *            other {@link AbstractVector}
     * @param mathContext
     *            {@link MathContext}
     * @return dot product
     * @since 1
     */
    protected abstract E dotProduct(V other, MathContext mathContext);

    /**
     * Returns the scalar product of the given scalar and this
     * {@link AbstractVector}
     *
     * @param scalar
     *            scalar
     * @param mathContext
     *            {@link MathContext}
     * @return scalar product
     * @since 1
     */
    protected abstract V scalarMultiply(E scalar, MathContext mathContext);

    /**
     * Returns the negated {@link AbstractVector} of this one
     *
     * @param mathContext
     *            {@link MathContext}
     * @return negated {@link AbstractVector}
     * @since 1
     */
    protected abstract V negate(MathContext mathContext);

    /**
     * Returns if {@code this} is orthogonal to the other {@link AbstractVector}
     *
     * @param other
     *            other {@link AbstractVector}
     * @param mathContext
     *            {@link MathContext}
     * @return {@code true} if {@code this} is orthogonal to the other
     *         {@link AbstractVector}, {@code false} otherwise
     * @since 1
     */
    protected abstract boolean orthogonalTo(V other, MathContext mathContext);

    /**
     * Returns the taxicab norm of this {@link AbstractVector}
     *
     * @param context
     *            context
     * @return taxicab norm
     * @since 1
     */
    protected abstract N taxicabNorm(C context);

    /**
     * Returns the square of the euclidean norm of this {@link AbstractVector}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return square of the euclidean norm
     * @since 1
     */
    protected abstract P euclideanNormPow2(MathContext mathContext);

    /**
     * Returns the maximum norm of this {@link AbstractVector}
     *
     * @param context
     *            context
     * @return maximum norm
     * @since 1
     */
    protected abstract P maxNorm(C context);

    /**
     * Returns the taxicab distance from this {@link AbstractVector} to the given
     * one
     *
     * @param other
     *            other {@link AbstractVector}
     * @param mathContext
     *            {@link MathContext}
     * @return taxicab distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final N taxicabDistance(final V other, final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other, mathContext).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean distance from this {@link AbstractVector}
     * to the given one
     *
     * @param other
     *            other {@link AbstractVector}
     * @param context
     *            context
     * @return square of the euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final P euclideanDistancePow2(final V other, final C context) {
        requireNonNull(other, "other");
        requireNonNull(context, "context");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).euclideanNormPow2();
    }

    /**
     * Returns the maximum distance from this {@link AbstractVector} to the given
     * one
     *
     * @param other
     *            other {@link AbstractVector}
     * @param mathContext
     *            {@link MathContext}
     * @return maximum distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    public final N maxDistance(final V other, final MathContext mathContext) {
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
     * @param mathContext
     *            {@link MathContext}
     * @return dyadic product
     * @since 1
     */
    protected abstract M dyadicProduct(V other, MathContext mathContext);
}
