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

package mathmyday.lib.util

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
  def void createPositiveIntBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveInt(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveIntShouldSucceed() {
    val integer = mathRandom.createPositiveInt(bound)
    assertTrue(0 <= integer)
    assertTrue(integer < bound)
  }

  @Test
  def void createNegativeIntBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeInt(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeIntShouldSucceed() {
    val it = mathRandom.createNegativeInt(bound)
    assertTrue(-bound < it)
    assertTrue(it <= 0)
  }

  @Test
  def void createIntBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createInt(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createIntShouldSucceed() {
    val it = mathRandom.createInt(bound)
    assertTrue(-bound < it)
    assertTrue(it < bound)
  }

  @Test
  def void createPositiveIntsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveInts(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveIntsShouldSucceed() {
    val it = mathRandom.createPositiveInts(bound, howMany)
    assertEquals(howMany, length)
  }

  @Test
  def void createNegativeIntsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeInts(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeIntsShouldSucceed() {
    val it = mathRandom.createNegativeInts(bound, howMany)
    assertEquals(howMany, length)
  }

  @Test
  def void createIntsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createInts(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createIntsShouldSucceed() {
    val it = mathRandom.createInts(bound, howMany)
    assertEquals(howMany, length)
  }

  @Test
  def void createPositiveDecimalBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveDecimal(0, givenScale)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveDecimalScaleTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveDecimal(bound, 0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveDecimalShouldSucceed() {
    val it = mathRandom.createPositiveDecimal(bound, givenScale)
    assertThat(it).isGreaterThanOrEqualTo(0BD).isLessThan(decimalBound)
    assertEquals(givenScale, scale)
  }

  @Test
  def void createNegativeDecimalBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeDecimal(0, givenScale)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeDecimalScaleTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeDecimal(bound, 0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeDecimalShouldSucceed() {
    val it = mathRandom.createNegativeDecimal(bound, givenScale)
    assertThat(it).isGreaterThan(-decimalBound).isLessThanOrEqualTo(0BD)
    assertEquals(givenScale, scale)
  }

  @Test
  def void createDecimalBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createDecimal(0, givenScale)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createDecimalScaleTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createDecimal(bound, 0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createDecimalShouldSucceed() {
    val it = mathRandom.createDecimal(bound, givenScale)
    assertThat(it).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    assertEquals(givenScale, scale)
  }

  @Test
  def void createPositiveDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveDecimals(bound, givenScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveDecimalsShouldSucceed() {
    val it = mathRandom.createPositiveDecimals(bound, givenScale, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createNegativeDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeDecimals(bound, givenScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeDecimalsShouldSucceed() {
    val it = mathRandom.createNegativeDecimals(bound, givenScale, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createDecimalsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createDecimals(bound, givenScale, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createDecimalsShouldSucceed() {
    val it = mathRandom.createDecimals(bound, givenScale, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createPositiveFractionBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveFraction(1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveFractionShouldSucceed() {
    val it = mathRandom.createPositiveFraction(bound)
    assertThat(numerator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
    assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
  }

  @Test
  def void createNegativeFractionBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeFraction(1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeFractionShouldSucceed() {
    val it = mathRandom.createNegativeFraction(bound)
    assertThat(numerator).isGreaterThan(-bigBound).isLessThanOrEqualTo(0BI)
    assertThat(denominator).isGreaterThanOrEqualTo(0BI).isLessThan(bigBound)
  }

  @Test
  def void createFractionBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createFraction(1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createFractionShouldSucceed() {
    val it = mathRandom.createFraction(bound)
    assertThat(numerator).isGreaterThan(-bigBound).isLessThan(bigBound)
    assertThat(denominator).isGreaterThan(-bigBound).isLessThanOrEqualTo(bigBound)
  }

  @Test
  def void createPositiveFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createPositiveFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createPositiveFractionsShouldSucceed() {
    val it = mathRandom.createPositiveFractions(bound, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createNegativeFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createNegativeFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createNegativeFractionsShouldSucceed() {
    val it = mathRandom.createNegativeFractions(bound, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createFractionsTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createFractions(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createFractionsShouldSucceed() {
    val it = mathRandom.createFractions(bound, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createSimpleComplexNumberBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createSimpleComplexNumber(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createSimpleComplexNumberShouldSucceed() {
    val it = mathRandom.createSimpleComplexNumber(bound)
    assertThat(real).isGreaterThan(-bigBound).isLessThan(bigBound)
    assertThat(imaginary).isGreaterThan(-bigBound).isLessThan(bigBound)
  }

  @Test
  def void createSimpleComplexNumbersTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createSimpleComplexNumbers(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createSimpleComplexNumbersShouldSucceed() {
    val it = mathRandom.createSimpleComplexNumbers(bound, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createRealComplexNumberBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createRealComplexNumber(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createRealComplexNumberShouldSucceed() {
    val it = mathRandom.createRealComplexNumber(bound)
    assertThat(real).isGreaterThan(-decimalBound).isLessThan(decimalBound)
    assertThat(imaginary).isGreaterThan(-decimalBound).isLessThan(decimalBound)
  }

  @Test
  def void createRealComplexNumbersTooLessShoudThrowException() {
    assertThatThrownBy[
      mathRandom.createRealComplexNumbers(bound, 1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createRealComplexNumbersShouldSucceed() {
    val it = mathRandom.createRealComplexNumbers(bound, howMany)
    assertEquals(howMany, size)
  }

  @Test
  def void createBigIntAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createBigIntAndSqrt(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createBigIntAndSqrtShouldSucceed() {
    val it = mathRandom.createBigIntAndSqrt(bound)
    assertThat(number).isEqualTo(sqrt ** 2)
  }

  @Test
  def void createFractionAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createFractionAndSqrt(1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createFractionAndSqrtShouldSucceed() {
    val it = mathRandom.createFractionAndSqrt(bound)
    assertThat(number).isEqualTo(sqrt.pow(2))
  }

  @Test
  def void createSimpleComplexNumberAndSqrtBoundTooLowShouldThrowException() {
    assertThatThrownBy[
      mathRandom.createSimpleComplexNumberAndSqrt(0)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void createSimpleComplexNumberAndSqrtShouldSucceed() {
    val it = mathRandom.createSimpleComplexNumberAndSqrt(bound)
    assertThat(number).isEqualTo(sqrt.pow(2))
  }
}
