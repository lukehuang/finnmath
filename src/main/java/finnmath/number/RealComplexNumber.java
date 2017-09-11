/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2017, Lars Tennstedt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package finnmath.number;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import finnmath.linear.DecimalMatrix;
import finnmath.util.SquareRootCalculator;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An immutable implementation of a complex number which uses {@link BigDecimal}
 * as type for its real and imaginary part
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class RealComplexNumber
        extends ComplexNumber<BigDecimal, RealComplexNumber, RealComplexNumber, DecimalMatrix> {
    /**
     * {@code 0} as {@link RealComplexNumber}
     */
    public static final RealComplexNumber ZERO = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ZERO);

    /**
     * {@code 1} as {@link RealComplexNumber}
     */
    public static final RealComplexNumber ONE = new RealComplexNumber(BigDecimal.ONE, BigDecimal.ZERO);

    /**
     * {@code i} as {@link RealComplexNumber}
     */
    public static RealComplexNumber I = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ONE);

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
     * @since 1
     * @author Lars Tennstedt
     */
    public RealComplexNumber(final BigDecimal real, final BigDecimal imaginary) {
        super(real, imaginary);
    }

    /**
     * Constructs a {@link RealComplexNumber} by the given
     * {@link SimpleComplexNumber}
     *
     * @param complexNumber
     *            {@link SimpleComplexNumber}
     * @throws NullPointerException
     *             if {@code complexNumber == null}
     * @since 1
     * @author Lars Tennstedt
     */
    public RealComplexNumber(final SimpleComplexNumber complexNumber) {
        super(new BigDecimal(requireNonNull(complexNumber, "complexNumber").getReal()),
                new BigDecimal(complexNumber.getImaginary()));
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
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber add(final RealComplexNumber summand) {
        requireNonNull(summand, "summand");
        return new RealComplexNumber(real.add(summand.getReal()), imaginary.add(summand.getImaginary()));
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
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber subtract(final RealComplexNumber subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        return new RealComplexNumber(real.subtract(subtrahend.getReal()),
                imaginary.subtract(subtrahend.getImaginary()));
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
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber multiply(final RealComplexNumber factor) {
        requireNonNull(factor, "factor");
        final BigDecimal newReal = real.multiply(factor.getReal()).subtract(imaginary.multiply(factor.getImaginary()));
        final BigDecimal newImaginary = real.multiply(factor.getImaginary()).add(imaginary.multiply(factor.getReal()));
        return new RealComplexNumber(newReal, newImaginary);
    }

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber divide(final RealComplexNumber divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        final BigDecimal denominator = divisor.getReal().pow(2).add(divisor.getImaginary().pow(2));
        final BigDecimal newReal = real.multiply(divisor.getReal()).add(imaginary.multiply(divisor.getImaginary()))
                .divide(denominator, BigDecimal.ROUND_HALF_UP);
        final BigDecimal newImaginary = imaginary.multiply(divisor.getReal())
                .subtract(real.multiply(divisor.getImaginary())).divide(denominator, BigDecimal.ROUND_HALF_UP);
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
     * @since 1
     * @author Lars Tennstedt
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
     * Returns the negated {@link RealComplexNumber} of this one
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber negate() {
        return new RealComplexNumber(real.negate(), imaginary.negate());
    }

    /**
     * Returns the inverted {@link RealComplexNumber} of this one
     *
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code this == 0}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber invert() {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        return ONE.divide(this);
    }

    /**
     * Returns if this {@link RealComplexNumber} is invertible
     *
     * @return {@code true} if {@code this != ZERO}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public boolean invertible() {
        return !equals(ZERO);
    }

    /**
     * Returns the absolute of this {@link RealComplexNumber}
     *
     * @return The absolute
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigDecimal abs() {
        return SquareRootCalculator.sqrt(absPow2());
    }

    /**
     * Returns the square of the absolute of this {@link RealComplexNumber}
     *
     * @return The square of the absolute
     * @since 1
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
     */
    @Override
    public RealComplexNumber conjugate() {
        return new RealComplexNumber(real, imaginary.negate());
    }

    /**
     * Returns a matrix representation of this {@link RealComplexNumber}
     *
     * @return The matrix representation
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public DecimalMatrix matrix() {
        return DecimalMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
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
        if (!(object instanceof RealComplexNumber)) {
            return false;
        }
        final RealComplexNumber other = (RealComplexNumber) object;
        return real.equals(other.getReal()) && imaginary.equals(other.getImaginary());
    }
}
