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

package finnmath.util

import finnmath.linear.BigIntMatrix
import finnmath.linear.BigIntVector
import finnmath.linear.DecimalMatrix
import finnmath.linear.DecimalVector
import finnmath.number.Fraction
import finnmath.number.RealComplexNumber
import finnmath.number.SimpleComplexNumber
import java.math.BigDecimal
import java.math.BigInteger
import org.assertj.core.api.Condition
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

final class MathRandomTest {
    val mathRandom = new MathRandom
    static val bound = 10
    static val howMany = 10
    static val validScale = 2
    val decimalBound = BigDecimal::valueOf(bound)
    val bigBound = BigInteger::valueOf(bound)
    static val validSize = 3
    static val validRowSize = 2
    static val validColumnSize = 3

    @Test
    def void nextPositiveLongBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveLong(0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
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
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
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
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
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
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextPositiveLongsShouldSucceed() {
        val integers = mathRandom.nextPositiveLongs(bound, howMany)
        assertEquals(howMany, integers.length)
        for (integer : integers) {
            assertTrue(0 <= integer)
            assertTrue(integer < bound)
        }
    }

    @Test
    def void nextNegativeLongsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeLongs(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextNegativeLongsShouldSucceed() {
        val integers = mathRandom.nextNegativeLongs(bound, howMany)
        assertEquals(howMany, integers.length)
        for (integer : integers) {
            assertTrue(-bound < integer)
            assertTrue(integer <= 0)
        }
    }

    @Test
    def void nextLongsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextLongs(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextLongsShouldSucceed() {
        val integers = mathRandom.nextLongs(bound, howMany)
        assertEquals(howMany, integers.length)
        for (integer : integers) {
            assertTrue(-bound < integer)
            assertTrue(integer < bound)
        }
    }

    @Test
    def void nextPositiveDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimal(0, validScale)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextPositiveDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextPositiveDecimalShouldSucceed() {
        val it = mathRandom.nextPositiveDecimal(bound, validScale)
        assertThat(it).isExactlyInstanceOf(BigDecimal)
        assertThat(it).isGreaterThanOrEqualTo(0BD).isLessThan(decimalBound)
        assertEquals(validScale, scale)
    }

    @Test
    def void nextNegativeDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimal(0, validScale)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextNegativeDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextNegativeDecimalShouldSucceed() {
        val it = mathRandom.nextNegativeDecimal(bound, validScale)
        assertThat(it).isExactlyInstanceOf(BigDecimal)
        assertThat(it).isGreaterThan(-decimalBound).isLessThanOrEqualTo(0BD)
        assertEquals(validScale, scale)
    }

    @Test
    def void nextDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimal(0, validScale)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimal(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDecimalShouldSucceed() {
        val it = mathRandom.nextDecimal(bound, validScale)
        assertThat(it).isExactlyInstanceOf(BigDecimal)
        assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
        assertEquals(validScale, scale)
    }

    @Test
    def void nextPositiveDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimals(0, validScale, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextPositiveDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimals(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextPositiveDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveDecimals(bound, validScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextPositiveDecimalsShouldSucceed() {
        val decimals = mathRandom.nextPositiveDecimals(bound, validScale, howMany)
        assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany).are(new Condition([ BigDecimal decimal |
            0BD <= decimal && decimal < decimalBound
        ], 'bounds')).are(new Condition([ BigDecimal decimal |
            decimal.scale === validScale
        ], 'scale'))
    }

    @Test
    def void nextNegativeDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimals(0, validScale, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextNegativeDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimals(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextNegativeDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeDecimals(bound, validScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextNegativeDecimalsShouldSucceed() {
        val decimals = mathRandom.nextNegativeDecimals(bound, validScale, howMany)
        assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany).are(new Condition([ BigDecimal decimal |
            -decimalBound < decimal && decimal <= 0BD
        ], 'bounds')).are(new Condition([ BigDecimal decimal |
            decimal.scale === validScale
        ], 'scale'))
    }

    @Test
    def void nextDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimals(0, validScale, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimals(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDecimalsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextDecimals(bound, validScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextDecimalsShouldSucceed() {
        val decimals = mathRandom.nextDecimals(bound, validScale, howMany)
        assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany).are(new Condition([ BigDecimal decimal |
            -decimalBound < decimal && decimal < decimalBound
        ], 'bounds')).are(new Condition([ BigDecimal decimal |
            decimal.scale === validScale
        ], 'scale'))
    }

    @Test
    def void nextPositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextPositiveFractionShouldSucceed() {
        val it = mathRandom.nextPositiveFraction(bound)
        assertThat(it).isExactlyInstanceOf(Fraction)
        assertThat(numerator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
        assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    }

    @Test
    def void nextNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextNegativeFractionShouldSucceed() {
        val it = mathRandom.nextNegativeFraction(bound)
        assertThat(it).isExactlyInstanceOf(Fraction)
        assertThat(numerator).isGreaterThan(-bigBound).isLessThanOrEqualTo(0BI)
        assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    }

    @Test
    def void nextFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextFraction(1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextFractionShouldSucceed() {
        val it = mathRandom.nextFraction(bound)
        assertThat(it).isExactlyInstanceOf(Fraction)
        assertThat(numerator).isGreaterThan(-bigBound).isLessThan(bigBound)
        assertThat(denominator).isGreaterThan(-bigBound).isLessThanOrEqualTo(bigBound)
    }

    @Test
    def void nextPositiveFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveFractions(1, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextPositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextPositiveFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextPositiveFractionsShouldSucceed() {
        val fractions = mathRandom.nextPositiveFractions(bound, howMany)
        assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany).are(new Condition([ Fraction fraction |
            0BI <= fraction.numerator && fraction.numerator < bigBound
        ], 'numerator')).are(new Condition([ Fraction fraction |
            0BI <= fraction.denominator && fraction.denominator < bigBound
        ], 'denominator'))
    }

    @Test
    def void nextNegativeFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeFractions(1, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextNegativeFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextNegativeFractionsShouldSucceed() {
        val fractions = mathRandom.nextNegativeFractions(bound, howMany)
        assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany).are(new Condition([ Fraction fraction |
            -bigBound < fraction.numerator && fraction.numerator <= 0BI
        ], 'numerator')).are(new Condition([ Fraction fraction |
            0BI <= fraction.denominator && fraction.denominator < bigBound
        ], 'denominator'))
    }

    @Test
    def void nextFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextFractions(1, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
    }

    @Test
    def void nextFractionsTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextFractions(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextFractionsShouldSucceed() {
        val fractions = mathRandom.nextFractions(bound, howMany)
        assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany).are(new Condition([ Fraction fraction |
            -bigBound < fraction.numerator && fraction.numerator < bigBound
        ], 'numerator')).are(new Condition([ Fraction fraction |
            -bigBound < fraction.denominator && fraction.denominator < bigBound
        ], 'denominator'))
    }

    @Test
    def void nextSimpleComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumber(0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSimpleComplexNumberShouldSucceed() {
        val it = mathRandom.nextSimpleComplexNumber(bound)
        assertThat(it).isExactlyInstanceOf(SimpleComplexNumber)
        assertThat(real).isGreaterThan(-bigBound).isLessThan(bigBound)
        assertThat(imaginary).isGreaterThan(-bigBound).isLessThan(bigBound)
    }

    @Test
    def void nextSimpleComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumbers(0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextSimpleComplexNumbers(bound, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextSimpleComplexNumbersShouldSucceed() {
        val complexNumbers = mathRandom.nextSimpleComplexNumbers(bound, howMany)
        assertThat(complexNumbers).hasOnlyElementsOfType(SimpleComplexNumber).hasSize(howMany).are(
            new Condition([ SimpleComplexNumber complexNumber |
                -bigBound < complexNumber.real && complexNumber.real < bigBound
            ], 'real')).are(new Condition([ SimpleComplexNumber complexNumber |
            -bigBound < complexNumber.imaginary && complexNumber.imaginary < bigBound
        ], 'imaginary'))
    }

    @Test
    def void nextRealComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumber(0, validScale)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextRealComplexNumberScaleTooLowShouldThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumber(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextRealComplexNumberShouldSucceed() {
        val it = mathRandom.nextRealComplexNumber(bound, validScale)
        assertThat(it).isExactlyInstanceOf(RealComplexNumber)
        assertThat(real).isGreaterThan(-decimalBound).isLessThan(decimalBound)
        assertThat(imaginary).isGreaterThan(-decimalBound).isLessThan(decimalBound)
        assertEquals(validScale, real.scale)
        assertEquals(validScale, imaginary.scale)
    }

    @Test
    def void nextRealComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumbers(0, validScale, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextRealComplexNumbersScaleTooLowShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumbers(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy[
            mathRandom.nextRealComplexNumbers(bound, validScale, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextRealComplexNumbersShouldSucceed() {
        val complexNumbers = mathRandom.nextRealComplexNumbers(bound, validScale, howMany)
        assertThat(complexNumbers).hasOnlyElementsOfType(RealComplexNumber).hasSize(howMany).are(
            new Condition([ RealComplexNumber complexNumber |
                0BD <= complexNumber.real && complexNumber.real < decimalBound
            ], 'real')).are(new Condition([ RealComplexNumber complexNumber |
            0BD <= complexNumber.imaginary && complexNumber.imaginary < decimalBound
        ], 'imaginary')).are(new Condition([ RealComplexNumber complexNumber |
            complexNumber.real.scale === validScale && complexNumber.imaginary.scale === validScale
        ], 'scale'))
    }

    @Test
    def void nextBigIntVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntVector(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextBigIntVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntVector(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextBigIntVectorShouldSucceed() {
        val it = mathRandom.nextBigIntVector(bound, validSize)
        assertThat(it).isInstanceOf(BigIntVector)
        assertEquals(validSize, size)
        assertThat(map.values).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bounds'))
    }

    @Test
    def void nextBigIntVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntVectors(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextBigIntVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntVectors(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextBigIntVectorsTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntVectors(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextBigIntVectorsShouldSucceed() {
        val vectors = mathRandom.nextBigIntVectors(bound, validSize, howMany)
        assertThat(vectors).hasOnlyElementsOfType(BigIntVector).hasSize(howMany).are(
            new Condition([ BigIntVector vector |
                vector.size === validSize
            ], 'size'))
        vectors.forEach [
            assertThat(map.values).are(new Condition([ BigInteger integer |
                -bigBound < integer && integer < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextDecimalVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVector(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalVectorScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVector(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextDecimalVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVector(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void nextDecimalVectorShouldSucceed() {
        val it = mathRandom.nextDecimalVector(bound, validScale, validSize)
        assertThat(it).isInstanceOf(DecimalVector)
        assertEquals(validSize, size)
        assertThat(map.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bounds')).are(new Condition([ BigDecimal entry |
            entry.scale === validScale
        ], 'scale'))
    }

    @Test
    def void nextDecimalVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVectors(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalVectorsScaleTooLowhouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVectors(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDecimalVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVectors(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextDecimalVectorsTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalVectors(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextDecimalVectorsShouldSucceed() {
        val vectors = mathRandom.nextDecimalVectors(bound, validScale, validSize, howMany)
        assertThat(vectors).hasOnlyElementsOfType(DecimalVector).hasSize(howMany).are(
            new Condition([ DecimalVector vector |
                vector.size === validSize
            ], 'size'))
        vectors.forEach [
            assertThat(map.values).are(new Condition([ BigDecimal decimal |
                -decimalBound <= decimal && decimal < decimalBound
            ], 'bound')).are(new Condition([ BigDecimal decimal |
                decimal.scale === validScale
            ], 'scale'))
        ]
    }

    @Test
    def void nextBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrix(0, validRowSize, validColumnSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrix(bound, 0, validColumnSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected rowSize > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrix(bound, validRowSize, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected columnSize > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextBigIntMatrix(bound, validRowSize, validColumnSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validRowSize, rowSize)
        assertEquals(validColumnSize, columnSize)
    }

    @Test
    def void nextUpperTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextUpperTriangularBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(upperTriangular)
    }

    @Test
    def void nextLowerTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextLowerTriangularBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(lowerTriangular)
    }

    @Test
    def void nextTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextTriangularBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextTriangularBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(triangular)
    }

    @Test
    def void nextDiagonalBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDiagonalBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextDiagonalBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextDiagonalBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(diagonal)
    }

    @Test
    def void nextSymmetricBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSymmetricBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSymmetricBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextSymmetricBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(symmetric)
    }

    @Test
    def void nextSkewSymmetricBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricBigIntMatrix(0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricBigIntMatrix(bound, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricBigIntMatrixShouldSucceed() {
        val it = mathRandom.nextSkewSymmetricBigIntMatrix(bound, validSize)
        assertThat(it).isExactlyInstanceOf(BigIntMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
            -bigBound < entry && entry < bigBound
        ], 'bound'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(skewSymmetric)
    }

    @Test
    def void nextBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrices(0, validRowSize, validColumnSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrices(bound, 0, validColumnSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected rowSize > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrices(bound, validRowSize, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected columnSize > 0 but actual 0')
    }

    @Test
    def void nextBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextBigIntMatrices(bound, validRowSize, validColumnSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextBigIntMatrices(bound, validRowSize, validColumnSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validRowSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validColumnSize
        ], 'column size'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextUpperTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextUpperTriangularBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextUpperTriangularBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.upperTriangular
        ], 'upper triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextLowerTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextLowerTriangularBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextLowerTriangularBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.lowerTriangular
        ], 'lower triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextTriangularBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextTriangularBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.triangular
        ], 'triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextDiagonalBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDiagonalBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextDiagonalBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextDiagonalBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextDiagonalBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.diagonal
        ], 'diagonal'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextSymmetricBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSymmetricBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSymmetricBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextSymmetricBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextSymmetricBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.symmetric
        ], 'symmetric'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextSkewSymmetricBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricBigIntMatrices(0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricBigIntMatrices(bound, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricBigIntMatrices(bound, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextSkewSymmetricBigIntMatricesShouldSucceed() {
        val matrices = mathRandom.nextSkewSymmetricBigIntMatrices(bound, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(BigIntMatrix).hasSize(howMany).are(
            new Condition([ BigIntMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ BigIntMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ BigIntMatrix matrix |
            matrix.skewSymmetric
        ], 'skewSymmetric'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigInteger).are(new Condition([ BigInteger entry |
                -bigBound < entry && entry < bigBound
            ], 'bound'))
        ]
    }

    @Test
    def void nextDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrix(0, validScale, validRowSize, validColumnSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrix(bound, 0, validRowSize, validColumnSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrix(bound, validScale, 0, validColumnSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected rowSize > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrix(bound, validScale, validRowSize, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected columnSize > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextDecimalMatrix(bound, validScale, validRowSize, validColumnSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound')).are(new Condition([ BigDecimal entry |
            entry.scale === validScale
        ], 'scale'))
        assertEquals(validRowSize, rowSize)
        assertEquals(validColumnSize, columnSize)
    }

    @Test
    def void nextUpperTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextUpperTriangularDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound'))
        cells.forEach [ cell |
            if (cell.rowKey <= cell.columnKey)
                assertEquals(validScale, cell.value.scale)
        ]
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(upperTriangular)
    }

    @Test
    def void nextLowerTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextLowerTriangularDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound'))
        cells.forEach [ cell |
            if (cell.rowKey >= cell.columnKey)
                assertEquals(validScale, cell.value.scale)
        ]
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(lowerTriangular)
    }

    @Test
    def void nextTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextTriangularDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound'))
        if (upperTriangular)
            cells.forEach [ cell |
                if (cell.rowKey <= cell.columnKey)
                    assertEquals(validScale, cell.value.scale)
            ]
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(triangular)
    }

    @Test
    def void nextDiagonalDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextDiagonalDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound'))
        cells.forEach [ cell |
            if (cell.rowKey == cell.columnKey)
                assertEquals(validScale, cell.value.scale)
        ]
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(diagonal)
    }

    @Test
    def void nextSymmetricDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextSymmetricDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound')).are(new Condition([ BigDecimal entry |
            entry.scale === validScale
        ], 'scale'))
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(symmetric)
    }

    @Test
    def void nextSkewSymmetricDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrix(0, validScale, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrix(bound, 0, validSize)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrix(bound, validScale, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatrixShouldSucceed() {
        val it = mathRandom.nextSkewSymmetricDecimalMatrix(bound, validScale, validSize)
        assertThat(it).isExactlyInstanceOf(DecimalMatrix)
        assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
            -decimalBound < entry && entry < decimalBound
        ], 'bound'))
        cells.forEach [ cell |
            if (cell.rowKey != cell.columnKey)
                assertEquals(validScale, cell.value.scale)
        ]
        assertEquals(validSize, rowSize)
        assertEquals(validSize, columnSize)
        assertTrue(skewSymmetric)
    }

    @Test
    def void nextDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrices(0, validScale, validRowSize, validColumnSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrices(bound, 0, validRowSize, validColumnSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrices(bound, validScale, 0, validColumnSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected rowSize > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected columnSize > 0 but actual 0')
    }

    @Test
    def void nextDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, validColumnSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, validColumnSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validRowSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validColumnSize
        ], 'column size'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound')).are(new Condition([ BigDecimal entry |
                entry.scale === validScale
            ], 'scale'))
        ]
    }

    @Test
    def void nextUpperTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextUpperTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextUpperTriangularDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.upperTriangular
        ], 'upper triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound'))
            cells.forEach [ cell |
                if (cell.rowKey <= cell.columnKey)
                    assertEquals(validScale, cell.value.scale)
            ]
        ]
    }

    @Test
    def void nextLowerTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextLowerTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextLowerTriangularDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.lowerTriangular
        ], 'lower triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound'))
            cells.forEach [ cell |
                if (cell.rowKey >= cell.columnKey)
                    assertEquals(validScale, cell.value.scale)
            ]
        ]
    }

    @Test
    def void nextTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextTriangularDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextTriangularDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextTriangularDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.triangular
        ], 'triangular'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound'))
            if (lowerTriangular)
                cells.forEach [ cell |
                    if (cell.rowKey >= cell.columnKey)
                        assertEquals(validScale, cell.value.scale)
                ]
            else
                cells.forEach [ cell |
                    if (cell.rowKey <= cell.columnKey)
                        assertEquals(validScale, cell.value.scale)
                ]
        ]
    }

    @Test
    def void nextDiagonalDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextDiagonalDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextDiagonalDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextDiagonalDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextDiagonalDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.diagonal
        ], 'diagonal'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound'))
            cells.forEach [ cell |
                if (cell.rowKey == cell.columnKey)
                    assertEquals(validScale, cell.value.scale)
            ]
        ]
    }

    @Test
    def void nextSymmetricDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSymmetricDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSymmetricDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextSymmetricDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextSymmetricDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.symmetric
        ], 'symmetric'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound')).are(new Condition([ BigDecimal entry |
                entry.scale === validScale
            ], 'scale'))
        ]
    }

    @Test
    def void nextSkewSymmetricDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrices(0, validScale, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, 0, validSize, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, 0, howMany)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected size > 0 but actual 0')
    }

    @Test
    def void nextSkewSymmetricDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy [
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, validSize, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected howMany > 1 but actual 1')
    }

    @Test
    def void nextSkewSymmetricDecimalMatricesShouldSucceed() {
        val matrices = mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, validSize, howMany)
        assertThat(matrices).hasOnlyElementsOfType(DecimalMatrix).hasSize(howMany).are(
            new Condition([ DecimalMatrix matrix |
                matrix.rowSize === validSize
            ], 'row size')).are(new Condition([ DecimalMatrix matrix |
            matrix.columnSize === validSize
        ], 'column size')).are(new Condition([ DecimalMatrix matrix |
            matrix.skewSymmetric
        ], 'skewSymmetric'))
        matrices.forEach [
            assertThat(table.values).hasOnlyElementsOfType(BigDecimal).are(new Condition([ BigDecimal entry |
                -decimalBound < entry && entry < decimalBound
            ], 'bound'))
            cells.forEach [ cell |
                if (cell.rowKey != cell.columnKey)
                    assertEquals(validScale, cell.value.scale)
            ]
        ]
    }
}
