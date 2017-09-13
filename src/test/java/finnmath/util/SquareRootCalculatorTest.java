/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2017, Lars Tennstedt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package finnmath.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finnmath.number.ScientificNotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SquareRootCalculatorTest {
    private static final Logger log = LoggerFactory.getLogger(SquareRootCalculatorTest.class);
    private final BigDecimal precision = BigDecimal.valueOf(0.00000001);
    private final int scale = 8;
    private final int roundingMoude = BigDecimal.ROUND_HALF_EVEN;
    private final MathRandom mathRandom = new MathRandom();

    @After
    public void after() {
        log.debug("");
    }

    @Test
    public void sqrtOfBigIntNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigInteger) null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ONE.negate());
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected integer >= 0 but actual -1");
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
    public void sqrtOfBigIntNullWithPrecisonShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigInteger) null, precision);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ONE.negate(), precision);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected integer >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithPrecisonNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void sqrtOfBigIntPrecisonTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void sqrtOfBigIntPrecisonTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void sqrtOfBigIntZeroWithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ZERO, precision)).isBetween(BigDecimal.valueOf(-0.001),
                BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfBigIntOnewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ONE, precision)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfBigIntTwowithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(2), precision)).isBetween(BigDecimal.valueOf(1.413),
                BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfBigIntThreewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(3), precision)).isBetween(BigDecimal.valueOf(1.731),
                BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfBigIntFourwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(4), precision)).isBetween(BigDecimal.valueOf(1.999),
                BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfBigIntFivewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(5), precision)).isBetween(BigDecimal.valueOf(2.235),
                BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfBigIntSixwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(6), precision)).isBetween(BigDecimal.valueOf(2.448),
                BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfBigIntSevenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(7), precision)).isBetween(BigDecimal.valueOf(2.644),
                BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfBigIntEightwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(8), precision)).isBetween(BigDecimal.valueOf(2.827),
                BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfBigIntNinewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(9), precision)).isBetween(BigDecimal.valueOf(2.999),
                BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfBigIntTenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.TEN, precision)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfBigIntSixteenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(16), precision)).isBetween(BigDecimal.valueOf(3.999),
                BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfBigIntTwentyFivewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(25), precision)).isBetween(BigDecimal.valueOf(4.999),
                BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfBigIntThirtySixwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(36), precision)).isBetween(BigDecimal.valueOf(5.999),
                BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfBigIntFortyNinewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(49), precision)).isBetween(BigDecimal.valueOf(6.999),
                BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfBigIntSixtyFourwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(64), precision)).isBetween(BigDecimal.valueOf(7.999),
                BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfBigIntEightyOnewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(81), precision)).isBetween(BigDecimal.valueOf(8.999),
                BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfBigIntOneHundredwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(100), precision)).isBetween(BigDecimal.valueOf(9.999),
                BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfBigIntNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigInteger) null, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ONE.negate(), scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected integer >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, -1, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, 0, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, 0, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void sqrtOfBigIntZeroWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ZERO, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(-0.001), BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfBigIntOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ONE, scale, roundingMoude)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfBigIntTwoWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(2), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfBigIntThreeWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(3), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfBigIntFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(4), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfBigIntFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(5), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfBigIntSixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(6), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfBigIntSevenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(7), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfBigIntEightWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(8), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfBigIntNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(9), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfBigIntTenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.TEN, scale, roundingMoude)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfBigIntSixteenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(16), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfBigIntTwentyFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(25), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfBigIntThirtySixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(36), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfBigIntFortyNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(49), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfBigIntSixtyFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(64), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfBigIntEightyOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(81), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfBigIntOneHundredWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(100), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfBigIntNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigInteger) null, precision, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfBigIntNegativeWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ONE.negate(), precision, scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected integer >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, null, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, BigDecimal.ZERO, scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, BigDecimal.ONE, scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, precision, -1, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionAndScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, precision, 0, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void sqrtOfBigIntWithPrecisionAndScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigInteger.ZERO, precision, 0, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void sqrtOfBigIntZeroWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ZERO, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(-0.001), BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfBigIntOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.ONE, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(0.999), BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfBigIntTwoWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(2), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfBigIntThreeWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(3), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfBigIntFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(4), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfBigIntFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(5), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfBigIntSixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(6), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfBigIntSevenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(7), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfBigIntEightWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(8), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfBigIntNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(9), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfBigIntTenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.TEN, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.161), BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfBigIntSixteenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(16), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfBigIntTwentyFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(25), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfBigIntThirtySixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(36), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfBigIntFortyNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(49), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfBigIntSixtyFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(64), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfBigIntEightyOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(81), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfBigIntOneHundredWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigInteger.valueOf(100), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfDecimalNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigDecimal) null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
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
    public void sqrtOfNullWithPrecisonShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigDecimal) null, precision);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
    }

    @Test
    public void sqrtOfDecimalWithPrecisonNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void sqrtOfDecimalPrecisonTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void sqrtOfDecimalPrecisonTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void sqrtOfDecimalZeroWithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ZERO, precision)).isBetween(BigDecimal.valueOf(-0.001),
                BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfDecimalOnewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ONE, precision)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfDecimalTwowithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(2), precision)).isBetween(BigDecimal.valueOf(1.413),
                BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfDecimalThreewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(3), precision)).isBetween(BigDecimal.valueOf(1.731),
                BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfDecimalFourwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(4), precision)).isBetween(BigDecimal.valueOf(1.999),
                BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfDecimalFivewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(5), precision)).isBetween(BigDecimal.valueOf(2.235),
                BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfDecimalSixwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(6), precision)).isBetween(BigDecimal.valueOf(2.448),
                BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfDecimalSevenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(7), precision)).isBetween(BigDecimal.valueOf(2.644),
                BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfDecimalEightwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(8), precision)).isBetween(BigDecimal.valueOf(2.827),
                BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfDecimalNinewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(9), precision)).isBetween(BigDecimal.valueOf(2.999),
                BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfDecimalTenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.TEN, precision)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfDecimalSixteenwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(16), precision)).isBetween(BigDecimal.valueOf(3.999),
                BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfDecimalTwentyFivewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(25), precision)).isBetween(BigDecimal.valueOf(4.999),
                BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfDecimalThirtySixwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(36), precision)).isBetween(BigDecimal.valueOf(5.999),
                BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfDecimalFortyNinewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(49), precision)).isBetween(BigDecimal.valueOf(6.999),
                BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfDecimalSixtyFourwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(64), precision)).isBetween(BigDecimal.valueOf(7.999),
                BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfDecimalEightyOnewithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(81), precision)).isBetween(BigDecimal.valueOf(8.999),
                BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfDecimalOneHundredwithPrecision() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(100), precision)).isBetween(BigDecimal.valueOf(9.999),
                BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfDecimalNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigDecimal) null, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
    }

    @Test
    public void sqrtOfDecimalWithScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, -1, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void sqrtOfDecimalWithScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, 0, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void sqrtOfDecimalWithScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, 0, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void sqrtOfDecimalZeroWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ZERO, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(-0.001), BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfDecimalOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ONE, scale, roundingMoude)).isBetween(BigDecimal.valueOf(0.999),
                BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfDecimalTwoWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(2), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfDecimalThreeWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(3), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfDecimalFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(4), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfDecimalFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(5), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfDecimalSixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(6), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfDecimalSevenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(7), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfDecimalEightWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(8), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfDecimalNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(9), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfDecimalTenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.TEN, scale, roundingMoude)).isBetween(BigDecimal.valueOf(3.161),
                BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfDecimalSixteenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(16), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfDecimalTwentyFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(25), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfDecimalThirtySixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(36), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfDecimalFortyNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(49), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfDecimalSixtyFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(64), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfDecimalEightyOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(81), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfDecimalOneHundredWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(100), scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfDecimalNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt((BigDecimal) null, precision, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("decimal");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, null, scale, roundingMoude);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, BigDecimal.ZERO, scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, BigDecimal.ONE, scale, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, precision, -1, roundingMoude);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionAndScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, precision, 0, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void sqrtOfDecimalWithPrecisionAndScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrt(BigDecimal.ZERO, precision, 0, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void sqrtOfDecimalZeroWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ZERO, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(-0.001), BigDecimal.valueOf(0.001));
    }

    @Test
    public void sqrtOfDecimalOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.ONE, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(0.999), BigDecimal.valueOf(1.001));
    }

    @Test
    public void sqrtOfDecimalTwoWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(2), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.413), BigDecimal.valueOf(1.415));
    }

    @Test
    public void sqrtOfDecimalThreeWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(3), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.731), BigDecimal.valueOf(1.733));
    }

    @Test
    public void sqrtOfDecimalFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(4), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(1.999), BigDecimal.valueOf(2.001));
    }

    @Test
    public void sqrtOfDecimalFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(5), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.235), BigDecimal.valueOf(2.237));
    }

    @Test
    public void sqrtOfDecimalSixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(6), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.448), BigDecimal.valueOf(2.45));
    }

    @Test
    public void sqrtOfDecimalSevenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(7), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.644), BigDecimal.valueOf(2.646));
    }

    @Test
    public void sqrtOfDecimalEightWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(8), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.827), BigDecimal.valueOf(2.829));
    }

    @Test
    public void sqrtOfDecimalNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(9), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(2.999), BigDecimal.valueOf(3.001));
    }

    @Test
    public void sqrtOfDecimalTenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.TEN, precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.161), BigDecimal.valueOf(3.163));
    }

    @Test
    public void sqrtOfDecimalSixteenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(16), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(3.999), BigDecimal.valueOf(4.001));
    }

    @Test
    public void sqrtOfDecimalTwentyFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(25), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(4.999), BigDecimal.valueOf(5.001));
    }

    @Test
    public void sqrtOfDecimalThirtySixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(36), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(5.999), BigDecimal.valueOf(6.001));
    }

    @Test
    public void sqrtOfDecimalFortyNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(49), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(6.999), BigDecimal.valueOf(7.001));
    }

    @Test
    public void sqrtOfDecimalSixtyFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(64), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(7.999), BigDecimal.valueOf(8.001));
    }

    @Test
    public void sqrtOfDecimalEightyOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(81), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(8.999), BigDecimal.valueOf(9.001));
    }

    @Test
    public void sqrtOfDecimalOneHundredWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator.sqrt(BigDecimal.valueOf(100), precision, scale, roundingMoude))
                .isBetween(BigDecimal.valueOf(9.999), BigDecimal.valueOf(10.001));
    }

    @Test
    public void sqrtOfPerfectSquareNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrtOfPerfectSquare(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("integer");
    }

    @Test
    public void sqrtOfPerfectSquareNotPerfectSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            SquareRootCalculator.sqrtOfPerfectSquare(BigInteger.valueOf(2));
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected perfect square but actual 2");
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
        final ScientificNotation actual = SquareRootCalculator.scientificNotationForSqrt(BigDecimal.ZERO);
        assertThat(actual).isEqualTo(new ScientificNotation(BigDecimal.ZERO, 0));
    }

    @Test
    public void scientificNotationForSqrtInbetweenZeroAndOneHundred() {
        final BigDecimal decimal = mathRandom.nextInvertiblePositiveDecimal(100, scale);
        final ScientificNotation actual = SquareRootCalculator.scientificNotationForSqrt(decimal);
        assertThat(actual).isEqualTo(new ScientificNotation(decimal, 0));
    }

    @Test
    public void scientificNotationForSqrtGreaterThanOneHundred() {
        final BigDecimal decimal = mathRandom.nextInvertiblePositiveDecimal(100, scale).add(BigDecimal.valueOf(100));
        final ScientificNotation actual = SquareRootCalculator.scientificNotationForSqrt(decimal);
        final ScientificNotation expected = new ScientificNotation(
                decimal.divide(BigDecimal.valueOf(100), roundingMoude), 2);
        assertThat(actual).isEqualTo(expected);
    }
}
