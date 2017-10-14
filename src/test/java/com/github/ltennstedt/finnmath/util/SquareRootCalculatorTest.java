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

import com.github.ltennstedt.finnmath.number.ScientificNotation;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SquareRootCalculatorTest {
    private static final int validScale = 8;
    private final SquareRootCalculator squareRootCalculator = new SquareRootCalculator();
    private final BigDecimal validPrecision = BigDecimal.valueOf(0.00000001);
    private final RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private final MathRandom mathRandom = new MathRandom(7);
    private final Logger log = LoggerFactory.getLogger(SquareRootCalculatorTest.class);

    @After
    public void after() {
        log.debug("");
    }

    @Test
    public void newPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void newPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void newPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void newPrecisionShouldSucceed() {
        final BigDecimal precision = BigDecimal.valueOf(0.00000001);
        final SquareRootCalculator actual = new SquareRootCalculator(precision);
        assertThat(actual.getPrecision()).isEqualByComparingTo(precision);
        assertThat(actual.getScale()).isEqualTo(SquareRootCalculator.DEFAULT_SCALE);
        assertThat(actual.getRoundingMode()).isEqualTo(SquareRootCalculator.DEFAULT_ROUNDING_MODE);
    }

    @Test
    public void newScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(-1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void newScaleAndRoundingModeShouldSucceed() {
        final int scale = 8;
        final SquareRootCalculator actual = new SquareRootCalculator(scale, roundingMode);
        assertThat(actual.getPrecision())
                .isEqualByComparingTo(SquareRootCalculator.DEFAULT_PRECISION);
        assertThat(actual.getScale()).isEqualTo(scale);
        assertThat(actual.getRoundingMode()).isEqualTo(roundingMode);
    }

    @Test
    public void newAllPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(null, validScale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void newAllPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(BigDecimal.ZERO, validScale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void newAllPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(BigDecimal.ONE, validScale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void newAllScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            new SquareRootCalculator(validPrecision, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void newAllShouldSucceed() {
        final BigDecimal precision = BigDecimal.valueOf(0.00000001);
        final int scale = 8;
        final SquareRootCalculator actual = new SquareRootCalculator(precision, scale,
                roundingMode);
        assertThat(actual.getPrecision()).isEqualByComparingTo(precision);
        assertThat(actual.getScale()).isEqualTo(scale);
        assertThat(actual.getRoundingMode()).isEqualTo(roundingMode);
    }

    @Test
    public void sqrtOfBigIntNullShouldThrowException() {
        assertThatThrownBy(() -> {
            squareRootCalculator.sqrt((BigInteger) null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeShouldThrowException() {
        assertThatThrownBy(() -> {
            squareRootCalculator.sqrt(BigInteger.ONE.negate());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected integer >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntZero() {
        assertThat(squareRootCalculator.sqrt(BigInteger.ZERO)).isBetween(BigDecimal.valueOf(-0.001),
                BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfBigIntOne() {
        assertThat(squareRootCalculator.sqrt(BigInteger.ONE)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfBigIntTwo() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(2)))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfBigIntThree() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(3)))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfBigIntFour() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(4)))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfBigIntFive() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(5)))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfBigIntSix() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(6)))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfBigIntSeven() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(7)))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfBigIntEight() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(8)))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfBigIntNine() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(9)))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfBigIntTen() {
        assertThat(squareRootCalculator.sqrt(BigInteger.TEN)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfBigIntSixteen() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(16)))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfBigIntTwentyFive() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(25)))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfBigIntThirtySix() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(36)))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfBigIntFortyNine() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(49)))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfBigIntSixtyFour() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(64)))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfBigIntEightyOne() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(81)))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfBigIntOneHundred() {
        assertThat(squareRootCalculator.sqrt(BigInteger.valueOf(100)))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfDecimalNullShouldThrowException() {
        assertThatThrownBy(() -> {
            squareRootCalculator.sqrt((BigDecimal) null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
    }

    @Test
    public void sqrtOfDecimalZero() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.ZERO)).isBetween(BigDecimal.valueOf(-0.001),
                BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfDecimalOne() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.ONE)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfDecimalTwo() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(2)))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfDecimalThree() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(3)))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfDecimalFour() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(4)))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfDecimalFive() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(5)))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfDecimalSix() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(6)))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfDecimalSeven() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(7)))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfDecimalEight() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(8)))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfDecimalNine() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(9)))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfDecimalTen() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.TEN)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfDecimalSixteen() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(16)))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfDecimalTwentyFive() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(25)))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfDecimalThirtySix() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(36)))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfDecimalFortyNine() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(49)))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfDecimalSixtyFour() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(64)))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfDecimalEightyOne() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(81)))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfDecimalOneHundred() {
        assertThat(squareRootCalculator.sqrt(BigDecimal.valueOf(100)))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfPerfectSquareNullShouldThrowException() {
        assertThatThrownBy(() -> {
            squareRootCalculator.sqrtOfPerfectSquare(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfPerfectSquareNotPerfectSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(2));
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected perfect square but actual 2");
    }

    @Test
    public void sqrtOfPerfectSquareZero() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.ZERO))
                .isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void sqrtOfPerfectSquareOne() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.ONE))
                .isEqualTo(BigInteger.ONE);
    }

    @Test
    public void sqrtOfPerfectSquareFour() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(4)))
                .isEqualTo(BigInteger.valueOf(2));
    }

    @Test
    public void sqrtOfPerfectSquareNine() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(9)))
                .isEqualTo(BigInteger.valueOf(3));
    }

    @Test
    public void sqrtOfPerfectSquareSixteen() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(16)))
                .isEqualTo(BigInteger.valueOf(4));
    }

    @Test
    public void sqrtOfPerfectSquareTwentyFive() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(25)))
                .isEqualTo(BigInteger.valueOf(5));
    }

    @Test
    public void sqrtOfPerfectSquareThirtySix() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(36)))
                .isEqualTo(BigInteger.valueOf(6));
    }

    @Test
    public void sqrtOfPerfectSquareFortyNine() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(49)))
                .isEqualTo(BigInteger.valueOf(7));
    }

    @Test
    public void sqrtOfPerfectSquareSixtyFour() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(64)))
                .isEqualTo(BigInteger.valueOf(8));
    }

    @Test
    public void sqrtOfPerfectSquareEightyOne() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(81)))
                .isEqualTo(BigInteger.valueOf(9));
    }

    @Test
    public void sqrtOfPerfectSquareOneHundred() {
        assertThat(squareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(100)))
                .isEqualTo(BigInteger.TEN);
    }

    @Test
    public void scientificNotationForSqrtForZero() {
        final ScientificNotation actual = squareRootCalculator
                .scientificNotationForSqrt(BigDecimal.ZERO);
        assertThat(actual).isEqualTo(new ScientificNotation(BigDecimal.ZERO, 0));
    }

    @Test
    public void scientificNotationForSqrtInbetweenZeroAndOneHundred() {
        final BigDecimal decimal = mathRandom.nextInvertiblePositiveDecimal(100, validScale);
        final ScientificNotation actual = squareRootCalculator.scientificNotationForSqrt(decimal);
        assertThat(actual).isEqualTo(new ScientificNotation(decimal, 0));
    }

    @Test
    public void scientificNotationForSqrtGreaterThanOneHundred() {
        final BigDecimal decimal = mathRandom.nextInvertiblePositiveDecimal(100, validScale)
                .add(BigDecimal.valueOf(100));
        final ScientificNotation actual = squareRootCalculator.scientificNotationForSqrt(decimal);
        final ScientificNotation expected = new ScientificNotation(
                decimal.divide(BigDecimal.valueOf(100), roundingMode), 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void scientificNotationForSqrtExponentShouldBePowerOfTwo() {
        mathRandom.nextDecimals(10, 2, 10).forEach(decimal -> {
            assertThat(squareRootCalculator.scientificNotationForSqrt(decimal).getExponent() % 2)
                    .isEqualTo(0);
        });
    }

    @Test
    public void toStringShouldSucceed() {
        final String expected = MoreObjects.toStringHelper(squareRootCalculator)
                .add("precision", SquareRootCalculator.DEFAULT_PRECISION)
                .add("scale", SquareRootCalculator.DEFAULT_SCALE)
                .add("roundingMode", SquareRootCalculator.DEFAULT_ROUNDING_MODE).toString();
        assertThat(squareRootCalculator.toString()).isEqualTo(expected);
    }
}
