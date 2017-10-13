/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ltennstedt.finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.addExact;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An immutable implementation of a vector which uses {@link BigInteger} as type for its elements
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class BigIntVector extends AbstractVector<BigInteger, BigIntVector, BigDecimal> {
    private BigIntVector(final ImmutableMap<Integer, BigInteger> map) {
        super(map);
    }

    /**
     * Returns the sum of this {@link BigIntVector} and the given one
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigIntVector add(final BigIntVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "equal sizes expected but actual %s != %s",
                map.size(), summand.size());
        final BigIntVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> {
            builder.put(element.add(summand.element(index)));
        });
        return builder.build();
    }

    /**
     * Returns the difference of this {@link BigIntVector} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code size != subtrahend.size}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigIntVector subtract(final BigIntVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "equal sizes expected but actual %s != %s",
                map.size(), subtrahend.size());
        final BigIntVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> {
            builder.put(element.subtract(subtrahend.element(index)));
        });
        return builder.build();
    }

    /**
     * Returns the scalar product of the given scalar and this {@link BigIntVector}
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
    public BigIntVector scalarMultiply(final BigInteger scalar) {
        requireNonNull(scalar, "scalar");
        final BigIntVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> {
            builder.put(scalar.multiply(element));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link BigIntVector} of this one
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    @Override
    public BigIntVector negate() {
        return scalarMultiply(BigInteger.ONE.negate());
    }

    /**
     * Returns the taxicab norm of this {@link BigIntVector}
     *
     * @return The taxicab norm
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    protected BigInteger taxicabNorm() {
        BigInteger norm = BigInteger.ZERO;
        for (final BigInteger element : map.values()) {
            norm = norm.add(element.abs());
        }
        return norm;
    }

    /**
     * Returns the taxicab distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @return The taxicab distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #taxicabNorm
     */
    @Override
    protected BigInteger taxicabDistance(final BigIntVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        return subtract(other).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean norm of this {@link BigIntVector}
     *
     * @return The square of the euclidean norm
     * @since 1
     * @author Lars Tennstedt
     * @see #dotProduct
     */
    @Override
    public BigInteger euclideanNormPow2() {
        return dotProduct(this);
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
    @Override
    public BigDecimal euclideanNorm() {
        return new SquareRootCalculator().sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
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
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument(
                (BigDecimal.ZERO.compareTo(precision) < 0)
                        && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     *
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanNorm(final int scale, final RoundingMode roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link BigIntVector}
     *
     * @param precision
     *            the precision for the termination condition
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision, final int scale,
            final RoundingMode roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument(
                (BigDecimal.ZERO.compareTo(precision) < 0)
                        && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the dot product of this {@link BigIntVector} and the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @return The dot product
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigInteger dotProduct(final BigIntVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        BigInteger result = BigInteger.ZERO;
        for (final Integer index : map.keySet()) {
            result = result.add(map.get(index).multiply(other.element(index)));
        }
        return result;
    }

    /**
     * Returns the square of the euclidean distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #euclideanNormPow2
     */
    @Override
    public BigInteger euclideanDistancePow2(final BigIntVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        return subtract(other).euclideanNormPow2();
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigDecimal euclideanDistance(final BigIntVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        return new SquareRootCalculator().sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @param precision
     *            the precision for the termination condition
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanDistance(final BigIntVector other, final BigDecimal precision) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument(
                (BigDecimal.ZERO.compareTo(precision) < 0)
                        && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanDistance(final BigIntVector other, final int scale,
            final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @param precision
     *            the precision for the termination condition
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     */
    @Override
    public BigDecimal euclideanDistance(final BigIntVector other, final BigDecimal precision,
            final int scale, final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument(
                (BigDecimal.ZERO.compareTo(precision) < 0)
                        && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode)
                .sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the max norm of this {@link BigIntVector}
     *
     * @return The max norm
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    protected BigInteger maxNorm() {
        BigInteger norm = BigInteger.ZERO;
        for (final BigInteger element : map.values()) {
            norm = norm.max(element.abs());
        }
        return norm;
    }

    /**
     * Returns the max distance from this {@link BigIntVector} to the given one
     *
     * @param other
     *            The other {@link BigIntVector}
     * @return The max distance
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     * @author Lars Tennstedt
     * @see #subtract
     * @see #maxNorm
     */
    @Override
    protected BigInteger maxDistance(final BigIntVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s",
                map.size(), other.size());
        return subtract(other).maxNorm();
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
     * Returns a {@link BigIntVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link BigIntVector}
     * @return A {@link BigIntVectorBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    public static BigIntVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new BigIntVectorBuilder(size);
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
        if (!(object instanceof BigIntVector)) {
            return false;
        }
        final BigIntVector other = (BigIntVector) object;
        return Objects.deepEquals(map, other.getMap());
    }

    /**
     * The builder for {@link BigIntVector BigIntVectors}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    public static final class BigIntVectorBuilder
            extends AbstractVectorBuilder<BigInteger, BigIntVector, BigIntVectorBuilder> {
        private BigIntVectorBuilder(final Integer size) {
            super(size);
        }

        /**
         * Puts the given element on the first free index and returns {@code this}
         *
         * @param element
         *            the element
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code element == null}
         * @throws ArithmeticException
         *             if ({@code size + 1} overflows
         * @throws IllegalStateException
         *             if {@code size == size}
         * @since 1
         * @author Lars Tennstedt
         */
        public BigIntVectorBuilder put(final BigInteger element) {
            requireNonNull(element, "element");
            final int index = addExact(map.size(), 1);
            checkState(map.size() < size, "expected index in [1, %s] but actual %s", size, index);
            map.put(index, element);
            return this;
        }

        /**
         * Puts the given element on the given index and returns {@code this}
         *
         * @param index
         *            the index
         * @param element
         *            the element
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code index == null}
         * @throws NullPointerException
         *             if {@code element == null}
         * @throws IllegalArgumentException
         *             if {@code index <= 0 || size < index}
         * @since 1
         * @author Lars Tennstedt
         */
        public BigIntVectorBuilder put(final Integer index, final BigInteger element) {
            requireNonNull(index, "index");
            requireNonNull(element, "element");
            checkArgument((0 < index) && (index <= size), "expected index in [1, %s] but actual %s",
                    size, index);
            map.put(index, element);
            return this;
        }

        /**
         * Puts the given element on all indices and returns {@code this}
         *
         * @param element
         *            the element
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code element == null}
         * @since 1
         * @author Lars Tennstedt
         */
        public BigIntVectorBuilder putAll(final BigInteger element) {
            requireNonNull(element, "element");
            for (int index = 1; index <= size; index++) {
                map.put(index, element);
            }
            return this;
        }

        /**
         * Returns the built {@link BigIntVector}
         *
         * @return The {@link BigIntVector}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableMap#copyOf
         */
        @Override
        public BigIntVector build() {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(index -> {
                requireNonNull(map.get(index), "map.value");
            });
            return new BigIntVector(ImmutableMap.copyOf(map));
        }
    }
}
