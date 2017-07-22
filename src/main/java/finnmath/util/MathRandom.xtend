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

import com.google.common.annotations.Beta
import finnmath.linear.BigIntMatrix
import finnmath.linear.BigIntVector
import finnmath.linear.DecimalMatrix
import finnmath.linear.DecimalVector
import finnmath.number.BigIntAndSqrt
import finnmath.number.Fraction
import finnmath.number.FractionAndSqrt
import finnmath.number.RealComplexNumber
import finnmath.number.SimpleComplexNumber
import finnmath.number.SimpleComplexNumberAndSqrt
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

  def nextInvertiblePositiveDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal < 0BD)
      return -decimal
    decimal
  }

  def nextInvertibleNegativeDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = nextDecimal(bound, scale)
    if (decimal > 0BD)
      return -decimal
    decimal
  }

  def nextInvertibleDecimal(long bound, int scale) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    val decimal = BigDecimal.valueOf(RandomUtils.nextLong(1, bound))
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

  def nextInvertiblePositiveDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertiblePositiveDecimal(bound, scale)
    decimals
  }

  def nextInvertibleNegativeDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertibleNegativeDecimal(bound, scale)
    decimals
  }

  def nextInvertibleDecimals(long bound, int scale, int howMany) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += nextInvertibleDecimal(bound, scale)
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

  def nextInvertibleSimpleComplexNumber(long bound) {
    checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
    val nonZeroPart = BigInteger.valueOf(RandomUtils.nextLong(1, bound))
    val possibleZeroPart = if (random.nextBoolean) RandomUtils.nextLong(1, bound) else nextLong(bound)
    if (random.nextBoolean)
      return new SimpleComplexNumber(BigInteger.valueOf(possibleZeroPart), nonZeroPart)
    new SimpleComplexNumber(nonZeroPart, BigInteger.valueOf(possibleZeroPart))
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

  def nextBigIntMatrix(long bound, int rowSize, int columnSize) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
    val builder = BigIntMatrix::builder(rowSize, columnSize)
    (1 .. rowSize).forEach [ rowIndex |
      (1 .. columnSize).forEach [ columnIndex |
        builder.put(rowIndex, columnIndex, BigInteger.valueOf(nextLong(bound)))
      ]
    ]
    builder.build
  }

  def nextBigIntVector(long bound, int size) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    val builder = BigIntVector::builder(size)
    for (index : 1 .. size)
      builder.put(BigInteger.valueOf(nextLong(bound)))
    builder.build
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

  def nextDecimalVector(long bound, int scale, int size) {
    checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
    checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    val builder = DecimalVector::builder(size)
    for (index : 1 .. size)
      builder.put(nextDecimal(bound, scale))
    builder.build
  }
}