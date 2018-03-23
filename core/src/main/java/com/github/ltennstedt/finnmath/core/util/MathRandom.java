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

package com.github.ltennstedt.finnmath.core.util;

import static com.google.common.base.Preconditions.checkArgument;

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalVector;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalVector.BigDecimalVectorBuilder;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector.BigIntegerVectorBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix.RealComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberVector;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberVector.RealComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix.SimpleComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberVector;
import com.github.ltennstedt.finnmath.core.number.Fraction;
import com.github.ltennstedt.finnmath.core.number.PolarForm;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;

/**
 * A pseudo random generator for {@code long}, {@link BigDecimal},
 * {@link Fraction}, {@link SimpleComplexNumber}, {@link RealComplexNumber},
 * {@link BigIntegerVector}, {@link BigDecimalVector}, {@link BigIntegerMatrix}
 * and {@link BigDecimalMatrix}
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class MathRandom {
    private final Random random;

    /**
     * Default constructor
     *
     * @since 1
     */
    public MathRandom() {
        random = new Random();
    }

    /**
     * Constructor which has the seed value as parameter
     *
     * @param seed
     *            The seed
     * @since 1
     */
    public MathRandom(final long seed) {
        random = new Random(seed);
    }

    /**
     * Returns a positive {@link BigInteger} bounded below by {@code 0} (inclusive)
     * and above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link BigInteger}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     */
    public BigInteger nextPositiveBigInteger(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return BigInteger.valueOf(RandomUtils.nextLong(0, bound));
    }

    /**
     * Returns a negative {@link BigInteger} bounded below by {@code -bound}
     * (exclusive) and above by {@code 0} (inclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link BigInteger}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextPositiveBigInteger(long)
     * @since 1
     */
    public BigInteger nextNegativeBigInteger(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return nextPositiveBigInteger(bound).negate();
    }

    /**
     * Returns a {@link BigInteger} bounded below by {@code -bound} (exclusive) and
     * above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link BigInteger}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextPositiveBigInteger(long)
     * @see #nextNegativeBigInteger(long)
     * @since 1
     */
    public BigInteger nextBigInteger(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return random.nextBoolean() ? nextNegativeBigInteger(bound) : nextPositiveBigInteger(bound);
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive
     * {@link BigInteger BigIntegers}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigInteger BigIntegers}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextPositiveBigInteger(long)
     * @since 1
     */
    public List<BigInteger> nextPositiveBigIntegers(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigInteger> integers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> integers.add(nextPositiveBigInteger(bound)));
        return integers;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative
     * {@link BigInteger BigIntegers}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigInteger BigIntegers}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextNegativeBigInteger(long)
     * @since 1
     */
    public List<BigInteger> nextNegativeBigIntegers(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigInteger> integers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> integers.add(nextNegativeBigInteger(bound)));
        return integers;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigInteger BigIntegers}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigInteger BigIntegers}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextBigInteger(long)
     * @since 1
     */
    public List<BigInteger> nextBigIntegers(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigInteger> integers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> integers.add(nextBigInteger(bound)));
        return integers;
    }

    /**
     * Returns a positive {@link BigDecimal} of a given {@code scale} bounded below
     * by {@code 0} (inclusive) and above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     */
    public BigDecimal nextPositiveBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = nextBigDecimal(bound, scale);
        return decimal.compareTo(BigDecimal.ZERO) < 0 ? decimal.negate() : decimal;
    }

    /**
     * Returns a negative {@link BigDecimal} of a given {@code scale} bounded below
     * by {@code -bound} (exclusive) and above by {@code 0} (inclusive)
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     */
    public BigDecimal nextNegativeBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = nextBigDecimal(bound, scale);
        return decimal.compareTo(BigDecimal.ZERO) > 0 ? decimal.negate() : decimal;
    }

    /**
     * Returns a {@link BigDecimal} of a given {@code scale} bounded below by
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @since 1
     */
    public BigDecimal nextBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
        return keepBigDecimalInBound(decimal, bound).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * Returns a positive {@link BigDecimal} which is invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #nextPositiveBigDecimal
     * @since 1
     */
    public BigDecimal nextInvertiblePositiveBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = nextInvertibleBigDecimal(bound, scale);
        return decimal.compareTo(BigDecimal.ZERO) < 0 ? decimal.negate() : decimal;
    }

    /**
     * Returns a negative {@link BigDecimal} which is invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #nextNegativeBigDecimal
     * @see #nextInvertibleBigDecimal
     * @since 1
     */
    public BigDecimal nextInvertibleNegativeBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = nextInvertibleBigDecimal(bound, scale);
        return decimal.compareTo(BigDecimal.ZERO) > 0 ? decimal.negate() : decimal;
    }

    /**
     * Returns a {@link BigDecimal} which is invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #nextBigDecimal
     * @since 1
     */
    public BigDecimal nextInvertibleBigDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal decimal = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
        return keepBigDecimalInBound(decimal, bound).setScale(scale, RoundingMode.HALF_UP);
    }

    @VisibleForTesting
    BigDecimal keepBigDecimalInBound(final BigDecimal decimal, final long bound) {
        assert decimal != null;
        assert bound > 1;
        BigDecimal result = decimal;
        final BigDecimal decimalBound = BigDecimal.valueOf(bound);
        if (result.compareTo(BigDecimal.ZERO) > -1) {
            while (result.compareTo(decimalBound) > -1) {
                result = result.subtract(decimalBound);
            }
        } else {
            while (result.abs().compareTo(decimalBound) > -1) {
                result = result.add(decimalBound);
            }
        }
        return result;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive
     * {@link BigDecimal BigDecimals}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextPositiveBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextPositiveBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextPositiveBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative
     * {@link BigDecimal BigDecimals}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextNegativeBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextNegativeBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextNegativeBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigDecimal BigDecimals}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive
     * {@link BigDecimal BigDecimals} which are invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertiblePositiveBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextInvertiblePositiveBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextInvertiblePositiveBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative
     * {@link BigDecimal BigDecimals} which are invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleNegativeBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextInvertibleNegativeBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextInvertibleNegativeBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@code List} of the size of {@code howMany} containing
     * {@link BigDecimal BigDecimals} which are invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleBigDecimal
     * @since 1
     */
    public List<BigDecimal> nextInvertibleBigDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> decimals.add(nextInvertibleBigDecimal(bound, scale)));
        return decimals;
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by
     * {@code 0} (inclusive) and above by {@code
     * bound} (exclusive) and whose {@code denominator} is bounded below {@code 1}
     * (inclusive) and {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @since 1
     */
    public Fraction nextPositiveFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger numerator = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
        final BigInteger denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        return Fraction.of(numerator, denominator);
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by
     * {@code -bound} (exclusive) and above by {@code 0} (inclusive) and whose
     * {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound}
     * (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @since 1
     */
    public Fraction nextNegativeFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        return nextPositiveFraction(bound).negate();
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive) and whose
     * {@code denominator} is bounded below {@code -bound} (exclusive) and {@code
     * bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @since 1
     */
    public Fraction nextFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        return random.nextBoolean() ? nextNegativeFraction(bound) : nextPositiveFraction(bound);
    }

    /**
     * Returns a positive {@link Fraction} which is invertible
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @see #nextPositiveFraction
     * @since 1
     */
    public Fraction nextInvertiblePositiveFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger numerator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        final BigInteger denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        return Fraction.of(numerator, denominator);
    }

    /**
     * Returns a negative {@link Fraction} which is invertible
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @see #nextNegativeFraction
     * @since 1
     */
    public Fraction nextInvertibleNegativeFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        return nextInvertiblePositiveFraction(bound).negate();
    }

    /**
     * Returns a {@link Fraction} which is invertible
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @see #nextFraction
     * @since 1
     */
    public Fraction nextInvertibleFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        return random.nextBoolean() ? nextInvertibleNegativeFraction(bound) : nextInvertiblePositiveFraction(bound);
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive
     * {@link Fraction Fractions}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextPositiveFraction
     * @since 1
     */
    public List<Fraction> nextPositiveFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextPositiveFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative
     * {@link Fraction Fractions}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextNegativeFraction
     * @since 1
     */
    public List<Fraction> nextNegativeFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextNegativeFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link Fraction Fractions}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextFraction
     * @since 1
     */
    public List<Fraction> nextFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive
     * {@link Fraction Fractions} which are invertible
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertiblePositiveFraction
     * @since 1
     */
    public List<Fraction> nextInvertiblePositiveFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextInvertiblePositiveFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative
     * {@link Fraction Fractions} which are invertible
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleNegativeFraction
     * @since 1
     */
    public List<Fraction> nextInvertibleNegativeFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextInvertibleNegativeFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link Fraction Fractions} which are invertible
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleFraction
     * @since 1
     */
    public List<Fraction> nextInvertibleFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> fractions.add(nextInvertibleFraction(bound)));
        return fractions;
    }

    /**
     * Returns a {@link SimpleComplexNumber} whose {@code real} and
     * {@code imaginary} part are bounded below by {@code
     * -bound} (exclusive) and above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextBigInteger(long)
     * @since 1
     */
    public SimpleComplexNumber nextSimpleComplexNumber(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return SimpleComplexNumber.of(nextBigInteger(bound), nextBigInteger(bound));
    }

    /**
     * Returns a {@link SimpleComplexNumber} which is invertible
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextBigInteger(long)
     * @since 1
     */
    public SimpleComplexNumber nextInvertibleSimpleComplexNumber(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger nonZeroPart = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        final BigInteger possibleZeroPart = nextBigInteger(bound);
        return random.nextBoolean() ? SimpleComplexNumber.of(possibleZeroPart, nonZeroPart)
            : SimpleComplexNumber.of(nonZeroPart, possibleZeroPart);
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link SimpleComplexNumber SimpleComplexNumbers}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A List {@link List} of pseudo random {@link SimpleComplexNumber
     *         SimpleComplexNumbers}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextSimpleComplexNumber
     * @since 1
     */
    public List<SimpleComplexNumber> nextSimpleComplexNumbers(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> complexNumbers.add(nextSimpleComplexNumber(bound)));
        return complexNumbers;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link SimpleComplexNumber SimpleComplexNumbers} which are invertible
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link SimpleComplexNumber
     *         SimpleComplexNumbers}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleSimpleComplexNumber
     * @since 1
     */
    public List<SimpleComplexNumber> nextInvertibleSimpleComplexNumbers(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> complexNumbers.add(nextInvertibleSimpleComplexNumber(bound)));
        return complexNumbers;
    }

    /**
     * Returns a {@link RealComplexNumber} whose {@code real} and {@code imaginary}
     * part are bounded below by {@code
     * -bound} (exclusive) and above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link RealComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     */
    public RealComplexNumber nextRealComplexNumber(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal real = nextBigDecimal(bound, scale);
        final BigDecimal imaginary = nextBigDecimal(bound, scale);
        return RealComplexNumber.of(real, imaginary);
    }

    /**
     * Returns a {@link RealComplexNumber} which is invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link RealComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextRealComplexNumber
     * @since 1
     */
    public RealComplexNumber nextInvertibleRealComplexNumber(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        final BigDecimal nonZeroPart = nextInvertibleBigDecimal(bound, scale);
        final BigDecimal possibleZeroPart =
            random.nextBoolean() ? nextInvertibleBigDecimal(bound, scale) : nextBigDecimal(bound, scale);
        return random.nextBoolean() ? RealComplexNumber.of(possibleZeroPart, nonZeroPart)
            : RealComplexNumber.of(nonZeroPart, possibleZeroPart);
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link RealComplexNumber RealComplexNumbers}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumber
     *         RealComplexNumbers}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextRealComplexNumber
     * @since 1
     */
    public List<RealComplexNumber> nextRealComplexNumbers(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumber> complexNumbers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> complexNumbers.add(nextRealComplexNumber(bound, scale)));
        return complexNumbers;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link RealComplexNumber RealComplexNumbers} which are invertible
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumber
     *         RealComplexNumbers}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleRealComplexNumber
     * @since 1
     */
    public List<RealComplexNumber> nextInvertibleRealComplexNumbers(final long bound, final int scale,
        final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumber> complexNumbers = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> complexNumbers.add(nextInvertibleRealComplexNumber(bound, scale)));
        return complexNumbers;
    }

    /**
     * Returns a {@link PolarForm}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A {@link PolarForm}
     * @throws IllegalArgumentException
     *             if {@code  bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @see #nextInvertibleRealComplexNumber
     * @since 1
     */
    public PolarForm nextPolarForm(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        return new PolarForm(nextBigDecimal(bound, scale), nextBigDecimal(bound, scale));
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link PolarForm PolarForms}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumber
     *         RealComplexNumbers}
     * @throws IllegalArgumentException
     *             if {@code  bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleRealComplexNumber
     * @since 1
     */
    public List<PolarForm> nextPolarForms(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<PolarForm> polarForms = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> polarForms.add(nextPolarForm(bound, scale)));
        return polarForms;
    }

    /**
     * Returns a {@link BigIntegerVector}
     *
     * @param bound
     *            the bound
     * @param size
     *            the size of the resulting {@link BigIntegerVector}
     * @return A pseudo random {@link BigIntegerVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigInteger(long)
     * @since 1
     */
    public BigIntegerVector nextBigIntegerVector(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerVectorBuilder builder = BigIntegerVector.builder(size);
        IntStream.rangeClosed(1, size).forEach(i -> builder.put(nextBigInteger(bound)));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigIntegerVector BigIntegerVectors}
     *
     * @param bound
     *            the bound
     * @param size
     *            the sizes of the resulting {@link BigIntegerVector
     *            BigIntegerVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntegerVector
     *         BigIntegerVectors}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextBigIntegerVector
     * @since 1
     */
    public List<BigIntegerVector> nextBigIntegerVectors(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerVector> vectors = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> vectors.add(nextBigIntegerVector(bound, size)));
        return vectors;
    }

    /**
     * Returns a {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link BigIntegerMatrix}
     * @param columnSize
     *            the column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextBigInteger(long)
     * @since 1
     */
    public BigIntegerMatrix nextBigIntegerMatrix(final long bound, final int rowSize, final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList())
            .forEach(rowIndex -> IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList())
                .forEach(columnIndex -> builder.put(rowIndex, columnIndex, nextBigInteger(bound))));
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of resulting {@link BigIntegerMatrix}
     * @return A pseudo random upper triangular {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigInteger(long)
     * @see BigIntegerMatrix#upperTriangular
     * @since 1
     */
    public BigIntegerMatrix nextUpperTriangularBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 1) {
                    builder.put(rowIndex, columnIndex, nextBigInteger(bound));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random lower triangular {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigInteger(long)
     * @see BigIntegerMatrix#lowerTriangular
     * @since 1
     */
    public BigIntegerMatrix nextLowerTriangularBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) > -1) {
                    builder.put(rowIndex, columnIndex, nextBigInteger(bound));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a triangular {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random triangular {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextUpperTriangularBigIntegerMatrix
     * @see #nextLowerTriangularBigIntegerMatrix
     * @see BigIntegerMatrix#triangular
     * @since 1
     */
    public BigIntegerMatrix nextTriangularBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return random.nextBoolean() ? nextLowerTriangularBigIntegerMatrix(bound, size)
            : nextUpperTriangularBigIntegerMatrix(bound, size);
    }

    /**
     * Returns a diagonal {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random diagonal {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigIntegerMatrix#diagonal
     * @since 1
     */
    public BigIntegerMatrix nextDiagonalBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, nextBigInteger(bound));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a symmetric {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random symmetric {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigIntegerMatrix#symmetric
     * @since 1
     */
    public BigIntegerMatrix nextSymmetricBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigInteger element = nextBigInteger(bound);
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, element);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link BigIntegerMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix}
     * @return A pseudo random skew-symmetric {@link BigIntegerMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigIntegerMatrix#skewSymmetric
     * @since 1
     */
    public BigIntegerMatrix nextSkewSymmetricBigIntegerMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 0) {
                    final BigInteger element = nextBigInteger(bound);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param columnSize
     *            the column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntegerMatrix
     *         BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextBigIntegerMatrices(final long bound, final int rowSize, final int columnSize,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextBigIntegerMatrix(bound, rowSize, columnSize)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular
     *         {@link BigIntegerMatrix BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextUpperTriangularBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextUpperTriangularBigIntegerMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextUpperTriangularBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular
     *         {@link BigIntegerMatrix BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextLowerTriangularBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextLowerTriangularBigIntegerMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextLowerTriangularBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link BigIntegerMatrix
     *         BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextTriangularBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextTriangularBigIntegerMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextTriangularBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link BigIntegerMatrix
     *         BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDiagonalBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextDiagonalBigIntegerMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextDiagonalBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link BigIntegerMatrix
     *         BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSymmetricBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextSymmetricBigIntegerMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSymmetricBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntegerMatrix
     *            BigIntegerMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric
     *         {@link BigIntegerMatrix BigIntegerMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSkewSymmetricBigIntegerMatrix
     * @since 1
     */
    public List<BigIntegerMatrix> nextSkewSymmetricBigIntegerMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigIntegerMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSkewSymmetricBigIntegerMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link BigDecimalVector}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size
     *            the size of the resulting {@link BigDecimalVector}
     * @return A speudo random {@link BigDecimalVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigDecimal
     * @since 1
     */
    public BigDecimalVector nextBigDecimalVector(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalVectorBuilder builder = BigDecimalVector.builder(size);
        IntStream.rangeClosed(1, size).forEach(i -> builder.put(nextBigDecimal(bound, scale)));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigDecimalVector BigDecimalVectors}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size
     *            the size of the resulting {@link BigDecimalVector
     *            BigDecimalVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimalVector
     *         BigDecimalVectors}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextBigDecimalVector
     * @since 1
     */
    public List<BigDecimalVector> nextBigDecimalVectors(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalVector> vectors = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> vectors.add(nextBigDecimalVector(bound, scale, size)));
        return vectors;
    }

    /**
     * Returns a {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param rowSize
     *            the row size of the resulting {@link BigDecimalMatrix}
     * @param columnSize
     *            the column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextBigDecimal
     * @since 1
     */
    public BigDecimalMatrix nextBigDecimalMatrix(final long bound, final int scale, final int rowSize,
        final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList())
            .forEach(rowIndex -> IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList())
                .forEach(columnIndex -> builder.put(rowIndex, columnIndex, nextBigDecimal(bound, scale))));
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of resulting {@link BigDecimalMatrix}
     * @return A pseudo random upper triangular {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigDecimal
     * @see BigDecimalMatrix#upperTriangular
     * @since 1
     */
    public BigDecimalMatrix nextUpperTriangularBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 1) {
                    builder.put(rowIndex, columnIndex, nextBigDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random lower triangular {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextBigDecimal
     * @see BigDecimalMatrix#lowerTriangular
     * @since 1
     */
    public BigDecimalMatrix nextLowerTriangularBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) > -1) {
                    builder.put(rowIndex, columnIndex, nextBigDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a triangular {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random triangular {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextUpperTriangularBigDecimalMatrix
     * @see #nextLowerTriangularBigDecimalMatrix
     * @see BigDecimalMatrix#triangular
     * @since 1
     */
    public BigDecimalMatrix nextTriangularBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return random.nextBoolean() ? nextLowerTriangularBigDecimalMatrix(bound, scale, size)
            : nextUpperTriangularBigDecimalMatrix(bound, scale, size);
    }

    /**
     * Returns a diagonal {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random diagonal {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigDecimalMatrix#diagonal
     * @since 1
     */
    public BigDecimalMatrix nextDiagonalBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, nextBigDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a symmetric {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random symmetric {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigDecimalMatrix#symmetric
     * @since 1
     */
    public BigDecimalMatrix nextSymmetricBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigDecimal element = nextBigDecimal(bound, scale);
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, element);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link BigDecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix}
     * @return A pseudo random skew-symmetric {@link BigDecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see BigDecimalMatrix#skewSymmetric
     * @since 1
     */
    public BigDecimalMatrix nextSkewSymmetricBigDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 0) {
                    final BigDecimal element = nextBigDecimal(bound, scale);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param rowSize
     *            the row size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param columnSize
     *            the column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimalMatrix
     *         BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextBigDecimalMatrices(final long bound, final int scale, final int rowSize,
        final int columnSize, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextBigDecimalMatrix(bound, scale, rowSize, columnSize)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular
     *         {@link BigDecimalMatrix BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextUpperTriangularBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextUpperTriangularBigDecimalMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextUpperTriangularBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular
     *         {@link BigDecimalMatrix BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextLowerTriangularBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextLowerTriangularBigDecimalMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextLowerTriangularBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link BigDecimalMatrix
     *         BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextTriangularBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextTriangularBigDecimalMatrices(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextTriangularBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link BigDecimalMatrix
     *         BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDiagonalBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextDiagonalBigDecimalMatrices(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextDiagonalBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link BigDecimalMatrix
     *         BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSymmetricBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextSymmetricBigDecimalMatrices(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSymmetricBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link BigDecimalMatrix
     *            BigDecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric
     *         {@link BigDecimalMatrix BigDecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSkewSymmetricBigDecimalMatrix
     * @since 1
     */
    public List<BigDecimalMatrix> nextSkewSymmetricBigDecimalMatrices(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<BigDecimalMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSkewSymmetricBigDecimalMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link SimpleComplexNumberVector}
     *
     * @param bound
     *            the bound
     * @param size
     *            the size of the resulting {@link SimpleComplexNumberVector}
     * @return A pseudo random {@link SimpleComplexNumberVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextSimpleComplexNumber(long)
     * @since 1
     */
    public SimpleComplexNumberVector nextSimpleComplexNumberVector(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder builder =
            SimpleComplexNumberVector.builder(size);
        IntStream.rangeClosed(1, size).forEach(i -> builder.put(nextSimpleComplexNumber(bound)));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link SimpleComplexNumberVector SimpleComplexNumberVectors}
     *
     * @param bound
     *            the bound
     * @param size
     *            the sizes of the resulting {@link SimpleComplexNumberVector
     *            SimpleComplexNumberVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link SimpleComplexNumberVector
     *         SimpleComplexNumberVectors}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextSimpleComplexNumberVector
     * @since 1
     */
    public List<SimpleComplexNumberVector> nextSimpleComplexNumberVectors(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberVector> vectors = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> vectors.add(nextSimpleComplexNumberVector(bound, size)));
        return vectors;
    }

    /**
     * Returns a {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link SimpleComplexNumberMatrix}
     * @param columnSize
     *            the column size of the resulting {@link SimpleComplexNumberMatrix}
     * @return A pseudo random {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextSimpleComplexNumber(long)
     * @since 1
     */
    public SimpleComplexNumberMatrix nextSimpleComplexNumberMatrix(final long bound, final int rowSize,
        final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList())
            .forEach(rowIndex -> IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList())
                .forEach(columnIndex -> builder.put(rowIndex, columnIndex, nextSimpleComplexNumber(bound))));
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random upper triangular {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see SimpleComplexNumberMatrix#upperTriangular
     * @since 1
     */
    public SimpleComplexNumberMatrix nextUpperTriangularSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 1) {
                    builder.put(rowIndex, columnIndex, nextSimpleComplexNumber(bound));
                } else {
                    builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random lower triangular {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see SimpleComplexNumberMatrix#lowerTriangular
     * @since 1
     */
    public SimpleComplexNumberMatrix nextLowerTriangularSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) > -1) {
                    builder.put(rowIndex, columnIndex, nextSimpleComplexNumber(bound));
                } else {
                    builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a triangular {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random triangular {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextUpperTriangularSimpleComplexNumberMatrix
     * @see #nextLowerTriangularSimpleComplexNumberMatrix
     * @see SimpleComplexNumberMatrix#triangular
     * @since 1
     */
    public SimpleComplexNumberMatrix nextTriangularSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return random.nextBoolean() ? nextLowerTriangularSimpleComplexNumberMatrix(bound, size)
            : nextUpperTriangularSimpleComplexNumberMatrix(bound, size);
    }

    /**
     * Returns a diagonal {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random diagonal {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see SimpleComplexNumberMatrix#diagonal
     * @since 1
     */
    public SimpleComplexNumberMatrix nextDiagonalSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, nextSimpleComplexNumber(bound));
                } else {
                    builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a symmetric {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random symmetric {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see SimpleComplexNumberMatrix#symmetric
     * @since 1
     */
    public SimpleComplexNumberMatrix nextSymmetricSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final SimpleComplexNumber element = nextSimpleComplexNumber(bound);
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, element);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link SimpleComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix}
     * @return A pseudo random skew-symmetric {@link SimpleComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see SimpleComplexNumberMatrix#skewSymmetric
     * @since 1
     */
    public SimpleComplexNumberMatrix nextSkewSymmetricSimpleComplexNumberMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 0) {
                    final SimpleComplexNumber element = nextSimpleComplexNumber(bound);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link SimpleComplexNumberMatrix
     *            SimpleComplexNumberMatrices}
     * @param columnSize
     *            the column size of the resulting {@link SimpleComplexNumberMatrix
     *            SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link SimpleComplexNumberMatrix
     *         SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextSimpleComplexNumberMatrices(final long bound, final int rowSize,
        final int columnSize, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextSimpleComplexNumberMatrix(bound, rowSize, columnSize)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextUpperTriangularSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextUpperTriangularSimpleComplexNumberMatrices(final long bound,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextUpperTriangularSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextLowerTriangularSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextLowerTriangularSimpleComplexNumberMatrices(final long bound,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextLowerTriangularSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextTriangularSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextTriangularSimpleComplexNumberMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextTriangularSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDiagonalSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextDiagonalSimpleComplexNumberMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextDiagonalSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSymmetricSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextSymmetricSimpleComplexNumberMatrices(final long bound, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSymmetricSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting
     *            {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric
     *         {@link SimpleComplexNumberMatrix SimpleComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSkewSymmetricSimpleComplexNumberMatrix
     * @since 1
     */
    public List<SimpleComplexNumberMatrix> nextSkewSymmetricSimpleComplexNumberMatrices(final long bound,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<SimpleComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextSkewSymmetricSimpleComplexNumberMatrix(bound, size)));
        return matrices;
    }

    /**
     * Returns a {@link RealComplexNumberVector}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the size of the resulting {@link RealComplexNumberVector}
     * @return A pseudo random {@link RealComplexNumberVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextRealComplexNumber(long, int)
     * @since 1
     */
    public RealComplexNumberVector nextRealComplexNumberVector(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberVectorBuilder builder = RealComplexNumberVector.builder(size);
        IntStream.rangeClosed(1, size).forEach(i -> builder.put(nextRealComplexNumber(bound, scale)));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link RealComplexNumberVector RealComplexNumberVectors}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the sizes of the resulting {@link RealComplexNumberVector
     *            RealComplexNumberVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumberVector
     *         RealComplexNumberVectors}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextRealComplexNumberVector
     * @since 1
     */
    public List<RealComplexNumberVector> nextRealComplexNumberVectors(final long bound, final int scale, final int size,
        final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberVector> vectors = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> vectors.add(nextRealComplexNumberVector(bound, scale, size)));
        return vectors;
    }

    /**
     * Returns a {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param rowSize
     *            the row size of the resulting {@link RealComplexNumberMatrix}
     * @param columnSize
     *            the column size of the resulting {@link RealComplexNumberMatrix}
     * @return A pseudo random {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextRealComplexNumber(long, int)
     * @since 1
     */
    public RealComplexNumberMatrix nextRealComplexNumberMatrix(final long bound, final int scale, final int rowSize,
        final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList())
            .forEach(rowIndex -> IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList())
                .forEach(columnIndex -> builder.put(rowIndex, columnIndex, nextRealComplexNumber(bound, scale))));
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random upper triangular {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextRealComplexNumber(long, int)
     * @see RealComplexNumberMatrix#upperTriangular
     * @since 1
     */
    public RealComplexNumberMatrix nextUpperTriangularRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 1) {
                    builder.put(rowIndex, columnIndex, nextRealComplexNumber(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random lower triangular {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextRealComplexNumber(long, int)
     * @see RealComplexNumberMatrix#lowerTriangular
     * @since 1
     */
    public RealComplexNumberMatrix nextLowerTriangularRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) > -1) {
                    builder.put(rowIndex, columnIndex, nextRealComplexNumber(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a triangular {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random triangular {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextUpperTriangularRealComplexNumberMatrix
     * @see #nextLowerTriangularRealComplexNumberMatrix
     * @see RealComplexNumberMatrix#triangular
     * @since 1
     */
    public RealComplexNumberMatrix nextTriangularRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return random.nextBoolean() ? nextLowerTriangularRealComplexNumberMatrix(bound, scale, size)
            : nextUpperTriangularRealComplexNumberMatrix(bound, scale, size);
    }

    /**
     * Returns a diagonal {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random diagonal {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see RealComplexNumberMatrix#diagonal
     * @since 1
     */
    public RealComplexNumberMatrix nextDiagonalRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, nextRealComplexNumber(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a symmetric {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random symmetric {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see RealComplexNumberMatrix#symmetric
     * @since 1
     */
    public RealComplexNumberMatrix nextSymmetricRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final RealComplexNumber element = nextRealComplexNumber(bound, scale);
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, element);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link RealComplexNumberMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix}
     * @return A pseudo random skew-symmetric {@link RealComplexNumberMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see RealComplexNumberMatrix#skewSymmetric
     * @since 1
     */
    public RealComplexNumberMatrix nextSkewSymmetricRealComplexNumberMatrix(final long bound, final int scale,
        final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(
            rowIndex -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.compareTo(columnIndex) < 0) {
                    final RealComplexNumber element = nextRealComplexNumber(bound, scale);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                } else if (rowIndex.compareTo(columnIndex) == 0) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            }));
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param rowSize
     *            the row size of the resulting {@link RealComplexNumberMatrix
     *            RealComplexNumberMatrices}
     * @param columnSize
     *            the column size of the resulting {@link RealComplexNumberMatrix
     *            RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumberMatrix
     *         RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextRealComplexNumberMatrices(final long bound, final int scale,
        final int rowSize, final int columnSize, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextRealComplexNumberMatrix(bound, scale, rowSize, columnSize)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextUpperTriangularRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextUpperTriangularRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextUpperTriangularRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextLowerTriangularRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextLowerTriangularRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextLowerTriangularRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextTriangularRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextTriangularRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextTriangularRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDiagonalRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextDiagonalRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany).forEach(i -> matrices.add(nextDiagonalRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSymmetricRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextSymmetricRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextSymmetricRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting
     *            {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric
     *         {@link RealComplexNumberMatrix RealComplexNumberMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 0}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextSkewSymmetricRealComplexNumberMatrix
     * @since 1
     */
    public List<RealComplexNumberMatrix> nextSkewSymmetricRealComplexNumberMatrices(final long bound, final int scale,
        final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > -1, "expected scale > -1 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 0, "expected howMany > 0 but actual %s", howMany);
        final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
        IntStream.range(0, howMany)
            .forEach(i -> matrices.add(nextSkewSymmetricRealComplexNumberMatrix(bound, scale, size)));
        return matrices;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("random", random).toString();
    }

    @VisibleForTesting
    Random getRandom() {
        return random;
    }
}
