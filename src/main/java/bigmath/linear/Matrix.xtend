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
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.util.Map
import java.util.Set
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument

@Beta
@EqualsHashCode
abstract class Matrix<M, E, V> {
    protected Table<Integer, Integer, E> table

    protected new(Table<Integer, Integer, E> table) {
        this.table = ImmutableTable.copyOf(table)
    }

    def rowIndexes() {
        ImmutableSet.copyOf(table.rowKeySet) as Set<Integer>
    }

    def columnIndexes() {
        ImmutableSet.copyOf(table.columnKeySet) as Set<Integer>
    }

    def entry(Integer rowIndex, Integer columnIndex) {
        checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [0, %s] but actual %s',
            table.rowKeySet.size, rowIndex)
        checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [0, %s] but actual %s',
            table.columnKeySet.size, columnIndex)
        table.get(rowIndex, columnIndex)
    }

    def row(Integer rowIndex) {
        checkArgument(table.rowKeySet.contains(rowIndex), 'expected row index in [0, %s] but actual %s',
            table.rowKeySet.size, rowIndex)
        ImmutableMap.copyOf(table.row(rowIndex)) as Map<Integer, E>
    }

    def column(Integer columnIndex) {
        checkArgument(table.columnKeySet.contains(columnIndex), 'expected column index in [0, %s] but actual %s',
            table.columnKeySet.size, columnIndex)
        ImmutableMap.copyOf(table.column(columnIndex)) as Map<Integer, E>
    }

    def rows() {
        ImmutableMap.copyOf(table.rowMap) as Map<Integer, Map<Integer, E>>
    }

    def columns() {
        ImmutableMap.copyOf(table.columnMap) as Map<Integer, Map<Integer, E>>
    }

    def size() {
        Long.valueOf(rowSize) * Long.valueOf(columnSize)
    }

    def rowSize() {
        table.rowKeySet.size
    }

    def columnSize() {
        table.columnKeySet.size
    }

    def M add(M summand)

    def M subtract(M subtrahend)

    def M multiply(M factor)

    def V multiplyVector(V vector)

    def E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column)

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

    def getTable() {
        ImmutableTable.copyOf(table) as Table<Integer, Integer, E>
    }
}
