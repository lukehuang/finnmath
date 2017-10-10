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

package com.github.ltennstedt.finnmath.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.linear.BigIntMatrix.BigIntMatrixBuilder;
import com.google.common.base.MoreObjects;
import java.math.BigInteger;
import org.junit.Test;

public final class BigIntMatrixBuilderTest {
    private static final BigIntMatrixBuilder builder = BigIntMatrix.builder(2, 3);

    @Test
    public void putRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(null, 1, BigInteger.ZERO);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("rowIndex");
    }

    @Test
    public void putColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(1, null, BigInteger.ZERO);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("columnIndex");
    }

    @Test
    public void putElementNullShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(1, 1, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("element");
    }

    @Test
    public void putRowIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(0, 1, BigInteger.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowIndex in [1, %s] but actual %s",
                2, 0);
    }

    @Test
    public void putRowIndexTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(3, 1, BigInteger.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowIndex in [1, %s] but actual %s",
                2, 3);
    }

    @Test
    public void putColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(1, 0, BigInteger.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnIndex in [1, %s] but actual %s", 3, 0);
    }

    @Test
    public void putColumnIndexTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.put(1, 4, BigInteger.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnIndex in [1, %s] but actual %s", 3, 4);
    }

    @Test
    public void putAllNullShouldThrowException() {
        assertThatThrownBy(() -> {
            builder.putAll(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("element");
    }

    @Test
    public void buildNullShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(2, 3).build();
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("cell.value");
    }

    @Test
    public void toStringShouldSucceed() {
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 4).putAll(BigInteger.ZERO);
        assertThat(builder.toString())
                .isEqualTo(MoreObjects.toStringHelper(builder).add("table", builder.getTable()).toString());
    }
}
