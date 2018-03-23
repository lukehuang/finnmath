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

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Comparator;

/**
 * An immutable implementation of a complex number which uses {@link BigDecimal}
 * as type for its real and imaginary part
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class RealComplexNumber extends AbstractComplexNumber<BigDecimal, RealComplexNumber, BigDecimalMatrix> {
    /**
     * {@code 0} as {@link RealComplexNumber}
     *
     * @since 1
     */
    public static final RealComplexNumber ZERO = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ZERO);

    /**
     * {@code 1} as {@link RealComplexNumber}
     *
     * @since 1
     */
    public static final RealComplexNumber ONE = new RealComplexNumber(BigDecimal.ONE, BigDecimal.ZERO);

    /**
     * {@code i} as {@link RealComplexNumber}
     *
     * @since 1
     */
    public static final RealComplexNumber IMAGINARY = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ONE);

    /**
     * Comparator
     *
     * @since 1
     */
    public static final RealComplexNumberComparator REAL_COMPLEX_NUMBER_COMPARATOR = new RealComplexNumberComparator();

    private RealComplexNumber(final BigDecimal real, final BigDecimal imaginary) {
        super(real, imaginary);
    }

    /**
     * Returns the sum of this {@link RealComplexNumber} and the given one
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     */
    @Override
    public RealComplexNumber add(final RealComplexNumber summand) {
        requireNonNull(summand, "summand");
        return new RealComplexNumber(real.add(summand.getReal()), imaginary.add(summand.getImaginary()));
    }

    /**
     * Returns the sum of this {@link RealComplexNumber} and the given one
     *
     * @param summand
     *            the summand
     * @param mathContext
     *            {@link MathContext}
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber add(final RealComplexNumber summand, final MathContext mathContext) {
        requireNonNull(summand, "summand");
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(real.add(summand.getReal(), mathContext),
            imaginary.add(summand.getImaginary(), mathContext));
    }

    /**
     * Returns the difference of this {@link RealComplexNumber} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @since 1
     */
    @Override
    public RealComplexNumber subtract(final RealComplexNumber subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        return new RealComplexNumber(real.subtract(subtrahend.getReal()),
            imaginary.subtract(subtrahend.getImaginary()));
    }

    /**
     * Returns the difference of this {@link RealComplexNumber} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @param mathContext
     *            {@link MathContext}
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber subtract(final RealComplexNumber subtrahend, final MathContext mathContext) {
        requireNonNull(subtrahend, "subtrahend");
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(real.subtract(subtrahend.getReal(), mathContext),
            imaginary.subtract(subtrahend.getImaginary(), mathContext));
    }

    /**
     * Returns the product of this {@link RealComplexNumber} and the given one
     *
     * @param factor
     *            the factor
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @since 1
     */
    @Override
    public RealComplexNumber multiply(final RealComplexNumber factor) {
        requireNonNull(factor, "factor");
        final BigDecimal newReal = real.multiply(factor.getReal()).subtract(imaginary.multiply(factor.getImaginary()));
        final BigDecimal newImaginary = real.multiply(factor.getImaginary()).add(imaginary.multiply(factor.getReal()));
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the product of this {@link RealComplexNumber} and the given one
     *
     * @param factor
     *            the factor
     * @param mathContext
     *            {@link MathContext}
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber multiply(final RealComplexNumber factor, final MathContext mathContext) {
        requireNonNull(factor, "factor");
        requireNonNull(mathContext, "mathContext");
        final BigDecimal newReal = real.multiply(factor.getReal(), mathContext)
            .subtract(imaginary.multiply(factor.getImaginary(), mathContext), mathContext);
        final BigDecimal newImaginary = real.multiply(factor.getImaginary(), mathContext)
            .add(imaginary.multiply(factor.getReal(), mathContext), mathContext);
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
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
     * @since 1
     */
    @Override
    public RealComplexNumber divide(final RealComplexNumber divisor, final RoundingMode roundingMode) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        requireNonNull(roundingMode, "roundingMode");
        final BigDecimal denominator = divisor.getReal().pow(2).add(divisor.getImaginary().pow(2));
        final BigDecimal newReal = real.multiply(divisor.getReal()).add(imaginary.multiply(divisor.getImaginary()))
            .divide(denominator, roundingMode);
        final BigDecimal newImaginary = imaginary.multiply(divisor.getReal())
            .subtract(real.multiply(divisor.getImaginary())).divide(denominator, roundingMode);
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @param scale
     *            scale
     * @param roundingMode
     *            the rounding mode
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #invertible
     * @since 1
     */
    public RealComplexNumber divide(final RealComplexNumber divisor, final int scale, final RoundingMode roundingMode) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        requireNonNull(roundingMode, "roundingMode");
        final BigDecimal denominator = divisor.getReal().pow(2).add(divisor.getImaginary().pow(2));
        final BigDecimal newReal = real.multiply(divisor.getReal()).add(imaginary.multiply(divisor.getImaginary()))
            .divide(denominator, scale, roundingMode);
        final BigDecimal newImaginary = imaginary.multiply(divisor.getReal())
            .subtract(real.multiply(divisor.getImaginary())).divide(denominator, scale, roundingMode);
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @param mathContext
     *            {@link MathContext}
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see #invertible
     * @since 1
     */
    public RealComplexNumber divide(final RealComplexNumber divisor, final MathContext mathContext) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        requireNonNull(mathContext, "mathContext");
        final BigDecimal denominator =
            divisor.getReal().pow(2, mathContext).add(divisor.getImaginary().pow(2, mathContext), mathContext);
        final BigDecimal newReal = real.multiply(divisor.getReal(), mathContext)
            .add(imaginary.multiply(divisor.getImaginary(), mathContext), mathContext).divide(denominator, mathContext);
        final BigDecimal newImaginary = imaginary.multiply(divisor.getReal(), mathContext)
            .subtract(real.multiply(divisor.getImaginary(), mathContext), mathContext).divide(denominator, mathContext);
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns power of this {@link RealComplexNumber} by the given exponent
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @see #pow
     * @see #multiply
     * @since 1
     */
    @Override
    public RealComplexNumber pow(final int exponent) {
        checkArgument(exponent > -1, "expected exponent > -1 but actual %s", exponent);
        if (exponent > 1) {
            return multiply(pow(exponent - 1));
        } else if (exponent == 1) {
            return this;
        }
        return ONE;
    }

    /**
     * Returns power of this {@link RealComplexNumber} by the given exponent
     *
     * @param exponent
     *            the exponent
     * @param mathContext
     *            {@link MathContext}
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see #pow
     * @see #multiply
     * @since 1
     */
    public RealComplexNumber pow(final int exponent, final MathContext mathContext) {
        checkArgument(exponent > -1, "expected exponent > -1 but actual %s", exponent);
        requireNonNull(mathContext, "mathContext");
        if (exponent > 1) {
            return multiply(pow(exponent - 1, mathContext), mathContext);
        } else if (exponent == 1) {
            return this;
        }
        return ONE;
    }

    /**
     * Returns the negated {@link RealComplexNumber} of this one
     *
     * @return The negated
     * @since 1
     */
    @Override
    public RealComplexNumber negate() {
        return new RealComplexNumber(real.negate(), imaginary.negate());
    }

    /**
     * Returns the negated {@link RealComplexNumber} of this one
     *
     * @param mathContext
     *            {@link MathContext}
     * @return The negated
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber negate(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(real.negate(mathContext), imaginary.negate(mathContext));
    }

    /**
     * Returns the inverted {@link RealComplexNumber} of this one
     *
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code this == 0}
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
     * Returns the inverted {@link RealComplexNumber} of this one
     *
     * @param roundingMode
     *            rounding mode
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #invertible
     * @see #divide
     * @since 1
     */
    public RealComplexNumber invert(final RoundingMode roundingMode) {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        requireNonNull(roundingMode, "roundingMode");
        return ONE.divide(this, roundingMode);
    }

    /**
     * Returns the inverted {@link RealComplexNumber} of this one
     *
     * @param scale
     *            scale
     * @param roundingMode
     *            Rounding mode
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @see #invertible
     * @see #divide
     * @since 1
     */
    public RealComplexNumber invert(final int scale, final RoundingMode roundingMode) {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        requireNonNull(roundingMode, "roundingMode");
        return ONE.divide(this, scale, roundingMode);
    }

    /**
     * Returns the inverted {@link RealComplexNumber} of this one
     *
     * @param mathContext
     *            Math context
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @see #invertible
     * @see #divide
     * @since 1
     */
    public RealComplexNumber invert(final MathContext mathContext) {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return ONE.divide(this, mathContext);
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumber}
     * is invertible
     *
     * @return {@code true} if {@code this != ZERO}, {@code false} otherwise
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
     * Returns the square of the absolute of this {@link RealComplexNumber}
     *
     * @return The square of the absolute
     * @since 1
     */
    @Override
    public BigDecimal absPow2() {
        return real.pow(2).add(imaginary.pow(2));
    }

    /**
     * Returns the conjugate of this {@link RealComplexNumber}
     *
     * @return The conjugated
     * @since 1
     */
    @Override
    public RealComplexNumber conjugate() {
        return new RealComplexNumber(real, imaginary.negate());
    }

    /**
     * Returns the conjugate of this {@link RealComplexNumber}
     *
     * @param mathContext
     *            Math context
     * @return The conjugated
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber conjugate(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(real, imaginary.negate(mathContext));
    }

    /**
     * Returns the argument of this {@link RealComplexNumber}
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
     * Returns the argument of this {@link RealComplexNumber} considering the given
     * precision
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
     * Returns the argument of this {@link RealComplexNumber} considering the given
     * rounding mode
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
     * Returns the argument of this {@link RealComplexNumber} considering the given
     * precision and rounding mode
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
     * Returns the argument of this {@link RealComplexNumber} considering the given
     * {@link MathContext}
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
        checkState(real.compareTo(BigDecimal.ZERO) != 0 || imaginary.compareTo(BigDecimal.ZERO) != 0,
            "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        final Context context = BigFloat.context(mathContext);
        if (real.compareTo(BigDecimal.ZERO) != 0) {
            final BigFloat arctan = BigFloat.atan(context.valueOf(imaginary.divide(real, mathContext)));
            if (real.compareTo(BigDecimal.ZERO) > 0) {
                return arctan.toBigDecimal();
            }
            final BigFloat pi = context.pi();
            return imaginary.compareTo(BigDecimal.ZERO) > -1 ? arctan.add(pi).toBigDecimal()
                : arctan.subtract(pi).toBigDecimal();
        }
        final BigDecimal piDividedByTwo = context.pi().divide(context.valueOf(BigDecimal.valueOf(2L))).toBigDecimal();
        return imaginary.compareTo(BigDecimal.ZERO) > 0 ? piDividedByTwo : piDividedByTwo.negate();
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
     * @see #abs
     * @see #argument(MathContext)
     * @since 1
     */
    @Override
    public PolarForm polarForm(final MathContext mathContext) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return new PolarForm(abs(), argument(mathContext));
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumber}
     * is equal to a given {@link RealComplexNumber} by comparing the real and
     * imaginary part
     *
     * @param other
     *            The other {@link RealComplexNumber}
     * @return {@code true} if equaliyt holds, {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @see BigDecimal#compareTo(BigDecimal)
     * @since 1
     */
    public boolean isEqualToByComparingParts(final RealComplexNumber other) {
        requireNonNull(other, "other");
        return real.compareTo(other.getReal()) == 0 && imaginary.compareTo(other.getImaginary()) == 0;
    }

    /**
     * Returns a matrix representation of this {@link RealComplexNumber}
     *
     * @return The matrix representation
     * @see BigDecimalMatrix#builder
     * @since 1
     */
    @Override
    public BigDecimalMatrix matrix() {
        return BigDecimalMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
            .put(2, 2, real).build();
    }

    /**
     * Returns a {@link RealComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link RealComplexNumber}
     * @since 1
     */
    public static RealComplexNumber of(final long real, final long imaginary) {
        return RealComplexNumber.of(BigInteger.valueOf(real), BigInteger.valueOf(imaginary));
    }

    /**
     * Returns a {@link RealComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link RealComplexNumber}
     * @since 1
     */
    public static RealComplexNumber of(final double real, final double imaginary) {
        return RealComplexNumber.of(BigDecimal.valueOf(real), BigDecimal.valueOf(imaginary));
    }

    /**
     * Returns a {@link RealComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link RealComplexNumber}
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     * @since 1
     */
    public static RealComplexNumber of(final BigInteger real, final BigInteger imaginary) {
        requireNonNull(real, "real");
        requireNonNull(imaginary, "imaginary");
        return RealComplexNumber.of(new BigDecimal(real), new BigDecimal(imaginary));
    }

    /**
     * Returns a {@link RealComplexNumber} based on the given real and imaginary
     * part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @return {@link RealComplexNumber}
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     * @since 1
     */
    public static RealComplexNumber of(final BigDecimal real, final BigDecimal imaginary) {
        requireNonNull(real, "real");
        requireNonNull(imaginary, "imaginary");
        return new RealComplexNumber(real, imaginary);
    }

    /**
     * Returns a {@link RealComplexNumber} based on the given
     * {@link SimpleComplexNumber}
     *
     * @param simpleComplexNumber
     *            {@link SimpleComplexNumber}
     * @return {@link RealComplexNumber}
     * @throws NullPointerException
     *             if {@code simpleComplexNumber == null}
     * @since 1
     */
    public static RealComplexNumber of(final SimpleComplexNumber simpleComplexNumber) {
        requireNonNull(simpleComplexNumber, "simpleComplexNumber");
        return of(simpleComplexNumber.getReal(), simpleComplexNumber.getImaginary());
    }

    /**
     * Comparator for {@link RealComplexNumber RealComplexNumbers}
     *
     * @since 1
     */
    @Beta
    public static final class RealComplexNumberComparator implements Comparator<RealComplexNumber> {
        private RealComplexNumberComparator() {
        }

        @Override
        public int compare(final RealComplexNumber first, final RealComplexNumber second) {
            if (first == null) {
                return -1;
            }
            if (second == null) {
                return 1;
            }
            if (first.equals(second)) {
                return 0;
            }
            return first.isEqualToByComparingParts(second) ? 0 : 1;
        }
    }
}
