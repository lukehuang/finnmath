package com.github.ltennstedt.finnmath.core.linear;

import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import java.math.BigDecimal;
import java.math.MathContext;

public interface MathContextMatrix<E, V extends AbstractVector<E, V, M, N, B>, M extends AbstractMatrix<E, V, M, N, B>,
    N, B> {
    /**
     * Returns the sum of this {@link MathContextMatrix} and the given one
     *
     * @param summand
     *            summand
     * @param mathContext
     *            {@link MathContext}
     * @return sum
     * @since 1
     */
    M add(M summand, MathContext mathContext);

    /**
     * Returns the difference of this {@link MathContextMatrix} and the given one
     *
     * @param subtrahend
     *            subtrahend
     * @param mathContext
     *            {@link MathContext}
     * @return difference
     * @since 1
     */
    M subtract(M subtrahend, MathContext mathContext);

    /**
     * Returns the product of this {@link MathContextMatrix} and the given one
     *
     * @param factor
     *            factor
     * @param mathContext
     *            {@link MathContext}
     * @return product
     * @since 1
     */
    M multiply(M factor, MathContext mathContext);

    /**
     * Returns the product of this {@link MathContextMatrix} and the given
     * {@link AbstractVector}
     *
     * @param vector
     *            vector
     * @param mathContext
     *            {@link MathContext}
     * @return product
     * @since 1
     */
    V multiplyVector(V vector, MathContext mathContext);

    /**
     * Returns the scalar product of this {@link MathContextMatrix} and the given
     * scalar
     *
     * @param scalar
     *            scalar
     * @param mathContext
     *            {@link MathContext}
     * @return scalar product
     * @since 1
     */
    M scalarMultiply(E scalar, MathContext mathContext);

    /**
     * Returns the negated {@link MathContextMatrix} and this one
     *
     * @param mathContext
     *            {@link MathContext}
     * @return negated {@link MathContextMatrix}
     * @since 1
     */
    M negate(MathContext mathContext);

    /**
     * Returns the trace of this {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return trace
     * @since 1
     */
    E trace(MathContext mathContext);

    /**
     * Returns the determinant of this {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return determinant
     * @since 1
     */
    E determinant(MathContext mathContext);

    /**
     * Returns the maximum absolute column sum norm of this
     * {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return maximum absolute column sum norm
     * @since 1
     */
    N maxAbsColumnSumNorm(MathContext mathContext);

    /**
     * Returns the maximum absolute row sum norm of this {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return maximum absolute row sum norm
     * @since 1
     */
    N maxAbsRowSumNorm(MathContext mathContext);

    /**
     * Returns the square of the frobenius norm of this {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return square of the frobenius norm
     * @since 1
     */
    B frobeniusNormPow2(MathContext mathContext);

    /**
     * Returns the frobenius norm of this {@link MathContextMatrix}
     *
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return frobenius norm
     * @since 1
     */
    BigDecimal frobeniusNorm(final SquareRootContext squareRootContext);

    /**
     * Returns the maximum norm of this {@link MathContextMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return maximum norm
     * @since 1
     */
    N maxNorm(MathContext mathContext);
}
