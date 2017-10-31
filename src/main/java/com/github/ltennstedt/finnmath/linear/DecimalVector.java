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
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An immutable implementation of a vector which uses {@link BigDecimal} as type
 * for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class DecimalVector extends AbstractVector<BigDecimal, DecimalVector, BigDecimal> {
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
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public DecimalVector add(final DecimalVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "expected equal sizes but actual %s != %s", map.size(),
                        summand.size());
        final DecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> {
            builder.put(element.add(summand.element(index)));
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
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public DecimalVector subtract(final DecimalVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "expected equal sizes but actual %s != %s", map.size(),
                        subtrahend.size());
        final DecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> {
            builder.put(element.subtract(subtrahend.element(index)));
        });
        return builder.build();
    }

    /**
     * Returns the dot product of this {@link DecimalVector} and the given one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @return The dot product
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code map.size != other.size}
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public BigDecimal dotProduct(final DecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Integer index : map.keySet()) {
            result = result.add(map.get(index).multiply(other.element(index)));
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
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public DecimalVector scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final DecimalVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> {
            builder.put(scalar.multiply(element));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link DecimalVector} of this one
     *
     * @return The negated
     * @author Lars Tennstedt
     * @see #scalarMultiply
     * @since 1
     */
    @Override
    public DecimalVector negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * Returns the taxicab norm of this {@link DecimalVector}
     *
     * @return The taxicab norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    protected BigDecimal taxicabNorm() {
        BigDecimal norm = BigDecimal.ZERO;
        for (final BigDecimal element : map.values()) {
            norm = norm.add(element.abs());
        }
        return norm;
    }

    /**
     * Returns the taxicab distance from this {@link DecimalVector} to the given one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @return The taxicab distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @author Lars Tennstedt
     * @see #subtract
     * @see #taxicabNorm
     * @since 1
     */
    @Override
    protected BigDecimal taxicabDistance(final DecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean norm of this {@link DecimalVector}
     *
     * @return The square of the euclidean norm
     * @author Lars Tennstedt
     * @see #dotProduct
     * @since 1
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * Returns the euclidean norm of this {@link DecimalVector}
     *
     * @return The euclidean norm
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm() {
        return new SquareRootCalculator().sqrt(euclideanNormPow2());
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
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                        "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanNormPow2());
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
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final int scale, final RoundingMode roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanNormPow2());
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
     * @author Lars Tennstedt
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision, final int scale, final RoundingMode roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                        "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the square of the euclidean distance from this {@link DecimalVector}
     * to the given one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @author Lars Tennstedt
     * @see #subtract
     * @see #euclideanNormPow2
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistancePow2(final DecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).euclideanNormPow2();
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return new SquareRootCalculator().sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param other
     *            The other {@link DecimalVector}
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
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector other, final BigDecimal precision) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                        "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The deuclidean istance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector other, final int scale, final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link DecimalVector} to the given
     * one
     *
     * @param other
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
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @author Lars Tennstedt
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final DecimalVector other, final BigDecimal precision, final int scale,
                    final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                        "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the max norm of this {@link DecimalVector}
     *
     * @return The max norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    protected BigDecimal maxNorm() {
        BigDecimal norm = BigDecimal.ZERO;
        for (final BigDecimal element : map.values()) {
            norm = norm.max(element.abs());
        }
        return norm;
    }

    /**
     * Returns the max distance from this {@link DecimalVector} to the given one
     *
     * @param other
     *            The other {@link DecimalVector}
     * @return The max distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @author Lars Tennstedt
     * @see #subtract
     * @see #maxNorm
     * @since 1
     */
    @Override
    protected BigDecimal maxDistance(final DecimalVector other) {
        requireNonNull(other, "vector");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).maxNorm();
    }

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @author Lars Tennstedt
     * @since 1
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
     * @author Lars Tennstedt
     * @since 1
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
     * @author Lars Tennstedt
     * @since 1
     */
    @Beta
    public static final class DecimalVectorBuilder
                    extends AbstractVectorBuilder<BigDecimal, DecimalVector, DecimalVectorBuilder> {
        private DecimalVectorBuilder(final int size) {
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
         * @author Lars Tennstedt
         * @since 1
         */
        public DecimalVectorBuilder put(final BigDecimal element) {
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
         * @author Lars Tennstedt
         * @since 1
         */
        public DecimalVectorBuilder put(final Integer index, final BigDecimal element) {
            requireNonNull(index, "index");
            requireNonNull(element, "element");
            checkArgument((0 < index) && (index <= size), "expected index in [1, %s] but actual %s", size, index);
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
         * @author Lars Tennstedt
         * @since 1
         */
        public DecimalVectorBuilder putAll(final BigDecimal element) {
            requireNonNull(element, "element");
            for (int index = 1; index <= size; index++) {
                map.put(index, element);
            }
            return this;
        }

        /**
         * Returns the built {@link DecimalVector}
         *
         * @return The {@link DecimalVector}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @author Lars Tennstedt
         * @see ImmutableMap#copyOf
         * @since 1
         */
        @Override
        public DecimalVector build() {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(index -> {
                requireNonNull(map.get(index), "map.value");
            });
            return new DecimalVector(ImmutableMap.copyOf(map));
        }
    }
}
