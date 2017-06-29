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

import com.google.common.annotations.Beta
import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.List
import java.util.Random
import mathmyday.lib.number.BigIntAndSqrt
import mathmyday.lib.number.Fraction
import mathmyday.lib.number.FractionAndSqrt
import mathmyday.lib.number.RealComplexNumber
import mathmyday.lib.number.SimpleComplexNumber
import mathmyday.lib.number.SimpleComplexNumberAndSqrt
import org.apache.commons.lang3.RandomUtils
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@ToString
final class MathRandom {
  val random = new Random

  def createPositiveInt(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    RandomUtils.nextLong(0, bound)
  }

  def createNegativeInt(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    (-1) * RandomUtils.nextLong(0, bound)
  }

  def createInt(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    if(random.nextBoolean)
      return createNegativeInt(bound)
    createPositiveInt(bound)
  }

  def createPositiveInts(long bound, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createPositiveInt(bound))
    ints
  }

  def createNegativeInts(long bound, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createNegativeInt(bound))
    ints
  }

  def createInts(long bound, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createInt(bound))
    ints
  }

  def createPositiveDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    val decimal = createDecimal(bound, scale)
    if(decimal < 0BD)
      return -decimal
    decimal
  }

  def createNegativeDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    val decimal = createDecimal(bound, scale)
    if(decimal > 0BD)
      return -decimal
    decimal
  }

  def createDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    val decimal = BigDecimal.valueOf(RandomUtils.nextLong(0, bound))
    keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP)
  }

  def keepDecimalInBound(BigDecimal decimal, long bound) {
    requireNonNull(decimal, 'decimal')
    checkArgument(bound > 0, 'expected > 0 but actual %s', bound)
    var it = decimal
    val decimalBound = BigDecimal.valueOf(bound)
    if(it >= 0BD)
      while(it >= decimalBound)
        it -= decimalBound
    else
      while(abs >= decimalBound)
        it += decimalBound
    it
  }

  def createPositiveDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createPositiveDecimal(bound, scale)
    decimals
  }

  def createNegativeDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createNegativeDecimal(bound, scale)
    decimals
  }

  def createDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(scale > 0, 'expected > 0 but actual %s.', scale)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createDecimal(bound, scale)
    decimals
  }

  def createPositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    val numerator = BigInteger.valueOf(RandomUtils.nextLong(0, bound))
    val denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    new Fraction(numerator, denominator)
  }

  def createNegativeFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    createPositiveFraction(bound).negate
  }

  def createFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    if(random.nextBoolean)
      return createNegativeFraction(bound)
    createPositiveFraction(bound)
  }

  def createPositiveFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createPositiveFraction(bound)
    fractions
  }

  def createNegativeFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createNegativeFraction(bound)
    fractions
  }

  def createFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createFraction(bound)
    fractions
  }

  def createInvertiblePositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    val numerator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    val denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    new Fraction(numerator, denominator)
  }

  def createInvertibleNegativeFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    createInvertiblePositiveFraction(bound).negate
  }

  def createInvertibleFraction(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    if(random.nextBoolean)
      return createInvertibleNegativeFraction(bound)
    createInvertiblePositiveFraction(bound)
  }

  def createInvertiblePositiveFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertiblePositiveFraction(bound)
    fractions
  }

  def createInvertibleNegativeFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertibleNegativeFraction(bound)
    fractions
  }

  def createInvertibleFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertibleFraction(bound)
    fractions
  }

  def createSimpleComplexNumber(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    val real = BigInteger.valueOf(createInt(bound))
    val imaginary = BigInteger.valueOf(createInt(bound))
    new SimpleComplexNumber(real, imaginary)
  }

  def createSimpleComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<SimpleComplexNumber> complexNumbers = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      complexNumbers += createSimpleComplexNumber(bound)
    complexNumbers
  }

  def createRealComplexNumber(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    val real = BigDecimal.valueOf(createInt(bound))
    val imaginary = BigDecimal.valueOf(createInt(bound))
    new RealComplexNumber(real, imaginary)
  }

  def createRealComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    checkArgument(howMany > 1, 'expected > 1 but actual %s.', howMany)
    val List<RealComplexNumber> complexNumbers = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      complexNumbers += createRealComplexNumber(bound)
    complexNumbers
  }

  def createBigIntAndSqrt(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    val it = BigInteger.valueOf(createPositiveInt(bound))
    new BigIntAndSqrt(it ** 2, it)
  }

  def createFractionAndSqrt(long bound) {
    checkArgument(bound > 1, 'expected > 1 but actual %s.', bound)
    val it = createPositiveFraction(bound)
    new FractionAndSqrt(pow(2), it)
  }

  def createSimpleComplexNumberAndSqrt(long bound) {
    checkArgument(bound > 0, 'expected > 0 but actual %s.', bound)
    val it = createSimpleComplexNumber(bound)
    new SimpleComplexNumberAndSqrt(pow(2), it)
  }
}
