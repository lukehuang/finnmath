package mathmyday.lib.linear

import com.google.common.collect.Table
import java.math.BigInteger
import java.util.Map
import java.util.Set

interface Matrix<T> {
    def Matrix<T> add(Matrix<T> summand)

    def Matrix<T> subtract(Matrix<T> summand)

    def Matrix<T> multiply(Matrix<T> summand)

    def Matrix<T> multiplyVector(Vector<T> summand)

    def Matrix<T> negate()

    def T tr()

    def T det()
    
    def Set<Integer> rowIndexes()
    
    def Set<Integer> columnIndexes()

    def T get(Integer row, Integer column)

    def Map<Integer, T> row(Integer row)

    def Map<Integer, T> column(Integer column)
    
    def BigInteger size()

    def int rowSize()

    def int columnSize()

    def boolean squareMatrix()

    def boolean triangular()

    def boolean upperTriangular()

    def boolean lowerTriangular()

    def boolean diagonal()

    def boolean id()

    def Table<Integer, Integer, T> getTable()
}
