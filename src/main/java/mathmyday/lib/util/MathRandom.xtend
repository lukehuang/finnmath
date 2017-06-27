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
import mathmyday.lib.number.Fraction
import mathmyday.lib.number.NumberAndSqrt
import mathmyday.lib.number.RealComplexNumber
import mathmyday.lib.number.SimpleComplexNumber
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkNotNull

@Beta
@ToString
final class MathRandom {
  val random = new Random

  def createPositiveInt(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    random.nextInt(bound)
  }

  def createNegativeInt(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    (-1) * random.nextInt(bound)
  }

  def createInt(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    if(random.nextBoolean)
      return createNegativeInt(bound)
    createPositiveInt(bound)
  }

  def createPositiveInts(int bound, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val ints = newIntArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createPositiveInt(bound))
    ints
  }

  def createNegativeInts(int bound, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val ints = newIntArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createNegativeInt(bound))
    ints
  }

  def createInts(int bound, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val ints = newIntArrayOfSize(howMany)
    for (i : 0 ..< howMany)
      ints.set(i, createInt(bound))
    ints
  }

  def createPositiveDecimal(int bound, int scale) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    val decimal = createDecimal(bound, scale)
    if(decimal < 0BD)
      return -decimal
    decimal
  }

  def createNegativeDecimal(int bound, int scale) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    val decimal = createDecimal(bound, scale)
    if(decimal > 0BD)
      return -decimal
    decimal
  }

  def createDecimal(int bound, int scale) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    val decimal = BigDecimal.valueOf(random.nextDouble)
    keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP)
  }

  def keepDecimalInBound(BigDecimal decimal, int bound) {
    checkNotNull(decimal, 'The decimal number is not allowed to be null but is %s.', decimal)
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
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

  def createPositiveDecimals(int bound, int scale, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createPositiveDecimal(bound, scale)
    decimals
  }

  def createNegativeDecimals(int bound, int scale, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createNegativeDecimal(bound, scale)
    decimals
  }

  def createDecimals(int bound, int scale, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(scale > 0)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<BigDecimal> decimals = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      decimals += createDecimal(bound, scale)
    decimals
  }

  def createPositiveFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    val numerator = BigInteger.valueOf(random.nextInt(bound))
    val denominator = BigInteger.valueOf(random.nextInt(bound - 1) + 1)
    new Fraction(numerator, denominator)
  }

  def createNegativeFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    createPositiveFraction(bound).negate
  }

  def createFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    if(random.nextBoolean)
      return createNegativeFraction(bound)
    createPositiveFraction(bound)
  }

  def createPositiveFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createPositiveFraction(bound)
    fractions
  }

  def createNegativeFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createNegativeFraction(bound)
    fractions
  }

  def createFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createFraction(bound)
    fractions
  }

  def createInvertiblePositiveFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    val newBound = bound - 1
    val numerator = BigInteger.valueOf(random.nextInt(newBound) + 1)
    val denominator = BigInteger.valueOf(random.nextInt(newBound) + 1)
    new Fraction(numerator, denominator)
  }

  def createInvertibleNegativeFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    createInvertiblePositiveFraction(bound).negate
  }

  def createInvertibleFraction(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    if(random.nextBoolean)
      return createInvertibleNegativeFraction(bound)
    createInvertiblePositiveFraction(bound)
  }

  def createInvertiblePositiveFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertiblePositiveFraction(bound)
    fractions
  }

  def createInvertibleNegativeFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertibleNegativeFraction(bound)
    fractions
  }

  def createInvertibleFractions(int bound, int howMany) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<Fraction> fractions = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      fractions += createInvertibleFraction(bound)
    fractions
  }

  def createSimpleComplexNumber(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    val real = BigInteger.valueOf(createInt(bound))
    val imaginary = BigInteger.valueOf(createInt(bound))
    new SimpleComplexNumber(real, imaginary)
  }

  def createSimpleComplexNumbers(int bound, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<SimpleComplexNumber> complexNumbers = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      complexNumbers += createSimpleComplexNumber(bound)
    complexNumbers
  }

  def createRealComplexNumber(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    val real = BigDecimal.valueOf(createInt(bound))
    val imaginary = BigDecimal.valueOf(createInt(bound))
    new RealComplexNumber(real, imaginary)
  }

  def createRealComplexNumbers(int bound, int howMany) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    checkArgument(howMany > 1, 'The number of numbers has to be greater than one but is %s.', howMany)
    val List<RealComplexNumber> complexNumbers = new ArrayList(howMany)
    for (i : 0 ..< howMany)
      complexNumbers += createRealComplexNumber(bound)
    complexNumbers
  }

  def createBigIntAndSqrt(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    val it = BigInteger.valueOf(createPositiveInt(bound))
    new NumberAndSqrt(it ** 2, it)
  }

  def createFractionAndSqrt(int bound) {
    checkArgument(bound > 1, 'The bound has to be greater than one but is %s.', bound)
    val it = createPositiveFraction(bound)
    new NumberAndSqrt(pow(2), it)
  }

  def createSimpleComplexNumberAndSqrt(int bound) {
    checkArgument(bound > 0, 'The bound has to be greater than zero but is %s.', bound)
    val it = createSimpleComplexNumber(bound)
    new NumberAndSqrt(pow(2), it)
  }
}
