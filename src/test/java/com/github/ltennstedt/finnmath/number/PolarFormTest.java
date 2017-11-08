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

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;
import com.github.ltennstedt.finnmath.util.MathRandom;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public final class PolarFormTest {
    private static final int howMany = 10;
    private static final PolarForm zeroPolarForm = new PolarForm(BigDecimal.ZERO, BigDecimal.ZERO);
    private static final List<PolarForm> polarForms = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom(7);
        final long bound = 10;
        final int scale = 2;
        for (int i = 0; i < howMany; i++) {
            polarForms.add(mathRandom.nextPolarForm(bound, scale));
        }
    }

    @Test
    public void newRadialNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new PolarForm(null, BigDecimal.ZERO);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("radial");
    }

    @Test
    public void newAngularNullShouldThrowException() {
        assertThatThrownBy(() -> {
            new PolarForm(BigDecimal.ZERO, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("angular");
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
            assertThat(polarForm.complexNumber()).isEqualTo(new RealComplexNumber(real, imaginary));
        });
    }

    @Test
    public void complexNumberPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroPolarForm.complexNumber(-1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void complexNumberWithPrecisionShouldSucceed() {
        final Context context = BigFloat.context(99);
        polarForms.forEach(polarForm -> {
            final BigDecimal radial = polarForm.getRadial();
            final BigDecimal angular = polarForm.getAngular();
            final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
            final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
            assertThat(polarForm.complexNumber(99)).isEqualTo(new RealComplexNumber(real, imaginary));
        });
    }

    @Test
    public void complexNumberRoundingModeNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroPolarForm.complexNumber(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void complexNumberWithRoundingModeShouldSucceed() {
        final Context context = BigFloat.context(new MathContext(PolarForm.DEFAULT_PRECISION, RoundingMode.HALF_DOWN));
        polarForms.forEach(polarForm -> {
            final BigDecimal radial = polarForm.getRadial();
            final BigDecimal angular = polarForm.getAngular();
            final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
            final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
            assertThat(polarForm.complexNumber(RoundingMode.HALF_DOWN))
                .isEqualTo(new RealComplexNumber(real, imaginary));
        });
    }

    @Test
    public void complexNumberPrecisionTooLowAndWithRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroPolarForm.complexNumber(-1, RoundingMode.HALF_UP);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision > -1 but actual -1");
    }

    @Test
    public void complexNumberWithPrecisionButRoundingModeNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroPolarForm.complexNumber(0, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("roundingMode");
    }

    @Test
    public void complexNumberWithPrecisionAndRoundingModeShouldSucceed() {
        final Context context = BigFloat.context(new MathContext(99, RoundingMode.HALF_DOWN));
        polarForms.forEach(polarForm -> {
            final BigDecimal radial = polarForm.getRadial();
            final BigDecimal angular = polarForm.getAngular();
            final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
            final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
            assertThat(polarForm.complexNumber(99, RoundingMode.HALF_DOWN))
                .isEqualTo(new RealComplexNumber(real, imaginary));
        });
    }
}
