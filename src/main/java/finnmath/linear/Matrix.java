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
abstract class Matrix<E, V, M> {
    /**
     * The table holding the entries of this {@link Matrix}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    protected final ImmutableTable<Integer, Integer, E> table;

    protected Matrix(final ImmutableTable<Integer, Integer, E> table) {
        this.table = table;
    }

    abstract M add(M summand);

    abstract M subtract(M subtrahend);

    abstract M multiply(M factor);

    abstract V multiplyVector(V vector);

    abstract E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column);

    abstract M scalarMultiply(E scalar);

    abstract M negate();

    abstract E trace();

    abstract E det();

    abstract M transpose();

    abstract M minor(final Integer rowIndex, final Integer columnIndex);

    abstract boolean square();

    abstract boolean triangular();

    abstract boolean upperTriangular();

    abstract boolean lowerTriangular();

    abstract boolean diagonal();

    abstract boolean id();

    abstract boolean invertible();

    abstract boolean symmetric();

    abstract boolean skewSymmetric();

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
     * Returns the matrix entry dependent on the given row and column index
     *
     * @param rowIndex
     *            the row index
     * @param columnIndex
     *            the column index
     * @return The entry
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
    public E entry(final Integer rowIndex, final Integer columnIndex) {
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
     * Returns all matrix entries as {@link ImmutableCollection}
     *
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#values
     */
    public ImmutableCollection<E> entries() {
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
