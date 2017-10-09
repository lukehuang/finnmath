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
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import java.util.Map;
import java.util.Set;

/**
 * @since 1
 * @author Lars Tennstedt
 * @see ImmutableTable
 */
@Beta
abstract class AbstractMatrix<E, V, M> {
    /**
     * The table holding the elements of this {@link AbstractMatrix}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    protected final ImmutableTable<Integer, Integer, E> table;

    protected AbstractMatrix(final ImmutableTable<Integer, Integer, E> table) {
        this.table = table;
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

    protected abstract E ruleOfSarrus();

    protected abstract M transpose();

    protected abstract M minor(final Integer rowIndex, final Integer columnIndex);

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is a
     * square one
     *
     * @return {@code true} if {@code rowSize == columnSize}, {@code false}
     *         otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    public boolean square() {
        return table.rowKeySet().size() == table.columnKeySet().size();
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
     * triangular
     *
     * @return {@code true} if {@code upperTriangular || lowerTriangular},
     *         {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    public boolean triangular() {
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
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    public boolean diagonal() {
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
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #transpose
     */
    public boolean symmetric() {
        return square() && equals(transpose());
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link AbstractMatrix} is
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
    public boolean skewSymmetric() {
        return square() && transpose().equals(negate());
    }

    /**
     * Returns the row indices starting from {@code 1}
     *
     * @return The row indices
     * @since 1
     * @author Lars Tennstedt
     * @see Table#rowKeySet
     */
    public ImmutableSet<Integer> rowIndexes() {
        return table.rowKeySet();
    }

    /**
     * Returns the column indices starting from {@code 1}
     *
     * @return The column indices
     * @since 1
     * @author Lars Tennstedt
     * @see Table#columnKeySet
     */
    public ImmutableSet<Integer> columnIndexes() {
        return table.columnKeySet();
    }

    /**
     * Returns the matrix element dependent on the given row and column index
     *
     * @param rowIndex
     *            the row index
     * @param columnIndex
     *            the column index
     * @return The element
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
     * @see Table#get
     */
    public E element(final Integer rowIndex, final Integer columnIndex) {
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
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#cellSet
     */
    public ImmutableSet<Cell<Integer, Integer, E>> cells() {
        return table.cellSet();
    }

    /**
     * Returns the matrix row as {@link ImmutableMap} dependent on the given row
     * index
     *
     * @param rowIndex
     *            the row index
     * @return The row
     * @throws NullPointerException
     *             if {@code rowIndex == null}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1 || rowSize < rowIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see Table#row
     */
    public ImmutableMap<Integer, E> row(final Integer rowIndex) {
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
     * @return The column
     * @throws NullPointerException
     *             if {@code columnIndex == null}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see Table#column
     */
    public ImmutableMap<Integer, E> column(final Integer columnIndex) {
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.columnKeySet().contains(columnIndex), "expected column index in [1, %s] but actual %s",
                table.columnKeySet().size(), columnIndex);
        return table.column(columnIndex);
    }

    /**
     * Returns all matrix rows as {@link ImmutableMap}
     *
     * @return The rows
     * @since 1
     * @author Lars Tennstedt
     * @see Table#rowMap
     */
    public ImmutableMap<Integer, Map<Integer, E>> rows() {
        return table.rowMap();
    }

    /**
     * Returns all matrix columns as {@link ImmutableMap}
     *
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#columnMap
     */
    public ImmutableMap<Integer, Map<Integer, E>> columns() {
        return table.columnMap();
    }

    /**
     * Returns all matrix elements as {@link ImmutableCollection}
     *
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#values
     */
    public ImmutableCollection<E> elements() {
        return table.values();
    }

    /**
     * Returns the size of matrix
     *
     * @return The size
     * @since 1
     * @author Lars Tennstedt
     */
    public long size() {
        return Long.valueOf(table.rowKeySet().size()) * Long.valueOf(table.columnKeySet().size());
    }

    /**
     * Returns the row size of matrix
     *
     * @return The row size
     * @since 1
     * @author Lars Tennstedt
     * @see Set#size
     */
    public int rowSize() {
        return table.rowKeySet().size();
    }

    /**
     * Returns the column size of matrix
     *
     * @return The column size
     * @since 1
     * @author Lars Tennstedt
     * @see Set#size
     */
    public int columnSize() {
        return table.columnKeySet().size();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("table", table).toString();
    }

    public ImmutableTable<Integer, Integer, E> getTable() {
        return table;
    }
}
