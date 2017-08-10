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

import finnmath.number.ScientificNotation
import org.junit.After
import org.junit.Test
import org.slf4j.LoggerFactory

import static org.assertj.core.api.Assertions.assertThat

final class SquareRootCalculatorTest {
  val defaultScale = SquareRootCalculator.DEFAULT_SCALE
  val mathRandom = new MathRandom
  private static val log = LoggerFactory.getLogger(SquareRootCalculatorTest)

  @After
  def void after() {
    log.debug('')
  }

  @Test
  def void scientificNotationForSqrtForZero() {
    val actual = SquareRootCalculator.scientificNotationForSqrt(0BD)
    assertThat(actual).isEqualTo(new ScientificNotation(0BD, 0))
  }

  @Test
  def void scientificNotationForSqrtInbetweenZeroAndOneHundred() {
    val decimal = mathRandom.nextInvertiblePositiveDecimal(100, defaultScale)
    val actual = SquareRootCalculator.scientificNotationForSqrt(decimal)
    assertThat(actual).isEqualTo(new ScientificNotation(decimal, 0))
  }

  @Test
  def void scientificNotationForSqrtGreaterThanOneHundred() {
    val decimal = mathRandom.nextInvertiblePositiveDecimal(100, defaultScale) + 100BD
    val actual = SquareRootCalculator.scientificNotationForSqrt(decimal)
    val expected = new ScientificNotation(decimal / 100BD, 2)
    assertThat(actual).isEqualTo(expected)
  }

  @Test
  def void sqrtOfZero() {
    assertThat(SquareRootCalculator.sqrt(0BD)).isBetween(-0.001BD, 0.001BD)
  }

  @Test
  def void sqrtOfOne() {
    assertThat(SquareRootCalculator.sqrt(1BD)).isBetween(0.999BD, 1.001BD)
  }

  @Test
  def void sqrtOfTwo() {
    assertThat(SquareRootCalculator.sqrt(2BD)).isBetween(1.413BD, 1.415BD)
  }

  @Test
  def void sqrtOfThree() {
    assertThat(SquareRootCalculator.sqrt(3BD)).isBetween(1.731BD, 1.733BD)
  }

  @Test
  def void sqrtOfFour() {
    assertThat(SquareRootCalculator.sqrt(4BD)).isBetween(1.999BD, 2.001BD)
  }

  @Test
  def void sqrtOfFive() {
    assertThat(SquareRootCalculator.sqrt(5BD)).isBetween(2.235BD, 2.237BD)
  }

  @Test
  def void sqrtOfSix() {
    assertThat(SquareRootCalculator.sqrt(6BD)).isBetween(2.448BD, 2.45BD)
  }

  @Test
  def void sqrtOfSeven() {
    assertThat(SquareRootCalculator.sqrt(7BD)).isBetween(2.644BD, 2.646BD)
  }

  @Test
  def void sqrtOfEight() {
    assertThat(SquareRootCalculator.sqrt(8BD)).isBetween(2.827BD, 2.829BD)
  }

  @Test
  def void sqrtOfNine() {
    assertThat(SquareRootCalculator.sqrt(9BD)).isBetween(2.999BD, 3.001BD)
  }

  @Test
  def void sqrtOfTen() {
    assertThat(SquareRootCalculator.sqrt(10BD)).isBetween(3.161BD, 3.163BD)
  }

  @Test
  def void sqrtOfSixteen() {
    assertThat(SquareRootCalculator.sqrt(16BD)).isBetween(3.999BD, 4.001BD)
  }

  @Test
  def void sqrtOfTwentyFive() {
    assertThat(SquareRootCalculator.sqrt(25BD)).isBetween(4.999BD, 5.001BD)
  }

  @Test
  def void sqrtOfThirtySix() {
    assertThat(SquareRootCalculator.sqrt(36BD)).isBetween(5.999BD, 6.001BD)
  }

  @Test
  def void sqrtOfFortyNine() {
    assertThat(SquareRootCalculator.sqrt(49BD)).isBetween(6.999BD, 7.001BD)
  }

  @Test
  def void sqrtOfSixtyFour() {
    assertThat(SquareRootCalculator.sqrt(64BD)).isBetween(7.999BD, 8.001BD)
  }

  @Test
  def void sqrtOfEightyOne() {
    assertThat(SquareRootCalculator.sqrt(81BD)).isBetween(8.999BD, 9.001BD)
  }

  @Test
  def void sqrtOfOneHundred() {
    assertThat(SquareRootCalculator.sqrt(100BD)).isBetween(9.999BD, 10.001BD)
  }
}
