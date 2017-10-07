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

package com.github.ltennstedt.finnmath.number;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * An immutable implementation of a fraction which uses {@link BigInteger} as
 * type for its numerator and denominator
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class Fraction extends Number implements MathNumber<Fraction, Fraction, Fraction>, Comparable<Fraction> {
    /**
     * {@code 0} as {@link Fraction}
     */
    public static final Fraction ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE);

    /**
     * {@code 1} as {@link Fraction}
     */
    public static final Fraction ONE = new Fraction(BigInteger.ONE, BigInteger.ONE);

    private static final long serialVersionUID = 1L;

    /**
     * {@code numerator} of this {@link Fraction}
     */
    private final BigInteger numerator;

    /**
     * {@code denominator} of this {@link Fraction}
     */
    private final BigInteger denominator;

    /**
     * Constructs a {@link Fraction} by the given numerator and denominator
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param numerator
     *            the numerator
     * @param denominator
     *            the denominator
     * @throws NullPointerException
     *             if {@code numerator == null}
     * @throws NullPointerException
     *             if {@code denominator == null}
     * @throws IllegalArgumentException
     *             if {@code denominator == 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    public Fraction(final BigInteger numerator, final BigInteger denominator) {
        super();
        this.numerator = requireNonNull(numerator, "numerator");
        this.denominator = requireNonNull(denominator, "denominator");
        checkArgument(!denominator.equals(BigInteger.ZERO), "expected denominator != 0 but actual %s", denominator);
    }

    /**
     * Returns an {@code int} representation of this {@link Fraction}
     *
     * @return The {@code int} value
     * @since 1
     * @author Lars Tennstedt
     * @see BigDecimal#intValue
     */
    @Override
    public int intValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), BigDecimal.ROUND_HALF_UP).intValue();
    }

    /**
     * Returns a {@code long} representation of this {@link Fraction}
     *
     * @return The {@code long} value
     * @since 1
     * @author Lars Tennstedt
     * @see BigDecimal#intValue
     */
    @Override
    public long longValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * Returns a {@code float} representation of this {@link Fraction}
     *
     * @return The {@code float} value
     * @since 1
     * @author Lars Tennstedt
     * @see BigDecimal#intValue
     */
    @Override
    public float floatValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * Returns a {@code double} representation of this {@link Fraction}
     *
     * @return The {@code double} value
     * @since 1
     * @author Lars Tennstedt
     * @see BigDecimal#intValue
     */
    @Override
    public double doubleValue() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Returns the sum of this {@link Fraction} and the given one
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param summand
     *            the summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    @Override
    public Fraction add(final Fraction summand) {
        requireNonNull(summand, "summand");
        final BigInteger newNumerator = summand.getDenominator().multiply(numerator)
                .add(denominator.multiply(summand.getNumerator()));
        final BigInteger newDenominator = denominator.multiply(summand.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns the difference of this {@link Fraction} and the given one
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    @Override
    public Fraction subtract(final Fraction subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        final BigInteger newNumerator = subtrahend.getDenominator().multiply(numerator)
                .subtract(denominator.multiply(subtrahend.getNumerator()));
        final BigInteger newDenominator = denominator.multiply(subtrahend.getDenominator());
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Returns the product of this {@link Fraction} and the given one
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param factor
     *            the factor
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
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
     *
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
     * @since 1
     * @author Lars Tennstedt
     * @see #invert
     * @see #multiply
     * @see #normalize
     * @see #reduce
     */
    @Override
    public Fraction divide(final Fraction divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return multiply(divisor.invert());
    }

    /**
     * Returns the power of this {@link Fraction} raised by the given exponent
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
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
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    @Override
    public Fraction negate() {
        return new Fraction(numerator.negate(), denominator);
    }

    /**
     * Returns the inverted {@link Fraction} of this one
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code numerator == 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
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
     * @author Lars Tennstedt
     */
    @Override
    public boolean invertible() {
        return !numerator.equals(BigInteger.ZERO);
    }

    /**
     * Returns the absolute {@link Fraction} of this one
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @return The absolute
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     * @see BigInteger#abs
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
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThan
     * @see #greaterThan
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
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
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
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThanOrEqualTo
     * @see #equivalent
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
     * @since 1
     * @author Lars Tennstedt
     * @see #greaterThanOrEqualTo
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
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThanOrEqualTo
     */
    public boolean greaterThan(final Fraction other) {
        requireNonNull(other, "other");
        return !lessThanOrEqualTo(other);
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the minimum
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param other
     *            another {@link Fraction}
     * @return The minimum of this {@link Fraction} and the other one
     * @since 1
     * @author Lars Tennstedt
     * @see #greaterThan
     * @see #normalize
     * @see #reduce
     */
    public Fraction min(final Fraction other) {
        requireNonNull(other, "other");
        if (greaterThan(other)) {
            return other;
        }
        return this;
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the maximum
     *
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     *
     * @param other
     *            another {@link Fraction}
     * @return The maximum of this {@link Fraction} and the other one
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThan
     * @see #normalize
     * @see #reduce
     */
    public Fraction max(final Fraction other) {
        requireNonNull(other, "other");
        if (lessThan(other)) {
            return other;
        }
        return this;
    }

    /**
     * Returns the normalized {@link Fraction} of this one
     *
     * <p>
     * The returned {@link Fraction} is not reduced.
     *
     * @return {@code new Fraction(-numerator.abs, denominator.abs)} if the
     *         {@code signum < 0}, {@code ZERO} if {@code signum == 0}, {@code this}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #signum
     * @see #abs
     * @see BigInteger#abs
     * @see #reduce
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
     *
     * <p>
     * The returned {@link Fraction} is not normalized.
     *
     * @return The reduced
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see BigInteger#gcd
     */
    public Fraction reduce() {
        final BigInteger gcd = numerator.gcd(denominator);
        return new Fraction(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * Returns the signum of this {@link Fraction}
     *
     * @return The signum of this {@link Fraction}
     * @since 1
     * @author Lars Tennstedt
     * @see BigInteger#signum
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
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    public boolean equivalent(final Fraction other) {
        requireNonNull(other, "other");
        return normalize().reduce().equals(other.normalize().reduce());
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
