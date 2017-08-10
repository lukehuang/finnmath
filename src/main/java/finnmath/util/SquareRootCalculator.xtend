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
import com.google.common.math.BigIntegerMath
import finnmath.number.ScientificNotation
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import org.slf4j.LoggerFactory

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

@Beta
final class SquareRootCalculator {
  public static val BigDecimal DEFAULT_PRECISION = 0.0000000001BD
  public static val int DEFAULT_SCALE = 10
  public static val int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP
  static val log = LoggerFactory.getLogger(SquareRootCalculator)

  static def sqrt(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    sqrt(new BigDecimal(integer), DEFAULT_PRECISION, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
  }

  static def sqrt(BigDecimal decimal) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    heronsMethod(decimal, DEFAULT_PRECISION).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
  }

  static def sqrt(BigInteger integer, BigDecimal precision) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
    sqrt(new BigDecimal(integer), precision, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
  }

  static def sqrt(BigDecimal decimal, BigDecimal precision) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    heronsMethod(decimal, precision).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
  }

  static def sqrt(BigInteger integer, int scale, int roundingMode) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s', roundingMode)
    sqrt(new BigDecimal(integer), DEFAULT_PRECISION, scale, roundingMode)
  }

  static def sqrt(BigDecimal decimal, int scale, int roundingMode) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s', roundingMode)
    heronsMethod(decimal, DEFAULT_PRECISION).setScale(scale, roundingMode)
  }

  static def sqrt(BigInteger integer, BigDecimal precision, int scale, int roundingMode) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s', roundingMode)
    sqrt(new BigDecimal(integer), precision, scale, roundingMode)
  }

  static def sqrt(BigDecimal decimal, BigDecimal precision, int scale, int roundingMode) {
    requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s', roundingMode)
    heronsMethod(decimal, precision).setScale(scale, roundingMode)
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
    log.debug('calculating square root for {} with precision = {}', decimal.toPlainString, precision.toPlainString)
    var predecessor = seedValue(decimal)
    log.debug('seed value = {}', predecessor.toPlainString)
    var successor = calculateSuccessor(predecessor, decimal)
    var iterations = 1
    while ((successor - predecessor).abs >= precision) {
      log.debug('|successor - predecessor| = {}', (successor - predecessor).abs.toPlainString)
      predecessor = successor
      successor = calculateSuccessor(successor, decimal)
      iterations++
    }
    log.debug('terminated after {} iterations', iterations)
    log.debug('sqrt({}) = {}', decimal.toPlainString, successor.toPlainString)
    successor
  }

  protected static def calculateSuccessor(BigDecimal predecessor, BigDecimal decimal) {
    log.debug('iteration')
    log.debug('predecessor = {}', predecessor.toPlainString)
    val successor = (predecessor ** 2 + decimal) / (2BD * predecessor)
    log.debug('successor = {}', successor.toPlainString)
    successor
  }

  protected static def seedValue(BigDecimal decimal) {
    val it = scientificNotationForSqrt(decimal)
    log.debug('Scientific notation of {} is {}.', decimal.toPlainString, asString)
    if (coefficient >= 10BD)
      return 6BD * 10BD ** (exponent / 2)
    2BD * 10BD ** (exponent / 2)
  }

  protected static def scientificNotationForSqrt(BigDecimal decimal) {
    log.debug('calculating scientific notification for {}', decimal.toPlainString)
    if (0BD < decimal && decimal < 100BD) {
      log.debug('{} in (0, 100)', decimal.toPlainString)
      return new ScientificNotation(decimal, 0)
    } else if (decimal >= 100BD) {
      log.debug('{} >= 100)', decimal.toPlainString)
      var coefficient = decimal
      var exponent = 0
      log.debug('coefficient = {}', coefficient.toPlainString)
      log.debug('exponent = {}', exponent)
      while (coefficient.abs >= 100BD) {
        log.debug('iteration step for scientific notification')
        coefficient /= 100BD
        exponent = addExact(exponent, 2)
        log.debug('coefficient = {}', coefficient.toPlainString)
        log.debug('exponent = {}', exponent)
      }
      return new ScientificNotation(coefficient, exponent)
    }
    new ScientificNotation(0BD, 0)
  }
}
