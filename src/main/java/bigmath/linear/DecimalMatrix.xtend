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

package bigmath.linear

import com.google.common.annotations.Beta
import com.google.common.collect.Table
import java.math.BigDecimal
import java.util.Map
import org.apache.commons.lang3.builder.Builder
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
@ToString
final class DecimalMatrix extends Matrix<DecimalMatrix, BigDecimal, DecimalVector> {
  private new(Table<Integer, Integer, BigDecimal> table) {
    super(table)
  }

  override add(DecimalMatrix summand) {
    requireNonNull(summand, 'summand')
    checkArgument(table.rowKeySet.size == summand.rowSize, 'expected equal row sizes but actual %s != %s',
      table.rowKeySet.size, summand.rowSize)
    checkArgument(table.columnKeySet.size == summand.columnSize, 'expected equal column sizes but actual %s != %s',
      table.columnKeySet.size, summand.columnSize)
    val builder = builder(rowSize, columnSize)
    table.cellSet.forEach [
      builder.put(rowKey, columnKey, value + summand.entry(rowKey, columnKey))
    ]
    builder.build
  }

  override subtract(DecimalMatrix subtrahend) {
    requireNonNull(subtrahend, 'subtrahend')
    checkArgument(table.rowKeySet.size == subtrahend.rowSize, 'expected equal row sizes but actual %s != %s',
      table.rowKeySet.size, subtrahend.rowSize)
    checkArgument(table.columnKeySet.size == subtrahend.columnSize, 'expected equal column sizes but actual %s != %s',
      table.columnKeySet.size, subtrahend.columnSize)
    val builder = builder(rowSize, columnSize)
    table.cellSet.forEach [
      builder.put(rowKey, columnKey, value - subtrahend.entry(rowKey, columnKey))
    ]
    builder.build
  }

  override multiply(DecimalMatrix factor) {
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

  override multiplyVector(DecimalVector vector) {
    requireNonNull(vector, 'vector')
    checkArgument(table.columnKeySet.size == vector.size, 'expected columnSize == vectorSize but actual %s != %s',
      table.columnKeySet.size, vector.size)
    val builder = DecimalVector::builder(vector.size)
    table.rowMap.forEach [ rowIndex, row |
      row.forEach [ columnIndex, matrixEntry |
        builder.addToEntryAndPut(rowIndex, matrixEntry * vector.entry(columnIndex))
      ]
    ]
    builder.build
  }

  override multiplyRowWithColumn(Map<Integer, BigDecimal> row, Map<Integer, BigDecimal> column) {
    requireNonNull(row, 'row')
    requireNonNull(column, 'column')
    checkArgument(row.size == column.size, 'expected row size == column size but actual %s != %s', row.size,
      column.size)
    var result = 0BD
    for (index : row.keySet)
      result += row.get(index) * column.get(index)
    result
  }

  override scalarMultiply(BigDecimal scalar) {
    requireNonNull(scalar, 'scalar')
    val builder = builder(table.rowKeySet.size, table.columnKeySet.size)
    table.cellSet.forEach [
      builder.put(rowKey, columnKey, scalar * value)
    ]
    builder.build
  }

  override negate() {
    scalarMultiply(-1BD)
  }

  override tr() {
    checkState(square, 'expected square matrix but actual %s x %s', table.rowKeySet.size, table.columnKeySet.size)
    var result = 0BD
    for (index : table.rowKeySet)
      result += table.get(index, index)
    result
  }

  override det() {
    checkState(square, 'expected square matrix but actual %s x %s', table.rowKeySet.size, table.columnKeySet.size)
    if (table.rowKeySet.size > 1) {
      var result = 0BD
      for (it : table.row(1).entrySet)
        result += (-1BD) ** (key + 1) * value * minor(1, key).det
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
    if (diagonal)
      for (index : table.rowKeySet)
        return !(table.get(index, index) != 1BI)
    false
  }

  override invertible() {
    det != 0BD
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
    checkArgument(table.containsRow(rowIndex), 'expected row index in [1, %s] but actual %s', table.rowKeySet.size,
      rowIndex)
    checkArgument(table.containsColumn(rowIndex), 'expected column index in [1, %s] but actual %s',
      table.columnKeySet.size, columnIndex)
    val builder = builder(table.rowKeySet.size - 1, table.columnKeySet.size - 1)
    table.cellSet.forEach [
      val newRowIndex = if (rowKey >= rowIndex) rowKey - 1 else rowKey
      val newColumnIndex = if (columnKey >= columnIndex) columnKey - 1 else columnKey
      builder.put(newRowIndex, newColumnIndex, value)
    ]
    builder.build
  }

  static def builder(int rowSize, int columnSize) {
    checkArgument(rowSize > 0, 'expected row size > 0 but actual %s', rowSize)
    checkArgument(columnSize > 0, 'expected column size > 0 but actual %s', columnSize)
    new DecimalMatrixBuilder(rowSize, columnSize)
  }

  @Beta
  @ToString
  static class DecimalMatrixBuilder extends MatrixBuilder<DecimalMatrix, BigDecimal> implements Builder<DecimalMatrix> {
    private new(int rowSize, int columnSize) {
      super(rowSize, columnSize)
    }

    override build() {
      table.cellSet.forEach [
        requireNonNull(value, 'value')
      ]
      new DecimalMatrix(table)
    }
  }
}
