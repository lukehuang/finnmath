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

package finnmath.util;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import finnmath.linear.BigIntMatrix;
import finnmath.linear.BigIntMatrix.BigIntMatrixBuilder;
import finnmath.linear.BigIntVector;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import finnmath.linear.DecimalMatrix;
import finnmath.linear.DecimalMatrix.DecimalMatrixBuilder;
import finnmath.linear.DecimalVector;
import finnmath.linear.DecimalVector.DecimalVectorBuilder;
import finnmath.number.Fraction;
import finnmath.number.RealComplexNumber;
import finnmath.number.SimpleComplexNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;

/**
 * A pseudo random generator for {@code long}, {@link BigDecimal},
 * {@link Fraction}, {@link SimpleComplexNumber}, {@link RealComplexNumber},
 * {@link BigIntVector}, {@link DecimalVector}, {@link BigIntMatrix} and
 * {@link DecimalMatrix}
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class MathRandom {
    private final Random random = new Random();

    /**
     * Returns a positive {@code long} bounded below by {@code 0} (inclusive) and
     * above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public long nextPositiveLong(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return RandomUtils.nextLong(0, bound);
    }

    /**
     * Returns a negative {@code long} bounded below by {@code -bound} (exclusive)
     * and above by {@code 0} (inclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public long nextNegativeLong(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        return (-1) * RandomUtils.nextLong(0, bound);
    }

    /**
     * Returns a {@code long} bounded below by {@code -bound} (exclusive) and above
     * by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public long nextLong(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        if (random.nextBoolean()) {
            return nextNegativeLong(bound);
        }
        return nextPositiveLong(bound);
    }

    /**
     * Returns an array of the length of {@code howMany} containing positive
     * {@code long longs}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the length of the resulting array
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextPositiveLong
     * @since 1
     * @author Lars Tennstedt
     */
    public long[] nextPositiveLongs(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final long[] ints = new long[howMany];
        for (int i = 0; i < howMany; i++) {
            ints[i] = nextPositiveLong(bound);
        }
        return ints;
    }

    /**
     * Returns an array of the length of {@code howMany} containing negative
     * {@code long longs}
     *
     * @param bound
     *            {@code long}
     * @param howMany
     *            {@code int}
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextNegativeLong
     * @since 1
     * @author Lars Tennstedt
     */
    public long[] nextNegativeLongs(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final long[] ints = new long[howMany];
        for (int i = 0; i < howMany; i++) {
            ints[i] = nextNegativeLong(bound);
        }
        return ints;
    }

    /**
     * Returns an array of the length of {@code howMany} containing
     * {@code long longs}
     *
     * @param bound
     *            the bound
     * @param howMany
     *            the length of the resulting array
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException
     *             if {@code  bound < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
     */
    public long[] nextLongs(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final long[] ints = new long[howMany];
        for (int i = 0; i < howMany; i++) {
            ints[i] = nextLong(bound);
        }
        return ints;
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
     *             if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextPositiveDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = nextDecimal(bound, scale);
        if (decimal.compareTo(BigDecimal.ZERO) < 0) {
            return decimal.negate();
        }
        return decimal;
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
     *             if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextNegativeDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = nextDecimal(bound, scale);
        if (decimal.compareTo(BigDecimal.ZERO) > 0) {
            return decimal.negate();
        }
        return decimal;
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
     *             if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextDecimal(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
        return keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP);
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
     *             if {@code scale < 1}
     * @see #nextPositiveDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextInvertiblePositiveDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = nextInvertibleDecimal(bound, scale);
        if (decimal.compareTo(BigDecimal.ZERO) < 0) {
            return decimal.negate();
        }
        return decimal;
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
     *             if {@code scale < 1}
     * @see #nextNegativeDecimal
     * @see #nextInvertibleDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextInvertibleNegativeDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = nextInvertibleDecimal(bound, scale);
        if (decimal.compareTo(BigDecimal.ZERO) > 0) {
            return decimal.negate();
        }
        return decimal;
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
     *             if {@code scale < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public BigDecimal nextInvertibleDecimal(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal decimal = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
        return keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal keepDecimalInBound(final BigDecimal decimal, final long bound) {
        requireNonNull(decimal, "decimal");
        checkArgument(bound > 0, "expected > 0 but actual %s", bound);
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextPositiveDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextPositiveDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextPositiveDecimal(bound, scale));
        }
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextNegativeDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextNegativeDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextNegativeDecimal(bound, scale));
        }
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextDecimal(bound, scale));
        }
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertiblePositiveDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextInvertiblePositiveDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextInvertiblePositiveDecimal(bound, scale));
        }
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleNegativeDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextInvertibleNegativeDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextInvertibleNegativeDecimal(bound, scale));
        }
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
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextInvertibleDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigDecimal> nextInvertibleDecimals(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigDecimal> decimals = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            decimals.add(nextInvertibleDecimal(bound, scale));
        }
        return decimals;
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by
     * {@code 0} (inclusive) and above by {@code bound} (exclusive) and whose
     * {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound}
     * (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     */
    public Fraction nextPositiveFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger numerator = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
        final BigInteger denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        return new Fraction(numerator, denominator);
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
     * @author Lars Tennstedt
     */
    public Fraction nextNegativeFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        return nextPositiveFraction(bound).negate();
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive) and whose
     * {@code denominator} is bounded below {@code -bound} (exclusive) and
     * {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException
     *             if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     */
    public Fraction nextFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        if (random.nextBoolean()) {
            return nextNegativeFraction(bound);
        }
        return nextPositiveFraction(bound);
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextPositiveFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextPositiveFraction(bound));
        }
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextNegativeFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextNegativeFraction(bound));
        }
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextFraction(bound));
        }
        return fractions;
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
     * @author Lars Tennstedt
     */
    public Fraction nextInvertiblePositiveFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger numerator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        final BigInteger denominator = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        return new Fraction(numerator, denominator);
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
     * @author Lars Tennstedt
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
     * @author Lars Tennstedt
     */
    public Fraction nextInvertibleFraction(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        if (random.nextBoolean()) {
            return nextInvertibleNegativeFraction(bound);
        }
        return nextInvertiblePositiveFraction(bound);
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextInvertiblePositiveFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextInvertiblePositiveFraction(bound));
        }
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextInvertibleNegativeFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextInvertibleNegativeFraction(bound));
        }
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
     * @author Lars Tennstedt
     */
    public List<Fraction> nextInvertibleFractions(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<Fraction> fractions = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            fractions.add(nextInvertibleFraction(bound));
        }
        return fractions;
    }

    /**
     * Returns a {@link SimpleComplexNumber} whose {@code real} and
     * {@code imaginary} part are bounded below by {@code -bound} (exclusive) and
     * above by {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public SimpleComplexNumber nextSimpleComplexNumber(final long bound) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        final BigInteger real = BigInteger.valueOf(nextLong(bound));
        final BigInteger imaginary = BigInteger.valueOf(nextLong(bound));
        return new SimpleComplexNumber(real, imaginary);
    }

    /**
     * Returns a {@link SimpleComplexNumber} which is invertible
     *
     * @param bound
     *            the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @see #nextSimpleComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    public SimpleComplexNumber nextInvertibleSimpleComplexNumber(final long bound) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        final BigInteger nonZeroPart = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
        final long possibleZeroPart = random.nextBoolean() ? RandomUtils.nextLong(1, bound) : nextLong(bound);
        if (random.nextBoolean()) {
            return new SimpleComplexNumber(BigInteger.valueOf(possibleZeroPart), nonZeroPart);
        }
        return new SimpleComplexNumber(nonZeroPart, BigInteger.valueOf(possibleZeroPart));
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
     * @author Lars Tennstedt
     */
    public List<SimpleComplexNumber> nextSimpleComplexNumbers(final long bound, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(nextSimpleComplexNumber(bound));
        }
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
     * @author Lars Tennstedt
     */
    public List<SimpleComplexNumber> nextInvertibleSimpleComplexNumbers(final long bound, final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(nextInvertibleSimpleComplexNumber(bound));
        }
        return complexNumbers;
    }

    /**
     * Returns a {@link RealComplexNumber} whose {@code real} and {@code imaginary}
     * part are bounded below by {@code -bound} (exclusive) and above by
     * {@code bound} (exclusive)
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link RealComplexNumber}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    public RealComplexNumber nextRealComplexNumber(final long bound, final int scale) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal real = nextDecimal(bound, scale);
        final BigDecimal imaginary = nextDecimal(bound, scale);
        return new RealComplexNumber(real, imaginary);
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
     * @author Lars Tennstedt
     */
    public RealComplexNumber nextInvertibleRealComplexNumber(final long bound, final int scale) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        final BigDecimal nonZeroPart = nextInvertibleDecimal(bound, scale);
        final BigDecimal possibleZeroPart = random.nextBoolean() ? nextInvertibleDecimal(bound, scale)
                : nextDecimal(bound, scale);
        if (random.nextBoolean()) {
            return new RealComplexNumber(possibleZeroPart, nonZeroPart);
        }
        return new RealComplexNumber(nonZeroPart, possibleZeroPart);
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
     *             if {@code howMany < 2}
     * @see #nextRealComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    public List<RealComplexNumber> nextRealComplexNumbers(final long bound, final int scale, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<RealComplexNumber> complexNumbers = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(nextRealComplexNumber(bound, scale));
        }
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
     *             if {@code howMany < 2}
     * @see #nextInvertibleRealComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    public List<RealComplexNumber> nextInvertibleRealComplexNumbers(final long bound, final int scale,
            final int howMany) {
        checkArgument(bound > 1, "expected bound > 1 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<RealComplexNumber> complexNumbers = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(nextInvertibleRealComplexNumber(bound, scale));
        }
        return complexNumbers;
    }

    /**
     * Returns a {@link BigIntVector}
     *
     * @param bound
     *            the bound
     * @param size
     *            the size of the resulting {@link BigIntVector}
     * @return A pseudo random {@link BigIntVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
     */
    public BigIntVector nextBigIntVector(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntVectorBuilder builder = BigIntVector.builder(size);
        for (int i = 0; i < size; i++) {
            builder.put(BigInteger.valueOf(nextLong(bound)));
        }
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigIntVector BigIntVectors}
     *
     * @param bound
     *            the bound
     * @param size
     *            the sizes of the resulting {@link BigIntVector BigIntVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntVector BigIntVectors}
     * @throws IllegalArgumentException
     *             if {@code  bound < 2}
     * @throws IllegalArgumentException
     *             if {@code howMany < 2}
     * @see #nextBigIntVector
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigIntVector> nextBigIntVectors(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntVector> vectors = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            vectors.add(nextBigIntVector(bound, size));
        }
        return vectors;
    }

    /**
     * Returns a {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link BigIntMatrix}
     * @param columnSize
     *            the column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
     */
    public BigIntMatrix nextBigIntMatrix(final long bound, final int rowSize, final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                builder.put(rowIndex, columnIndex, BigInteger.valueOf(nextLong(bound)));
            });
        });
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of resulting {@link BigIntMatrix}
     * @return A pseudo random upper triangular {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     * @see BigIntMatrix#upperTriangular
     */
    public BigIntMatrix nextUpperTriangularBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex <= columnIndex) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(nextLong(bound)));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random lower triangular {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     * @see BigIntMatrix#lowerTriangular
     */
    public BigIntMatrix nextLowerTriangularBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex >= columnIndex) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(nextLong(bound)));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a triangular {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random triangular {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularBigIntMatrix
     * @see #nextLowerTriangularBigIntMatrix
     * @see BigIntMatrix#triangular
     */
    public BigIntMatrix nextTriangularBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        if (random.nextBoolean()) {
            return nextLowerTriangularBigIntMatrix(bound, size);
        }
        return nextUpperTriangularBigIntMatrix(bound, size);
    }

    /**
     * Returns a diagonal {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random diagonal {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#diagonal
     */
    public BigIntMatrix nextDiagonalBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(nextLong(bound)));
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a symmetric {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random symmetric {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#symmetric
     */
    public BigIntMatrix nextSymmetricBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigInteger element = BigInteger.valueOf(nextLong(bound));
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, element);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link BigIntMatrix}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random skew-symmetric {@link BigIntMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#skewSymmetric
     */
    public BigIntMatrix nextSkewSymmetricBigIntMatrix(final long bound, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigInteger element = BigInteger.valueOf(nextLong(bound));
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param rowSize
     *            the row size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param columnSize
     *            the column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextBigIntMatrix
     * @since 1
     * @author Lars Tennstedt
     */
    public List<BigIntMatrix> nextBigIntMatrices(final long bound, final int rowSize, final int columnSize,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextBigIntMatrix(bound, rowSize, columnSize));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularBigIntMatrix
     */
    public List<BigIntMatrix> nextUpperTriangularBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextUpperTriangularBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLowerTriangularBigIntMatrix
     */
    public List<BigIntMatrix> nextLowerTriangularBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextLowerTriangularBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextTriangularBigIntMatrix
     */
    public List<BigIntMatrix> nextTriangularBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextTriangularBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDiagonalBigIntMatrix
     */
    public List<BigIntMatrix> nextDiagonalBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextDiagonalBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSymmetricBigIntMatrix
     */
    public List<BigIntMatrix> nextSymmetricBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextSymmetricBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link BigIntMatrix BigIntMatrices}
     *
     * @param bound
     *            the bound
     * @param size
     *            the row and column size of the resulting {@link BigIntMatrix
     *            BigIntMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric {@link BigIntMatrix
     *         BigIntMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSkewSymmetricBigIntMatrix
     */
    public List<BigIntMatrix> nextSkewSymmetricBigIntMatrices(final long bound, final int size, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextSkewSymmetricBigIntMatrix(bound, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link DecimalVector}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size
     *            the size of the resulting {@link DecimalVector}
     * @return A speudo random {@link DecimalVector}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public DecimalVector nextDecimalVector(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalVectorBuilder builder = DecimalVector.builder(size);
        for (int i = 0; i < size; i++) {
            builder.put(nextDecimal(bound, scale));
        }
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link DecimalVector DecimalVectors}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size
     *            the size of the resulting {@link DecimalVector DecimalVectors}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link DecimalVector DecimalVectors}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDecimalVector
     * @since 1
     * @author Lars Tennstedt
     */
    public List<DecimalVector> nextDecimalVectors(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalVector> vectors = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            vectors.add(nextDecimalVector(bound, scale, size));
        }
        return vectors;
    }

    /**
     * Returns a {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param rowSize
     *            the row size of the resulting {@link DecimalMatrix}
     * @param columnSize
     *            the column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    public DecimalMatrix nextDecimalMatrix(final long bound, final int scale, final int rowSize, final int columnSize) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(rowSize, columnSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                builder.put(rowIndex, columnIndex, nextDecimal(bound, scale));
            });
        });
        return builder.build();
    }

    /**
     * Returns an upper triangular {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of resulting {@link DecimalMatrix}
     * @return A pseudo random upper triangular {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     * @see DecimalMatrix#upperTriangular
     */
    public DecimalMatrix nextUpperTriangularDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex <= columnIndex) {
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns an lower triangular {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random lower triangular {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     * @see DecimalMatrix#lowerTriangular
     */
    public DecimalMatrix nextLowerTriangularDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex >= columnIndex) {
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a triangular {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random triangular {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularDecimalMatrix
     * @see #nextLowerTriangularDecimalMatrix
     * @see DecimalMatrix#triangular
     */
    public DecimalMatrix nextTriangularDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        if (random.nextBoolean()) {
            return nextLowerTriangularDecimalMatrix(bound, scale, size);
        }
        return nextUpperTriangularDecimalMatrix(bound, scale, size);
    }

    /**
     * Returns a diagonal {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random diagonal {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#diagonal
     */
    public DecimalMatrix nextDiagonalDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale));
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a symmetric {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random symmetric {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#symmetric
     */
    public DecimalMatrix nextSymmetricDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigDecimal element = nextDecimal(bound, scale);
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, element);
                }
            });
        });
        return builder.build();
    }

    /**
     * Returns a skew-symmetric {@link DecimalMatrix}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random skew-symmetric {@link DecimalMatrix}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#skewSymmetric
     */
    public DecimalMatrix nextSkewSymmetricDecimalMatrix(final long bound, final int scale, final int size) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigDecimal element = nextDecimal(bound, scale);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });

        });
        return builder.build();
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale to be set on the {@link BigDecimal BigDecimals}
     * @param rowSize
     *            the row size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param columnSize
     *            the column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code rowSize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnSize < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @see #nextDecimalMatrix
     * @since 1
     * @author Lars Tennstedt
     */
    public List<DecimalMatrix> nextDecimalMatrices(final long bound, final int scale, final int rowSize,
            final int columnSize, final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextDecimalMatrix(bound, scale, rowSize, columnSize));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper
     * triangular {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularDecimalMatrix
     */
    public List<DecimalMatrix> nextUpperTriangularDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextUpperTriangularDecimalMatrix(bound, scale, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower
     * triangular {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLowerTriangularDecimalMatrix
     */
    public List<DecimalMatrix> nextLowerTriangularDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextLowerTriangularDecimalMatrix(bound, scale, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular
     * {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextTriangularDecimalMatrix
     */
    public List<DecimalMatrix> nextTriangularDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextTriangularDecimalMatrix(bound, scale, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal
     * {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDiagonalDecimalMatrix
     */
    public List<DecimalMatrix> nextDiagonalDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextDiagonalDecimalMatrix(bound, scale, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric
     * {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSymmetricDecimalMatrix
     */
    public List<DecimalMatrix> nextSymmetricDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextSymmetricDecimalMatrix(bound, scale, size));
        }
        return matrices;
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing
     * skew-symmetric {@link DecimalMatrix DecimalMatrices}
     *
     * @param bound
     *            the bound
     * @param scale
     *            the scale
     * @param size
     *            the row and column size of the resulting {@link DecimalMatrix
     *            DecimalMatrices}
     * @param howMany
     *            the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric {@link DecimalMatrix
     *         DecimalMatrices}
     * @throws IllegalArgumentException
     *             if {@code bound < 1}
     * @throws IllegalArgumentException
     *             if {@code scale < 1}
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @throws IllegalArgumentException
     *             if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSkewSymmetricDecimalMatrix
     */
    public List<DecimalMatrix> nextSkewSymmetricDecimalMatrices(final long bound, final int scale, final int size,
            final int howMany) {
        checkArgument(bound > 0, "expected bound > 0 but actual %s", bound);
        checkArgument(scale > 0, "expected scale > 0 but actual %s", scale);
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        checkArgument(howMany > 1, "expected howMany > 1 but actual %s", howMany);
        final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
        for (int i = 0; i < howMany; i++) {
            matrices.add(nextSkewSymmetricDecimalMatrix(bound, scale, size));
        }
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
