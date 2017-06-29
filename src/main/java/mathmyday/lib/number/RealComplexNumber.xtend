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
import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@Data
final class RealComplexNumber implements MathNumber<RealComplexNumber, RealComplexNumber>, ComplexNumber<BigDecimal, RealComplexNumber> {
  public static val ZERO = new RealComplexNumber(0BD, 0BD)
  public static val ONE = new RealComplexNumber(1BD, 0BD)
  public static val I = new RealComplexNumber(0BD, 1BD)
  BigDecimal real
  BigDecimal imaginary

  new(SimpleComplexNumber complexNumber) {
    requireNonNull(complexNumber, 'complexNumber')
    real = new BigDecimal(complexNumber.real)
    imaginary = new BigDecimal(complexNumber.imaginary)
  }

  new(BigDecimal real, BigDecimal imaginary) {
    this.real = requireNonNull(real, 'real')
    this.imaginary = requireNonNull(imaginary, 'imaginary')
  }

  override add(RealComplexNumber summand) {
    requireNonNull(summand, 'summand')
    new RealComplexNumber(real + summand.real, imaginary + summand.imaginary)
  }

  override subtract(RealComplexNumber subtrahend) {
    requireNonNull(subtrahend, 'subtrahend')
    new RealComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
  }

  override multiply(RealComplexNumber factor) {
    requireNonNull(factor, 'factor')
    val newReal = real * factor.real - imaginary * factor.imaginary
    val newImaginary = real * factor.imaginary + imaginary * factor.real
    new RealComplexNumber(newReal, newImaginary)
  }

  override divide(RealComplexNumber divisor) {
    requireNonNull(divisor, 'divisor')
    checkArgument(divisor != ZERO, 'expected divisor != 0 but actual %s', divisor)
    val denominator = divisor.real ** 2 + divisor.imaginary ** 2
    val newReal = (real * divisor.real + imaginary * divisor.imaginary) / denominator
    val newImaginary = (imaginary * divisor.real - real * divisor.imaginary) / denominator
    new RealComplexNumber(newReal, newImaginary)
  }

  override pow(int exponent) {
    checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
    if(exponent > 1)
      return multiply(pow(exponent - 1))
    else if(exponent == 1)
      return this
    ONE
  }

  override negate() {
    new RealComplexNumber(-real, -imaginary)
  }

  override asString() {
    if(real != 0BD)
      if(imaginary > 0BD)
        return '''«real» + «imaginary»i'''
      else if(imaginary < 0BD)
        return '''«real» - «imaginary»i'''
      else
        return '''«real»'''
    if(imaginary > 0BD)
      return '''«imaginary»i'''
    else if(imaginary < 0BD)
      return '''- «imaginary»i'''
    '0'
  }

  override absPow2() {
    real ** 2 + imaginary ** 2
  }

  override conjugate() {
    new RealComplexNumber(real, -imaginary)
  }
}
