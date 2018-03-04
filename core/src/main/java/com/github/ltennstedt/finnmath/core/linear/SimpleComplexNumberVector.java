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
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * An immutable implementation of a vector which uses
 * {@link SimpleComplexNumber} as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SimpleComplexNumberVector
    extends AbstractVector<SimpleComplexNumber, SimpleComplexNumberVector, BigDecimal, BigInteger> {
    private SimpleComplexNumberVector(final ImmutableMap<Integer, SimpleComplexNumber> map) {
        super(map);
    }

    /**
     * Returns the sum of this {@link SimpleComplexNumberVector} and the given one
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector add(final SimpleComplexNumberVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "equal sizes expected but actual %s != %s", map.size(),
            summand.size());
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.add(summand.element(index))));
        return builder.build();
    }

    /**
     * Returns the difference of this {@link SimpleComplexNumberVector} and the
     * given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code size != subtrahend.size}
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector subtract(final SimpleComplexNumberVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "equal sizes expected but actual %s != %s", map.size(),
            subtrahend.size());
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.subtract(subtrahend.element(index))));
        return builder.build();
    }

    /**
     * Returns the scalar product of the given scalar and this
     * {@link SimpleComplexNumberVector}
     *
     * @param scalar
     *            the scalar
     * @return The scalar product
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector scalarMultiply(final SimpleComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * Returns the negated {@link SimpleComplexNumberVector} of this one
     *
     * @return The negated
     * @see #scalarMultiply
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector negate() {
        return scalarMultiply(SimpleComplexNumber.ONE.negate());
    }

    /**
     * Returns the taxicab norm of this {@link SimpleComplexNumberVector}
     *
     * @return The taxicab norm
     * @since 1
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add).get();
    }

    /**
     * Returns the square of the euclidean norm of this
     * {@link SimpleComplexNumberVector}
     *
     * @return The square of the euclidean norm
     * @since 1
     */
    @Override
    public BigInteger euclideanNormPow2() {
        return map.values().stream().map(SimpleComplexNumber::absPow2).reduce(BigInteger::add).get();
    }

    /**
     * Returns the euclidean norm of this {@link SimpleComplexNumberVector}
     *
     * @return The euclidean norm
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm() {
        return new SquareRootCalculator().sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link SimpleComplexNumberVector}
     *
     * @param precision
     *            the precision for the termination condition
     * @return The euclidean norm
     * @throws NullPointerException
     *             if {@code precision == null}
     * @throws IllegalArgumentException
     *             if {@code precision <= 0 || 1 <= precision}
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument(BigDecimal.ZERO.compareTo(precision) < 0 && precision.compareTo(BigDecimal.ONE) < 0,
            "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link SimpleComplexNumberVector}
     *
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The euclidean norm
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final int scale, final RoundingMode roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the euclidean norm of this {@link SimpleComplexNumberVector}
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
     * @see #euclideanNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal precision, final int scale, final RoundingMode roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument(BigDecimal.ZERO.compareTo(precision) < 0 && precision.compareTo(BigDecimal.ONE) < 0,
            "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(euclideanNormPow2());
    }

    /**
     * Returns the dot product of this {@link SimpleComplexNumberVector} and the
     * given one
     *
     * @param other
     *            The other {@link SimpleComplexNumberVector}
     * @return The dot product
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public SimpleComplexNumber dotProduct(final SimpleComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(SimpleComplexNumber::add).get();
    }

    /**
     * Returns the euclidean distance from this {@link SimpleComplexNumberVector} to
     * the given one
     *
     * @param other
     *            The other {@link SimpleComplexNumberVector}
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final SimpleComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return new SquareRootCalculator().sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link SimpleComplexNumberVector} to
     * the given one
     *
     * @param other
     *            The other {@link SimpleComplexNumberVector}
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
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final SimpleComplexNumberVector other, final BigDecimal precision) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument(BigDecimal.ZERO.compareTo(precision) < 0 && precision.compareTo(BigDecimal.ONE) < 0,
            "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link SimpleComplexNumberVector} to
     * the given one
     *
     * @param other
     *            The other {@link SimpleComplexNumberVector}
     * @param scale
     *            the scale to be set on the result
     * @param roundingMode
     *            the rounding mode to be used during the setting of the scale of
     *            the result
     * @return The euclidean distance
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final SimpleComplexNumberVector other, final int scale,
        final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the euclidean distance from this {@link SimpleComplexNumberVector} to
     * the given one
     *
     * @param other
     *            The other {@link SimpleComplexNumberVector}
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
     * @see #euclideanDistancePow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal euclideanDistance(final SimpleComplexNumberVector other, final BigDecimal precision,
        final int scale, final RoundingMode roundingMode) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        requireNonNull(precision, "precision");
        checkArgument(BigDecimal.ZERO.compareTo(precision) < 0 && precision.compareTo(BigDecimal.ONE) < 0,
            "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(euclideanDistancePow2(other));
    }

    /**
     * Returns the max norm of this {@link SimpleComplexNumberVector}
     *
     * @return The max norm
     * @since 1
     */
    @Override
    protected BigDecimal maxNorm() {
        return map.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::max).get();
    }

    /**
     * Returns a {@link SimpleComplexNumberVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link SimpleComplexNumberVector}
     * @return A {@link SimpleComplexNumberVectorBuilder}
     * @since 1
     */
    public static SimpleComplexNumberVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new SimpleComplexNumberVectorBuilder(size);
    }

    /**
     * The builder for {@link SimpleComplexNumberVector SimpleComplexNumberVectors}
     *
     * @since 1
     */
    @Beta
    public static final class SimpleComplexNumberVectorBuilder extends
        AbstractVectorBuilder<SimpleComplexNumber, SimpleComplexNumberVector, SimpleComplexNumberVectorBuilder> {
        private SimpleComplexNumberVectorBuilder(final Integer size) {
            super(size);
        }

        /**
         * Returns the built {@link SimpleComplexNumberVector}
         *
         * @return The {@link SimpleComplexNumberVector}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @see ImmutableMap#copyOf
         * @since 1
         */
        @Override
        public SimpleComplexNumberVector build() {
            if (map.values().stream().anyMatch(Objects::isNull)) {
                throw new NullPointerException("element");
            }
            return new SimpleComplexNumberVector(ImmutableMap.copyOf(map));
        }
    }
}
