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
import com.google.common.collect.ImmutableTable
import java.math.BigInteger
import java.util.Map
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a matrix which is based on {@link ImmutableTable} and uses {@link BigInteger} as 
 * type for its entries
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@EqualsHashCode
final class BigIntMatrix extends Matrix<BigInteger, BigIntVector, BigIntMatrix> {
    private new(ImmutableTable<Integer, Integer, BigInteger> table) {
        super(table)
    }

    /**
     * Returns the sum of this {@link BigIntMatrix} and the given one
     * 
     * @param summand The summand
     * @return The sum
     * @throws NullPointerException if {@code summand == null}
     * @throws IllegalArgumentException if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException if {@code columnSize != summand.columnSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override add(BigIntMatrix summand) {
        requireNonNull(summand, 'summand')
        checkArgument(table.rowKeySet.size == summand.rowSize, 'expected equal row sizes but actual %s != %s',
            table.rowKeySet.size, summand.rowSize)
        checkArgument(table.columnKeySet.size == summand.columnSize, 'expected column sizes equal but actual %s != %s',
            table.columnKeySet.size, summand.columnSize)
        val builder = builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value + summand.entry(rowKey, columnKey))
        ]
        builder.build
    }

    /**
     * Returns the difference of this {@link BigIntMatrix} and the given one
     * 
     * @param subtrahend the subtrahend
     * @return The difference
     * @throws NullPointerException if {@code subtrahend == null}
     * @throws IllegalArgumentException if {@code rowSize != summand.rowSize}
     * @throws IllegalArgumentException if {@code columnSize != summand.columnSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override subtract(BigIntMatrix subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        checkArgument(table.rowKeySet.size == subtrahend.rowSize, 'expected equal row sizes but actual %s != %s',
            table.rowKeySet.size, subtrahend.rowSize)
        checkArgument(table.columnKeySet.size == subtrahend.columnSize,
            'expected equal column sizes but actual %s != %s', table.columnKeySet.size, subtrahend.columnSize)
        val builder = builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value - subtrahend.entry(rowKey, columnKey))
        ]
        builder.build
    }

    /**
     * Returns the product of this {@link BigIntMatrix} and the given one
     * 
     * @param factor the factor
     * @return The product
     * @throws NullPointerException if {@code factor == null}
     * @throws IllegalArgumentException if {@code columnSize != factor.rowSize}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override multiply(BigIntMatrix factor) {
        requireNonNull(factor, 'factor')
        checkArgument(table.columnKeySet.size == factor.rowSize,
            'expected columnSize == factor.rowSize but actual %s != %s', table.columnKeySet.size, factor.rowSize)
        val builder = builder(table.rowKeySet.size, factor.columnSize)
        table.rowKeySet.forEach [ rowIndex |
            factor.columnIndexes.forEach [ columnIndex |
                val entry = multiplyRowWithColumn(table.row(rowIndex), factor.column(columnIndex))
                builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    /**
     * Returns the product of this {@link BigIntMatrix} and the given {@link BigIntVector}
     * 
     * @param vector the vector
     * @return The product
     * @throws NullPointerException if {@code vector == null}
     * @throws IllegalArgumentException if {@code columnSize != vector.size}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntVector#builder
     */
    override multiplyVector(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        checkArgument(table.columnKeySet.size == vector.size, 'expected columnSize == vectorSize but actual %s != %s',
            table.columnKeySet.size, vector.size)
        val builder = BigIntVector::builder(table.rowKeySet.size)
        table.rowMap.forEach [ rowIndex, row |
            row.forEach [ columnIndex, matrixEntry |
                val oldEntry = builder.entry(rowIndex) ?: 0BI
                builder.put(rowIndex, oldEntry + matrixEntry * vector.entry(columnIndex))
            ]
        ]
        builder.build
    }

    protected override multiplyRowWithColumn(Map<Integer, BigInteger> row, Map<Integer, BigInteger> column) {
        requireNonNull(row, 'row')
        requireNonNull(column, 'column')
        checkArgument(row.size == column.size, 'expected row size == column size but actual %s != %s', row.size,
            column.size)
        var result = 0BI
        for (it : row.entrySet)
            result += value * column.get(key)
        result
    }

    /**
     * Returns the scalar product of this {@link BigIntMatrix} and the given {@link BigInteger}
     * 
     * @param scalar the scalar
     * @return The scalar product
     * @throws NullPointerException if {@code scalar == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override scalarMultiply(BigInteger scalar) {
        requireNonNull(scalar, 'scalar')
        val builder = builder(table.rowKeySet.size, table.columnKeySet.size)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, scalar * value)
        ]
        builder.build
    }

    /**
     * Returns the negated {@link BigIntMatrix} and this one
     * 
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #scalarMultiply
     */
    override negate() {
        scalarMultiply(-1BI)
    }

    /**
     * Returns the trace of this {@link BigIntMatrix}
     * 
     * @return The trace
     * @throws IllegalStateException if {@code !square}
     * @since 1
     * @author Lars Tennstedt
     */
    override tr() {
        checkState(square, 'expected square matrix but actual %s x %s', table.rowKeySet.size, table.columnKeySet.size)
        var result = 0BI
        for (index : table.rowKeySet)
            result += table.get(index, index)
        result
    }

    /**
     * Returns the determinant of this {@link BigIntMatrix}
     * 
     * @return The determinant
     * @throws IllegalStateException if {@code !square}
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #minor
     */
    override det() {
        checkState(square, 'expected square matrix but actual %s x %s', table.rowKeySet.size, table.columnKeySet.size)
        if (table.rowKeySet.size > 1) {
            var result = 0BI
            for (it : table.row(1).entrySet)
                result += (-1BI) ** (key + 1) * value * minor(1, key).det
            return result
        }
        table.get(1, 1)
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is a square one
     * 
     * @return {@code true} if {@code rowSize == columnSize}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override square() {
        table.rowKeySet.size === table.columnKeySet.size
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is triangular
     * 
     * @return {@code true} if {@code upperTriangular || lowerTriangular}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    override triangular() {
        upperTriangular || lowerTriangular
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is upper triangular
     * 
     * @return {@code true} if {@code this} is upper triangular, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override upperTriangular() {
        if (square) {
            for (it : table.cellSet)
                if (rowKey > columnKey && value != 0BI)
                    return false
            return true
        }
        false
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is lower triangular
     * 
     * @return {@code true} if {@code this} is lower triangular, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override lowerTriangular() {
        if (square) {
            for (it : table.cellSet)
                if (rowKey < columnKey && value != 0BI)
                    return false
            return true
        }
        false
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is diagonal
     * 
     * @return {@code true} if {@code upperTriangular && lowerTriangular}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #upperTriangular
     * @see #lowerTriangular
     */
    override diagonal() {
        upperTriangular && lowerTriangular
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is the identity one
     * 
     * @return {@code true} if {@code this} is the identity matrix, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #diagonal
     */
    override id() {
        if (diagonal) {
            for (index : table.rowKeySet)
                if (table.get(index, index) != 1BI)
                    return false
            return true
        }
        false
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is invertible
     * 
     * @return {@code true} if {@code det == -1 || det == 1}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #det
     */
    override invertible() {
        det == -1BI || det == 1BI
    }

    /**
     * Returns the transpose of this {@link BigIntMatrix}
     * 
     * @return The transpose
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override transpose() {
        val builder = builder(table.columnKeySet.size, table.rowKeySet.size)
        table.cellSet.forEach [
            builder.put(columnKey, rowKey, value)
        ]
        builder.build
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is symmetric
     * 
     * @return {@code true} if {@code square && equals(transpose)}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #transpose
     */
    override symmetric() {
        square && this == transpose
    }

    /**
     * Returns a {@code boolean} which indicates if this {@link BigIntMatrix} is skew symmetric
     * 
     * @return {@code true} if {@code square && equals(transpose.negate)}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #square
     * @see #transpose
     * @see #negate
     */
    override skewSymmetric() {
        square && transpose == negate
    }

    /**
     * Returns the minor of this {@link BigIntMatrix} dependent on the given row and column index
     * 
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return The minor
     * @throws NullPointerException if {@code rowIndex == null}
     * @throws NullPointerException if {@code columnIndex == null}
     * @throws IllegalArgumentException if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     * @author Lars Tennstedt
     * @see #builder
     */
    override minor(Integer rowIndex, Integer columnIndex) {
        requireNonNull(rowIndex, 'rowIndex')
        requireNonNull(columnIndex, 'columnIndex')
        checkArgument(table.containsRow(rowIndex), 'expected row index in [1, %s] but actual %s', table.rowKeySet.size,
            rowIndex)
        checkArgument(table.containsColumn(columnIndex), 'expected column index in [1, %s] but actual %s',
            table.columnKeySet.size, columnIndex)
        val builder = builder(table.rowKeySet.size - 1, table.columnKeySet.size - 1)
        table.cellSet.forEach [
            if (rowKey != rowIndex && columnKey != columnIndex) {
                val newRowIndex = if (rowKey > rowIndex) rowKey - 1 else rowKey
                val newColumnIndex = if (columnKey > columnIndex) columnKey - 1 else columnKey
                builder.put(newRowIndex, newColumnIndex, value)
            }
        ]
        builder.build
    }

    /**
     * Returns a {@link BigIntMatrixBuilder}
     * 
     * @param rowSize the row size the resulting {@link BigIntMatrix}
     * @param columnSize the column size the resulting {@link BigIntMatrix}
     * @return A {@link BigIntMatrixBuilder}
     * @since 1
     * @author Lars Tennstedt
     */
    static def builder(int rowSize, int columnSize) {
        checkArgument(rowSize > 0, 'expected row size > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected column size > 0 but actual %s', columnSize)
        new BigIntMatrixBuilder(rowSize, columnSize)
    }

    /**
     * The builder for {@link BigIntMatrix BigIntMatrices}
     * 
     * @since 1
     * @author Lars Tennstedt
     */
    @Beta
    static final class BigIntMatrixBuilder extends MatrixBuilder<BigInteger, BigIntMatrix> {
        private new(int rowSize, int columnSize) {
            super(rowSize, columnSize)
        }

        /**
         * Returns the built {@link BigIntMatrix}
         * 
         * @return The {@link BigIntMatrix}
         * @throws NullPointerException if one {@code entry == null}
         * @since 1
         * @author Lars Tennstedt  
         * @see ImmutableTable#copyOf
         */
        override build() {
            table.cellSet.forEach [
                requireNonNull(value, 'value')
            ]
            new BigIntMatrix(ImmutableTable::copyOf(table))
        }
    }
}
