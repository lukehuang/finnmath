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

package finnmath.linear

import com.google.common.annotations.Beta
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

/**
 * @since 1
 * @author Lars Tennstedt
 * @see ImmutableTable
 */
@Beta
@EqualsHashCode
@ToString
abstract class Matrix<E, V, M> {
    /**
     * The table holding the entries of this {@link Matrix}
     * 
     * @since 1
     * @author Lars Tennstedt
     */
    @Accessors
    protected val ImmutableTable<Integer, Integer, E> table

    protected new(ImmutableTable<Integer, Integer, E> table) {
        this.table = table
    }

    /**
     * Returns the row indices starting from {@code 1}
     * 
     * @return The row indices
     * @since 1
     * @author Lars Tennstedt
     * @see Table#rowKeySet
     */
    def rowIndexes() {
        table.rowKeySet
    }

    /**
     * Returns the column indices starting from {@code 1}
     * 
     * @return The column indices
     * @since 1
     * @author Lars Tennstedt
     * @see Table#columnKeySet
     */
    def columnIndexes() {
        table.columnKeySet
    }

    /**
     * Returns the matrix entry dependent on the given row and column index
     * 
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return The entry
     * @throws NullPointerException if {@code rowIndex == null}
     * @throws NullPointerException if {@code columnIndex == null}
     * @throws IllegalArgumentException if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see Table#get
     */
    def entry(Integer rowIndex, Integer columnIndex) {
        requireNonNull(rowIndex, 'rowIndex')
        requireNonNull(columnIndex, 'columnIndex')
        checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [1, %s] but actual %s',
            table.rowKeySet.size, rowIndex)
        checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [1, %s] but actual %s',
            table.columnKeySet.size, columnIndex)
        table.get(rowIndex, columnIndex)
    }

    /**
     * Returns the matrix row as {@link ImmutableMap} dependent on the given row index
     * 
     * @param rowIndex the row index
     * @return The row
     * @throws NullPointerException if {@code rowIndex == null}
     * @throws IllegalArgumentException if {@code rowIndex < 1 || rowSize < rowIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see Table#row
     */
    def row(Integer rowIndex) {
        requireNonNull(rowIndex, 'rowIndex')
        checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [1, %s] but actual %s',
            table.rowKeySet.size, rowIndex)
        table.row(rowIndex)
    }

    /**
     * Returns the matrix column as {@link ImmutableMap} dependent on the given column index
     * 
     * @param columnIndex the column index
     * @return The column
     * @throws NullPointerException if {@code columnIndex == null}
     * @throws IllegalArgumentException if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see Table#column
     */
    def column(Integer columnIndex) {
        checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [1, %s] but actual %s',
            table.columnKeySet.size, columnIndex)
        table.column(columnIndex)
    }

    /**
     * Returns all matrix rows as {@link ImmutableMap}
     * 
     * @return The rows
     * @since 1
     * @author Lars Tennstedt
     * @see Table#rowMap
     */
    def rows() {
        table.rowMap
    }

    /**
     * Returns all matrix columns as {@link ImmutableMap}
     * 
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#columnMap
     */
    def columns() {
        table.columnMap
    }

    /**
     * Returns all matrix cells as {@link ImmutableSet}
     * 
     * @return The columns
     * @since 1
     * @author Lars Tennstedt
     * @see Table#cellSet
     */
    def cells() {
        table.cellSet
    }

    /**
     * Returns the size of matrix
     * 
     * @return The size
     * @since 1
     * @author Lars Tennstedt
     */
    def size() {
        Long::valueOf(rowSize) * Long::valueOf(columnSize)
    }

    /**
     * Returns the row size of matrix
     * 
     * @return The row size
     * @since 1
     * @author Lars Tennstedt
     * @see Set#size
     */
    def rowSize() {
        table.rowKeySet.size
    }

    /**
     * Returns the column size of matrix
     * 
     * @return The column size
     * @since 1
     * @author Lars Tennstedt
     * @see Set#size
     */
    def columnSize() {
        table.columnKeySet.size
    }

    def M add(M summand)

    def M subtract(M subtrahend)

    def M multiply(M factor)

    def V multiplyVector(V vector)

    protected def E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column)

    def M scalarMultiply(E scalar)

    def M negate()

    def E tr()

    def E det()

    def boolean square()

    def boolean triangular()

    def boolean upperTriangular()

    def boolean lowerTriangular()

    def boolean diagonal()

    def boolean id()

    def boolean invertible()

    def M transpose()

    def boolean symmetric()

    def boolean skewSymmetric()

    def M minor(Integer rowIndex, Integer columnIndex)
}
