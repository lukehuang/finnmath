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

import finnmath.util.MathRandom
import java.math.BigInteger
import java.util.List
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

final class FractionTest {
  static val ZERO = Fraction::ZERO
  static val ONE = Fraction::ONE
  static var List<Fraction> fractions
  static var List<Fraction> others
  static var List<Fraction> invertibles

  @BeforeClass
  static def void setUpClass() {
    val it = new MathRandom
    val howMany = 10
    val bound = 10
    fractions = nextFractions(bound, howMany)
    others = nextFractions(bound, howMany)
    invertibles = nextInvertibleFractions(bound, howMany)
  }

  @Test
  def void newNumeratorNullShouldThrowException() {
    assertThatThrownBy[
      new Fraction(null, 1BI)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('numerator')
  }

  @Test
  def void newDenominatorNullShouldThrowException() {
    assertThatThrownBy[
      new Fraction(0BI, null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('denominator')
  }

  @Test
  def void newDenominatorZeroShouldThrowException() {
    assertThatThrownBy[
      new Fraction(0BI, 0BI)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected denominator != 0 but actual 0')
  }

  @Test
  def void newShouldSucceed() {
    val it = new Fraction(0BI, 1BI)
    assertThat(it).isExactlyInstanceOf(Fraction)
    assertThat(numerator).isExactlyInstanceOf(BigInteger)
    assertThat(denominator).isExactlyInstanceOf(BigInteger)
  }

  @Test
  def void addNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.add(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('summand')
  }

  @Test
  def void addShouldSucceed() {
    fractions.forEach [
      others.forEach [ other |
        val expectedNumerator = other.denominator * numerator + denominator * other.numerator
        val expectedDenominator = denominator * other.denominator
        val actual = add(other)
        val expected = new Fraction(expectedNumerator, expectedDenominator)
        assertThat(actual).isExactlyInstanceOf(Fraction).isEqualTo((expected))
      ]
    ]
  }

  @Test
  def void addZeroShouldBeEqualToSelf() {
    fractions.forEach [
      assertThat(add(ZERO)).isEqualTo(it)
    ]
  }

  @Test
  def void addShouldBeCommutative() {
    fractions.forEach [
      others.forEach [ other |
        assertThat(add(other)).isEqualTo(other.add(it))
      ]
    ]
  }

  @Test
  def void addShouldBeAssociative() {
    fractions.forEach [
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
    fractions.forEach [
      others.forEach [ other |
        val expectedNumerator = other.denominator * numerator - denominator * other.numerator
        val expectedDenominator = denominator * other.denominator
        val actual = subtract(other)
        val expected = new Fraction(expectedNumerator, expectedDenominator)
        assertThat(actual).isExactlyInstanceOf(Fraction).isEqualTo(expected)
      ]
    ]
  }

  @Test
  def void subtractZeroShouldBeEqualToSelf() {
    fractions.forEach [
      assertThat(subtract(ZERO)).isEqualTo(it)
    ]
  }

  @Test
  def void subtractSelfShouldBeEqualToZero() {
    fractions.forEach [
      assertThat(subtract(it).normalize.reduce).isEqualTo(ZERO)
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
    fractions.forEach [
      others.forEach [ other |
        val expectedNumerator = numerator * other.numerator
        val expectedDenominator = denominator * other.denominator
        val actual = multiply(other)
        val expected = new Fraction(expectedNumerator, expectedDenominator)
        assertThat(actual).isExactlyInstanceOf(Fraction).isEqualTo(expected)
      ]
    ]
  }

  @Test
  def void multiplyOneShouldBeEqualToSelf() {
    fractions.forEach [
      assertThat(multiply(ONE)).isEqualTo(it)
    ]
  }

  @Test
  def void multiplyZeroShouldBeEqualToZero() {
    fractions.forEach [
      assertThat(multiply(ZERO).reduce).isEqualTo(ZERO)
    ]
  }

  @Test
  def void multiplyShouldBeCommutative() {
    fractions.forEach [
      others.forEach [ other |
        assertThat(multiply(other)).isEqualTo(other.multiply(it))
      ]
    ]
  }

  @Test
  def void multiplyShouldBeAssociative() {
    fractions.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          assertThat(multiply(other).multiply(invertible)).isEqualTo(multiply(other.multiply(invertible)))
        ]
      ]
    ]
  }

  @Test
  def void addAndMultiplyShouldBeDistributive() {
    fractions.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          assertThat(multiply(other.add(invertible)).reduce).isEqualTo(multiply(other).add(multiply(invertible)).reduce)
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
    assertThatThrownBy[
      ZERO.divide(ZERO)
    ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected divisor.numerator != 0 but actual 0')
  }

  @Test
  def void divideShouldSucceed() {
    fractions.forEach [
      invertibles.forEach [ invertible |
        assertThat(divide(invertible)).isExactlyInstanceOf(Fraction).isEqualTo(multiply(invertible.invert))
      ]
    ]
  }

  @Test
  def void divideOneShouldBeEqualToSelf() {
    fractions.forEach [
      assertThat(divide(ONE)).isEqualTo(it)
    ]
  }

  @Test
  def void negateShouldSucceed() {
    fractions.forEach [
      assertThat(negate).isExactlyInstanceOf(Fraction).isEqualTo(new Fraction(-numerator, denominator))
    ]
  }

  @Test
  def void negateZeroShouldBeEqualToSelf() {
    assertThat(ZERO.negate).isEqualTo(ZERO)
  }

  @Test
  def void addNegatedShouldBeEqualToZero() {
    fractions.forEach [
      assertThat(add(negate).reduce).isEqualTo(ZERO)
    ]
  }

  @Test
  def void multiplyMinusOneShouldBeEqualToNegated() {
    fractions.forEach [
      assertThat(multiply(ONE.negate)).isEqualTo(negate)
    ]
  }

  @Test
  def void divideMinusOneShouldBeEqualToNegated() {
    fractions.forEach [
      assertThat(divide(ONE.negate).normalize.reduce).isEqualTo(negate.reduce)
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
    fractions.forEach [
      assertThat(pow(3)).isExactlyInstanceOf(Fraction).isEqualTo(multiply(multiply(it)))
      assertThat(pow(2)).isEqualTo(multiply(it))
    ]
  }

  @Test
  def void powOneShouldBeTheSame() {
    fractions.forEach [
      assertThat(pow(1)).isSameAs(it)
    ]
  }

  @Test
  def void powZeroShouldBeSameAsOne() {
    fractions.forEach [
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
  def void invertZeroShouldThrowException() {
    assertThatThrownBy[
      ZERO.invert
    ].isExactlyInstanceOf(IllegalStateException).hasMessage('expected numerator != 0 but actual 0')
  }

  @Test
  def void invertShouldSucceed() {
    invertibles.forEach [
      assertThat(invert).isExactlyInstanceOf(Fraction).isEqualTo(new Fraction(denominator, numerator))
    ]
  }

  @Test
  def void invertOneShouldBeEqualToOne() {
    assertThat(ONE.invert).isEqualTo(ONE)
  }

  @Test
  def void invertSelfShouldBeEqualToOneDividedBySelf() {
    invertibles.forEach [
      assertThat(invert.reduce.normalize).isEqualTo(ONE.divide(it).reduce.normalize)
    ]
  }

  @Test
  def void multiplyInvertedShouldBeEqualToOne() {
    invertibles.forEach [
      assertThat(multiply(invert).reduce.normalize).isEqualTo(ONE)
    ]
  }

  @Test
  def void invertibleZeroShouldBeFalse() {
    assertFalse(ZERO.invertible)
  }

  @Test
  def void invertibleShouldBePredictable() {
    fractions.forEach [
      assertEquals(numerator != 0BI, invertible)
    ]
  }

  @Test
  def void invertibleShouldSucceed() {
    invertibles.forEach [
      assertTrue(invertible)
    ]
  }

  @Test
  def void lessThanOrEqualToNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.lessThanOrEqualTo(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void lessThanOrEqualToShouldSucceed() {
    fractions.forEach [
      val greater = add(ONE)
      val lower = subtract(ONE)
      assertTrue(lessThanOrEqualTo(greater))
      assertFalse(lessThanOrEqualTo(lower))
      assertTrue(lessThanOrEqualTo(it))
    ]
  }

  @Test
  def void greaterThanOrEqualToNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.greaterThanOrEqualTo(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void greaterThanOrEqualToShouldSucceed() {
    fractions.forEach [
      val lower = subtract(ONE)
      val greater = add(ONE)
      assertTrue(greaterThanOrEqualTo(lower))
      assertFalse(greaterThanOrEqualTo(greater))
      assertTrue(greaterThanOrEqualTo(it))
    ]
  }

  @Test
  def void lessThanNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.lessThan(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void lessThanShouldSucceed() {
    fractions.forEach [
      val greater = add(ONE)
      val lower = subtract(ONE)
      assertTrue(lessThan(greater))
      assertFalse(lessThan(lower))
      assertFalse(lessThan(it))
    ]
  }

  @Test
  def void greaterThanNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.greaterThan(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void greaterThanShouldSucceed() {
    fractions.forEach [
      val lower = subtract(ONE)
      val greater = add(ONE)
      assertTrue(greaterThan(lower))
      assertFalse(greaterThan(greater))
      assertFalse(greaterThan(it))
    ]
  }

  @Test
  def void minNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.min(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void minShouldSucceed() {
    fractions.forEach [
      val maximum = add(ONE)
      val minimum = subtract(ONE)
      assertThat(min(it)).isExactlyInstanceOf(Fraction).isSameAs(it)
      assertThat(min(maximum)).isSameAs(it)
      assertThat(min(minimum)).isSameAs(minimum)
    ]
  }

  @Test
  def void maxNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.max(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void maxShouldSucceed() {
    fractions.forEach [
      val minimum = subtract(ONE)
      val maximum = add(ONE)
      assertThat(max(it)).isExactlyInstanceOf(Fraction).isSameAs(it)
      assertThat(max(minimum)).isSameAs(it)
      assertThat(max(maximum)).isSameAs(maximum)
    ]
  }

  @Test
  def void normalizeShouldSucceed() {
    val mathRandom = new MathRandom
    val bound = 10
    val howMany = 10
    mathRandom.nextInvertiblePositiveFractions(bound, howMany).forEach [
      assertThat(normalize).isExactlyInstanceOf(Fraction)
      val expectedForNegativeSignum = new Fraction(-numerator, denominator)
      assertThat(new Fraction(numerator, -denominator).normalize).isEqualTo(expectedForNegativeSignum)
      assertThat(new Fraction(-numerator, denominator).normalize).isEqualTo(expectedForNegativeSignum)
      assertThat(new Fraction(0BI, denominator).normalize).isEqualTo(ZERO)
      assertThat(new Fraction(-numerator, -denominator).normalize).isEqualTo(it)
      val normalized = normalize
      assertThat(normalized.normalize).isSameAs(normalized)
    ]
  }

  @Test
  def void normalizeZeroShouldBeTheSame() {
    assertThat(ZERO.normalize).isSameAs(ZERO)
  }

  @Test
  def void normalizeOneShouldBeTheSame() {
    assertThat(ONE.normalize).isSameAs(ONE)
  }

  @Test
  def void absShouldSucceed() {
    fractions.forEach [
      assertThat(abs).isExactlyInstanceOf(Fraction).isEqualTo(new Fraction(numerator.abs, denominator.abs))
    ]
  }

  @Test
  def void absZeroShouldBeEqualToZero() {
    assertThat(ZERO.abs).isEqualTo(ZERO)
  }

  @Test
  def void absOneShouldBeEqualToOne() {
    assertThat(ONE.abs).isEqualTo(ONE)
  }

  @Test
  def void absMinusOneShouldBeEqualToOne() {
    assertThat(ONE.negate.abs).isEqualTo(ONE)
  }

  @Test
  def void signumShouldSucceed() {
    fractions.forEach [
      assertEquals(signum, numerator.signum * denominator.signum)
    ]
  }

  @Test
  def void signumMinusOneShouldBeEqualToMinusOne() {
    assertEquals(-1, ONE.negate.signum)
  }

  @Test
  def void signumZeroShouldBeEqualToZero() {
    assertEquals(0, ZERO.signum)
  }

  @Test
  def void signumOneShouldBeEqualToOne() {
    assertEquals(1, ONE.signum)
  }

  @Test
  def void reduceShouldSucceed() {
    fractions.forEach [
      val gcd = numerator.gcd(denominator)
      assertThat(reduce).isExactlyInstanceOf(Fraction).isEqualTo(new Fraction(numerator / gcd, denominator / gcd))
    ]
  }

  @Test
  def void reduceZeroShouldBeEqualToZero() {
    assertThat(ZERO.reduce).isEqualTo(ZERO)
  }

  @Test
  def void reduceOneShouldBeEqualToOne() {
    assertThat(ONE.reduce).isEqualTo(ONE)
  }

  @Test
  def void equivalentNullShouldThrowException() {
    assertThatThrownBy[
      ZERO.equivalent(null)
    ].isExactlyInstanceOf(NullPointerException).hasMessage('other')
  }

  @Test
  def void equivalentShouldSucceed() {
    fractions.forEach [
      others.forEach [ other |
        assertEquals(equivalent(other), reduce == other.reduce)
      ]
    ]
  }

  @Test
  def void equivalentShouldBeReflexive() {
    fractions.forEach [
      assertTrue(equivalent(it))
    ]
  }

  @Test
  def void equivalentShouldBeSymmetric() {
    fractions.forEach [
      others.forEach [ other |
        assertEquals(equivalent(other), other.equivalent(it))
      ]
    ]
  }

  @Test
  def void equivalentShouldBeTransitive() {
    fractions.forEach [
      others.forEach [ other |
        invertibles.forEach [ invertible |
          val a = equivalent(other)
          val b = other.equivalent(invertible)
          val c = equivalent(invertible)
          if (a && b)
            assertTrue(c)
          else
            assertFalse(a && b && c)
          if (!c)
            assertFalse(a && b)
          else if (!a)
            assertFalse(b)
          else if (!b)
            assertFalse(a)
        ]
      ]
    ]
  }
}
