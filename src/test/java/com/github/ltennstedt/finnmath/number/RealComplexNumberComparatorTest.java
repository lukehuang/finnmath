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

import com.github.ltennstedt.finnmath.util.MathRandom;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public final class RealComplexNumberComparatorTest {
    private static final int howMany = 10;
    private static final List<RealComplexNumber> complexNumbers = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom(7);
        final int bound = 10;
        final int scale = 2;
        for (int i = 0; i < howMany; i++) {
            complexNumbers.add(mathRandom.nextRealComplexNumber(bound, scale));
        }
    }

    @Test
    public void compareFirstNullShouldReturnMinusOne() {
        assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(null, RealComplexNumber.ZERO))
                .isEqualTo(-1);
    }

    @Test
    public void compareSecondNullShouldReturnOne() {
        assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(RealComplexNumber.ZERO, null)).isEqualTo(1);
    }

    @Test
    public void compareEqualShouldReturnZero() {
        complexNumbers.forEach(complexNumber -> {
            assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(complexNumber, complexNumber))
                    .isEqualTo(0);
        });
    }

    @Test
    public void compareEqualPartsShouldReturnZero() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber second =
                    new RealComplexNumber(complexNumber.getReal(), complexNumber.getImaginary());
            assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(complexNumber, second)).isEqualTo(0);
        });
    }

    @Test
    public void compareRealPartNotEqualShouldReturnOne() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber second =
                    new RealComplexNumber(complexNumber.getReal().add(BigDecimal.ONE), complexNumber.getImaginary());
            assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(complexNumber, second)).isEqualTo(1);
        });
    }

    @Test
    public void compareImaginaryPartNotEqualShouldReturnOne() {
        complexNumbers.forEach(complexNumber -> {
            final RealComplexNumber second =
                    new RealComplexNumber(complexNumber.getReal(), complexNumber.getImaginary().add(BigDecimal.ONE));
            assertThat(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR.compare(complexNumber, second)).isEqualTo(1);
        });
    }
}
