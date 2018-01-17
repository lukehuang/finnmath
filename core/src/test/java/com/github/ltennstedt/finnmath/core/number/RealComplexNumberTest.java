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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;
import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

public final class RealComplexNumberTest {
    private final int bound = 10;
    private final int scale = 2;
    private final int howMany = 10;
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<RealComplexNumber> complexNumbers = mathRandom.nextRealComplexNumbers(bound, scale, howMany);
    private final List<RealComplexNumber> others = mathRandom.nextRealComplexNumbers(bound, scale, howMany);
    private final List<RealComplexNumber> invertibles =
            mathRandom.nextInvertibleRealComplexNumbers(bound, scale, howMany);
    private final int precision = 10;
    private final RoundingMode roundingMode = RoundingMode.HALF_DOWN;

    @Test
    public void newRealNullShouldThrowException() {
        assertThatThrownBy(() -> new RealComplexNumber(null, BigDecimal.ZERO))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("real");
    }

    @Test
    public void newImaginaryNullShouldThrowException() {
        assertThatThrownBy(() -> new RealComplexNumber(BigDecimal.ZERO, null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("imaginary");
    }

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.add(null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("summand");
    }

    @Test
    public void addShouldSucceed() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> {
            final BigDecimal expectedReal = complexNumber.getReal().add(other.getReal());
            final BigDecimal expectedImaginary = complexNumber.getImaginary().add(other.getImaginary());
            assertThat(complexNumber.add(other)).isExactlyInstanceOf(RealComplexNumber.class)
                    .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
        }));
    }

    @Test
    public void addZeroShouldBeEqualToZero() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.add(RealComplexNumber.ZERO)).isEqualTo(complexNumber));
    }

    @Test
    public void addShouldBeCommutative() {
        complexNumbers.forEach(complexNumber -> others
                .forEach(other -> assertThat(complexNumber.add(other)).isEqualTo(other.add(complexNumber))));
    }

    @Test
    public void addShouldBeAssociative() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> invertibles.forEach(
                invertible -> assertThat(complexNumber.add(other).add(invertible))
                        .isEqualTo(complexNumber.add(other.add(invertible))))));
    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.subtract(null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("subtrahend");
    }

    @Test
    public void subtractShouldSucceed() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> {
            final BigDecimal expectedReal = complexNumber.getReal().subtract(other.getReal());
            final BigDecimal expectedImaginary = complexNumber.getImaginary().subtract(other.getImaginary());
            assertThat(complexNumber.subtract(other)).isExactlyInstanceOf(RealComplexNumber.class)
                    .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
        }));
    }

    @Test
    public void subtractZeroShouldBeEqualToSelf() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.subtract(RealComplexNumber.ZERO)).isEqualTo(complexNumber));
    }

    @Test
    public void subtractSelfShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber difference = complexNumber.subtract(complexNumber);
            final BigDecimal scaledReal =
                    BigDecimal.ZERO.setScale(difference.getReal().scale(), RoundingMode.UNNECESSARY);
            final BigDecimal scaledImaginary =
                    BigDecimal.ZERO.setScale(difference.getImaginary().scale(), RoundingMode.UNNECESSARY);
            assertThat(difference).isEqualTo(new RealComplexNumber(scaledReal, scaledImaginary));
        });
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.multiply(null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("factor");
    }

    @Test
    public void multiplyShouldSucceed() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> {
            final BigDecimal expectedReal = complexNumber.getReal().multiply(other.getReal())
                    .subtract(complexNumber.getImaginary().multiply(other.getImaginary()));
            final BigDecimal expectedImaginary = complexNumber.getImaginary().multiply(other.getReal())
                    .add(complexNumber.getReal().multiply(other.getImaginary()));
            assertThat(complexNumber.multiply(other)).isExactlyInstanceOf(RealComplexNumber.class)
                    .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
        }));
    }

    @Test
    public void multiplyOneShouldBeEqualToOne() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.multiply(RealComplexNumber.ONE)).isEqualTo(complexNumber));
    }

    @Test
    public void multiplyZeroShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber actual = complexNumber.multiply(RealComplexNumber.ZERO);
            final int realScale = actual.getReal().scale();
            final int imaginaryScale = actual.getImaginary().scale();
            final RealComplexNumber scaledZero =
                    new RealComplexNumber(BigDecimal.ZERO.setScale(realScale, RoundingMode.UNNECESSARY),
                            BigDecimal.ZERO.setScale(imaginaryScale, RoundingMode.UNNECESSARY));
            assertThat(actual).isEqualTo(scaledZero);
        });
    }

    @Test
    public void multiplyShouldBeCommutative() {
        complexNumbers.forEach(complexNumber -> others
                .forEach(other -> assertThat(complexNumber.multiply(other)).isEqualTo(other.multiply(complexNumber))));
    }

    @Test
    public void multiplyShouldBeAssociative() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> invertibles.forEach(
                invertible -> assertThat(complexNumber.multiply(other).multiply(invertible))
                        .isEqualTo(complexNumber.multiply(other.multiply(invertible))))));
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        complexNumbers.forEach(complexNumber -> others.forEach(other -> invertibles.forEach(
                invertible -> assertThat(complexNumber.multiply(other.add(invertible)))
                        .isEqualTo(complexNumber.multiply(other).add(complexNumber.multiply(invertible))))));
    }

    @Test
    public void divideNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.divide(null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("divisor");
    }

    @Test
    public void divideZeroShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.divide(RealComplexNumber.ZERO))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected divisor to be invertible but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void divideShouldSucceed() {
        complexNumbers.forEach(complexNumber -> invertibles.forEach(invertible -> {
            final BigDecimal denominator = invertible.getReal().pow(2).add(invertible.getImaginary().pow(2));
            final BigDecimal expectedReal = complexNumber.getReal().multiply(invertible.getReal())
                    .add(complexNumber.getImaginary().multiply(invertible.getImaginary()))
                    .divide(denominator, AbstractComplexNumber.DEFAULT_ROUNDING_MODE);
            final BigDecimal expectedImaginary = complexNumber.getImaginary().multiply(invertible.getReal())
                    .subtract(complexNumber.getReal().multiply(invertible.getImaginary()))
                    .divide(denominator, AbstractComplexNumber.DEFAULT_ROUNDING_MODE);
            assertThat(complexNumber.divide(invertible)).isExactlyInstanceOf(RealComplexNumber.class)
                    .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
        }));
    }

    @Test
    public void divideOneShouldBeEqualToSelf() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.divide(RealComplexNumber.ONE)).isEqualTo(complexNumber));
    }

    @Test
    public void powNegativeExponentShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.pow(-1)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected exponent > -1 but actual -1");
    }

    @Test
    public void powShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.pow(3)).isExactlyInstanceOf(RealComplexNumber.class)
                    .isEqualTo(complexNumber.multiply(complexNumber).multiply(complexNumber));
            assertThat(complexNumber.pow(2)).isEqualTo(complexNumber.multiply(complexNumber));
        });
    }

    @Test
    public void powOneShouldBeTheSame() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.pow(1)).isSameAs(complexNumber));
    }

    @Test
    public void powZeroShouldBeSameAsOne() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.pow(0)).isSameAs(RealComplexNumber.ONE));
    }

    @Test
    public void powOfOneShouldBeEqualToOne() {
        assertThat(RealComplexNumber.ONE.pow(3)).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void powOfZeroForExponentNotEqualToZeroShouldBeEqualToZero() {
        assertThat(RealComplexNumber.ZERO.pow(3)).isEqualTo(RealComplexNumber.ZERO);
    }

    @Test
    public void powOfIForExponentNotEqualToZeroShouldBeEqualToMinusOne() {
        assertThat(RealComplexNumber.IMAGINARY.pow(2)).isEqualTo(RealComplexNumber.ONE.negate());
    }

    @Test
    public void negateShouldSucceed() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.negate()).isExactlyInstanceOf(RealComplexNumber.class)
                        .isEqualTo(new RealComplexNumber(complexNumber.getReal().negate(),
                                complexNumber.getImaginary().negate())));
    }

    @Test
    public void negateZeroShouldBeEqualToSelf() {
        assertThat(RealComplexNumber.ZERO.negate()).isEqualTo(RealComplexNumber.ZERO);
    }

    @Test
    public void addNegatedShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber actual = complexNumber.add(complexNumber.negate());
            final int realScale = actual.getReal().scale();
            final int imaginaryScale = actual.getImaginary().scale();
            final RealComplexNumber expected =
                    new RealComplexNumber(BigDecimal.ZERO.setScale(realScale, RoundingMode.UNNECESSARY),
                            BigDecimal.ZERO.setScale(imaginaryScale, RoundingMode.UNNECESSARY));
            assertThat(actual).isEqualTo(expected);
        });
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.negate().negate()).isEqualTo(complexNumber));
    }

    @Test
    public void multiplyMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.multiply(RealComplexNumber.ONE.negate()))
                .isEqualTo(complexNumber.negate()));
    }

    @Test
    public void divideMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.divide(RealComplexNumber.ONE.negate()))
                .isEqualTo(complexNumber.negate()));
    }

    @Test
    public void invertZeroShouldThrowException() {
        assertThatThrownBy(RealComplexNumber.ZERO::invert).isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected to be invertible but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void invertShouldSucceed() {
        invertibles.forEach(
                complexNumber -> assertThat(complexNumber.invert()).isExactlyInstanceOf(RealComplexNumber.class)
                        .isEqualTo(RealComplexNumber.ONE.divide(complexNumber)));
    }

    @Test
    public void invertOneShouldBeEqualToOne() {
        assertThat(RealComplexNumber.ONE.invert()).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void invertSelfShouldBeEqualToOneDividedBySelf() {
        invertibles.forEach(
                invertible -> assertThat(invertible.invert()).isEqualTo(RealComplexNumber.ONE.divide(invertible)));
    }

    @Test
    public void invertibleZeroShouldBeFalse() {
        assertThat(RealComplexNumber.ZERO.invertible()).isFalse();
    }

    @Test
    public void invertibleShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.invertible()).isTrue());
    }

    @Test
    public void invertibleShouldBePredictable() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.invertible())
                .isEqualTo(!complexNumber.equals(RealComplexNumber.ZERO)));
    }

    @Test
    public void absPow2ShouldSucceed() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.absPow2()).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(complexNumber.getReal().pow(2).add(complexNumber.getImaginary().pow(2))));
    }

    @Test
    public void absPow2ZeroShouldBeEqualToZero() {
        assertThat(RealComplexNumber.ZERO.absPow2()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void absPow2OneShouldBeEqualToOne() {
        assertThat(RealComplexNumber.ONE.absPow2()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void absPow2MinusOneShouldBeSameAsOne() {
        assertThat(RealComplexNumber.ONE.negate().absPow2()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void absShouldSucceed() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.abs()).isExactlyInstanceOf(BigDecimal.class)
                .isEqualTo(new SquareRootCalculator().sqrt(complexNumber.absPow2())));
    }

    @Test
    public void conjugateShouldSucceed() {
        complexNumbers.forEach(
                complexNumber -> assertThat(complexNumber.conjugate()).isExactlyInstanceOf(RealComplexNumber.class)
                        .isEqualTo(
                                new RealComplexNumber(complexNumber.getReal(), complexNumber.getImaginary().negate())));
    }

    @Test
    public void conjugateRealNumberShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber realNumber = new RealComplexNumber(complexNumber.getReal(), BigDecimal.ZERO);
            assertThat(realNumber.conjugate()).isEqualTo(realNumber);
        });
    }

    @Test
    public void argumentZeroShouldThrowException() {
        assertThatThrownBy(RealComplexNumber.ZERO::argument).isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void argumentShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.argument()).isEqualTo(invertible
                .argument(new MathContext(PolarForm.DEFAULT_PRECISION, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))));
    }

    @Test
    public void argumentZeroWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.argument(PolarForm.DEFAULT_PRECISION))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void argumentZeroPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.argument(-1)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void argumentWithPrecisionShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.argument(precision)).isEqualTo(
                invertible.argument(new MathContext(precision, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))));
    }

    @Test
    public void argumentZeroWithRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.argument(AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void argumentRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.argument((RoundingMode) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void argumentWithRoundingModeShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.argument(roundingMode))
                .isEqualTo(invertible.argument(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode))));
    }

    @Test
    public void argumentZeroWithPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO
                .argument(PolarForm.DEFAULT_PRECISION, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void argumentZeroWithPrecisionTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.argument(-1, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void argumentWithPrecisionAndRoundingModeNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.argument(PolarForm.DEFAULT_PRECISION, (RoundingMode) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void argumentWithPrecisionAndRoundingModeShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.argument(precision, roundingMode))
                .isEqualTo(invertible.argument(new MathContext(precision, roundingMode))));
    }

    @Test
    public void argumentZeroWithMathContextShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.argument(new MathContext(PolarForm.DEFAULT_PRECISION)))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void argumentWithMathContextNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.argument((MathContext) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("mathContext");
    }

    @Test
    public void argumentWithMathContextShouldSucceed() {
        invertibles.forEach(invertible -> {
            final MathContext mathContext = new MathContext(precision, roundingMode);
            final Context context = BigFloat.context(mathContext);
            final BigDecimal real = invertible.getReal();
            final BigDecimal imaginary = invertible.getImaginary();
            final BigDecimal expected;
            if (real.compareTo(BigDecimal.ZERO) != 0) {
                final BigFloat arctan = BigFloat.atan(context.valueOf(imaginary.divide(real, mathContext)));
                if (real.compareTo(BigDecimal.ZERO) > 0) {
                    expected = arctan.toBigDecimal();
                } else {
                    final BigFloat pi = context.pi();
                    expected = imaginary.compareTo(BigDecimal.ZERO) > -1 ? arctan.add(pi).toBigDecimal() :
                            arctan.subtract(pi).toBigDecimal();
                }
            } else {
                final BigDecimal piDividedByTwo =
                        context.pi().divide(context.valueOf(BigDecimal.valueOf(2L))).toBigDecimal();
                expected = imaginary.compareTo(BigDecimal.ZERO) > 0 ? piDividedByTwo : piDividedByTwo.negate();
            }
            assertThat(invertible.argument(mathContext)).isEqualTo(expected);
        });
    }

    @Test
    public void polarFormZeroShouldThrowException() {
        assertThatThrownBy(RealComplexNumber.ZERO::polarForm).isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void polarFormShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.polarForm()).isEqualTo(invertible
                .polarForm(new MathContext(PolarForm.DEFAULT_PRECISION, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))));
    }

    @Test
    public void polarFormZeroWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.polarForm(precision))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void polarFormZeroPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.polarForm(-1))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void polarFormWithPrecisionShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.polarForm(precision)).isEqualTo(
                invertible.polarForm(new MathContext(precision, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))));
    }

    @Test
    public void polarFormZeroWithRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.polarForm(AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void polarFormRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.polarForm((RoundingMode) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void polarFormWithRoundingModeShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.polarForm(roundingMode))
                .isEqualTo(invertible.polarForm(new MathContext(PolarForm.DEFAULT_PRECISION, roundingMode))));
    }

    @Test
    public void polarFormZeroWithPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO
                .polarForm(PolarForm.DEFAULT_PRECISION, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void polarFormZeroWithPrecisionTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.polarForm(-1, AbstractComplexNumber.DEFAULT_ROUNDING_MODE))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void polarFormWithPrecisionAndRoundingModeNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.polarForm(PolarForm.DEFAULT_PRECISION, (RoundingMode) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void polarFormWithPrecisionAndRoundingModeShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.polarForm(precision, roundingMode))
                .isEqualTo(invertible.polarForm(new MathContext(precision, roundingMode))));
    }

    @Test
    public void polarFormZeroWithMathContextShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.polarForm(new MathContext(PolarForm.DEFAULT_PRECISION)))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected this != 0 but actual %s", RealComplexNumber.ZERO);
    }

    @Test
    public void polarFormWithMathContextNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ONE.polarForm((MathContext) null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("mathContext");
    }

    @Test
    public void polarFormWithMathContextShouldSucceed() {
        invertibles.forEach(invertible -> {
            final MathContext mathContext = new MathContext(precision, roundingMode);
            assertThat(invertible.polarForm(mathContext))
                    .isEqualTo(new PolarForm(invertible.abs(), invertible.argument(mathContext)));
        });
    }

    @Test
    public void matrixShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            final BigDecimal real = complexNumber.getReal();
            final BigDecimal imaginary = complexNumber.getImaginary();
            final BigDecimalMatrix expected =
                    BigDecimalMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
                            .put(2, 2, real).build();
            assertThat(complexNumber.matrix()).isExactlyInstanceOf(BigDecimalMatrix.class).isEqualTo(expected);
        });
    }

    @Test
    public void isEqualToByComparingPartsNullShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumber.ZERO.isEqualToByComparingParts(null))
                .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void isEqualToByComparingPartsRealPartsNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> assertThat(
                complexNumber.isEqualToByComparingParts(complexNumber.add(RealComplexNumber.ONE))).isFalse());
    }

    @Test
    public void isEqualToByComparingPartsImaginaryPartsNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> assertThat(
                complexNumber.isEqualToByComparingParts(complexNumber.add(RealComplexNumber.IMAGINARY))).isFalse());
    }

    @Test
    public void isEqualToByComparingPartsRealPartNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final int otherScale = complexNumber.getReal().scale() + 1;
            final BigDecimal otherReal = complexNumber.getReal().setScale(otherScale, RoundingMode.HALF_UP);
            final BigDecimal otherImaginary = complexNumber.getImaginary().setScale(otherScale, RoundingMode.HALF_UP);
            assertThat(complexNumber.isEqualToByComparingParts(new RealComplexNumber(otherReal, otherImaginary)))
                    .isTrue();
        });
    }

    @Test
    public void hashCodeShouldSucceed() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.hashCode())
                .isEqualTo(Objects.hash(complexNumber.getReal(), complexNumber.getImaginary())));
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.equals(complexNumber)).isTrue());
    }

    @Test
    public void equalsNotSimpleComplexNumberShouldReturnFalse() {
        assertThat(SimpleComplexNumber.ZERO.equals(new Object())).isFalse();
    }

    @Test
    public void equalsRealNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final BigDecimal real = complexNumber.getReal().add(BigDecimal.ONE);
            final RealComplexNumber other = new RealComplexNumber(real, complexNumber.getImaginary());
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsImaginaryNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final BigDecimal imaginary = complexNumber.getImaginary().add(BigDecimal.ONE);
            final RealComplexNumber other = new RealComplexNumber(complexNumber.getReal(), imaginary);
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        complexNumbers.forEach(complexNumber -> assertThat(
                complexNumber.equals(new RealComplexNumber(complexNumber.getReal(), complexNumber.getImaginary())))
                .isTrue());
    }

    @Test
    public void hashCodeEqualRealComplexNumbersShouldHaveEqualHashCodes() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber other =
                    new RealComplexNumber(complexNumber.getReal(), complexNumber.getImaginary());
            assertThat(complexNumber.hashCode()).isEqualTo(other.hashCode());
        });
    }

    @Test
    public void toStringShouldSucceed() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.toString()).isEqualTo(
                MoreObjects.toStringHelper(complexNumber).add("real", complexNumber.getReal())
                        .add("imaginary", complexNumber.getImaginary()).toString()));
    }
}
