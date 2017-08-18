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
import finnmath.linear.BigIntMatrix
import finnmath.util.SquareRootCalculator
import java.math.BigDecimal
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a complex number which uses {@code BigInteger} for the real and imaginary part
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@FinalFieldsConstructor
final class SimpleComplexNumber extends ComplexNumber<BigInteger, SimpleComplexNumber, RealComplexNumber, BigIntMatrix> {
    public static val ZERO = new SimpleComplexNumber(0BI, 0BI)
    public static val SimpleComplexNumber ONE = new SimpleComplexNumber(1BI, 0BI)
    public static val I = new SimpleComplexNumber(0BI, 1BI)

    /**
     * Returns the sum of this {@link SimpleComplexNumber} complex number and the given one
     * 
     * @param summand {@link SimpleComplexNumber}
     * @return sum {@link SimpleComplexNumber}
     * @throws NullPointerException if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override add(SimpleComplexNumber summand) {
        requireNonNull(summand, 'summand')
        new SimpleComplexNumber(real + summand.real, imaginary + summand.imaginary)
    }

    /**
     * Returns the difference of this {@link SimpleComplexNumber} and the given one
     * 
     * @param subtrahend {@link SimpleComplexNumber}
     * @return difference {@link SimpleComplexNumber}
     * @throws NullPointerException if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override subtract(SimpleComplexNumber subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        new SimpleComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
    }

    /**
     * Returns the product of this {@link SimpleComplexNumber} and the given one
     * 
     * @param factor {@link SimpleComplexNumber}
     * @return product {@link SimpleComplexNumber}
     * @throws NullPointerException if {@code factor == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override multiply(SimpleComplexNumber factor) {
        requireNonNull(factor, 'factor')
        val newReal = real * factor.real - imaginary * factor.imaginary
        val newImaginary = real * factor.imaginary + imaginary * factor.real
        new SimpleComplexNumber(newReal, newImaginary)
    }

    /**
     * Returns the quotient as {@link RealComplexNumber} of this {@link SimpleComplexNumber} and the given one
     * 
     * @param divisor {@link SimpleComplexNumber}
     * @return quotient {@link RealComplexNumber}
     * @throws NullPointerException if {@code divisor == null}
     * @throws IllegalArgumentException if {@code divisor == 0}
     * @since 1
     * @author Lars Tennstedt
     */
    override divide(SimpleComplexNumber divisor) {
        requireNonNull(divisor, 'divisor')
        checkArgument(divisor != ZERO, 'expected divisor != 0 but actual %s', divisor)
        new RealComplexNumber(this).divide(new RealComplexNumber(divisor))
    }

    /**
     * Returns the power of this {@link SimpleComplexNumber} raised by the given exponent
     * 
     * @param exponent int
     * @return power {@link SimpleComplexNumber}
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
     * Returns the negated {@link SimpleComplexNumber} of this one
     * 
     * @return negated {@link SimpleComplexNumber}
     * @since 1
     * @author Lars Tennstedt
     */
    override negate() {
        new SimpleComplexNumber(-real, -imaginary)
    }

    /**
     * Returns the inverted {@link SimpleComplexNumber} of this one
     * 
     * @return inverted {@link SimpleComplexNumber}
     * @throws IllegalStateException if {@code numerator == 0}
     * @since 1
     * @author Lars Tennstedt
     */
    override invert() {
        checkState(invertible, 'expected != 0 but actual %s', this)
        ONE.divide(this)
    }

    /**
     * Returns if this {@link SimpleComplexNumber} is invertible
     * 
     * @return {@code true} if {@code this != 0}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override invertible() {
        this != ZERO
    }

    /**
     * Returns a string representation of this {@link SimpleComplexNumber}
     * 
     * @return string {@link String}
     * @since 1
     * @author Lars Tennstedt
     */
    override asString() {
        if (real != 0BI)
            if (imaginary > 0BI)
                return '''«real» + «imaginary»i'''
            else if (imaginary < 0BI)
                return '''«real» - «imaginary.abs»i'''
            else
                return '''«real»'''
        if (imaginary > 0BI)
            return '''«imaginary»i'''
        else if (imaginary < 0BI)
            return '''- «imaginary»i'''
        '0'
    }

    /**
     * Returns the square of the absolute of this {@link SimpleComplexNumber}
     * 
     * @return square of the absolute {@link BigInteger}
     * @since 1
     * @author Lars Tennstedt
     */
    override absPow2() {
        real ** 2 + imaginary ** 2
    }

    /**
     * Returns the absolute as {@link RealComplexNumber} of this {@link SimpleComplexNumber}
     * 
     * @return absolute {@link BigDecimal}
     * @since 1
     * @author Lars Tennstedt
     */
    override abs() {
        SquareRootCalculator::sqrt(absPow2)
    }

    /**
     * Returns the conjugate of this {@link SimpleComplexNumber}
     * 
     * @return conjugated {@link SimpleComplexNumber}
     * @since 1
     * @author Lars Tennstedt
     */
    override conjugate() {
        new SimpleComplexNumber(real, -imaginary)
    }

    /**
     * Returns a matrix representation of this {@link SimpleComplexNumber}
     * 
     * @return matrix {@link BigIntMatrix}
     * @since 1
     * @author Lars Tennstedt
     */
    override matrix() {
        BigIntMatrix::builder(2, 2).put(1, 1, real).put(1, 2, -imaginary).put(2, 1, imaginary).put(2, 2, real).build
    }
}
