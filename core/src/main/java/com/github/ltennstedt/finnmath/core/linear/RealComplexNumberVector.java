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

import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
     * {@inheritDoc}
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
     */
    @Override
    public RealComplexNumberVector negate() {
        return scalarMultiply(RealComplexNumber.ONE.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return map.values().stream().map(RealComplexNumber::absPow2).reduce(BigDecimal::add).get();
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
    public RealComplexNumber dotProduct(final RealComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(RealComplexNumber::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal maxNorm() {
        return map.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::max).get();
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
     * The builder for {@link RealComplexNumberVector RealComplexNumberVectors}
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
         * Returns the built {@link RealComplexNumberVector}
         *
         * @return The {@link RealComplexNumberVector}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @see ImmutableMap#copyOf
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
