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
import finnmath.util.SquareRootCalculator
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Map

import static com.google.common.base.Preconditions.checkArgument
import static finnmath.util.SquareRootCalculator.sqrt
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a vector which uses {@link BigInteger} as type for its entries
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
final class BigIntVector extends Vector<BigInteger, BigIntVector, BigDecimal> {
    private new(ImmutableMap<Integer, BigInteger> map) {
        super(map)
    }

    /**
     * Returns the sum of this {@link BigIntVector} and the given one
     * 
     * @param summand the summand
     * @return The sum
     * @throws NullPointerException if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override add(BigIntVector summand) {
        requireNonNull(summand, 'summand')
        checkArgument(map.size == summand.size, 'equal sizes expected but actual %s != %s', map.size, summand.size)
        val builder = builder(map.size)
        map.forEach [ index, entry |
            builder.put(entry + summand.entry(index))
        ]
        builder.build
    }

    /**
     * Returns the difference of this {@link BigIntVector} and the given one
     * 
     * @param subtrahend the subtrahend
     * @return The difference
     * @throws NullPointerException if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override subtract(BigIntVector subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        checkArgument(map.size == subtrahend.size, 'equal sizes expected but actual %s != %s', map.size,
            subtrahend.size)
        val builder = builder(map.size)
        map.forEach [ index, entry |
            builder.put(entry - subtrahend.entry(index))
        ]
        builder.build
    }

    /**
     * Returns the scalar product of the given scalar and this {@link BigIntVector}
     * 
     * @param scalar the scalar
     * @return The scalar product
     * @throws NullPointerException if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    override scalarMultiply(BigInteger scalar) {
        requireNonNull(scalar, 'scalar')
        val builder = builder(map.size)
        map.forEach [ index, entry |
            builder.put(scalar * entry)
        ]
        builder.build
    }

    /**
     * Returns the negated {@link BigIntVector} of this one
     * 
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    override negate() {
        scalarMultiply(-1BI)
    }

    /**
     * Returns the square of the norm of this {@link BigIntVector}
     * 
     * @return The square of the norm
     * @since 1
     * @author Lars Tennstedt
     * @see #dotProduct
     */
    override normPow2() {
        dotProduct
    }

    /**
     * Returns the norm of this {@link BigIntVector}
     * 
     * @return The norm
     * @since 1
     * @author Lars Tennstedt
     * @see #normPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    override norm() {
        sqrt(normPow2)
    }

    /**
     * Returns the norm of this {@link BigIntVector}
     * 
     * @param precision the precision for the termination condition
     * @return The norm
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #normPow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal)
     */
    override norm(BigDecimal precision) {
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        sqrt(normPow2, precision)
    }

    /**
     * Returns the norm of this {@link BigIntVector}
     * 
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The norm
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #normPow2
     * @see SquareRootCalculator#sqrt(BigInteger, int, int)
     */
    override norm(int scale, int roundingMode) {
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        sqrt(normPow2, scale, roundingMode)
    }

    /**
     * Returns the norm of this {@link BigIntVector}
     * 
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The norm
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #normPow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal, int, int)
     */
    override norm(BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        sqrt(normPow2, precision, scale, roundingMode)
    }

    /**
     * Returns the dot product of this {@link BigIntVector} and the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The dot product
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     */
    override dotProduct(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        var result = 0BI
        for (index : (1 .. map.size))
            result += map.get(index) * vector.entry(index)
        result
    }

    /**
     * Returns the square of the distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The square of the distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #normPow2
     */
    override distancePow2(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        subtract(vector).normPow2
    }

    /**
     * Returns the distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The the distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @see #distancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     * @author Lars Tennstedt
     */
    override distance(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        sqrt(distancePow2(vector))
    }

    /**
     * Returns the distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param precision the precision for the termination condition
     * @return The the distance
     * @throws NullPointerException if {@code vector == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #distancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    override distance(BigIntVector vector, BigDecimal precision) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        sqrt(distancePow2(vector))
    }

    /**
     * Returns the distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The the distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #distancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    override distance(BigIntVector vector, int scale, int roundingMode) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        sqrt(distancePow2(vector))
    }

    /**
     * Returns the distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The the distance
     * @throws NullPointerException if {@code vector == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #distancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    override distance(BigIntVector vector, BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size == vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        sqrt(distancePow2(vector))
    }

    /**
     * Returns the size of the underlying {@link Map}
     * 
     * @return size the size
     * @since 1
     * @author Lars Tennstedt
     */
    override size() {
        map.size
    }

    /**
     * Returns a {@link BigIntVectorBuilder}
     * 
     * @param size the size the resulting {@link BigIntVector}
     * @return the {@link BigIntVectorBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    static def builder(int size) {
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        new BigIntVectorBuilder(size)
    }

    /**
     * The builder for {@link BigIntVector BigIntVectors}
     * 
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    static final class BigIntVectorBuilder extends VectorBuilder<BigInteger, BigIntVector, BigIntVectorBuilder> {
        private new(Integer size) {
            super(size)
        }

        override addToEntryAndPut(Integer index, BigInteger entry) {
            requireNonNull(index, 'index')
            checkArgument(map.containsKey(index), 'expected index in [1, %s] but actual %s', map.size, index)
            val existing = map.get(index)
            requireNonNull(existing, 'existing')
            requireNonNull(entry, 'entry')
            map.put(index, map.get(index) + entry)
            this
        }

        /**
         * Returns the built {@link BigIntVector}
         * 
         * @return the {@link BigIntVector}
         * @throws NullPointerException if one {@code entry == null}
         * @since 1
         * @author Lars Tennstedt  
         */
        override build() {
            map.forEach [ index, entry |
                requireNonNull(entry, 'entry')
            ]
            new BigIntVector(ImmutableMap::copyOf(map));
        }
    }
}
