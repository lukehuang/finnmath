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

import com.github.ltennstedt.finnmath.util.MathRandom;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;

public final class ScientificNotationTest {
    private static final int howMany = 10;
    private static final List<ScientificNotation> scientificNotations = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom(7);
        final int bound = 10;
        for (int i = 0; i < howMany; i++) {
            scientificNotations.add(new ScientificNotation(mathRandom.nextDecimal(10, 2), new Random().nextInt(bound)));
        }
    }

    @Test
    public void newCoefficientNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new ScientificNotation(null, 0);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("coefficient");
    }

    @Test
    public void newShouldSucceed() {
        final ScientificNotation actual = new ScientificNotation(BigDecimal.ZERO, 0);
        assertThat(actual.getCoefficient()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(actual.getExponent()).isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void asStringCoefficientIsZero() {
        assertThat(new ScientificNotation(BigDecimal.ZERO, 1).asString()).isEqualTo("0");
    }

    @Test
    public void asStringExponentIsNegative() {
        final String expected =
                new StringBuilder(BigDecimal.ONE.toPlainString()).append(" * 10**(").append(-1).append(")").toString();
        assertThat(new ScientificNotation(BigDecimal.ONE, -1).asString()).isEqualTo(expected);
    }

    @Test
    public void asStringExponentIsZero() {
        assertThat(new ScientificNotation(BigDecimal.ONE, 0).asString()).isEqualTo(BigDecimal.ONE.toPlainString());
    }

    @Test
    public void asStringExponentIsOne() {
        final String expected = new StringBuilder(BigDecimal.ONE.toPlainString()).append(" * 10").toString();
        assertThat(new ScientificNotation(BigDecimal.ONE, 1).asString()).isEqualTo(expected);
    }

    @Test
    public void asStringExponentGreaterThanOne() {
        final String expected =
                new StringBuilder(BigDecimal.ONE.toPlainString()).append(" * 10**").append(2).toString();
        assertThat(new ScientificNotation(BigDecimal.ONE, 2).asString()).isEqualTo(expected);
    }

    @Test
    public void hashCodeShouldSucceed() {
        scientificNotations.forEach(scientificNotation -> {
            assertThat(scientificNotation.hashCode())
                    .isEqualTo(Objects.hash(scientificNotation.getCoefficient(), scientificNotation.getExponent()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        scientificNotations.forEach(scientificNotation -> {
            assertThat(scientificNotation.equals(scientificNotation)).isTrue();
        });
    }

    @Test
    public void equalsNotScientificNotationShouldReturnFalse() {
        assertThat(new ScientificNotation(BigDecimal.ZERO, 0).equals(new Object())).isFalse();
    }

    @Test
    public void equalsCoefficientNotEqualShouldReturnFalse() {
        scientificNotations.forEach(scientificNotation -> {
            final ScientificNotation other =
                    new ScientificNotation(scientificNotation.getCoefficient().add(BigDecimal.ONE),
                            scientificNotation.getExponent());
            assertThat(scientificNotation.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsExponentNotEqualShouldReturnFalse() {
        scientificNotations.forEach(scientificNotation -> {
            final ScientificNotation other =
                    new ScientificNotation(scientificNotation.getCoefficient(), scientificNotation.getExponent() + 1);
            assertThat(scientificNotation.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        scientificNotations.forEach(scientificNotation -> {
            final ScientificNotation other =
                    new ScientificNotation(scientificNotation.getCoefficient(), scientificNotation.getExponent());
            assertThat(scientificNotation.equals(other)).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        scientificNotations.forEach(scientificNotation -> {
            final String expected = MoreObjects.toStringHelper(scientificNotation)
                    .add("coefficient", scientificNotation.getCoefficient())
                    .add("exponent", scientificNotation.getExponent()).toString();
            assertThat(scientificNotation.toString()).isEqualTo(expected);
        });
    }
}
