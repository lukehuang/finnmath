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
import java.util.Map
import org.eclipse.xtend.lib.annotations.Data

import static java.util.Objects.requireNonNull

@Beta
@Data
final class BigIntVector implements Vector<BigIntVector, BigInteger> {
    val Map<Integer, BigInteger> map

    new(Map<Integer, BigInteger> map) {
        requireNonNull(map)
        this.map = map
    }

    override negate() {
        null
    }

    override abs() {
        null
    }

    override size() {
        0
    }

    static def builder() {
        new BigIntVectorBuilder
    }

    @Data
    static class BigIntVectorBuilder extends AbstractVectorBuilder<BigInteger> implements VectorBuilder<BigIntVector, BigInteger> {
        override addPut(Integer index, BigInteger entry) {
            requireNonNull(entry)
            map.put(index, map.get(index) + entry)
        }

        override build() {
            map.forEach [ index, entry |
                requireNonNull(entry)
            ]
            new BigIntVector(map)
        }
    }
}
