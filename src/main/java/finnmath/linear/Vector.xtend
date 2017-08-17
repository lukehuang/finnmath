/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, Lars Tennstedt
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

package finnmath.linear

import com.google.common.annotations.Beta
import com.google.common.collect.ImmutableMap
import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
@ToString
@Accessors
abstract class Vector<E, V, N> {
    protected val ImmutableMap<Integer, E> map

    protected new(ImmutableMap<Integer, E> map) {
        this.map = map
    }

    def entry(Integer index) {
        requireNonNull(index, 'index')
        checkArgument(map.containsKey(index), 'expected index in [1, %s] but actual %s', map.size, index)
        map.get(index)
    }

    def V add(V summand)

    def V subtract(V subtrahend)

    def V scalarMultiply(E scalar)

    def V negate()

    def E normPow2()

    def N norm()

    def N norm(BigDecimal precision)

    def N norm(int scale, int roundingMode)

    def N norm(BigDecimal precision, int scale, int roundingMode)

    def E dotProduct(V vector)

    def E distancePow2(V vector)

    def N distance(V vector)

    def N distance(V vector, BigDecimal precision)

    def N distance(V vector, int scale, int roundingMode)

    def N distance(V vector, BigDecimal precision, int scale, int roundingMode)

    def int size()
}
