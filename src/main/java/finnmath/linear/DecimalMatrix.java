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
import com.google.common.collect.Table.Cell;
import finnmath.linear.DecimalVector.DecimalVectorBuilder;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * An immutable implementation of a matrix which uses {@link BigDecimal} as type
 * for its elements
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class DecimalMatrix extends AbstractMatrix<BigDecimal, DecimalVector, DecimalMatrix> {
    private DecimalMatrix(final ImmutableTable<Integer, Integer, BigDecimal> table) {
        super(table);
    }

    /**
     * Returns the sum of this {@link DecimalMatrix} and the given one
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
    public DecimalMatrix add(final DecimalMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final DecimalMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the difference of this {@link DecimalMatrix} and the given one
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
    public DecimalMatrix subtract(final DecimalMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(),
                subtrahend.columnSize());
        final DecimalMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().subtract(subtrahend.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link DecimalMatrix} and the given one
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
    public DecimalMatrix multiply(final DecimalMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
                "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(),
                factor.rowSize());
        final DecimalMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> {
            factor.columns().forEach((columnIndex, column) -> {
                final BigDecimal element = multiplyRowWithColumn(row, column);
                builder.put(rowIndex, columnIndex, element);
            });
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link DecimalMatrix} and the given
     * {@link DecimalVector}
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
     * @see DecimalVector#builder
     */
    @Override
    public DecimalVector multiplyVector(final DecimalVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
                "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final DecimalVectorBuilder builder = DecimalVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> {
            row.forEach((columnIndex, matrixEntry) -> {
                final BigDecimal oldEntry = builder.element(rowIndex) != null ? builder.element(rowIndex)
                        : BigDecimal.ZERO;
                builder.put(rowIndex, oldEntry.add(matrixEntry.multiply(vector.element(columnIndex))));
            });
        });
        return builder.build();
    }

    @Override
    protected BigDecimal multiplyRowWithColumn(final Map<Integer, BigDecimal> row,
            final Map<Integer, BigDecimal> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected row size == column size but actual %s != %s", row.size(),
                column.size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Entry<Integer, BigDecimal> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey())));
        }
        return result;
    }

    /**
     * Returns the scalar product of this {@link DecimalMatrix} and the given
     * {@link BigDecimal}
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
    public DecimalMatrix scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final DecimalMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet().forEach(cell -> {
            builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link DecimalMatrix} and this one
     *
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    @Override
    public DecimalMatrix negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * Returns the trace of this {@link DecimalMatrix}
     *
     * @return The trace
     * @since 1
     * @author Lars Tennstedt
     */
    @Override
    public BigDecimal trace() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Integer index : table.rowKeySet()) {
            result = result.add(table.get(index, index));
        }
        return result;
    }

    /**
     * Returns the determinant of this {@link DecimalMatrix}
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
    public BigDecimal determinant() {
        checkState(table.rowKeySet().size() < 4, "expected row size < 4 but actual %s", table.rowKeySet().size());
        checkState(table.columnKeySet().size() < 4, "expected column size < 4 but actual %s",
                table.columnKeySet().size());
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        final int scale = table.get(1, 1).scale();
        if (triangular()) {
            BigDecimal result = BigDecimal.ONE.setScale(scale);
            for (final Cell<Integer, Integer, BigDecimal> cell : table.cellSet()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    result = result.multiply(cell.getValue());
                }
            }
            return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        if (table.rowKeySet().size() == 3) {
            return ruleOfSarrus();
        }
        if (table.rowKeySet().size() == 2) {
            return table.get(1, 1).multiply(table.get(2, 2)).subtract(table.get(1, 2).multiply(table.get(2, 1)))
                    .setScale(scale, BigDecimal.ROUND_HALF_UP);
        }
        return table.get(1, 1);
    }

    @Override
    protected BigDecimal ruleOfSarrus() {
        final BigDecimal firstSummand = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final BigDecimal secondSummand = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final BigDecimal thirdSummand = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final BigDecimal fourthSummand = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(3, 1)).negate();
        final BigDecimal fifthSummand = table.get(1, 2).multiply(table.get(2, 1)).multiply(table.get(3, 3)).negate();
        final BigDecimal sixthSummand = table.get(1, 1).multiply(table.get(2, 3)).multiply(table.get(3, 2)).negate();
        return firstSummand.add(secondSummand).add(thirdSummand).add(fourthSummand).add(fifthSummand).add(sixthSummand)
                .setScale(table.get(1, 1).scale(), BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the transpose of this {@link DecimalMatrix}
     *
     * @return The transpose
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    @Override
    public DecimalMatrix transpose() {
        final DecimalMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> {
            builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
        });
        return builder.build();
    }

    /**
     * Returns the minor of this {@link DecimalMatrix} dependent on the given row
     * and column index
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
    public DecimalMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected row index in [1, %s] but actual %s",
                table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected column index in [1, %s] but actual %s",
                table.columnKeySet().size(), columnIndex);
        final DecimalMatrixBuilder builder = builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
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
     * Returns a {@code boolean} which indicates if this {@link DecimalMatrix} is
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
            for (final Cell<Integer, Integer, BigDecimal> cell : table.cellSet()) {
                if ((cell.getRowKey() > cell.getColumnKey()) && !cell.getValue().equals(BigDecimal.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link DecimalMatrix} is
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
            for (final Cell<Integer, Integer, BigDecimal> cell : table.cellSet()) {
                if ((cell.getRowKey() < cell.getColumnKey()) && !cell.getValue().equals(BigDecimal.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link DecimalMatrix} is
     * the identity one
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
                if (!table.get(index, index).equals(BigDecimal.ONE)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link DecimalMatrix} is
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
        return !determinant().equals(BigDecimal.ZERO);
    }

    /**
     * Returns a {@link DecimalMatrixBuilder}
     *
     * @param rowSize
     *            the row size the resulting {@link DecimalMatrix}
     * @param columnSize
     *            the column size the resulting {@link DecimalMatrix}
     * @return A {@link DecimalMatrixBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    public static DecimalMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected row size > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected column size > 0 but actual %s", columnSize);
        return new DecimalMatrixBuilder(rowSize, columnSize);
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
        if (!(object instanceof DecimalMatrix)) {
            return false;
        }
        final DecimalMatrix other = (DecimalMatrix) object;
        return Objects.deepEquals(table, other.getTable());
    }

    /**
     * The builder for {@link DecimalMatrix DecimalMatrices}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    public static final class DecimalMatrixBuilder extends AbstractMatrixBuilder<BigDecimal, DecimalMatrix> {
        public DecimalMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        public DecimalMatrixBuilder put(final Integer rowIndex, final Integer columnIndex, final BigDecimal element) {
            requireNonNull(element, "element");
            requireNonNull(rowIndex, "rowIndex");
            requireNonNull(columnIndex, "columnIndex");
            checkArgument(table.rowKeySet().contains(rowIndex), "expected row index in [1, %s] but actual %s",
                    table.rowKeySet().size(), rowIndex);
            checkArgument(table.columnKeySet().contains(columnIndex), "expected column index in [1, %s] but actual %s",
                    table.columnKeySet().size(), columnIndex);
            table.put(rowIndex, columnIndex, element);
            return this;
        }

        public DecimalMatrixBuilder putAll(final BigDecimal element) {
            requireNonNull(element, "element");
            table.rowKeySet().forEach(rowKey -> {
                table.columnKeySet().forEach(columnKey -> {
                    table.put(rowKey, columnKey, element);
                });
            });
            return this;
        }

        /**
         * Returns the built {@link DecimalMatrix}
         *
         * @return The {@link DecimalMatrix}
         * @throws NullPointerException
         *             if one {@code element == null}
         * @since 1
         * @author Lars Tennstedt
         * @see ImmutableTable#copyOf
         */
        @Override
        public DecimalMatrix build() {
            table.cellSet().forEach(cell -> {
                requireNonNull(cell.getValue(), "cell.value");
            });
            return new DecimalMatrix(ImmutableTable.copyOf(table));
        }
    }
}
