package mathmyday.lib.linear

import com.google.common.collect.ArrayTable
import com.google.common.collect.Table
import lombok.NonNull

import static com.google.common.base.Preconditions.checkArgument

abstract class AbstractMatrixBuilder<T> {
    protected Table<Integer, Integer, T> table

    new(int rowSize, int columnSize) {
        checkArgument(rowSize > 0)
        checkArgument(columnSize > 0)
        table = ArrayTable.create((1 .. rowSize), (1 .. columnSize))
    }

    def put(int row, int column, @NonNull T entry) {
        checkArgument(row <= table.rowKeySet.size)
        checkArgument(column <= table.columnKeySet.size)
        table.put(row, column, entry)
    }
}
