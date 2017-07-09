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

import bigmath.util.MathRandom
import java.util.List
import org.apache.commons.lang3.RandomUtils
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy

final class BigIntMatrixTest {
  static var BigIntMatrix zeroMatrixForAddition
  static var BigIntMatrix zeroMatrixForMultiplication
  static var BigIntMatrix identityMatrix
  static val List<BigIntMatrix> matrices = newArrayList
  static val List<BigIntMatrix> squarematrices = newArrayList
  static val List<BigIntMatrix> othersForAddition = newArrayList
  static val List<BigIntMatrix> additionalOthersForAddition = newArrayList
  static val List<BigIntMatrix> othersForMultiplication = newArrayList
  static val List<BigIntMatrix> additionalOthersForMultiplication = newArrayList
  static var BigIntVector zeroVector
  static var BigIntVector vector

  @BeforeClass
  static def void setUp() {
    val mathRandom = new MathRandom
    val howMany = 10
    val bound = 10
    val rowSize = RandomUtils.nextInt(2, 10)
    val columnSize = RandomUtils.nextInt(2, 10)
    val columnSizeForOthers = RandomUtils.nextInt(2, 10)
    val columnSizeForAdditionalOthers = RandomUtils.nextInt(2, 10)
    zeroMatrixForAddition = BigIntMatrix::builder(rowSize, columnSize).putAll(0BI).build
    zeroMatrixForMultiplication = BigIntMatrix::builder(columnSize, rowSize).putAll(0BI).build
    val identyMatrixBuilder = BigIntMatrix::builder(rowSize, rowSize).putAll(0BI)
    for (index : (1 .. rowSize))
      identyMatrixBuilder.put(index, index, 1BI)
    identityMatrix = identyMatrixBuilder.build
    for (i : 1 .. howMany) {
      matrices += mathRandom.nextBigIntMatrix(bound, rowSize, columnSize)
      squarematrices += mathRandom.nextBigIntMatrix(bound, rowSize, rowSize)
      othersForAddition += mathRandom.nextBigIntMatrix(bound, rowSize, columnSize)
      additionalOthersForAddition += mathRandom.nextBigIntMatrix(bound, rowSize, columnSize)
      othersForMultiplication += mathRandom.nextBigIntMatrix(bound, columnSize, columnSizeForOthers)
      additionalOthersForMultiplication +=
        mathRandom.nextBigIntMatrix(bound, columnSizeForOthers, columnSizeForAdditionalOthers)
    }
    val vectorBuilder = BigIntVector::builder(columnSize)
    zeroVector = vectorBuilder.putAll(0BI).build
    vector = mathRandom.nextBigIntVector(bound, columnSize)
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
        assertThat(add(other)).isEqualTo(builder.build)
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
        assertThat(subtract(other)).isEqualTo(builder.build)
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
  def void multiplyNullShouldThrowException() {
    assertThatThrownBy[
      zeroMatrixForMultiplication.multiply(null)
    ].isExactlyInstanceOf(NullPointerException)
  }

  @Test
  def void multiplyShouldBeAssociative() {
    matrices.forEach [
      othersForMultiplication.forEach [ other |
        additionalOthersForMultiplication.forEach [ additionalOthers |
          assertThat(multiply(other).multiply(additionalOthers)).isEqualTo(multiply(other.multiply(additionalOthers)))
        ]
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
}
