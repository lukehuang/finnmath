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

import java.math.BigDecimal
import java.util.List
import mathmyday.lib.util.MathRandom
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat

class SimpleComplexNumberTest {
  static List<SimpleComplexNumber> complexNumbers
  static List<SimpleComplexNumber> others

  @BeforeClass
  def static void setUpClass() {
    val size = 100
    val mathRandom = new MathRandom
    complexNumbers = mathRandom.createSimpleComplexNumbers(Integer.MAX_VALUE, size)
    others = mathRandom.createSimpleComplexNumbers(Integer.MAX_VALUE, size)
  }

  @Test(expected=NullPointerException)
  def void divideException() {
    SimpleComplexNumber.ZERO.divide(null)
  }

  @Test
  def divideTest() {
    complexNumbers.forEach [
      val other = others.get(complexNumbers.indexOf(it))
      val denominator = new BigDecimal(other.real ** 2 + other.imaginary ** 2)
      val newReal = new BigDecimal(real * other.real + imaginary * other.imaginary) / denominator
      val newImaginary = new BigDecimal(imaginary * other.real - real * other.imaginary) / denominator
      new RealComplexNumber(newReal, newImaginary)
      assertThat(divide(other)).isEqualTo(new RealComplexNumber(newReal, newImaginary))
    ]
  }
}
