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

package com.github.ltennstedt.finnmath.core.number;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigInteger;
import java.util.Objects;

/**
 * An immutable implementation of a fraction which uses {@link BigInteger} as
 * type for its numerator and denominator
 * <p>
 * The returned {@link Fraction Fractions} of most methods are neither
 * normalized nor reduced
 *
 * @see #normalize()
 * @see #reduce()
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class Fraction implements MathNumber<Fraction, Fraction, Fraction>, Comparable<Fraction> {
    /**
     * {@code 0} as {@link Fraction}
     *
     * @since 1
     */
    public static final Fraction ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE);

    /**
     * {@code 1} as {@link Fraction}
     *
     * @since 1
     */
    public static final Fraction ONE = new Fraction(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    private Fraction(final BigInteger numerator, final BigInteger denominator) {
        assert numerator != null;
        assert denominator != null;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Returns a {@link Fraction} based on the given {@code numerator} and
     * {@code denominator}
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param numerator
     *            the numerator
     * @param denominator
     *            the denominator
     * @return {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code denominator == 0}
     * @since 1
     */
    public static Fraction of(final long numerator, final long denominator) {
        checkArgument(denominator != 0L, "expected denominator != 0 but actual %s", denominator);
        return of(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * Returns a {@link Fraction} based on the given {@code numerator} and
     * {@code denominator}
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param numerator
     *            the numerator
     * @param denominator
     *            the denominator
     * @return {@link Fraction}
     * @throws NullPointerException
     *             if {@code numerator == null}
     * @throws NullPointerException
     *             if {@code denominator == null}
     * @throws IllegalArgumentException
     *             if {@code denominator == 0}
     * @since 1
     */
    public static Fraction of(final BigInteger numerator, final BigInteger denominator) {
        requireNonNull(numerator, "numerator");
        requireNonNull(denominator, "denominator");
        checkArgument(denominator.compareTo(BigInteger.ZERO) != 0, "expected denominator != 0 but actual %s",
            denominator);
        return new Fraction(numerator, denominator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     */
    @Override
    public Fraction add(final Fraction summand) {
        requireNonNull(summand, "summand");
        final BigInteger newNumerator =
            summand.getDenominator().multiply(numerator).add(denominator.multiply(summand.getNumerator()));
        final BigInteger newDenominator = denominator.multiply(summand.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     */
    @Override
    public Fraction subtract(final Fraction subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        final BigInteger newNumerator =
            subtrahend.getDenominator().multiply(numerator).subtract(denominator.multiply(subtrahend.getNumerator()));
        final BigInteger newDenominator = denominator.multiply(subtrahend.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code factor == null}
     */
    @Override
    public Fraction multiply(final Fraction factor) {
        requireNonNull(factor, "factor");
        final BigInteger newNumerator = numerator.multiply(factor.getNumerator());
        final BigInteger newDenominator = denominator.multiply(factor.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code !divisor.invertible()}
     */
    @Override
    public Fraction divide(final Fraction divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return multiply(divisor.invert());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fraction pow(final int exponent) {
        checkArgument(exponent > -1, "expected exponent > -1 but actual %s", exponent);
        if (exponent > 1) {
            return multiply(pow(exponent - 1));
        } else if (exponent == 1) {
            return this;
        }
        return ONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fraction negate() {
        return new Fraction(numerator.negate(), denominator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fraction invert() {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        return new Fraction(denominator, numerator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean invertible() {
        return numerator.compareTo(BigInteger.ZERO) != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fraction abs() {
        return new Fraction(numerator.abs(), denominator.abs());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    @Override
    public boolean equalsByComparingFields(final Fraction other) {
        requireNonNull(other, "other");
        return numerator.compareTo(other.getNumerator()) == 0 && denominator.compareTo(other.getDenominator()) == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code other == null}
     */
    @Override
    public int compareTo(final Fraction other) {
        requireNonNull(other, "other");
        if (lessThan(other)) {
            return -1;
        }
        if (greaterThan(other)) {
            return 1;
        }
        return 0;
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean}
     * which indicates which one is less
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code true} if {@code this <= other}, {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public boolean lessThanOrEqualTo(final Fraction other) {
        requireNonNull(other, "other");
        final Fraction normalized = normalize();
        final Fraction normalizedOther = other.normalize();
        final BigInteger left = normalizedOther.getDenominator().multiply(normalized.getNumerator());
        final BigInteger right = normalized.getDenominator().multiply(normalizedOther.getNumerator());
        return left.compareTo(right) < 1;
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean}
     * which indicates which one is greater
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code true} if {@code this >= other}, {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public boolean greaterThanOrEqualTo(final Fraction other) {
        requireNonNull(other, "other");
        return !lessThanOrEqualTo(other) || equivalent(other);
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean}
     * which indicates which one is less
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code true} if {@code this < other}, {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public boolean lessThan(final Fraction other) {
        requireNonNull(other, "other");
        return !greaterThanOrEqualTo(other);
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean}
     * which indicates which one is greater
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code true} if {@code this < other}, {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public boolean greaterThan(final Fraction other) {
        requireNonNull(other, "other");
        return !lessThanOrEqualTo(other);
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the minimum
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param other
     *            another {@link Fraction}
     * @return minimum
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public Fraction min(final Fraction other) {
        requireNonNull(other, "other");
        return greaterThan(other) ? other : this;
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the maximum
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param other
     *            another {@link Fraction}
     * @return maximum
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public Fraction max(final Fraction other) {
        requireNonNull(other, "other");
        return lessThan(other) ? other : this;
    }

    /**
     * Returns the normalized {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced.
     *
     * @return normalized {@link Fraction}
     * @since 1
     */
    public Fraction normalize() {
        if (signum() < 0) {
            return new Fraction(numerator.abs().negate(), denominator.abs());
        }
        if (signum() == 0) {
            return ZERO;
        }
        if (numerator.compareTo(BigInteger.ZERO) < 0) {
            return abs();
        }
        return this;
    }

    /**
     * Returns the reduced {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not normalized.
     *
     * @return reduced {@link Fraction}
     * @since 1
     */
    public Fraction reduce() {
        final BigInteger gcd = numerator.gcd(denominator);
        return new Fraction(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * Returns the signum of this {@link Fraction}
     *
     * @return The signum of this {@link Fraction}
     * @see BigInteger#signum
     * @since 1
     */
    public int signum() {
        return numerator.signum() * denominator.signum();
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean}
     * which indicates if this {@link Fraction} is equivalent to the given one
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code true} if the {@code this} is equivalent to {@code other},
     *         {@code false} otherwise
     * @throws NullPointerException
     *             if {@code other == null}
     * @since 1
     */
    public boolean equivalent(final Fraction other) {
        requireNonNull(other, "other");
        return normalize().reduce().equals(other.normalize().reduce());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Fraction)) {
            return false;
        }
        final Fraction other = (Fraction) object;
        return numerator.equals(other.getNumerator()) && denominator.equals(other.getDenominator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("numerator", numerator).add("denominator", denominator).toString();
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }
}
