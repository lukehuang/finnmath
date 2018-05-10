/*
 * Copyright 2018 Lars Tennstedt
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix.BigIntegerMatrixBuilder;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

public final class AbstractMatrixTest {
    private final long bound = 10;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final BigIntegerMatrix zeroSquareMatrix =
        BigIntegerMatrix.builder(rowSize, rowSize).putAll(BigInteger.ZERO).build();
    private final BigIntegerMatrix identityMatrix = Matrices.buildIdentityBigIntegerMatrix(size);
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigIntegerMatrix> matrices =
        mathRandom.nextBigIntegerMatrices(bound, rowSize, columnSize, howMany);
    private final List<BigIntegerMatrix> squareMatrices = mathRandom.nextBigIntegerMatrices(bound, size, size, howMany);
    private final List<BigIntegerMatrix> upperTriangularMatrices =
        mathRandom.nextUpperTriangularBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerMatrix> lowerTriangularMatrices =
        mathRandom.nextLowerTriangularBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerMatrix> triangularMatrices =
        mathRandom.nextTriangularBigIntegerMatrices(bound, size, howMany);
    private final List<BigIntegerMatrix> diagonalMatrices =
        mathRandom.nextDiagonalBigIntegerMatrices(bound, size, howMany);
    private final List<Integer> rowRange = IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList());
    private final List<Integer> columnRange = IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList());
    private final List<Integer> range = rowRange;

    @Test
    public void frobeniusNormShouldSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected = matrix.frobeniusNorm(AbstractMatrix.DEFAULT_SQUARE_ROOT_CONTEXT);
            assertThat(matrix.frobeniusNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void squareNonSquareMatricesShouldReturnFalse() {
        matrices.forEach(matrix -> {
            assertThat(matrix.square()).isFalse();
        });
    }

    @Test
    public void squareSquareMatricesShouldReturnTrue() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.square()).isTrue();
        });
    }

    @Test
    public void triangularShouldSucceed() {
        matrices.forEach(
            matrix -> assertThat(matrix.triangular()).isEqualTo(matrix.upperTriangular() && matrix.lowerTriangular()));
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(RandomUtils.nextLong(1, bound)));
                } else {
                    builder.put(rowIndex, columnIndex, cell.getValue());
                }
            });
            assertThat(builder.build().triangular()).isFalse();
        });
    }

    @Test
    public void triangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(RandomUtils.nextLong(1, bound)));
                } else {
                    builder.put(rowIndex, columnIndex, cell.getValue());
                }
            });
            assertThat(builder.build().triangular()).isFalse();
        });
    }

    @Test
    public void triangularTriangularMatricesShouldReturnTrue() {
        triangularMatrices.forEach(matrix -> assertThat(matrix.triangular()).isTrue());
    }

    @Test
    public void diagonalShouldSucceed() {
        squareMatrices.forEach(
            matrix -> assertThat(matrix.diagonal()).isEqualTo(matrix.upperTriangular() && matrix.lowerTriangular()));
    }

    @Test
    public void diagonalNonSquareMatricesShouldReturnFalse() {
        matrices.forEach(matrix -> assertThat(matrix.diagonal()).isFalse());
    }

    @Test
    public void diagonalDiagonalMatricesShouldReturnTrue() {
        diagonalMatrices.forEach(matrix -> assertThat(matrix.diagonal()).isTrue());
    }

    @Test
    public void diagonalNotNotDiagonalShouldReturnFalse() {
        diagonalMatrices.forEach(matrix -> {
            final Integer rowIndex = RandomUtils.nextInt(2, rowSize);
            final Integer columnIndex = RandomUtils.nextInt(1, rowIndex);
            final int factor = new Random().nextBoolean() ? -1 : 1;
            final BigInteger element = BigInteger.valueOf(factor * RandomUtils.nextLong(1, bound));
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize, matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (rowKey.compareTo(rowIndex) == 0 && columnKey.compareTo(columnIndex) == 0) {
                    builder.put(rowKey, columnKey, element);
                } else {
                    builder.put(rowKey, columnKey, cell.getValue());
                }
            });
            assertThat(builder.build().diagonal()).isFalse();
        });
    }

    @Test
    public void symmetricNonSquareMatricesShouldReturnFalse() {
        matrices.forEach(matrix -> assertThat(matrix.symmetric()).isFalse());
    }

    @Test
    public void symmetricNotSymmetricShouldReturnFalse() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
            if (rowIndex.compareTo(columnIndex) < 0) {
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, element);
            }
        }));
        assertThat(builder.build().symmetric()).isFalse();
    }

    @Test
    public void symmetricShouldSucceed() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
            if (rowIndex.compareTo(columnIndex) < 0) {
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element);
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, element);
            }
        }));
        assertThat(builder.build().symmetric()).isTrue();
    }

    @Test
    public void skewSymmetricNotquareShouldReturnFalse() {
        final BigIntegerMatrix matrix = BigIntegerMatrix.builder(4, 5).putAll(BigInteger.ZERO).build();
        assertThat(matrix.skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricNotSymmetricShouldReturnFalse() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element);
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigInteger.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigInteger.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void rowIndexesShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.rowIndexes()).isEqualTo(matrix.getTable().rowKeySet()));
    }

    @Test
    public void columnIndexesShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.columnIndexes()).isEqualTo(matrix.getTable().columnKeySet()));
    }

    @Test
    public void elementRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.element(null, 1)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("rowIndex");
    }

    @Test
    public void elementRowIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.element(0, 1)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected row index in [1, %s] but actual 0", zeroSquareMatrix.rowSize());
    }

    @Test
    public void elementColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.element(1, null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("columnIndex");
    }

    @Test
    public void elementColumnIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.element(1, 0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected column index in [1, %s] but actual 0", zeroSquareMatrix.columnSize());
    }

    @Test
    public void elementShouldSucceed() {
        matrices.forEach(matrix -> rowRange
            .forEach(rowIndex -> columnRange.forEach(columnIndex -> assertThat(matrix.element(rowIndex, columnIndex))
                .isEqualTo(matrix.getTable().get(rowIndex, columnIndex)))));
    }

    @Test
    public void cellsShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.cells()).isEqualTo(matrix.getTable().cellSet()));
    }

    @Test
    public void rowRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.row(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("rowIndex");
    }

    @Test
    public void rowRowIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.row(0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected row index in [1, %s] but actual 0", zeroSquareMatrix.rowSize());
    }

    @Test
    public void rowShouldSucceed() {
        matrices.forEach(matrix -> IntStream.rangeClosed(1, matrix.rowSize()).boxed().collect(Collectors.toList())
            .forEach(rowIndex -> assertThat(matrix.row(rowIndex)).isEqualTo(matrix.getTable().row(rowIndex))));
    }

    @Test
    public void columnColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.column(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("columnIndex");
    }

    @Test
    public void columnColumnIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.column(0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected column index in [1, %s] but actual 0", zeroSquareMatrix.columnSize());
    }

    @Test
    public void columnShouldSucceed() {
        matrices.forEach(matrix -> columnRange.forEach(
            columnIndex -> assertThat(matrix.column(columnIndex)).isEqualTo(matrix.getTable().column(columnIndex))));
    }

    @Test
    public void rowsShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.rows()).isEqualTo(matrix.getTable().rowMap()));
    }

    @Test
    public void columnsShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.columns()).isEqualTo(matrix.getTable().columnMap()));
    }

    @Test
    public void elementsShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.elements()).isEqualTo(matrix.getTable().values()));
    }

    @Test
    public void sizeShouldSucceed() {
        matrices
            .forEach(matrix -> assertThat(matrix.size()).isEqualTo(Long.valueOf(matrix.getTable().rowKeySet().size())
                * Long.valueOf(matrix.getTable().columnKeySet().size())));
    }

    @Test
    public void rowSizeShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.rowSize()).isEqualTo(matrix.getTable().rowKeySet().size()));
    }

    @Test
    public void columnSizeShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.columnSize()).isEqualTo(matrix.getTable().columnKeySet().size()));
    }

    @Test
    public void hashCodeShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.hashCode()).isEqualTo(Objects.hash(matrix.getTable())));
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        matrices.forEach(matrix -> assertThat(matrix.equals(matrix)).isTrue());
    }

    @Test
    public void equalsNotAbstractMatrixShouldReturnFalse() {
        assertThat(zeroSquareMatrix.equals(new Object())).isFalse();
    }

    @Test
    public void equalsNotEqualShouldReturnFalse() {
        squareMatrices.forEach(matrix -> assertThat(matrix.equals(matrix.add(identityMatrix))).isFalse());
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        matrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
            assertThat(matrix.equals(builder.build())).isTrue();
        });
    }

    @Test
    public void hashCodeEqualMatricesShouldHaveEqualHashCodes() {
        matrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue()));
            assertThat(matrix.hashCode()).isEqualTo(builder.build().hashCode());
        });
    }

    @Test
    public void toStringShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.toString())
            .isEqualTo(MoreObjects.toStringHelper(matrix).add("table", matrix.getTable()).toString()));
    }
}
