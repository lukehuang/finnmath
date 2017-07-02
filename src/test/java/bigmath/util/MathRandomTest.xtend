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

package bigmath.util

import java.math.BigDecimal
import java.math.BigInteger
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

final class MathRandomTest {
    val mathRandom = new MathRandom
    val bound = 10
    val howMany = 10
    val givenScale = 2
    val decimalBound = BigDecimal.valueOf(bound)
    val bigBound = BigInteger.valueOf(bound)

    @Test
    def void nextPositiveLongBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveLong(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveLongShouldSucceed() {
        val integer = mathRandom.nextPositiveLong(bound)
        assertTrue(0 <= integer)
        assertTrue(integer < bound)
    }

    @Test
    def void nextNegativeLongBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeLong(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeLongShouldSucceed() {
        val it = mathRandom.nextNegativeLong(bound)
        assertTrue(-bound < it)
        assertTrue(it <= 0)
    }

    @Test
    def void nextLongBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextLong(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextLongShouldSucceed() {
        val it = mathRandom.nextLong(bound)
        assertTrue(-bound < it)
        assertTrue(it < bound)
    }

    @Test
    def void nextPositiveLongsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveLongs(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveLongsShouldSucceed() {
        val it = mathRandom.nextPositiveLongs(bound, howMany)
        assertEquals(howMany, length)
    }

    @Test
    def void nextNegativeLongsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeLongs(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeLongsShouldSucceed() {
        val it = mathRandom.nextNegativeLongs(bound, howMany)
        assertEquals(howMany, length)
    }

    @Test
    def void nextLongsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextLongs(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextLongsShouldSucceed() {
        val it = mathRandom.nextLongs(bound, howMany)
        assertEquals(howMany, length)
    }

    @Test
    def void nextPositiveDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimal(0, givenScale)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveDecimalShouldSucceed() {
        val it = mathRandom.nextPositiveDecimal(bound, givenScale)
        assertThat(it).isGreaterThanOrEqualTo(0BD).isLessThan(decimalBound)
        assertEquals(givenScale, scale)
    }

    @Test
    def void nextNegativeDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimal(0, givenScale)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeDecimalShouldSucceed() {
        val it = mathRandom.nextNegativeDecimal(bound, givenScale)
        assertThat(it).isGreaterThan(-decimalBound).isLessThanOrEqualTo(0BD)
        assertEquals(givenScale, scale)
    }

    @Test
    def void nextDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimal(0, givenScale)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextDecimalShouldSucceed() {
        val it = mathRandom.nextDecimal(bound, givenScale)
        assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
        assertEquals(givenScale, scale)
    }

    @Test
    def void nextPositiveDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimals(bound, givenScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveDecimalsShouldSucceed() {
        val it = mathRandom.nextPositiveDecimals(bound, givenScale, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextNegativeDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimals(bound, givenScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeDecimalsShouldSucceed() {
        val it = mathRandom.nextNegativeDecimals(bound, givenScale, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimals(bound, givenScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextDecimalsShouldSucceed() {
        val it = mathRandom.nextDecimals(bound, givenScale, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextPositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveFractionShouldSucceed() {
        val it = mathRandom.nextPositiveFraction(bound)
        assertThat(numerator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
        assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    }

    @Test
    def void nextNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeFractionShouldSucceed() {
        val it = mathRandom.nextNegativeFraction(bound)
        assertThat(numerator).isGreaterThan(-bigBound).isLessThanOrEqualTo(0BI)
        assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    }

    @Test
    def void nextFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextFractionShouldSucceed() {
        val it = mathRandom.nextFraction(bound)
        assertThat(numerator).isGreaterThan(-bigBound).isLessThan(bigBound)
        assertThat(denominator).isGreaterThan(-bigBound).isLessThanOrEqualTo(bigBound)
    }

    @Test
    def void nextPositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextPositiveFractionsShouldSucceed() {
        val it = mathRandom.nextPositiveFractions(bound, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextNegativeFractionsShouldSucceed() {
        val it = mathRandom.nextNegativeFractions(bound, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextFractionsShouldSucceed() {
        val it = mathRandom.nextFractions(bound, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextSimpleComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumber(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextSimpleComplexNumberShouldSucceed() {
        val it = mathRandom.nextSimpleComplexNumber(bound)
        assertThat(real).isGreaterThan(-bigBound).isLessThan(bigBound)
        assertThat(imaginary).isGreaterThan(-bigBound).isLessThan(bigBound)
    }

    @Test
    def void nextSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumbers(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextSimpleComplexNumbersShouldSucceed() {
        val it = mathRandom.nextSimpleComplexNumbers(bound, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextRealComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumber(0, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextRealComplexNumberShouldSucceed() {
        val it = mathRandom.nextRealComplexNumber(bound, 1)
        assertThat(real).isGreaterThan(-decimalBound).isLessThan(decimalBound)
        assertThat(imaginary).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    }

    @Test
    def void nextRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumbers(bound, 1, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextRealComplexNumbersShouldSucceed() {
        val it = mathRandom.nextRealComplexNumbers(bound, 1, howMany)
        assertEquals(howMany, size)
    }

    @Test
    def void nextBigIntAndSqrtBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextBigIntAndSqrt(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextBigIntAndSqrtShouldSucceed() {
        val it = mathRandom.nextBigIntAndSqrt(bound)
        assertThat(number).isEqualTo(sqrt ** 2)
    }

    @Test
    def void nextFractionAndSqrtBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextFractionAndSqrt(1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextFractionAndSqrtShouldSucceed() {
        val it = mathRandom.nextFractionAndSqrt(bound)
        assertThat(number).isEqualTo(sqrt.pow(2))
    }

    @Test
    def void nextSimpleComplexNumberAndSqrtBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumberAndSqrt(0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextSimpleComplexNumberAndSqrtShouldSucceed() {
        val it = mathRandom.nextSimpleComplexNumberAndSqrt(bound)
        assertThat(number).isEqualTo(sqrt.pow(2))
    }
}
