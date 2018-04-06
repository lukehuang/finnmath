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

import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix.SimpleComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * An immutable implementation of a vector which uses
 * {@link SimpleComplexNumber} as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SimpleComplexNumberVector extends
    AbstractVector<SimpleComplexNumber, SimpleComplexNumberVector, SimpleComplexNumberMatrix, BigDecimal, BigInteger> {
    private SimpleComplexNumberVector(final ImmutableMap<Integer, SimpleComplexNumber> map) {
        super(map);
    }

    /**
     * Returns a {@link SimpleComplexNumberVectorBuilder}
     *
     * @param size
     *            size
     * @return {@link SimpleComplexNumberVectorBuilder}
     * @since 1
     */
    public static SimpleComplexNumberVectorBuilder builder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return new SimpleComplexNumberVectorBuilder(size);
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
    public SimpleComplexNumberVector add(final SimpleComplexNumberVector summand) {
        requireNonNull(summand, "summand");
        checkArgument(map.size() == summand.size(), "equal sizes expected but actual %s != %s", map.size(),
            summand.size());
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
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
    public SimpleComplexNumberVector subtract(final SimpleComplexNumberVector subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(map.size() == subtrahend.size(), "equal sizes expected but actual %s != %s", map.size(),
            subtrahend.size());
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
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
    public SimpleComplexNumberVector scalarMultiply(final SimpleComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final SimpleComplexNumberVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector negate() {
        return scalarMultiply(SimpleComplexNumber.ONE.negate());
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
    public boolean orthogonalTo(final SimpleComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return dotProduct(other).equals(SimpleComplexNumber.ZERO);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigInteger euclideanNormPow2() {
        return map.values().stream().map(SimpleComplexNumber::absPow2).reduce(BigInteger::add).get();
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
    public SimpleComplexNumber dotProduct(final SimpleComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(SimpleComplexNumber::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigDecimal maxNorm() {
        return map.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::max).get();
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
    public SimpleComplexNumberMatrix dyadicProduct(final SimpleComplexNumberVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(map.size(), other.size());
        map.entrySet().forEach(entry -> other.entries().forEach(otherEntry -> builder.put(entry.getKey(),
            otherEntry.getKey(), entry.getValue().multiply(otherEntry.getValue()))));
        return builder.build();
    }

    /**
     * {@link AbstractVectorBuilder} for {@link SimpleComplexNumberVector
     * SimpleComplexNumberVectors}
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
         * {@inheritDoc}
         *
         * @throws NullPointerException
         *             if one {@code element == null}
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
