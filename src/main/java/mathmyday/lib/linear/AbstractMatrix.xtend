package mathmyday.lib.linear

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.EqualsHashCode

@EqualsHashCode
abstract class AbstractMatrix<T> implements Matrix<T> {
    Table<Integer, Integer, T> table

    protected new(Table<Integer, Integer, T> table) {
        this.table = ImmutableTable.copyOf(table)
    }
    
    override rowIndexes() {
        table.rowKeySet
    }
    
    override columnIndexes() {
        table.columnKeySet
    }

    override get(Integer row, Integer column) {
        table.get(row, column)
    }

    override row(Integer row) {
        ImmutableMap.copyOf(table.row(row))
    }

    override column(Integer column) {
        ImmutableMap.copyOf(table.column(column))
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
