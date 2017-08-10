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

import finnmath.util.MathRandom
import java.util.List
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

final class RealComplexNumberTest {
  static val ZERO = RealComplexNumber::ZERO
  static val ONE = RealComplexNumber::ONE
  static val I = RealComplexNumber::I
  static List<RealComplexNumber> complexNumbers
  static List<RealComplexNumber> others
  static List<RealComplexNumber> invertibles

  @BeforeClass
  def static void setUpClass() {
    val mathRandom = new MathRandom
    val bound = 10
    val scale = 2
    val howMany = 10
    complexNumbers = mathRandom.nextRealComplexNumbers(bound, scale, howMany)
    others = mathRandom.nextRealComplexNumbers(bound, scale, howMany)
    invertibles = mathRandom.nextInvertibleRealComplexNumbers(bound, scale, howMany)
  }

  @Test
  def void newRealNullShouldThrowException() {
    assertThatThrownBy[
      new RealComplexNumber(null, 0BD)
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void newImaginaryNullShouldThrowException() {
    assertThatThrownBy[
      new RealComplexNumber(0BD, null)
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void addNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.add(null)
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void addShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real + other.real
        val expectedImaginary = imaginary + other.imaginary
        assertThat(add(other)).isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary))
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
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void subtractShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real - other.real
        val expectedImaginary = imaginary - other.imaginary
        assertThat(subtract(other)).isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary))
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
      val difference = subtract(it)
      val scaledReal = 0BD.scale = difference.real.scale
      val scaledImaginary = 0BD.scale = difference.imaginary.scale
      assertThat(difference).isEqualTo(new RealComplexNumber(scaledReal, scaledImaginary))
    ]
  }

  @Test
  def void multiplyNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.multiply(null)
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void multiplyShouldSucceed() {
    complexNumbers.forEach [
      others.forEach [ other |
        val expectedReal = real * other.real - imaginary * other.imaginary
        val expectedImaginary = imaginary * other.real + real * other.imaginary
        assertThat(multiply(other)).isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary))
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
      val it = multiply(ZERO)
      val scaledZero = new RealComplexNumber(0BD.scale = real.scale, 0BD.scale = imaginary.scale)
      assertThat(it).isEqualTo(scaledZero)
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
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def divideShouldSucceed() {
    complexNumbers.forEach [
      invertibles.forEach [ invertible |
        val denominator = invertible.real ** 2 + invertible.imaginary ** 2
        val expectedReal = (real * invertible.real + imaginary * invertible.imaginary) / denominator
        val expectedImaginary = (imaginary * invertible.real - real * invertible.imaginary) / denominator
        new RealComplexNumber(expectedReal, expectedImaginary)
        assertThat(divide(invertible)).isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary))
      ]
    ]
  }

  @Test
  def divideOneShouldBeEqualToSelf() {
    complexNumbers.forEach [
      assertThat(divide(ONE)).isEqualTo(it)
    ]
  }

  @Test
  def void powNegativeExponentShouldThrowException() {
    assertThatThrownBy[
      ZERO.pow(-1)
    ].isExactlyInstanceOf(IllegalArgumentException)
  }

  @Test
  def void powShouldSucceed() {
    complexNumbers.forEach [
      assertThat(pow(3)).isEqualTo(multiply(multiply(it)))
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
      assertThat(negate).isEqualTo(new RealComplexNumber(-real, -imaginary))
    ]
  }

  @Test
  def void negateZeroShouldBeEqualToSelf() {
    assertThat(ZERO.negate).isEqualTo(ZERO)
  }

  @Test
  def void addNegatedShouldBeEqualToZero() {
    complexNumbers.forEach [
      val it = add(negate)
      val scaledZero = new RealComplexNumber(0BD.scale = real.scale, 0BD.scale = imaginary.scale)
      assertThat(it).isEqualTo(scaledZero)
    ]
  }

  @Test
  def void absPow2ShouldSucceed() {
    complexNumbers.forEach [
      assertThat(absPow2).isEqualTo(real ** 2 + imaginary ** 2)
    ]
  }

  @Test
  def void absPow2ZeroShouldBeEqualToZero() {
    assertThat(ZERO.absPow2).isEqualTo(0BD)
  }

  @Test
  def void absPow2OneShouldBeEqualToOne() {
    assertThat(ONE.absPow2).isEqualTo(1BD)
  }

  @Test
  def void absPow2MinusOneShouldBeSameAsOne() {
    assertThat(ONE.negate.absPow2).isEqualTo(1BD)
  }

  @Test
  def void conjugateShouldSucceed() {
    complexNumbers.forEach [
      assertThat(conjugate).isEqualTo(new RealComplexNumber(real, -imaginary))
    ]
  }

  @Test
  def void conjugateRealNumberShouldBeEqualToSelf() {
    complexNumbers.forEach [
      val realNumber = new RealComplexNumber(real, 0BD)
      assertThat(realNumber.conjugate).isEqualTo(realNumber)
    ]
  }
}
