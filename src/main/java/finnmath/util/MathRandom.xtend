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

import com.google.common.annotations.Beta
import finnmath.linear.BigIntMatrix
import finnmath.linear.BigIntVector
import finnmath.linear.DecimalMatrix
import finnmath.linear.DecimalVector
import finnmath.number.Fraction
import finnmath.number.RealComplexNumber
import finnmath.number.SimpleComplexNumber
import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.List
import java.util.Random
import org.apache.commons.lang3.RandomUtils
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

/**
 * A pseudo random generator for {@code long}, {@code BigDecimal}, {@code Fraction}, {@code SimpleComplexNumber}, 
 * {@code RealComplexNumber}, {@code BigIntVector}, {@code DecimalVector}, {@code BigIntMatrix} and 
 * {@code DecimalMatrix}
 */
@Beta
@ToString
final class MathRandom {
  val random = new Random

  /**
   * Returns a positive {code long} bounded below by {@code 0} (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @return long
   * @throws IllegalArgumentException if {@code bound < 1}
   */
  def nextPositiveLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    RandomUtils::nextLong(0, bound)
  }

  /**
   * Returns a negative {code long} bounded below by {@code -bound} (exclusive) and above by {@code 0} (inclusive)
   * 
   * @param bound
   * @return long
   * @throws IllegalArgumentException if {@code bound < 1}
   */
  def nextNegativeLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    (-1) * RandomUtils::nextLong(0, bound)
  }

  /**
   * Returns a {code long} bounded below by {@code -bound} (exclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @return long
   * @throws IllegalArgumentException if {@code bound < 1}
   */
  def nextLong(long bound) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    if (random.nextBoolean)
      return nextNegativeLong(bound)
    nextPositiveLong(bound)
  }

  /**
   * Returns an array of the length of {@code howMany} containing positive {code long}s bounded below by {@code 0} 
   * (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return long array
   * @throws IllegalArgumentException if {@code bound < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextPositiveLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextPositiveLong(bound))
    ints
  }

  /**
   * Returns an array of the length of {@code howMany} containing negative {code long}s bounded below by  
   * {@code -bound} (exclusive) and above by {@code 0} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return long array
   * @throws IllegalArgumentException if {@code bound < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextNegativeLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextNegativeLong(bound))
    ints
  }

  /**
   * Returns an array of the length of {@code howMany} containing {code long}s bounded below by {@code -bound}  
   * (exclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return long array
   * @throws IllegalArgumentException if {@code  bound < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextLongs(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val ints = newLongArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, nextLong(bound))
    ints
  }

  /**
   * Returns a positive {@code BigDecimal} of a given {@code scale} bounded below by {@code 0} (inclusive) and above  
   * by {@code bound} (exclusive)
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 1}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextPositiveDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal < 0BD)
      return -decimal
    decimal
  }

  /**
   * Returns a negative {@code BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and  
   * above by {@code 0} (inclusive)
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 1}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextNegativeDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal > 0BD)
      return -decimal
    decimal
  }

  /**
   * Returns a {@code BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and above by  
   * {@code bound} (exclusive)
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 1}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextDecimal(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = BigDecimal::valueOf(RandomUtils::nextLong(0, bound))
    keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal::ROUND_HALF_UP)
  }

  /**
   * Returns a positive {@code BigDecimal} of a given {@code scale} bounded below by {@code 0} (exclusive) and above 
   * by {@code bound} (exclusive) which is invertible
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 2}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextInvertiblePositiveDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal < 0BD)
      return -decimal
    decimal
  }

  /**
   * Returns a negative {@code BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and  
   * above by {@code 0} (exclusive) which is invertible
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 2}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextInvertibleNegativeDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal > 0BD)
      return -decimal
    decimal
  }

  /**
   * Returns a {@code BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and above by  
   * {@code bound} (exclusive) which is invertible
   * 
   * @param bound
   * @return BigDecimal
   * @throws IllegalArgumentException if {@code bound < 2}
   * @throws IllegalArgumentException if {@code scale < 1}
   */
  def nextInvertibleDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = BigDecimal::valueOf(RandomUtils::nextLong(1, bound))
    keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal::ROUND_HALF_UP)
  }

  private def keepDecimalInBound(BigDecimal decimal, long bound) {
    requireNonNull(decimal, 'decimal')
    checkArgument(bound > 0, 'expected > 0 but actual %s', bound)
    var it = decimal
    val decimalBound = BigDecimal::valueOf(bound)
    if (it >= 0BD)
      while (it >= decimalBound)
        it -= decimalBound
    else
      while (abs >= decimalBound)
        it += decimalBound
    it
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} containing positive {@code BigDecimal}s of the given  
   * {@code scale} bounded below by {@code 0} (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 1}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextPositiveDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextPositiveDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} containing negative {@code BigDecimal}s of the given  
   * {@code scale} bounded below by {@code -bound} (exclusive) and above by {@code 0} (inclusive)
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 1}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextNegativeDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextNegativeDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} containing {@code BigDecimal}s of the given {@code scale}  
   * bounded below by {@code -bound} (exclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 1}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a positive {@code BigDecimal} of  
   * the given {@code scale} bounded below by {@code 0} (inclusive) and above by {@code bound} (exclusive) which are  
   * invertible
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextInvertiblePositiveDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertiblePositiveDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a negative {@code BigDecimal} of 
   * the given  {@code scale} bounded below by {@code -bound} (exclusive) and above by {@code 0} (inclusive) which  
   * are invertible
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextInvertibleNegativeDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertibleNegativeDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a {@code BigDecimal} of the given  
   * {@code scale} bounded below by {@code -bound} (exclusive) and above by {@code bound} (exclusive) which are  
   * invertible
   * 
   * @param bound
   * @param scale
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code  scale < 1}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextInvertibleDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertibleDecimal(bound, scale)
    decimals
  }

  /**
   * Returns a {@code Fraction} whose {@code numerator} is bounded below by {@code 0} (inclusive) and above by 
   * {@code bound} (exclusive) and whose {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound} 
   * (exclusive)
   * 
   * @param bound
   * @return Fraction
   * @throws IllegalArgumentException if {@code bound < 2}
   */
  def nextPositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val numerator = BigInteger::valueOf(RandomUtils::nextLong(0, bound))
    val denominator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
    new Fraction(numerator, denominator)
  }

  /**
   * Returns a {@code Fraction} whose {@code numerator} is bounded below by {@code -bound} (exclusive) and above by  
   * {@code 0} (inclusive) and whose {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound}  
   * (exclusive)
   * 
   * @param bound
   * @return Fraction
   * @throws IllegalArgumentException if {@code bound < 2}
   */
  def nextNegativeFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    nextPositiveFraction(bound).negate
  }

  /**
   * Returns a {@code Fraction} whose {@code numerator} is bounded below by {@code -bound} (exclusive) and above by  
   * {@code bound} (exclusive) and whose {@code denominator} is bounded below {@code -bound} (exclusive) and  
   * {@code bound} (exclusive)
   * 
   * @param bound
   * @return Fraction
   * @throws IllegalArgumentException if {@code bound < 2}
   */
  def nextFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    if (random.nextBoolean)
      return nextNegativeFraction(bound)
    nextPositiveFraction(bound)
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a {@code Fraction} whose  
   * {@code numerator}s are bounded below by {@code 0} (inclusive) and above by {@code bound} (exclusive) and whose  
   * {@code denominator}s are bounded below by {@code 1} (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextPositiveFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
    for (i : 0 ..< howMany)
      fractions += nextPositiveFraction(bound)
    fractions
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a {@code Fraction} whose 
   * {@code numerator} is bounded below by {@code -bound} (exclusive) and above by {@code 0} (inclusive) and whose 
   * {@code denominator} is bounded below by {@code 1} (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextNegativeFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += nextNegativeFraction(bound)
    fractions
  }

  /**
   * Returns a {@code List} of the size of {@code howMany} whose elements are each a {@code Fraction} whose 
   * {@code numerator} is bounded below by {@code -bound} (exclusive) and above by {@code bound} (exclusive) and 
   * whose {@code denominator} is bounded below by {@code 1} (inclusive) and above by {@code bound} (exclusive)
   * 
   * @param bound
   * @param howMany
   * @return List
   * @throws IllegalArgumentException if {@code  bound < 2}
   * @throws IllegalArgumentException if {@code howMany < 2}
   */
  def nextFractions(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += nextFraction(bound)
    fractions
  }

  /**
   * Returns a {@code Fraction} whose {@code numerator} and {@code denominator} are bounded below by {@code 1} 
   * (inclusive) and above by {@code bound} (exclusive) and whose {@code denominator} is bounded below  
   * {@code -bound} (exclusive) and {@code bound} (exclusive)
   * 
   * @param bound
   * @return Fraction
   * @throws IllegalArgumentException if {@code bound < 2}
   */
  def nextInvertiblePositiveFraction(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val numerator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
    val denominator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
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
    val real = BigInteger::valueOf(nextLong(bound))
    val imaginary = BigInteger::valueOf(nextLong(bound))
    new SimpleComplexNumber(real, imaginary)
  }

  def nextInvertibleSimpleComplexNumber(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val nonZeroPart = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
    val possibleZeroPart = if (random.nextBoolean) RandomUtils::nextLong(1, bound) else nextLong(bound)
    if (random.nextBoolean)
      return new SimpleComplexNumber(BigInteger::valueOf(possibleZeroPart), nonZeroPart)
    new SimpleComplexNumber(nonZeroPart, BigInteger::valueOf(possibleZeroPart))
  }

  def nextSimpleComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextSimpleComplexNumber(bound)
    complexNumbers
  }

  def nextInvertibleSimpleComplexNumbers(long bound, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextInvertibleSimpleComplexNumber(bound)
    complexNumbers
  }

  def nextRealComplexNumber(long bound, int scale) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val real = nextDecimal(bound, scale)
    val imaginary = nextDecimal(bound, scale)
    new RealComplexNumber(real, imaginary)
  }

  def nextInvertibleRealComplexNumber(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val nonZeroPart = nextInvertibleDecimal(bound, scale)
    val possibleZeroPart = if (random.nextBoolean)
        nextInvertibleDecimal(bound, scale)
      else
        nextDecimal(bound, scale)
    if (random.nextBoolean)
      return new RealComplexNumber(possibleZeroPart, nonZeroPart)
    new RealComplexNumber(nonZeroPart, possibleZeroPart)
  }

  def nextRealComplexNumbers(long bound, int scale, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextRealComplexNumber(bound, scale)
    complexNumbers
  }

  def nextInvertibleRealComplexNumbers(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
    for (i : 0 ..< howMany)
      complexNumbers += nextInvertibleRealComplexNumber(bound, scale)
    complexNumbers
  }

  def nextBigIntVector(long bound, int size) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    val builder = BigIntVector::builder(size)
    for (index : 1 .. size)
      builder.put(BigInteger::valueOf(nextLong(bound)))
    builder.build
  }

  def nextBigIntVectors(long bound, int size, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val vectors = new ArrayList<BigIntVector>(howMany) as List<BigIntVector>
    for (i : 1 .. howMany)
      vectors += nextBigIntVector(bound, size)
    vectors
  }

  def nextBigIntMatrix(long bound, int rowSize, int columnSize) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
    val builder = BigIntMatrix::builder(rowSize, columnSize)
    (1 .. rowSize).forEach [ rowIndex |
      (1 .. columnSize).forEach [ columnIndex |
        builder.put(rowIndex, columnIndex, BigInteger::valueOf(nextLong(bound)))
      ]
    ]
    builder.build
  }

  def nextBigIntMatrices(long bound, int rowSize, int columnSize, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
    for (i : 1 .. howMany)
      matrices += nextBigIntMatrix(bound, rowSize, columnSize)
    matrices
  }

  def nextDecimalVector(long bound, int scale, int size) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    val builder = DecimalVector::builder(size)
    for (index : 1 .. size)
      builder.put(nextDecimal(bound, scale))
    builder.build
  }

  def nextDecimalVectors(long bound, int scale, int size, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val vectors = new ArrayList<DecimalVector>(howMany) as List<DecimalVector>
    for (i : 1 .. howMany)
      vectors += nextDecimalVector(bound, scale, size)
    vectors
  }

  def nextDecimalMatrix(long bound, int scale, int rowSize, int columnSize) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
    val builder = DecimalMatrix::builder(rowSize, columnSize)
    (1 .. rowSize).forEach [ rowIndex |
      (1 .. columnSize).forEach [ columnIndex |
        builder.put(rowIndex, columnIndex, nextDecimal(bound, scale))
      ]
    ]
    builder.build
  }

  def nextDecimalMatrices(long bound, int scale, int rowSize, int columnSize, int howMany) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
    for (i : 1 .. howMany)
      matrices += nextDecimalMatrix(bound, scale, rowSize, columnSize)
    matrices
  }
}
