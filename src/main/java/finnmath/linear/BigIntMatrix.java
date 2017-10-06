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

package finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * An immutable implementation of a matrix which uses {@link BigInteger} as type
 * for its elements
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class BigIntMatrix extends AbstractMatrix<BigInteger, BigIntVector, BigIntMatrix> {
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
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final BigIntMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey)));
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
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().subtract(subtrahend.element(rowKey, columnKey)));
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
                final BigInteger element = multiplyRowWithColumn(row, column);
                builder.put(rowIndex, columnIndex, element);
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
        final BigIntVectorBuilder builder = BigIntVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> {
            row.forEach((columnIndex, matrixEntry) -> {
                final BigInteger oldEntry = builder.element(rowIndex) != null ? builder.element(rowIndex)
                        : BigInteger.ZERO;
                builder.put(rowIndex, oldEntry.add(matrixEntry.multiply(vector.element(columnIndex))));
            });
        });
        return builder.build();
    }

    @Override
    protected BigInteger multiplyRowWithColumn(final Map<Integer, BigInteger> row,
            final Map<Integer, BigInteger> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected rowSize == columnSize but actual %s != %s", row.size(),
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
        table.cellSet().forEach(cell -> {
            builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
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
     * <p>
     * Only implemented for 1 x 1, 2 x 2, and 3 x 3 matrices
     *
     * @return The determinant
     * @throws IllegalStateException
     *             if {@code !square}
     * @throws IllegalStateException
     *             if {@code rowSize > 3}
     * @throws IllegalStateException
     *             if {@code columnSize > 3}
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #minor
     */
    @Override
    public BigInteger determinant() {
        checkState(table.rowKeySet().size() < 4, "expected rowSize < 4 but actual %s", table.rowKeySet().size());
        checkState(table.columnKeySet().size() < 4, "expected columnSize < 4 but actual %s",
                table.columnKeySet().size());
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        if (triangular()) {
            BigInteger result = BigInteger.ONE;
            for (final Cell<Integer, Integer, BigInteger> cell : table.cellSet()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    result = result.multiply(cell.getValue());
                }
            }
            return result;
        }
        if (table.rowKeySet().size() == 3) {
            return ruleOfSarrus();
        }
        return table.get(1, 1).multiply(table.get(2, 2)).subtract(table.get(1, 2).multiply(table.get(2, 1)));
    }

    @Override
    protected BigInteger ruleOfSarrus() {
        final BigInteger firstSummand = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final BigInteger secondSummand = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final BigInteger thirdSummand = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final BigInteger fourthSummand = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(1, 3)).negate();
        final BigInteger fifthSummand = table.get(3, 2).multiply(table.get(2, 3)).multiply(table.get(1, 1)).negate();
        final BigInteger sixthSummand = table.get(3, 3).multiply(table.get(2, 1)).multiply(table.get(1, 2)).negate();
        return firstSummand.add(secondSummand).add(thirdSummand).add(fourthSummand).add(fifthSummand).add(sixthSummand);
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
        checkArgument(table.containsRow(rowIndex), "expected rowIndex in [1, %s] but actual %s",
                table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected columnIndex in [1, %s] but actual %s",
                table.columnKeySet().size(), columnIndex);
        final BigIntMatrixBuilder builder = builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            if (!rowKey.equals(rowIndex) && !columnKey.equals(columnIndex)) {
                final Integer newRowIndex = rowKey > rowIndex ? rowKey - 1 : rowKey;
                final Integer newColumnIndex = columnKey > columnIndex ? columnKey - 1 : columnKey;
                builder.put(newRowIndex, newColumnIndex, cell.getValue());
            }
        });
        return builder.build();
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
    public boolean identity() {
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
     * @see #determinant
     */
    @Override
    public boolean invertible() {
        return determinant().equals(BigInteger.ONE.negate()) || determinant().equals(BigInteger.ONE);
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
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
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
    public static final class BigIntMatrixBuilder extends AbstractMatrixBuilder<BigInteger, BigIntMatrix> {
        private BigIntMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * Puts the given element on the {@link Table} dependent on the given row and
         * column index
         *
         * @param rowIndex
         *            thr row index
         * @param columnIndex
         *            the column index
         * @param element
         *            the element
         * @return {@code this}
         */
        public BigIntMatrixBuilder put(final Integer rowIndex, final Integer columnIndex, final BigInteger element) {
            requireNonNull(element, "element");
            requireNonNull(rowIndex, "rowIndex");
            requireNonNull(columnIndex, "columnIndex");
            checkArgument(table.rowKeySet().contains(rowIndex), "expected rowIndex in [1, %s] but actual %s",
                    table.rowKeySet().size(), rowIndex);
            checkArgument(table.columnKeySet().contains(columnIndex), "expected columnIndex in [1, %s] but actual %s",
                    table.columnKeySet().size(), columnIndex);
            table.put(rowIndex, columnIndex, element);
            return this;
        }

        /**
         * Puts the given element on all indices and returns {@code this}
         *
         * @param element
         *            the element
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code element == null}
         * @since 1
         * @author Lars Tennstedt
         */
        public BigIntMatrixBuilder putAll(final BigInteger element) {
            requireNonNull(element, "element");
            table.rowKeySet().forEach(rowKey -> {
                table.columnKeySet().forEach(columnKey -> {
                    table.put(rowKey, columnKey, element);
                });
            });
            return this;
        }

        /**
         * Returns the built {@link BigIntMatrix}
         *
         * @return The {@link BigIntMatrix}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableTable#copyOf
         */
        @Override
        public BigIntMatrix build() {
            table.cellSet().forEach(cell -> {
                requireNonNull(cell.getValue(), "cell.value");
            });
            return new BigIntMatrix(ImmutableTable.copyOf(table));
        }
    }
}
