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

package finnmath.util

import finnmath.number.ScientificNotation
import java.math.BigDecimal
import java.math.BigInteger
import org.junit.After
import org.junit.Test
import org.slf4j.LoggerFactory

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

final class SquareRootCalculatorTest {
    val precision = 0.00000001BD
    val scale = 8
    val roundingMoude = BigDecimal::ROUND_HALF_EVEN
    val mathRandom = new MathRandom
    private static val log = LoggerFactory::getLogger(SquareRootCalculatorTest)

    @After
    def void after() {
        log.debug('')
    }

    @Test
    def void sqrtOfBigIntNullShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigInteger)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('integer')
    }

    @Test
    def void sqrtOfBigIntNegativeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(-1BI)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected integer >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntZero() {
        assertThat(SquareRootCalculator::sqrt(0BI)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfBigIntOne() {
        assertThat(SquareRootCalculator::sqrt(1BI)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfBigIntTwo() {
        assertThat(SquareRootCalculator::sqrt(2BI)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfBigIntThree() {
        assertThat(SquareRootCalculator::sqrt(3BI)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfBigIntFour() {
        assertThat(SquareRootCalculator::sqrt(4BI)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfBigIntFive() {
        assertThat(SquareRootCalculator::sqrt(5BI)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfBigIntSix() {
        assertThat(SquareRootCalculator::sqrt(6BI)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfBigIntSeven() {
        assertThat(SquareRootCalculator::sqrt(7BI)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfBigIntEight() {
        assertThat(SquareRootCalculator::sqrt(8BI)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfBigIntNine() {
        assertThat(SquareRootCalculator::sqrt(9BI)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfBigIntTen() {
        assertThat(SquareRootCalculator::sqrt(10BI)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfBigIntSixteen() {
        assertThat(SquareRootCalculator::sqrt(16BI)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfBigIntTwentyFive() {
        assertThat(SquareRootCalculator::sqrt(25BI)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfBigIntThirtySix() {
        assertThat(SquareRootCalculator::sqrt(36BI)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfBigIntFortyNine() {
        assertThat(SquareRootCalculator::sqrt(49BI)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfBigIntSixtyFour() {
        assertThat(SquareRootCalculator::sqrt(64BI)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfBigIntEightyOne() {
        assertThat(SquareRootCalculator::sqrt(81BI)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfBigIntOneHundred() {
        assertThat(SquareRootCalculator::sqrt(100BI)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfBigIntNullWithPrecisonShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigInteger, precision)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('integer')
    }

    @Test
    def void sqrtOfBigIntNegativeWithPrecisionShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(-1BI, precision)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected integer >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithPrecisonNullShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('precision')
    }

    @Test
    def void sqrtOfBigIntPrecisonTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 0BD)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 0')
    }

    @Test
    def void sqrtOfBigIntPrecisonTooHighShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 1BD)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 1')
    }

    @Test
    def void sqrtOfBigIntZeroWithPrecision() {
        assertThat(SquareRootCalculator::sqrt(0BI, precision)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfBigIntOnewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(1BI, precision)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfBigIntTwowithPrecision() {
        assertThat(SquareRootCalculator::sqrt(2BI, precision)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfBigIntThreewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(3BI, precision)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfBigIntFourwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(4BI, precision)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfBigIntFivewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(5BI, precision)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfBigIntSixwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(6BI, precision)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfBigIntSevenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(7BI, precision)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfBigIntEightwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(8BI, precision)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfBigIntNinewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(9BI, precision)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfBigIntTenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(10BI, precision)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfBigIntSixteenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(16BI, precision)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfBigIntTwentyFivewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(25BI, precision)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfBigIntThirtySixwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(36BI, precision)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfBigIntFortyNinewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(49BI, precision)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfBigIntSixtyFourwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(64BI, precision)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfBigIntEightyOnewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(81BI, precision)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfBigIntOneHundredwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(100BI, precision)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfBigIntNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigInteger, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('integer')
    }

    @Test
    def void sqrtOfBigIntNegativeWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(-1BI, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected integer >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, -1, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 0, -1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 0, 8)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual 8')
    }

    @Test
    def void sqrtOfBigIntZeroWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(0BI, scale, roundingMoude)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfBigIntOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(1BI, scale, roundingMoude)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfBigIntTwoWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(2BI, scale, roundingMoude)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfBigIntThreeWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(3BI, scale, roundingMoude)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfBigIntFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(4BI, scale, roundingMoude)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfBigIntFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(5BI, scale, roundingMoude)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfBigIntSixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(6BI, scale, roundingMoude)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfBigIntSevenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(7BI, scale, roundingMoude)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfBigIntEightWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(8BI, scale, roundingMoude)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfBigIntNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(9BI, scale, roundingMoude)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfBigIntTenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(10BI, scale, roundingMoude)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfBigIntSixteenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(16BI, scale, roundingMoude)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfBigIntTwentyFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(25BI, scale, roundingMoude)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfBigIntThirtySixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(36BI, scale, roundingMoude)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfBigIntFortyNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(49BI, scale, roundingMoude)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfBigIntSixtyFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(64BI, scale, roundingMoude)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfBigIntEightyOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(81BI, scale, roundingMoude)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfBigIntOneHundredWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(100BI, scale, roundingMoude)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfBigIntNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigInteger, precision, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('integer')
    }

    @Test
    def void sqrtOfBigIntNegativeWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(-1BI, precision, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected integer >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, null, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('precision')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 0BD, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 0')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, 1BD, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 1')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, precision, -1, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale >= 0 but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionAndScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, precision, 0, -1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual -1')
    }

    @Test
    def void sqrtOfBigIntWithPrecisionAndScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BI, precision, 0, 8)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual 8')
    }

    @Test
    def void sqrtOfBigIntZeroWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(0BI, precision, scale, roundingMoude)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfBigIntOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(1BI, precision, scale, roundingMoude)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfBigIntTwoWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(2BI, precision, scale, roundingMoude)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfBigIntThreeWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(3BI, precision, scale, roundingMoude)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfBigIntFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(4BI, precision, scale, roundingMoude)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfBigIntFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(5BI, precision, scale, roundingMoude)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfBigIntSixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(6BI, precision, scale, roundingMoude)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfBigIntSevenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(7BI, precision, scale, roundingMoude)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfBigIntEightWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(8BI, precision, scale, roundingMoude)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfBigIntNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(9BI, precision, scale, roundingMoude)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfBigIntTenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(10BI, precision, scale, roundingMoude)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfBigIntSixteenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(16BI, precision, scale, roundingMoude)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfBigIntTwentyFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(25BI, precision, scale, roundingMoude)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfBigIntThirtySixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(36BI, precision, scale, roundingMoude)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfBigIntFortyNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(49BI, precision, scale, roundingMoude)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfBigIntSixtyFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(64BI, precision, scale, roundingMoude)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfBigIntEightyOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(81BI, precision, scale, roundingMoude)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfBigIntOneHundredWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(100BI, precision, scale, roundingMoude)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfDecimalNullShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigDecimal)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('decimal')
    }

    @Test
    def void sqrtOfDecimalZero() {
        assertThat(SquareRootCalculator::sqrt(0BD)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfDecimalOne() {
        assertThat(SquareRootCalculator::sqrt(1BD)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfDecimalTwo() {
        assertThat(SquareRootCalculator::sqrt(2BD)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfDecimalThree() {
        assertThat(SquareRootCalculator::sqrt(3BD)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfDecimalFour() {
        assertThat(SquareRootCalculator::sqrt(4BD)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfDecimalFive() {
        assertThat(SquareRootCalculator::sqrt(5BD)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfDecimalSix() {
        assertThat(SquareRootCalculator::sqrt(6BD)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfDecimalSeven() {
        assertThat(SquareRootCalculator::sqrt(7BD)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfDecimalEight() {
        assertThat(SquareRootCalculator::sqrt(8BD)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfDecimalNine() {
        assertThat(SquareRootCalculator::sqrt(9BD)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfDecimalTen() {
        assertThat(SquareRootCalculator::sqrt(10BD)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfDecimalSixteen() {
        assertThat(SquareRootCalculator::sqrt(16BD)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfDecimalTwentyFive() {
        assertThat(SquareRootCalculator::sqrt(25BD)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfDecimalThirtySix() {
        assertThat(SquareRootCalculator::sqrt(36BD)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfDecimalFortyNine() {
        assertThat(SquareRootCalculator::sqrt(49BD)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfDecimalSixtyFour() {
        assertThat(SquareRootCalculator::sqrt(64BD)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfDecimalEightyOne() {
        assertThat(SquareRootCalculator::sqrt(81BD)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfDecimalOneHundred() {
        assertThat(SquareRootCalculator::sqrt(100BD)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfNullWithPrecisonShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigDecimal, precision)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('decimal')
    }

    @Test
    def void sqrtOfDecimalWithPrecisonNullShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('precision')
    }

    @Test
    def void sqrtOfDecimalPrecisonTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 0BD)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 0')
    }

    @Test
    def void sqrtOfDecimalPrecisonTooHighShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 1BD)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 1')
    }

    @Test
    def void sqrtOfDecimalZeroWithPrecision() {
        assertThat(SquareRootCalculator::sqrt(0BD, precision)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfDecimalOnewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(1BD, precision)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfDecimalTwowithPrecision() {
        assertThat(SquareRootCalculator::sqrt(2BD, precision)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfDecimalThreewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(3BD, precision)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfDecimalFourwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(4BD, precision)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfDecimalFivewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(5BD, precision)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfDecimalSixwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(6BD, precision)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfDecimalSevenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(7BD, precision)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfDecimalEightwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(8BD, precision)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfDecimalNinewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(9BD, precision)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfDecimalTenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(10BD, precision)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfDecimalSixteenwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(16BD, precision)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfDecimalTwentyFivewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(25BD, precision)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfDecimalThirtySixwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(36BD, precision)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfDecimalFortyNinewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(49BD, precision)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfDecimalSixtyFourwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(64BD, precision)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfDecimalEightyOnewithPrecision() {
        assertThat(SquareRootCalculator::sqrt(81BD, precision)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfDecimalOneHundredwithPrecision() {
        assertThat(SquareRootCalculator::sqrt(100BD, precision)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfDecimalNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigDecimal, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('decimal')
    }

    @Test
    def void sqrtOfDecimalWithScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, -1, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale >= 0 but actual -1')
    }

    @Test
    def void sqrtOfDecimalWithScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 0, -1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual -1')
    }

    @Test
    def void sqrtOfDecimalWithScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 0, 8)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual 8')
    }

    @Test
    def void sqrtOfDecimalZeroWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(0BD, scale, roundingMoude)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfDecimalOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(1BD, scale, roundingMoude)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfDecimalTwoWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(2BD, scale, roundingMoude)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfDecimalThreeWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(3BD, scale, roundingMoude)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfDecimalFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(4BD, scale, roundingMoude)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfDecimalFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(5BD, scale, roundingMoude)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfDecimalSixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(6BD, scale, roundingMoude)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfDecimalSevenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(7BD, scale, roundingMoude)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfDecimalEightWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(8BD, scale, roundingMoude)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfDecimalNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(9BD, scale, roundingMoude)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfDecimalTenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(10BD, scale, roundingMoude)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfDecimalSixteenWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(16BD, scale, roundingMoude)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfDecimalTwentyFiveWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(25BD, scale, roundingMoude)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfDecimalThirtySixWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(36BD, scale, roundingMoude)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfDecimalFortyNineWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(49BD, scale, roundingMoude)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfDecimalSixtyFourWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(64BD, scale, roundingMoude)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfDecimalEightyOneWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(81BD, scale, roundingMoude)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfDecimalOneHundredWithScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(100BD, scale, roundingMoude)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfDecimalNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(null as BigDecimal, precision, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('decimal')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, null, scale, roundingMoude)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('precision')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 0BD, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 0')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, 1BD, scale, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected precision in (0, 1) but actual 1')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, precision, -1, roundingMoude)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected scale >= 0 but actual -1')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionAndScaleAndRoundingModeTooLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, precision, 0, -1)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual -1')
    }

    @Test
    def void sqrtOfDecimalWithPrecisionAndScaleAndRoundingModeHighLowShouldThrowException() {
        assertThatThrownBy[
            SquareRootCalculator::sqrt(0BD, precision, 0, 8)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected roundingMode in [0, 7] but actual 8')
    }

    @Test
    def void sqrtOfDecimalZeroWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(0BD, precision, scale, roundingMoude)).isBetween(-0.001BD, 0.001BD)
    }

    @Test
    def void sqrtOfDecimalOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(1BD, precision, scale, roundingMoude)).isBetween(0.999BD, 1.001BD)
    }

    @Test
    def void sqrtOfDecimalTwoWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(2BD, precision, scale, roundingMoude)).isBetween(1.413BD, 1.415BD)
    }

    @Test
    def void sqrtOfDecimalThreeWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(3BD, precision, scale, roundingMoude)).isBetween(1.731BD, 1.733BD)
    }

    @Test
    def void sqrtOfDecimalFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(4BD, precision, scale, roundingMoude)).isBetween(1.999BD, 2.001BD)
    }

    @Test
    def void sqrtOfDecimalFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(5BD, precision, scale, roundingMoude)).isBetween(2.235BD, 2.237BD)
    }

    @Test
    def void sqrtOfDecimalSixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(6BD, precision, scale, roundingMoude)).isBetween(2.448BD, 2.45BD)
    }

    @Test
    def void sqrtOfDecimalSevenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(7BD, precision, scale, roundingMoude)).isBetween(2.644BD, 2.646BD)
    }

    @Test
    def void sqrtOfDecimalEightWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(8BD, precision, scale, roundingMoude)).isBetween(2.827BD, 2.829BD)
    }

    @Test
    def void sqrtOfDecimalNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(9BD, precision, scale, roundingMoude)).isBetween(2.999BD, 3.001BD)
    }

    @Test
    def void sqrtOfDecimalTenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(10BD, precision, scale, roundingMoude)).isBetween(3.161BD, 3.163BD)
    }

    @Test
    def void sqrtOfDecimalSixteenWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(16BD, precision, scale, roundingMoude)).isBetween(3.999BD, 4.001BD)
    }

    @Test
    def void sqrtOfDecimalTwentyFiveWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(25BD, precision, scale, roundingMoude)).isBetween(4.999BD, 5.001BD)
    }

    @Test
    def void sqrtOfDecimalThirtySixWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(36BD, precision, scale, roundingMoude)).isBetween(5.999BD, 6.001BD)
    }

    @Test
    def void sqrtOfDecimalFortyNineWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(49BD, precision, scale, roundingMoude)).isBetween(6.999BD, 7.001BD)
    }

    @Test
    def void sqrtOfDecimalSixtyFourWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(64BD, precision, scale, roundingMoude)).isBetween(7.999BD, 8.001BD)
    }

    @Test
    def void sqrtOfDecimalEightyOneWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(81BD, precision, scale, roundingMoude)).isBetween(8.999BD, 9.001BD)
    }

    @Test
    def void sqrtOfDecimalOneHundredWithPrecisionAndScaleAndRoundingMode() {
        assertThat(SquareRootCalculator::sqrt(100BD, precision, scale, roundingMoude)).isBetween(9.999BD, 10.001BD)
    }

    @Test
    def void sqrtOfPerfectSquareNullShouldThrowException() {
        assertThatThrownBy [
            SquareRootCalculator::sqrtOfPerfectSquare(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('integer')
    }

    @Test
    def void sqrtOfPerfectSquareNotPerfectSquareShouldThrowException() {
        assertThatThrownBy [
            SquareRootCalculator::sqrtOfPerfectSquare(2BI)
        ].isExactlyInstanceOf(IllegalArgumentException).hasMessage('expected perfect square but actual 2')
    }

    @Test
    def void sqrtOfPerfectSquareZero() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(0BI)).isEqualTo(0BI)
    }

    @Test
    def void sqrtOfPerfectSquareOne() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(1BI)).isEqualTo(1BI)
    }

    @Test
    def void sqrtOfPerfectSquareFour() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(4BI)).isEqualTo(2BI)
    }

    @Test
    def void sqrtOfPerfectSquareNine() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(9BI)).isEqualTo(3BI)
    }

    @Test
    def void sqrtOfPerfectSquareSixteen() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(16BI)).isEqualTo(4BI)
    }

    @Test
    def void sqrtOfPerfectSquareTwentyFive() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(25BI)).isEqualTo(5BI)
    }

    @Test
    def void sqrtOfPerfectSquareThirtySix() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(36BI)).isEqualTo(6BI)
    }

    @Test
    def void sqrtOfPerfectSquareFortyNine() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(49BI)).isEqualTo(7BI)
    }

    @Test
    def void sqrtOfPerfectSquareSixtyFour() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(64BI)).isEqualTo(8BI)
    }

    @Test
    def void sqrtOfPerfectSquareEightyOne() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(81BI)).isEqualTo(9BI)
    }

    @Test
    def void sqrtOfPerfectSquareOneHundred() {
        assertThat(SquareRootCalculator::sqrtOfPerfectSquare(100BI)).isEqualTo(10BI)
    }

    @Test
    def void scientificNotationForSqrtForZero() {
        val actual = SquareRootCalculator::scientificNotationForSqrt(0BD)
        assertThat(actual).isEqualTo(new ScientificNotation(0BD, 0))
    }

    @Test
    def void scientificNotationForSqrtInbetweenZeroAndOneHundred() {
        val decimal = mathRandom.nextInvertiblePositiveDecimal(100, scale)
        val actual = SquareRootCalculator::scientificNotationForSqrt(decimal)
        assertThat(actual).isEqualTo(new ScientificNotation(decimal, 0))
    }

    @Test
    def void scientificNotationForSqrtGreaterThanOneHundred() {
        val decimal = mathRandom.nextInvertiblePositiveDecimal(100, scale) + 100BD
        val actual = SquareRootCalculator::scientificNotationForSqrt(decimal)
        val expected = new ScientificNotation(decimal / 100BD, 2)
        assertThat(actual).isEqualTo(expected)
    }
}
