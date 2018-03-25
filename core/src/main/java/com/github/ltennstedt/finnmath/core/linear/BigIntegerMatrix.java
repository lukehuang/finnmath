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

import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector.BigIntegerVectorBuilder;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * An immutable implementation of a matrix which uses {@link BigInteger} as type
 * for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class BigIntegerMatrix
    extends AbstractMatrix<BigInteger, BigIntegerVector, BigIntegerMatrix, BigInteger, BigInteger> {
    private BigIntegerMatrix(final ImmutableTable<Integer, Integer, BigInteger> table) {
        super(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerMatrix add(final BigIntegerMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final BigIntegerMatrixBuilder builder = builder(rowSize(), columnSize());
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
    public BigIntegerMatrix subtract(final BigIntegerMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), subtrahend.columnSize());
        final BigIntegerMatrixBuilder builder = builder(rowSize(), columnSize());
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
    public BigIntegerMatrix multiply(final BigIntegerMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
            "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(), factor.rowSize());
        final BigIntegerMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> factor.columns().forEach((columnIndex, column) -> {
            final BigInteger element = multiplyRowWithColumn(row, column);
            builder.put(rowIndex, columnIndex, element);
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerVector multiplyVector(final BigIntegerVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
            "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final BigIntegerVectorBuilder builder = BigIntegerVector.builder(table.rowKeySet().size());
        table.rowMap().values().forEach(row -> {
            final BigInteger element = table.columnKeySet().stream()
                .map(columnIndex -> row.get(columnIndex).multiply(vector.element(columnIndex))).reduce(BigInteger::add)
                .get();
            builder.put(element);
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     */
    @Override
    public BigIntegerMatrix scalarMultiply(final BigInteger scalar) {
        requireNonNull(scalar, "scalar");
        final BigIntegerMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet()
            .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerMatrix negate() {
        return scalarMultiply(BigInteger.ONE.negate());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger trace() {
        checkState(square(), "expected square matrix but actual %s x %s", table.rowKeySet().size(),
            table.columnKeySet().size());
        return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
            .map(Cell::getValue).reduce(BigInteger::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger determinant() {
        final int rowSize = table.rowKeySet().size();
        checkState(square(), "expected square matrix but actual %s x %s", rowSize, table.columnKeySet().size());
        if (triangular()) {
            return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).reduce(BigInteger::multiply).get();
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
    protected BigInteger leibnizFormula() {
        BigInteger result = BigInteger.ZERO;
        for (final List<Integer> permutation : Collections2.permutations(table.rowKeySet())) {
            BigInteger product = BigInteger.ONE;
            int inversions = 0;
            final int size = table.rowKeySet().size();
            for (int i = 0; i < size; i++) {
                final Integer sigma = permutation.get(i);
                for (int j = i + 1; j < size; j++) {
                    if (sigma.compareTo(permutation.get(j)) > 0) {
                        inversions++;
                    }
                }
                product = product.multiply(table.get(sigma, i + 1));
            }
            result = result.add(BigInteger.ONE.negate().pow(inversions).multiply(product));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BigInteger ruleOfSarrus() {
        final BigInteger first = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final BigInteger second = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final BigInteger third = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final BigInteger fourth = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(1, 3));
        final BigInteger fifth = table.get(3, 2).multiply(table.get(2, 3)).multiply(table.get(1, 1));
        final BigInteger sixth = table.get(3, 3).multiply(table.get(2, 1)).multiply(table.get(1, 2));
        return first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerMatrix transpose() {
        final BigIntegerMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigIntegerMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected rowIndex in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected columnIndex in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        final BigIntegerMatrixBuilder builder = builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
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
    public BigInteger maxAbsColumnSumNorm() {
        return table.columnMap().values().asList().stream()
            .map(column -> column.values().stream().map(BigInteger::abs).reduce(BigInteger::add)).map(Optional::get)
            .reduce(BigInteger::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger maxAbsRowSumNorm() {
        return table.rowMap().values().asList().stream()
            .map(row -> row.values().stream().map(BigInteger::abs).reduce(BigInteger::add)).map(Optional::get)
            .reduce(BigInteger::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNorm(final SquareRootContext squareRootContext) {
        return SquareRootCalculator.sqrt(frobeniusNormPow2(), squareRootContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger frobeniusNormPow2() {
        return table.values().stream().map(element -> element.pow(2)).reduce(BigInteger::add).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger maxNorm() {
        return table.values().stream().map(BigInteger::abs).reduce(BigInteger::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upperTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) > 0)
            .map(Cell::getValue).anyMatch(it -> it.compareTo(BigInteger.ZERO) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean lowerTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) < 0)
            .map(Cell::getValue).anyMatch(value -> value.compareTo(BigInteger.ZERO) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean identity() {
        return diagonal()
            && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).anyMatch(value -> value.compareTo(BigInteger.ONE) != 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean invertible() {
        if (square()) {
            final BigInteger determinant = determinant();
            return determinant.compareTo(BigInteger.ONE.negate()) == 0 || determinant.compareTo(BigInteger.ONE) == 0;
        }
        return false;
    }

    /**
     * Returns a {@link BigIntegerMatrixBuilder}
     *
     * @param rowSize
     *            the row size the resulting {@link BigIntegerMatrix}
     * @param columnSize
     *            the column size the resulting {@link BigIntegerMatrix}
     * @return A {@link BigIntegerMatrixBuilder}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1}
     * @since 1
     */
    public static BigIntegerMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return new BigIntegerMatrixBuilder(rowSize, columnSize);
    }

    /**
     * The builder for {@link BigIntegerMatrix BigIntegerMatrices}
     *
     * @since 1
     */
    @Beta
    public static final class BigIntegerMatrixBuilder
        extends AbstractMatrixBuilder<BigInteger, BigIntegerMatrix, BigIntegerMatrixBuilder> {
        private BigIntegerMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BigIntegerMatrix build() {
            table.cellSet().forEach(cell -> requireNonNull(cell.getValue(), "cell.value"));
            return new BigIntegerMatrix(ImmutableTable.copyOf(table));
        }
    }
}
