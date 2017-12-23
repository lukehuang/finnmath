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

package com.github.ltennstedt.finnmath.core.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.Math.addExact;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An immutable implementation of a vector which uses {@link BigDecimal} as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class BigDecimalVector extends AbstractVector<BigDecimal, BigDecimalVector, BigDecimal, BigDecimal> {
    private BigDecimalVector(final ImmutableMap<Integer, BigDecimal> map) {
        super(map);
    }

    /**
     * Returns the sum of this {@link BigDecimalVector} and the given one
     *
     * @param summand
     *         the summand
     * @return The sum
     * @throws NullPointerException
     *         if {@code summand == null}
     * @since 1
     */
    @Override
    public BigDecimalVector add(final BigDecimalVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "expected equal sizes but actual %s != %s", map.size(),
                summand.size());
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.add(summand.element(index))));
        return builder.build();
    }

    /**
     * Returns the difference of this {@link BigDecimalVector} and the given one
     *
     * @param subtrahend
     *         the subtrahend
     * @return The difference
     * @throws NullPointerException
     *         if {@code subtrahend == null}
     * @since 1
     */
    @Override
    public BigDecimalVector subtract(final BigDecimalVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "expected equal sizes but actual %s != %s", map.size(),
                subtrahend.size());
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.subtract(subtrahend.element(index))));
        return builder.build();
    }

    /**
     * Returns the dot product of this {@link BigDecimalVector} and the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @return The dot product
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code map.size != other.size}
     * @since 1
     */
    @Override
    public BigDecimal dotProduct(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Integer index : map.keySet()) {
            result = result.add(map.get(index).multiply(other.element(index)));
        }
        return result;
    }

    /**
     * Returns the scalar product of the given scalar and this {@link BigDecimalVector}
     *
     * @param scalar
     *         the scalar
     * @return The scalar product
     * @throws NullPointerException
     *         if {@code summand == null}
     * @since 1
     */
    @Override
    public BigDecimalVector scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * Returns the negated {@link BigDecimalVector} of this one
     *
     * @return The negated
     * @see #scalarMultiply
     * @since 1
     */
    @Override
    public BigDecimalVector negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * Returns the taxicab norm of this {@link BigDecimalVector}
     *
     * @return The taxicab norm
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
     * Returns the taxicab distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @return The taxicab distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @see #subtract
     * @see #taxicabNorm
     * @since 1
     */
    @Override
    protected BigDecimal taxicabDistance(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).taxicabNorm();
    }

    /**
     * Returns the square of the euclidean norm of this {@link BigDecimalVector}
     *
     * @return The square of the euclidean norm
     * @see #dotProduct
     * @since 1
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @return The euclidean norm
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm() {
        return new SquareRootCalculator().sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param precision
     *         the precision for the termination condition
     * @return The euclidean norm
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
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
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
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
     * Returns the euclidean norm of this {@link BigDecimalVector}
     *
     * @param precision
     *         the precision for the termination condition
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean norm
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
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
     * Returns the square of the euclidean distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @return The square of the euclidean distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @see #subtract
     * @see #euclideanNormPow2
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistancePow2(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).euclideanNormPow2();
    }

    /**
     * Returns the euclidean distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @return The euclidean distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return new SquareRootCalculator().sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @param precision
     *         the precision for the termination condition
     * @return The euclidean distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final BigDecimalVector other, final BigDecimal precision) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The deuclidean istance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final BigDecimalVector other, final int scale,
            final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @param precision
     *         the precision for the termination condition
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The euclidean distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigDecimal)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final BigDecimalVector other, final BigDecimal precision, final int scale,
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
     * Returns the max norm of this {@link BigDecimalVector}
     *
     * @return The max norm
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
     * Returns the max distance from this {@link BigDecimalVector} to the given one
     *
     * @param other
     *         The other {@link BigDecimalVector}
     * @return The max distance
     * @throws NullPointerException
     *         if {@code other == null}
     * @throws IllegalArgumentException
     *         if {@code size != other.size}
     * @see #subtract
     * @see #maxNorm
     * @since 1
     */
    @Override
    protected BigDecimal maxDistance(final BigDecimalVector other) {
        requireNonNull(other, "vector");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return subtract(other).maxNorm();
    }

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @since 1
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Returns a {@code boolean} which indicates if the elements of the {@link BigDecimalVector BigDecimalVectors} are
     * equal by the {@link BigDecimal#compareTo(BigDecimal) compareTo} method
     *
     * @param other
     *         The other vector
     * @return true if {@code compareTo == 0} for all elements, false otherwise
     */
    public boolean equalByComparingTo(final BigDecimalVector other) {
        for (final Entry<Integer, BigDecimal> entry : map.entrySet()) {
            if (entry.getValue().compareTo(other.element(entry.getKey())) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a {@link BigDecimalVectorBuilder}
     *
     * @param size
     *         the size the resulting {@link BigDecimalVector}
     * @return A {@link BigDecimalVectorBuilder}
     * @since 1
     */
    public static BigDecimalVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new BigDecimalVectorBuilder(size);
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
        if (!(object instanceof BigDecimalVector)) {
            return false;
        }
        final BigDecimalVector other = (BigDecimalVector) object;
        return Objects.deepEquals(map, other.getMap());
    }

    /**
     * The builder for {@link BigDecimalVector BigDecimalVectors}
     *
     * @since 1
     */
    @Beta
    public static final class BigDecimalVectorBuilder
            extends AbstractVectorBuilder<BigDecimal, BigDecimalVector, BigDecimalVectorBuilder> {
        private BigDecimalVectorBuilder(final int size) {
            super(size);
        }

        /**
         * Puts the given element on the first free index and returns {@code this}
         *
         * @param element
         *         the element
         * @return {@code this}
         * @throws NullPointerException
         *         if {@code element == null}
         * @throws ArithmeticException
         *         if ({@code size + 1} overflows
         * @throws IllegalStateException
         *         if {@code size == size}
         * @since 1
         */
        public BigDecimalVectorBuilder put(final BigDecimal element) {
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
         *         the index
         * @param element
         *         the element
         * @return {@code this}
         * @throws NullPointerException
         *         if {@code index == null}
         * @throws NullPointerException
         *         if {@code element == null}
         * @throws IllegalArgumentException
         *         if {@code index <= 0 || size < index}
         * @since 1
         */
        public BigDecimalVectorBuilder put(final Integer index, final BigDecimal element) {
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
         *         the element
         * @return {@code this}
         * @throws NullPointerException
         *         if {@code element == null}
         * @since 1
         */
        public BigDecimalVectorBuilder putAll(final BigDecimal element) {
            requireNonNull(element, "element");
            for (int index = 1; index <= size; index++) {
                map.put(index, element);
            }
            return this;
        }

        /**
         * Returns the built {@link BigDecimalVector}
         *
         * @return The {@link BigDecimalVector}
         * @throws NullPointerException
         *         if one {@code element == null}
         * @see ImmutableMap#copyOf
         * @since 1
         */
        @Override
        public BigDecimalVector build() {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList())
                    .forEach(index -> requireNonNull(map.get(index), "map.value"));
            return new BigDecimalVector(ImmutableMap.copyOf(map));
        }
    }
}