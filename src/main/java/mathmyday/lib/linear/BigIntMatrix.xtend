package mathmyday.lib.linear

import com.google.common.annotations.Beta
import com.google.common.collect.ImmutableTable
import java.math.BigInteger
import java.util.Map
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
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
        val builder = BigIntMatrix.builder(rowSize, factor.columnSize)
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
        null
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
        0BI
    }

    override det() {
        0BI
    }

    override squareMatrix() {
        false
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

    override id() {
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
