/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, Lars Tennstedt
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

package finnmath.linear

import finnmath.util.MathRandom
import java.math.BigInteger
import java.util.ArrayList
import java.util.List
import org.apache.commons.lang3.RandomUtils
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

final class BigIntMatrixTest {
    static var BigIntMatrix zeroMatrixForAddition
    static var BigIntMatrix zeroMatrixForMultiplication
    static var BigIntMatrix identityMatrix
    static var List<BigIntMatrix> matrices
    static var List<BigIntMatrix> squarematrices
    static var List<BigIntMatrix> othersForAddition
    static var List<BigIntMatrix> additionalOthersForAddition
    static var List<BigIntMatrix> othersForMultiplication
    static var List<BigIntMatrix> additionalOthersForMultiplication
    static var List<BigIntVector> vectors
    static var List<BigInteger> scalars
    static var List<BigInteger> otherScalars

    @BeforeClass
    static def void setUp() {
        val mathRandom = new MathRandom
        val howMany = 10
        val bound = 10
        val rowSize = RandomUtils::nextInt(2, 10)
        val columnSize = RandomUtils::nextInt(2, 10)
        val columnSizeForOthers = RandomUtils::nextInt(2, 10)
        val columnSizeForAdditionalOthers = RandomUtils::nextInt(2, 10)
        zeroMatrixForAddition = BigIntMatrix::builder(rowSize, columnSize).putAll(0BI).build
        zeroMatrixForMultiplication = BigIntMatrix::builder(columnSize, rowSize).putAll(0BI).build
        val identityMatrixBuilder = BigIntMatrix::builder(rowSize, rowSize).putAll(0BI)
        for (index : (1 .. rowSize))
            identityMatrixBuilder.put(index, index, 1BI)
        identityMatrix = identityMatrixBuilder.build
        matrices = mathRandom.nextBigIntMatrices(bound, rowSize, columnSize, howMany)
        squarematrices = mathRandom.nextBigIntMatrices(bound, rowSize, rowSize, howMany)
        othersForAddition = mathRandom.nextBigIntMatrices(bound, rowSize, columnSize, howMany)
        additionalOthersForAddition = mathRandom.nextBigIntMatrices(bound, rowSize, columnSize, howMany)
        othersForMultiplication = mathRandom.nextBigIntMatrices(bound, columnSize, columnSizeForOthers, howMany)
        additionalOthersForMultiplication = mathRandom.nextBigIntMatrices(bound, columnSizeForOthers,
            columnSizeForAdditionalOthers, howMany)
        vectors = mathRandom.nextBigIntVectors(bound, columnSize, howMany)
        scalars = new ArrayList(howMany)
        otherScalars = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++) {
            scalars += BigInteger::valueOf(mathRandom.nextLong(bound))
            otherScalars += BigInteger::valueOf(mathRandom.nextLong(bound))
        }
    }

    @Test
    def void addNullShouldThrowException() {
        assertThatThrownBy[
            zeroMatrixForAddition.add(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('summand')
    }

    @Test
    def void addShouldSucceed() {
        matrices.forEach [
            othersForAddition.forEach [ other |
                val builder = BigIntMatrix::builder(rowSize, columnSize)
                rowIndexes.forEach [ rowIndex |
                    columnIndexes.forEach [ columnIndex |
                        val expectedEntry = entry(rowIndex, columnIndex) + other.entry(rowIndex, columnIndex)
                        builder.put(rowIndex, columnIndex, expectedEntry)
                    ]
                ]
                assertThat(add(other)).isExactlyInstanceOf(BigIntMatrix).isEqualTo(builder.build)
            ]
        ]
    }

    @Test
    def void addShouldBeCommutative() {
        matrices.forEach [
            othersForAddition.forEach [ other |
                assertThat(add(other)).isEqualTo(other.add(it))
            ]
        ]
    }

    @Test
    def void addShouldBeAssociative() {
        matrices.forEach [
            othersForAddition.forEach [ other |
                additionalOthersForAddition.forEach [ additionalOthers |
                    assertThat(add(other).add(additionalOthers)).isEqualTo(add(other.add(additionalOthers)))
                ]
            ]
        ]
    }

    @Test
    def void addZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach [
            assertThat(add(zeroMatrixForAddition)).isEqualTo(it)
        ]
    }

    @Test
    def void subtractNullShouldThrowException() {
        assertThatThrownBy[
            zeroMatrixForAddition.subtract(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('subtrahend')
    }

    @Test
    def void subtractShouldSucceed() {
        matrices.forEach [
            othersForAddition.forEach [ other |
                val builder = BigIntMatrix::builder(rowSize, columnSize)
                rowIndexes.forEach [ rowIndex |
                    columnIndexes.forEach [ columnIndex |
                        val expectedEntry = entry(rowIndex, columnIndex) - other.entry(rowIndex, columnIndex)
                        builder.put(rowIndex, columnIndex, expectedEntry)
                    ]
                ]
                assertThat(subtract(other)).isExactlyInstanceOf(BigIntMatrix).isEqualTo(builder.build)
            ]
        ]
    }

    @Test
    def void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach [
            assertThat(subtract(zeroMatrixForAddition)).isEqualTo(it)
        ]
    }

    @Test
    def void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach [
            assertThat(subtract(it)).isEqualTo(zeroMatrixForAddition)
        ]
    }

    @Test
    def void multiplyNullShouldThrowException() {
        assertThatThrownBy[
            zeroMatrixForMultiplication.multiply(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('factor')
    }

    @Test
    def void multiplyShouldSucceed() {
        matrices.forEach [
            othersForMultiplication.forEach [ other |
                val builder = BigIntMatrix::builder(rowSize, other.columnSize)
                rows.forEach [ rowIndex, row |
                    other.columns.forEach [ otherColumnIndex, otherColumn |
                        var entry = 0BI
                        for (rowEntry : row.entrySet)
                            entry += rowEntry.value * otherColumn.get(rowEntry.key)
                        builder.put(rowIndex, otherColumnIndex, entry)
                    ]
                ]
                assertThat(multiply(other)).isExactlyInstanceOf(BigIntMatrix).isEqualTo(builder.build)
            ]
        ]
    }

    @Test
    def void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach [
            multiply(zeroMatrixForMultiplication).cells.forEach [
                assertThat(value).isEqualTo(0BI)
            ]
        ]
    }

    @Test
    def void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squarematrices.forEach [
            assertThat(multiply(identityMatrix)).isEqualTo(it)
        ]
    }

    @Test
    def void multiplyShouldBeAssociative() {
        matrices.forEach [
            othersForMultiplication.forEach [ other |
                additionalOthersForMultiplication.forEach [ additionalOthers |
                    assertThat(multiply(other).multiply(additionalOthers)).isEqualTo(
                        multiply(other.multiply(additionalOthers)))
                ]
            ]
        ]
    }

    @Test
    def void addAndMultiplyShouldBeDistributive() {
        squarematrices.forEach [
            squarematrices.forEach [ other |
                squarematrices.forEach [ additionalOther |
                    assertThat(multiply(other.add(additionalOther))).isEqualTo(
                        multiply(other).add(multiply(additionalOther)))
                ]
            ]
        ]
    }

    @Test
    def void multiplyNullVectorShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.multiplyVector(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('vector')
    }

    @Test
    def void multiplyVectorShouldSucceed() {
        matrices.forEach [
            vectors.forEach [ vector |
                val builder = BigIntVector::builder(rowSize)
                rows.forEach [ rowIndex, row |
                    var entry = 0BI
                    for (columnIndex : columnIndexes)
                        entry += row.get(columnIndex) * vector.entry(columnIndex)
                    builder.put(entry)
                ]
                assertThat(multiplyVector(vector)).isExactlyInstanceOf(BigIntVector).isEqualTo(builder.build)
            ]
        ]
    }

    @Test
    def void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach [
            val zeroVector = BigIntVector::builder(columnSize).putAll(0BI).build
            val expected = BigIntVector::builder(rowSize).putAll(0BI).build
            assertThat(multiplyVector(zeroVector)).isEqualTo(expected)
        ]
    }

    @Test
    def void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.scalarMultiply(null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('scalar')
    }

    @Test
    def void scalarMultiplyShouldSucceed() {
        matrices.forEach [
            scalars.forEach [ scalar |
                val builder = BigIntMatrix::builder(rowSize, columnSize)
                cells.forEach [ cell |
                    builder.put(cell.rowKey, cell.columnKey, scalar * cell.value)
                ]
                assertThat(scalarMultiply(scalar)).isExactlyInstanceOf(BigIntMatrix).isEqualTo(builder.build)
            ]
        ]
    }

    @Test
    def void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach [
            scalars.forEach [ scalar |
                otherScalars.forEach [ otherScalar |
                    assertThat(scalarMultiply(scalar * otherScalar)).isEqualTo(
                        scalarMultiply(otherScalar).scalarMultiply(scalar))
                ]
            ]
        ]
    }

    @Test
    def void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach [
            othersForMultiplication.forEach [ other |
                scalars.forEach [ scalar |
                    assertThat(scalarMultiply(scalar).multiply(other)).isEqualTo(multiply(other).scalarMultiply(scalar))
                ]
            ]
        ]
    }

    @Test
    def void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach [
            scalars.forEach [ scalar |
                otherScalars.forEach [ otherScalar |
                    assertThat(scalarMultiply(scalar + otherScalar)).isEqualTo(
                        scalarMultiply(scalar).add(scalarMultiply(otherScalar)))
                ]
            ]
        ]
    }

    @Test
    def void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach [
            othersForAddition.forEach [ other |
                scalars.forEach [ scalar |
                    assertThat(add(other).scalarMultiply(scalar)).isEqualTo(
                        scalarMultiply(scalar).add(other.scalarMultiply(scalar)))
                ]
            ]
        ]
    }

    @Test
    def void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        matrices.forEach [
            assertThat(scalarMultiply(0BI)).isEqualTo(zeroMatrixForAddition)
        ]
    }

    @Test
    def void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach [
            assertThat(scalarMultiply(1BI)).isEqualTo(it)
        ]
    }

    @Test
    def void negateShouldSucceed() {
        matrices.forEach [
            assertThat(negate).isExactlyInstanceOf(BigIntMatrix).isEqualTo(scalarMultiply(-1BI))
        ]
    }

    @Test
    def void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate).isEqualTo(zeroMatrixForAddition)
    }

    @Test
    def void addNegatedShouldBeEqualToZeroMatrix() {
        matrices.forEach [
            add(negate).cells.forEach [
                assertThat(value).isEqualTo(0BI)
            ]
        ]
    }

    @Test
    def void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squarematrices.forEach [
            val builder = BigIntMatrix::builder(rowSize, columnSize).putAll(0BI)
            rowIndexes.forEach [ index |
                builder.put(index, index, -1BI)
            ]
            assertThat(multiply(builder.build)).isEqualTo(negate)
        ]
    }

    @Test
    def void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach [
            assertThat(scalarMultiply(-1BI)).isEqualTo(negate)
        ]
    }

    @Test
    def void trNotSquareShouldThrowException() {
        assertThatThrownBy [
            BigIntMatrix::builder(2, 3).putAll(0BI).build.trace
        ].isExactlyInstanceOf(IllegalStateException).hasMessage('expected square matrix but actual 2 x 3')
    }

    @Test
    def void trShouldSucceed() {
        squarematrices.forEach [
            var expected = 0BI
            for (index : rowIndexes)
                expected += entry(index, index)
            assertThat(trace).isExactlyInstanceOf(BigInteger).isEqualTo(expected)
        ]
    }

    @Test
    def void detNotSquareShouldThrowException() {
        assertThatThrownBy [
            BigIntMatrix::builder(2, 3).putAll(0BI).build.det
        ].isExactlyInstanceOf(IllegalStateException).hasMessage('expected square matrix but actual 2 x 3')
    }

    @Test
    def void squareOfNonSquareMatrixShouldBeFalse() {
        assertFalse(BigIntMatrix::builder(2, 3).putAll(0BI).build.square)
    }

    @Test
    def void squareOfSquareMatricesShouldBeTrue() {
        squarematrices.forEach [
            assertTrue(square)
        ]
    }

    @Test
    def void squareShouldBePredictable() {
        matrices.forEach [
            assertEquals(rowSize === columnSize, square)
        ]
    }

    @Test
    def void transposeShouldSucceed() {
        matrices.forEach [
            val builder = BigIntMatrix::builder(columnSize, rowSize)
            cells.forEach [ cell |
                builder.put(cell.columnKey, cell.rowKey, cell.value)
            ]
            assertThat(transpose).isEqualTo(builder.build)
        ]
    }

    @Test
    def void minorRowIndexNullShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.minor(null, 1)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('rowIndex')
    }

    @Test
    def void minorColumnIndexNullShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.minor(1, null)
        ].isExactlyInstanceOf(NullPointerException).hasMessage('columnIndex')
    }

    @Test
    def void minorRowIndexTooLowShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.minor(0, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).
            hasMessage('''expected row index in [1, «zeroMatrixForAddition.rowSize»] but actual 0''')
    }

    @Test
    def void minorRowIndexTooHighShouldThrowException() {
        val invalidRowIndex = zeroMatrixForAddition.rowSize + 1
        assertThatThrownBy [
            zeroMatrixForAddition.minor(invalidRowIndex, 1)
        ].isExactlyInstanceOf(IllegalArgumentException).
            hasMessage('''expected row index in [1, «zeroMatrixForAddition.rowSize»] but actual «invalidRowIndex»''')
    }

    @Test
    def void minorColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy [
            zeroMatrixForAddition.minor(1, 0)
        ].isExactlyInstanceOf(IllegalArgumentException).
            hasMessage('''expected column index in [1, «zeroMatrixForAddition.columnSize»] but actual 0''')
    }

    @Test
    def void minorColumnIndexTooHighShouldThrowException() {
        val invalidColumnIndex = zeroMatrixForAddition.columnSize + 1
        assertThatThrownBy [
            zeroMatrixForAddition.minor(1, invalidColumnIndex)
        ].isExactlyInstanceOf(IllegalArgumentException).
            hasMessage('''expected column index in [1, «zeroMatrixForAddition.columnSize»] but actual «invalidColumnIndex»''')
    }

    @Test
    def void minorShouldSucceed() {
        matrices.forEach [
            val rowIndex = RandomUtils::nextInt(1, rowSize + 1)
            val columnIndex = RandomUtils::nextInt(1, columnSize + 1)
            val builder = BigIntMatrix::builder(rowSize - 1, columnSize - 1)
            cells.forEach [ cell |
                if (cell.rowKey != rowIndex && cell.columnKey != columnIndex) {
                    val newRowIndex = if (cell.rowKey > rowIndex) cell.rowKey - 1 else cell.rowKey
                    val newColumnIndex = if (cell.columnKey > columnIndex) cell.columnKey - 1 else cell.columnKey
                    builder.put(newRowIndex, newColumnIndex, cell.value)
                }
            ]
            assertThat(minor(rowIndex, columnIndex)).isExactlyInstanceOf(BigIntMatrix).isEqualTo(builder.build)
        ]
    }
}
