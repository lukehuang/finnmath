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

package bigmath.util

import bigmath.number.ScientificNotation
import com.google.common.annotations.Beta
import java.math.BigDecimal
import java.math.BigInteger

import static com.google.common.base.Preconditions.checkArgument
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

@Beta
final class SquareRootCalculator {
  private new() {
  }

  static def sqrt(BigInteger integer) {
    var sqrt = seedValue(integer)
    for (i : 1 .. 1000)
      sqrt = sqrt - sqrt ** 2 / 2BD * sqrt
    sqrt
  }

  static def sqrt(BigDecimal decimal) {
    var sqrt = seedValue(decimal)
    for (i : 1 .. 1000)
      sqrt = sqrt - sqrt ** 2 / 2BD * sqrt
    sqrt
  }

  protected static def seedValue(BigInteger integer) {
    requireNonNull(integer, 'integer')
    val it = scientificNotationForSqrt(integer)
    if (coefficient >= 10BD)
      return 2BD * 10BD ** (exponent / 2)
    6BD * 10BD ** (exponent / 2)
  }

  protected static def scientificNotationForSqrt(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected >= 0 but actual %s', integer)
    val decimal = new BigDecimal(integer)
    if (0BI < integer && integer < 100BI)
      return new ScientificNotation(decimal, 0)
    else if (integer >= 100BI) {
      var coefficient = decimal
      var exponent = 0
      while (coefficient.abs >= 100BD) {
        coefficient /= 100BD
        exponent = addExact(exponent, 2)
      }
      return new ScientificNotation(coefficient, exponent)
    }
    new ScientificNotation(0BD, 0)
  }

  protected static def seedValue(BigDecimal decimal) {
    requireNonNull(decimal, 'decimal')
    val it = scientificNotationForSqrt(decimal)
    if (coefficient >= 10BD)
      return 2BD * 10BD ** (exponent / 2)
    6BD * 10BD ** (exponent / 2)
  }

  protected static def scientificNotationForSqrt(BigDecimal decimal) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected >= 0 but actual %s', decimal)
    if (0BD < decimal && decimal < 100BD)
      return new ScientificNotation(decimal, 0)
    else if (decimal >= 100BD) {
      var coefficient = decimal
      var exponent = 0
      while (coefficient.abs >= 100BD) {
        coefficient /= 100BD
        exponent = addExact(exponent, 2)
      }
      return new ScientificNotation(coefficient, exponent)
    }
    new ScientificNotation(0BD, 0)
  }
}
