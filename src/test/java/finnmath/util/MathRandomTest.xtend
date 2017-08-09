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

package finnmath.util

import finnmath.linear.BigIntVector
import finnmath.linear.DecimalVector
import finnmath.number.BigIntAndSqrt
import finnmath.number.Fraction
import finnmath.number.FractionAndSqrt
import finnmath.number.RealComplexNumber
import finnmath.number.SimpleComplexNumber
import finnmath.number.SimpleComplexNumberAndSqrt
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class MathRandomTest {
  val mathRandom = new MathRandom
  val bound = 10
  val howMany = 10
  val validScale = 2
  val decimalBound = BigDecimal.valueOf(bound)
  val bigBound = BigInteger.valueOf(bound)
  val validSize = 3

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
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextPositiveLongsShouldSucceed() {
    val integers = mathRandom.nextPositiveLongs(bound, howMany)
    assertEquals(howMany, integers.length)
    integers.forEach [
      assertTrue(0 <= it)
      assertTrue(it < bound)
    ]
  }

  @Test
  def void nextNegativeLongsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextNegativeLongs(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextNegativeLongsShouldSucceed() {
    val integers = mathRandom.nextNegativeLongs(bound, howMany)
    assertEquals(howMany, integers.length)
    integers.forEach [
      assertTrue(-bound < it)
      assertTrue(it <= 0)
    ]
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
    integers.forEach [
      assertTrue(-bound < it)
      assertTrue(it < bound)
    ]
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
    ].isExactlyInstanceOf(IllegalArgumentException)
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
    ].isExactlyInstanceOf(IllegalArgumentException)
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
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextDecimalShouldSucceed() {
    val it = mathRandom.nextDecimal(bound, validScale)
    assertThat(it).isExactlyInstanceOf(BigDecimal)
    assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    assertEquals(validScale, scale)
  }

  @Test
  def void nextPositiveDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextPositiveDecimals(bound, validScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextPositiveDecimalsShouldSucceed() {
    val decimals = mathRandom.nextPositiveDecimals(bound, validScale, howMany)
    assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany)
    decimals.forEach [
      assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
      assertEquals(validScale, scale)
    ]
  }

  @Test
  def void nextNegativeDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextNegativeDecimals(bound, validScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextNegativeDecimalsShouldSucceed() {
    val decimals = mathRandom.nextNegativeDecimals(bound, validScale, howMany)
    assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany)
    decimals.forEach [
      assertThat(it).isGreaterThan(-decimalBound).isLessThanOrEqualTo(0BD)
      assertEquals(validScale, scale)
    ]
  }

  @Test
  def void nextDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextDecimals(bound, validScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextDecimalsShouldSucceed() {
    val decimals = mathRandom.nextDecimals(bound, validScale, howMany)
    assertThat(decimals).hasOnlyElementsOfType(BigDecimal).hasSize(howMany)
    decimals.forEach [
      assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
      assertEquals(validScale, scale)
    ]
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
  def void nextPositiveFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextPositiveFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextPositiveFractionsShouldSucceed() {
    val fractions = mathRandom.nextPositiveFractions(bound, howMany)
    assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany)
    fractions.forEach [
      assertThat(numerator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
      assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    ]
  }

  @Test
  def void nextNegativeFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextNegativeFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextNegativeFractionsShouldSucceed() {
    val fractions = mathRandom.nextNegativeFractions(bound, howMany)
    assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany)
    fractions.forEach [
      assertThat(numerator).isGreaterThan(-bigBound).isLessThanOrEqualTo(0BI)
      assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    ]
  }

  @Test
  def void nextFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextFractionsShouldSucceed() {
    val fractions = mathRandom.nextFractions(bound, howMany)
    assertThat(fractions).hasOnlyElementsOfType(Fraction).hasSize(howMany)
    fractions.forEach [
      assertThat(numerator).isGreaterThan(-bigBound).isLessThan(bigBound)
      assertThat(denominator).isGreaterThan(-bigBound).isLessThanOrEqualTo(bigBound)
    ]
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
  def void nextSimpleComplexNumbersTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextSimpleComplexNumbers(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextSimpleComplexNumbersShouldSucceed() {
    val complexNumbers = mathRandom.nextSimpleComplexNumbers(bound, howMany)
    assertThat(complexNumbers).hasOnlyElementsOfType(SimpleComplexNumber).hasSize(howMany)
    complexNumbers.forEach [
      assertThat(real).isGreaterThan(-bigBound).isLessThan(bigBound)
      assertThat(imaginary).isGreaterThan(-bigBound).isLessThan(bigBound)
    ]
  }

  @Test
  def void nextRealComplexNumberBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.nextRealComplexNumber(0, validScale)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
  }

  @Test
  def void nextRealComplexNumberShouldSucceed() {
    val it = mathRandom.nextRealComplexNumber(bound, validScale)
    assertThat(it).isExactlyInstanceOf(RealComplexNumber)
    assertThat(real).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    assertThat(imaginary).isGreaterThan(-decimalBound).isLessThan(decimalBound)
  }

  @Test
  def void nextRealComplexNumbersTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.nextRealComplexNumbers(bound, validScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextRealComplexNumbersShouldSucceed() {
    val complexNumbers = mathRandom.nextRealComplexNumbers(bound, validScale, howMany)
    assertThat(complexNumbers).hasOnlyElementsOfType(RealComplexNumber).hasSize(howMany)
    complexNumbers.forEach [
      assertThat(real).isGreaterThan(-decimalBound).isLessThan(decimalBound)
      assertThat(imaginary).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    ]
  }

  @Test
  def void nextBigIntAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.nextBigIntAndSqrt(0)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
  }

  @Test
  def void nextBigIntAndSqrtShouldSucceed() {
    val it = mathRandom.nextBigIntAndSqrt(bound)
    assertThat(it).isExactlyInstanceOf(BigIntAndSqrt)
    assertThat(number).isEqualTo(sqrt ** 2)
  }

  @Test
  def void nextFractionAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.nextFractionAndSqrt(1)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 1 but actual 1')
  }

  @Test
  def void nextFractionAndSqrtShouldSucceed() {
    val it = mathRandom.nextFractionAndSqrt(bound)
    assertThat(it).isExactlyInstanceOf(FractionAndSqrt)
    assertThat(number).isEqualTo(sqrt.pow(2))
  }

  @Test
  def void nextSimpleComplexNumberAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.nextSimpleComplexNumberAndSqrt(0)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected bound > 0 but actual 0')
  }

  @Test
  def void nextSimpleComplexNumberAndSqrtShouldSucceed() {
    val it = mathRandom.nextSimpleComplexNumberAndSqrt(bound)
    assertThat(it).isExactlyInstanceOf(SimpleComplexNumberAndSqrt)
    assertThat(number).isEqualTo(sqrt.pow(2))
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
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void nextBigIntVectorShouldSucceed() {
    val it = mathRandom.nextBigIntVector(bound, validSize)
    assertThat(it).isInstanceOf(BigIntVector)
    assertEquals(validSize, size)
    map.values.forEach [ entry |
      assertThat(entry).isGreaterThan(-bigBound).isLessThan(bigBound)
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
    map.values.forEach [ entry |
      assertThat(entry).isGreaterThan(-decimalBound).isLessThan(decimalBound)
      assertEquals(validScale, entry.scale)
    ]
  }
}
