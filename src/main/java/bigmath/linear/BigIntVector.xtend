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
 * IMPLIED WARRANTIES OF MERCHANTABDLITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABDLITY, WHETHER IN CONTRACT, STRICT LIABDLITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBDLITY OF SUCH DAMAGE.
 */

package bigmath.linear

import com.google.common.annotations.Beta
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Map
import org.apache.commons.lang3.builder.Builder
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static bigmath.util.SquareRootCalculator.sqrt
import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
@ToString
final class BigIntVector extends Vector<BigIntVector, BigInteger, BigDecimal> {
  private new(Map<Integer, BigInteger> map) {
    super(map)
  }

  override negate() {
    val builder = builder(map.size)
    map.entrySet.forEach [
      builder.put(-value)
    ]
    builder.build
  }

  override add(BigIntVector summand) {
    requireNonNull(summand, 'summand')
    checkArgument(map.size == summand.size, 'equal sizes expected but actual %s != %s', map.size, summand.size)
    val builder = builder(map.size)
    map.forEach [ index, entry |
      builder.put(entry + summand.entry(index))
    ]
    builder.build
  }

  override subtract(BigIntVector subtrahend) {
    requireNonNull(subtrahend, 'subtrahend')
    checkArgument(map.size == subtrahend.size, 'equal sizes expected but actual %s != %s', map.size, subtrahend.size)
    val builder = builder(map.size)
    map.forEach [ index, entry |
      builder.put(entry - subtrahend.entry(index))
    ]
    builder.build
  }

  override scalarMultiply(BigInteger scalar) {
    requireNonNull(scalar, 'scalar')
    val builder = builder(map.size)
    map.forEach [ index, entry |
      builder.put(scalar * entry)
    ]
    builder.build
  }

  override norm() {
    sqrt(normPow2)
  }

  override norm(BigDecimal precision) {
    sqrt(normPow2, precision)
  }

  override norm(int scale) {
    sqrt(normPow2, scale)
  }

  override norm(BigDecimal precision, int scale) {
    sqrt(normPow2, precision, scale)
  }

  override normPow2() {
    dotProduct
  }

  override dotProduct(BigIntVector vector) {
    requireNonNull(vector, 'vector')
    checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
    var result = 0BI
    for (index : (1 .. map.size))
      result += map.get(index) * vector.entry(index)
    result
  }

  override distance(BigIntVector vector) {
    requireNonNull(vector, 'vector')
    checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
    sqrt(distancePow2(vector))
  }

  override distancePow2(BigIntVector vector) {
    requireNonNull(vector, 'vector')
    checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
    subtract(vector).normPow2
  }

  override size() {
    map.size
  }

  static def builder(int size) {
    checkArgument(size > 0, 'expected size > 0 but actual %s', size)
    new BigIntVectorBuilder(size)
  }

  @Beta
  @ToString
  static class BigIntVectorBuilder extends VectorBuilder<BigIntVectorBuilder, BigIntVector, BigInteger> implements Builder<BigIntVector> {
    private new(Integer size) {
      super(size)
    }

    def addToEntryAndPut(Integer index, BigInteger entry) {
      requireNonNull(index, 'index')
      checkArgument(map.containsKey(index), 'expected index in [1, %s] but actual %s', map.size, index)
      val existing = map.get(index)
      requireNonNull(existing, 'existing')
      requireNonNull(entry, 'entry')
      map.put(index, map.get(index) + entry)
    }

    override build() {
      map.forEach [ index, entry |
        requireNonNull(entry, 'entry')
      ]
      new BigIntVector(map)
    }
  }
}
