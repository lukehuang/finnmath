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

package finnmath.number

import com.google.common.annotations.Beta
import finnmath.linear.DecimalMatrix
import finnmath.util.SquareRootCalculator
import java.math.BigDecimal
import java.util.Optional
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a complex number which uses {@link BigDecimal} as type for its real and imaginary 
 * part
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@FinalFieldsConstructor
final class RealComplexNumber extends ComplexNumber<BigDecimal, RealComplexNumber, RealComplexNumber, DecimalMatrix> {
    /**
     * {@code 0} as {@link RealComplexNumber}
     */
    public static val ZERO = new RealComplexNumber(0BD, 0BD)

    /**
     * {@code 1} as {@link RealComplexNumber}
     */
    public static val ONE = new RealComplexNumber(1BD, 0BD)

    /**
     * {@code i} as {@link RealComplexNumber}
     */
    public static val I = new RealComplexNumber(0BD, 1BD)

    /**
     * Constructs a {@link RealComplexNumber} by the given {@link SimpleComplexNumber} 
     * 
     * @param complexNumber {@link SimpleComplexNumber}
     * @throws NullPointerException if {@code complexNumber == null}
     * @since 1
     * @author Lars Tennstedt
     */
    new(SimpleComplexNumber complexNumber) {
        super(new BigDecimal(requireNonNull(complexNumber, 'complexNumber').real),
            new BigDecimal(complexNumber.imaginary))
    }

    /**
     * Returns the sum of this {@link RealComplexNumber} and the given one
     * 
     * @param summand the summand
     * @return The sum
     * @throws NullPointerException if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override add(RealComplexNumber summand) {
        requireNonNull(summand, 'summand')
        new RealComplexNumber(real + summand.real, imaginary + summand.imaginary)
    }

    /**
     * Returns the difference of this {@link RealComplexNumber} and the given one
     * 
     * @param subtrahend the subtrahend
     * @return The difference
     * @throws NullPointerException if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override subtract(RealComplexNumber subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        new RealComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
    }

    /**
     * Returns the product of this {@link RealComplexNumber} and the given one
     * 
     * @param factor the factor
     * @return The product
     * @throws NullPointerException if {@code factor == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override multiply(RealComplexNumber factor) {
        requireNonNull(factor, 'factor')
        val newReal = real * factor.real - imaginary * factor.imaginary
        val newImaginary = real * factor.imaginary + imaginary * factor.real
        new RealComplexNumber(newReal, newImaginary)
    }

    /**
     * Returns the quotient of this {@link RealComplexNumber} and the given one
     * 
     * @param divisor the divisor
     * @return The quotient
     * @throws NullPointerException if {@code divisor == null}
     * @throws IllegalArgumentException if {@code divisor == 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #invert
     */
    override divide(RealComplexNumber divisor) {
        requireNonNull(divisor, 'divisor')
        checkArgument(divisor.invertible, 'expected divisor to be invertible but actual %s', divisor)
        val denominator = divisor.real ** 2 + divisor.imaginary ** 2
        val newReal = (real * divisor.real + imaginary * divisor.imaginary) / denominator
        val newImaginary = (imaginary * divisor.real - real * divisor.imaginary) / denominator
        new RealComplexNumber(newReal, newImaginary)
    }

    /**
     * Returns power of this {@link RealComplexNumber} by the given exponent
     * 
     * @param exponent the exponent
     * @return The power
     * @throws IllegalArgumentException if {@code exponent < 0}
     * @since 1
     * @author Lars Tennstedt
     */
    override pow(int exponent) {
        checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
        if (exponent > 1)
            return multiply(pow(exponent - 1))
        else if (exponent == 1)
            return this
        ONE
    }

    /**
     * Returns the negated {@link RealComplexNumber} of this one
     * 
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     */
    override negate() {
        new RealComplexNumber(-real, -imaginary)
    }

    /**
     * Returns an {@link Optional} with the inverted {@link RealComplexNumber } of this one if this 
     * {@link RealComplexNumber} is invertible and an emtpy {@link Optional} otherwise
     * 
     * @return The optional inverted
     * @since 1
     * @author Lars Tennstedt
     * @see #invertible
     */
    override invert() {
        if (invertible) Optional.of(ONE.divide(this)) else Optional.empty
    }

    /**
     * Returns if this {@link RealComplexNumber} is invertible
     * 
     * @return {@code true} if {@code this != ZERO}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override invertible() {
        this != ZERO
    }

    /**
     * Returns a string representation of this {@link RealComplexNumber}
     * 
     * @return The string representation
     * @since 1
     * @author Lars Tennstedt
     */
    override asString() {
        if (real != 0BD)
            if (imaginary > 0BD)
                return '''«real» + «imaginary»i'''
            else if (imaginary < 0BD)
                return '''«real» - «imaginary»i'''
            else
                return '''«real»'''
        if (imaginary > 0BD)
            return '''«imaginary»i'''
        else if (imaginary < 0BD)
            return '''- «imaginary»i'''
        '0'
    }

    /**
     * Returns the square of the absolute of this {@link RealComplexNumber}
     * 
     * @return The square of the absolute
     * @since 1
     * @author Lars Tennstedt
     */
    override absPow2() {
        real ** 2 + imaginary ** 2
    }

    /**
     * Returns the absolute of this {@link RealComplexNumber}
     * 
     * @return The absolute
     * @since 1
     * @author Lars Tennstedt
     */
    override abs() {
        SquareRootCalculator::sqrt(absPow2)
    }

    /**
     * Returns the conjugate of this {@link RealComplexNumber}
     * 
     * @return The conjugated
     * @since 1
     * @author Lars Tennstedt
     */
    override conjugate() {
        new RealComplexNumber(real, -imaginary)
    }

    /**
     * Returns a matrix representation of this {@link RealComplexNumber}
     * 
     * @return The matrix representation
     * @since 1
     * @author Lars Tennstedt
     */
    override matrix() {
        DecimalMatrix::builder(2, 2).put(1, 1, real).put(1, 2, -imaginary).put(2, 1, imaginary).put(2, 2, real).build
    }
}
