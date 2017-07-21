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
import com.google.common.math.BigIntegerMath
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

@Beta
@ToString
final class SquareRootCalculator {
  public static val BigDecimal DEFAULT_PRECISION = 0.0000000001BD
  public static val int DEFAULT_SCALE = 10

  static def sqrt(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    sqrt(new BigDecimal(integer), DEFAULT_PRECISION, DEFAULT_SCALE)
  }

  static def sqrt(BigDecimal decimal) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    heronsMethod(decimal, DEFAULT_PRECISION).scale = DEFAULT_SCALE
  }

  static def sqrt(BigInteger integer, BigDecimal precision) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    sqrt(new BigDecimal(integer), precision, DEFAULT_SCALE)
  }

  static def sqrt(BigDecimal decimal, BigDecimal precision) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    heronsMethod(decimal, precision).scale = DEFAULT_SCALE
  }

  static def sqrt(BigInteger integer, int scale) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    sqrt(new BigDecimal(integer), DEFAULT_PRECISION, scale)
  }

  static def sqrt(BigDecimal decimal, int scale) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    heronsMethod(decimal, DEFAULT_PRECISION).scale = scale
  }

  static def sqrt(BigInteger integer, BigDecimal precision, int scale) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    sqrt(new BigDecimal(integer), precision, scale)
  }

  static def sqrt(BigDecimal decimal, BigDecimal precision, int scale) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    heronsMethod(decimal, precision).scale = scale
  }

  static def sqrtOfPerfectSquare(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    checkState(perfectSquare(integer), 'expected perfect square but actual %s', integer)
    BigIntegerMath.sqrt(integer, RoundingMode.UNNECESSARY)
  }

  static def perfectSquare(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    var sum = 0BI
    for (var odd = 1BI; sum < integer; odd += 2BI)
      sum += odd
    sum == integer
  }

  protected static def heronsMethod(BigDecimal decimal, BigDecimal precision) {
    var predecessor = seedValue(decimal)
    var successor = calculateSuccessor(predecessor, decimal)
    while ((successor - predecessor).abs >= precision) {
      predecessor = successor
      successor = calculateSuccessor(successor, decimal)
    }
    successor
  }

  protected static def calculateSuccessor(BigDecimal predecessor, BigDecimal decimal) {
    (predecessor ** 2 + decimal) / (2BD * predecessor)
  }

  protected static def seedValue(BigDecimal decimal) {
    val it = scientificNotationForSqrt(decimal)
    if (coefficient >= 10BD)
      return 6BD * 10BD ** (exponent / 2)
    2BD * 10BD ** (exponent / 2)
  }

  protected static def scientificNotationForSqrt(BigDecimal decimal) {
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
