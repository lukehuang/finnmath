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
import static com.google.common.base.Preconditions.checkNotNull
import static com.google.common.base.Preconditions.checkState

@Beta
@Data
final class BigIntMatrix extends AbstractMatrix<BigIntMatrix, BigInteger, BigIntVector> {
  override add(BigIntMatrix summand) {
    checkNotNull(summand, 'expected: not null but actual: %s', summand)
    checkArgument(table.rowKeySet.size == summand.rowSize, 'expected: row sizes to be equal but actual: %s != %s',
      table.rowKeySet.size, summand.rowSize)
    checkArgument(table.columnKeySet.size == summand.columnSize,
      'expected: column sizes to be equal but actual: %s != %s', table.columnKeySet.size, summand.columnSize)
    val builder = BigIntMatrix::builder(rowSize, columnSize)
    table.cellSet.forEach [
      builder.put(rowKey, columnKey, value + summand.entry(rowKey, columnKey))
    ]
    builder.build
  }

  override subtract(BigIntMatrix subtrahend) {
    checkNotNull(subtrahend, 'expected: not null but actual: %s', subtrahend)
    checkArgument(table.rowKeySet.size == subtrahend.rowSize, 'expected: row sizes to be equal but actual: %s != %s',
      table.rowKeySet.size, subtrahend.rowSize)
    checkArgument(table.columnKeySet.size == subtrahend.columnSize,
      'expected: column sizes to be equal but actual: %s != %s', table.columnKeySet.size, subtrahend.columnSize)
    val builder = BigIntMatrix::builder(rowSize, columnSize)
    table.cellSet.forEach [
      builder.put(rowKey, columnKey, value - subtrahend.entry(rowKey, columnKey))
    ]
    builder.build
  }

  override multiply(BigIntMatrix factor) {
    checkNotNull(factor, 'expected: not null but actual: %s', factor)
    checkArgument(table.columnKeySet.size == factor.rowSize,
      'expected: columnSize == factor.rowSize but actual: %s != %s', table.columnKeySet.size, factor.rowSize)
    val builder = BigIntMatrix::builder(table.rowKeySet.size, factor.columnSize)
    rowIndexes.forEach [ rowIndex |
      factor.columnIndexes.forEach [ columnIndex |
        val entry = multiplyRowWithColumn(table.row(rowIndex), factor.column(columnIndex))
        builder.put(rowIndex, columnIndex, entry)
      ]
    ]
    builder.build
  }

  override multiplyVector(BigIntVector vector) {
    checkNotNull(vector, 'expected: not null but actual: %s', vector)
    val builder = BigIntVector::builder
    table.rowMap.forEach [ rowIndex, row |
      row.forEach [ columnIndex, matrixEntry |
        builder.addToEntryAndPut(rowIndex, matrixEntry * vector.entry(columnIndex))
      ]
    ]
    builder.build
  }

  override multiplyRowWithColumn(Map<Integer, BigInteger> row, Map<Integer, BigInteger> column) {
    checkNotNull(row, 'expected: not null but actual: %s', row)
    checkNotNull(column, 'expected: not null but actual: %s', column)
    var result = 0BI
    for (index : (1 .. row.size)) {
      result += row.get(index) * column.get(index)
    }
    result
  }

  override negate() {
    val builder = BigIntMatrix::builder(rowSize, columnSize)
    rowIndexes.forEach [ rowIndex |
      columnIndexes.forEach [ columnIndex |
        builder.put(rowIndex, columnIndex, -entry(rowIndex, columnIndex))
      ]
    ]
    builder.build
  }

  override tr() {
    checkState(square, 'expected: square matrix but actual: %s x %s', table.rowKeySet.size, table.columnKeySet.size)
    var result = 0BI
    for (index : (1 .. rowSize))
      result += table.get(index, index)
    result
  }

  override det() {
    0BI
  }

  override square() {
    table.rowKeySet.size == table.columnKeySet.size
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
    checkArgument(rowSize > 0, 'expected: > 0 but actual: %s <= 0', rowSize)
    checkArgument(columnSize > 0, 'expected: > 0 but actual: %s <= 0', columnSize)
    new BigIntMatrixBuilder(rowSize, columnSize)
  }

  static class BigIntMatrixBuilder extends AbstractMatrixBuilder<BigInteger> implements Builder<BigIntMatrix> {
    private new(int rowSize, int columnSize) {
      super(rowSize, columnSize)
    }

    override build() {
      table.cellSet.forEach [
        checkNotNull(value, 'expected: not null but actual: %s', value)
      ]
      new BigIntMatrix(table)
    }
  }
}
