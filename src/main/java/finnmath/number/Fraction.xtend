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

package finnmath.number

import com.google.common.annotations.Beta
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a fraction
 */
@Beta
@EqualsHashCode
@ToString
@Accessors
final class Fraction implements MathNumber<Fraction, Fraction>, Comparable<Fraction> {
  public static val ZERO = new Fraction(0BI, 1BI)
  public static val ONE = new Fraction(1BI, 1BI)
  val BigInteger numerator
  val BigInteger denominator

  /**
   * Constructs a fraction by the given numerator and denominator
   * 
   * @param numerator
   * @param denominator
   * @throws NullPointerException if one of the arguments is {@code null}
   * @throws IllegalArgumentException if denominator == 0
   */
  new(BigInteger numerator, BigInteger denominator) {
    checkArgument(denominator != 0BI, 'expected denominator != 0 but actual %s', denominator)
    this.numerator = requireNonNull(numerator, 'numerator')
    this.denominator = requireNonNull(denominator, 'denominator')
  }

  /**
   * Returns the sum of this fraction and the given one
   * 
   * @param summand
   * @return sum
   * @throws NullPointerException
   */
  override add(Fraction summand) {
    requireNonNull(summand, 'summand')
    val newNumerator = summand.denominator * numerator + denominator * summand.numerator
    val newDenominator = denominator * summand.denominator
    new Fraction(newNumerator, newDenominator)
  }

  /**
   * Returns the difference of this fraction and the given one
   * 
   * @param subtrahend
   * @return difference
   * @throws NullPointerException
   */
  override subtract(Fraction subtrahend) {
    requireNonNull(subtrahend, 'subtrahend')
    val newNumerator = subtrahend.denominator * numerator - denominator * subtrahend.numerator
    val newDenominator = denominator * subtrahend.denominator
    new Fraction(newNumerator, newDenominator)
  }

  /**
   * Returns the product of this fraction and the given one
   * 
   * @param factor
   * @return product
   * @throws NullPointerException
   */
  override multiply(Fraction factor) {
    requireNonNull(factor, 'factor')
    val newNumerator = numerator * factor.numerator
    val newDenominator = denominator * factor.denominator
    new Fraction(newNumerator, newDenominator)
  }

  /**
   * Return the quotient of this fraction and the given one
   * 
   * @param divisor
   * @return quotient
   * @throws NullPointerException
   */
  override divide(Fraction divisor) {
    requireNonNull(divisor, 'divisor')
    checkArgument(divisor.numerator != 0BI, 'expected divisor.numerator != 0 but actual %s', divisor.numerator)
    multiply(divisor.invert)
  }

  /**
   * Calculates the power of this fraction by the given exponent and returns it
   * 
   * @param exponent
   * @return pow
   * @throws IllegalArgumentException if exponent < 0
   */
  override pow(int exponent) {
    checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
    if (exponent > 1)
      return multiply(pow(exponent - 1))
    else if (exponent == 1)
      return this
    ONE
  }

  /**
   * Returns the negated fraction of this one
   * 
   * @return negated
   */
  override negate() {
    new Fraction(-numerator, denominator)
  }

  /**
   * Returns the inverted fraction of this one
   * 
   * @return inverted
   * @throws IllegalStateException if numerator == 0
   */
  override invert() {
    checkState(invertible, 'expected numerator != 0 but actual %s', numerator)
    new Fraction(denominator, numerator)
  }

  /**
   * Returns if this fraction is invertible
   * 
   * @return true if numerator != 0, false otherwise
   */
  override invertible() {
    numerator != 0BI
  }

  /**
   * Returns a string representation of this fraction
   * 
   * @return string
   */
  override asString() {
    if (denominator < 0BI)
      return '''«numerator» / («denominator»)'''
    '''«numerator» / «denominator»'''
  }

  /**
   * Compares this fraction to the given one and returns an int which indicates which one is less
   * 
   * @param other
   * @return -1 if this < other, 1 if this > other, 0 otherwise 
   */
  override compareTo(Fraction other) {
    requireNonNull(other, 'other')
    if (lessThan(other))
      return -1
    if (greaterThan(other))
      return 1
    0
  }

  /**
   * Compares this fraction to the given one and returns a boolean which indicates which one is less
   * 
   * @param other
   * @return true if this <= other, false otherwise
   */
  def lessThanOrEqualTo(Fraction other) {
    requireNonNull(other, 'other')
    val normalized = normalize
    val normalizedOther = other.normalize
    normalizedOther.denominator * normalized.numerator <= normalized.denominator * normalizedOther.numerator
  }

  /**
   * Compares this fraction to the given one and returns a boolean which indicates which one is greater
   * 
   * @param other
   * @return true if this >= other, false otherwise
   */
  def greaterThanOrEqualTo(Fraction other) {
    requireNonNull(other, 'other')
    !lessThanOrEqualTo(other) || equivalent(other)
  }

  /**
   * Compares this fraction to the given one and returns a boolean which indicates which one is less
   * 
   * @param other
   * @return true if this < other, false otherwise
   */
  def lessThan(Fraction other) {
    requireNonNull(other, 'other')
    !greaterThanOrEqualTo(other)
  }

  /**
   * Compares this fraction to the given one and returns a boolean which indicates which one is greater
   * 
   * @param other
   * @return true if this < other, false otherwise
   */
  def greaterThan(Fraction other) {
    requireNonNull(other, 'other')
    !lessThanOrEqualTo(other)
  }

  /**
   * Compares this fraction to the given one and returns a the minimum
   * 
   * @param other
   * @return minimum
   */
  def min(Fraction other) {
    requireNonNull(other, 'other')
    if (greaterThan(other))
      return other
    this
  }

  /**
   * Compares this fraction to the given one and returns a the maximum
   * 
   * @param other
   * @return maximum
   */
  def max(Fraction other) {
    requireNonNull(other, 'other')
    if (lessThan(other))
      return other
    this
  }

  /**
   * Returns the normalized fraction of this one
   * 
   * @return normalized
   */
  def normalize() {
    if (signum < 0)
      return new Fraction(-numerator.abs, denominator.abs)
    if (signum == 0)
      return ZERO
    if (numerator < 0BI)
      return new Fraction(numerator.abs, denominator.abs)
    this
  }

  /**
   * Returns the absolute fraction of this one
   * 
   * @return absolute
   */
  def abs() {
    new Fraction(numerator.abs, denominator.abs)
  }

  /**
   * Returns the signum of this fraction
   * 
   * @return signum
   */
  def signum() {
    numerator.signum * denominator.signum
  }

  /**
   * Returns the reduced fraction of this one
   * 
   * @return reduced
   */
  def reduce() {
    val gcd = numerator.gcd(denominator)
    new Fraction(numerator / gcd, denominator / gcd)
  }

  /**
   * Compares this fraction to the given one and returns a boolean which indicates if this fraction is equivalent to the given one
   * 
   * @return true if the fractions are equivalent to themselves, false otherwise
   */
  def equivalent(Fraction other) {
    requireNonNull(other, 'other')
    normalize.reduce == other.normalize.reduce
  }
}
