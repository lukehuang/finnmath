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
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
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
public final class BigIntegerVector extends AbstractVector<BigInteger, BigIntegerVector, BigInteger, BigInteger> {
    private BigIntegerVector(final ImmutableMap<Integer, BigInteger> map) {
        super(map);
    }

    /**
     * {@inheritDoc}
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
     */
    @Override
    public BigIntegerVector negate() {
        return scalarMultiply(BigInteger.ONE.negate());
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public BigInteger euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal abortCriterion, final RoundingMode roundingMode) {
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(roundingMode, "roundingMode");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return SquareRootCalculator.sqrt(euclideanNormPow2(), abortCriterion, roundingMode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal euclideanNorm(final BigDecimal abortCriterion, final MathContext mathContext) {
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(mathContext, "mathContext");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return SquareRootCalculator.sqrt(euclideanNormPow2(), abortCriterion, mathContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger dotProduct(final BigIntegerVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        BigInteger result = BigInteger.ZERO;
        for (final Integer index : map.keySet()) {
            result = result.add(map.get(index).multiply(other.element(index)));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigInteger maxNorm() {
        return map.values().stream().map(BigInteger::abs).reduce(BigInteger::max).get();
    }

    /**
     * Returns a {@link BigIntegerVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link BigIntegerVector}
     * @return A {@link BigIntegerVectorBuilder}
     * @since 1
     */
    public static BigIntegerVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new BigIntegerVectorBuilder(size);
    }

    /**
     * The builder for {@link BigIntegerVector BigIntegerVectors}
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
         * Returns the built {@link BigIntegerVector}
         *
         * @return The {@link BigIntegerVector}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @see ImmutableMap#copyOf
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
