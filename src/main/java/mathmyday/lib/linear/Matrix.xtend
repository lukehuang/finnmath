package mathmyday.lib.linear

import com.google.common.collect.Table
import java.math.BigInteger
import java.util.Map
import java.util.Set

interface Matrix<M, E, V> {
    def M add(M summand)

    def M subtract(M subtrahend)

    def M multiply(M factor)

    def M multiplyVector(V vector)
    
    def E multiplyRowWithColumn(Map<Integer, E> row, Map<Integer, E> column)
    
    def M negate()

    def E tr()

    def E det()

    def Set<Integer> rowIndexes()

    def Set<Integer> columnIndexes()

    def E get(Integer rowIndex, Integer columnIndex)

    def Map<Integer, E> row(Integer rowIndex)

    def Map<Integer, E> column(Integer columnIndex)

    def BigInteger size()

    def int rowSize()

    def int columnSize()

    def boolean squareMatrix()

    def boolean triangular()

    def boolean upperTriangular()

    def boolean lowerTriangular()

    def boolean diagonal()

    def boolean id()

    def Table<Integer, Integer, E> getTable()
}
