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
import com.google.common.collect.ArrayTable
import com.google.common.collect.Table
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
@EqualsHashCode
abstract class AbstractMatrixBuilder<T> {
  protected Table<Integer, Integer, T> table

  new(int rowSize, int columnSize) {
    checkArgument(rowSize > 0, 'expected > 0 but %s <= 0', rowSize)
    checkArgument(columnSize > 0, 'expected > 0 but %s <= 0', columnSize)
    table = ArrayTable.create((1 .. rowSize), (1 .. columnSize))
  }

  def put(int rowIndex, int columnIndex, T entry) {
    requireNonNull(entry, 'entry')
    checkArgument(table.rowKeySet.contains(rowIndex), 'expected in [0, %s] but actual %s', table.rowKeySet.size,
      rowIndex)
    checkArgument(table.columnKeySet.contains(columnIndex), 'expected in [0, %s] but actual %s',
      table.columnKeySet.size, columnIndex)
    table.put(rowIndex, columnIndex, entry)
  }
}
