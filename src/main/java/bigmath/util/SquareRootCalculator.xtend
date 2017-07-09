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
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
@ToString
@Accessors
final class SquareRootCalculator {
  val BigDecimal decimal
  val BigDecimal epsilon

  new(BigInteger integer) {
    requireNonNull(integer, 'integer')
    decimal = new BigDecimal(integer)
    epsilon = 0.0000000001BD
    checkArgument(integer >= 0BI, 'expected >= 0 but actual %s', integer)
  }

  new(BigDecimal decimal) {
    this.decimal = requireNonNull(decimal, 'decimal')
    epsilon = 0.0000000001BD
    checkArgument(decimal >= 0BD, 'expected >= 0 but actual %s', decimal)
  }

  new(BigInteger integer, BigDecimal epsilon) {
    requireNonNull(integer, 'integer')
    decimal = new BigDecimal(integer)
    this.epsilon = requireNonNull(epsilon, 'epsilon')
    checkArgument(integer >= 0BI, 'expected >= 0 but actual %s', integer)
    checkArgument(0BD < epsilon && epsilon < 1BD, 'expected epsilon in (0, 1) but actual %s', epsilon)
  }

  new(BigDecimal decimal, BigDecimal epsilon) {
    this.decimal = requireNonNull(decimal, 'decimal')
    this.epsilon = requireNonNull(epsilon, 'epsilon')
    checkArgument(decimal >= 0BD, 'expected >= 0 but actual %s', decimal)
    checkArgument(0BD < epsilon && epsilon < 1BD, 'expected epsilon in (0, 1) but actual %s', epsilon)
  }

  def sqrt() {
    heronsMethod
  }

  protected def heronsMethod() {
    var predecessor = seedValue
    var successor = heronsMethod(predecessor)
    while ((successor - predecessor).abs > epsilon) {
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
