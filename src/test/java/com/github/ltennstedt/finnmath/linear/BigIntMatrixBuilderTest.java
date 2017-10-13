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
  @Test
  public void putRowIndexNullShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(null, 1, BigInteger.ZERO);
    }).isExactlyInstanceOf(NullPointerException.class).hasMessage("rowIndex");
  }

  @Test
  public void putColumnIndexNullShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(1, null, BigInteger.ZERO);
    }).isExactlyInstanceOf(NullPointerException.class).hasMessage("columnIndex");
  }

  @Test
  public void putElementNullShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(1, 1, null);
    }).isExactlyInstanceOf(NullPointerException.class).hasMessage("element");
  }

  @Test
  public void putRowIndexTooLowShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(0, 1, BigInteger.ZERO);
    }).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("expected rowIndex in [1, %s] but actual %s", 4, 0);
  }

  @Test
  public void putRowIndexTooHighShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(5, 1, BigInteger.ZERO);
    }).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("expected rowIndex in [1, %s] but actual %s", 4, 5);
  }

  @Test
  public void putColumnIndexTooLowShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(1, 0, BigInteger.ZERO);
    }).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("expected columnIndex in [1, %s] but actual %s", 5, 0);
  }

  @Test
  public void putColumnIndexTooHighShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).put(1, 6, BigInteger.ZERO);
    }).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("expected columnIndex in [1, %s] but actual %s", 5, 6);
  }

  @Test
  public void putAllNullShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).putAll(null);
    }).isExactlyInstanceOf(NullPointerException.class).hasMessage("element");
  }

  @Test
  public void buildNullShouldThrowException() {
    assertThatThrownBy(() -> {
      BigIntMatrix.builder(4, 5).build();
    }).isExactlyInstanceOf(NullPointerException.class).hasMessage("cell.value");
  }

  @Test
  public void toStringShouldSucceed() {
    final BigIntMatrixBuilder builder = BigIntMatrix.builder(4, 5).putAll(BigInteger.ZERO);
    assertThat(builder.toString())
        .isEqualTo(MoreObjects.toStringHelper(builder).add("table", builder.getTable()).toString());
  }
}
