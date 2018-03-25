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
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * An immutable implementation of a matrix which uses {@link BigDecimal} as type
 * for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class BigDecimalMatrix
    extends AbstractMatrix<BigDecimal, BigDecimalVector, BigDecimalMatrix, BigDecimal, BigDecimal> {
    private BigDecimalMatrix(final ImmutableTable<Integer, Integer, BigDecimal> table) {
        super(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix add(final BigDecimalMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final BigDecimalMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix subtract(final BigDecimalMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), subtrahend.columnSize());
        final BigDecimalMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().subtract(subtrahend.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix multiply(final BigDecimalMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
            "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(), factor.rowSize());
        final BigDecimalMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> factor.columns().forEach((columnIndex, column) -> {
            final BigDecimal element = multiplyRowWithColumn(row, column);
            builder.put(rowIndex, columnIndex, element);
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalVector multiplyVector(final BigDecimalVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
            "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final BigDecimalVector.BigDecimalVectorBuilder builder = BigDecimalVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> row.forEach((columnIndex, matrixEntry) -> {
            final BigDecimal oldEntry = builder.element(rowIndex) != null ? builder.element(rowIndex) : BigDecimal.ZERO;
            builder.put(rowIndex, oldEntry.add(matrixEntry.multiply(vector.element(columnIndex))));
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal multiplyRowWithColumn(final Map<Integer, BigDecimal> row,
        final Map<Integer, BigDecimal> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected rowSize == columnSize but actual %s != %s", row.size(),
            column.size());
        BigDecimal result = BigDecimal.ZERO;
        for (final Entry<Integer, BigDecimal> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey())));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix scalarMultiply(final BigDecimal scalar) {
        requireNonNull(scalar, "scalar");
        final BigDecimalMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet()
            .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix negate() {
        return scalarMultiply(BigDecimal.ONE.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal trace() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
            table.columnKeySet().size());
        return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
            .map(Cell::getValue).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal determinant() {
        final int rowSize = table.rowKeySet().size();
        checkState(square(), "expected square matrix but actual %s x %s", rowSize, table.columnKeySet().size());
        if (triangular()) {
            return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).reduce(BigDecimal::multiply).get();
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal leibnizFormula() {
        BigDecimal result = BigDecimal.ZERO;
        for (final List<Integer> permutation : Collections2.permutations(table.rowKeySet())) {
            BigDecimal product = BigDecimal.ONE;
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
            result = result.add(BigDecimal.ONE.negate().pow(inversions).multiply(product));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigDecimal ruleOfSarrus() {
        final BigDecimal first = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final BigDecimal second = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final BigDecimal third = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final BigDecimal fourth = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(1, 3));
        final BigDecimal fifth = table.get(3, 2).multiply(table.get(2, 3)).multiply(table.get(1, 1));
        final BigDecimal sixth = table.get(3, 3).multiply(table.get(2, 1)).multiply(table.get(1, 2));
        return first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix transpose() {
        final BigDecimalMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimalMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected rowIndex in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected columnIndex in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        final BigDecimalMatrixBuilder builder = builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            if (rowKey.compareTo(rowIndex) != 0 && columnKey.compareTo(columnIndex) != 0) {
                final Integer newRowIndex = rowKey.compareTo(rowIndex) > 0 ? rowKey - 1 : rowKey;
                final Integer newColumnIndex = columnKey.compareTo(columnIndex) > 0 ? columnKey - 1 : columnKey;
                builder.put(newRowIndex, newColumnIndex, cell.getValue());
            }
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal maxAbsColumnSumNorm() {
        return table.columnMap().values().asList().stream()
            .map(column -> column.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add)).map(Optional::get)
            .reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal maxAbsRowSumNorm() {
        return table.rowMap().values().asList().stream()
            .map(column -> column.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add)).map(Optional::get)
            .reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm(final SquareRootContext squareRootContext) {
        requireNonNull(squareRootContext, "squareRootContext");
        return SquareRootCalculator.sqrt(frobeniusNormPow2(), squareRootContext);
    }

    /**
     * Returns the square of the frobenius norm of this {@link BigIntegerMatrix}
     *
     * @return The square of the frobenius norm
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNormPow2() {
        return table.values().stream().map(element -> element.pow(2)).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal maxNorm() {
        return table.values().stream().map(BigDecimal::abs).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upperTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) > 0)
            .map(Cell::getValue).anyMatch(value -> value.compareTo(BigDecimal.ZERO) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean lowerTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) < 0)
            .map(Cell::getValue).anyMatch(value -> value.compareTo(BigDecimal.ZERO) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean identity() {
        return diagonal()
            && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).anyMatch(value -> value.compareTo(BigDecimal.ONE) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean invertible() {
        return square() && determinant().compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * Returns a {@link BigDecimalMatrixBuilder}
     *
     * @param rowSize
     *            the row size the resulting {@link BigDecimalMatrix}
     * @param columnSize
     *            the column size the resulting {@link BigDecimalMatrix}
     * @return A {@link BigDecimalMatrixBuilder}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1}
     * @since 1
     */
    public static BigDecimalMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return new BigDecimalMatrixBuilder(rowSize, columnSize);
    }

    /**
     * The builder for {@link BigDecimalMatrix BigDecimalMatrices}
     *
     * @since 1
     */
    @Beta
    public static final class BigDecimalMatrixBuilder
        extends AbstractMatrixBuilder<BigDecimal, BigDecimalMatrix, BigDecimalMatrixBuilder> {
        private BigDecimalMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BigDecimalMatrix build() {
            table.cellSet().forEach(cell -> requireNonNull(cell.getValue(), "cell.value"));
            return new BigDecimalMatrix(ImmutableTable.copyOf(table));
        }
    }
}
