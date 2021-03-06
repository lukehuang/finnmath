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

import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootCalculator;
import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.google.common.annotations.Beta;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * An immutable implementation of a matrix which uses {@link RealComplexNumber}
 * as type for its elements
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class RealComplexNumberMatrix extends AbstractContextMatrix<RealComplexNumber, RealComplexNumberVector,
    RealComplexNumberMatrix, BigDecimal, BigDecimal, SquareRootContext> {
    private RealComplexNumberMatrix(final ImmutableTable<Integer, Integer, RealComplexNumber> table) {
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code summand == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != summand.columnSize}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix add(final RealComplexNumberMatrix summand, final MathContext mathContext) {
        requireNonNull(summand, "summand");
        requireNonNull(mathContext, "mathContext");
        checkArgument(table.rowKeySet().size() == summand.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), summand.rowSize());
        checkArgument(table.columnKeySet().size() == summand.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), summand.columnSize());
        final RealComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey, cell.getValue().add(summand.element(rowKey, columnKey), mathContext));
        });
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code subtrahend == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != subtrahend.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != subtrahend.columnSize}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix subtract(final RealComplexNumberMatrix subtrahend) {
        requireNonNull(subtrahend, "subtrahend");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), subtrahend.columnSize());
        final RealComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
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
     *             if {@code subtrahend == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != subtrahend.rowSize}
     * @throws IllegalArgumentException
     *             if {@code columnSize != subtrahend.columnSize}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix subtract(final RealComplexNumberMatrix subtrahend, final MathContext mathContext) {
        requireNonNull(subtrahend, "subtrahend");
        requireNonNull(mathContext, "mathContext");
        checkArgument(table.rowKeySet().size() == subtrahend.rowSize(), "expected equal row sizes but actual %s != %s",
            table.rowKeySet().size(), subtrahend.rowSize());
        checkArgument(table.columnKeySet().size() == subtrahend.columnSize(),
            "expected equal column sizes but actual %s != %s", table.columnKeySet().size(), subtrahend.columnSize());
        final RealComplexNumberMatrixBuilder builder = builder(rowSize(), columnSize());
        table.cellSet().forEach(cell -> {
            final Integer rowKey = cell.getRowKey();
            final Integer columnKey = cell.getColumnKey();
            builder.put(rowKey, columnKey,
                cell.getValue().subtract(subtrahend.element(rowKey, columnKey), mathContext));
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
    public RealComplexNumberMatrix multiply(final RealComplexNumberMatrix factor) {
        requireNonNull(factor, "factor");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
            "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(), factor.rowSize());
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> factor.columns().forEach((columnIndex, column) -> {
            final RealComplexNumber element = multiplyRowWithColumn(row, column);
            builder.put(rowIndex, columnIndex, element);
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code factor == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != factor.rowSize}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix multiply(final RealComplexNumberMatrix factor, final MathContext mathContext) {
        requireNonNull(factor, "factor");
        requireNonNull(mathContext, "mathContext");
        checkArgument(table.columnKeySet().size() == factor.rowSize(),
            "expected columnSize == factor.rowSize but actual %s != %s", table.columnKeySet().size(), factor.rowSize());
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), factor.columnSize());
        table.rowMap().forEach((rowIndex, row) -> factor.columns().forEach((columnIndex, column) -> {
            final RealComplexNumber element = multiplyRowWithColumn(row, column);
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
    public RealComplexNumberVector multiplyVector(final RealComplexNumberVector vector) {
        requireNonNull(vector, "vector");
        checkArgument(table.columnKeySet().size() == vector.size(),
            "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final RealComplexNumberVector.RealComplexNumberVectorBuilder builder =
            RealComplexNumberVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> row.forEach((columnIndex, matrixEntry) -> {
            final RealComplexNumber oldEntry =
                builder.element(rowIndex) != null ? builder.element(rowIndex) : RealComplexNumber.ZERO;
            builder.put(rowIndex, oldEntry.add(matrixEntry.multiply(vector.element(columnIndex))));
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code vector == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code columnSize != vector.size}
     * @since 1
     */
    @Override
    public RealComplexNumberVector multiplyVector(final RealComplexNumberVector vector, final MathContext mathContext) {
        requireNonNull(vector, "vector");
        requireNonNull(mathContext, "mathContext");
        checkArgument(table.columnKeySet().size() == vector.size(),
            "expected columnSize == vectorSize but actual %s != %s", table.columnKeySet().size(), vector.size());
        final RealComplexNumberVector.RealComplexNumberVectorBuilder builder =
            RealComplexNumberVector.builder(table.rowKeySet().size());
        table.rowMap().forEach((rowIndex, row) -> row.forEach((columnIndex, matrixEntry) -> {
            final RealComplexNumber oldEntry =
                builder.element(rowIndex) != null ? builder.element(rowIndex) : RealComplexNumber.ZERO;
            builder.put(rowIndex,
                oldEntry.add(matrixEntry.multiply(vector.element(columnIndex), mathContext), mathContext));
        }));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix scalarMultiply(final RealComplexNumber scalar) {
        requireNonNull(scalar, "scalar");
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet()
            .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code scalar == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix scalarMultiply(final RealComplexNumber scalar, final MathContext mathContext) {
        requireNonNull(scalar, "scalar");
        requireNonNull(mathContext, "mathContext");
        final RealComplexNumberMatrixBuilder builder = builder(table.rowKeySet().size(), table.columnKeySet().size());
        table.cellSet().forEach(
            cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue(), mathContext)));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix negate() {
        return scalarMultiply(RealComplexNumber.ONE.negate());
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix negate(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return scalarMultiply(RealComplexNumber.ONE.negate(mathContext), mathContext);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException
     *             if this {@link RealComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public RealComplexNumber trace() {
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
            .map(Cell::getValue).reduce(RealComplexNumber::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalStateException
     *             if this {@link RealComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public RealComplexNumber trace(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
            .map(Cell::getValue).reduce((element, other) -> element.add(other, mathContext)).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException
     *             if this {@link RealComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public RealComplexNumber determinant() {
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        if (triangular()) {
            return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).reduce(RealComplexNumber::multiply).get();
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
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalStateException
     *             if this {@link RealComplexNumberMatrix} is not square
     * @since 1
     */
    @Override
    public RealComplexNumber determinant(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        checkState(square(), "expected square matrix but was a %sx%s matrix", table.rowKeySet().size(),
            table.columnKeySet().size());
        if (triangular()) {
            return table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).reduce((element, other) -> element.multiply(other, mathContext)).get();
        }
        final int rowSize = table.rowKeySet().size();
        if (rowSize > 3) {
            return leibnizFormula();
        }
        if (rowSize == 3) {
            return ruleOfSarrus();
        }

        // rowSize == 2
        return table.get(1, 1).multiply(table.get(2, 2), mathContext)
            .subtract(table.get(1, 2).multiply(table.get(2, 1), mathContext), mathContext);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public RealComplexNumberMatrix transpose() {
        final RealComplexNumberMatrixBuilder builder = builder(table.columnKeySet().size(), table.rowKeySet().size());
        table.cellSet().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
        return builder.build();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code rowIndex == null}
     * @throws NullPointerException
     *             if {@code columnIndex == null}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1 || columnSize < columnIndex}
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
     *
     * @since 1
     */
    @Override
    public BigDecimal maxAbsColumnSumNorm() {
        return table.columnMap().values().asList().stream()
            .map(column -> column.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add))
            .map(Optional::get).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @since 1
     */
    @Override
    public BigDecimal maxAbsColumnSumNorm(final SquareRootContext squareRootContext) {
        requireNonNull(squareRootContext, "squareRootContext");
        return table.columnMap().values().asList().stream()
            .map(column -> column.values().stream().map(element -> element.abs(squareRootContext))
                .reduce((element, other) -> element.add(other, squareRootContext.getMathContext())))
            .map(Optional::get).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigDecimal maxAbsRowSumNorm() {
        return table.rowMap().values().asList().stream()
            .map(row -> row.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add)).map(Optional::get)
            .reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @since 1
     */
    @Override
    public BigDecimal maxAbsRowSumNorm(final SquareRootContext squareRootContext) {
        requireNonNull(squareRootContext, "squareRootContext");
        return table.rowMap().values().asList().stream()
            .map(row -> row.values().stream().map(element -> element.abs(squareRootContext))
                .reduce((element, other) -> element.add(other, squareRootContext.getMathContext())))
            .map(Optional::get).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNormPow2() {
        return table.values().stream().map(RealComplexNumber::absPow2).reduce(BigDecimal::add).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    public BigDecimal frobeniusNormPow2(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return table.values().stream().map(element -> element.absPow2(mathContext))
            .reduce((element, other) -> element.add(other, mathContext)).get();
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
     *
     * @since 1
     */
    @Override
    public BigDecimal maxNorm() {
        return table.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @since 1
     */
    @Override
    public BigDecimal maxNorm(final SquareRootContext squareRootContext) {
        requireNonNull(squareRootContext, "squareRootContext");
        return table.values().stream().map(element -> element.abs(squareRootContext)).reduce(BigDecimal::max).get();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public boolean upperTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) > 0)
            .map(Cell::getValue).anyMatch(value -> !value.equals(RealComplexNumber.ZERO));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public boolean lowerTriangular() {
        return square() && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) < 0)
            .map(Cell::getValue).anyMatch(value -> !value.equals(RealComplexNumber.ZERO));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public boolean identity() {
        return diagonal()
            && !table.cellSet().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                .map(Cell::getValue).anyMatch(value -> !value.equals(RealComplexNumber.ONE));
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
    @Override
    public boolean invertible() {
        if (square()) {
            final RealComplexNumber determinant = determinant();
            return square()
                && (determinant.equals(RealComplexNumber.ONE.negate()) || determinant.equals(RealComplexNumber.ONE));
        }
        return false;
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code row == null}
     * @throws NullPointerException
     *             if {@code column == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code rowSize != columnSize}
     * @since 1
     */
    @Override
    protected RealComplexNumber multiplyRowWithColumn(final Map<Integer, RealComplexNumber> row,
        final Map<Integer, RealComplexNumber> column, final MathContext mathContext) {
        requireNonNull(row, "row");
        requireNonNull(column, "column");
        requireNonNull(mathContext, "mathContext");
        checkArgument(row.size() == column.size(), "expected rowSize == columnSize but actual %s != %s", row.size(),
            column.size());
        RealComplexNumber result = RealComplexNumber.ZERO;
        for (final Entry<Integer, RealComplexNumber> rowEntry : row.entrySet()) {
            result = result.add(rowEntry.getValue().multiply(column.get(rowEntry.getKey()), mathContext), mathContext);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
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

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    protected RealComplexNumber leibnizFormula(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
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
                product = product.multiply(table.get(sigma, i + 1), mathContext);
            }
            result = result.add(
                RealComplexNumber.ONE.negate(mathContext).pow(inversions, mathContext).multiply(product, mathContext),
                mathContext);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @since 1
     */
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
     * {@inheritDoc}
     *
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    @Override
    protected RealComplexNumber ruleOfSarrus(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        final RealComplexNumber first =
            table.get(1, 1).multiply(table.get(2, 2), mathContext).multiply(table.get(3, 3), mathContext);
        final RealComplexNumber second =
            table.get(1, 2).multiply(table.get(2, 3), mathContext).multiply(table.get(3, 1), mathContext);
        final RealComplexNumber third =
            table.get(1, 3).multiply(table.get(2, 1), mathContext).multiply(table.get(3, 2), mathContext);
        final RealComplexNumber fourth =
            table.get(3, 1).multiply(table.get(2, 2), mathContext).multiply(table.get(1, 3), mathContext);
        final RealComplexNumber fifth =
            table.get(3, 2).multiply(table.get(2, 3), mathContext).multiply(table.get(1, 1), mathContext);
        final RealComplexNumber sixth =
            table.get(3, 3).multiply(table.get(2, 1), mathContext).multiply(table.get(1, 2), mathContext);
        return first.add(second, mathContext).add(third, mathContext).subtract(fourth, mathContext)
            .subtract(fifth, mathContext).subtract(sixth, mathContext);
    }

    /**
     * Returns a {@link RealComplexNumberMatrixBuilder}
     *
     * @param rowSize
     *            ow size the resulting {@link RealComplexNumberMatrix}
     * @param columnSize
     *            column size the resulting {@link RealComplexNumberMatrix}
     * @return A {@link RealComplexNumberMatrixBuilder}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1}
     * @since 1
     */
    public static RealComplexNumberMatrixBuilder builder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        return new RealComplexNumberMatrixBuilder(rowSize, columnSize);
    }

    /**
     * {@link AbstractMatrixBuilder} for {@link RealComplexNumberMatrix
     * BigIntegerMatrices}
     *
     * @since 1
     */
    @Beta
    public static final class RealComplexNumberMatrixBuilder
        extends AbstractMatrixBuilder<RealComplexNumber, RealComplexNumberMatrix, RealComplexNumberMatrixBuilder> {
        private RealComplexNumberMatrixBuilder(final int rowSize, final int columnSize) {
            super(rowSize, columnSize);
        }

        /**
         * {@inheritDoc}
         *
         * @throws NullPointerException
         *             if one {@code cell.value == null}
         * @since 1
         */
        @Override
        public RealComplexNumberMatrix build() {
            table.cellSet().forEach(cell -> requireNonNull(cell.getValue(), "cell.value"));
            return new RealComplexNumberMatrix(ImmutableTable.copyOf(table));
        }
    }
}
