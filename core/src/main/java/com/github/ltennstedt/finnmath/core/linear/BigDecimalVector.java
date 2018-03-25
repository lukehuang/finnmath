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

import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
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
public final class BigDecimalVector extends AbstractVector<BigDecimal, BigDecimalVector, BigDecimal, BigDecimal> {
    private BigDecimalVector(final ImmutableMap<Integer, BigDecimal> map) {
        super(map);
    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public BigDecimal dotProduct(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalVector scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalVector negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add).get();
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
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal maxNorm() {
        return map.values().stream().map(BigDecimal::abs).reduce(BigDecimal::max).get();
    }

    /**
     * Returns a {@code boolean} which indicates if the elements of the
     * {@link BigDecimalVector BigDecimalVectors} are equal by the
     * {@link BigDecimal#compareTo(BigDecimal) compareTo} method
     *
     * @param other
     *            The other vector
     * @return true if {@code compareTo == 0} for all elements, false otherwise
     */
    public boolean equalByComparingTo(final BigDecimalVector other) {
        return !map.entrySet().stream()
            .anyMatch(entry -> entry.getValue().compareTo(other.element(entry.getKey())) != 0);
    }

    /**
     * Returns a {@link BigDecimalVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link BigDecimalVector}
     * @return A {@link BigDecimalVectorBuilder}
     * @since 1
     */
    public static BigDecimalVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new BigDecimalVectorBuilder(size);
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
         * Returns the built {@link BigDecimalVector}
         *
         * @return The {@link BigDecimalVector}
         * @throws NullPointerException
         *             if one {@code element == null}
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
