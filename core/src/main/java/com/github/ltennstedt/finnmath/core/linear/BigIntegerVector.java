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

import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix.BigIntegerMatrixBuilder;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An immutable implementation of a vector which uses {@link BigInteger} as type
 * for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class BigIntegerVector
    extends AbstractVector<BigInteger, BigIntegerVector, BigIntegerMatrix, BigInteger, BigInteger> {
    private BigIntegerVector(final ImmutableMap<Integer, BigInteger> map) {
        super(map);
    }

    /**
     * Returns a {@link BigIntegerVectorBuilder}
     *
     * @param size
     *            size the resulting {@link BigIntegerVector}
     * @return {@link BigIntegerVectorBuilder}
     * @since 1
     */
    public static BigIntegerVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new BigIntegerVectorBuilder(size);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     * @since 1
     */
    @Override
    public BigIntegerVector add(final BigIntegerVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "equal sizes expected but actual %s != %s", map.size(),
            summand.size());
        final BigIntegerVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.add(summand.element(index))));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code size != subtrahend.size}
     * @since 1
     */
    @Override
    public BigIntegerVector subtract(final BigIntegerVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "equal sizes expected but actual %s != %s", map.size(),
            subtrahend.size());
        final BigIntegerVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.subtract(subtrahend.element(index))));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @since 1
     */
    @Override
    public BigIntegerVector scalarMultiply(final BigInteger scalar) {
        requireNonNull(scalar, "scalar");
        final BigIntegerVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigIntegerVector negate() {
        return scalarMultiply(BigInteger.ONE.negate());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public boolean orthogonalTo(final BigIntegerVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return dotProduct(other).compareTo(BigInteger.ZERO) == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigInteger taxicabNorm() {
        return map.values().stream().map(BigInteger::abs).reduce(BigInteger::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigInteger euclideanNormPow2() {
        return map.values().stream().map(value -> value.pow(2)).reduce(BigInteger::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     * @see SquareRootCalculator#sqrt(BigDecimal)
     */
    @Override
    public BigDecimal euclideanNorm() {
        return SquareRootCalculator.sqrt(euclideanNormPow2());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @since 1
     * @see SquareRootCalculator#sqrt(BigDecimal, SquareRootContext)
     */
    @Override
    public BigDecimal euclideanNorm(final SquareRootContext squareRootContext) {
        requireNonNull(squareRootContext, "squareRootContext");
        return SquareRootCalculator.sqrt(euclideanNormPow2(), squareRootContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public BigInteger dotProduct(final BigIntegerVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.keySet().stream().map(index -> map.get(index).multiply(other.element(index))).reduce(BigInteger::add)
            .get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigInteger maxNorm() {
        return map.values().stream().map(BigInteger::abs).reduce(BigInteger::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public BigIntegerMatrix dyadicProduct(final BigIntegerVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(map.size(), other.size());
        map.entrySet().forEach(entry -> other.entries().forEach(otherEntry -> builder.put(entry.getKey(),
            otherEntry.getKey(), entry.getValue().multiply(otherEntry.getValue()))));
        return builder.build();
    }

    /**
     * {@link AbstractVectorBuilder} for {@link BigIntegerVector BigIntegerVectors}
     *
     * @since 1
     */
    @Beta
    public static final class BigIntegerVectorBuilder
        extends AbstractVectorBuilder<BigInteger, BigIntegerVector, BigIntegerVectorBuilder> {
        private BigIntegerVectorBuilder(final Integer size) {
            super(size);
        }

        /**
         * {@inheritDoc}
         *
         * @throws NullPointerException
         *             if one {@code element == null}
         * @since 1
         */
        @Override
        public BigIntegerVector build() {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList())
                .forEach(index -> requireNonNull(map.get(index), "map.value"));
            return new BigIntegerVector(ImmutableMap.copyOf(map));
        }
    }
}
