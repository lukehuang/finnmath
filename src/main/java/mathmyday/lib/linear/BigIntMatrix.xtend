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
import com.google.common.collect.ImmutableTable
import java.math.BigInteger
import java.util.Map
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

@Beta
@Data
final class BigIntMatrix extends AbstractMatrix<BigIntMatrix, BigInteger, BigIntVector> {
    override add(BigIntMatrix summand) {
        requireNonNull(summand)
        checkArgument(rowSize == summand.rowSize)
        checkArgument(columnSize == summand.columnSize)
        val builder = BigIntMatrix.builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value + summand.get(rowKey, columnKey))
        ]
        builder.build
    }

    override subtract(BigIntMatrix subtrahend) {
        requireNonNull(subtrahend)
        checkArgument(rowSize == subtrahend.rowSize)
        checkArgument(columnSize == subtrahend.columnSize)
        val builder = BigIntMatrix.builder(rowSize, columnSize)
        table.cellSet.forEach [
            builder.put(rowKey, columnKey, value - subtrahend.get(rowKey, columnKey))
        ]
        builder.build
    }

    override multiply(BigIntMatrix factor) {
        requireNonNull(factor)
        checkArgument(columnSize == factor.rowSize)
        val builder = BigIntMatrix::builder(rowSize, factor.columnSize)
        rowIndexes.forEach [ rowIndex |
            factor.columnIndexes.forEach [ columnIndex |
                val entry = multiplyRowWithColumn(table.row(rowIndex), factor.column(columnIndex))
                builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    override multiplyVector(BigIntVector vector) {
        requireNonNull(vector)
        val builder = BigIntVector::builder
        table.rowMap.forEach [ rowIndex, row |
            row.forEach [ columnIndex, matrixEntry |
                vector.map.forEach [ vectorIndex, vectorEntry |
                    builder.addPut(rowIndex, matrixEntry * vectorEntry)
                ]
            ]
        ]
        builder.build
    }

    override multiplyRowWithColumn(Map<Integer, BigInteger> row, Map<Integer, BigInteger> column) {
        requireNonNull(row)
        requireNonNull(column)
        var result = 0BI
        for (i : (1 .. row.size)) {
            result += row.get(i) * column.get(i)
        }
        result
    }

    override negate() {
        val builder = BigIntMatrix.builder(rowSize, columnSize)
        rowIndexes.forEach [ rowIndex |
            columnIndexes.forEach [ columnIndex |
                builder.put(rowIndex, columnIndex, -get(rowIndex, columnIndex))
            ]
        ]
        builder.build
    }

    override tr() {
        checkState(square)
        var result = 0BI
        for (i : 1 .. rowSize)
            result += table.get(i, i)
        result
    }

    override det() {
        0BI
    }

    override square() {
        rowSize == columnSize
    }

    override triangular() {
        false
    }

    override upperTriangular() {
        false
    }

    override lowerTriangular() {
        false
    }

    override diagonal() {
        false
    }

    override getTable() {
        ImmutableTable.copyOf(table)
    }

    def static builder(int rowSize, int columnSize) {
        checkArgument(rowSize > 0)
        checkArgument(columnSize > 0)
        new BigIntMatrixBuilder(rowSize, columnSize)
    }

    static class BigIntMatrixBuilder extends AbstractMatrixBuilder<BigInteger> implements MatrixBuilder<BigIntMatrix> {
        private new(int rowSize, int columnSize) {
            super(rowSize, columnSize)
        }

        override build() {
            table.cellSet.forEach [
                requireNonNull(value)
            ]
            new BigIntMatrix(table)
        }
    }
}
