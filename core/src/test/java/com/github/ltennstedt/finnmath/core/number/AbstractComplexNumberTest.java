/*
 * Copyright 2018 Lars Tennstedt
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
import org.junit.Test;

public final class AbstractComplexNumberTest {
    private final long bound = 10;
    private final int howMany = 10;
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<SimpleComplexNumber> complexNumbers = mathRandom.nextSimpleComplexNumbers(bound, howMany);
    private final List<SimpleComplexNumber> invertibles = mathRandom.nextInvertibleSimpleComplexNumbers(bound, howMany);

    @Test
    public void divideNullShouldThrowException() {
        assertThatThrownBy(() -> SimpleComplexNumber.ZERO.divide(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("divisor");
    }

    @Test
    public void divideNotInvertibleShouldThrowException() {
        assertThatThrownBy(() -> SimpleComplexNumber.ONE.divide(SimpleComplexNumber.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected divisor to be invertible but actual " + SimpleComplexNumber.ZERO);
    }

    @Test
    public void divideShoulSucceed() {
        complexNumbers.forEach(complexNumber -> invertibles
            .forEach(invertible -> complexNumber.divide(invertible, AbstractComplexNumber.DEFAULT_ROUNDING_MODE)));
    }

    @Test
    public void absShouldSucceed() {
        complexNumbers.forEach(complexNumber -> assertThat(complexNumber.abs())
            .isEqualTo(complexNumber.abs(AbstractComplexNumber.DEFAULT_SQUARE_ROOT_CONTEXT)));
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
            final BigInteger real = complexNumber.getReal().add(BigInteger.ONE);
            final SimpleComplexNumber other = SimpleComplexNumber.of(real, complexNumber.getImaginary());
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsImaginaryNotEqualShouldReturnFalse() {
        complexNumbers.forEach(complexNumber -> {
            final BigInteger imaginary = complexNumber.getImaginary().add(BigInteger.ONE);
            final SimpleComplexNumber other = SimpleComplexNumber.of(complexNumber.getReal(), imaginary);
            assertThat(complexNumber.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        complexNumbers.forEach(complexNumber -> assertThat(
            complexNumber.equals(SimpleComplexNumber.of(complexNumber.getReal(), complexNumber.getImaginary())))
                .isTrue());
    }

    @Test
    public void hashCodeEqualSimpleComplexNumbersShouldHaveEqualHashCodes() {
        complexNumbers.forEach(complexNumber -> {
            final SimpleComplexNumber other =
                SimpleComplexNumber.of(complexNumber.getReal(), complexNumber.getImaginary());
            assertThat(complexNumber.hashCode()).isEqualTo(other.hashCode());
        });
    }

    @Test
    public void toStringShouldSucceed() {
        complexNumbers.forEach(
            complexNumber -> assertThat(complexNumber.toString()).isEqualTo(MoreObjects.toStringHelper(complexNumber)
                .add("real", complexNumber.getReal()).add("imaginary", complexNumber.getImaginary()).toString()));
    }
}
