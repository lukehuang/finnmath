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

public final class BigIntegerVectorBuilderTest {
    private final BigIntegerVector.BigIntegerVectorBuilder builder = BigIntegerVector.builder(4);

    @Test
    public void putIndexNullShouldThrowException() {
        assertThatThrownBy(() -> builder.put(null, BigInteger.ZERO)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("index");
    }

    @Test
    public void putElementNullShouldThrowException() {
        assertThatThrownBy(() -> builder.put(1, null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("element");
    }

    @Test
    public void putIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> builder.put(0, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected index in [1, %s] but actual %s", 4, 0);
    }

    @Test
    public void putRowIndexTooHighShouldThrowException() {
        assertThatThrownBy(() -> builder.put(5, BigInteger.ZERO)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected index in [1, %s] but actual %s", 4, 5);
    }

    @Test
    public void putAllNullShouldThrowException() {
        assertThatThrownBy(() -> builder.putAll(null)).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("element");
    }

    @Test
    public void buildNullShouldThrowException() {
        assertThatThrownBy(() -> BigIntegerVector.builder(4).build()).isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("map.value");
    }

    @Test
    public void toStringShouldSucceed() {
        final BigIntegerVector.BigIntegerVectorBuilder actual = builder.putAll(BigInteger.ZERO);
        assertThat(actual.toString()).isEqualTo(
                MoreObjects.toStringHelper(builder).add("map", builder.getMap()).add("size", builder.getSize())
                        .toString());
    }
}
