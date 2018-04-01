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

import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An immutable implementation of a vector which uses {@link RealComplexNumber}
 * as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class RealComplexNumberVector
    extends AbstractVector<RealComplexNumber, RealComplexNumberVector, BigDecimal, BigDecimal> {
    private RealComplexNumberVector(final ImmutableMap<Integer, RealComplexNumber> map) {
        super(map);
    }

    /**
     * Returns a {@link RealComplexNumberVectorBuilder}
     *
     * @param size
     *            the size the resulting {@link RealComplexNumberVector}
     * @return A {@link RealComplexNumberVectorBuilder}
     * @since 1
     */
    public static RealComplexNumberVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new RealComplexNumberVectorBuilder(size);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     */
    @Override
    public RealComplexNumberVector add(final RealComplexNumberVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "equal sizes expected but actual %s != %s", map.size(),
            summand.size());
        final RealComplexNumberVectorBuilder builder = builder(map.size());
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
    public RealComplexNumberVector subtract(final RealComplexNumberVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "equal sizes expected but actual %s != %s", map.size(),
            subtrahend.size());
        final RealComplexNumberVectorBuilder builder = builder(map.size());
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
    public RealComplexNumberVector scalarMultiply(final RealComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final RealComplexNumberVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public RealComplexNumberVector negate() {
        return scalarMultiply(RealComplexNumber.ONE.negate());
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return map.values().stream().map(RealComplexNumber::absPow2).reduce(BigDecimal::add).get();
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
    public RealComplexNumber dotProduct(final RealComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(RealComplexNumber::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigDecimal maxNorm() {
        return map.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::max).get();
    }

    /**
     * {@link AbstractVectorBuilder} for {@link RealComplexNumberVector
     * RealComplexNumberVectors}
     *
     * @since 1
     */
    @Beta
    public static final class RealComplexNumberVectorBuilder
        extends AbstractVectorBuilder<RealComplexNumber, RealComplexNumberVector, RealComplexNumberVectorBuilder> {
        private RealComplexNumberVectorBuilder(final Integer size) {
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
        public RealComplexNumberVector build() {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList())
                .forEach(index -> requireNonNull(map.get(index), "map.value"));
            return new RealComplexNumberVector(ImmutableMap.copyOf(map));
        }
    }
}
