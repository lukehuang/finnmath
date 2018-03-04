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

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix.BigIntegerMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix.RealComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix.SimpleComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.IntStream;
import org.junit.Test;

public final class MatricesTest {
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;

    @Test
    public void buildZeroBigIntegerMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroBigIntegerMatrix(0, columnSize))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void buildZeroBigIntegerMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroBigIntegerMatrix(rowSize, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void buildZeroBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrix expected = BigIntegerMatrix.builder(rowSize, columnSize).putAll(BigInteger.ZERO).build();
        assertThat(Matrices.buildZeroBigIntegerMatrix(rowSize, columnSize)).isEqualTo(expected);
    }

    @Test
    public void buildIdentityBigIntegerMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildIdentityBigIntegerMatrix(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildIdentityBigIntegerMatrixShouldSucceed() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, BigInteger.ONE));
        final BigIntegerMatrix expected = builder.nullsToElement(BigInteger.ZERO).build();
        assertThat(Matrices.buildIdentityBigIntegerMatrix(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroBigDecimalMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroBigDecimalMatrix(0, columnSize))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void buildZeroBigDecimalMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroBigDecimalMatrix(rowSize, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void buildZeroBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrix expected = BigDecimalMatrix.builder(rowSize, columnSize).putAll(BigDecimal.ZERO).build();
        assertThat(Matrices.buildZeroBigDecimalMatrix(rowSize, columnSize)).isEqualTo(expected);
    }

    @Test
    public void buildIdentityBigDecimalMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildIdentityBigDecimalMatrix(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildIdentityBigDecimalMatrixShouldSucceed() {
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, BigDecimal.ONE));
        final BigDecimalMatrix expected = builder.nullsToElement(BigDecimal.ZERO).build();
        assertThat(Matrices.buildIdentityBigDecimalMatrix(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroSimpleComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroSimpleComplexNumberMatrix(0, columnSize))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void buildZeroSimpleComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroSimpleComplexNumberMatrix(rowSize, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void buildZeroSimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrix expected =
            SimpleComplexNumberMatrix.builder(rowSize, columnSize).putAll(SimpleComplexNumber.ZERO).build();
        assertThat(Matrices.buildZeroSimpleComplexNumberMatrix(rowSize, columnSize)).isEqualTo(expected);
    }

    @Test
    public void buildIdentitySimpleComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildIdentitySimpleComplexNumberMatrix(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildIdentitySimpleComplexNumberMatrixShouldSucceed() {
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, SimpleComplexNumber.ONE));
        final SimpleComplexNumberMatrix expected = builder.nullsToElement(SimpleComplexNumber.ZERO).build();
        assertThat(Matrices.buildIdentitySimpleComplexNumberMatrix(size)).isEqualTo(expected);
    }

    @Test
    public void buildZeroRealComplexNumberMatrixRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroRealComplexNumberMatrix(0, columnSize))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void buildZeroRealComplexNumberMatrixColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroRealComplexNumberMatrix(rowSize, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void buildZeroRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrix expected =
            RealComplexNumberMatrix.builder(rowSize, columnSize).putAll(RealComplexNumber.ZERO).build();
        assertThat(Matrices.buildZeroRealComplexNumberMatrix(rowSize, columnSize)).isEqualTo(expected);
    }

    @Test
    public void buildIdentityRealComplexNumberMatrixSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildIdentityRealComplexNumberMatrix(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void buildIdentityRealComplexNumberMatrixShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, RealComplexNumber.ONE));
        final RealComplexNumberMatrix expected = builder.nullsToElement(RealComplexNumber.ZERO).build();
        assertThat(Matrices.buildIdentityRealComplexNumberMatrix(size)).isEqualTo(expected);
    }
}
