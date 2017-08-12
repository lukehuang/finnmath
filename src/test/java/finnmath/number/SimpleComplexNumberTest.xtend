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
 * IMPLIED WARRANTIES OF MERCHANTABDLITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABDLITY, WHETHER IN CONTRACT, STRICT LIABDLITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBDLITY OF SUCH DAMAGE.
 */

package finnmath.number

import finnmath.linear.BigIntMatrix
import finnmath.util.MathRandom
import finnmath.util.SquareRootCalculator
import java.math.BigDecimal
import java.math.BigInteger
import java.util.List
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

final class SimpleComplexNumberTest {
  static val ZERO = SimpleComplexNumber::ZERO
  static val ONE = SimpleComplexNumber::ONE
  static val I = SimpleComplexNumber::I
  static List<SimpleComplexNumber> complexNumbers
  static List<SimpleComplexNumber> others
  static List<SimpleComplexNumber> invertibles

  @BeforeClass
  def static void setUpClass() {
    val mathRandom = new MathRandom
    val bound = 10
    val howMany = 10
    complexNumbers = mathRandom.nextSimpleComplexNumbers(bound, howMany)
    others = mathRandom.nextSimpleComplexNumbers(bound, howMany)
    invertibles = mathRandom.nextInvertibleSimpleComplexNumbers(bound, howMany)
  }

  @Test
  def void newRealNullShouldThrowException() {
    assertThatThrownBy[
      new SimpleComplexNumber(null, 0BI)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('real')
  }

  @Test
  def void newImaginaryNullShouldThrowException() {
    assertThatThrownBy[
      new SimpleComplexNumber(0BI, null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('imaginary')
  }

  @Test
  def void addNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.add(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('summand')
  }

  @Test
  def void addShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real + other.real
        val expectedImaginary = imaginary + other.imaginary
        assertThat(add(other)).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(
          new SimpleComplexNumber(expectedReal, expectedImaginary))
      ]
    ]
  }

  @Test
  def void addZeroShouldBeEqualToZero() {
    complexNumbers.forEach [
      assertThat(add(ZERO)).isEqualTo(it)
    ]
  }

  @Test
  def void addShouldBeCommutative() {
    complexNumbers.forEach [
      others.forEach [ other |
        assertThat(add(other)).isEqualTo(other.add(it))
      ]
    ]
  }

  @Test
  def void addShouldBeAssociative() {
    complexNumbers.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          assertThat(add(other).add(invertible)).isEqualTo(add(other.add(invertible)))
        ]
      ]
    ]
  }

  @Test
  def void subtractNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.subtract(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('subtrahend')
  }

  @Test
  def void subtractShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real - other.real
        val expectedImaginary = imaginary - other.imaginary
        assertThat(subtract(other)).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(
          new SimpleComplexNumber(expectedReal, expectedImaginary))
      ]
    ]
  }

  @Test
  def void subtractZeroShouldBeEqualToSelf() {
    complexNumbers.forEach [
      assertThat(subtract(ZERO)).isEqualTo(it)
    ]
  }

  @Test
  def void subtractSelfShouldBeEqualToZero() {
    complexNumbers.forEach [
      assertThat(subtract(it)).isEqualTo(ZERO)
    ]
  }

  @Test
  def void multiplyNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.multiply(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('factor')
  }

  @Test
  def void multiplyShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real * other.real - imaginary * other.imaginary
        val expectedImaginary = imaginary * other.real + real * other.imaginary
        assertThat(multiply(other)).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(
          new SimpleComplexNumber(expectedReal, expectedImaginary))
      ]
    ]
  }

  @Test
  def void multiplyOneShouldBeEqualToOne() {
    complexNumbers.forEach [
      assertThat(multiply(ONE)).isEqualTo(it)
    ]
  }

  @Test
  def void multiplyZeroShouldBeEqualToZero() {
    complexNumbers.forEach [
      assertThat(multiply(ZERO)).isEqualTo(ZERO)
    ]
  }

  @Test
  def void multiplyShouldBeCommutative() {
    complexNumbers.forEach [
      others.forEach [ other |
        assertThat(multiply(other)).isEqualTo(other.multiply(it))
      ]
    ]
  }

  @Test
  def void multiplyShouldBeAssociative() {
    complexNumbers.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          assertThat(multiply(other).multiply(invertible)).isEqualTo(multiply(other.multiply(invertible)))
        ]
      ]
    ]
  }

  @Test
  def void addAndMultiplyShouldBeDistributive() {
    complexNumbers.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          assertThat(multiply(other.add(invertible))).isEqualTo(multiply(other).add(multiply(invertible)))
        ]
      ]
    ]
  }

  @Test
  def void divideNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.divide(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('divisor')
  }

  @Test
  def void divideZeroShouldThrowException() {
    assertThatThrownBy [
      ONE.divide(ZERO)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('''expected divisor != 0 but actual «ZERO»''')
  }

  @Test
  def divideShouldSucceed() {
    complexNumbers.forEach [
      invertibles.forEach [ invertible |
        val denominator = new BigDecimal(invertible.real ** 2 + invertible.imaginary ** 2)
        val expectedReal = new BigDecimal(real * invertible.real + imaginary * invertible.imaginary) / denominator
        val expectedImaginary = new BigDecimal(imaginary * invertible.real - real * invertible.imaginary) / denominator
        assertThat(divide(invertible)).isExactlyInstanceOf(RealComplexNumber).isEqualTo(
          new RealComplexNumber(expectedReal, expectedImaginary))
      ]
    ]
  }

  @Test
  def divideOneShouldBeEqualToSelf() {
    complexNumbers.forEach [
      assertThat(divide(ONE)).isEqualTo(new RealComplexNumber(it))
    ]
  }

  @Test
  def void powNegativeExponentShouldThrowException() {
    assertThatThrownBy[
      ZERO.pow(-1)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected exponent > -1 but actual -1')
  }

  @Test
  def void powShouldSucceed() {
    complexNumbers.forEach [
      assertThat(pow(3)).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(multiply(multiply(it)))
      assertThat(pow(2)).isEqualTo(multiply(it))
    ]
  }

  @Test
  def void powOneShouldBeTheSame() {
    complexNumbers.forEach [
      assertThat(pow(1)).isSameAs(it)
    ]
  }

  @Test
  def void powZeroShouldBeSameAsOne() {
    complexNumbers.forEach [
      assertThat(pow(0)).isSameAs(ONE)
    ]
  }

  @Test
  def void powOfOneShouldBeEqualToOne() {
    assertThat(ONE.pow(3)).isEqualTo(ONE)
  }

  @Test
  def void powOfZeroForExponentNotEqualToZeroShouldBeEqualToZero() {
    assertThat(ZERO.pow(3)).isEqualTo(ZERO)
  }

  @Test
  def void powOfIForExponentNotEqualToZeroShouldBeEqualToMinusOne() {
    assertThat(I.pow(2)).isEqualTo(ONE.negate)
  }

  @Test
  def void negateShouldSucceed() {
    complexNumbers.forEach [
      assertThat(negate).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(new SimpleComplexNumber(-real, -imaginary))
    ]
  }

  @Test
  def void negateZeroShouldBeEqualToSelf() {
    assertThat(ZERO.negate).isEqualTo(ZERO)
  }

  @Test
  def void addNegatedShouldBeEqualToZero() {
    complexNumbers.forEach [
      assertThat(add(negate)).isEqualTo(ZERO)
    ]
  }

  @Test
  def void multiplyMinusOneShouldBeEqualToNegated() {
    complexNumbers.forEach [
      assertThat(multiply(ONE.negate)).isEqualTo(negate)
    ]
  }

  @Test
  def void divideMinusOneShouldBeEqualToNegated() {
    complexNumbers.forEach [
      assertThat(divide(ONE.negate)).isEqualTo(new RealComplexNumber(negate))
    ]
  }

  @Test
  def void absPow2ShouldSucceed() {
    complexNumbers.forEach [
      assertThat(absPow2).isExactlyInstanceOf(BigInteger).isEqualTo(real ** 2 + imaginary ** 2)
    ]
  }

  @Test
  def void absPow2ZeroShouldBeEqualToZero() {
    assertThat(ZERO.absPow2).isEqualTo(0BI)
  }

  @Test
  def void absPow2OneShouldBeEqualToOne() {
    assertThat(ONE.absPow2).isEqualTo(1BI)
  }

  @Test
  def void absPow2MinusOneShouldBeSameAsOne() {
    assertThat(ONE.negate.absPow2).isEqualTo(1BI)
  }

  @Test
  def void absShouldSucceed() {
    complexNumbers.forEach [
      assertThat(abs).isExactlyInstanceOf(BigDecimal).isEqualTo(SquareRootCalculator::sqrt(absPow2))
    ]
  }

  @Test
  def void conjugateShouldSucceed() {
    complexNumbers.forEach [
      assertThat(conjugate).isExactlyInstanceOf(SimpleComplexNumber).isEqualTo(
        new SimpleComplexNumber(real, -imaginary))
    ]
  }

  @Test
  def void conjugateRealNumberShouldBeEqualToSelf() {
    complexNumbers.forEach [
      val realNumber = new SimpleComplexNumber(real, 0BI)
      assertThat(realNumber.conjugate).isEqualTo(realNumber)
    ]
  }

  @Test
  def void matrixShouldSucceed() {
    complexNumbers.forEach [
      val expected = BigIntMatrix::builder(2, 2).put(1, 1, real).put(1, 2, -imaginary).put(2, 1, imaginary).put(2, 2,
        real).build
      assertThat(matrix).isExactlyInstanceOf(BigIntMatrix).isEqualTo(expected)
    ]
  }
}
