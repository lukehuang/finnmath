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

import java.math.BigDecimal
import java.util.List
import mathmyday.lib.util.MathRandom
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

final class SimpleComplexNumberTest {
    static val ZERO = SimpleComplexNumber::ZERO
    static val ONE = SimpleComplexNumber::ONE
    static List<SimpleComplexNumber> complexNumbers
    static List<SimpleComplexNumber> others

    @BeforeClass
    def static void setUpClass() {
        val mathRandom = new MathRandom
        val bound = 10
        val howMany = 10
        complexNumbers = mathRandom.createSimpleComplexNumbers(bound, howMany)
        others = mathRandom.createSimpleComplexNumbers(bound, howMany)
    }

    @Test
    def void newRealNullShouldThrowException() {
        assertThatThrownBy[
            new SimpleComplexNumber(null, 0BI)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def void newImaginaryNullShouldThrowException() {
        assertThatThrownBy[
            new SimpleComplexNumber(0BI, null)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def void addNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.add(null)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def void testAdd() {
        complexNumbers.forEach [
            others.forEach [ other |
                val expectedReal = real + other.real
                val expectedImaginary = imaginary + other.imaginary
                assertThat(add(other)).isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary))
            ]
        ]
    }

    @Test
    def void subtractNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.subtract(null)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def void testSubtract() {
        complexNumbers.forEach [
            others.forEach [ other |
                val expectedReal = real - other.real
                val expectedImaginary = imaginary - other.imaginary
                assertThat(subtract(other)).isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary))
            ]
        ]
    }

    @Test
    def void multiplyNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.multiply(null)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def void testMultiply() {
        complexNumbers.forEach [
            others.forEach [ other |
                val expectedReal = real * other.real - imaginary * other.imaginary
                val expectedImaginary = imaginary * other.real + real * other.imaginary
                assertThat(multiply(other)).isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary))
            ]
        ]
    }

    @Test
    def void divideNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.divide(null)
        ].isInstanceOf(NullPointerException)
    }

    @Test
    def divideTest() {
        complexNumbers.forEach [
            others.forEach [ other |
                val denominator = new BigDecimal(other.real ** 2 + other.imaginary ** 2)
                val expectedReal = new BigDecimal(real * other.real + imaginary * other.imaginary) / denominator
                val expectedImaginary = new BigDecimal(imaginary * other.real - real * other.imaginary) / denominator
                new RealComplexNumber(expectedReal, expectedImaginary)
                assertThat(divide(other)).isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary))
            ]
        ]
    }

    @Test
    def void powNegativeExponentShouldThrowException() {
        assertThatThrownBy[
            ZERO.pow(-1)
        ].isInstanceOf(IllegalArgumentException)
    }

    @Test
    def void testPow() {
        complexNumbers.forEach [
            assertThat(pow(3)).isEqualTo(multiply(multiply(it)))
            assertThat(pow(2)).isEqualTo(multiply(it))
            assertThat(pow(1)).isSameAs(it)
            assertThat(pow(0)).isSameAs(ONE)
        ]
        assertThat(ONE.pow(3)).isEqualTo(ONE)
        assertThat(ZERO.pow(0)).isEqualTo(ONE)
    }

    @Test
    def void testNegate() {
        complexNumbers.forEach [
            assertThat(negate).isEqualTo(new SimpleComplexNumber(-real, -imaginary))
        ]
    }

    @Test
    def void testAbsPow2() {
        complexNumbers.forEach [
            assertThat(absPow2).isEqualTo(real ** 2 + imaginary ** 2)
        ]
    }

    @Test
    def void testConjugate() {
        complexNumbers.forEach [
            assertThat(conjugate).isEqualTo(new SimpleComplexNumber(real, -imaginary))
        ]
    }
}
