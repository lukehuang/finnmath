package mathmyday.lib.linear

import com.google.common.annotations.Beta
import com.google.common.collect.ArrayTable
import com.google.common.collect.Table

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

@Beta
abstract class AbstractMatrixBuilder<T> {
    protected Table<Integer, Integer, T> table

    new(int rowSize, int columnSize) {
        checkArgument(rowSize > 0)
        checkArgument(columnSize > 0)
        table = ArrayTable.create((1 .. rowSize), (1 .. columnSize))
    }

    def put(int rowIndex, int columnIndex, T entry) {
        requireNonNull(entry)
        checkArgument(table.rowKeySet.contains(rowIndex))
        checkArgument(table.columnKeySet.contains(columnIndex))
        table.put(rowIndex, columnIndex, entry)
    }
}
