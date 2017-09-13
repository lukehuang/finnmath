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

import finnmath.util.MathRandom;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public final class FractionTest {
    private static final int howMany = 10;
    private static final Fraction ZERO = Fraction.ZERO;
    private static final Fraction ONE = Fraction.ONE;
    private static final List<Fraction> fractions = new ArrayList<>(howMany);
    private static final List<Fraction> others = new ArrayList<>(howMany);
    private static final List<Fraction> invertibles = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom();
        final long bound = 10;
        for (int i = 0; i < howMany; i++) {
            fractions.add(mathRandom.nextFraction(bound));
            others.add(mathRandom.nextFraction(bound));
            invertibles.add(mathRandom.nextInvertibleFraction(bound));
        }
    }

    @Test
    public void newNumeratorNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new Fraction(null, BigInteger.ONE);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("numerator");
    }

    @Test
    public void newDenominatorNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new Fraction(BigInteger.ZERO, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("denominator");
    }

    @Test
    public void newDenominatorZeroShouldThrowException() {
        assertThatThrownBy(() -> {
            new Fraction(BigInteger.ZERO, BigInteger.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected denominator != 0 but actual 0");
    }

    @Test
    public void newShouldSucceed() {
        final Fraction fraction = new Fraction(BigInteger.ZERO, BigInteger.ONE);
        assertThat(fraction).isExactlyInstanceOf(Fraction.class);
        assertThat(fraction.getNumerator()).isExactlyInstanceOf(BigInteger.class);
        assertThat(fraction.getDenominator()).isExactlyInstanceOf(BigInteger.class);
    }

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.add(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("summand");
    }

    @Test
    public void addShouldSucceed() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                final BigInteger expectedNumerator = other.getDenominator().multiply(fraction.getNumerator())
                        .add(fraction.getDenominator().multiply(other.getNumerator()));
                final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
                final Fraction actual = fraction.add(other);
                final Fraction expected = new Fraction(expectedNumerator, expectedDenominator);
                assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo((expected));
            });
        });
    }

    @Test
    public void addZeroShouldBeEqualToSelf() {
        fractions.forEach(it -> {
            assertThat(it.add(ZERO)).isEqualTo(it);
        });
    }

    @Test
    public void addShouldBeCommutative() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                assertThat(fraction.add(other)).isEqualTo(other.add(fraction));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(fraction.add(other).add(invertible)).isEqualTo(fraction.add(other.add(invertible)));
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
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                final BigInteger expectedNumerator = other.getDenominator().multiply(fraction.getNumerator())
                        .subtract(fraction.getDenominator().multiply(other.getNumerator()));
                final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
                final Fraction actual = fraction.subtract(other);
                final Fraction expected = new Fraction(expectedNumerator, expectedDenominator);
                assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
            });
        });
    }

    @Test
    public void subtractZeroShouldBeEqualToSelf() {
        fractions.forEach(it -> {
            assertThat(it.subtract(ZERO)).isEqualTo(it);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZero() {
        fractions.forEach(it -> {
            assertThat(it.subtract(it).normalize().reduce()).isEqualTo(ZERO);
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
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                final BigInteger expectedNumerator = fraction.getNumerator().multiply(other.getNumerator());
                final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
                final Fraction actual = fraction.multiply(other);
                final Fraction expected = new Fraction(expectedNumerator, expectedDenominator);
                assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
            });
        });
    }

    @Test
    public void multiplyOneShouldBeEqualToSelf() {
        fractions.forEach(it -> {
            assertThat(it.multiply(ONE)).isEqualTo(it);
        });
    }

    @Test
    public void multiplyZeroShouldBeEqualToZero() {
        fractions.forEach(it -> {
            assertThat(it.multiply(ZERO).reduce()).isEqualTo(ZERO);
        });
    }

    @Test
    public void multiplyShouldBeCommutative() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                assertThat(fraction.multiply(other)).isEqualTo(other.multiply(fraction));
            });
        });
    }

    @Test
    public void multiplyShouldBeAssociative() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(fraction.multiply(other).multiply(invertible))
                            .isEqualTo(fraction.multiply(other.multiply(invertible)));
                });
            });
        });
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        fractions.forEach(fraction -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    assertThat(fraction.multiply(other.add(invertible)).reduce())
                            .isEqualTo(fraction.multiply(other).add(fraction.multiply(invertible)).reduce());
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
            ZERO.divide(ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected divisor to be invertible but actual %s", ZERO);
    }

    @Test
    public void divideShouldSucceed() {
        fractions.forEach(fraction -> {
            invertibles.forEach(invertible -> {
                assertThat(fraction.divide(invertible)).isExactlyInstanceOf(Fraction.class)
                        .isEqualTo(fraction.multiply(invertible.invert()));
            });
        });
    }

    @Test
    public void divideOneShouldBeEqualToSelf() {
        fractions.forEach(it -> {
            assertThat(it.divide(ONE)).isEqualTo(it);
        });
    }

    @Test
    public void negateShouldSucceed() {
        fractions.forEach(it -> {
            assertThat(it.negate()).isExactlyInstanceOf(Fraction.class)
                    .isEqualTo(new Fraction(it.getNumerator().negate(), it.getDenominator()));
        });
    }

    @Test
    public void negateZeroShouldBeEqualToSelf() {
        assertThat(ZERO.negate()).isEqualTo(ZERO);
    }

    @Test
    public void addNegatedShouldBeEqualToZero() {
        fractions.forEach(it -> {
            assertThat(it.add(it.negate()).reduce()).isEqualTo(ZERO);
        });
    }

    @Test
    public void multiplyMinusOneShouldBeEqualToNegated() {
        fractions.forEach(it -> {
            assertThat(it.multiply(ONE.negate())).isEqualTo(it.negate());
        });
    }

    @Test
    public void divideMinusOneShouldBeEqualToNegated() {
        fractions.forEach(it -> {
            assertThat(it.divide(ONE.negate()).normalize().reduce()).isEqualTo(it.negate().reduce());
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
        fractions.forEach(it -> {
            assertThat(it.pow(3)).isExactlyInstanceOf(Fraction.class).isEqualTo(it.multiply(it).multiply(it));
            assertThat(it.pow(2)).isEqualTo(it.multiply(it));
        });
    }

    @Test
    public void powOneShouldBeTheSame() {
        fractions.forEach(it -> {
            assertThat(it.pow(1)).isSameAs(it);
        });
    }

    @Test
    public void powZeroShouldBeSameAsOne() {
        fractions.forEach(it -> {
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
    public void invertZeroShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.invert();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected to be invertible but actual %s", ZERO);
    }

    @Test
    public void invertShouldSucceed() {
        invertibles.forEach(it -> {
            assertThat(it.invert()).isExactlyInstanceOf(Fraction.class)
                    .isEqualTo(new Fraction(it.getDenominator(), it.getNumerator()));
        });
    }

    @Test
    public void invertOneShouldBeEqualToOne() {
        assertThat(ONE.invert()).isEqualTo(ONE);
    }

    @Test
    public void invertSelfShouldBeEqualToOneDividedBySelf() {
        invertibles.forEach(it -> {
            assertThat(it.invert().reduce().normalize()).isEqualTo(ONE.divide(it).reduce().normalize());
        });
    }

    @Test
    public void multiplyInvertedShouldBeEqualToOne() {
        invertibles.forEach(it -> {
            assertThat(it.multiply(it.invert()).reduce().normalize()).isEqualTo(ONE);
        });
    }

    @Test
    public void invertibleZeroShouldBeFalse() {
        assertThat(ZERO.invertible()).isFalse();
    }

    @Test
    public void invertibleShouldBePredictable() {
        fractions.forEach(it -> {
            assertThat(it.invertible()).isEqualTo(!it.getNumerator().equals(BigInteger.ZERO));
        });
    }

    @Test
    public void invertibleShouldSucceed() {
        invertibles.forEach(it -> {
            assertThat(it.invertible()).isTrue();
        });
    }

    @Test
    public void lessThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.lessThanOrEqualTo(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void lessThanOrEqualToShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction greater = it.add(ONE);
            final Fraction lower = it.subtract(ONE);
            assertThat(it.lessThanOrEqualTo(greater)).isTrue();
            assertThat(it.lessThanOrEqualTo(lower)).isFalse();
            assertThat(it.lessThanOrEqualTo(it)).isTrue();
        });
    }

    @Test
    public void greaterThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.greaterThanOrEqualTo(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void greaterThanOrEqualToShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction lower = it.subtract(ONE);
            final Fraction greater = it.add(ONE);
            assertThat(it.greaterThanOrEqualTo(lower)).isTrue();
            assertThat(it.greaterThanOrEqualTo(greater)).isFalse();
            assertThat(it.greaterThanOrEqualTo(it)).isTrue();
        });
    }

    @Test
    public void lessThanNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.lessThan(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void lessThanShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction greater = it.add(ONE);
            final Fraction lower = it.subtract(ONE);
            assertThat(it.lessThan(greater)).isTrue();
            assertThat(it.lessThan(lower)).isFalse();
            assertThat(it.lessThan(it)).isFalse();
        });
    }

    @Test
    public void greaterThanNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.greaterThan(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void greaterThanShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction lower = it.subtract(ONE);
            final Fraction greater = it.add(ONE);
            assertThat(it.greaterThan(lower)).isTrue();
            assertThat(it.greaterThan(greater)).isFalse();
            assertThat(it.greaterThan(it)).isFalse();
        });
    }

    @Test
    public void minNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.min(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void minShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction maximum = it.add(ONE);
            final Fraction minimum = it.subtract(ONE);
            assertThat(it.min(it)).isExactlyInstanceOf(Fraction.class).isSameAs(it);
            assertThat(it.min(maximum)).isSameAs(it);
            assertThat(it.min(minimum)).isSameAs(minimum);
        });
    }

    @Test
    public void maxNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.max(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void maxShouldSucceed() {
        fractions.forEach(it -> {
            final Fraction minimum = it.subtract(ONE);
            final Fraction maximum = it.add(ONE);
            assertThat(it.max(it)).isExactlyInstanceOf(Fraction.class).isSameAs(it);
            assertThat(it.max(minimum)).isSameAs(it);
            assertThat(it.max(maximum)).isSameAs(maximum);
        });
    }

    @Test
    public void normalizeShouldSucceed() {
        final MathRandom mathRandom = new MathRandom();
        final int bound = 10;
        mathRandom.nextInvertiblePositiveFractions(bound, howMany).forEach(it -> {
            assertThat(it.normalize()).isExactlyInstanceOf(Fraction.class);
            final BigInteger numerator = it.getNumerator();
            final BigInteger denominator = it.getDenominator();
            final BigInteger negatedNumerator = numerator.negate();
            final Fraction expectedForNegativeSignum = new Fraction(negatedNumerator, denominator);
            assertThat(new Fraction(negatedNumerator, denominator).normalize()).isEqualTo(expectedForNegativeSignum);
            final BigInteger negatedDenominator = denominator.negate();
            assertThat(new Fraction(numerator, negatedDenominator).normalize()).isEqualTo(expectedForNegativeSignum);
            assertThat(new Fraction(BigInteger.ZERO, denominator).normalize()).isEqualTo(ZERO);
            assertThat(new Fraction(negatedNumerator, negatedDenominator).normalize()).isEqualTo(it);
            final Fraction normalized = it.normalize();
            assertThat(normalized.normalize()).isSameAs(normalized);
        });
    }

    @Test
    public void normalizeZeroShouldBeTheSame() {
        assertThat(ZERO.normalize()).isSameAs(ZERO);
    }

    @Test
    public void normalizeOneShouldBeTheSame() {
        assertThat(ONE.normalize()).isSameAs(ONE);
    }

    @Test
    public void absShouldSucceed() {
        fractions.forEach(it -> {
            assertThat(it.abs()).isExactlyInstanceOf(Fraction.class)
                    .isEqualTo(new Fraction(it.getNumerator().abs(), it.getDenominator().abs()));
        });
    }

    @Test
    public void absZeroShouldBeEqualToZero() {
        assertThat(ZERO.abs()).isEqualTo(ZERO);
    }

    @Test
    public void absOneShouldBeEqualToOne() {
        assertThat(ONE.abs()).isEqualTo(ONE);
    }

    @Test
    public void absMinusOneShouldBeEqualToOne() {
        assertThat(ONE.negate().abs()).isEqualTo(ONE);
    }

    @Test
    public void signumShouldSucceed() {
        fractions.forEach(it -> {
            assertThat(it.signum()).isEqualTo(it.getNumerator().signum() * it.getDenominator().signum());
        });
    }

    @Test
    public void signumMinusOneShouldBeEqualToMinusOne() {
        assertThat(ONE.negate().signum()).isEqualTo(-1);
    }

    @Test
    public void signumZeroShouldBeEqualToZero() {
        assertThat(ZERO.signum()).isEqualTo(0);
    }

    @Test
    public void signumOneShouldBeEqualToOne() {
        assertThat(ONE.signum()).isEqualTo(1);
    }

    @Test
    public void reduceShouldSucceed() {
        fractions.forEach(it -> {
            final BigInteger gcd = it.getNumerator().gcd(it.getDenominator());
            final Fraction expected = new Fraction(it.getNumerator().divide(gcd), it.getDenominator().divide(gcd));
            assertThat(it.reduce()).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
        });
    }

    @Test
    public void reduceZeroShouldBeEqualToZero() {
        assertThat(ZERO.reduce()).isEqualTo(ZERO);
    }

    @Test
    public void reduceOneShouldBeEqualToOne() {
        assertThat(ONE.reduce()).isEqualTo(ONE);
    }

    @Test
    public void equivalentNullShouldThrowException() {
        assertThatThrownBy(() -> {
            ZERO.equivalent(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void equivalentShouldSucceed() {
        fractions.forEach(it -> {
            others.forEach(other -> {
                assertThat(it.equivalent(other)).isEqualTo(it.reduce().equals(other.reduce()));
            });
        });
    }

    @Test
    public void equivalentShouldBeReflexive() {
        fractions.forEach(it -> {
            assertThat(it.equivalent(it)).isTrue();
        });
    }

    @Test
    public void equivalentShouldBeSymmetric() {
        fractions.forEach(it -> {
            others.forEach(other -> {
                assertThat(it.equivalent(other)).isEqualTo(other.equivalent(it));
            });
        });
    }

    @Test
    public void equivalentShouldBeTransitive() {
        fractions.forEach(it -> {
            others.forEach(other -> {
                invertibles.forEach(invertible -> {
                    final boolean a = it.equivalent(other);
                    final boolean b = other.equivalent(invertible);
                    final boolean c = it.equivalent(invertible);
                    if (a && b) {
                        assertThat(c).isTrue();
                    } else {
                        assertThat(a && b && c).isFalse();
                    }
                    if (!c) {
                        assertThat(a && b).isFalse();
                    } else if (!a) {
                        assertThat(b).isFalse();
                    } else if (!b) {
                        assertThat(a).isFalse();
                    }
                });
            });
        });
    }
}
