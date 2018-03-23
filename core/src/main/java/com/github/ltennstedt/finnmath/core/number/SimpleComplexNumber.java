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
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * An immutable implementation of a complex number which uses {@link BigInteger}
 * as type for its real and imaginary part
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SimpleComplexNumber
    extends AbstractComplexNumber<BigInteger, SimpleComplexNumber, BigIntegerMatrix> {
    /**
     * {@code 0} as {@link SimpleComplexNumber}
     *
     * @since 1
     */
    public static final SimpleComplexNumber ZERO = new SimpleComplexNumber(BigInteger.ZERO, BigInteger.ZERO);

    /**
     * {@code 1} as {@link SimpleComplexNumber}
     *
     * @since 1
     */
    public static final SimpleComplexNumber ONE = new SimpleComplexNumber(BigInteger.ONE, BigInteger.ZERO);

    /**
     * {@code i} as {@link SimpleComplexNumber}
     *
     * @since 1
     */
    public static final SimpleComplexNumber IMAGINARY = new SimpleComplexNumber(BigInteger.ZERO, BigInteger.ONE);

    private SimpleComplexNumber(final BigInteger real, final BigInteger imaginary) {
        super(real, imaginary);
    }

    /**
     * Returns the sum of this {@link SimpleComplexNumber} and the given one
     *
     * @param summand
     *            The summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     */
    @Override
    public SimpleComplexNumber add(final SimpleComplexNumber summand) {
        requireNonNull(summand, "summand");
        return new SimpleComplexNumber(real.add(summand.getReal()), imaginary.add(summand.getImaginary()));
    }

    /**
     * Returns the difference of this {@link SimpleComplexNumber} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @since 1
     */
    @Override
    public SimpleComplexNumber subtract(final SimpleComplexNumber subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        return new SimpleComplexNumber(real.subtract(subtrahend.getReal()),
            imaginary.subtract(subtrahend.getImaginary()));
    }

    /**
     * Returns the product of this {@link SimpleComplexNumber} and the given one
     *
     * @param factor
     *            the factor
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @since 1
     */
    @Override
    public SimpleComplexNumber multiply(final SimpleComplexNumber factor) {
        requireNonNull(factor, "factor");
        final BigInteger newReal = real.multiply(factor.getReal()).subtract(imaginary.multiply(factor.getImaginary()));
        final BigInteger newImaginary = real.multiply(factor.getImaginary()).add(imaginary.multiply(factor.getReal()));
        return new SimpleComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the quotient of this {@link SimpleComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @param roundingMode
     *            the rounding mode
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #invertible
     * @see RealComplexNumber#divide(RealComplexNumber, RoundingMode)
     * @since 1
     */
    @Override
    public RealComplexNumber divide(final SimpleComplexNumber divisor, final RoundingMode roundingMode) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        requireNonNull(roundingMode, "roundingMode");
        return RealComplexNumber.of(this).divide(RealComplexNumber.of(divisor), roundingMode);
    }

    /**
     * Returns the power of this {@link SimpleComplexNumber} raised by the given
     * exponent
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @see #multiply
     * @since 1
     */
    @Override
    public SimpleComplexNumber pow(final int exponent) {
        checkArgument(exponent > -1, "expected exponent > -1 but actual %s", exponent);
        if (exponent > 1) {
            return multiply(pow(exponent - 1));
        } else if (exponent == 1) {
            return this;
        }
        return ONE;
    }

    /**
     * Returns the negated {@link SimpleComplexNumber} of this one
     *
     * @return The negated
     * @since 1
     */
    @Override
    public SimpleComplexNumber negate() {
        return new SimpleComplexNumber(real.negate(), imaginary.negate());
    }

    /**
     * Returns the inverted {@link SimpleComplexNumber} of this one
     *
     * @return The inverted {@link SimpleComplexNumber} of this one
     * @throws IllegalStateException
     *             if {@code numerator == 0}
     * @see #invertible
     * @see #divide
     * @since 1
     */
    @Override
    public RealComplexNumber invert() {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        return ONE.divide(this);
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link SimpleComplexNumber}
     * is invertible
     *
     * @return {@code true} if {@code this != 0}, {@code false} otherwise
     * @since 1
     */
    @Override
    public boolean invertible() {
        return !equals(ZERO);
    }

    /**
     * Returns the absolute value of the {@link RealComplexNumber}
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
    @Override
    public BigDecimal abs(final BigDecimal abortCriterion, final RoundingMode roundingMode) {
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(roundingMode, "roundingMode");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return SquareRootCalculator.sqrt(absPow2(), abortCriterion, roundingMode);
    }

    /**
     * Returns the absolute value of the {@link RealComplexNumber}
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
    @Override
    public BigDecimal abs(final BigDecimal abortCriterion, final MathContext mathContext) {
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(mathContext, "mathContext");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return SquareRootCalculator.sqrt(absPow2(), abortCriterion, mathContext);
    }

    /**
     * Returns the square of the absolute of this {@link SimpleComplexNumber}
     *
     * @return The square of the absolute
     * @since 1
     */
    @Override
    public BigInteger absPow2() {
        return real.pow(2).add(imaginary.pow(2));
    }

    /**
     * Returns the conjugate of this {@link SimpleComplexNumber}
     *
     * @return The conjugated
     * @since 1
     */
    @Override
    public SimpleComplexNumber conjugate() {
        return new SimpleComplexNumber(real, imaginary.negate());
    }

    /**
     * Returns the argument of this {@link SimpleComplexNumber}
     *
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @see #argument(MathContext)
     * @since 1
     */
    @Override
    public BigDecimal argument() {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        return argument(new MathContext(PolarForm.DEFAULT_PRECISION));
    }

    /**
     * Returns the argument of this {@link SimpleComplexNumber} considering the
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
    @Override
    public BigDecimal argument(final int precision) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        return argument(new MathContext(precision));
    }

    /**
     * Returns the argument of this {@link SimpleComplexNumber} considering the
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
    @Override
    public BigDecimal argument(final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(roundingMode, "roundingMode");
        return argument(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode));
    }

    /**
     * Returns the argument of this {@link SimpleComplexNumber} considering the
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
    @Override
    public BigDecimal argument(final int precision, final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        requireNonNull(roundingMode, "roundingMode");
        return argument(new MathContext(precision, roundingMode));
    }

    /**
     * Returns the argument of this {@link SimpleComplexNumber} considering the
     * given {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see RealComplexNumber#argument(MathContext)
     * @since 1
     */
    @Override
    public BigDecimal argument(final MathContext mathContext) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return RealComplexNumber.of(this).argument(mathContext);
    }

    /**
     * Return the corresponding polar form of the complex number
     *
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @see #polarForm(MathContext)
     * @since 1
     */
    @Override
    public PolarForm polarForm() {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        return polarForm(new MathContext(PolarForm.DEFAULT_PRECISION));
    }

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
    @Override
    public PolarForm polarForm(final int precision) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        return polarForm(new MathContext(precision));
    }

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
    @Override
    public PolarForm polarForm(final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(roundingMode, "roundingMode");
        return polarForm(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode));
    }

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
    @Override
    public PolarForm polarForm(final int precision, final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        requireNonNull(roundingMode, "roundingMode");
        return polarForm(new MathContext(precision, roundingMode));
    }

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
     * @see RealComplexNumber#polarForm(MathContext)
     * @since 1
     */
    @Override
    public PolarForm polarForm(final MathContext mathContext) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return RealComplexNumber.of(this).polarForm(mathContext);
    }

    /**
     * Returns a matrix representation of this {@link SimpleComplexNumber}
     *
     * @return The matrix representation
     * @see BigIntegerMatrix#builder
     * @since 1
     */
    @Override
    public BigIntegerMatrix matrix() {
        return BigIntegerMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
            .put(2, 2, real).build();
    }

    /**
     * Returns a {@link SimpleComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link SimpleComplexNumber}
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     * @since 1
     */
    public static SimpleComplexNumber of(final long real, final long imaginary) {
        return SimpleComplexNumber.of(BigInteger.valueOf(real), BigInteger.valueOf(imaginary));
    }

    /**
     * Returns a {@link SimpleComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link SimpleComplexNumber}
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     * @since 1
     */
    public static SimpleComplexNumber of(final BigInteger real, final BigInteger imaginary) {
        requireNonNull(real, "real");
        requireNonNull(imaginary, "imaginary");
        return new SimpleComplexNumber(real, imaginary);
    }
}
