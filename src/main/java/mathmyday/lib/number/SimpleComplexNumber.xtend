/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, togliu
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

package mathmyday.lib.number

import com.google.common.annotations.Beta
import java.math.BigInteger
import mathmyday.lib.linear.BigIntMatrix
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@Data
final class SimpleComplexNumber implements MathNumber<SimpleComplexNumber, RealComplexNumber>, ComplexNumber<BigInteger, SimpleComplexNumber, BigIntMatrix> {
    public static val ZERO = new SimpleComplexNumber(0BI, 0BI)
    public static val SimpleComplexNumber ONE = new SimpleComplexNumber(1BI, 0BI)
    public static val I = new SimpleComplexNumber(0BI, 1BI)
    BigInteger real
    BigInteger imaginary

    new(BigInteger real, BigInteger imaginary) {
        this.real = requireNonNull(real, 'real')
        this.imaginary = requireNonNull(imaginary, 'imaginary')
    }

    override add(SimpleComplexNumber summand) {
        requireNonNull(summand, 'summand')
        new SimpleComplexNumber(real + summand.real, imaginary + summand.imaginary)
    }

    override subtract(SimpleComplexNumber subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        new SimpleComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
    }

    override multiply(SimpleComplexNumber factor) {
        requireNonNull(factor, 'factor')
        val newReal = real * factor.real - imaginary * factor.imaginary
        val newImaginary = real * factor.imaginary + imaginary * factor.real
        new SimpleComplexNumber(newReal, newImaginary)
    }

    override divide(SimpleComplexNumber divisor) {
        requireNonNull(divisor, 'divisor')
        checkArgument(divisor != ZERO, 'expected divisor != 0 but actual %s', divisor)
        new RealComplexNumber(this).divide(new RealComplexNumber(divisor))
    }

    override pow(int exponent) {
        checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
        if (exponent > 1)
            return multiply(pow(exponent - 1))
        else if (exponent == 1)
            return this
        ONE
    }

    override negate() {
        new SimpleComplexNumber(-real, -imaginary)
    }

    override asString() {
        if (real != 0BI)
            if (imaginary > 0BI)
                return '''«real» + «imaginary»i'''
            else if (imaginary < 0BI)
                return '''«real» - «imaginary»i'''
            else
                return '''«real»'''
        if (imaginary > 0BI)
            return '''«imaginary»i'''
        else if (imaginary < 0BI)
            return '''- «imaginary»i'''
        '0'
    }

    override absPow2() {
        real ** 2 + imaginary ** 2
    }

    override conjugate() {
        new SimpleComplexNumber(real, -imaginary)
    }

    override matrix() {
        val builder = BigIntMatrix::builder(2, 2)
        builder.put(1, 1, real)
        builder.put(1, 2, -imaginary)
        builder.put(2, 1, imaginary)
        builder.put(2, 2, real)
        builder.build
    }
}
