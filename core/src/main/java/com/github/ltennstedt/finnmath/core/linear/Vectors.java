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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector.BigIntegerVectorBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberVector.RealComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.google.common.annotations.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Utility class for building zero vectors
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class Vectors {
    private Vectors() {
    }

    /**
     * Returns the zero vector
     *
     * @param size
     *            size
     * @return zero vector
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static BigIntegerVector buildZeroBigIntegerVector(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return BigIntegerVector.builder(size).putAll(BigInteger.ZERO).build();
    }

    /**
     * Returns the zero vector
     *
     * @param size
     *            size
     * @return zero vector
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static BigDecimalVector buildZeroBigDecimalVector(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return BigDecimalVector.builder(size).putAll(BigDecimal.ZERO).build();
    }

    /**
     * Returns the zero vector
     *
     * @param size
     *            size
     * @return zero vector
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static SimpleComplexNumberVector buildZeroSimpleComplexNumberVector(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return SimpleComplexNumberVector.builder(size).putAll(SimpleComplexNumber.ZERO).build();
    }

    /**
     * Returns the zero vector
     *
     * @param size
     *            size
     * @return zero vector
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static RealComplexNumberVector buildZeroRealComplexNumberVector(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        return RealComplexNumberVector.builder(size).putAll(RealComplexNumber.ZERO).build();
    }

    /**
     * Returns a copy of the given {@link BigIntegerVector}
     *
     * @param vector
     *            {@link BigIntegerVector}
     * @return copy
     * @throws NullPointerException
     *             if {@code vector == null}
     * @since 1
     */
    public static BigIntegerVector copyOf(final BigIntegerVector vector) {
        requireNonNull(vector, "vector");
        final BigIntegerVectorBuilder builder = BigIntegerVector.builder(vector.size());
        IntStream.rangeClosed(1, vector.size()).forEach(i -> builder.put(vector.element(i)));
        return builder.build();
    }

    /**
     * Returns a copy of the given {@link SimpleComplexNumberVector}
     *
     * @param vector
     *            {@link SimpleComplexNumberVector}
     * @return copy
     * @throws NullPointerException
     *             if {@code vector == null}
     * @since 1
     */
    public static SimpleComplexNumberVector copyOf(final SimpleComplexNumberVector vector) {
        requireNonNull(vector, "vector");
        final SimpleComplexNumberVectorBuilder builder = SimpleComplexNumberVector.builder(vector.size());
        IntStream.rangeClosed(1, vector.size()).forEach(i -> builder.put(vector.element(i)));
        return builder.build();
    }

    /**
     * Returns a copy of the given {@link SimpleComplexNumberVector}
     *
     * @param vector
     *            {@link SimpleComplexNumberVector}
     * @return copy
     * @throws NullPointerException
     *             if {@code vector == null}
     * @since 1
     */
    public static RealComplexNumberVector copyOf(final RealComplexNumberVector vector) {
        requireNonNull(vector, "vector");
        final RealComplexNumberVectorBuilder builder = RealComplexNumberVector.builder(vector.size());
        IntStream.rangeClosed(1, vector.size()).forEach(i -> builder.put(vector.element(i)));
        return builder.build();
    }
}
