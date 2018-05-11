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

package com.github.ltennstedt.finnmath.core.number;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.linear.AbstractMatrix;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Base class for complex numbers
 *
 * @param <B>
 *            The type of the real and imaginary part of the complex number
 * @param <S>
 *            The type of the complex number
 * @param <M>
 *            The type of the related matrix
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public abstract class AbstractComplexNumber<B, S extends AbstractComplexNumber<B, S, M>,
    M extends AbstractMatrix<B, ?, M, B, B>> implements MathNumber<S, RealComplexNumber, BigDecimal> {
    /**
     * Default {@link RoundingMode}
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Default {@link SquareRootContext}
     */
    public static final SquareRootContext DEFAULT_SQUARE_ROOT_CONTEXT =
        SquareRootCalculator.DEFAULT_SQUARE_ROOT_CONTEXT;

    /**
     * {@code real} part of this {@link AbstractComplexNumber}
     *
     * @since 1
     */
    protected final B real;

    /**
     * {@code imaginary} part of this {@link AbstractComplexNumber}
     *
     * @since 1
     */
    protected final B imaginary;

    /**
     * Required arguments constructor
     *
     * @param real
     *            real
     * @param imaginary
     *            imaginary
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     */
    protected AbstractComplexNumber(final B real, final B imaginary) {
        this.real = requireNonNull(real, "real");
        this.imaginary = requireNonNull(imaginary, "imaginary");
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code !divisor.invertible}
     * @since 1
     */
    @Override
    public RealComplexNumber divide(final S divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return divide(divisor, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the quotient of this {@link AbstractComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @param roundingMode
     *            {@link RoundingMode}
     * @return quotient
     * @since 1
     */
    protected abstract RealComplexNumber divide(S divisor, RoundingMode roundingMode);

    /**
     * Returns the quotient of this {@link AbstractComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @param scale
     *            scale
     * @param roundingMode
     *            {@link RoundingMode}
     * @return quotient
     * @since 1
     */
    protected abstract RealComplexNumber divide(S divisor, int scale, RoundingMode roundingMode);

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
     *
     * @param divisor
     *            divisor
     * @param mathContext
     *            {@link MathContext}
     * @return quotient
     * @since 1
     */
    protected abstract RealComplexNumber divide(S divisor, MathContext mathContext);

    /**
     * {@inheritDoc}
     *
     * @since 1
     * @see #abs(SquareRootContext)
     */
    @Override
    public final BigDecimal abs() {
        return abs(DEFAULT_SQUARE_ROOT_CONTEXT);
    }

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return absolute value
     * @since 1
     */
    protected abstract BigDecimal abs(SquareRootContext squareRootContext);

    /**
     * Returns the square of the absolute value of this
     * {@link AbstractComplexNumber}
     *
     * @return square of the absolute value
     * @since 1
     */
    protected abstract B absPow2();

    /**
     * Returns the conjugate of this {@link AbstractComplexNumber}
     *
     * @return conjugate
     * @since 1
     */
    protected abstract S conjugate();

    /**
     * Returns the argument of this {@link AbstractComplexNumber}
     *
     * @return argument if {@code this == 0}
     * @since 1
     */
    protected abstract BigDecimal argument();

    /**
     * Returns the argument of this {@link AbstractComplexNumber} considering the
     * given {@link MathContext}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return argument
     * @since 1
     */
    protected abstract BigDecimal argument(MathContext mathContext);

    /**
     * Return the corresponding polar form of the complex number
     *
     * @return polar form
     * @since 1
     */
    protected abstract PolarForm polarForm();

    /**
     * Return the corresponding polar form of the complex number considering the
     * given {@link MathContext}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return polar form
     * @since 1
     */
    protected abstract PolarForm polarForm(MathContext mathContext);

    /**
     * Returns a matrix representation of this {@link AbstractComplexNumber}
     *
     * @return matrix representation
     * @since 1
     */
    protected abstract M matrix();

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public final int hashCode() {
        return Objects.hash(real, imaginary);
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
        if (!(object instanceof AbstractComplexNumber)) {
            return false;
        }
        final AbstractComplexNumber<?, ?, ?> other = (AbstractComplexNumber<?, ?, ?>) object;
        return real.equals(other.getReal()) && imaginary.equals(other.getImaginary());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("real", real).add("imaginary", imaginary).toString();
    }

    /**
     * Returns the real part
     *
     * @return real
     * @since 1
     */
    public final B getReal() {
        return real;
    }

    /**
     * Returns the imaginary part
     *
     * @return imaginary part
     * @since 1
     */
    public final B getImaginary() {
        return imaginary;
    }
}
