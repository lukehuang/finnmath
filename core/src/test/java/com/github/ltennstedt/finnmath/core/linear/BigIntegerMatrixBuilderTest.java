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

import com.google.common.base.MoreObjects;
import java.math.BigInteger;
import org.junit.Test;

public final class BigIntegerMatrixBuilderTest {
    private final BigIntegerMatrix.BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(4, 5);

    @Test
    public void putRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> builder.put(null, 1, BigInteger.ZERO)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("rowIndex");
    }

    @Test
    public void putColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> builder.put(1, null, BigInteger.ZERO)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("columnIndex");
    }

    @Test
    public void putElementNullShouldThrowException() {
        assertThatThrownBy(() -> builder.put(1, 1, null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("element");
    }

    @Test
    public void putRowIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> builder.put(0, 1, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowIndex in [1, %s] but actual %s", 4, 0);
    }

    @Test
    public void putRowIndexTooHighShouldThrowException() {
        assertThatThrownBy(() -> builder.put(5, 1, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowIndex in [1, %s] but actual %s", 4, 5);
    }

    @Test
    public void putColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> builder.put(1, 0, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnIndex in [1, %s] but actual %s", 5, 0);
    }

    @Test
    public void putColumnIndexTooHighShouldThrowException() {
        assertThatThrownBy(() -> builder.put(1, 6, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnIndex in [1, %s] but actual %s", 5, 6);
    }

    @Test
    public void putAllNullShouldThrowException() {
        assertThatThrownBy(() -> builder.putAll(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("element");
    }

    @Test
    public void buildNullShouldThrowException() {
        assertThatThrownBy(() -> BigIntegerMatrix.builder(4, 4).build()).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("cell.value");
    }

    @Test
    public void toStringShouldSucceed() {
        final BigIntegerMatrix.BigIntegerMatrixBuilder actual = builder.putAll(BigInteger.ZERO);
        assertThat(actual.toString())
            .isEqualTo(MoreObjects.toStringHelper(builder).add("table", builder.getTable()).toString());
    }
}
