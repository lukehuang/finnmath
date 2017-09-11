/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2017, Lars Tennstedt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABDLITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABDLITY, WHETHER IN CONTRACT, STRICT LIABDLITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBDLITY OF SUCH DAMAGE.
 */

package finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table.Cell;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * An immutable implementation of a matrix which uses {@link BigInteger} as type
 * for its entries
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class BigIntMatrix extends Matrix<BigInteger, BigIntVector, BigIntMatrix> {
    private BigIntMatrix(final ImmutableTable<Integer, Integer, BigInteger> table) {
        super(table);
    }

    /**
     * Returns the sum of this {@link BigIntMatrix} and the given one
     *
     * @param summand
     *            The summand
     * @return The sum
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != summand.columnSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix add(final BigIntMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
                "expected column sizes equal but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final BigIntMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(it -> {
            final Integer rowKey = it.getRowKey();
            final Integer columnKey = it.getColumnKey();
            builder.put(rowKey, columnKey, it.getValue().add(summand.entry(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the difference of this {@link BigIntMatrix} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != summand.columnSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix subtract(final BigIntMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(),
                subtrahend.columnSize());
        final BigIntMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(it -> {
            final Integer rowKey = it.getRowKey();
            final Integer columnKey = it.getColumnKey();
            builder.put(rowKey, columnKey, it.getValue().subtract(subtrahend.entry(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link BigIntMatrix} and the given one
     *
     * @param factor
     *            the factor
     * @return The product
     * @throws NullPointerException
     *             if {@code factor == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != factor.rowSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix multiply(final BigIntMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
                "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(),
                factor.rowSize());
        final BigIntMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> {
            factor.columns().forEach((columnIndex, column) -> {
                final BigInteger entry = multiplyRowWithColumn(row, column);
                builder.put(rowIndex, columnIndex, entry);
            });
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link BigIntMatrix} and the given
     * {@link BigIntVector}
     *
     * @param vector
     *            the vector
     * @return The product
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntVector#builder
     */
    @Override
    public BigIntVector multiplyVector(final BigIntVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
                "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final BigIntVectorBuilder builder = BigIntVector.builder(vector.size());
        table.rowMap().entrySet().forEach(row -> {
            row.getValue().entrySet().forEach(column -> {
                builder.addToEntryAndPut(row.getKey(), column.getValue().multiply(vector.entry(column.getKey())));
            });
        });
        return builder.build();
    }

    @Override
    protected BigInteger multiplyRowWithColumn(final Map<Integer, BigInteger> row,
            final Map<Integer, BigInteger> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected row size == column size but actual %s != %s", row.size(),
                column.size());
        BigInteger result = BigInteger.ZERO;
        for (final Entry<Integer, BigInteger> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey())));
        }
        return result;
    }

    /**
     * Returns the scalar product of this {@link BigIntMatrix} and the given
     * {@link BigInteger}
     *
     * @param scalar
     *            the scalar
     * @return The scalar product
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix scalarMultiply(final BigInteger scalar) {
        requireNonNull(scalar, "scalar");
        final BigIntMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet().forEach(it -> {
            builder.put(it.getRowKey(), it.getColumnKey(), scalar.multiply(it.getValue()));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link BigIntMatrix} and this one
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    @Override
    public BigIntMatrix negate() {
        return scalarMultiply(BigInteger.ONE.negate());
    }

    /**
     * Returns the trace of this {@link BigIntMatrix}
     *
     * @return The trace
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigInteger trace() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        BigInteger result = BigInteger.ZERO;
        for (final Integer index : table.rowKeySet()) {
            result = result.add(table.get(index, index));
        }
        return result;
    }

    /**
     * Returns the determinant of this {@link BigIntMatrix}
     *
     * @return The determinant
     * @throws IllegalStateException
     *             if {@code !square}
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #minor
     */
    @Override
    public BigInteger det() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        if (table.rowKeySet().size() > 1) {
            BigInteger result = BigInteger.ZERO;
            for (final Entry<Integer, BigInteger> entry : table.row(1).entrySet()) {
                final Integer key = entry.getKey();
                final BigInteger factor = BigInteger.ONE.negate().pow(key + 1).multiply(entry.getValue());
                result = result.add(factor.multiply(minor(1, key).det()));
            }
            return result;
        }
        return table.get(1, 1);
    }

    /**
     * Returns the transpose of this {@link BigIntMatrix}
     *
     * @return The transpose
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix transpose() {
        final BigIntMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> {
            builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
        });
        return builder.build();
    }

    /**
     * Returns the minor of this {@link BigIntMatrix} dependent on the given row and
     * column index
     *
     * @param rowIndex
     *            the row index
     * @param columnIndex
     *            the column index
     * @return The minor
     * @throws NullPointerException
     *             if {@code rowIndex == null}
     * @throws NullPointerException
     *             if {@code columnIndex == null}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public BigIntMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected row index in [1, %s] but actual %s",
                table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(rowIndex), "expected column index in [1, %s] but actual %s",
                table.columnKeySet().size(), columnIndex);
        final BigIntMatrixBuilder builder = builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
        table.cellSet().forEach(it -> {
            final Integer rowKey = it.getRowKey();
            final Integer columnKey = it.getColumnKey();
            final Integer newRowIndex = rowKey >= rowIndex ? rowKey - 1 : rowKey;
            final Integer newColumnIndex = columnKey >= columnIndex ? columnKey - 1 : columnKey;
            builder.put(newRowIndex, newColumnIndex, it.getValue());
        });
        return builder.build();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is a
     * square one
     *
     * @return {@code true} if {@code rowSize == columnSize}, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public boolean square() {
        return table.rowKeySet().size() == table.columnKeySet().size();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * triangular
     *
     * @return {@code true} if {@code upperTriangular || lowerTriangular},
     *         {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    @Override
    public boolean triangular() {
        return upperTriangular() || lowerTriangular();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * upper triangular
     *
     * @return {@code true} if {@code this} is upper triangular, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public boolean upperTriangular() {
        if (square()) {
            for (final Cell<Integer, Integer, BigInteger> cell : table.cellSet()) {
                if ((cell.getRowKey() > cell.getColumnKey()) && !cell.getValue().equals(BigInteger.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * lower triangular
     *
     * @return {@code true} if {@code this} is lower triangular, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public boolean lowerTriangular() {
        if (square()) {
            for (final Cell<Integer, Integer, BigInteger> cell : table.cellSet()) {
                if ((cell.getRowKey() < cell.getColumnKey()) && !cell.getValue().equals(BigInteger.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * diagonal
     *
     * @return {@code true} if {@code upperTriangular && lowerTriangular},
     *         {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    @Override
    public boolean diagonal() {
        return upperTriangular() && lowerTriangular();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is the
     * identity one
     *
     * @return {@code true} if {@code this} is the identity matrix, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #diagonal
     */
    @Override
    public boolean id() {
        if (diagonal()) {
            for (final Integer index : table.rowKeySet()) {
                if (!table.get(index, index).equals(BigInteger.ONE)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * invertible
     *
     * @return {@code true} if {@code det == -1 || det == 1}, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #det
     */
    @Override
    public boolean invertible() {
        return det().equals(BigInteger.ONE.negate()) || det().equals(BigInteger.ONE);
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * symmetric
     *
     * @return {@code true} if {@code square && equals(transpose)}, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #transpose
     */
    @Override
    public boolean symmetric() {
        return square() && equals(transpose());
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is
     * skew symmetric
     *
     * @return {@code true} if {@code square && equals(transpose.negate)},
     *         {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #transpose
     * @see #negate
     */
    @Override
    public boolean skewSymmetric() {
        return square() && transpose().equals(negate());
    }

    /**
     * Returns a {@link BigIntMatrixBuilder}
     *
     * @param rowSize
     *            the row size the resulting {@link BigIntMatrix}
     * @param columnSize
     *            the column size the resulting {@link BigIntMatrix}
     * @return A {@link BigIntMatrixBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    public static BigIntMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected row size > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected column size > 0 but actual %s", columnSize);
        return new BigIntMatrixBuilder(rowSize, columnSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BigIntMatrix)) {
            return false;
        }
        final BigIntMatrix other = (BigIntMatrix) object;
        return Objects.deepEquals(table, other.getTable());
    }

    /**
     * The builder for {@link BigIntMatrix BigIntMatrices}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    public static final class BigIntMatrixBuilder extends MatrixBuilder<BigInteger, BigIntMatrix> {
        private BigIntMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        public BigIntMatrixBuilder put(final Integer rowIndex, final Integer columnIndex, final BigInteger entry) {
            requireNonNull(entry, "entry");
            requireNonNull(rowIndex, "rowIndex");
            requireNonNull(columnIndex, "columnIndex");
            checkArgument(table.rowKeySet().contains(rowIndex), "expected row index in [1, %s] but actual %s",
                    table.rowKeySet().size(), rowIndex);
            checkArgument(table.columnKeySet().contains(columnIndex), "expected column index in [1, %s] but actual %s",
                    table.columnKeySet().size(), columnIndex);
            table.put(rowIndex, columnIndex, entry);
            return this;
        }

        public BigIntMatrixBuilder putAll(final BigInteger entry) {
            requireNonNull(entry, "entry");
            table.rowKeySet().forEach(rowKey -> {
                table.columnKeySet().forEach(columnKey -> {
                    table.put(rowKey, columnKey, entry);
                });
            });
            return this;
        }

        /**
         * Returns the built {@link BigIntMatrix}
         *
         * @return The {@link BigIntMatrix}
         * @throws NullPointerException
         *             if one {@code entry == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableTable#copyOf
         */
        @Override
        public BigIntMatrix build() {
            table.cellSet().forEach(it -> {
                requireNonNull(it.getValue(), "it.value");
            });
            return new BigIntMatrix(ImmutableTable.copyOf(table));
        }
    }
}
