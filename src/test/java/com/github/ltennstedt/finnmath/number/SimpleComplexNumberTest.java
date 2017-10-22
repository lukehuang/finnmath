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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.linear.BigIntMatrix;
import com.github.ltennstedt.finnmath.util.MathRandom;
import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.BeforeClass;
import org.junit.Test;

public final class SimpleComplexNumberTest {
    private static final int howMany = 10;
    private static final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
    private static final List<SimpleComplexNumber> others = new ArrayList<>(howMany);
    private static final List<SimpleComplexNumber> invertibles = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom(7);
        final int bound = 10;
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(mathRandom.nextSimpleComplexNumber(bound));
            others.add(mathRandom.nextSimpleComplexNumber(bound));
            invertibles.add(mathRandom.nextInvertibleSimpleComplexNumber(bound));
        }
    }

    @Test
    public void newRealNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new SimpleComplexNumber(null, BigInteger.ZERO);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("real");
    }

    @Test
    public void newImaginaryNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new SimpleComplexNumber(BigInteger.ZERO, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("imaginary");
    }

    @Test
    public void newShouldSucceed() {
        final SimpleComplexNumber complexNumber = new SimpleComplexNumber(BigInteger.ONE, BigInteger.ONE);
        assertThat(complexNumber.getReal()).isExactlyInstanceOf(BigInteger.class);
        assertThat(complexNumber.getImaginary()).isExactlyInstanceOf(BigInteger.class);
    }

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.add(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("summand");
    }

    @Test
    public void addShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                final BigInteger expectedReal = complexNumber.getReal().add(other.getReal());
                final BigInteger expectedImaginary = complexNumber.getImaginary().add(other.getImaginary());
                assertThat(complexNumber.add(other))
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void addZeroShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.add(SimpleComplexNumber.ZERO)).isEqualTo(complexNumber);
        });
    }

    @Test
    public void addShouldBeCommutative() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                assertThat(complexNumber.add(other)).isEqualTo(other.add(complexNumber));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(complexNumber.add(other).add(invertible))
                            .isEqualTo(complexNumber.add(other.add(invertible)));
                });
            });
        });
    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.subtract(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("subtrahend");
    }

    @Test
    public void subtractShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                final BigInteger expectedReal = complexNumber.getReal().subtract(other.getReal());
                final BigInteger expectedImaginary = complexNumber.getImaginary().subtract(other.getImaginary());
                assertThat(complexNumber.subtract(other))
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void subtractZeroShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.subtract(SimpleComplexNumber.ZERO)).isEqualTo(complexNumber);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.subtract(complexNumber)).isEqualTo(SimpleComplexNumber.ZERO);
        });
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.multiply(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");
    }

    @Test
    public void multiplyShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                final BigInteger expectedReal = complexNumber.getReal().multiply(other.getReal())
                        .subtract(complexNumber.getImaginary().multiply(other.getImaginary()));
                final BigInteger expectedImaginary = complexNumber.getImaginary().multiply(other.getReal())
                        .add(complexNumber.getReal().multiply(other.getImaginary()));
                assertThat(complexNumber.multiply(other))
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void multiplyOneShouldBeEqualToOne() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.multiply(SimpleComplexNumber.ONE)).isEqualTo(complexNumber);
        });
    }

    @Test
    public void multiplyZeroShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.multiply(SimpleComplexNumber.ZERO)).isEqualTo(SimpleComplexNumber.ZERO);
        });
    }

    @Test
    public void multiplyShouldBeCommutative() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                assertThat(complexNumber.multiply(other)).isEqualTo(other.multiply(complexNumber));
            });
        });
    }

    @Test
    public void multiplyShouldBeAssociative() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(complexNumber.multiply(other).multiply(invertible))
                            .isEqualTo(complexNumber.multiply(other.multiply(invertible)));
                });
            });
        });
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(complexNumber.multiply(other.add(invertible)))
                            .isEqualTo(complexNumber.multiply(other).add(complexNumber.multiply(invertible)));
                });
            });
        });
    }

    @Test
    public void divideNullShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.divide(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("divisor");
    }

    @Test
    public void divideZeroShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ONE.divide(SimpleComplexNumber.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected divisor to be invertible but actual %s", SimpleComplexNumber.ZERO);
    }

    @Test
    public void divideShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            invertibles.forEach(invertible -> {
                final BigDecimal denominator =
                        new BigDecimal(invertible.getReal().pow(2).add(invertible.getImaginary().pow(2)));
                final BigDecimal expectedReal = new BigDecimal(complexNumber.getReal().multiply(invertible.getReal())
                        .add(complexNumber.getImaginary().multiply(invertible.getImaginary())))
                        .divide(denominator, BigDecimal.ROUND_HALF_UP);
                final BigDecimal expectedImaginary = new BigDecimal(
                        complexNumber.getImaginary().multiply(invertible.getReal())
                                .subtract(complexNumber.getReal().multiply(invertible.getImaginary())))
                        .divide(denominator, BigDecimal.ROUND_HALF_UP);
                assertThat(complexNumber.divide(invertible))
                        .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void divideOneShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.divide(SimpleComplexNumber.ONE)).isEqualTo(new RealComplexNumber(complexNumber));
        });
    }

    @Test
    public void powNegativeExponentShouldThrowException() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.pow(-1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected exponent > -1 but actual -1");
    }

    @Test
    public void powShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.pow(3)).isEqualTo(complexNumber.multiply(complexNumber).multiply(complexNumber));
            assertThat(complexNumber.pow(2)).isEqualTo(complexNumber.multiply(complexNumber));
        });
    }

    @Test
    public void powOneShouldBeTheSame() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.pow(1)).isSameAs(complexNumber);
        });
    }

    @Test
    public void powZeroShouldBeSameAsOne() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.pow(0)).isSameAs(SimpleComplexNumber.ONE);
        });
    }

    @Test
    public void powOfOneShouldBeEqualToOne() {
        assertThat(SimpleComplexNumber.ONE.pow(3)).isEqualTo(SimpleComplexNumber.ONE);
    }

    @Test
    public void powOfZeroForExponentNotEqualToZeroShouldBeEqualToZero() {
        assertThat(SimpleComplexNumber.ZERO.pow(3)).isEqualTo(SimpleComplexNumber.ZERO);
    }

    @Test
    public void powOfIForExponentNotEqualToZeroShouldBeEqualToMinusOne() {
        assertThat(SimpleComplexNumber.IMAGINARY.pow(2)).isEqualTo(SimpleComplexNumber.ONE.negate());
    }

    @Test
    public void negateShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            final SimpleComplexNumber expected =
                    new SimpleComplexNumber(complexNumber.getReal().negate(), complexNumber.getImaginary().negate());
            assertThat(complexNumber.negate()).isEqualTo(expected);
        });
    }

    @Test
    public void negateZeroShouldBeEqualToSelf() {
        assertThat(SimpleComplexNumber.ZERO.negate()).isEqualTo(SimpleComplexNumber.ZERO);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.negate().negate()).isEqualTo(complexNumber);
        });
    }

    @Test
    public void multiplyMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.multiply(SimpleComplexNumber.ONE.negate())).isEqualTo(complexNumber.negate());
        });
    }

    @Test
    public void divideMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.divide(SimpleComplexNumber.ONE.negate()))
                    .isEqualTo(new RealComplexNumber(complexNumber).negate());
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZero() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.add(complexNumber.negate())).isEqualTo(SimpleComplexNumber.ZERO);
        });
    }

    @Test
    public void invertZeroShouldReturnAnEmptyOptional() {
        assertThatThrownBy(() -> {
            SimpleComplexNumber.ZERO.invert();
        }).isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("expected to be invertible but actual %s", SimpleComplexNumber.ZERO);
    }

    @Test
    public void invertShouldSucceed() {
        invertibles.forEach(invertible -> {
            assertThat(invertible.invert()).isEqualTo(SimpleComplexNumber.ONE.divide(invertible));
        });
    }

    @Test
    public void invertOneShouldBeEqualToOne() {
        assertThat(SimpleComplexNumber.ONE.invert()).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void invertSelfShouldBeEqualToOneDividedBySelf() {
        invertibles.forEach(invertible -> {
            assertThat(invertible.invert()).isEqualTo(SimpleComplexNumber.ONE.divide(invertible));
        });
    }

    @Test
    public void invertibleZeroShouldBeFalse() {
        assertThat(SimpleComplexNumber.ZERO.invertible()).isFalse();
    }

    @Test
    public void invertibleShouldSucceed() {
        invertibles.forEach(invertible -> {
            assertThat(invertible.invertible()).isTrue();
        });
    }

    @Test
    public void invertibleShouldBePredictable() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.invertible()).isEqualTo(!complexNumber.equals(SimpleComplexNumber.ZERO));
        });
    }

    @Test
    public void absPow2ShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.absPow2())
                    .isEqualTo(complexNumber.getReal().pow(2).add(complexNumber.getImaginary().pow(2)));
        });
    }

    @Test
    public void absPow2ZeroShouldBeEqualToZero() {
        assertThat(SimpleComplexNumber.ZERO.absPow2()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void absPow2OneShouldBeEqualToOne() {
        assertThat(SimpleComplexNumber.ONE.absPow2()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void absPow2MinusOneShouldBeSameAsOne() {
        assertThat(SimpleComplexNumber.ONE.negate().absPow2()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void absShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.abs()).isEqualTo(new SquareRootCalculator().sqrt(complexNumber.absPow2()));
        });
    }

    @Test
    public void conjugateShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.conjugate())
                    .isEqualTo(new SimpleComplexNumber(complexNumber.getReal(), complexNumber.getImaginary().negate()));
        });
    }

    @Test
    public void conjugateRealNumberShouldBeEqualToSelf() {
        complexNumbers.forEach(complexNumber -> {
            final SimpleComplexNumber realNumber = new SimpleComplexNumber(complexNumber.getReal(), BigInteger.ZERO);
            assertThat(realNumber.conjugate()).isEqualTo(realNumber);
        });
    }

    @Test
    public void matrixShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            final BigInteger real = complexNumber.getReal();
            final BigInteger imaginary = complexNumber.getImaginary();
            final BigIntMatrix expected =
                    BigIntMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate()).put(2, 1, imaginary)
                            .put(2, 2, real).build();
            assertThat(complexNumber.matrix()).isEqualTo(expected);
        });
    }

    @Test
    public void hashCodeShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.hashCode())
                    .isEqualTo(Objects.hash(complexNumber.getReal(), complexNumber.getImaginary()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(complexNumber.equals(complexNumber)).isTrue();
        });
    }

    @Test
    public void equalsNotSimpleComplexNumberShouldReturnFalse() {
        assertThat(SimpleComplexNumber.ZERO.equals(new Object())).isFalse();
    }

    @Test
    public void equalsRealNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final BigInteger real = complexNumber.getReal().add(BigInteger.ONE);
            final SimpleComplexNumber other = new SimpleComplexNumber(real, complexNumber.getImaginary());
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsImaginaryNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final BigInteger imaginary = complexNumber.getImaginary().add(BigInteger.ONE);
            final SimpleComplexNumber other = new SimpleComplexNumber(complexNumber.getReal(), imaginary);
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        complexNumbers.forEach(complexNumber -> {
            final SimpleComplexNumber other =
                    new SimpleComplexNumber(complexNumber.getReal(), complexNumber.getImaginary());
            assertThat(complexNumber.equals(other)).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            final String expected = MoreObjects.toStringHelper(complexNumber).add("real", complexNumber.getReal())
                    .add("imaginary", complexNumber.getImaginary()).toString();
            assertThat(complexNumber.toString()).isEqualTo(expected);
        });
    }
}
