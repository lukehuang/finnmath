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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.linear.RealComplexNumberVector.RealComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.annotations.Beta;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * An immutable implementation of a matrix which uses {@link RealComplexNumber} as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class RealComplexNumberMatrix extends
        AbstractMatrix<RealComplexNumber, RealComplexNumberVector, RealComplexNumberMatrix, BigDecimal, BigDecimal> {
    private RealComplexNumberMatrix(final ImmutableTable<Integer, Integer, RealComplexNumber> table) {
        super(table);
    }

    /**
     * Returns the sum of this {@link RealComplexNumberMatrix} and the given one
     *
     * @param summand
     *         The summand
     * @return The sum
     * @throws NullPointerException
     *         if {@code summand == null}
     * @throws IllegalArgumentException
     *         if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *         if {@code columnSize != summand.columnSize}
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix add(final RealComplexNumberMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final RealComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the difference of this {@link RealComplexNumberMatrix} and the given one
     *
     * @param subtrahend
     *         the subtrahend
     * @return The difference
     * @throws NullPointerException
     *         if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *         if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *         if {@code columnSize != summand.columnSize}
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix subtract(final RealComplexNumberMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
                table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
                "expected equal column sizes but actual %s != %s", table.columnKeySet().size(),
                subtrahend.columnSize());
        final RealComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().subtract(subtrahend.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link RealComplexNumberMatrix} and the given one
     *
     * @param factor
     *         the factor
     * @return The product
     * @throws NullPointerException
     *         if {@code factor == null}
     * @throws IllegalArgumentException
     *         if {@code columnSize != factor.rowSize}
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix multiply(final RealComplexNumberMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
                "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(),
                factor.rowSize());
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> {
            factor.columns().forEach((columnIndex, column) -> {
                final RealComplexNumber element = multiplyRowWithColumn(row, column);
                builder.put(rowIndex, columnIndex, element);
            });
        });
        return builder.build();
    }

    /**
     * Returns the product of this {@link RealComplexNumberMatrix} and the given {@link RealComplexNumberVector}
     *
     * @param vector
     *         the vector
     * @return The product
     * @throws NullPointerException
     *         if {@code vector == null}
     * @throws IllegalArgumentException
     *         if {@code columnSize != vector.size}
     * @author Lars Tennstedt
     * @see RealComplexNumberVector#builder
     * @since 1
     */
    @Override
    public RealComplexNumberVector multiplyVector(final RealComplexNumberVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
                "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final RealComplexNumberVectorBuilder builder = RealComplexNumberVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> {
            row.forEach((columnIndex, matrixEntry) -> {
                final RealComplexNumber oldEntry =
                        builder.element(rowIndex) != null ? builder.element(rowIndex) : RealComplexNumber.ZERO;
                builder.put(rowIndex, oldEntry.add(matrixEntry.multiply(vector.element(columnIndex))));
            });
        });
        return builder.build();
    }

    @Override
    protected RealComplexNumber multiplyRowWithColumn(final Map<Integer, RealComplexNumber> row,
                                                      final Map<Integer, RealComplexNumber> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected rowSize == columnSize but actual %s != %s", row.size(),
                column.size());
        RealComplexNumber result = RealComplexNumber.ZERO;
        for (final Entry<Integer, RealComplexNumber> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey())));
        }
        return result;
    }

    /**
     * Returns the scalar product of this {@link RealComplexNumberMatrix} and the given {@link RealComplexNumber}
     *
     * @param scalar
     *         the scalar
     * @return The scalar product
     * @throws NullPointerException
     *         if {@code scalar == null}
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix scalarMultiply(final RealComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet().forEach(cell -> {
            builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
        });
        return builder.build();
    }

    /**
     * Returns the negated {@link RealComplexNumberMatrix} and this one
     *
     * @return The negated
     * @author Lars Tennstedt
     * @see #scalarMultiply
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix negate() {
        return scalarMultiply(RealComplexNumber.ONE.negate());
    }

    /**
     * Returns the trace of this {@link RealComplexNumberMatrix}
     *
     * @return The trace
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public RealComplexNumber trace() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
                table.columnKeySet().size());
        RealComplexNumber result = RealComplexNumber.ZERO;
        for (final Integer index : table.rowKeySet()) {
            result = result.add(table.get(index, index));
        }
        return result;
    }

    /**
     * Returns the determinant of this {@link RealComplexNumberMatrix}
     *
     * @return The determinant
     * @throws IllegalStateException
     *         if {@code !square}
     * @author Lars Tennstedt
     * @see #square
     * @see #minor
     * @since 1
     */
    @Override
    public RealComplexNumber determinant() {
        final int rowSize = table.rowKeySet().size();
        checkState(square(), "expected square matrix but actual %s x %s", rowSize, table.columnKeySet().size());
        if (triangular()) {
            RealComplexNumber result = RealComplexNumber.ONE;
            for (final Cell<Integer, Integer, RealComplexNumber> cell : table.cellSet()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    result = result.multiply(cell.getValue());
                }
            }
            return result;
        }
        if (rowSize > 3) {
            return leibnizFormula();
        }
        if (rowSize == 3) {
            return ruleOfSarrus();
        }

        // rowSize == 2
        return table.get(1, 1).multiply(table.get(2, 2)).subtract(table.get(1, 2).multiply(table.get(2, 1)));
    }

    @Override
    protected RealComplexNumber leibnizFormula() {
        RealComplexNumber result = RealComplexNumber.ZERO;
        for (final List<Integer> permutation : Collections2.permutations(table.rowKeySet())) {
            RealComplexNumber product = RealComplexNumber.ONE;
            int inversions = 0;
            final int size = table.rowKeySet().size();
            for (int i = 0; i < size; i++) {
                final Integer sigma = permutation.get(i);
                for (int j = i + 1; j < size; j++) {
                    if (sigma > permutation.get(j)) {
                        inversions++;
                    }
                }
                product = product.multiply(table.get(sigma, i + 1));
            }
            result = result.add(RealComplexNumber.ONE.negate().pow(inversions).multiply(product));
        }
        return result;
    }

    @Override
    protected RealComplexNumber ruleOfSarrus() {
        final RealComplexNumber first = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final RealComplexNumber second = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final RealComplexNumber third = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final RealComplexNumber fourth = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(1, 3));
        final RealComplexNumber fifth = table.get(3, 2).multiply(table.get(2, 3)).multiply(table.get(1, 1));
        final RealComplexNumber sixth = table.get(3, 3).multiply(table.get(2, 1)).multiply(table.get(1, 2));
        return first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
    }

    /**
     * Returns the transpose of this {@link RealComplexNumberMatrix}
     *
     * @return The transpose
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix transpose() {
        final RealComplexNumberMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> {
            builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
        });
        return builder.build();
    }

    /**
     * Returns the minor of this {@link RealComplexNumberMatrix} dependent on the given row and column index
     *
     * @param rowIndex
     *         the row index
     * @param columnIndex
     *         the column index
     * @return The minor
     * @throws NullPointerException
     *         if {@code rowIndex == null}
     * @throws NullPointerException
     *         if {@code columnIndex == null}
     * @throws IllegalArgumentException
     *         if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException
     *         if {@code columnIndex < 1 || columnSize < columnIndex}
     * @author Lars Tennstedt
     * @see #builder
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected rowIndex in [1, %s] but actual %s",
                table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected columnIndex in [1, %s] but actual %s",
                table.columnKeySet().size(), columnIndex);
        final RealComplexNumberMatrixBuilder builder =
                builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            if (!rowKey.equals(rowIndex) && !columnKey.equals(columnIndex)) {
                final Integer newRowIndex = rowKey.compareTo(rowIndex) > 0 ? rowKey - 1 : rowKey;
                final Integer newColumnIndex = columnKey.compareTo(columnIndex) > 0 ? columnKey - 1 : columnKey;
                builder.put(newRowIndex, newColumnIndex, cell.getValue());
            }
        });
        return builder.build();
    }

    /**
     * Returns the maximum absolute column sum norm of this {@link RealComplexNumberMatrix}
     *
     * @return The maximum absolute column sum norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public BigDecimal maxAbsColumnSumNorm() {
        BigDecimal norm = BigDecimal.ZERO;
        for (final Map<Integer, RealComplexNumber> column : table.columnMap().values().asList()) {
            BigDecimal sum = BigDecimal.ZERO;
            for (final RealComplexNumber element : column.values()) {
                sum = sum.add(element.abs());
            }
            norm = norm.max(sum);
        }
        return norm;
    }

    /**
     * Returns the maximum absolute row sum norm of this {@link RealComplexNumberMatrix}
     *
     * @return The maximum absolute row sum norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public BigDecimal maxAbsRowSumNorm() {
        BigDecimal norm = BigDecimal.ZERO;
        for (final Map<Integer, RealComplexNumber> row : table.rowMap().values().asList()) {
            BigDecimal sum = BigDecimal.ZERO;
            for (final RealComplexNumber element : row.values()) {
                sum = sum.add(element.abs());
            }
            norm = norm.max(sum);
        }
        return norm;
    }

    /**
     * Returns the square of the frobenius norm of this {@link RealComplexNumberMatrix}
     *
     * @return The square of the frobenius norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNormPow2() {
        BigDecimal normPow2 = BigDecimal.ZERO;
        for (final RealComplexNumber element : table.values()) {
            normPow2 = normPow2.add(element.absPow2());
        }
        return normPow2;
    }

    /**
     * Returns the frobenius norm of this {@link RealComplexNumberMatrix}
     *
     * @return The frobenius norm
     * @author Lars Tennstedt
     * @see #frobeniusNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm() {
        return new SquareRootCalculator().sqrt(frobeniusNormPow2());
    }

    /**
     * Returns the frobenius norm of this {@link RealComplexNumberMatrix}
     *
     * @param precision
     *         the precision for the termination condition
     * @return The frobenius norm
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @author Lars Tennstedt
     * @see #frobeniusNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        return new SquareRootCalculator(precision).sqrt(frobeniusNormPow2());
    }

    /**
     * Returns the frobenius norm of this {@link RealComplexNumberMatrix}
     *
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The frobenius norm
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @author Lars Tennstedt
     * @see #frobeniusNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm(final int scale, final RoundingMode roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(scale, roundingMode).sqrt(frobeniusNormPow2());
    }

    /**
     * Returns the frobenius norm of this {@link RealComplexNumberMatrix}
     *
     * @param precision
     *         the precision for the termination condition
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @return The frobenius norm
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @author Lars Tennstedt
     * @see #frobeniusNormPow2
     * @see SquareRootCalculator#sqrt(BigInteger)
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm(final BigDecimal precision, final int scale, final RoundingMode roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        return new SquareRootCalculator(precision, scale, roundingMode).sqrt(frobeniusNormPow2());
    }

    /**
     * Returns the maximum norm of this {@link RealComplexNumberMatrix}
     *
     * @return The maximum norm
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public BigDecimal maxNorm() {
        BigDecimal norm = BigDecimal.ZERO;
        for (final RealComplexNumber element : table.values()) {
            norm = norm.max(element.abs());
        }
        return norm;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumberMatrix} is upper triangular
     *
     * @return {@code true} if {@code this} is upper triangular, {@code false} otherwise
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public boolean upperTriangular() {
        if (square()) {
            for (final Cell<Integer, Integer, RealComplexNumber> cell : table.cellSet()) {
                if ((cell.getRowKey() > cell.getColumnKey()) && !cell.getValue().equals(RealComplexNumber.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumberMatrix} is lower triangular
     *
     * @return {@code true} if {@code this} is lower triangular, {@code false} otherwise
     * @author Lars Tennstedt
     * @since 1
     */
    @Override
    public boolean lowerTriangular() {
        if (square()) {
            for (final Cell<Integer, Integer, RealComplexNumber> cell : table.cellSet()) {
                if ((cell.getRowKey() < cell.getColumnKey()) && !cell.getValue().equals(RealComplexNumber.ZERO)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumberMatrix} is the identity one
     *
     * @return {@code true} if {@code this} is the identity matrix, {@code false} otherwise
     * @author Lars Tennstedt
     * @see #diagonal
     * @since 1
     */
    @Override
    public boolean identity() {
        if (diagonal()) {
            for (final Integer index : table.rowKeySet()) {
                if (!table.get(index, index).equals(RealComplexNumber.ONE)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link RealComplexNumberMatrix} is invertible
     *
     * @return {@code true} if {@code det == -1 || det == 1}, {@code false} otherwise
     * @author Lars Tennstedt
     * @see #determinant
     * @since 1
     */
    @Override
    public boolean invertible() {
        return determinant().equals(RealComplexNumber.ONE.negate()) || determinant().equals(RealComplexNumber.ONE);
    }

    /**
     * Returns a {@link RealComplexNumberMatrixBuilder}
     *
     * @param rowSize
     *         the row size the resulting {@link RealComplexNumberMatrix}
     * @param columnSize
     *         the column size the resulting {@link RealComplexNumberMatrix}
     * @return A {@link RealComplexNumberMatrixBuilder}
     * @author Lars Tennstedt
     * @since 1
     */
    public static RealComplexNumberMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return new RealComplexNumberMatrixBuilder(rowSize, columnSize);
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
        if (!(object instanceof RealComplexNumberMatrix)) {
            return false;
        }
        final RealComplexNumberMatrix other = (RealComplexNumberMatrix) object;
        return Objects.deepEquals(table, other.getTable());
    }

    /**
     * The builder for {@link RealComplexNumberMatrix BigIntMatrices}
     *
     * @author Lars Tennstedt
     * @since 1
     */
    @Beta
    public static final class RealComplexNumberMatrixBuilder
            extends AbstractMatrixBuilder<RealComplexNumber, RealComplexNumberMatrix> {
        private RealComplexNumberMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * Puts the given element on the {@link Table} dependent on the given row and column index
         *
         * @param rowIndex
         *         thr row index
         * @param columnIndex
         *         the column index
         * @param element
         *         the element
         * @return {@code this}
         */
        public RealComplexNumberMatrixBuilder put(final Integer rowIndex, final Integer columnIndex,
                                                  final RealComplexNumber element) {
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
         *         the element
         * @return {@code this}
         * @throws NullPointerException
         *         if {@code element == null}
         * @author Lars Tennstedt
         * @since 1
         */
        public RealComplexNumberMatrixBuilder putAll(final RealComplexNumber element) {
            requireNonNull(element, "element");
            table.rowKeySet().forEach(rowKey -> {
                table.columnKeySet().forEach(columnKey -> {
                    table.put(rowKey, columnKey, element);
                });
            });
            return this;
        }

        /**
         * Returns the built {@link RealComplexNumberMatrix}
         *
         * @return The {@link RealComplexNumberMatrix}
         * @throws NullPointerException
         *         if one {@code element == null}
         * @author Lars Tennstedt
         * @see ImmutableTable#copyOf
         * @since 1
         */
        @Override
        public RealComplexNumberMatrix build() {
            table.cellSet().forEach(cell -> {
                requireNonNull(cell.getValue(), "cell.value");
            });
            return new RealComplexNumberMatrix(ImmutableTable.copyOf(table));
        }
    }
}
