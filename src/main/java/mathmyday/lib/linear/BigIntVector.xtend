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

package mathmyday.lib.linear

import com.google.common.annotations.Beta
import java.math.BigInteger
import org.apache.commons.lang3.builder.Builder
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@Data
final class BigIntVector extends AbstractVector<BigInteger> implements Vector<BigIntVector, BigInteger> {
  override negate() {
    val builder = builder
    map.entrySet.forEach [
      builder.put(-value)
    ]
    builder.build
  }

  override abs() {
    var result = 0BI
    for (it : map.entrySet)
      result += value ** 2
    result
  }

  override size() {
    map.size
  }

  static def builder() {
    new BigIntVectorBuilder
  }

  @Beta
  @Data
  static class BigIntVectorBuilder extends AbstractVectorBuilder<BigInteger> implements Builder<BigIntVector> {
    private new() {
    }

    def addToEntryAndPut(Integer index, BigInteger entry) {
      requireNonNull(index, 'index')
      checkArgument(map.containsKey(index), 'expected index in [0, %s] but actual %s', map.size, index)
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
