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
import java.util.Optional
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
@ToString
@Accessors
final class SquareRootCalculator {
  val Optional<BigInteger> optInteger;
  val BigDecimal decimal
  val BigDecimal precision
  val int scale

  new(BigInteger integer) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    optInteger = Optional.of(integer)
    decimal = new BigDecimal(integer)
    precision = 0.0000000001BD
    scale = 0
  }

  new(BigInteger integer, BigDecimal precision) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    optInteger = Optional.of(integer)
    decimal = new BigDecimal(integer)
    this.precision = requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    scale = 10
  }

  new(BigInteger integer, int scale) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    optInteger = Optional.of(integer)
    decimal = new BigDecimal(integer)
    this.precision = 0.0000000001BD
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    this.scale = scale
  }

  new(BigInteger integer, BigDecimal precision, int scale) {
    requireNonNull(integer, 'integer')
    checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
    optInteger = Optional.of(integer)
    decimal = new BigDecimal(integer)
    this.precision = requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    this.scale = scale
  }

  new(BigDecimal decimal) {
    optInteger = Optional.empty
    this.decimal = requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    precision = 0.0000000001BD
    scale = 10
  }

  new(BigDecimal decimal, int scale) {
    optInteger = Optional.empty
    this.decimal = requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    precision = 0.0000000001BD
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    this.scale = scale
  }

  new(BigDecimal decimal, BigDecimal precision) {
    optInteger = Optional.empty
    this.decimal = requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    this.precision = requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    scale = 10
  }

  new(BigDecimal decimal, BigDecimal precision, int scale) {
    optInteger = Optional.empty
    this.decimal = requireNonNull(decimal, 'decimal')
    checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
    this.precision = requireNonNull(precision, 'precision')
    checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
    checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
    this.scale = scale
  }

  def sqrt() {
    heronsMethod.scale = scale
  }

  def sqrtOfPerfectSquare() {
    checkState(perfectSquare, 'expected perfect square but actual %s', optInteger.get)
    BigIntegerMath.sqrt(optInteger.get, RoundingMode.UNNECESSARY)
  }

  def perfectSquare() {
    checkState(optInteger.present, 'expect optInteger.present to be true but actual %s', optInteger.present)
    val integer = optInteger.get
    var sum = 0BI
    for (var odd = 1BI; sum < integer; odd += 2BI)
      sum += odd
    sum == integer
  }

  protected def heronsMethod() {
    var predecessor = seedValue
    var successor = heronsMethod(predecessor)
    while ((successor - predecessor).abs > precision) {
      predecessor = successor
      successor = heronsMethod(successor)
    }
    successor
  }

  protected def heronsMethod(BigDecimal predecessor) {
    (predecessor ** 2 + decimal) / (2BD * predecessor)
  }

  protected def seedValue() {
    val it = scientificNotationForSqrt
    if (coefficient >= 10BD)
      return 6BD * 10BD ** (exponent / 2)
    2BD * 10BD ** (exponent / 2)
  }

  protected def scientificNotationForSqrt() {
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
