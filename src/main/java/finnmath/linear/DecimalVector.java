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

package finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static finnmath.util.SquareRootCalculator.sqrt;
import static java.lang.Math.addExact;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import finnmath.util.SquareRootCalculator;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * An immutable implementation of a vector which uses {@link BigDecimal} as type
 * for its entries
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class DecimalVector extends Vector<BigDecimal, DecimalVector, BigDecimal> {
    private DecimalVector(final ImmutableMap<Integer, BigDecimal> map) {
        super(map);
    }

    /**
     * Returns the sum of this {@link DecimalVector} and the given one
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public DecimalVector add(final DecimalVector summand) {
        checkArgument(map.size() == summand.size(), "expected equal sizes but actual %s != %s", map.size(),
                summand.size());
        final DecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, entry) -> {
            builder.put(entry.add(summand.entry(index)));
        });
        return builder.build();
    }

    /**
     * Returns the difference of this {@link DecimalVector} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public DecimalVector subtract(final DecimalVector subtrahend) {
        checkArgument(map.size() == subtrahend.size(), "expected equal sizes but actual %s != %s", map.size(),
                subtrahend.size());
        final DecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, entry) -> {
            builder.put(entry.subtract(subtrahend.entry(index)));
        });
        return builder.build();
    }

    /**
     * Returns the dot product of this {@link DecimalVector} and the given one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @return The dot product
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigDecimal dotProduct(final DecimalVector vector) {
        requireNonNull(vector, "vector");
        ;
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Integer index : map.keySet()) {
            result = result.add(map.get(index).multiply(vector.entry(index)));
        }
        return result;
    }

    /**
     * Returns the scalar product of the given scalar and this {@link DecimalVector}
     *
     * @param scalar
     *            the scalar
     * @return The scalar product
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public DecimalVector scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final DecimalVectorBuilder builder = builder(map.size());
        map.values().forEach(it -> {
            builder.put(scalar.multiply(it));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link DecimalVector} of this one
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    @Override
    public DecimalVector negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * Returns the square of the euclidean norm of this {@link DecimalVector}
     *
     * @return The square of the euclidean norm
     * @since 1
     * @author Lars Tennstedt
     * @see #dotProduct
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * Returns the euclidean norm of this {@link DecimalVector}
     *
     * @return The euclidean norm
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     */
    @Override
    public BigDecimal euclideanNorm() {
        return sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link DecimalVector}
     *
     * @param precision
     *            the precision for the termination condition
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal, BigDecimal)
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual {}", precision);
        return sqrt(euclideanNormPow2(), precision);
    }

    /**
     * Returns the euclidean norm of this {@link DecimalVector}
     *
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The euclidean norm
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal, int, int)
     */
    @Override
    public BigDecimal euclideanNorm(final int scale, final int roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual {}", scale);
        checkArgument((0 <= roundingMode) && (roundingMode <= 7), "expected roundingMode in [0, 7] but actual {}",
                roundingMode);
        return sqrt(euclideanNormPow2(), scale, roundingMode);
    }

    /**
     * Returns the euclidean norm of this {@link DecimalVector}
     *
     * @param precision
     *            the precision for the termination condition
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal, BigDecimal, int, int)
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision, final int scale, final int roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual {}", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual {}", scale);
        checkArgument((0 <= roundingMode) && (roundingMode <= 7), "expected roundingMode in [0, 7] but actual {}",
                roundingMode);
        return sqrt(euclideanNormPow2(), precision, scale, roundingMode);
    }

    /**
     * Returns the square of the euclidean distance from this {@link DecimalVector}
     * to the given one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #euclideanNormPow2
     */
    @Override
    public BigDecimal euclideanDistancePow2(final DecimalVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        return subtract(vector).euclideanNormPow2();
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        return sqrt(euclideanDistancePow2(vector));
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @param precision
     *            the precision for the termination condition
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal, BigDecimal)
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector vector, final BigDecimal precision) {
        requireNonNull(vector, "vector");
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual {}", precision);
        return sqrt(euclideanDistancePow2(vector), precision);
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The deuclidean istance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal, int, int)
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector vector, final int scale, final int roundingMode) {
        requireNonNull(vector, "vector");
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        checkArgument(scale >= 0, "expected scale >= 0 but actual {}", scale);
        checkArgument((0 <= roundingMode) && (roundingMode <= 7), "expected roundingMode in [0, 7] but actual {}",
                roundingMode);
        return sqrt(euclideanDistancePow2(vector), scale, roundingMode);
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param vector
     *            The other {@link DecimalVector}
     * @param precision
     *            the precision for the termination condition
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != vector.size}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal, BigDecimal, int, int)
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector vector, final BigDecimal precision, final int scale,
            final int roundingMode) {
        requireNonNull(vector, "vector");
        checkArgument(map.size() == vector.size(), "expected equal sizes but actual %s != %s", map.size(),
                vector.size());
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual {}", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual {}", scale);
        checkArgument((0 <= roundingMode) && (roundingMode <= 7), "expected roundingMode in [0, 7] but actual {}",
                roundingMode);
        return sqrt(euclideanDistancePow2(vector), precision, scale, roundingMode);
    }

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns a {@link DecimalVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link DecimalVector}
     * @return A {@link DecimalVectorBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    public static DecimalVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new DecimalVectorBuilder(size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DecimalVector)) {
            return false;
        }
        final DecimalVector other = (DecimalVector) object;
        return Objects.deepEquals(map, other.getMap());
    }

    /**
     * The builder for {@link DecimalVector DecimalVectors}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    public static final class DecimalVectorBuilder
            extends VectorBuilder<BigDecimal, DecimalVector, DecimalVectorBuilder> {
        private DecimalVectorBuilder(final int size) {
            super(size);
        }

        /**
         * Puts the given entry on the first free index and returns {@code this}
         *
         * @param entry
         *            the entry
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code entry == null}
         * @throws ArithmeticException
         *             if ({@code map.size() + 1} overflows
         * @throws IllegalStateException
         *             if {@code map.size == size}
         * @since 1
         * @author Lars Tennstedt
         */
        public DecimalVectorBuilder put(final BigDecimal entry) {
            requireNonNull(entry, "entry");
            final int index = addExact(map.size(), 1);
            checkState(map.size() < size, "expected index in [1, %s] but actual %s", size, index);
            map.put(index, entry);
            return this;
        }

        /**
         * Puts the given entry on the given index and returns {@code this}
         *
         * @param index
         *            the index
         * @param entry
         *            the entry
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code index == null}
         * @throws NullPointerException
         *             if {@code entry == null}
         * @throws IllegalArgumentException
         *             if {@code index <= 0 || size < index}
         * @since 1
         * @author Lars Tennstedt
         */
        public DecimalVectorBuilder put(final Integer index, final BigDecimal entry) {
            requireNonNull(index, "index");
            requireNonNull(entry, "entry");
            checkArgument((0 < index) && (index <= size), "expected index in [1, %s] but actual %s", size, index);
            map.put(index, entry);
            return this;
        }

        /**
         * Puts the given entry on all indices and returns {@code this}
         *
         * @param entry
         *            the entry
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code entry == null}
         * @since 1
         * @author Lars Tennstedt
         */
        public DecimalVectorBuilder putAll(final BigDecimal entry) {
            requireNonNull(entry, "entry");
            for (int index = 1; index <= size; index++) {
                map.put(index, entry);
            }
            return this;
        }

        /**
         * Returns the built {@link DecimalVector}
         *
         * @return The {@link DecimalVector}
         * @throws NullPointerException
         *             if one {@code entry == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableMap#copyOf
         */
        @Override
        public DecimalVector build() {
            map.values().forEach(it -> {
                requireNonNull(it, "it");
            });
            return new DecimalVector(ImmutableMap.copyOf(map));
        }
    }
}
