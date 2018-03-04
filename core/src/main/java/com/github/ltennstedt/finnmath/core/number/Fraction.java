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
 *
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

    /**
     * {@code numerator} of this {@link Fraction}
     *
     * @since 1
     */
    private final BigInteger numerator;

    /**
     * {@code denominator} of this {@link Fraction}
     *
     * @since 1
     */
    private final BigInteger denominator;

    private Fraction(final BigInteger numerator, final BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Returns the sum of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @see #normalize
     * @see #reduce
     * @since 1
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
     * Returns the difference of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @see #normalize
     * @see #reduce
     * @since 1
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
     * Returns the product of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param factor
     *            the factor
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    @Override
    public Fraction multiply(final Fraction factor) {
        requireNonNull(factor, "factor");
        final BigInteger newNumerator = numerator.multiply(factor.getNumerator());
        final BigInteger newDenominator = denominator.multiply(factor.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Return the quotient of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code !divisor.invertible}
     * @see #invert
     * @see #multiply
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    @Override
    public Fraction divide(final Fraction divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return multiply(divisor.invert());
    }

    /**
     * Returns the power of this {@link Fraction} raised by the given exponent
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @see #normalize
     * @see #reduce
     * @since 1
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
     * Returns the negated {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The negated
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    @Override
    public Fraction negate() {
        return new Fraction(numerator.negate(), denominator);
    }

    /**
     * Returns the inverted {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code numerator == 0}
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    @Override
    public Fraction invert() {
        checkState(invertible(), "expected to be invertible but actual %s", this);
        return new Fraction(denominator, numerator);
    }

    /**
     * Returns if this {@link Fraction} is invertible
     *
     * @return {@code true} if {@code numerator != 0}, {@code false} otherwise
     * @since 1
     */
    @Override
    public boolean invertible() {
        return numerator.compareTo(BigInteger.ZERO) != 0;
    }

    /**
     * Returns the absolute {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The absolute
     * @see #normalize
     * @see #reduce
     * @see BigInteger#abs
     * @since 1
     */
    @Override
    public Fraction abs() {
        return new Fraction(numerator.abs(), denominator.abs());
    }

    /**
     * Compares this {@link Fraction} to the given one and returns an int which
     * indicates which one is less
     *
     * @param other
     *            another {@link Fraction}
     * @return {@code -1} if {@code this < other}, {@code 1} if
     *         {@code this > other}, {@code 0} otherwise
     * @see #lessThan
     * @see #greaterThan
     * @since 1
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
     * @see #normalize
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
     * @see #lessThanOrEqualTo
     * @see #equivalent
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
     * @see #greaterThanOrEqualTo
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
     * @see #lessThanOrEqualTo
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
     * @return The minimum of this {@link Fraction} and the other one
     * @see #greaterThan
     * @see #normalize
     * @see #reduce
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
     * @return The maximum of this {@link Fraction} and the other one
     * @see #lessThan
     * @see #normalize
     * @see #reduce
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
     * @return {@code new Fraction(-numerator.abs, denominator.abs)} if the
     *         {@code signum < 0}, {@code ZERO} if {@code
     * signum == 0}, {@code this} otherwise
     * @see #signum
     * @see #abs
     * @see BigInteger#abs
     * @see #reduce
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
     * @return The reduced
     * @see #normalize
     * @see BigInteger#gcd
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
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    public boolean equivalent(final Fraction other) {
        requireNonNull(other, "other");
        return normalize().reduce().equals(other.normalize().reduce());
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
     * @see #normalize
     * @see #reduce
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
     * @see #normalize
     * @see #reduce
     * @since 1
     */
    public static Fraction of(final BigInteger numerator, final BigInteger denominator) {
        requireNonNull(numerator, "numerator");
        requireNonNull(denominator, "denominator");
        checkArgument(denominator.compareTo(BigInteger.ZERO) != 0, "expected denominator != 0 but actual %s",
            denominator);
        return new Fraction(numerator, denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

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
