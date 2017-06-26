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

package mathmyday.lib.number

import com.google.common.annotations.Beta
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkNotNull
import static com.google.common.base.Preconditions.checkState

@Beta
@Data
final class Fraction implements MathNumber<Fraction, Fraction> {
  public static val ZERO = new Fraction(0BI, 1BI)
  public static val ONE = new Fraction(1BI, 1BI)
  BigInteger numerator
  BigInteger denominator

  new(BigInteger numerator, BigInteger denominator) {
    checkArgument(denominator != 0BI, "The denominator is not allowed to be equal to zero but is %s.", denominator)
    this.numerator = checkNotNull(numerator, "The numerator is not allowed to be null but is %s.", numerator)
    this.denominator = checkNotNull(denominator, "The denominator is not allowed to be null but is %s.", denominator)
  }

  override add(Fraction summand) {
    checkNotNull(summand, "The summand is not allowed to be null but is %s.", summand)
    val newNumerator = summand.denominator * numerator + denominator * summand.numerator
    val newDenominator = denominator * summand.denominator
    new Fraction(newNumerator, newDenominator)
  }

  override subtract(Fraction subtrahend) {
    checkNotNull(subtrahend, "The subtrahend is not allowed to be null but is %s.", subtrahend)
    val newNumerator = subtrahend.denominator * numerator - denominator * subtrahend.numerator
    val newDenominator = denominator * subtrahend.denominator
    new Fraction(newNumerator, newDenominator)
  }

  override multiply(Fraction factor) {
    checkNotNull(factor, "The factor is not allowed to be null but is %s.", factor)
    val newNumerator = numerator * factor.numerator
    val newDenominator = denominator * factor.denominator
    new Fraction(newNumerator, newDenominator)
  }

  override divide(Fraction divisor) {
    checkNotNull(divisor, "The divisor is not allowed to be null but is %s.", divisor)
    checkArgument(divisor.numerator != 0BI, "The divisor's numerator is not allowed to be zero but is %s.",
      divisor.numerator)
    multiply(divisor.invert)
  }

  override negate() {
    new Fraction(-numerator, denominator)
  }

  override pow(int exponent) {
    checkArgument(exponent > -1, "The exponent is not allowed to be negative but is %s.", exponent)
    if(exponent > 1)
      return multiply(pow(exponent - 1))
    else if(exponent == 1)
      return this
    ONE
  }

  override asString() {
    if(denominator < 0BI)
      return '''«numerator» / («denominator»)'''
    '''«numerator» / «denominator»'''
  }

  def invert() {
    checkState(numerator != 0BI, "Cannot invert because the numerator is not allowed to be zero but is %s.", numerator)
    new Fraction(denominator, numerator)
  }

  def lessThanOrEqualTo(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    val normalized = normalize
    val normalizedOther = other.normalize
    normalizedOther.denominator * normalized.numerator <= normalized.denominator * normalizedOther.numerator
  }

  def greaterThanOrEqualTo(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    !lessThanOrEqualTo(other) || equivalent(other)
  }

  def lessThan(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    !greaterThanOrEqualTo(other)
  }

  def greaterThan(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    !lessThanOrEqualTo(other)
  }

  def min(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    if(greaterThan(other))
      return other
    this
  }

  def max(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    if(lessThan(other))
      return other
    this
  }

  def normalize() {
    if(signum < 0)
      return new Fraction(-numerator.abs, denominator.abs)
    if(signum == 0)
      return ZERO
    if(numerator < 0BI)
      return new Fraction(numerator.abs, denominator.abs)
    this
  }

  def abs() {
    new Fraction(numerator.abs, denominator.abs)
  }

  def signum() {
    numerator.signum * denominator.signum
  }

  def reduce() {
    val gcd = numerator.gcd(denominator)
    new Fraction(numerator / gcd, denominator / gcd)
  }

  def equivalent(Fraction other) {
    checkNotNull(other, "The other fraction is not allowed to be null but is %s.", other)
    normalize.reduce == other.normalize.reduce
  }
}
