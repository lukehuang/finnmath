/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ltennstedt.finnmath.core.linear;

import static org.assertj.guava.api.Assertions.assertThat;

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public final class BigDecimalMatrixGuavaTest {
    private final long bound = 10;
    private final int scale = 2;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final BigDecimalMatrix zeroMatrixForAddition = Matrices.buildZeroBigDecimalMatrix(rowSize, columnSize);
    private final BigDecimalMatrix identityMatrix = Matrices.buildIdentityBigDecimalMatrix(size);
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigDecimalMatrix> matrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<BigDecimalMatrix> squareMatrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, rowSize, howMany);
    private final List<BigDecimalMatrix> othersForAddition =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<BigDecimalMatrix> additionalOthersForAddition =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<BigDecimalMatrix> othersForMultiplication =
        mathRandom.nextBigDecimalMatrices(bound, scale, columnSize, columnSize, howMany);
    private final List<BigDecimalMatrix> additionalOthersForMultiplication =
        mathRandom.nextBigDecimalMatrices(bound, scale, columnSize, columnSize, howMany);
    private final List<BigDecimal> scalars = mathRandom.nextBigDecimals(bound, scale, howMany);
    private final List<BigDecimal> otherScalars = mathRandom.nextBigDecimals(bound, scale, howMany);

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final BigDecimal expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
                builder.put(rowIndex, columnIndex, expectedEntry);
            });
            assertThat(matrix.add(other).getTable()).isEqualTo(builder.build().getTable());
        }));
    }

    @Test
    public void addShouldBeCommutative() {
        matrices.forEach(matrix -> othersForAddition
            .forEach(other -> assertThat(matrix.add(other).getTable()).isEqualTo(other.add(matrix).getTable())));
    }

    @Test
    public void addShouldBeAssociative() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> additionalOthersForAddition
            .forEach(additionalOthers -> assertThat(matrix.add(other).add(additionalOthers).getTable())
                .isEqualTo(matrix.add(other.add(additionalOthers)).getTable()))));
    }

    @Test
    public void addZeroMatrixShouldBeEqualToSelf() {
        matrices
            .forEach(matrix -> assertThat(matrix.add(zeroMatrixForAddition).getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final BigDecimal expectedEntry = cell.getValue().subtract(other.element(rowIndex, columnIndex));
                builder.put(rowIndex, columnIndex, expectedEntry);
            });
            assertThat(matrix.subtract(other).getTable()).isEqualTo(builder.build().getTable());
        }));
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(
            matrix -> assertThat(matrix.subtract(zeroMatrixForAddition).getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), other.columnSize());
            matrix.rows().forEach((rowIndex, row) -> other.columns().forEach((otherColumnIndex, otherColumn) -> {
                BigDecimal element = BigDecimal.ZERO;
                for (final Entry<Integer, BigDecimal> rowEntry : row.entrySet()) {
                    element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                }
                builder.put(rowIndex, otherColumnIndex, element);
            }));
            assertThat(matrix.multiply(other).getTable()).isEqualTo(builder.build().getTable());
        }));
    }

    @Test
    public void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squareMatrices
            .forEach(matrix -> assertThat(matrix.multiply(identityMatrix).getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void multiplyShouldBeAssociative() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> additionalOthersForMultiplication
            .forEach(additionalOthers -> assertThat(matrix.multiply(other).multiply(additionalOthers).getTable())
                .isEqualTo(matrix.multiply(other.multiply(additionalOthers)).getTable()))));
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> squareMatrices
            .forEach(additionalOther -> assertThat(matrix.multiply(other.add(additionalOther)).getTable())
                .isEqualTo(matrix.multiply(other).add(matrix.multiply(additionalOther)).getTable()))));
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells()
                .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
            assertThat(matrix.scalarMultiply(scalar).getTable()).isEqualTo(builder.build().getTable());
        }));
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(matrix.scalarMultiply(scalar.multiply(otherScalar)).getTable())
                .isEqualTo(matrix.scalarMultiply(otherScalar).scalarMultiply(scalar).getTable()))));
    }

    @Test
    public void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(
            other -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).multiply(other).getTable())
                .isEqualTo(matrix.multiply(other).scalarMultiply(scalar).getTable()))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(matrix.scalarMultiply(scalar.add(otherScalar)).getTable())
                .isEqualTo(matrix.scalarMultiply(scalar).add(matrix.scalarMultiply(otherScalar)).getTable()))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach(matrix -> othersForAddition
            .forEach(other -> scalars.forEach(scalar -> assertThat(matrix.add(other).scalarMultiply(scalar).getTable())
                .isEqualTo(matrix.scalarMultiply(scalar).add(other.scalarMultiply(scalar)).getTable()))));
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(
            matrix -> assertThat(matrix.scalarMultiply(BigDecimal.ONE).getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.negate().getTable())
            .isEqualTo(matrix.scalarMultiply(BigDecimal.ONE.negate()).getTable()));
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate().getTable()).isEqualTo(zeroMatrixForAddition.getTable());
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.negate().negate().getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.multiply(identityMatrix.negate()).getTable()).isEqualTo(matrix.negate().getTable());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(BigDecimal.ONE.negate()).getTable())
            .isEqualTo(matrix.negate().getTable()));
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.columnSize(), matrix.rowSize());
            matrix.cells().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            assertThat(matrix.transpose().getTable()).isEqualTo(builder.build().getTable());
        });
    }

    @Test
    public void transposeTwiceIsEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.transpose().transpose().getTable()).isEqualTo(matrix.getTable()));
    }

    @Test
    public void transposeShouldBeAdditive() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).transpose().getTable())
                .isEqualTo(matrix.transpose().add(other.transpose()).getTable())));
    }

    @Test
    public void transposeShouldBeLinear() {
        matrices.forEach(
            matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).transpose().getTable())
                .isEqualTo(matrix.transpose().scalarMultiply(scalar).getTable())));
    }

    @Test
    public void transposeShouldBeMultiplicativeWithInversedOrder() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.multiply(other).transpose().getTable())
                .isEqualTo(other.transpose().multiply(matrix.transpose()).getTable())));
    }

    @Test
    public void minorShouldSucceed() {
        matrices.forEach(matrix -> {
            final Integer rowIndex = RandomUtils.nextInt(1, rowSize + 1);
            final Integer columnIndex = RandomUtils.nextInt(1, columnSize + 1);
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(rowSize - 1, columnSize - 1);
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (rowKey.compareTo(rowIndex) != 0 && columnKey.compareTo(columnIndex) != 0) {
                    final Integer newRowIndex = rowKey.compareTo(rowIndex) > 0 ? rowKey - 1 : rowKey;
                    final Integer newColumnIndex = columnKey.compareTo(columnIndex) > 0 ? columnKey - 1 : columnKey;
                    builder.put(newRowIndex, newColumnIndex, cell.getValue());
                }
            });
            assertThat(matrix.minor(rowIndex, columnIndex).getTable()).isEqualTo(builder.build().getTable());
        });
    }
}
