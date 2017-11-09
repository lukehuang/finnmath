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

package com.github.ltennstedt.finnmath.number;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.linear.BigIntMatrix;
import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * An immutable implementation of a complex number which uses {@link BigInteger} as type for its real and imaginary part
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SimpleComplexNumber
    extends AbstractComplexNumber<BigInteger, SimpleComplexNumber, RealComplexNumber, BigIntMatrix> {
    /**
     * {@code 0} as {@link SimpleComplexNumber}
     */
    public static final SimpleComplexNumber ZERO = new SimpleComplexNumber(BigInteger.ZERO, BigInteger.ZERO);

    /**
     * {@code 1} as {@link SimpleComplexNumber}
     */
    public static final SimpleComplexNumber ONE = new SimpleComplexNumber(BigInteger.ONE, BigInteger.ZERO);

    /**
     * {@code i} as {@link SimpleComplexNumber}
     */
    public static final SimpleComplexNumber IMAGINARY = new SimpleComplexNumber(BigInteger.ZERO, BigInteger.ONE);

    /**
     * Constructs a {@link SimpleComplexNumber} by the given real and imaginary part
     *
     * @param real
     *            the real part
     * @param imaginary
     *            the imaginary part
     * @throws NullPointerException
     *             if {@code real == null}
     * @throws NullPointerException
     *             if {@code imaginary == null}
     * @author Lars Tennstedt
     * @since 1
     */
    public SimpleComplexNumber(final BigInteger real, final BigInteger imaginary) {
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
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
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
     * Returns the quotient as {@link RealComplexNumber} of this {@link SimpleComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code !divisor.invertble}
     * @author Lars Tennstedt
     * @see #invertible
     * @see RealComplexNumber#divide
     * @since 1
     */
    @Override
    public RealComplexNumber divide(final SimpleComplexNumber divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return new RealComplexNumber(this).divide(new RealComplexNumber(divisor));
    }

    /**
     * Returns the power of this {@link SimpleComplexNumber} raised by the given exponent
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
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
     * Returns a {@code boolean} which indicates if this {@link SimpleComplexNumber} is invertible
     *
     * @return {@code true} if {@code this != 0}, {@code false} otherwise
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public boolean invertible() {
        return !equals(ZERO);
    }

    /**
     * Returns the absolute as {@link RealComplexNumber} of this {@link SimpleComplexNumber}
     *
     * @return The absolute
     * @author Lars Tennstedt
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal abs() {
        return new SquareRootCalculator().sqrt(absPow2());
    }

    /**
     * Returns the square of the absolute of this {@link SimpleComplexNumber}
     *
     * @return The square of the absolute
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public SimpleComplexNumber conjugate() {
        return new SimpleComplexNumber(real, imaginary.negate());
    }

    /**
     * Returns the argument of this complex number
     *
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @author Lars Tennstedt
     * @since 1
     * @see #argument(MathContext)
     */
    @Override
    public BigDecimal argument() {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        return argument(new MathContext(PolarForm.DEFAULT_PRECISION));
    }

    /**
     * Returns the argument of this complex number considering the given precision
     *
     * @param precision
     *            The precision
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @author Lars Tennstedt
     * @since 1
     * @see #argument(MathContext)
     */
    @Override
    public BigDecimal argument(final int precision) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        return argument(new MathContext(precision));
    }

    /**
     * Returns the argument of this complex number considering the given rounding mode
     *
     * @param roundingMode
     *            The rounding mode
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @author Lars Tennstedt
     * @since 1
     * @see #argument(MathContext)
     */
    @Override
    public BigDecimal argument(final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(roundingMode, "roundingMode");
        return argument(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode));
    }

    /**
     * Returns the argument of this complex number considering the given precision and rounding mode
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
     * @author Lars Tennstedt
     * @since 1
     * @see #argument(MathContext)
     */
    @Override
    public BigDecimal argument(final int precision, final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        requireNonNull(roundingMode, "roundingMode");
        return argument(new MathContext(precision, roundingMode));
    }

    /**
     * Returns the argument of this complex number considering the given {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The argument
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @author Lars Tennstedt
     * @since 1
     * @see RealComplexNumber#argument(MathContext)
     */
    @Override
    public BigDecimal argument(final MathContext mathContext) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(this).argument(mathContext);
    }

    /**
     * Return the corresponding polar form of the complex number
     *
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @author Lars Tennstedt
     * @since 1
     * @see #polarForm(MathContext)
     */
    @Override
    public PolarForm polarForm() {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        return polarForm(new MathContext(PolarForm.DEFAULT_PRECISION));
    }

    /**
     * Return the corresponding polar form of the complex number considering the given precision
     *
     * @param precision
     *            The precision
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @author Lars Tennstedt
     * @since 1
     * @see #polarForm(MathContext)
     */
    @Override
    public PolarForm polarForm(final int precision) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        return polarForm(new MathContext(precision));
    }

    /**
     * Return the corresponding polar form of the complex number considering the given rounding mode
     *
     * @param roundingMode
     *            The rounding mode
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @author Lars Tennstedt
     * @since 1
     * @see #polarForm(MathContext)
     */
    @Override
    public PolarForm polarForm(final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(roundingMode, "roundingMode");
        return polarForm(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode));
    }

    /**
     * Return the corresponding polar form of the complex number considering the given precision and rounding mode
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
     * @author Lars Tennstedt
     * @since 1
     * @see #polarForm(MathContext)
     */
    @Override
    public PolarForm polarForm(final int precision, final RoundingMode roundingMode) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        requireNonNull(roundingMode, "roundingMode");
        return polarForm(new MathContext(precision, roundingMode));
    }

    /**
     * Return the corresponding polar form of the complex number considering the given {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The polar form
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @author Lars Tennstedt
     * @since 1
     * @see RealComplexNumber#polarForm(MathContext)
     */
    @Override
    public PolarForm polarForm(final MathContext mathContext) {
        checkState(!equals(ZERO), "expected this != 0 but actual %s", this);
        requireNonNull(mathContext, "mathContext");
        return new RealComplexNumber(this).polarForm(mathContext);
    }

    /**
     * Returns a matrix representation of this {@link SimpleComplexNumber}
     *
     * @return The matrix representation
     *
     * @author Lars Tennstedt
     * @see BigIntMatrix#builder
     * @since 1
     */
    @Override
    public BigIntMatrix matrix() {
        return BigIntMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
            .put(2, 2, real).build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimpleComplexNumber)) {
            return false;
        }
        final SimpleComplexNumber other = (SimpleComplexNumber) object;
        return real.equals(other.getReal()) && imaginary.equals(other.getImaginary());
    }
}
