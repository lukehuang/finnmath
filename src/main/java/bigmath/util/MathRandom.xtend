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

import bigmath.number.BigIntAndSqrt
import bigmath.number.Fraction
import bigmath.number.FractionAndSqrt
import bigmath.number.RealComplexNumber
import bigmath.number.SimpleComplexNumber
import bigmath.number.SimpleComplexNumberAndSqrt
import com.google.common.annotations.Beta
import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.List
import java.util.Random
import org.apache.commons.lang3.RandomUtils
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@ToString
final class MathRandom {
  val random = new Random

  def nextPositiveLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    RandomUtils.nextLong(0, bound)
  }

  def nextNegativeLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    (-1) * RandomUtils.nextLong(0, bound)
  }

  def nextLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    if (random.nextBoolean)
      return nextNegativeLong(bound)
    nextPositiveLong(bound)
  }

  def nextPositiveLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextPositiveLong(bound))
    ints
  }

  def nextNegativeLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextNegativeLong(bound))
    ints
  }

  def nextLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextLong(bound))
    ints
  }

  def nextPositiveDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal < 0BD)
      return -decimal
    decimal
  }

  def nextNegativeDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal > 0BD)
      return -decimal
    decimal
  }

  def nextDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = BigDecimal.valueOf(RandomUtils.nextLong(0, bound))
    keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP)
  }

  def keepDecimalInBound(BigDecimal decimal, long bound) {
    requireNonNull(decimal, 'decimal')
    checkArgument(bound > 0, 'expected > 0 but actual %s', bound)
    var it = decimal
    val decimalBound = BigDecimal.valueOf(bound)
    if (it >= 0BD)
      while (it >= decimalBound)
        it -= decimalBound
    else
      while (abs >= decimalBound)
        it += decimalBound
    it
  }

  def nextPositiveDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextPositiveDecimal(bound, scale)
    decimals
  }

  def nextNegativeDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextNegativeDecimal(bound, scale)
    decimals
  }

  def nextDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextDecimal(bound, scale)
    decimals
  }

  def nextPositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val numerator = BigInteger.valueOf(RandomUtils.nextLong(0, bound))
    val denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    new Fraction(numerator, denominator)
  }

  def nextNegativeFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    nextPositiveFraction(bound).negate
  }

  def nextFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    if (random.nextBoolean)
      return nextNegativeFraction(bound)
    nextPositiveFraction(bound)
  }

  def nextPositiveFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
    for (i : 0 ..< howMany)
      fractions += nextPositiveFraction(bound)
    fractions
  }

  def nextNegativeFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += nextNegativeFraction(bound)
    fractions
  }

  def nextFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += nextFraction(bound)
    fractions
  }

  def nextInvertiblePositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val numerator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    val denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    new Fraction(numerator, denominator)
  }

  def nextInvertibleNegativeFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    nextInvertiblePositiveFraction(bound).negate
  }

  def nextInvertibleFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    if (random.nextBoolean)
      return nextInvertibleNegativeFraction(bound)
    nextInvertiblePositiveFraction(bound)
  }

  def nextInvertiblePositiveFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
    for (i : 0 ..< howMany)
      fractions += nextInvertiblePositiveFraction(bound)
    fractions
  }

  def nextInvertibleNegativeFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
    for (i : 0 ..< howMany)
      fractions += nextInvertibleNegativeFraction(bound)
    fractions
  }

  def nextInvertibleFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
    for (i : 0 ..< howMany)
      fractions += nextInvertibleFraction(bound)
    fractions
  }

  def nextSimpleComplexNumber(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    val real = BigInteger.valueOf(nextLong(bound))
    val imaginary = BigInteger.valueOf(nextLong(bound))
    new SimpleComplexNumber(real, imaginary)
  }

  def nextSimpleComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextSimpleComplexNumber(bound)
    complexNumbers
  }

  def nextRealComplexNumber(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    val real = BigDecimal.valueOf(nextLong(bound))
    val imaginary = BigDecimal.valueOf(nextLong(bound))
    new RealComplexNumber(real, imaginary)
  }

  def nextRealComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextRealComplexNumber(bound)
    complexNumbers
  }

  def nextBigIntAndSqrt(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    val it = BigInteger.valueOf(nextPositiveLong(bound))
    new BigIntAndSqrt(it ** 2, it)
  }

  def nextFractionAndSqrt(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val it = nextPositiveFraction(bound)
    new FractionAndSqrt(pow(2), it)
  }

  def nextSimpleComplexNumberAndSqrt(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    val it = nextSimpleComplexNumber(bound)
    new SimpleComplexNumberAndSqrt(pow(2), it)
  }
}
