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

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.math.MathContext;
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
public final class BigDecimalVector
    extends AbstractContextVector<BigDecimal, BigDecimalVector, BigDecimalMatrix, BigDecimal, BigDecimal, MathContext> {
    private BigDecimalVector(final ImmutableMap<Integer, BigDecimal> map) {
        super(map);
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != summand.size}
     * @since 1
     */
    @Override
    public BigDecimalVector add(final BigDecimalVector summand, final MathContext mathContext) {
        requireNonNull(summand, "summand");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == summand.size(), "expected equal sizes but actual %s != %s", map.size(),
            summand.size());
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.add(summand.element(index), mathContext)));
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
     *
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != subtrahend.size}
     * @since 1
     */
    @Override
    public BigDecimalVector subtract(final BigDecimalVector subtrahend, final MathContext mathContext) {
        requireNonNull(subtrahend, "subtrahend");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == subtrahend.size(), "expected equal sizes but actual %s != %s", map.size(),
            subtrahend.size());
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.forEach((index, element) -> builder.put(element.subtract(subtrahend.element(index), mathContext)));
        return builder.build();
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
    public BigDecimal dotProduct(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
            .reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public BigDecimal dotProduct(final BigDecimalVector other, final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return map.entrySet().stream()
            .map(entry -> entry.getValue().multiply(other.element(entry.getKey()), mathContext))
            .reduce((element, otherElement) -> element.add(otherElement, mathContext)).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimalVector scalarMultiply(final BigDecimal scalar, final MathContext mathContext) {
        requireNonNull(scalar, "scalar");
        requireNonNull(mathContext, "mathContext");
        final BigDecimalVectorBuilder builder = builder(map.size());
        map.values().forEach(element -> builder.put(scalar.multiply(element, mathContext)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigDecimalVector negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimalVector negate(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return scalarMultiply(BigDecimal.ONE.negate(mathContext), mathContext);
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
    public boolean orthogonalTo(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return dotProduct(other).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public boolean orthogonalTo(final BigDecimalVector other, final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        return dotProduct(other, mathContext).compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected BigDecimal taxicabNorm() {
        return map.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimal taxicabNorm(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return map.values().stream().map(element -> element.abs(mathContext))
            .reduce((element, other) -> element.add(other, mathContext)).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigDecimal euclideanNormPow2() {
        return dotProduct(this);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimal euclideanNormPow2(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return dotProduct(this, mathContext);
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
     * @since 1
     */
    @Override
    public BigDecimal maxNorm() {
        return map.values().stream().map(BigDecimal::abs).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimal maxNorm(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return map.values().stream().map(element -> element.abs(mathContext)).reduce(BigDecimal::max).get();
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
    public BigDecimalMatrix dyadicProduct(final BigDecimalVector other) {
        requireNonNull(other, "other");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(map.size(), other.size());
        map.entrySet().forEach(entry -> other.entries().forEach(otherEntry -> builder.put(entry.getKey(),
            otherEntry.getKey(), entry.getValue().multiply(otherEntry.getValue()))));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code size != other.size}
     * @since 1
     */
    @Override
    public BigDecimalMatrix dyadicProduct(final BigDecimalVector other, final MathContext mathContext) {
        requireNonNull(other, "other");
        requireNonNull(mathContext, "mathContext");
        checkArgument(map.size() == other.size(), "expected equal sizes but actual %s != %s", map.size(), other.size());
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(map.size(), other.size());
        map.entrySet().forEach(entry -> other.entries().forEach(otherEntry -> builder.put(entry.getKey(),
            otherEntry.getKey(), entry.getValue().multiply(otherEntry.getValue(), mathContext))));
        return builder.build();
    }

    /**
     * Returns a {@code boolean} which indicates if the elements of the
     * {@link BigDecimalVector BigDecimalVectors} are equal by the
     * {@link BigDecimal#compareTo(BigDecimal) compareTo} method
     *
     * @param other
     *            The other vector
     * @return true if {@code compareTo == 0} for all elements, false otherwise
     * @since 1
     */
    public boolean equalByComparingTo(final BigDecimalVector other) {
        return !map.entrySet().stream()
            .anyMatch(entry -> entry.getValue().compareTo(other.element(entry.getKey())) != 0);
    }

    /**
     * {@link AbstractVectorBuilder} for {@link BigDecimalVector BigDecimalVectors}
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
