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

import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
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
 * An immutable implementation of a matrix which uses
 * {@link SimpleComplexNumber} as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SimpleComplexNumberMatrix extends
    AbstractMatrix<SimpleComplexNumber, SimpleComplexNumberVector, SimpleComplexNumberMatrix, BigDecimal, BigInteger> {
    private SimpleComplexNumberMatrix(final ImmutableTable<Integer, Integer, SimpleComplexNumber> table) {
        super(table);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != summand.columnSize}
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix add(final SimpleComplexNumberMatrix summand) {
        requireNonNull(summand, "summand");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final SimpleComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * * @throws NullPointerException if {@code subtrahend == null}
     *
     * @throws IllegalArgumentException
     *             if {@code rowSize != subtrahend.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != subtrahend.columnSize}
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix subtract(final SimpleComplexNumberMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), subtrahend.columnSize());
        final SimpleComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().subtract(subtrahend.element(rowKey, columnKey)));
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code factor == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != factor.rowSize}
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix multiply(final SimpleComplexNumberMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
            "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(), factor.rowSize());
        final SimpleComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> factor.columns().forEach((columnIndex, column) -> {
            final SimpleComplexNumber element = multiplyRowWithColumn(row, column);
            builder.put(rowIndex, columnIndex, element);
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != vector.size}
     * @since 1
     */
    @Override
    public SimpleComplexNumberVector multiplyVector(final SimpleComplexNumberVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
            "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final SimpleComplexNumberVectorBuilder builder = SimpleComplexNumberVector.builder(table.rowKeySet().size());
        table.rowMap().values().forEach(row -> {
            final SimpleComplexNumber element = table.columnKeySet().stream()
                .map(columnIndex -> row.get(columnIndex).multiply(vector.element(columnIndex)))
                .reduce(SimpleComplexNumber::add).get();
            builder.put(element);
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code row == null}
     * @throws NullPointerException
     *             if {@code column == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != columnSize}
     * @since 1
     */
    @Override
    protected SimpleComplexNumber multiplyRowWithColumn(final Map<Integer, SimpleComplexNumber> row,
        final Map<Integer, SimpleComplexNumber> column) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        checkArgument(row.size() == column.size(), "expected rowSize == columnSize but actual %s != %s", row.size(),
            column.size());
        SimpleComplexNumber result = SimpleComplexNumber.ZERO;
        for (final Entry<Integer, SimpleComplexNumber> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey())));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix scalarMultiply(final SimpleComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final SimpleComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet()
            .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix negate() {
        return scalarMultiply(SimpleComplexNumber.ONE.negate());
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException
     *             if this {@link SimpleComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public SimpleComplexNumber trace() {
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
            .map(Cell::getValue).reduce(SimpleComplexNumber::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException
     *             if this {@link SimpleComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public SimpleComplexNumber determinant() {
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        if (triangular()) {
            return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).reduce(SimpleComplexNumber::multiply).get();
        }
        final int rowSize = table.rowKeySet().size();
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
     *
     * @since 1
     */
    @Override
    protected SimpleComplexNumber leibnizFormula() {
        SimpleComplexNumber result = SimpleComplexNumber.ZERO;
        for (final List<Integer> permutation : Collections2.permutations(table.rowKeySet())) {
            SimpleComplexNumber product = SimpleComplexNumber.ONE;
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
            result = result.add(SimpleComplexNumber.ONE.negate().pow(inversions).multiply(product));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    protected SimpleComplexNumber ruleOfSarrus() {
        final SimpleComplexNumber first = table.get(1, 1).multiply(table.get(2, 2)).multiply(table.get(3, 3));
        final SimpleComplexNumber second = table.get(1, 2).multiply(table.get(2, 3)).multiply(table.get(3, 1));
        final SimpleComplexNumber third = table.get(1, 3).multiply(table.get(2, 1)).multiply(table.get(3, 2));
        final SimpleComplexNumber fourth = table.get(3, 1).multiply(table.get(2, 2)).multiply(table.get(1, 3));
        final SimpleComplexNumber fifth = table.get(3, 2).multiply(table.get(2, 3)).multiply(table.get(1, 1));
        final SimpleComplexNumber sixth = table.get(3, 3).multiply(table.get(2, 1)).multiply(table.get(1, 2));
        return first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public SimpleComplexNumberMatrix transpose() {
        final SimpleComplexNumberMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SimpleComplexNumberMatrix minor(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.containsRow(rowIndex), "expected rowIndex in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        checkArgument(table.containsColumn(columnIndex), "expected columnIndex in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        final SimpleComplexNumberMatrixBuilder builder =
            builder(table.rowKeySet().size() - 1, table.columnKeySet().size() - 1);
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
            .map(column -> column.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add))
            .map(Optional::get).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal maxAbsRowSumNorm() {
        return table.rowMap().values().asList().stream()
            .map(column -> column.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add))
            .map(Optional::get).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigInteger frobeniusNormPow2() {
        return table.values().stream().map(SimpleComplexNumber::absPow2).reduce(BigInteger::add).get();
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
    public BigDecimal maxNorm() {
        return table.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upperTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) > 0)
            .map(Cell::getValue).anyMatch(value -> !value.equals(SimpleComplexNumber.ZERO));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean lowerTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) < 0)
            .map(Cell::getValue).anyMatch(value -> !value.equals(SimpleComplexNumber.ZERO));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean identity() {
        return diagonal()
            && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).anyMatch(value -> !value.equals(SimpleComplexNumber.ONE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean invertible() {
        if (square()) {
            final SimpleComplexNumber determinant = determinant();
            return determinant.equals(SimpleComplexNumber.ONE) || determinant.equals(SimpleComplexNumber.ONE.negate())
                || determinant.equals(SimpleComplexNumber.IMAGINARY)
                || determinant.equals(SimpleComplexNumber.IMAGINARY.negate());
        }
        return false;
    }

    /**
     * Returns a {@link SimpleComplexNumberMatrixBuilder}
     *
     * @param rowSize
     *            the row size the resulting {@link SimpleComplexNumberMatrix}
     * @param columnSize
     *            the column size the resulting {@link SimpleComplexNumberMatrix}
     * @return A {@link SimpleComplexNumberMatrixBuilder}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1}
     * @since 1
     */
    public static SimpleComplexNumberMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return new SimpleComplexNumberMatrixBuilder(rowSize, columnSize);
    }

    /**
     * The builder for {@link SimpleComplexNumberMatrix BigIntegerMatrices}
     *
     * @since 1
     */
    @Beta
    public static final class SimpleComplexNumberMatrixBuilder extends
        AbstractMatrixBuilder<SimpleComplexNumber, SimpleComplexNumberMatrix, SimpleComplexNumberMatrixBuilder> {
        private SimpleComplexNumberMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SimpleComplexNumberMatrix build() {
            table.cellSet().forEach(cell -> requireNonNull(cell.getValue(), "cell.value"));
            return new SimpleComplexNumberMatrix(ImmutableTable.copyOf(table));
        }
    }
}
