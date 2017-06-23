package mathmyday.lib.linear

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableTable
import com.google.common.collect.Table
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.EqualsHashCode

import static com.google.common.base.Preconditions.checkArgument

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

    override get(Integer rowIndex, Integer columnIndex) {
        checkArgument(table.rowKeySet.contains(rowIndex))
        checkArgument(table.columnKeySet.contains(columnIndex))
        table.get(rowIndex, columnIndex)
    }

    override row(Integer rowIndex) {
        checkArgument(table.rowKeySet.contains(rowIndex))
        ImmutableMap.copyOf(table.row(rowIndex))
    }

    override column(Integer columnIndex) {
        checkArgument(table.columnKeySet.contains(columnIndex))
        ImmutableMap.copyOf(table.column(columnIndex))
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
