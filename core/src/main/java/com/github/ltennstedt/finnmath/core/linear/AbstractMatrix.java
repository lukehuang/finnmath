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
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for matrices
 *
 * @param <E>
 *            type of the elements of the matrix
 * @param <V>
 *            type of the related vector
 * @param <M>
 *            type of the matrix
 * @param <N>
 *            type of the maximum absolute column sum norm, maximum absolute row
 *            sum norm and the maximum norm
 * @param <B>
 *            type of the square of the norms
 * @author Lars Tennstedt
 * @see ImmutableTable
 * @since 1
 */
@Beta
public abstract class AbstractMatrix<E, V extends AbstractVector<E, V, N, B>, M extends AbstractMatrix<E, V, M, N, B>,
    N, B> {
    /**
     * Default abort criterion
     *
     * @since 1
     */
    public static final BigDecimal DEFAULT_ABORT_CRITERION = BigDecimal.valueOf(0.0000000001);

    /**
     * Default rounding mode
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * {@link ImmutableTable} Table holding the elements of this
     * {@link AbstractMatrix}
     *
     * @since 1
     */
    protected final ImmutableTable<Integer, Integer, E> table;

    /**
     * Required arguments constructor
     *
     * @param table
     *            {@link ImmutableTable}
     * @throws NullPointerException
     *             if {@code table == null}
     * @since 1
     */
    protected AbstractMatrix(final ImmutableTable<Integer, Integer, E> table) {
        this.table = requireNonNull(table, "table");
    }

    protected abstract M add(M summand);

    protected abstract M subtract(M subtrahend);

    protected abstract M multiply(M factor);

    protected abstract V multiplyVector(V vector);

    protected abstract E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column);

    protected abstract M scalarMultiply(E scalar);

    protected abstract M negate();

    protected abstract E trace();

    protected abstract E determinant();

    protected abstract E leibnizFormula();

    protected abstract E ruleOfSarrus();

    protected abstract M transpose();

    protected abstract M minor(Integer rowIndex, Integer columnIndex);

    protected abstract N maxAbsColumnSumNorm();

    protected abstract N maxAbsRowSumNorm();

    protected abstract B frobeniusNormPow2();

    /**
     * Returns the frobenius norm of this {@link BigDecimalMatrix}
     *
     * @return Frobenius norm
     * @since 1
     */
    public final BigDecimal frobeniusNorm() {
        return frobeniusNorm(DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the frobenius norm of this {@link BigDecimalMatrix}
     *
     * @param abortCriterion
     *            abort criterion
     * @return Frobenius norm
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @since 1
     */
    public final BigDecimal frobeniusNorm(final BigDecimal abortCriterion) {
        requireNonNull(abortCriterion, "abortCriterion");
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return frobeniusNorm(abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the frobenius norm of this {@link BigDecimalMatrix}
     *
     * @param roundingMode
     *            {@link RoundingMode}
     * @return Frobenius norm
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @since 1
     */
    public final BigDecimal frobeniusNorm(final RoundingMode roundingMode) {
        requireNonNull(roundingMode, "roundingMode");
        return frobeniusNorm(DEFAULT_ABORT_CRITERION, roundingMode);
    }

    protected abstract BigDecimal frobeniusNorm(BigDecimal abortCriterion, RoundingMode roundingMode);

    /**
     * Returns the frobenius norm of this {@link BigDecimalMatrix}
     *
     * @param mathContext
     *            {@link MathContext}
     * @return Frobenius norm
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public final BigDecimal frobeniusNorm(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return frobeniusNorm(DEFAULT_ABORT_CRITERION, mathContext);
    }

    protected abstract BigDecimal frobeniusNorm(BigDecimal abortCriterion, MathContext mathContext);

    protected abstract N maxNorm();

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is a
     * square one
     *
     * @return {@code true} if {@code rowSize == columnSize}, {@code false}
     *         otherwise
     * @since 1
     */
    public final boolean square() {
        return table.rowKeySet().size() == table.columnKeySet().size();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
     * triangular
     *
     * @return {@code true} if {@code upperTriangular || lowerTriangular},
     *         {@code false} otherwise
     * @see #upperTriangular
     * @see #lowerTriangular
     * @since 1
     */
    public final boolean triangular() {
        return upperTriangular() || lowerTriangular();
    }

    protected abstract boolean upperTriangular();

    protected abstract boolean lowerTriangular();

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
     * diagonal
     *
     * @return {@code true} if {@code upperTriangular && lowerTriangular},
     *         {@code false} otherwise
     * @see #upperTriangular
     * @see #lowerTriangular
     * @since 1
     */
    public final boolean diagonal() {
        return upperTriangular() && lowerTriangular();
    }

    protected abstract boolean identity();

    protected abstract boolean invertible();

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
     * symmetric
     *
     * @return {@code true} if {@code square && equals(transpose)}, {@code false}
     *         otherwise
     * @see #square
     * @see #transpose
     * @since 1
     */
    public final boolean symmetric() {
        return square() && equals(transpose());
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
     * skew symmetric
     *
     * @return {@code true} if {@code square && equals(transpose.negate)},
     *         {@code false} otherwise
     * @see #square
     * @see #transpose
     * @see #negate
     * @since 1
     */
    public final boolean skewSymmetric() {
        return square() && transpose().equals(negate());
    }

    /**
     * Returns the row indices starting from {@code 1}
     *
     * @return Row indices
     * @since 1
     */
    public final ImmutableSet<Integer> rowIndexes() {
        return table.rowKeySet();
    }

    /**
     * Returns the column indices starting from {@code 1}
     *
     * @return Column indices
     * @since 1
     */
    public final ImmutableSet<Integer> columnIndexes() {
        return table.columnKeySet();
    }

    /**
     * Returns the matrix element dependent on the given row and column index
     *
     * @param rowIndex
     *            the row index
     * @param columnIndex
     *            the column index
     * @return Element
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
    public final E element(final Integer rowIndex, final Integer columnIndex) {
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.rowKeySet().contains(rowIndex), "expected row index in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        checkArgument(table.columnKeySet().contains(columnIndex), "expected column index in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        return table.get(rowIndex, columnIndex);
    }

    /**
     * Returns all matrix cells as {@link ImmutableSet}
     *
     * @return {@link Cell Cells}
     * @since 1
     */
    public final ImmutableSet<Cell<Integer, Integer, E>> cells() {
        return table.cellSet();
    }

    /**
     * Returns the matrix row as {@link ImmutableMap} dependent on the given row
     * index
     *
     * @param rowIndex
     *            the row index
     * @return Row
     * @throws NullPointerException
     *             if {@code rowIndex == null}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1 || rowSize < rowIndex}
     * @since 1
     */
    public final ImmutableMap<Integer, E> row(final Integer rowIndex) {
        requireNonNull(rowIndex, "rowIndex");
        checkArgument(table.rowKeySet().contains(rowIndex), "expected row index in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        return table.row(rowIndex);
    }

    /**
     * Returns the matrix column as {@link ImmutableMap} dependent on the given
     * column index
     *
     * @param columnIndex
     *            the column index
     * @return Column
     * @throws NullPointerException
     *             if {@code columnIndex == null}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     */
    public final ImmutableMap<Integer, E> column(final Integer columnIndex) {
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.columnKeySet().contains(columnIndex), "expected column index in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        return table.column(columnIndex);
    }

    /**
     * Returns all matrix rows as {@link ImmutableMap}
     *
     * @return Rows
     * @since 1
     */
    public final ImmutableMap<Integer, Map<Integer, E>> rows() {
        return table.rowMap();
    }

    /**
     * Returns all matrix columns as {@link ImmutableMap}
     *
     * @return Columns
     * @since 1
     */
    public final ImmutableMap<Integer, Map<Integer, E>> columns() {
        return table.columnMap();
    }

    /**
     * Returns all matrix elements as {@link ImmutableCollection}
     *
     * @return Elements
     * @since 1
     */
    public final ImmutableCollection<E> elements() {
        return table.values();
    }

    /**
     * Returns the size of this {@link AbstractMatrix}
     *
     * @return Size
     * @since 1
     */
    public final long size() {
        return Long.valueOf(table.rowKeySet().size()) * Long.valueOf(table.columnKeySet().size());
    }

    /**
     * Returns the row size of this {@link AbstractMatrix}
     *
     * @return Row size
     * @since 1
     */
    public final int rowSize() {
        return table.rowKeySet().size();
    }

    /**
     * Returns the column size of this {@link AbstractMatrix}
     *
     * @return Column size
     * @since 1
     */
    public final int columnSize() {
        return table.columnKeySet().size();
    }

    @Override
    public final int hashCode() {
        return Objects.hash(table);
    }

    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractMatrix)) {
            return false;
        }
        final AbstractMatrix<?, ?, ?, ?, ?> other = (AbstractMatrix<?, ?, ?, ?, ?>) object;
        return Objects.equals(table, other.getTable());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("table", table).toString();
    }

    public final ImmutableTable<Integer, Integer, E> getTable() {
        return table;
    }
}
