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

package number

import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Data
class RealComplexNumber implements MathNumber<RealComplexNumber>, ComplexNumber<BigDecimal, RealComplexNumber> {
  public static val ZERO = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ZERO)
  public static val ONE = new RealComplexNumber(BigDecimal.ONE, BigDecimal.ZERO)
  public static val I = new RealComplexNumber(BigDecimal.ZERO, BigDecimal.ONE)
  BigDecimal real
  BigDecimal imaginary

  new(BigDecimal real, BigDecimal imaginary) {
    this.real = requireNonNull(real)
    this.imaginary = requireNonNull(imaginary)
  }

  override add(RealComplexNumber summand) {
    requireNonNull(summand)
    new RealComplexNumber(real + summand.real, imaginary + summand.imaginary)
  }

  override subtract(RealComplexNumber subtrahend) {
    requireNonNull(subtrahend)
    new RealComplexNumber(real - subtrahend.real, imaginary - subtrahend.imaginary)
  }

  override multiply(RealComplexNumber factor) {
    requireNonNull(factor)
    val newReal = real * factor.real - imaginary * factor.imaginary
    val newImaginary = real * factor.imaginary + imaginary * factor.real
    new RealComplexNumber(newReal, newImaginary)
  }

  override pow(int exponent) {
    checkArgument(exponent >= 0, "exponent < 0")
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
    if(real != BigDecimal.ZERO)
      if(imaginary > BigDecimal.ZERO)
        return '''«real» + «imaginary»i'''
      else if(imaginary < BigDecimal.ZERO)
        return '''«real» - «imaginary»i'''
      else
        return '''«real»'''
    if(imaginary > BigDecimal.ZERO)
      return '''«imaginary»i'''
    else if(imaginary < BigDecimal.ZERO)
      return '''- «imaginary»i'''
    "0"
  }

  override absPow2() {
    real.pow(2) + imaginary.pow(2)
  }

  override conjugate() {
    new RealComplexNumber(real, -imaginary)
  }

  def divide(RealComplexNumber divisor) {
    requireNonNull(divisor)
    val denominator = divisor.real.pow(2) + divisor.imaginary.pow(2)
    val newReal = (real * divisor.real + imaginary * divisor.imaginary) / denominator
    val newImaginary = (imaginary * divisor.real - real * divisor.imaginary) / denominator
    new RealComplexNumber(newReal, newImaginary)
  }
}
