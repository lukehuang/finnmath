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

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableTable;
import java.math.MathContext;
import java.util.Map;

/**
 * Base class for matrices
 *
 * @author Lars Tennstedt
 *
 * @param <E>
 *            type of the elements
 * @param <V>
 *            type of the vector
 * @param <M>
 *            type of the matrix
 * @param <N>
 *            type of the norm
 * @param <B>
 *            type of the square of the norms
 * @param <C>
 *            type of the context
 * @since 1
 */
@Beta
public abstract class AbstractContextMatrix<E, V extends AbstractVector<E, V, M, N, B>,
    M extends AbstractMatrix<E, V, M, N, B>, N, B, C> extends AbstractMatrix<E, V, M, N, B> {
    /**
     * Required arguments constructor
     *
     * @param table
     *            {@link ImmutableTable}
     * @since 1
     */
    protected AbstractContextMatrix(final ImmutableTable<Integer, Integer, E> table) {
        super(table);
    }

    /**
     * Returns the sum of this {@link AbstractContextMatrix} and the given one
     *
     * @param summand
     *            summand
     * @param mathContext
     *            {@link MathContext}
     * @return sum
     * @since 1
     */
    protected abstract M add(M summand, MathContext mathContext);

    /**
     * Returns the difference of this {@link AbstractContextMatrix} and the given
     * one
     *
     * @param subtrahend
     *            subtrahend
     * @param mathContext
     *            {@link MathContext}
     * @return difference
     * @since 1
     */
    protected abstract M subtract(M subtrahend, MathContext mathContext);

    /**
     * Returns the product of this {@link AbstractContextMatrix} and the given one
     *
     * @param factor
     *            factor
     * @param mathContext
     *            {@link MathContext}
     * @return product
     * @since 1
     */
    protected abstract M multiply(M factor, MathContext mathContext);

    /**
     * Returns the product of this {@link AbstractContextMatrix} and the given
     * {@link AbstractVector}
     *
     * @param vector
     *            vector
     * @param mathContext
     *            {@link MathContext}
     * @return product
     * @since 1
     */
    protected abstract V multiplyVector(V vector, MathContext mathContext);

    /**
     * Returns the scalar product of this {@link AbstractContextMatrix} and the
     * given scalar
     *
     * @param scalar
     *            scalar
     * @param mathContext
     *            {@link MathContext}
     * @return scalar product
     * @since 1
     */
    protected abstract M scalarMultiply(E scalar, MathContext mathContext);

    /**
     * Returns the negated {@link AbstractContextMatrix} and this one
     *
     * @param mathContext
     *            {@link MathContext}
     * @return negated {@link AbstractContextMatrix}
     * @since 1
     */
    protected abstract M negate(MathContext mathContext);

    /**
     * Returns the trace of this {@link AbstractContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return trace
     * @since 1
     */
    protected abstract E trace(MathContext mathContext);

    /**
     * Returns the determinant of this {@link AbstractContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return determinant
     * @since 1
     */
    protected abstract E determinant(MathContext mathContext);

    /**
     * Returns the maximum absolute column sum norm of this
     * {@link AbstractContextMatrix}
     *
     * @param context
     *            context
     * @return maximum absolute column sum norm
     * @since 1
     */
    protected abstract N maxAbsColumnSumNorm(C context);

    /**
     * Returns the maximum absolute row sum norm of this
     * {@link AbstractContextMatrix}
     *
     * @param context
     *            context
     * @return maximum absolute row sum norm
     * @since 1
     */
    protected abstract N maxAbsRowSumNorm(C context);

    /**
     * Returns the square of the frobenius norm of this
     * {@link AbstractContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return square of the frobenius norm
     * @since 1
     */
    protected abstract B frobeniusNormPow2(MathContext mathContext);

    /**
     * Returns the maximum norm of this {@link AbstractContextMatrix}
     *
     * @param context
     *            context
     * @return maximum norm
     * @since 1
     */
    protected abstract N maxNorm(C context);

    /**
     * Returns the product of a matrix row and a matrix column
     *
     * @param row
     *            row
     * @param column
     *            column
     * @param mathContext
     *            {@link MathContext}
     * @return product
     * @since 1
     */
    protected abstract E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column, MathContext mathContext);

    /**
     * Leibniz formula
     *
     * @param mathContext
     *            {@link MathContext}
     * @return determinant
     * @since 1
     */
    protected abstract E leibnizFormula(MathContext mathContext);

    /**
     * Rule of Sarrus
     *
     * @param mathContext
     *            {@link MathContext}
     * @return determinant
     * @since 1
     */
    protected abstract E ruleOfSarrus(MathContext mathContext);

}
