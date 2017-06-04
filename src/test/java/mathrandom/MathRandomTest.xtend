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

package mathrandom

import java.math.BigDecimal
import java.math.BigInteger
import java.util.Random
import org.junit.BeforeClass
import org.junit.Test

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

class MathRandomTest {
    static val howMany = 100
    static val bounds = newIntArrayOfSize(howMany)
    static val scales = newIntArrayOfSize(howMany)
    static val sizes = newIntArrayOfSize(howMany)
    static val mathRandom = new MathRandom

    @BeforeClass
    def static setUpClass() {
        val random = new Random
        for (var i = 0; i < howMany; i++) {
            bounds.set(i, random.nextInt(Integer.MAX_VALUE))
            scales.set(i, random.nextInt(10))
        }
        for (var i = 0; i < 10; i++) {
            sizes.set(i, i + 1)
        }
    }

    @Test
    def createPositiveInt() {
        bounds.forEach [
            val integer = mathRandom.createPositiveInt(it)
            assertTrue(0 <= integer)
            assertTrue(integer < it)
        ]
    }

    @Test
    def createNegativeInt() {
        bounds.forEach [
            val integer = mathRandom.createNegativeInt(it)
            assertTrue(-it < integer)
            assertTrue(integer <= 0)
        ]
    }

    @Test
    def createInt() {
        bounds.forEach [
            val integer = mathRandom.createInt(it)
            assertTrue(-it < integer)
            assertTrue(integer < it)
        ]
    }

    @Test
    def createPositiveInts() {
        sizes.forEach [
            val size = mathRandom.createPositiveInts(2, it).length
            assertThat(size, is(it))
        ]
    }

    @Test
    def createNegativeInts() {
        sizes.forEach [
            val size = mathRandom.createNegativeInts(2, it).length
            assertThat(size, is(it))
        ]
    }

    @Test
    def createInts() {
        sizes.forEach [
            val size = mathRandom.createInts(2, it).length
            assertThat(size, is(it))
        ]
    }

    @Test
    def createPositiveDecimal() {
        bounds.forEach [ bound |
            val scale = scales.get(bounds.indexOf(bound))
            val decimal = mathRandom.createPositiveDecimal(bound, scale)
            assertTrue(BigDecimal.ZERO <= decimal)
            assertTrue(decimal < BigDecimal.valueOf(bound))
        ]
    }

    @Test
    def createNegativeDecimal() {
        bounds.forEach [ bound |
            val scale = scales.get(bounds.indexOf(bound))
            val decimal = mathRandom.createNegativeDecimal(bound, scale)
            assertTrue(BigDecimal.valueOf(bound).negate < decimal)
            assertTrue(decimal <= BigDecimal.ZERO)
        ]
    }

    @Test
    def createDecimal() {
        bounds.forEach [ bound |
            val scale = scales.get(bounds.indexOf(bound))
            val decimal = mathRandom.createDecimal(bound, scale)
            val newBound = BigDecimal.valueOf(bound)
            assertTrue(-newBound < decimal)
            assertTrue(decimal < newBound)
        ]
    }

    @Test
    def createPositiveDecimals() {
        sizes.forEach [
            val size = mathRandom.createPositiveDecimals(2, 1, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createNegativeDecimals() {
        sizes.forEach [
            val size = mathRandom.createNegativeDecimals(2, 1, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createDecimals() {
        sizes.forEach [
            val size = mathRandom.createDecimals(2, 1, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createPositiveFraction() {
        bounds.forEach [
            val fraction = mathRandom.createPositiveFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(BigInteger.ZERO <= fraction.numerator)
            assertTrue(fraction.numerator < bound)
            assertTrue(BigInteger.ZERO < fraction.denominator)
            assertTrue(fraction.denominator < bound)
        ]
    }

    @Test
    def createNegativeFraction() {
        bounds.forEach [
            val fraction = mathRandom.createNegativeFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(-bound < fraction.numerator)
            assertTrue(fraction.numerator <= BigInteger.ZERO)
            assertTrue(BigInteger.ZERO < fraction.denominator)
            assertTrue(fraction.denominator < bound)
        ]
    }

    @Test
    def createFraction() {
        bounds.forEach [
            val fraction = mathRandom.createFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(-bound < fraction.numerator)
            assertTrue(fraction.numerator < bound)
            assertTrue(-bound < fraction.denominator)
            assertTrue(fraction.denominator < bound)
            assertFalse(fraction.denominator == BigInteger.ZERO)
        ]
    }

    @Test
    def createPositiveFractions() {
        sizes.forEach [
            val size = mathRandom.createPositiveFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createNegativeFractions() {
        sizes.forEach [
            val size = mathRandom.createNegativeFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createFractions() {
        sizes.forEach [
            val size = mathRandom.createFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createInvertiblePositiveFraction() {
        bounds.forEach [
            val fraction = mathRandom.createInvertiblePositiveFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(BigInteger.ZERO < fraction.numerator)
            assertTrue(fraction.numerator < bound)
            assertTrue(BigInteger.ZERO < fraction.denominator)
            assertTrue(fraction.denominator < bound)
        ]
    }

    @Test
    def createInvertibleNegativeFraction() {
        bounds.forEach [
            val fraction = mathRandom.createInvertibleNegativeFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(-bound < fraction.numerator)
            assertTrue(fraction.numerator < BigInteger.ZERO)
            assertTrue(BigInteger.ZERO < fraction.denominator)
            assertTrue(fraction.denominator < bound)
        ]
    }

    @Test
    def createInvertibleFraction() {
        bounds.forEach [
            val fraction = mathRandom.createInvertibleFraction(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(-bound < fraction.numerator)
            assertTrue(fraction.numerator < bound)
            assertFalse(fraction.numerator == BigInteger.ZERO)
            assertTrue(-bound < fraction.denominator)
            assertTrue(fraction.denominator < bound)
            assertFalse(fraction.denominator == BigInteger.ZERO)
        ]
    }

    @Test
    def createInvertiblePositiveFractions() {
        sizes.forEach [
            val size = mathRandom.createInvertiblePositiveFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createInvertibleNegativeFractions() {
        sizes.forEach [
            val size = mathRandom.createInvertibleNegativeFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createInvertibleFractions() {
        sizes.forEach [
            val size = mathRandom.createInvertibleFractions(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createSimpleComplexNumber() {
        bounds.forEach [
            val complexNumber = mathRandom.createSimpleComplexNumber(it)
            val bound = BigInteger.valueOf(it)
            assertTrue(-bound < complexNumber.real)
            assertTrue(complexNumber.real < bound)
            assertTrue(-bound < complexNumber.imaginary)
            assertTrue(complexNumber.imaginary < bound)
        ]
    }

    @Test
    def createSimpleComplexNumbers() {
        sizes.forEach [
            val size = mathRandom.createSimpleComplexNumbers(2, it).size
            assertThat(size, is(it))
        ]
    }

    @Test
    def createRealComplexNumber() {
        bounds.forEach [
            val complexNumber = mathRandom.createRealComplexNumber(it)
            val bound = BigDecimal.valueOf(it)
            assertTrue(-bound < complexNumber.real)
            assertTrue(complexNumber.real < bound)
            assertTrue(-bound < complexNumber.imaginary)
            assertTrue(complexNumber.imaginary < bound)
        ]
    }

    @Test
    def createRealComplexNumbers() {
        sizes.forEach [
            val size = mathRandom.createRealComplexNumbers(2, it).size
            assertThat(size, is(size))
        ]
    }
}
