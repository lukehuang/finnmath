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

import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.base.MoreObjects;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.junit.Test;

public final class FractionTest {
    private final long bound = 10;
    private final int howMany = 10;
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<Fraction> fractions = mathRandom.nextFractions(bound, howMany);
    private final List<Fraction> others = mathRandom.nextFractions(bound, howMany);
    private final List<Fraction> invertibles = mathRandom.nextInvertibleFractions(bound, howMany);

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");
    }

    @Test
    public void addShouldSucceed() {
        fractions.forEach(fraction -> others.forEach(other -> {
            final BigInteger expectedNumerator = other.getDenominator().multiply(fraction.getNumerator())
                .add(fraction.getDenominator().multiply(other.getNumerator()));
            final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
            final Fraction actual = fraction.add(other);
            final Fraction expected = Fraction.of(expectedNumerator, expectedDenominator);
            assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
        }));
    }

    @Test
    public void addZeroShouldBeEqualToSelf() {
        fractions.forEach(fraction -> assertThat(fraction.add(Fraction.ZERO)).isEqualTo(fraction));
    }

    @Test
    public void addShouldBeCommutative() {
        fractions.forEach(
            fraction -> others.forEach(other -> assertThat(fraction.add(other)).isEqualTo(other.add(fraction))));
    }

    @Test
    public void addShouldBeAssociative() {
        fractions.forEach(fraction -> others
            .forEach(other -> invertibles.forEach(invertible -> assertThat(fraction.add(other).add(invertible))
                .isEqualTo(fraction.add(other.add(invertible))))));
    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.subtract(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("subtrahend");
    }

    @Test
    public void subtractShouldSucceed() {
        fractions.forEach(fraction -> others.forEach(other -> {
            final BigInteger expectedNumerator = other.getDenominator().multiply(fraction.getNumerator())
                .subtract(fraction.getDenominator().multiply(other.getNumerator()));
            final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
            final Fraction actual = fraction.subtract(other);
            final Fraction expected = Fraction.of(expectedNumerator, expectedDenominator);
            assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
        }));
    }

    @Test
    public void subtractZeroShouldBeEqualToSelf() {
        fractions.forEach(fraction -> assertThat(fraction.subtract(Fraction.ZERO)).isEqualTo(fraction));
    }

    @Test
    public void subtractSelfShouldBeEqualToZero() {
        fractions
            .forEach(fraction -> assertThat(fraction.subtract(fraction).normalize().reduce()).isEqualTo(Fraction.ZERO));
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.multiply(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("factor");
    }

    @Test
    public void multiplyShouldSucceed() {
        fractions.forEach(fraction -> others.forEach(other -> {
            final BigInteger expectedNumerator = fraction.getNumerator().multiply(other.getNumerator());
            final BigInteger expectedDenominator = fraction.getDenominator().multiply(other.getDenominator());
            final Fraction actual = fraction.multiply(other);
            final Fraction expected = Fraction.of(expectedNumerator, expectedDenominator);
            assertThat(actual).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
        }));
    }

    @Test
    public void multiplyOneShouldBeEqualToSelf() {
        fractions.forEach(fraction -> assertThat(fraction.multiply(Fraction.ONE)).isEqualTo(fraction));
    }

    @Test
    public void multiplyZeroShouldBeEqualToZero() {
        fractions.forEach(fraction -> assertThat(fraction.multiply(Fraction.ZERO).reduce()).isEqualTo(Fraction.ZERO));
    }

    @Test
    public void multiplyShouldBeCommutative() {
        fractions.forEach(fraction -> others
            .forEach(other -> assertThat(fraction.multiply(other)).isEqualTo(other.multiply(fraction))));
    }

    @Test
    public void multiplyShouldBeAssociative() {
        fractions.forEach(fraction -> others.forEach(
            other -> invertibles.forEach(invertible -> assertThat(fraction.multiply(other).multiply(invertible))
                .isEqualTo(fraction.multiply(other.multiply(invertible))))));
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        fractions.forEach(fraction -> others.forEach(
            other -> invertibles.forEach(invertible -> assertThat(fraction.multiply(other.add(invertible)).reduce())
                .isEqualTo(fraction.multiply(other).add(fraction.multiply(invertible)).reduce()))));
    }

    @Test
    public void divideNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.divide(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("divisor");
    }

    @Test
    public void divideZeroShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.divide(Fraction.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected divisor to be invertible but actual %s", Fraction.ZERO);
    }

    @Test
    public void divideShouldSucceed() {
        fractions.forEach(fraction -> invertibles.forEach(invertible -> assertThat(fraction.divide(invertible))
            .isExactlyInstanceOf(Fraction.class).isEqualTo(fraction.multiply(invertible.invert()))));
    }

    @Test
    public void divideOneShouldBeEqualToSelf() {
        fractions.forEach(fraction -> assertThat(fraction.divide(Fraction.ONE)).isEqualTo(fraction));
    }

    @Test
    public void negateShouldSucceed() {
        fractions.forEach(fraction -> assertThat(fraction.negate()).isExactlyInstanceOf(Fraction.class)
            .isEqualTo(Fraction.of(fraction.getNumerator().negate(), fraction.getDenominator())));
    }

    @Test
    public void negateZeroShouldBeEqualToSelf() {
        assertThat(Fraction.ZERO.negate()).isEqualTo(Fraction.ZERO);
    }

    @Test
    public void addNegatedShouldBeEqualToZero() {
        fractions.forEach(fraction -> assertThat(fraction.add(fraction.negate()).reduce()).isEqualTo(Fraction.ZERO));
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        fractions.forEach(fraction -> assertThat(fraction.negate().negate()).isEqualTo(fraction));
    }

    @Test
    public void multiplyMinusOneShouldBeEqualToNegated() {
        fractions
            .forEach(fraction -> assertThat(fraction.multiply(Fraction.ONE.negate())).isEqualTo(fraction.negate()));
    }

    @Test
    public void divideMinusOneShouldBeEqualToNegated() {
        fractions.forEach(fraction -> assertThat(fraction.divide(Fraction.ONE.negate()).normalize().reduce())
            .isEqualTo(fraction.negate().reduce()));
    }

    @Test
    public void powNegativeExponentShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.pow(-1)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected exponent > -1 but actual -1");
    }

    @Test
    public void powShouldSucceed() {
        fractions.forEach(fraction -> {
            assertThat(fraction.pow(3)).isExactlyInstanceOf(Fraction.class)
                .isEqualTo(fraction.multiply(fraction).multiply(fraction));
            assertThat(fraction.pow(2)).isEqualTo(fraction.multiply(fraction));
        });
    }

    @Test
    public void powOneShouldBeTheSame() {
        fractions.forEach(fraction -> assertThat(fraction.pow(1)).isSameAs(fraction));
    }

    @Test
    public void powZeroShouldBeSameAsOne() {
        fractions.forEach(fraction -> assertThat(fraction.pow(0)).isSameAs(Fraction.ONE));
    }

    @Test
    public void powOfOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.pow(3)).isEqualTo(Fraction.ONE);
    }

    @Test
    public void powOfZeroForExponentNotEqualToZeroShouldBeEqualToZero() {
        assertThat(Fraction.ZERO.pow(3)).isEqualTo(Fraction.ZERO);
    }

    @Test
    public void invertZeroShouldThrowException() {
        assertThatThrownBy(Fraction.ZERO::invert).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("expected to be invertible but actual %s", Fraction.ZERO);
    }

    @Test
    public void invertShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.invert()).isExactlyInstanceOf(Fraction.class)
            .isEqualTo(Fraction.of(invertible.getDenominator(), invertible.getNumerator())));
    }

    @Test
    public void invertOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.invert()).isEqualTo(Fraction.ONE);
    }

    @Test
    public void invertSelfShouldBeEqualToOneDividedBySelf() {
        invertibles.forEach(invertible -> assertThat(invertible.invert().reduce().normalize())
            .isEqualTo(Fraction.ONE.divide(invertible).reduce().normalize()));
    }

    @Test
    public void invertTwiceShouldBeEqualToSelf() {
        invertibles.forEach(invertible -> assertThat(invertible.invert().invert()).isEqualTo(invertible));
    }

    @Test
    public void multiplyInvertedShouldBeEqualToOne() {
        invertibles.forEach(invertible -> assertThat(invertible.multiply(invertible.invert()).reduce().normalize())
            .isEqualTo(Fraction.ONE));
    }

    @Test
    public void invertibleZeroShouldBeFalse() {
        assertThat(Fraction.ZERO.invertible()).isFalse();
    }

    @Test
    public void invertibleShouldBePredictable() {
        fractions.forEach(
            fraction -> assertThat(fraction.invertible()).isEqualTo(!fraction.getNumerator().equals(BigInteger.ZERO)));
    }

    @Test
    public void invertibleShouldSucceed() {
        invertibles.forEach(invertible -> assertThat(invertible.invertible()).isTrue());
    }

    @Test
    public void lessThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.lessThanOrEqualTo(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void lessThanOrEqualToShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction greater = fraction.add(Fraction.ONE);
            final Fraction lower = fraction.subtract(Fraction.ONE);
            assertThat(fraction.lessThanOrEqualTo(greater)).isTrue();
            assertThat(fraction.lessThanOrEqualTo(lower)).isFalse();
            assertThat(fraction.lessThanOrEqualTo(fraction)).isTrue();
        });
    }

    @Test
    public void greaterThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.greaterThanOrEqualTo(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void greaterThanOrEqualToShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction lower = fraction.subtract(Fraction.ONE);
            final Fraction greater = fraction.add(Fraction.ONE);
            assertThat(fraction.greaterThanOrEqualTo(lower)).isTrue();
            assertThat(fraction.greaterThanOrEqualTo(greater)).isFalse();
            assertThat(fraction.greaterThanOrEqualTo(fraction)).isTrue();
        });
    }

    @Test
    public void lessThanNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.lessThan(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void lessThanShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction greater = fraction.add(Fraction.ONE);
            final Fraction lower = fraction.subtract(Fraction.ONE);
            assertThat(fraction.lessThan(greater)).isTrue();
            assertThat(fraction.lessThan(lower)).isFalse();
            assertThat(fraction.lessThan(fraction)).isFalse();
        });
    }

    @Test
    public void greaterThanNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.greaterThan(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void greaterThanShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction lower = fraction.subtract(Fraction.ONE);
            final Fraction greater = fraction.add(Fraction.ONE);
            assertThat(fraction.greaterThan(lower)).isTrue();
            assertThat(fraction.greaterThan(greater)).isFalse();
            assertThat(fraction.greaterThan(fraction)).isFalse();
        });
    }

    @Test
    public void minNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.min(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void minShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction maximum = fraction.add(Fraction.ONE);
            final Fraction minimum = fraction.subtract(Fraction.ONE);
            assertThat(fraction.min(fraction)).isExactlyInstanceOf(Fraction.class).isSameAs(fraction);
            assertThat(fraction.min(maximum)).isSameAs(fraction);
            assertThat(fraction.min(minimum)).isSameAs(minimum);
        });
    }

    @Test
    public void maxNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.max(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void maxShouldSucceed() {
        fractions.forEach(fraction -> {
            final Fraction minimum = fraction.subtract(Fraction.ONE);
            final Fraction maximum = fraction.add(Fraction.ONE);
            assertThat(fraction.max(fraction)).isExactlyInstanceOf(Fraction.class).isSameAs(fraction);
            assertThat(fraction.max(minimum)).isSameAs(fraction);
            assertThat(fraction.max(maximum)).isSameAs(maximum);
        });
    }

    @Test
    public void normalizeShouldSucceed() {
        mathRandom.nextInvertiblePositiveFractions(bound, howMany).forEach(invertible -> {
            assertThat(invertible.normalize()).isExactlyInstanceOf(Fraction.class);
            final BigInteger numerator = invertible.getNumerator();
            final BigInteger denominator = invertible.getDenominator();
            final BigInteger negatedNumerator = numerator.negate();
            final Fraction expectedForNegativeSignum = Fraction.of(negatedNumerator, denominator);
            assertThat(Fraction.of(negatedNumerator, denominator).normalize()).isEqualTo(expectedForNegativeSignum);
            final BigInteger negatedDenominator = denominator.negate();
            assertThat(Fraction.of(numerator, negatedDenominator).normalize()).isEqualTo(expectedForNegativeSignum);
            assertThat(Fraction.of(BigInteger.ZERO, denominator).normalize()).isEqualTo(Fraction.ZERO);
            assertThat(Fraction.of(negatedNumerator, negatedDenominator).normalize()).isEqualTo(invertible);
            final Fraction normalized = invertible.normalize();
            assertThat(normalized.normalize()).isSameAs(normalized);
        });
    }

    @Test
    public void normalizeZeroShouldBeTheSame() {
        assertThat(Fraction.ZERO.normalize()).isSameAs(Fraction.ZERO);
    }

    @Test
    public void normalizeOneShouldBeTheSame() {
        assertThat(Fraction.ONE.normalize()).isSameAs(Fraction.ONE);
    }

    @Test
    public void absShouldSucceed() {
        fractions.forEach(fraction -> assertThat(fraction.abs()).isExactlyInstanceOf(Fraction.class)
            .isEqualTo(Fraction.of(fraction.getNumerator().abs(), fraction.getDenominator().abs())));
    }

    @Test
    public void absZeroShouldBeEqualToZero() {
        assertThat(Fraction.ZERO.abs()).isEqualTo(Fraction.ZERO);
    }

    @Test
    public void absOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.abs()).isEqualTo(Fraction.ONE);
    }

    @Test
    public void absMinusOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.negate().abs()).isEqualTo(Fraction.ONE);
    }

    @Test
    public void compareToNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.compareTo(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void compareToGreaterShouldReturnMinusOne() {
        fractions.forEach(fraction -> assertThat(fraction.compareTo(fraction.add(Fraction.ONE))).isEqualTo(-1));
    }

    @Test
    public void compareToEqualShouldReturnZero() {
        fractions.forEach(
            fraction -> assertThat(fraction.compareTo(Fraction.of(fraction.getNumerator(), fraction.getDenominator())))
                .isEqualTo(0));
    }

    @Test
    public void compareToLowerShouldReturnMinusOne() {
        fractions.forEach(fraction -> assertThat(fraction.compareTo(fraction.subtract(Fraction.ONE))).isEqualTo(1));
    }

    @Test
    public void signumShouldSucceed() {
        fractions.forEach(fraction -> assertThat(fraction.signum())
            .isEqualTo(fraction.getNumerator().signum() * fraction.getDenominator().signum()));
    }

    @Test
    public void signumMinusOneShouldBeEqualToMinusOne() {
        assertThat(Fraction.ONE.negate().signum()).isEqualTo(-1);
    }

    @Test
    public void signumZeroShouldBeEqualToZero() {
        assertThat(Fraction.ZERO.signum()).isEqualTo(0);
    }

    @Test
    public void signumOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.signum()).isEqualTo(1);
    }

    @Test
    public void reduceShouldSucceed() {
        fractions.forEach(fraction -> {
            final BigInteger gcd = fraction.getNumerator().gcd(fraction.getDenominator());
            final Fraction expected =
                Fraction.of(fraction.getNumerator().divide(gcd), fraction.getDenominator().divide(gcd));
            assertThat(fraction.reduce()).isExactlyInstanceOf(Fraction.class).isEqualTo(expected);
        });
    }

    @Test
    public void reduceZeroShouldBeEqualToZero() {
        assertThat(Fraction.ZERO.reduce()).isEqualTo(Fraction.ZERO);
    }

    @Test
    public void reduceOneShouldBeEqualToOne() {
        assertThat(Fraction.ONE.reduce()).isEqualTo(Fraction.ONE);
    }

    @Test
    public void equivalentNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.ZERO.equivalent(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void equivalentShouldSucceed() {
        fractions.forEach(fraction -> others.forEach(
            other -> assertThat(fraction.equivalent(other)).isEqualTo(fraction.reduce().equals(other.reduce()))));
    }

    @Test
    public void equivalentShouldBeReflexive() {
        fractions.forEach(fraction -> assertThat(fraction.equivalent(fraction)).isTrue());
    }

    @Test
    public void equivalentShouldBeSymmetric() {
        fractions.forEach(fraction -> others
            .forEach(other -> assertThat(fraction.equivalent(other)).isEqualTo(other.equivalent(fraction))));
    }

    @Test
    public void equivalentShouldBeTransitive() {
        fractions.forEach(fraction -> others.forEach(other -> invertibles.forEach(invertible -> {
            final boolean conditionA = fraction.equivalent(other);
            final boolean conditionB = other.equivalent(invertible);
            final boolean conditionC = fraction.equivalent(invertible);
            if (conditionA && conditionB) {
                assertThat(conditionC).isTrue();
            } else {
                assertThat(conditionA && conditionB && conditionC).isFalse();
            }
            if (!conditionC) {
                assertThat(conditionA && conditionB).isFalse();
            } else if (!conditionA) {
                assertThat(conditionB).isFalse();
            } else if (!conditionB) {
                assertThat(conditionA).isFalse();
            }
        })));
    }

    @Test
    public void ofLongDenominatorZeroShouldThrowException() {
        assertThatThrownBy(() -> Fraction.of(0L, 0L)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected denominator != 0 but actual 0");
    }

    @Test
    public void ofLongsShouldSucceed() {
        final Random random = new Random(7);
        random.longs(howMany, 0, bound).forEach(numerator -> random.longs(howMany, 1, bound).forEach(denominator -> {
            final Fraction actual = Fraction.of(numerator, denominator);
            assertThat(actual.getNumerator()).isEqualByComparingTo(BigInteger.valueOf(numerator));
            assertThat(actual.getDenominator()).isEqualByComparingTo(BigInteger.valueOf(denominator));
        }));
    }

    @Test
    public void ofBigIntegerNumeratorNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.of(null, BigInteger.ONE)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("numerator");
    }

    @Test
    public void ofBigIntegerDenominatorNullShouldThrowException() {
        assertThatThrownBy(() -> Fraction.of(BigInteger.ZERO, null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("denominator");
    }

    @Test
    public void ofBigIntegerDenominatorZeroShouldThrowException() {
        assertThatThrownBy(() -> Fraction.of(BigInteger.ZERO, BigInteger.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected denominator != 0 but actual 0");
    }

    @Test
    public void ofBigIntegersShouldSucceed() {
        final List<BigInteger> integers = mathRandom.nextBigIntegers(bound, howMany);
        integers.forEach(numerator -> integers.forEach(denominator -> {
            final BigInteger nonZeroDenominator =
                denominator.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ONE : denominator;
            final Fraction actual = Fraction.of(numerator, nonZeroDenominator);
            assertThat(actual.getNumerator()).isEqualByComparingTo(numerator);
            assertThat(actual.getDenominator()).isEqualByComparingTo(nonZeroDenominator);
        }));
    }

    @Test
    public void hashCodeShouldSucceed() {
        fractions.forEach(fraction -> assertThat(fraction.hashCode())
            .isEqualTo(Objects.hash(fraction.getNumerator(), fraction.getDenominator())));
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        fractions.forEach(fraction -> assertThat(fraction.equals(fraction)).isTrue());
    }

    @Test
    public void equalsNotFractionShouldReturnFalse() {
        assertThat(Fraction.ZERO.equals(new Object())).isFalse();
    }

    @Test
    public void equalsNumeratorNotEqualShouldReturnFalse() {
        fractions.forEach(fraction -> {
            final Fraction other = Fraction.of(fraction.getNumerator().add(BigInteger.ONE), fraction.getDenominator());
            assertThat(fraction.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsDenominatorNotEqualShouldReturnFalse() {
        fractions.forEach(fraction -> {
            final Fraction other = Fraction.of(fraction.getNumerator(), fraction.getDenominator().add(BigInteger.ONE));
            assertThat(fraction.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        fractions.forEach(
            fraction -> assertThat(fraction.equals(Fraction.of(fraction.getNumerator(), fraction.getDenominator())))
                .isTrue());
    }

    @Test
    public void hashCodeEqualFractionsShouldHaveEqualHashCodes() {
        fractions.forEach(fraction -> {
            final Fraction other = Fraction.of(fraction.getNumerator(), fraction.getDenominator());
            assertThat(fraction.hashCode()).isEqualTo(other.hashCode());
        });
    }

    @Test
    public void toStringShouldSucceed() {
        fractions.forEach(fraction -> assertThat(fraction.toString()).isEqualTo(MoreObjects.toStringHelper(fraction)
            .add("numerator", fraction.getNumerator()).add("denominator", fraction.getDenominator()).toString()));
    }
}
