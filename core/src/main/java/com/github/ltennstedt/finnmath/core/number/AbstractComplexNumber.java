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
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.github.ltennstedt.finnmath.core.linear.AbstractMatrix;
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
    M extends AbstractMatrix<B, ?, M, B, B>> implements MathNumber<S, AbstractComplexNumber, BigDecimal> {
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
     * Returns the quotient of this {@link AbstractComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @see #invertible
     * @since 1
     */
    @Override
    public final RealComplexNumber divide(final S divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return divide(divisor, DEFAULT_ROUNDING_MODE);
    }

    protected abstract RealComplexNumber divide(S divisor, RoundingMode roundingMode);

    /**
     * Returns the absolute of this {@link AbstractComplexNumber}
     *
     * @return absolute value
     * @since 1
     */
    @Override
    public final BigDecimal abs() {
        return abs(DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param abortCriterion
     *            abort criterion
     * @return absolute value
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     */
    public final BigDecimal abs(final BigDecimal abortCriterion) {
        requireNonNull(abortCriterion, "abortCriterion");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return abs(abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param roundingMode
     *            rounding mode
     * @return absolute value
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     */
    public final BigDecimal abs(final RoundingMode roundingMode) {
        requireNonNull(roundingMode, "roundingMode");
        return abs(DEFAULT_ABORT_CRITERION, roundingMode);
    }

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param abortCriterion
     *            abort criterion
     * @param roundingMode
     *            rounding mode
     * @return absolute value
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     */
    protected abstract BigDecimal abs(BigDecimal abortCriterion, RoundingMode roundingMode);

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param mathContext
     *            math context
     * @return absolute value
     * @throws NullPointerException
     *             if {@code mathContext == null}
     */
    public final BigDecimal abs(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return abs(DEFAULT_ABORT_CRITERION, mathContext);
    }

    /**
     * Returns the absolute value of the {@link AbstractComplexNumber}
     *
     * @param abortCriterion
     *            abort criterion
     * @param mathContext
     *            math context
     * @return absolute value
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     */
    protected abstract BigDecimal abs(BigDecimal abortCriterion, MathContext mathContext);

    /**
     * Returns the square of the absolute of this {@link AbstractComplexNumber}
     *
     * @return The square of the absolute
     * @since 1
     */
    protected abstract B absPow2();

    /**
     * Returns the conjugate of this {@link AbstractComplexNumber}
     *
     * @return The conjugated
     * @since 1
     */
    protected abstract S conjugate();

    /**
     * Returns the argument of this {@link AbstractComplexNumber}
     *
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @see #argument(MathContext)
     * @since 1
     */
    protected abstract BigDecimal argument();

    /**
     * Returns the argument of this {@link AbstractComplexNumber} considering the
     * given precision
     *
     * @param precision
     *            The precision
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @see #argument(MathContext)
     * @since 1
     */
    protected abstract BigDecimal argument(int precision);

    /**
     * Returns the argument of this {@link AbstractComplexNumber} considering the
     * given rounding mode
     *
     * @param roundingMode
     *            The rounding mode
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #argument(MathContext)
     * @since 1
     */
    protected abstract BigDecimal argument(RoundingMode roundingMode);

    /**
     * Returns the argument of this {@link AbstractComplexNumber} considering the
     * given precision and rounding mode
     *
     * @param precision
     *            The precision
     * @param roundingMode
     *            The rounding mode
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #argument(MathContext)
     * @since 1
     */
    protected abstract BigDecimal argument(int precision, RoundingMode roundingMode);

    /**
     * Returns the argument of this {@link AbstractComplexNumber} considering the
     * given {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see AbstractComplexNumber#argument(MathContext)
     * @since 1
     */
    protected abstract BigDecimal argument(MathContext mathContext);

    /**
     * Return the corresponding polar form of the complex number
     *
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @see #polarForm(MathContext)
     * @since 1
     */
    protected abstract PolarForm polarForm();

    /**
     * Return the corresponding polar form of the complex number considering the
     * given precision
     *
     * @param precision
     *            The precision
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @see #polarForm(MathContext)
     * @since 1
     */
    protected abstract PolarForm polarForm(int precision);

    /**
     * Return the corresponding polar form of the complex number considering the
     * given rounding mode
     *
     * @param roundingMode
     *            The rounding mode
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #polarForm(MathContext)
     * @since 1
     */
    protected abstract PolarForm polarForm(RoundingMode roundingMode);

    /**
     * Return the corresponding polar form of the complex number considering the
     * given precision and rounding mode
     *
     * @param precision
     *            The precision
     * @param roundingMode
     *            The rounding mode
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #polarForm(MathContext)
     * @since 1
     */
    protected abstract PolarForm polarForm(int precision, RoundingMode roundingMode);

    /**
     * Return the corresponding polar form of the complex number considering the
     * given {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see AbstractComplexNumber#polarForm(MathContext)
     * @since 1
     */
    protected abstract PolarForm polarForm(MathContext mathContext);

    /**
     * Returns a matrix representation of this {@link AbstractComplexNumber}
     *
     * @return The matrix representation
     * @since 1
     */
    protected abstract M matrix();

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return Objects.hash(real, imaginary);
    }

    /**
     * {@inheritDoc}
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
     */
    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("real", real).add("imaginary", imaginary).toString();
    }

    public final B getReal() {
        return real;
    }

    public final B getImaginary() {
        return imaginary;
    }
}
