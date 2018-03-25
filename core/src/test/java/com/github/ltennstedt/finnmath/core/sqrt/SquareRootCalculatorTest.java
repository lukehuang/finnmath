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

package com.github.ltennstedt.finnmath.core.sqrt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.number.ScientificNotation;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SquareRootCalculatorTest {
    private static final Logger log = LoggerFactory.getLogger(SquareRootCalculatorTest.class);
    private final int validScale = 8;
    private final RoundingMode roundingMode = RoundingMode.HALF_EVEN;
    private final MathRandom mathRandom = new MathRandom(7);

    @After
    public void after() {
        log.debug("");
    }

    @Test
    public void sqrtOfBigIntNullShouldThrowException() {
        assertThatThrownBy(() -> SquareRootCalculator.sqrt((BigInteger) null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeShouldThrowException() {
        assertThatThrownBy(() -> SquareRootCalculator.sqrt(BigInteger.ONE.negate()))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected integer >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntZero() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ZERO)).isBetween(BigDecimal.valueOf(-0.001),
            BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfBigIntOne() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ONE)).isBetween(BigDecimal.valueOf(0.999),
            BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfBigIntTwo() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(2))).isBetween(BigDecimal.valueOf(1.413),
            BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfBigIntThree() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(3))).isBetween(BigDecimal.valueOf(1.731),
            BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfBigIntFour() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(4))).isBetween(BigDecimal.valueOf(1.999),
            BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfBigIntFive() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(5))).isBetween(BigDecimal.valueOf(2.235),
            BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfBigIntSix() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(6))).isBetween(BigDecimal.valueOf(2.448),
            BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfBigIntSeven() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(7))).isBetween(BigDecimal.valueOf(2.644),
            BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfBigIntEight() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(8))).isBetween(BigDecimal.valueOf(2.827),
            BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfBigIntNine() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(9))).isBetween(BigDecimal.valueOf(2.999),
            BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfBigIntTen() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.TEN)).isBetween(BigDecimal.valueOf(3.161),
            BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfBigIntSixteen() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(16))).isBetween(BigDecimal.valueOf(3.999),
            BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfBigIntTwentyFive() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(25))).isBetween(BigDecimal.valueOf(4.999),
            BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfBigIntThirtySix() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(36))).isBetween(BigDecimal.valueOf(5.999),
            BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfBigIntFortyNine() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(49))).isBetween(BigDecimal.valueOf(6.999),
            BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfBigIntSixtyFour() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(64))).isBetween(BigDecimal.valueOf(7.999),
            BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfBigIntEightyOne() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(81))).isBetween(BigDecimal.valueOf(8.999),
            BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfBigIntOneHundred() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(100))).isBetween(BigDecimal.valueOf(9.999),
            BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfDecimalNullShouldThrowException() {
        assertThatThrownBy(() -> SquareRootCalculator.sqrt((BigDecimal) null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
    }

    @Test
    public void sqrtOfDecimalZero() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ZERO)).isBetween(BigDecimal.valueOf(-0.001),
            BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfDecimalOne() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ONE)).isBetween(BigDecimal.valueOf(0.999),
            BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfDecimalTwo() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(2))).isBetween(BigDecimal.valueOf(1.413),
            BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfDecimalThree() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(3))).isBetween(BigDecimal.valueOf(1.731),
            BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfDecimalFour() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(4))).isBetween(BigDecimal.valueOf(1.999),
            BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfDecimalFive() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(5))).isBetween(BigDecimal.valueOf(2.235),
            BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfDecimalSix() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(6))).isBetween(BigDecimal.valueOf(2.448),
            BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfDecimalSeven() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(7))).isBetween(BigDecimal.valueOf(2.644),
            BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfDecimalEight() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(8))).isBetween(BigDecimal.valueOf(2.827),
            BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfDecimalNine() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(9))).isBetween(BigDecimal.valueOf(2.999),
            BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfDecimalTen() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.TEN)).isBetween(BigDecimal.valueOf(3.161),
            BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfDecimalSixteen() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(16))).isBetween(BigDecimal.valueOf(3.999),
            BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfDecimalTwentyFive() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(25))).isBetween(BigDecimal.valueOf(4.999),
            BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfDecimalThirtySix() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(36))).isBetween(BigDecimal.valueOf(5.999),
            BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfDecimalFortyNine() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(49))).isBetween(BigDecimal.valueOf(6.999),
            BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfDecimalSixtyFour() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(64))).isBetween(BigDecimal.valueOf(7.999),
            BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfDecimalEightyOne() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(81))).isBetween(BigDecimal.valueOf(8.999),
            BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfDecimalOneHundred() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(100))).isBetween(BigDecimal.valueOf(9.999),
            BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfPerfectSquareNullShouldThrowException() {
        assertThatThrownBy(() -> SquareRootCalculator.sqrtOfPerfectSquare(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfPerfectSquareNotPerfectSquareShouldThrowException() {
        assertThatThrownBy(() -> SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(2)))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected perfect square but actual 2");
    }

    @Test
    public void sqrtOfPerfectSquareZero() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.ZERO)).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void sqrtOfPerfectSquareOne() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.ONE)).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void sqrtOfPerfectSquareFour() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(4))).isEqualTo(BigInteger.valueOf(2));
    }

    @Test
    public void sqrtOfPerfectSquareNine() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(9))).isEqualTo(BigInteger.valueOf(3));
    }

    @Test
    public void sqrtOfPerfectSquareSixteen() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(16))).isEqualTo(BigInteger.valueOf(4));
    }

    @Test
    public void sqrtOfPerfectSquareTwentyFive() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(25))).isEqualTo(BigInteger.valueOf(5));
    }

    @Test
    public void sqrtOfPerfectSquareThirtySix() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(36))).isEqualTo(BigInteger.valueOf(6));
    }

    @Test
    public void sqrtOfPerfectSquareFortyNine() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(49))).isEqualTo(BigInteger.valueOf(7));
    }

    @Test
    public void sqrtOfPerfectSquareSixtyFour() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(64))).isEqualTo(BigInteger.valueOf(8));
    }

    @Test
    public void sqrtOfPerfectSquareEightyOne() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(81))).isEqualTo(BigInteger.valueOf(9));
    }

    @Test
    public void sqrtOfPerfectSquareOneHundred() {
        assertThat(SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(100))).isEqualTo(BigInteger.TEN);
    }

    @Test
    public void scientificNotationForSqrtForZero() {
        final ScientificNotation actual =
            SquareRootCalculator.scientificNotationForSqrt(BigDecimal.ZERO, RoundingMode.HALF_UP);
        assertThat(actual).isEqualTo(new ScientificNotation(BigDecimal.ZERO, 0));
    }

    @Test
    public void scientificNotationForSqrtInbetweenZeroAndOneHundred() {
        final BigDecimal decimal = mathRandom.nextInvertiblePositiveBigDecimal(100, validScale);
        final ScientificNotation actual = SquareRootCalculator.scientificNotationForSqrt(decimal, RoundingMode.HALF_UP);
        assertThat(actual).isEqualTo(new ScientificNotation(decimal, 0));
    }

    @Test
    public void scientificNotationForSqrtGreaterThanOneHundred() {
        final BigDecimal decimal =
            mathRandom.nextInvertiblePositiveBigDecimal(100, validScale).add(BigDecimal.valueOf(100));
        final ScientificNotation actual = SquareRootCalculator.scientificNotationForSqrt(decimal, RoundingMode.HALF_UP);
        final ScientificNotation expected =
            new ScientificNotation(decimal.divide(BigDecimal.valueOf(100), roundingMode), 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void scientificNotationForSqrtExponentShouldBePowerOfTwo() {
        mathRandom.nextBigDecimals(10, 2, 10)
            .forEach(decimal -> assertThat(
                SquareRootCalculator.scientificNotationForSqrt(decimal, RoundingMode.HALF_UP).getExponent() % 2)
                    .isEqualTo(0));
    }
}
