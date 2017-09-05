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
        ].isExactlyInstanceOf(NullPointerException)
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
        ].isExactlyInstanceOf(NullPointerException)
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
        ].isExactlyInstanceOf(NullPointerException)
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
            multiply(zeroMatrixForMultiplication).table.cellSet.forEach [
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
                table.cellSet.forEach [ cell |
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
            add(negate).table.cellSet.forEach [
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
            assertEquals(rowSize == columnSize, square)
        ]
    }
}
