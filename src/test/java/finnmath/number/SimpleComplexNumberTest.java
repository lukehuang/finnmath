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

package finnmath.number;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finnmath.linear.BigIntMatrix;
import finnmath.util.MathRandom;
import finnmath.util.SquareRootCalculator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public final class SimpleComplexNumberTest {
    private static final int howMany = 10;
    private static final SimpleComplexNumber ZERO = SimpleComplexNumber.ZERO;
    private static final SimpleComplexNumber ONE = SimpleComplexNumber.ONE;
    private static final SimpleComplexNumber I = SimpleComplexNumber.I;
    private static final List<SimpleComplexNumber> complexNumbers = new ArrayList<>(howMany);
    private static final List<SimpleComplexNumber> others = new ArrayList<>(howMany);
    private static final List<SimpleComplexNumber> invertibles = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom();
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
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.add(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("summand");
    }

    @Test
    public void addShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                final BigInteger expectedReal = complexNumber.getReal().add(other.getReal());
                final BigInteger expectedImaginary = complexNumber.getImaginary().add(other.getImaginary());
                assertThat(complexNumber.add(other)).isExactlyInstanceOf(SimpleComplexNumber.class)
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void addZeroShouldBeEqualToZero() {
        complexNumbers.forEach(it -> {
            assertThat(it.add(ZERO)).isEqualTo(it);
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
            ZERO.subtract(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("subtrahend");
    }

    @Test
    public void subtractShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            others.forEach(other -> {
                final BigInteger expectedReal = complexNumber.getReal().subtract(other.getReal());
                final BigInteger expectedImaginary = complexNumber.getImaginary().subtract(other.getImaginary());
                assertThat(complexNumber.subtract(other)).isExactlyInstanceOf(SimpleComplexNumber.class)
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void subtractZeroShouldBeEqualToSelf() {
        complexNumbers.forEach(it -> {
            assertThat(it.subtract(ZERO)).isEqualTo(it);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZero() {
        complexNumbers.forEach(it -> {
            assertThat(it.subtract(it)).isEqualTo(ZERO);
        });
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.multiply(null);
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
                assertThat(complexNumber.multiply(other)).isExactlyInstanceOf(SimpleComplexNumber.class)
                        .isEqualTo(new SimpleComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void multiplyOneShouldBeEqualToOne() {
        complexNumbers.forEach(it -> {
            assertThat(it.multiply(ONE)).isEqualTo(it);
        });
    }

    @Test
    public void multiplyZeroShouldBeEqualToZero() {
        complexNumbers.forEach(it -> {
            assertThat(it.multiply(ZERO)).isEqualTo(ZERO);
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
            ZERO.divide(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("divisor");
    }

    @Test
    public void divideZeroShouldThrowException() {
        assertThatThrownBy(() -> {
            ONE.divide(ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected divisor to be invertible but actual %s", ZERO);
    }

    @Test
    public void divideShouldSucceed() {
        complexNumbers.forEach(complexNumber -> {
            invertibles.forEach(invertible -> {
                final BigDecimal denominator = new BigDecimal(
                        invertible.getReal().pow(2).add(invertible.getImaginary().pow(2)));
                final BigDecimal expectedReal = new BigDecimal(complexNumber.getReal().multiply(invertible.getReal())
                        .add(complexNumber.getImaginary().multiply(invertible.getImaginary()))).divide(denominator,
                                BigDecimal.ROUND_HALF_UP);
                final BigDecimal expectedImaginary = new BigDecimal(
                        complexNumber.getImaginary().multiply(invertible.getReal())
                                .subtract(complexNumber.getReal().multiply(invertible.getImaginary())))
                                        .divide(denominator, BigDecimal.ROUND_HALF_UP);
                assertThat(complexNumber.divide(invertible)).isExactlyInstanceOf(RealComplexNumber.class)
                        .isEqualTo(new RealComplexNumber(expectedReal, expectedImaginary));
            });
        });
    }

    @Test
    public void divideOneShouldBeEqualToSelf() {
        complexNumbers.forEach(it -> {
            assertThat(it.divide(ONE)).isEqualTo(new RealComplexNumber(it));
        });
    }

    @Test
    public void powNegativeExponentShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.pow(-1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected exponent > -1 but actual -1");
    }

    @Test
    public void powShouldSucceed() {
        complexNumbers.forEach(it -> {
            assertThat(it.pow(3)).isExactlyInstanceOf(SimpleComplexNumber.class)
                    .isEqualTo(it.multiply(it).multiply(it));
            assertThat(it.pow(2)).isEqualTo(it.multiply(it));
        });
    }

    @Test
    public void powOneShouldBeTheSame() {
        complexNumbers.forEach(it -> {
            assertThat(it.pow(1)).isSameAs(it);
        });
    }

    @Test
    public void powZeroShouldBeSameAsOne() {
        complexNumbers.forEach(it -> {
            assertThat(it.pow(0)).isSameAs(ONE);
        });
    }

    @Test
    public void powOfOneShouldBeEqualToOne() {
        assertThat(ONE.pow(3)).isEqualTo(ONE);
    }

    @Test
    public void powOfZeroForExponentNotEqualToZeroShouldBeEqualToZero() {
        assertThat(ZERO.pow(3)).isEqualTo(ZERO);
    }

    @Test
    public void powOfIForExponentNotEqualToZeroShouldBeEqualToMinusOne() {
        assertThat(I.pow(2)).isEqualTo(ONE.negate());
    }

    @Test
    public void negateShouldSucceed() {
        complexNumbers.forEach(it -> {
            assertThat(it.negate()).isExactlyInstanceOf(SimpleComplexNumber.class)
                    .isEqualTo(new SimpleComplexNumber(it.getReal().negate(), it.getImaginary().negate()));
        });
    }

    @Test
    public void negateZeroShouldBeEqualToSelf() {
        assertThat(ZERO.negate()).isEqualTo(ZERO);
    }

    @Test
    public void multiplyMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(it -> {
            assertThat(it.multiply(ONE.negate())).isEqualTo(it.negate());
        });
    }

    @Test
    public void divideMinusOneShouldBeEqualToNegated() {
        complexNumbers.forEach(it -> {
            assertThat(it.divide(ONE.negate())).isEqualTo(new RealComplexNumber(it).negate());
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZero() {
        complexNumbers.forEach(it -> {
            assertThat(it.add(it.negate())).isEqualTo(ZERO);
        });
    }

    @Test
    public void invertZeroShouldReturnAnEmptyOptional() {
        assertThatThrownBy(() -> {
            ZERO.invert();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected to be invertible but actual %s", ZERO);
    }

    @Test
    public void invertShouldSucceed() {
        invertibles.forEach(it -> {
            assertThat(it.invert()).isExactlyInstanceOf(RealComplexNumber.class).isEqualTo(ONE.divide(it));
        });
    }

    @Test
    public void invertOneShouldBeEqualToOne() {
        assertThat(ONE.invert()).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void invertSelfShouldBeEqualToOneDividedBySelf() {
        invertibles.forEach(it -> {
            assertThat(it.invert()).isEqualTo(ONE.divide(it));
        });
    }

    @Test
    public void invertibleZeroShouldBeFalse() {
        assertThat(ZERO.invertible()).isFalse();
    }

    @Test
    public void invertibleShouldSucceed() {
        invertibles.forEach(it -> {
            assertThat(it.invertible()).isTrue();
        });
    }

    @Test
    public void invertibleShouldBePredictable() {
        complexNumbers.forEach(it -> {
            assertThat(it.invertible()).isEqualTo(!it.equals(ZERO));
        });
    }

    @Test
    public void absPow2ShouldSucceed() {
        complexNumbers.forEach(it -> {
            assertThat(it.absPow2()).isExactlyInstanceOf(BigInteger.class)
                    .isEqualTo(it.getReal().pow(2).add(it.getImaginary().pow(2)));
        });
    }

    @Test
    public void absPow2ZeroShouldBeEqualToZero() {
        assertThat(ZERO.absPow2()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void absPow2OneShouldBeEqualToOne() {
        assertThat(ONE.absPow2()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void absPow2MinusOneShouldBeSameAsOne() {
        assertThat(ONE.negate().absPow2()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void absShouldSucceed() {
        complexNumbers.forEach(it -> {
            assertThat(it.abs()).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(SquareRootCalculator.sqrt(it.absPow2()));
        });
    }

    @Test
    public void conjugateShouldSucceed() {
        complexNumbers.forEach(it -> {
            assertThat(it.conjugate()).isExactlyInstanceOf(SimpleComplexNumber.class)
                    .isEqualTo(new SimpleComplexNumber(it.getReal(), it.getImaginary().negate()));
        });
    }

    @Test
    public void conjugateRealNumberShouldBeEqualToSelf() {
        complexNumbers.forEach(it -> {
            final SimpleComplexNumber realNumber = new SimpleComplexNumber(it.getReal(), BigInteger.ZERO);
            assertThat(realNumber.conjugate()).isEqualTo(realNumber);
        });
    }

    @Test
    public void matrixShouldSucceed() {
        complexNumbers.forEach(it -> {
            final BigInteger real = it.getReal();
            final BigInteger imaginary = it.getImaginary();
            final BigIntMatrix expected = BigIntMatrix.builder(2, 2).put(1, 1, real).put(1, 2, imaginary.negate())
                    .put(2, 1, imaginary).put(2, 2, real).build();
            assertThat(it.matrix()).isExactlyInstanceOf(BigIntMatrix.class).isEqualTo(expected);
        });
    }
}