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
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a vector which is based on {@link ImmutableMap} and uses {@link BigInteger} as type 
 * for its entries
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@EqualsHashCode
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
        checkArgument(map.size === summand.size, 'equal sizes expected but actual %s != %s', map.size, summand.size)
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
        checkArgument(map.size === subtrahend.size, 'equal sizes expected but actual %s != %s', map.size,
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
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        var result = 0BI
        for (index : (1 .. map.size))
            result += map.get(index) * vector.entry(index)
        result
    }

    /**
     * Returns the square of the euclidean norm of this {@link BigIntVector}
     * 
     * @return The square of the euclidean norm
     * @since 1
     * @author Lars Tennstedt
     * @see #dotProduct
     */
    override euclideanNormPow2() {
        dotProduct
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     * 
     * @return The euclidean norm
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    override euclideanNorm() {
        SquareRootCalculator::sqrt(euclideanNormPow2)
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     * 
     * @param precision the precision for the termination condition
     * @return The euclidean norm
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal)
     */
    override euclideanNorm(BigDecimal precision) {
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        SquareRootCalculator::sqrt(euclideanNormPow2, precision)
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     * 
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger, int, int)
     */
    override euclideanNorm(int scale, int roundingMode) {
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        SquareRootCalculator::sqrt(euclideanNormPow2, scale, roundingMode)
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     * 
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal, int, int)
     */
    override euclideanNorm(BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        SquareRootCalculator::sqrt(euclideanNormPow2, precision, scale, roundingMode)
    }

    /**
     * Returns the square of the euclidean distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #euclideanNormPow2
     */
    override euclideanDistancePow2(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        subtract(vector).euclideanNormPow2
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The the euclidean distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     * @author Lars Tennstedt
     */
    override euclideanDistance(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        SquareRootCalculator::sqrt(euclideanDistancePow2(vector))
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param precision the precision for the termination condition
     * @return The euclidean distance
     * @throws NullPointerException if {@code vector == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal)
     */
    override euclideanDistance(BigIntVector vector, BigDecimal precision) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        SquareRootCalculator::sqrt(euclideanDistancePow2(vector), precision)
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger, int, int)
     */
    override euclideanDistance(BigIntVector vector, int scale, int roundingMode) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        SquareRootCalculator::sqrt(euclideanDistancePow2(vector), scale, roundingMode)
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean distance
     * @throws NullPointerException if {@code vector == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger, BigDecimal, int, int)
     */
    override euclideanDistance(BigIntVector vector, BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        requireNonNull(precision, 'precision')
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual {}', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual {}', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual {}',
            roundingMode)
        SquareRootCalculator::sqrt(euclideanDistancePow2(vector), precision, scale, roundingMode)
    }

    /**
     * Returns the taxicab norm of this {@link BigIntVector}
     * 
     * @return The taxicab norm
     * @since 1
     * @author Lars Tennstedt
     */
    override taxicabNorm() {
        var result = 0BI
        for (entry : map.values)
            result += entry.abs
        result
    }

    /**
     * Returns the taxicab distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The taxicab distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #taxicabNorm
     * @see #subtract
     */
    override taxicabDistance(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        subtract(vector).taxicabNorm
    }

    /**
     * Returns the infinity norm of this {@link BigIntVector}
     * 
     * @return The infinity norm
     * @since 1
     * @author Lars Tennstedt
     * @see BigInteger#max
     * @see BigInteger#abs
     * @see IterableExtensions#min
     * @see IterableExtensions#max
     */
    override infinityNorm() {
        map.values.min.abs.max(map.values.max.abs)
    }

    /**
     * Returns the infinity distance from this {@link BigIntVector} to the given one
     * 
     * @param vector The other {@link BigIntVector}
     * @return The infinity distance
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #infinityNorm
     * @see #subtract
     */
    override infinityDistance(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(map.size === vector.size, 'expected equal sizes but actual %s != %s', map.size, vector.size)
        subtract(vector).infinityNorm
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
     * @return A {@link BigIntVectorBuilder}
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

        /**
         * Returns the built {@link BigIntVector}
         * 
         * @return The {@link BigIntVector}
         * @throws NullPointerException if one {@code entry == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableMap#copyOf
         */
        override build() {
            map.forEach [ index, entry |
                requireNonNull(entry, 'entry')
            ]
            new BigIntVector(ImmutableMap::copyOf(map));
        }
    }
}
