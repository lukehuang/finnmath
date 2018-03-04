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

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix.BigIntegerMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix.RealComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix.SimpleComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.google.common.annotations.Beta;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Utility class for building zero and identity matrices
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class Matrices {
    private Matrices() {
    }

    /**
     * Returns the zero matrix
     *
     * @param rowSize
     *            row size
     * @param columnSize
     *            column size
     * @return zero matrix
     * @throws IllegalArgumentException
     *             if {@code rowsize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnsize < 1}
     * @since 1
     */
    public static BigIntegerMatrix buildZeroBigIntegerMatrix(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return BigIntegerMatrix.builder(rowSize, columnSize).putAll(BigInteger.ZERO).build();
    }

    /**
     * Returns the identity matrix
     *
     * @param size
     *            size
     * @return identity matrix
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static BigIntegerMatrix buildIdentityBigIntegerMatrix(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, BigInteger.ONE));
        return builder.nullsToElement(BigInteger.ZERO).build();
    }

    /**
     * Returns the zero matrix
     *
     * @param rowSize
     *            row size
     * @param columnSize
     *            column size
     * @return zero matrix
     * @throws IllegalArgumentException
     *             if {@code rowsize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnsize < 1}
     * @since 1
     */
    public static BigDecimalMatrix buildZeroBigDecimalMatrix(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return BigDecimalMatrix.builder(rowSize, columnSize).putAll(BigDecimal.ZERO).build();
    }

    /**
     * Returns the identity matrix
     *
     * @param size
     *            size
     * @return identity matrix
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static BigDecimalMatrix buildIdentityBigDecimalMatrix(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, BigDecimal.ONE));
        return builder.nullsToElement(BigDecimal.ZERO).build();
    }

    /**
     * Returns the zero matrix
     *
     * @param rowSize
     *            row size
     * @param columnSize
     *            column size
     * @return zero matrix
     * @throws IllegalArgumentException
     *             if {@code rowsize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnsize < 1}
     * @since 1
     */
    public static SimpleComplexNumberMatrix buildZeroSimpleComplexNumberMatrix(final int rowSize,
        final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return SimpleComplexNumberMatrix.builder(rowSize, columnSize).putAll(SimpleComplexNumber.ZERO).build();
    }

    /**
     * Returns the identity matrix
     *
     * @param size
     *            size
     * @return identity matrix
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static SimpleComplexNumberMatrix buildIdentitySimpleComplexNumberMatrix(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, SimpleComplexNumber.ONE));
        return builder.nullsToElement(SimpleComplexNumber.ZERO).build();
    }

    /**
     * Returns the zero matrix
     *
     * @param rowSize
     *            row size
     * @param columnSize
     *            column size
     * @return zero matrix
     * @throws IllegalArgumentException
     *             if {@code rowsize < 1}
     * @throws IllegalArgumentException
     *             if {@code columnsize < 1}
     * @since 1
     */
    public static RealComplexNumberMatrix buildZeroRealComplexNumberMatrix(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return RealComplexNumberMatrix.builder(rowSize, columnSize).putAll(RealComplexNumber.ZERO).build();
    }

    /**
     * Returns the identity matrix
     *
     * @param size
     *            size
     * @return identity matrix
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     */
    public static RealComplexNumberMatrix buildIdentityRealComplexNumberMatrix(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        IntStream.rangeClosed(1, size).forEach(index -> builder.put(index, index, RealComplexNumber.ONE));
        return builder.nullsToElement(RealComplexNumber.ZERO).build();
    }
}
