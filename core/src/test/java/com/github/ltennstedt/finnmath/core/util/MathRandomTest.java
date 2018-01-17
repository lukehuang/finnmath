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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalVector;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberVector;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberVector;
import com.github.ltennstedt.finnmath.core.number.Fraction;
import com.github.ltennstedt.finnmath.core.number.PolarForm;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.assertj.core.api.Condition;
import org.junit.Test;

public final class MathRandomTest {
    private final int bound = 10;
    private final int howMany = 10;
    private final int validScale = 2;
    private final int validSize = 3;
    private final int validRowSize = 2;
    private final int validColumnSize = 3;
    private final MathRandom mathRandom = new MathRandom();
    private final BigDecimal decimalBound = BigDecimal.valueOf(bound);
    private final BigDecimal negatedBigDecimalBound = decimalBound.negate();
    private final BigInteger bigBound = BigInteger.valueOf(bound);
    private final BigInteger negatedBigBound = bigBound.negate();
    private final Condition<BigDecimal> positive =
            new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) > -1, "positive");
    private final Condition<BigDecimal> negative =
            new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) < 1, "negative");
    private final Condition<BigDecimal> upperBound =
            new Condition<>(decimal -> decimal.compareTo(decimalBound) < 0, "upper bound");
    private final Condition<BigDecimal> lowerBound =
            new Condition<>(decimal -> decimal.compareTo(negatedBigDecimalBound) > 0, "lower bound");
    private final Condition<BigDecimal> scaled = new Condition<>(decimal -> decimal.scale() == validScale, "scaled");

    @Test
    public void nextPositiveBigIntegerBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigInteger(0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPositiveBigIntegerShouldSucceed() {
        final BigInteger integer = mathRandom.nextPositiveBigInteger(bound);
        assertThat(integer).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextNegativeBigIntegerBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigInteger(0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextNegativeBigIntegerShouldSucceed() {
        final BigInteger integer = mathRandom.nextNegativeBigInteger(bound);
        assertThat(integer).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(BigInteger.ZERO);
    }

    @Test
    public void nextBigIntegerBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigInteger(0)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigIntegerShouldSucceed() {
        final BigInteger integer = mathRandom.nextBigInteger(bound);
        assertThat(integer).isGreaterThan(negatedBigBound).isLessThan(bigBound);
    }

    @Test
    public void nextPositiveBigIntegersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigIntegers(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextPositiveBigIntegersShouldSucceed() {
        final List<BigInteger> integers = mathRandom.nextPositiveBigIntegers(bound, howMany);
        assertThat(integers).hasSize(howMany);
        integers.forEach(integer -> assertThat(integer).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound));
    }

    @Test
    public void nextNegativeBigIntegersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigIntegers(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextNegativeBigIntegersShouldSucceed() {
        final List<BigInteger> integers = mathRandom.nextNegativeBigIntegers(bound, howMany);
        assertThat(integers).hasSize(howMany);
        integers.forEach(
                integer -> assertThat(integer).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(BigInteger.ZERO));
    }

    @Test
    public void nextBigIntegersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegers(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextBigIntegersShouldSucceed() {
        final List<BigInteger> integers = mathRandom.nextBigIntegers(bound, howMany);
        assertThat(integers).hasSize(howMany);
        integers.forEach(integer -> assertThat(integer).isStrictlyBetween(negatedBigBound, bigBound));
    }

    @Test
    public void nextPositiveBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigDecimal(0, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextPositiveBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextPositiveBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextPositiveBigDecimal(bound, validScale);
        assertThat(actual).isGreaterThanOrEqualTo(BigDecimal.ZERO).isLessThan(decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextNegativeBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigDecimal(0, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextNegativeBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextNegativeBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextNegativeBigDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedBigDecimalBound).isLessThanOrEqualTo(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimal(0, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextBigDecimal(bound, validScale);
        assertThat(actual).isStrictlyBetween(negatedBigDecimalBound, decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertiblePositiveBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveBigDecimal(1, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertiblePositiveBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertiblePositiveBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertiblePositiveBigDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(BigDecimal.ZERO).isLessThan(decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertibleNegativeBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeBigDecimal(1, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleNegativeBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertibleNegativeBigDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedBigDecimalBound).isLessThan(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertibleBigDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleBigDecimal(1, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleBigDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleBigDecimal(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextInvertibleBigDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertibleBigDecimal(bound, validScale);
        assertThat(actual).isStrictlyBetween(negatedBigDecimalBound, decimalBound)
                .isNotEqualByComparingTo(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void keepBigDecimalInBoundBigDecimalGreaterThanOrEqualToZeroAndBigDecimalGreaterThanOrEqualToBound() {
        final BigDecimal expected = mathRandom.nextPositiveBigDecimal(bound, validScale);
        final BigDecimal decimal = expected.add(decimalBound);
        assertThat(mathRandom.keepBigDecimalInBound(decimal, bound)).isEqualByComparingTo(expected);
    }

    @Test
    public void keepBigDecimalInBoundBigDecimalGreaterThanOrEqualToZeroAndBigDecimalLowerThanBound() {
        final BigDecimal decimal = mathRandom.nextPositiveBigDecimal(bound, validScale);
        assertThat(mathRandom.keepBigDecimalInBound(decimal, bound)).isEqualByComparingTo(decimal);
    }

    @Test
    public void keepBigDecimalInBoundBigDecimalLowerThanZeroAndBigDecimalLowerThanOrEqualToMinusBound() {
        final BigDecimal expected = mathRandom.nextNegativeBigDecimal(bound, validScale);
        final BigDecimal decimal = expected.subtract(decimalBound);
        assertThat(mathRandom.keepBigDecimalInBound(decimal, bound)).isEqualByComparingTo(expected);
    }

    @Test
    public void keepBigDecimalInBoundBigDecimalLowerThanZeroAndBigDecimalGreaterThanMinusBound() {
        final BigDecimal decimal = mathRandom.nextNegativeBigDecimal(bound, validScale);
        assertThat(mathRandom.keepBigDecimalInBound(decimal, bound)).isEqualByComparingTo(decimal);
    }

    @Test
    public void nextPositiveBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigDecimals(0, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPositiveBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextPositiveBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextPositiveBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextPositiveBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(positive).are(upperBound).are(scaled);
    }

    @Test
    public void nextNegativeBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigDecimals(0, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextNegativeBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextNegativeBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextNegativeBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextNegativeBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound).are(negative).are(scaled);
    }

    @Test
    public void nextBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimals(0, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound).are(upperBound).are(scaled);
    }

    @Test
    public void nextInvertiblePositiveBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveBigDecimals(1, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertiblePositiveBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertiblePositiveBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertiblePositiveBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertiblePositiveBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) > 0, "lower bound")).are(upperBound)
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextInvertibleNegativeBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeBigDecimals(1, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextInvertibleNegativeBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertibleNegativeBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertibleNegativeBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) < 0, "upper bound"))
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextInvertibleBigDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleBigDecimals(1, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleBigDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleBigDecimals(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextInvertibleBigDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleBigDecimals(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertibleBigDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertibleBigDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound).are(upperBound)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) != 0, "not zero"))
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextPositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveFraction(1)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextPositiveFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextPositiveFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeFraction(1)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextNegativeFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextNegativeFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(BigInteger.ZERO);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextFraction(1)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(bigBound);
    }

    @Test
    public void nextInvertiblePositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveFraction(1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertiblePositiveFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextInvertiblePositiveFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(BigInteger.ZERO).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeFraction(1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextInvertibleNegativeFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThan(BigInteger.ZERO);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleFraction(1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextInvertibleFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThan(bigBound)
                .isNotEqualTo(BigInteger.ZERO);
        assertThat(fraction.getDenominator()).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(bigBound);
    }

    @Test
    public void nextPositiveFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextPositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPositiveFractions(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextPositiveFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextPositiveFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(bigBound) < 0,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextNegativeFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextNegativeFractions(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextNegativeFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextNegativeFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(negatedBigBound) > 0,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(BigInteger.ZERO) < 1,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextFractions(bound, 0)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(negatedBigBound) > 0,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(bigBound) < 0,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(negatedBigBound) > 0,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextInvertiblePositiveFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertiblePositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertiblePositiveFractions(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertiblePositiveFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextInvertiblePositiveFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(bigBound) < 0,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextInvertibleNegativeFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleNegativeFractions(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertibleNegativeFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextInvertibleNegativeFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(negatedBigBound) > 0,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(BigInteger.ZERO) < 1,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(BigInteger.ZERO) > -1,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextInvertibleFractionsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleFractions(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleFractions(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertibleFractionsShouldSucceed() {
        final List<Fraction> fractions = mathRandom.nextInvertibleFractions(bound, howMany);
        assertThat(fractions).hasSize(howMany)
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(negatedBigBound) > 0,
                        "lower bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getNumerator().compareTo(bigBound) < 0,
                        "upper bound (numerator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(negatedBigBound) > 0,
                        "lower bound (denominator)"))
                .are(new Condition<>(fraction -> fraction.getDenominator().compareTo(bigBound) < 0,
                        "upper bound (denominator)"));
    }

    @Test
    public void nextSimpleComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumber(0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberShouldSucceed() {
        final SimpleComplexNumber actual = mathRandom.nextSimpleComplexNumber(bound);
        assertThat(actual.getReal()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(actual.getImaginary()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleSimpleComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleSimpleComplexNumber(1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleSimpleComplexNumberShouldSucceed() {
        final SimpleComplexNumber actual = mathRandom.nextInvertibleSimpleComplexNumber(bound);
        assertThat(actual.getReal()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(actual.getImaginary()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(actual).isNotEqualTo(SimpleComplexNumber.ZERO);
    }

    @Test
    public void nextSimpleComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumbers(0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumbers(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumbersShouldSucceed() {
        final List<SimpleComplexNumber> complexNumbers = mathRandom.nextSimpleComplexNumbers(bound, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(bigBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
    }

    @Test
    public void nextInvertibleSimpleComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleSimpleComplexNumbers(1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleSimpleComplexNumbers(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertibleSimpleComplexNumbersShouldSucceed() {
        final List<SimpleComplexNumber> complexNumbers = mathRandom.nextInvertibleSimpleComplexNumbers(bound, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(bigBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> !complexNumber.equals(SimpleComplexNumber.ZERO), "invertible"));
    }

    @Test
    public void nextRealComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumber(0, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumber(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumberShouldSucceed() {
        final RealComplexNumber actual = mathRandom.nextRealComplexNumber(bound, validScale);
        assertThat(actual.getReal()).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (real part)"));
        assertThat(actual.getImaginary()).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (imaginary part)"));
    }

    @Test
    public void nextInvertibleRealComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleRealComplexNumber(1, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleRealComplexNumberScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleRealComplexNumber(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleRealComplexNumberShouldSucceed() {
        final RealComplexNumber actual = mathRandom.nextInvertibleRealComplexNumber(bound, validScale);
        assertThat(actual.getReal()).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (real part)"));
        assertThat(actual.getImaginary()).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (imaginary part)"));
        assertThat((actual.getReal().compareTo(BigDecimal.ZERO) != 0) ||
                (actual.getImaginary().compareTo(BigDecimal.ZERO) != 0)).isTrue();
    }

    @Test
    public void nextRealComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumbers(0, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumbersScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumbers(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumbers(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumbersShouldSucceed() {
        final List<RealComplexNumber> complexNumbers = mathRandom.nextRealComplexNumbers(bound, validScale, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)")).are(new Condition<>(
                complexNumber -> complexNumber.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                "lower bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().scale() == validScale,
                        "scaled (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().scale() == validScale,
                        "scaled (imaginary part)"));
    }

    @Test
    public void nextInvertibleRealComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleRealComplexNumbers(1, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleRealComplexNumbersScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleRealComplexNumbers(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> mathRandom.nextInvertibleRealComplexNumbers(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertibleRealComplexNumbersShouldSucceed() {
        final List<RealComplexNumber> complexNumbers =
                mathRandom.nextInvertibleRealComplexNumbers(bound, validScale, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)")).are(new Condition<>(
                complexNumber -> complexNumber.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                "lower bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().scale() == validScale,
                        "scale of the real part"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().scale() == validScale,
                        "scale of the imaginary part")).are(new Condition<>(
                complexNumber -> (complexNumber.getReal().compareTo(BigDecimal.ZERO) != 0) ||
                        (complexNumber.getImaginary().compareTo(BigDecimal.ZERO) != 0), "invertible"));
    }

    @Test
    public void nextPolarFormBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPolarForm(0, validScale))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPolarFormScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPolarForm(bound, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextPolarFormShouldSucceed() {
        final PolarForm actual = mathRandom.nextPolarForm(bound, validScale);
        final BigDecimal radial = actual.getRadial();
        assertThat(radial).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound);
        assertThat(radial.scale()).isEqualTo(validScale);
        final BigDecimal angular = actual.getAngular();
        assertThat(angular).isGreaterThan(negatedBigDecimalBound).isLessThan(decimalBound);
        assertThat(angular.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextPolarFormsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPolarForms(0, validScale, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPolarFormsScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPolarForms(bound, -1, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextPolarFormsTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextPolarForms(bound, validScale, -1))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual -1");
    }

    @Test
    public void nextPolarFormsShouldSucceed() {
        assertThat(mathRandom.nextPolarForms(bound, validScale, howMany)).hasOnlyElementsOfType(PolarForm.class)
                .hasSize(howMany)
                .are(new Condition<>(polarForm -> polarForm.getRadial().compareTo(negatedBigDecimalBound) > 0,
                        "radial lower bound"))
                .are(new Condition<>(polarForm -> polarForm.getRadial().compareTo(decimalBound) < 0,
                        "radial upper bound"))
                .are(new Condition<>(polarForm -> polarForm.getRadial().scale() == validScale, "radial scaled"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().compareTo(negatedBigDecimalBound) > 0,
                        "angular lower bound"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().compareTo(decimalBound) < 0,
                        "angluar upper bound"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().scale() == validScale, "angular scaled"));
    }

    @Test
    public void nextBigIntegerVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerVector(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerVector(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextBigIntegerVectorShouldSucceed() {
        final BigIntegerVector actual = mathRandom.nextBigIntegerVector(bound, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
    }

    @Test
    public void nextBigIntegerVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerVectors(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigIntegerVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerVectors(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextBigIntegerVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerVectors(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextBigIntegerVectorsShouldSucceed() {
        final List<BigIntegerVector> vectors = mathRandom.nextBigIntegerVectors(bound, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> assertThat(vector.getMap().values())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrix(0, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrix(bound, 0, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrix(bound, validRowSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextBigIntegerMatrix(bound, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualByComparingTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextUpperTriangularBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.upperTriangular()).isTrue();
    }

    @Test
    public void nextLowerTriangularBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextLowerTriangularBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.lowerTriangular()).isTrue();
    }

    @Test
    public void nextTriangularBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextTriangularBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.triangular()).isTrue();
    }

    @Test
    public void nextDiagonalBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextDiagonalBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.diagonal()).isTrue();
    }

    @Test
    public void nextSymmetricBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextSymmetricBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigIntegerMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigIntegerMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix matrix = mathRandom.nextSkewSymmetricBigIntegerMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.skewSymmetric()).isTrue();
    }

    @Test
    public void nextBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrices(0, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrices(bound, 0, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrices(bound, validRowSize, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigIntegerMatrices(bound, validRowSize, validColumnSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices =
                mathRandom.nextBigIntegerMatrices(bound, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextUpperTriangularBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices =
                mathRandom.nextUpperTriangularBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(BigIntegerMatrix::upperTriangular, "upper triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextLowerTriangularBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices =
                mathRandom.nextLowerTriangularBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(BigIntegerMatrix::lowerTriangular, "lower triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextTriangularBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices = mathRandom.nextTriangularBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextDiagonalBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices = mathRandom.nextDiagonalBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextSymmetricBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices = mathRandom.nextSymmetricBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigIntegerMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigIntegerMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigIntegerMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntegerMatricesShouldSucceed() {
        final List<BigIntegerMatrix> matrices =
                mathRandom.nextSkewSymmetricBigIntegerMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextBigDecimalVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVector(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigDecimalVectorScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVector(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextBigDecimalVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVector(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void nextBigDecimalVectorShouldSucceed() {
        final BigDecimalVector actual = mathRandom.nextBigDecimalVector(bound, validScale, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element.compareTo(BigDecimal.ZERO) > -1, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                .are(new Condition<>(element -> element.scale() == validScale, "scale of the element"));
    }

    @Test
    public void nextBigDecimalVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVectors(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigDecimalVectorsScaleTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVectors(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextBigDecimalVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVectors(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextBigDecimalVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalVectors(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextBigDecimalVectorsShouldSucceed() {
        final List<BigDecimalVector> vectors = mathRandom.nextBigDecimalVectors(bound, validScale, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> assertThat(vector.getMap().values())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                .are(new Condition<>(element -> element.scale() == validScale, "scale of the element")));
    }

    @Test
    public void nextBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrix(0, validScale, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrix(bound, -1, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextBigDecimalMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrix(bound, validScale, 0, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrix(bound, validScale, validRowSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix =
                mathRandom.nextBigDecimalMatrix(bound, validScale, validRowSize, validColumnSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                .are(new Condition<>(element -> element.scale() == validScale, "scale of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextUpperTriangularBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey() <= cell.getColumnKey()) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.upperTriangular()).isTrue();
    }

    @Test
    public void nextLowerTriangularBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextLowerTriangularBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey() >= cell.getColumnKey()) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.lowerTriangular()).isTrue();
    }

    @Test
    public void nextTriangularBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextTriangularBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.cells().forEach(cell -> {
            if (matrix.upperTriangular() && (cell.getRowKey() <= cell.getColumnKey())) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
            if (matrix.lowerTriangular() && (cell.getRowKey() >= cell.getColumnKey())) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.triangular()).isTrue();
    }

    @Test
    public void nextDiagonalBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextDiagonalBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey().equals(cell.getColumnKey())) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.diagonal()).isTrue();
    }

    @Test
    public void nextSymmetricBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextSymmetricBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.elements().forEach(element -> assertThat(element.scale()).isEqualTo(validScale));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix matrix = mathRandom.nextSkewSymmetricBigDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.cells().forEach(cell -> {
            if (!cell.getRowKey().equals(cell.getColumnKey())) {
                assertThat(cell.getValue().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.skewSymmetric()).isTrue();
    }

    @Test
    public void nextBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextBigDecimalMatrices(0, validScale, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrices(bound, -1, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextBigDecimalMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrices(bound, validScale, 0, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrices(bound, validScale, validRowSize, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextBigDecimalMatrices(bound, validScale, validRowSize, validColumnSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextBigDecimalMatrices(bound, validScale, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextUpperTriangularBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextUpperTriangularBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(BigDecimalMatrix::upperTriangular, "upper triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextLowerTriangularBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextLowerTriangularBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(BigDecimalMatrix::lowerTriangular, "lower triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextTriangularBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextTriangularBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextDiagonalBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextDiagonalBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextSymmetricBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextSymmetricBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricBigDecimalMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigDecimalMatricesShouldSucceed() {
        final List<BigDecimalMatrix> matrices =
                mathRandom.nextSkewSymmetricBigDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element")));
    }

    @Test
    public void nextSimpleComplexNumberVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberVector(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberVector(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorShouldSucceed() {
        final SimpleComplexNumberVector actual = mathRandom.nextSimpleComplexNumberVector(bound, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
    }

    @Test
    public void nextSimpleComplexNumberVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberVectors(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberVectors(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberVectors(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsShouldSucceed() {
        final List<SimpleComplexNumberVector> vectors =
                mathRandom.nextSimpleComplexNumberVectors(bound, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> assertThat(vector.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrix(0, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrix(bound, 0, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrix(bound, validRowSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextSimpleComplexNumberMatrix(bound, validRowSize, validColumnSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualByComparingTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.upperTriangular()).isTrue();
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.lowerTriangular()).isTrue();
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.triangular()).isTrue();
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextDiagonalSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.diagonal()).isTrue();
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextSymmetricSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(0, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(bound, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements()).are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.skewSymmetric()).isTrue();
    }

    @Test
    public void nextSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrices(0, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrices(bound, 0, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, validColumnSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(SimpleComplexNumberMatrix::upperTriangular, "upper triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(SimpleComplexNumberMatrix::lowerTriangular, "lower triangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(0, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0, "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                        "upper bound (imaginary part)")));
    }

    @Test
    public void nextRealComplexNumberVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVector(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVector(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextRealComplexNumberVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVector(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void nextRealComplexNumberVectorShouldSucceed() {
        final RealComplexNumberVector actual = mathRandom.nextRealComplexNumberVector(bound, validScale, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale, "scaled (real part)"));
    }

    @Test
    public void nextRealComplexNumberVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVectors(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsScaleTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVectors(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumberVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVectors(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberVectors(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsShouldSucceed() {
        final List<RealComplexNumberVector> vectors =
                mathRandom.nextRealComplexNumberVectors(bound, validScale, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> assertThat(vector.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale, "scaled (real part)")));
    }

    @Test
    public void nextRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberMatrix(0, validScale, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberMatrix(bound, -1, validRowSize, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextRealComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberMatrix(bound, validScale, 0, validColumnSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberMatrix(bound, validScale, validRowSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextRealComplexNumberMatrix(bound, validScale, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale, "scaled (real part)"));
        assertThat(matrix.rowSize()).isEqualTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey() <= cell.getColumnKey()) {
                assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.upperTriangular()).isTrue();
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey() >= cell.getColumnKey()) {
                assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.lowerTriangular()).isTrue();
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"));
        matrix.cells().forEach(cell -> {
            if ((matrix.upperTriangular() && (cell.getRowKey() <= cell.getColumnKey())) ||
                    (matrix.lowerTriangular() && (cell.getRowKey() >= cell.getColumnKey()))) {
                assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.triangular()).isTrue();
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextDiagonalRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"));
        matrix.cells().forEach(cell -> {
            if (cell.getRowKey().equals(cell.getColumnKey())) {
                assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.diagonal()).isTrue();
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextSymmetricRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"));
        matrix.elements().forEach(element -> {
            assertThat(element.getReal().scale()).isEqualTo(validScale);
            assertThat(element.getImaginary().scale()).isEqualTo(validScale);
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrix(0, validScale, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, -1, validSize))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, validScale, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"));
        matrix.cells().forEach(cell -> {
            if (!cell.getRowKey().equals(cell.getColumnKey())) {
                assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
            }
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.skewSymmetric()).isTrue();
    }

    @Test
    public void nextRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextRealComplexNumberMatrices(0, validScale, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextRealComplexNumberMatrices(bound, -1, validRowSize, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextRealComplexNumberMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextRealComplexNumberMatrices(bound, validScale, 0, validColumnSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, validColumnSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale,
                        "scaled (imaginary part)")));
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextUpperTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(RealComplexNumberMatrix::upperTriangular, "upper triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if (cell.getRowKey() <= cell.getColumnKey()) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextLowerTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(RealComplexNumberMatrix::lowerTriangular, "lower triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if (cell.getRowKey() >= cell.getColumnKey()) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextTriangularRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if ((matrix.upperTriangular() && (cell.getRowKey() <= cell.getColumnKey())) ||
                        (matrix.lowerTriangular() && (cell.getRowKey() >= cell.getColumnKey()))) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextDiagonalRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextDiagonalRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSymmetricRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextSymmetricRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.elements().forEach(element -> {
                assertThat(element.getReal().scale()).isEqualTo(validScale);
                assertThat(element.getImaginary().scale()).isEqualTo(validScale);
            });
        });
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(
                () -> mathRandom.nextSkewSymmetricRealComplexNumberMatrices(0, validScale, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, -1, validSize, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, validScale, 0, howMany))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, validScale, validSize, 0))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if (!cell.getRowKey().equals(cell.getColumnKey())) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void toStringShouldSucceed() {
        assertThat(mathRandom.toString())
                .isEqualTo(MoreObjects.toStringHelper(mathRandom).add("random", mathRandom.getRandom()).toString());
    }
}
