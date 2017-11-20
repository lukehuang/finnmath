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

package com.github.ltennstedt.finnmath.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.linear.BigIntMatrix;
import com.github.ltennstedt.finnmath.linear.BigIntVector;
import com.github.ltennstedt.finnmath.linear.DecimalMatrix;
import com.github.ltennstedt.finnmath.linear.DecimalVector;
import com.github.ltennstedt.finnmath.linear.RealComplexNumberMatrix;
import com.github.ltennstedt.finnmath.linear.RealComplexNumberVector;
import com.github.ltennstedt.finnmath.linear.SimpleComplexNumberMatrix;
import com.github.ltennstedt.finnmath.linear.SimpleComplexNumberVector;
import com.github.ltennstedt.finnmath.number.Fraction;
import com.github.ltennstedt.finnmath.number.PolarForm;
import com.github.ltennstedt.finnmath.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.number.SimpleComplexNumber;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.assertj.core.api.Condition;
import org.junit.Test;

public final class MathRandomTest {
    private static final int bound = 10;
    private static final int howMany = 10;
    private static final int validScale = 2;
    private static final int validSize = 3;
    private static final int validRowSize = 2;
    private static final int validColumnSize = 3;
    private static final MathRandom mathRandom = new MathRandom();
    private static final BigDecimal decimalBound = BigDecimal.valueOf(bound);
    private static final BigDecimal negatedDecimalBound = decimalBound.negate();
    private static final BigInteger bigBound = BigInteger.valueOf(bound);
    private static final BigInteger negatedBigBound = bigBound.negate();
    private static final Condition<BigDecimal> positive =
            new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) > -1, "positive");
    private static final Condition<BigDecimal> negative =
            new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) < 1, "negative");
    private static final Condition<BigDecimal> upperBound =
            new Condition<>(decimal -> decimal.compareTo(decimalBound) < 0, "upper bound");
    private static final Condition<BigDecimal> lowerBound =
            new Condition<>(decimal -> decimal.compareTo(negatedDecimalBound) > 0, "lower bound");
    private static final Condition<BigDecimal> scaled =
            new Condition<>(decimal -> decimal.scale() == validScale, "scaled");

    @Test
    public void nextPositiveLongBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveLong(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPositiveLongShouldSucceed() {
        final long integer = mathRandom.nextPositiveLong(bound);
        assertThat(integer).isGreaterThanOrEqualTo(0).isLessThan(bound);
    }

    @Test
    public void nextNegativeLongBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeLong(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextNegativeLongShouldSucceed() {
        final long integer = mathRandom.nextNegativeLong(bound);
        assertThat(integer).isGreaterThan(-bound).isLessThanOrEqualTo(0);
    }

    @Test
    public void nextLongBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLong(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextLongShouldSucceed() {
        final long integer = mathRandom.nextLong(bound);
        assertThat(integer).isGreaterThan(-bound).isLessThan(bound);
    }

    @Test
    public void nextPositiveLongsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveLongs(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextPositiveLongsShouldSucceed() {
        final long[] integers = mathRandom.nextPositiveLongs(bound, howMany);
        assertThat(integers).hasSize(howMany);
        for (final long integer : integers) {
            assertThat(integer).isGreaterThanOrEqualTo(0).isLessThan(bound);
        }
    }

    @Test
    public void nextNegativeLongsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeLongs(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextNegativeLongsShouldSucceed() {
        final long[] integers = mathRandom.nextNegativeLongs(bound, howMany);
        assertThat(integers).hasSize(howMany);
        for (final long integer : integers) {
            assertThat(integer).isGreaterThan(-bound).isLessThanOrEqualTo(0);
        }
    }

    @Test
    public void nextLongsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLongs(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextLongsShouldSucceed() {
        final long[] integers = mathRandom.nextLongs(bound, howMany);
        assertThat(integers).hasSize(howMany);
        for (final long integer : integers) {
            assertThat(integer).isGreaterThan(-bound).isLessThan(bound);
        }
    }

    @Test
    public void nextPositiveDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveDecimal(0, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextPositiveDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextPositiveDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextPositiveDecimal(bound, validScale);
        assertThat(actual).isGreaterThanOrEqualTo(BigDecimal.ZERO).isLessThan(decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextNegativeDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeDecimal(0, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextNegativeDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextNegativeDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextNegativeDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedDecimalBound).isLessThanOrEqualTo(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimal(0, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertiblePositiveDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveDecimal(1, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertiblePositiveDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertiblePositiveDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertiblePositiveDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(BigDecimal.ZERO).isLessThan(decimalBound);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertibleNegativeDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeDecimal(1, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleNegativeDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleNegativeDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertibleNegativeDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedDecimalBound).isLessThan(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextInvertibleDecimalBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleDecimal(1, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleDecimalScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleDecimal(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleDecimalShouldSucceed() {
        final BigDecimal actual = mathRandom.nextInvertibleDecimal(bound, validScale);
        assertThat(actual).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound)
                .isNotEqualByComparingTo(BigDecimal.ZERO);
        assertThat(actual.scale()).isEqualTo(validScale);
    }

    @Test
    public void keepDecimalInBoundDecimalGreaterThanOrEqualToZeroAndDecimalGreaterThanOrEqualToBound() {
        final BigDecimal expected = mathRandom.nextPositiveDecimal(bound, validScale);
        final BigDecimal decimal = expected.add(decimalBound);
        assertThat(mathRandom.keepDecimalInBound(decimal, bound)).isEqualByComparingTo(expected);
    }

    @Test
    public void keepDecimalInBoundDecimalGreaterThanOrEqualToZeroAndDecimalLowerThanBound() {
        final BigDecimal decimal = mathRandom.nextPositiveDecimal(bound, validScale);
        assertThat(mathRandom.keepDecimalInBound(decimal, bound)).isEqualByComparingTo(decimal);
    }

    @Test
    public void keepDecimalInBoundDecimalLowerThanZeroAndDecimalLowerThanOrEqualToMinusBound() {
        final BigDecimal expected = mathRandom.nextNegativeDecimal(bound, validScale);
        final BigDecimal decimal = expected.subtract(decimalBound);
        assertThat(mathRandom.keepDecimalInBound(decimal, bound)).isEqualByComparingTo(expected);
    }

    @Test
    public void keepDecimalInBoundDecimalLowerThanZeroAndDecimalGreaterThanMinusBound() {
        final BigDecimal decimal = mathRandom.nextNegativeDecimal(bound, validScale);
        assertThat(mathRandom.keepDecimalInBound(decimal, bound)).isEqualByComparingTo(decimal);
    }

    @Test
    public void nextPositiveDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveDecimals(0, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextPositiveDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextPositiveDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextPositiveDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextPositiveDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(positive).are(upperBound).are(scaled);
    }

    @Test
    public void nextNegativeDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeDecimals(0, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextNegativeDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextNegativeDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextNegativeDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextNegativeDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound).are(negative).are(scaled);
    }

    @Test
    public void nextDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimals(0, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound)
                .are(new Condition<>(decimal -> decimal.compareTo(decimalBound) < 1, "upper bound")).are(scaled);
    }

    @Test
    public void nextInvertiblePositiveDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveDecimals(1, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertiblePositiveDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertiblePositiveDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertiblePositiveDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertiblePositiveDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) > 0, "lower bound")).are(upperBound)
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextInvertibleNegativeDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeDecimals(1, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextInvertibleNegativeDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertibleNegativeDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertibleNegativeDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) < 0, "upper bound"))
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextInvertibleDecimalsBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleDecimals(1, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleDecimalsScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleDecimals(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextInvertibleDecimalsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleDecimals(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextInvertibleDecimalsShouldSucceed() {
        final List<BigDecimal> decimals = mathRandom.nextInvertibleDecimals(bound, validScale, howMany);
        assertThat(decimals).hasSize(howMany).are(lowerBound).are(upperBound)
                .are(new Condition<>(decimal -> decimal.compareTo(BigDecimal.ZERO) != 0, "not zero"))
                .are(new Condition<>(decimal -> decimal.scale() == validScale, "scaled"));
    }

    @Test
    public void nextPositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextPositiveFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextPositiveFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextNegativeFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextNegativeFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(BigInteger.ZERO);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThan(negatedBigBound).isLessThanOrEqualTo(bigBound);
    }

    @Test
    public void nextInvertiblePositiveFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertiblePositiveFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextInvertiblePositiveFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(BigInteger.ZERO).isLessThan(bigBound);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleNegativeFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeFractionShouldSucceed() {
        final Fraction fraction = mathRandom.nextInvertibleNegativeFraction(bound);
        assertThat(fraction.getNumerator()).isGreaterThan(negatedBigBound).isLessThan(BigInteger.ZERO);
        assertThat(fraction.getDenominator()).isGreaterThanOrEqualTo(BigInteger.ZERO).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleFractionBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleFraction(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
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
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextPositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPositiveFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextNegativeFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
        assertThatThrownBy(() -> {
            mathRandom.nextFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

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
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertiblePositiveFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertiblePositiveFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");
    }

    @Test
    public void nextInvertibleNegativeFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleNegativeFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleFractions(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleFractionsTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleFractions(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

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
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumber(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberShouldSucceed() {
        final SimpleComplexNumber actual = mathRandom.nextSimpleComplexNumber(bound);
        assertThat(actual.getReal()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
        assertThat(actual.getImaginary()).isGreaterThan(negatedBigBound).isLessThan(bigBound);
    }

    @Test
    public void nextInvertibleSimpleComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleSimpleComplexNumber(1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

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
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumbers(0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumbers(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

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
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleSimpleComplexNumbers(1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleSimpleComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleSimpleComplexNumbers(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

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
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumber(0, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumber(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumberShouldSucceed() {
        final RealComplexNumber actual = mathRandom.nextRealComplexNumber(bound, validScale);
        assertThat(actual.getReal()).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (real part)"));
        assertThat(actual.getImaginary()).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (imaginary part)"));
    }

    @Test
    public void nextInvertibleRealComplexNumberBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleRealComplexNumber(1, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleRealComplexNumberScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleRealComplexNumber(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleRealComplexNumberShouldSucceed() {
        final RealComplexNumber actual = mathRandom.nextInvertibleRealComplexNumber(bound, validScale);
        assertThat(actual.getReal()).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (real part)"));
        assertThat(actual.getImaginary()).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound)
                .is(new Condition<>(decimal -> decimal.scale() == validScale, "scaled (imaginary part)"));
        assertThat((actual.getReal().compareTo(BigDecimal.ZERO) != 0)
                || (actual.getImaginary().compareTo(BigDecimal.ZERO) != 0)).isTrue();
    }

    @Test
    public void nextRealComplexNumbersBoundTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumbers(0, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumbersScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumbers(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumbers(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumbersShouldSucceed() {
        final List<RealComplexNumber> complexNumbers = mathRandom.nextRealComplexNumbers(bound, validScale, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleRealComplexNumbers(1, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 1 but actual 1");

    }

    @Test
    public void nextInvertibleRealComplexNumbersScaleTooLowShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleRealComplexNumbers(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextInvertibleRealComplexNumbersTooLessShoudThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextInvertibleRealComplexNumbers(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextInvertibleRealComplexNumbersShouldSucceed() {
        final List<RealComplexNumber> complexNumbers =
                mathRandom.nextInvertibleRealComplexNumbers(bound, validScale, howMany);
        assertThat(complexNumbers).hasSize(howMany)
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(negatedDecimalBound) > 0,
                        "lower bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (imaginary part)"))
                .are(new Condition<>(complexNumber -> complexNumber.getReal().scale() == validScale,
                        "scale of the real part"))
                .are(new Condition<>(complexNumber -> complexNumber.getImaginary().scale() == validScale,
                        "scale of the imaginary part"))
                .are(new Condition<>(complexNumber -> (complexNumber.getReal().compareTo(BigDecimal.ZERO) != 0)
                        || (complexNumber.getImaginary().compareTo(BigDecimal.ZERO) != 0), "invertible"));
    }

    @Test
    public void nextPolarFormBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPolarForm(0, validScale);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPolarFormScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPolarForm(bound, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextPolarFormShouldSucceed() {
        final PolarForm actual = mathRandom.nextPolarForm(bound, validScale);
        final BigDecimal radial = actual.getRadial();
        assertThat(radial).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound);
        assertThat(radial.scale()).isEqualTo(validScale);
        final BigDecimal angular = actual.getAngular();
        assertThat(angular).isGreaterThan(negatedDecimalBound).isLessThan(decimalBound);
        assertThat(angular.scale()).isEqualTo(validScale);
    }

    @Test
    public void nextPolarFormsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPolarForms(0, validScale, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextPolarFormsScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPolarForms(bound, -1, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextPolarFormsTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextPolarForms(bound, validScale, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual -1");
    }

    @Test
    public void nextPolarFormsShouldSucceed() {
        assertThat(mathRandom.nextPolarForms(bound, validScale, howMany)).hasOnlyElementsOfType(PolarForm.class)
                .hasSize(howMany)
                .are(new Condition<>(polarForm -> polarForm.getRadial().compareTo(negatedDecimalBound) > 0,
                        "radial lower bound"))
                .are(new Condition<>(polarForm -> polarForm.getRadial().compareTo(decimalBound) < 0,
                        "radial upper bound"))
                .are(new Condition<>(polarForm -> polarForm.getRadial().scale() == validScale, "radial scaled"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().compareTo(negatedDecimalBound) > 0,
                        "angular lower bound"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().compareTo(decimalBound) < 0,
                        "angluar upper bound"))
                .are(new Condition<>(polarForm -> polarForm.getAngular().scale() == validScale, "angular scaled"));
    }

    @Test
    public void nextBigIntVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntVector(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntVector(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextBigIntVectorShouldSucceed() {
        final BigIntVector actual = mathRandom.nextBigIntVector(bound, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element instanceof BigInteger, "type of the element"))
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
    }

    @Test
    public void nextBigIntVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntVectors(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextBigIntVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntVectors(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextBigIntVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntVectors(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextBigIntVectorsShouldSucceed() {
        final List<BigIntVector> vectors = mathRandom.nextBigIntVectors(bound, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> {
            assertThat(vector.getMap().values())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrix(0, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrix(bound, 0, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrix(bound, validRowSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextBigIntMatrix(bound, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualByComparingTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextUpperTriangularBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.upperTriangular()).isTrue();
    }

    @Test
    public void nextLowerTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextLowerTriangularBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.lowerTriangular()).isTrue();
    }

    @Test
    public void nextTriangularBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextTriangularBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.triangular()).isTrue();
    }

    @Test
    public void nextDiagonalBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextDiagonalBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.diagonal()).isTrue();
    }

    @Test
    public void nextSymmetricBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextSymmetricBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricBigIntMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricBigIntMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricBigIntMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntMatrixShouldSucceed() {
        final BigIntMatrix matrix = mathRandom.nextSkewSymmetricBigIntMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.skewSymmetric()).isTrue();
    }

    @Test
    public void nextBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrices(0, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrices(bound, 0, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrices(bound, validRowSize, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextBigIntMatrices(bound, validRowSize, validColumnSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices =
                mathRandom.nextBigIntMatrices(bound, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextUpperTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextUpperTriangularBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.upperTriangular(), "upper triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextLowerTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextLowerTriangularBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.lowerTriangular(), "lower triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextTriangularBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextTriangularBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextDiagonalBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextDiagonalBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextSymmetricBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextSymmetricBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextSkewSymmetricBigIntMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricBigIntMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricBigIntMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricBigIntMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricBigIntMatricesShouldSucceed() {
        final List<BigIntMatrix> matrices = mathRandom.nextSkewSymmetricBigIntMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedBigBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(bigBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextDecimalVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVector(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextDecimalVectorScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVector(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextDecimalVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVector(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void nextDecimalVectorShouldSucceed() {
        final DecimalVector actual = mathRandom.nextDecimalVector(bound, validScale, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element instanceof BigDecimal, "type of the element"))
                .are(new Condition<>(element -> element.compareTo(BigDecimal.ZERO) > -1, "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                .are(new Condition<>(element -> element.scale() == validScale, "scale of the element"));
    }

    @Test
    public void nextDecimalVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVectors(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextDecimalVectorsScaleTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVectors(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextDecimalVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVectors(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextDecimalVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalVectors(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextDecimalVectorsShouldSucceed() {
        final List<DecimalVector> vectors = mathRandom.nextDecimalVectors(bound, validScale, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> {
            assertThat(vector.getMap().values())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                    .are(new Condition<>(element -> element.scale() == validScale, "scale of the element"));
        });
    }

    @Test
    public void nextDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrix(0, validScale, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrix(bound, -1, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDecimalMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrix(bound, validScale, 0, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrix(bound, validScale, validRowSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextDecimalMatrix(bound, validScale, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"))
                .are(new Condition<>(element -> element.scale() == validScale, "scale of the element"));
        assertThat(matrix.rowSize()).isEqualTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextUpperTriangularDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
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
    public void nextLowerTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextLowerTriangularDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
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
    public void nextTriangularDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextTriangularDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
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
    public void nextDiagonalDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextDiagonalDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
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
    public void nextSymmetricDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextSymmetricDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                        "lower bound of the element"))
                .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        matrix.elements().forEach(element -> {
            assertThat(element.scale()).isEqualTo(validScale);
        });
        assertThat(matrix.rowSize()).isEqualTo(validSize);
        assertThat(matrix.columnSize()).isEqualTo(validSize);
        assertThat(matrix.symmetric()).isTrue();
    }

    @Test
    public void nextSkewSymmetricDecimalMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricDecimalMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricDecimalMatrixShouldSucceed() {
        final DecimalMatrix matrix = mathRandom.nextSkewSymmetricDecimalMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
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
    public void nextDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrices(0, validScale, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrices(bound, -1, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDecimalMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrices(bound, validScale, 0, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, validColumnSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextDecimalMatrices(bound, validScale, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextUpperTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextUpperTriangularDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.upperTriangular(), "upper triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextLowerTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextLowerTriangularDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.lowerTriangular(), "lower triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextTriangularDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextTriangularDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextDiagonalDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextDiagonalDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextSymmetricDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextSymmetricDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextSkewSymmetricDecimalMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricDecimalMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricDecimalMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricDecimalMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricDecimalMatricesShouldSucceed() {
        final List<DecimalMatrix> matrices =
                mathRandom.nextSkewSymmetricDecimalMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.compareTo(negatedDecimalBound) > 0,
                            "lower bound of the element"))
                    .are(new Condition<>(element -> element.compareTo(decimalBound) < 0, "upper bound of the element"));
        });
    }

    @Test
    public void nextSimpleComplexNumberVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberVector(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberVector(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorShouldSucceed() {
        final SimpleComplexNumberVector actual = mathRandom.nextSimpleComplexNumberVector(bound, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.getMap().values())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
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
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberVectors(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberVectors(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberVectors(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextSimpleComplexNumberVectorsShouldSucceed() {
        final List<SimpleComplexNumberVector> vectors =
                mathRandom.nextSimpleComplexNumberVectors(bound, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> {
            assertThat(vector.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextSimpleComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrix(0, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrix(bound, 0, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrix(bound, validRowSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextSimpleComplexNumberMatrix(bound, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextUpperTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextLowerTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextTriangularSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextDiagonalSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix = mathRandom.nextSymmetricSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(0, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(bound, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix matrix =
                mathRandom.nextSkewSymmetricSimpleComplexNumberMatrix(bound, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrices(0, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrices(bound, 0, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, validColumnSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSimpleComplexNumberMatrices(bound, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.upperTriangular(), "upper triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.lowerTriangular(), "lower triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextTriangularSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.triangular(), "riangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextDiagonalSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.diagonal(), "diagonal"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSymmetricSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSymmetricSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.symmetric(), "symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(0, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricSimpleComplexNumberMatricesShouldSucceed() {
        final List<SimpleComplexNumberMatrix> matrices =
                mathRandom.nextSkewSymmetricSimpleComplexNumberMatrices(bound, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.skewSymmetric(), "skew-symmetric"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element instanceof SimpleComplexNumber, "type of the element"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedBigBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(bigBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedBigBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(bigBound) < 0,
                            "upper bound (imaginary part)"));
        });
    }

    @Test
    public void nextRealComplexNumberVectorBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVector(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVector(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void nextRealComplexNumberVectorSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVector(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void nextRealComplexNumberVectorShouldSucceed() {
        final RealComplexNumberVector actual = mathRandom.nextRealComplexNumberVector(bound, validScale, validSize);
        assertThat(actual.size()).isEqualTo(validSize);
        assertThat(actual.elements())
                .are(new Condition<>(element -> element instanceof RealComplexNumber, "correctly typed"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale, "scaled (real part)"));
    }

    @Test
    public void nextRealComplexNumberVectorsBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVectors(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsScaleTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVectors(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");

    }

    @Test
    public void nextRealComplexNumberVectorsSizeTooLowhouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVectors(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberVectors(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");

    }

    @Test
    public void nextRealComplexNumberVectorsShouldSucceed() {
        final List<RealComplexNumberVector> vectors =
                mathRandom.nextRealComplexNumberVectors(bound, validScale, validSize, howMany);
        assertThat(vectors).hasSize(howMany)
                .are(new Condition<>(vector -> vector.size() == validSize, "size of the vector"));
        vectors.forEach(vector -> {
            assertThat(vector.elements())
                    .are(new Condition<>(element -> element instanceof RealComplexNumber, "correctly typed"))
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().scale() == validScale,
                            "scaled (real part)"));
        });
    }

    @Test
    public void nextRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrix(0, validScale, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrix(bound, -1, validRowSize, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextRealComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrix(bound, validScale, 0, validColumnSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrix(bound, validScale, validRowSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextRealComplexNumberMatrix(bound, validScale, validRowSize, validColumnSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof RealComplexNumber, "correctly typed"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().scale() == validScale, "scaled (real part)"));
        assertThat(matrix.rowSize()).isEqualTo(validRowSize);
        assertThat(matrix.columnSize()).isEqualTo(validColumnSize);
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element instanceof RealComplexNumber, "correctly typed"))
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextTriangularRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                        "upper bound (real part)"));
        matrix.cells().forEach(cell -> {
            if ((matrix.upperTriangular() && (cell.getRowKey() <= cell.getColumnKey()))
                    || (matrix.lowerTriangular() && (cell.getRowKey() >= cell.getColumnKey()))) {
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
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextDiagonalRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextSymmetricRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrix(0, validScale, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, -1, validSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, validScale, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix matrix =
                mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, validScale, validSize);
        assertThat(matrix.elements())
                .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                        "lower bound (real part)"))
                .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                        "upper bound (real part)"))
                .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrices(0, validScale, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrices(bound, -1, validRowSize, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextRealComplexNumberMatricesRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrices(bound, validScale, 0, validColumnSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, validColumnSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextRealComplexNumberMatrices(bound, validScale, validRowSize, validColumnSize, howMany);
        assertThat(matrices).hasSize(howMany)
                .are(new Condition<>(matrix -> matrix.rowSize() == validRowSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validColumnSize, "column size"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().scale() == validScale, "scaled (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().scale() == validScale,
                            "scaled (imaginary part)"));
        });
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextUpperTriangularRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.upperTriangular(), "upper triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
    }

    @Test
    public void nextLowerTriangularRealComplexNumberMatricesShouldSucceed() {
        final List<RealComplexNumberMatrix> matrices =
                mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, validScale, validSize, howMany);
        assertThat(matrices).hasSize(howMany).are(new Condition<>(matrix -> matrix.rowSize() == validSize, "row size"))
                .are(new Condition<>(matrix -> matrix.columnSize() == validSize, "column size"))
                .are(new Condition<>(matrix -> matrix.lowerTriangular(), "lower triangular"));
        matrices.forEach(matrix -> {
            assertThat(matrix.elements())
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextTriangularRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextTriangularRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
                            "lower bound (imaginary part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(decimalBound) < 0,
                            "upper bound (imaginary part)"));
            matrix.cells().forEach(cell -> {
                if ((matrix.upperTriangular() && (cell.getRowKey() <= cell.getColumnKey()))
                        || (matrix.lowerTriangular() && (cell.getRowKey() >= cell.getColumnKey()))) {
                    assertThat(cell.getValue().getReal().scale()).isEqualTo(validScale);
                    assertThat(cell.getValue().getImaginary().scale()).isEqualTo(validScale);
                }
            });
        });
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesBoundTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextDiagonalRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextDiagonalRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSymmetricRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSymmetricRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrices(0, validScale, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected bound > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, -1, validSize, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale > -1 but actual -1");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, validScale, 0, howMany);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void nextSkewSymmetricRealComplexNumberMatricesTooLessShouldThrowException() {
        assertThatThrownBy(() -> {
            mathRandom.nextSkewSymmetricRealComplexNumberMatrices(bound, validScale, validSize, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected howMany > 0 but actual 0");
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
                    .are(new Condition<>(element -> element.getReal().compareTo(negatedDecimalBound) > 0,
                            "lower bound (real part)"))
                    .are(new Condition<>(element -> element.getReal().compareTo(decimalBound) < 0,
                            "upper bound (real part)"))
                    .are(new Condition<>(element -> element.getImaginary().compareTo(negatedDecimalBound) > 0,
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
