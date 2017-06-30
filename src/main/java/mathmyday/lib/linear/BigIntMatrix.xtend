/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, togliu
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

package mathmyday.lib.linear

import com.google.common.annotations.Beta
import java.math.BigInteger
import java.util.Map
import org.apache.commons.lang3.builder.Builder
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

@Beta
@Data
final class BigIntMatrix extends AbstractMatrix<BigIntMatrix, BigInteger, BigIntVector> implements Matrix<BigIntMatrix, BigInteger, BigIntVector> {
    override add(BigIntMatrix summand) {
        requireNonNull(summand, 'summand')
        checkArgument(table.rowKeySet.size == summand.rowSize, 'equal row sizes expected but actual %s != %s',
            table.rowKeySet.last, summand.rowSize)
        checkArgument(table.columnKeySet.size == summand.columnSize, 'column sizes expected equal but actual %s != %s',
            table.columnKeySet.last, summand.columnSize)
        val builder = BigIntMatrix::builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value + summand.entry(rowKey, columnKey))
        ]
        builder.build
    }

    override subtract(BigIntMatrix subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        checkArgument(table.rowKeySet.size == subtrahend.rowSize, 'equal row sizes expected but actual %s != %s',
            table.rowKeySet.last, subtrahend.rowSize)
        checkArgument(table.columnKeySet.size == subtrahend.columnSize,
            'equal column sizes expected but actual %s != %s', table.columnKeySet.last, subtrahend.columnSize)
        val builder = builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value - subtrahend.entry(rowKey, columnKey))
        ]
        builder.build
    }

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

    override multiplyVector(BigIntVector vector) {
        requireNonNull(vector, 'vector')
        val builder = BigIntVector::builder
        table.rowMap.forEach [ rowIndex, row |
            row.forEach [ columnIndex, matrixEntry |
                builder.addToEntryAndPut(rowIndex, matrixEntry * vector.entry(columnIndex))
            ]
        ]
        builder.build
    }

    override multiplyRowWithColumn(Map<Integer, BigInteger> row, Map<Integer, BigInteger> column) {
        requireNonNull(row, 'row')
        requireNonNull(column, 'column')
        checkArgument(row.size == column.size, 'expected row size == column size but actual %s != %s', row.size,
            column.size)
        var result = 0BI
        for (index : row.keySet)
            result += row.get(index) * column.get(index)
        result
    }

    override scalarMultiply(BigInteger scalar) {
        requireNonNull(scalar, 'scalar')
        val builder = builder(table.rowKeySet.last, table.columnKeySet.last)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, scalar * value)
        ]
        builder.build
    }

    override negate() {
        scalarMultiply(-1BI)
    }

    override tr() {
        checkState(square, 'expected square matrix but actual %s x %s', table.rowKeySet.size, table.columnKeySet.size)
        var result = 0BI
        for (index : table.rowKeySet)
            result += table.get(index, index)
        result
    }

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

    override square() {
        table.rowKeySet.size == table.columnKeySet.size
    }

    override triangular() {
        upperTriangular || lowerTriangular
    }

    override upperTriangular() {
        if (square) {
            for (it : table.cellSet)
                if (rowKey > columnKey && value != 0BI)
                    return false
            return true
        }
        false
    }

    override lowerTriangular() {
        if (square) {
            for (it : table.cellSet)
                if (rowKey < columnKey && value != 0BI)
                    return false
            return true
        }
        false
    }

    override diagonal() {
        upperTriangular && lowerTriangular
    }

    override id() {
        if (diagonal) {
            for (index : table.rowKeySet)
                if (table.get(index, index) != 1BI)
                    return false
            return true
        }
        false
    }

    override invertible() {
        det == -1BI || det == 1BI
    }

    override transpose() {
        val builder = builder(table.columnKeySet.size, table.rowKeySet.size)
        table.cellSet.forEach [
            builder.put(columnKey, rowKey, value)
        ]
        builder.build
    }

    override symmetric() {
        square && equals(transpose)
    }

    override skewSymmetric() {
        square && equals(transpose.negate)
    }

    override minor(Integer rowIndex, Integer columnIndex) {
        requireNonNull(rowIndex, 'rowIndex')
        requireNonNull(columnIndex, 'columnIndex')
        checkArgument(table.containsRow(rowIndex), 'expected row index in [0, %s] but actual %s', table.rowKeySet.last,
            rowIndex)
        checkArgument(table.containsColumn(rowIndex), 'expected column index in [0, %s] but actual %s',
            table.columnKeySet.last, columnIndex)
        val builder = builder(table.rowKeySet.size - 1, table.columnKeySet.size - 1)
        table.cellSet.forEach [
            val newRowIndex = if(rowKey >= rowIndex) rowKey - 1 else rowKey
            val newColumnIndex = if(columnKey >= columnIndex) columnKey - 1 else columnKey
            builder.put(newRowIndex, newColumnIndex, value)
        ]
        builder.build
    }

    static def builder(int rowSize, int columnSize) {
        checkArgument(rowSize > 0, 'expected row size > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected column size > 0 but actual %s', columnSize)
        new BigIntMatrixBuilder(rowSize, columnSize)
    }

    static class BigIntMatrixBuilder extends AbstractMatrixBuilder<BigInteger> implements Builder<BigIntMatrix> {
        private new(int rowSize, int columnSize) {
            super(rowSize, columnSize)
        }

        override build() {
            table.cellSet.forEach [
                requireNonNull(value, 'value')
            ]
            new BigIntMatrix(table)
        }
    }
}
