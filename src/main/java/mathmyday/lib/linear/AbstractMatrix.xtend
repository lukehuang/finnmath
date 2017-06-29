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
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument

@Beta
@EqualsHashCode
abstract class AbstractMatrix<M, E, V> implements Matrix<M, E, V> {
  Table<Integer, Integer, E> table

  protected new(Table<Integer, Integer, E> table) {
    this.table = ImmutableTable.copyOf(table)
  }

  override rowIndexes() {
    table.rowKeySet
  }

  override columnIndexes() {
    table.columnKeySet
  }

  override entry(Integer rowIndex, Integer columnIndex) {
    checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [0, %s] but actual %s',
      table.rowKeySet.last, rowIndex)
    checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [0, %s] but actual %s',
      table.columnKeySet.last, columnIndex)
    table.get(rowIndex, columnIndex)
  }

  override row(Integer rowIndex) {
    checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [0, %s] but actual %s',
      table.rowKeySet.last, rowIndex)
    ImmutableMap.copyOf(table.row(rowIndex))
  }

  override column(Integer columnIndex) {
    checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [0, %s] but actual %s',
      table.columnKeySet.last, columnIndex)
    ImmutableMap.copyOf(table.column(columnIndex))
  }

  override rows() {
    ImmutableMap.copyOf(table.rowMap)
  }

  override columns() {
    ImmutableMap.copyOf(table.columnMap)
  }

  override size() {
    BigInteger.valueOf(rowSize) * BigInteger.valueOf(columnSize)
  }

  override rowSize() {
    table.rowKeySet.size
  }

  override columnSize() {
    table.columnKeySet.size
  }
}
