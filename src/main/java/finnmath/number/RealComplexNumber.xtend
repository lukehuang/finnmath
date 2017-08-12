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
import finnmath.linear.DecimalMatrix
import finnmath.util.SquareRootCalculator
import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a complex number which uses {@code BigDecimal} for the real and imaginary part 
 */
@Beta
@FinalFieldsConstructor
final class RealComplexNumber extends ComplexNumber<BigDecimal, RealComplexNumber, DecimalMatrix> implements MathNumber<RealComplexNumber, RealComplexNumber> {
  public static val ZERO = new RealComplexNumber(0BD, 0BD)
  public static val ONE = new RealComplexNumber(1BD, 0BD)
  public static val I = new RealComplexNumber(0BD, 1BD)

  new(SimpleComplexNumber complexNumber) {
    super(new BigDecimal(requireNonNull(complexNumber, 'complexNumber').real), new BigDecimal(complexNumber.imaginary))
  }

  /**
   * Returns the sum of this complex number and the given one
   * 
   * @param summand
   * @return sum
   * @throws NullPointerException
   */
  override add(RealComplexNumber summand) {
    requireNonNull(summand, 'summand')
    new RealComplexNumber(real + summand.real, imaginary + summand.imaginary)
  }

  /**
   * Returns the difference of this complex number and the given one
   * 
   * @param subtrahend
   * @return difference
   * @throws NullPointerException
   */
  override subtract(RealComplexNumber subtrahend) {
    requireNonNull(subtrahend, 'subtrahend')
    new RealComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
  }

  /**
   * Returns the product of this complex number and the given one
   * 
   * @param factor
   * @return product
   * @throws NullPointerException
   */
  override multiply(RealComplexNumber factor) {
    requireNonNull(factor, 'factor')
    val newReal = real * factor.real - imaginary * factor.imaginary
    val newImaginary = real * factor.imaginary + imaginary * factor.real
    new RealComplexNumber(newReal, newImaginary)
  }

  /**
   * Returns the quotient as {@RealComplexNumber} of this complex number and the given one
   * 
   * @param divisor
   * @return quotient
   * @throws NullPointerException
   */
  override divide(RealComplexNumber divisor) {
    requireNonNull(divisor, 'divisor')
    checkArgument(divisor != ZERO, 'expected divisor != 0 but actual %s', divisor)
    val denominator = divisor.real ** 2 + divisor.imaginary ** 2
    val newReal = (real * divisor.real + imaginary * divisor.imaginary) / denominator
    val newImaginary = (imaginary * divisor.real - real * divisor.imaginary) / denominator
    new RealComplexNumber(newReal, newImaginary)
  }

  /**
   * Calculates the power of this complex number by the given exponent and returns it
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
   * Returns the negated complex number of this one
   * 
   * @return negated
   */
  override negate() {
    new RealComplexNumber(-real, -imaginary)
  }

  /**
   * Returns the inverted complex number of this one
   * 
   * @return inverted
   * @throws IllegalStateException if numerator == 0
   */
  override invert() {
    checkState(invertible, 'expected != 0 but actual %s', this)
    ONE.divide(this)
  }

  /**
   * Returns if this complex number is invertible
   * 
   * @return true if this != 0, false otherwise
   */
  override invertible() {
    this != ZERO
  }

  /**
   * Returns a string representation of this complex number
   * 
   * @return string
   */
  override asString() {
    if (real != 0BD)
      if (imaginary > 0BD)
        return '''«real» + «imaginary»i'''
      else if (imaginary < 0BD)
        return '''«real» - «imaginary»i'''
      else
        return '''«real»'''
    if (imaginary > 0BD)
      return '''«imaginary»i'''
    else if (imaginary < 0BD)
      return '''- «imaginary»i'''
    '0'
  }

  /**
   * Returns the square of the absolute of this complex number
   * 
   * @return square of the absolute
   */
  override absPow2() {
    real ** 2 + imaginary ** 2
  }

  /**
   * Returns the absolute of this complex number
   * 
   * @return absolute
   */
  override abs() {
    SquareRootCalculator::sqrt(absPow2)
  }

  /**
   * Returns the conjugate of this complex number
   * 
   * @return conjugated
   */
  override conjugate() {
    new RealComplexNumber(real, -imaginary)
  }

  /**
   * Returns a matrix representation of this complex number
   * 
   * @return matrix
   */
  override matrix() {
    DecimalMatrix::builder(2, 2).put(1, 1, real).put(1, 2, -imaginary).put(2, 1, imaginary).put(2, 2, real).build
  }
}
