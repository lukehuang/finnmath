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

package com.github.ltennstedt.finnmath.core.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;

public final class VectorsTest {
    private final int size = 4;

    @Test
    public void buildZeroBigIntegerVectorSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Vectors.buildZeroBigIntegerVector(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildZeroBigIntegerVectorSizeShouldSucceed() {
        final BigIntegerVector expected = BigIntegerVector.builder(size).putAll(BigInteger.ZERO).build();
        assertThat(Vectors.buildZeroBigIntegerVector(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroBigDecimalVectorSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Vectors.buildZeroBigDecimalVector(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildZeroBigDecimalVectorSizeShouldSucceed() {
        final BigDecimalVector expected = BigDecimalVector.builder(size).putAll(BigDecimal.ZERO).build();
        assertThat(Vectors.buildZeroBigDecimalVector(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroSimpleComplexNumberVectorSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Vectors.buildZeroSimpleComplexNumberVector(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildZeroSimpleComplexNumberVectorSizeShouldSucceed() {
        final SimpleComplexNumberVector expected =
            SimpleComplexNumberVector.builder(size).putAll(SimpleComplexNumber.ZERO).build();
        assertThat(Vectors.buildZeroSimpleComplexNumberVector(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroRealComplexNumberVectorSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Vectors.buildZeroRealComplexNumberVector(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildZeroRealComplexNumberVectorSizeShouldSucceed() {
        final RealComplexNumberVector expected =
            RealComplexNumberVector.builder(size).putAll(RealComplexNumber.ZERO).build();
        assertThat(Vectors.buildZeroRealComplexNumberVector(size)).isEqualTo(expected);
    }
}
