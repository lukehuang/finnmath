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
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.junit.Test;

public final class PolarFormTest {
    private final long bound = 10;
    private final int scale = 2;
    private final MathRandom mathRandom = new MathRandom(7);
    private final int howMany = 10;
    private final PolarForm zeroPolarForm = new PolarForm(BigDecimal.ZERO, BigDecimal.ZERO);
    private final List<PolarForm> polarForms = mathRandom.nextPolarForms(bound, scale, howMany);

    @Test
    public void newRadialNullShouldThrowException() {
        assertThatThrownBy(() -> new PolarForm(null, BigDecimal.ZERO)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("radial");
    }

    @Test
    public void newAngularNullShouldThrowException() {
        assertThatThrownBy(() -> new PolarForm(BigDecimal.ZERO, null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("angular");
    }

    @Test
    public void newShouldSucceed() {
        final PolarForm actual = new PolarForm(BigDecimal.ONE, BigDecimal.TEN);
        assertThat(actual.getRadial()).isEqualTo(BigDecimal.ONE);
        assertThat(actual.getAngular()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    public void complexNumberShouldSucceed() {
        final Context context = BigFloat.context(PolarForm.DEFAULT_PRECISION);
        polarForms.forEach(polarForm -> {
            final BigDecimal radial = polarForm.getRadial();
            final BigDecimal angular = polarForm.getAngular();
            final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
            final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
            assertThat(polarForm.complexNumber()).isEqualTo(RealComplexNumber.of(real, imaginary));
        });
    }

    @Test
    public void complexNumberMathContextShouldThrowException() {
        assertThatThrownBy(() -> zeroPolarForm.complexNumber((MathContext) null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("mathContext");
    }

    @Test
    public void complexNumberWithMathContextShouldSucceed() {
        final MathContext mathContext = new MathContext(99, RoundingMode.HALF_DOWN);
        final Context context = BigFloat.context(mathContext);
        polarForms.forEach(polarForm -> {
            final BigDecimal radial = polarForm.getRadial();
            final BigDecimal angular = polarForm.getAngular();
            final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
            final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
            assertThat(polarForm.complexNumber(mathContext)).isEqualTo(RealComplexNumber.of(real, imaginary));
        });
    }

    @Test
    public void hashCodeShouldSucceed() {
        polarForms.forEach(polarForm -> assertThat(polarForm.hashCode())
            .isEqualTo(Objects.hash(polarForm.getRadial(), polarForm.getAngular())));
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        polarForms.forEach(polarForm -> assertThat(polarForm.equals(polarForm)).isTrue());
    }

    @Test
    public void equalsNotpolarFormShouldReturnFalse() {
        assertThat(zeroPolarForm.equals(new Object())).isFalse();
    }

    @Test
    public void equalsRadialNotEqualShouldReturnFalse() {
        polarForms.forEach(polarForm -> {
            final PolarForm other = new PolarForm(polarForm.getRadial().add(BigDecimal.ONE), polarForm.getAngular());
            assertThat(polarForm.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsAngularNotEqualShouldReturnFalse() {
        polarForms.forEach(polarForm -> {
            final PolarForm other = new PolarForm(polarForm.getRadial(), polarForm.getAngular().add(BigDecimal.ONE));
            assertThat(polarForm.equals(other)).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        polarForms.forEach(
            polarForm -> assertThat(polarForm.equals(new PolarForm(polarForm.getRadial(), polarForm.getAngular())))
                .isTrue());
    }

    @Test
    public void hashCodeEqualPolarFormsShouldHaveEqualHashCodes() {
        polarForms.forEach(polarForm -> {
            final PolarForm other = new PolarForm(polarForm.getRadial(), polarForm.getAngular());
            assertThat(polarForm.hashCode()).isEqualTo(other.hashCode());
        });
    }

    @Test
    public void toStringShouldSucceed() {
        polarForms.forEach(polarForm -> assertThat(polarForm.toString()).isEqualTo(MoreObjects.toStringHelper(polarForm)
            .add("radial", polarForm.getRadial()).add("angular", polarForm.getAngular()).toString()));
    }
}
