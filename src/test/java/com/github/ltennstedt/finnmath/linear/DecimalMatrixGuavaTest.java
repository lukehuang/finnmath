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

package com.github.ltennstedt.finnmath.linear;

import static org.assertj.guava.api.Assertions.assertThat;

import com.github.ltennstedt.finnmath.linear.DecimalMatrix.DecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.util.MathRandom;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public final class DecimalMatrixGuavaTest {
    private static DecimalMatrix zeroMatrixForAddition;
    private static DecimalMatrix identityMatrix;
    private static final int howMany = 10;
    private static final MathRandom mathRandom = new MathRandom(7);
    private static final long bound = 10;
    private static final int scale = 2;
    private static final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> squareMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> othersForAddition = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> additionalOthersForAddition = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> othersForMultiplication = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> additionalOthersForMultiplication = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> fourByFourMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> threeByThreeMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> twoByTwoMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> oneByOneMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> upperTriangularMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> lowerTriangularMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> triangularMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> threeByThreeTriangularMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> twoByTwoTriangularMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> diagonalMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> symmetricMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> skewSymmetricMatrices = new ArrayList<>(howMany);
    private static final List<DecimalVector> vectors = new ArrayList<>(howMany);
    private static final List<BigDecimal> scalars = new ArrayList<>(howMany);
    private static final List<BigDecimal> otherScalars = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUp() {
        final int rowSize = RandomUtils.nextInt(3, 10);
        final int columnSize = RandomUtils.nextInt(3, 10);
        final int columnSizeForOthers = RandomUtils.nextInt(3, 10);
        final int columnSizeForAdditionalOthers = RandomUtils.nextInt(3, 10);
        zeroMatrixForAddition = DecimalMatrix.builder(rowSize, columnSize).putAll(BigDecimal.ZERO).build();
        final DecimalMatrixBuilder identityMatrixBuilder = DecimalMatrix.builder(rowSize, rowSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    identityMatrixBuilder.put(rowIndex, columnIndex, BigDecimal.ONE);
                } else {
                    identityMatrixBuilder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        identityMatrix = identityMatrixBuilder.build();
        for (int i = 0; i < howMany; i++) {
            matrices.add(mathRandom.nextDecimalMatrix(bound, scale, rowSize, columnSize));
            squareMatrices.add(mathRandom.nextDecimalMatrix(bound, scale, rowSize, rowSize));
            othersForAddition.add(mathRandom.nextDecimalMatrix(bound, scale, rowSize, columnSize));
            additionalOthersForAddition.add(mathRandom.nextDecimalMatrix(bound, scale, rowSize, columnSize));
            othersForMultiplication.add(mathRandom.nextDecimalMatrix(bound, scale, columnSize, columnSizeForOthers));
            additionalOthersForMultiplication
                .add(mathRandom.nextDecimalMatrix(bound, scale, columnSizeForOthers, columnSizeForAdditionalOthers));
            fourByFourMatrices.add(mathRandom.nextDecimalMatrix(bound, scale, 4, 4));
            threeByThreeMatrices.add(mathRandom.nextDecimalMatrix(bound, scale, 3, 3));
            twoByTwoMatrices.add(mathRandom.nextDecimalMatrix(bound, scale, 2, 2));
            oneByOneMatrices.add(mathRandom.nextDecimalMatrix(bound, scale, 1, 1));
            vectors.add(mathRandom.nextDecimalVector(bound, scale, columnSize));
            upperTriangularMatrices.add(mathRandom.nextUpperTriangularDecimalMatrix(bound, scale, rowSize));
            lowerTriangularMatrices.add(mathRandom.nextLowerTriangularDecimalMatrix(bound, scale, rowSize));
            triangularMatrices.add(mathRandom.nextTriangularDecimalMatrix(bound, scale, rowSize));
            threeByThreeTriangularMatrices.add(mathRandom.nextTriangularDecimalMatrix(bound, scale, 3));
            twoByTwoTriangularMatrices.add(mathRandom.nextTriangularDecimalMatrix(bound, scale, 2));
            diagonalMatrices.add(mathRandom.nextDiagonalDecimalMatrix(bound, scale, rowSize));
            symmetricMatrices.add(mathRandom.nextSymmetricDecimalMatrix(bound, scale, rowSize));
            skewSymmetricMatrices.add(mathRandom.nextSkewSymmetricDecimalMatrix(bound, scale, rowSize));
            scalars.add(BigDecimal.valueOf(mathRandom.nextLong(bound)));
            otherScalars.add(BigDecimal.valueOf(mathRandom.nextLong(bound)));
        }
    }

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigDecimal expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.add(other).getTable()).isEqualTo(builder.build().getTable());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                assertThat(matrix.add(other).getTable()).isEqualTo(other.add(matrix).getTable());
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                additionalOthersForAddition.forEach(additionalOthers -> {
                    assertThat(matrix.add(other).add(additionalOthers).getTable())
                        .isEqualTo(matrix.add(other.add(additionalOthers)).getTable());
                });
            });
        });
    }

    @Test
    public void addZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.add(zeroMatrixForAddition).getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigDecimal expectedEntry = cell.getValue().subtract(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.subtract(other).getTable()).isEqualTo(builder.build().getTable());
            });
        });
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.subtract(zeroMatrixForAddition).getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), other.columnSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    other.columns().forEach((otherColumnIndex, otherColumn) -> {
                        BigDecimal element = BigDecimal.ZERO;
                        for (final Entry<Integer, BigDecimal> rowEntry : row.entrySet()) {
                            element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                        }
                        builder.put(rowIndex, otherColumnIndex, element);
                    });
                });
                assertThat(matrix.multiply(other).getTable()).isEqualTo(builder.build().getTable());
            });
        });
    }

    @Test
    public void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.multiply(identityMatrix).getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void multiplyShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                additionalOthersForMultiplication.forEach(additionalOthers -> {
                    assertThat(matrix.multiply(other).multiply(additionalOthers).getTable())
                        .isEqualTo(matrix.multiply(other.multiply(additionalOthers)).getTable());
                });
            });
        });
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                squareMatrices.forEach(additionalOther -> {
                    assertThat(matrix.multiply(other.add(additionalOther)).getTable())
                        .isEqualTo(matrix.multiply(other).add(matrix.multiply(additionalOther)).getTable());
                });
            });
        });
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
                });
                assertThat(matrix.scalarMultiply(scalar).getTable()).isEqualTo(builder.build().getTable());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(matrix.scalarMultiply(scalar.multiply(otherScalar)).getTable())
                        .isEqualTo(matrix.scalarMultiply(otherScalar).scalarMultiply(scalar).getTable());
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                scalars.forEach(scalar -> {
                    assertThat(matrix.scalarMultiply(scalar).multiply(other).getTable())
                        .isEqualTo(matrix.multiply(other).scalarMultiply(scalar).getTable());
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(matrix.scalarMultiply(scalar.add(otherScalar)).getTable())
                        .isEqualTo(matrix.scalarMultiply(scalar).add(matrix.scalarMultiply(otherScalar)).getTable());
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                scalars.forEach(scalar -> {
                    assertThat(matrix.add(other).scalarMultiply(scalar).getTable())
                        .isEqualTo(matrix.scalarMultiply(scalar).add(other.scalarMultiply(scalar)).getTable());
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(BigDecimal.ONE).getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.negate().getTable()).isEqualTo(matrix.scalarMultiply(BigDecimal.ONE.negate()).getTable());
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate().getTable()).isEqualTo(zeroMatrixForAddition.getTable());
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.negate().negate().getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder =
                DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize()).putAll(BigDecimal.ZERO);
            matrix.rowIndexes().forEach(index -> {
                builder.put(index, index, BigDecimal.ONE.negate());
            });
            assertThat(matrix.multiply(builder.build()).getTable()).isEqualTo(matrix.negate().getTable());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(BigDecimal.ONE.negate()).getTable()).isEqualTo(matrix.negate().getTable());
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.columnSize(), matrix.rowSize());
            matrix.cells().forEach(cell -> {
                builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            });
            assertThat(matrix.transpose().getTable()).isEqualTo(builder.build().getTable());
        });
    }

    @Test
    public void transposeTwiceIsEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.transpose().transpose().getTable()).isEqualTo(matrix.getTable());
        });
    }

    @Test
    public void transposeShouldBeAdditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).transpose().getTable())
                    .isEqualTo(matrix.transpose().add(other.transpose()).getTable());
            });
        });
    }

    @Test
    public void transposeShouldBeLinear() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).transpose().getTable())
                    .isEqualTo(matrix.transpose().scalarMultiply(scalar).getTable());
            });
        });
    }

    @Test
    public void transposeShouldBeMultiplicativeWithInversedOrder() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).transpose().getTable())
                    .isEqualTo(other.transpose().multiply(matrix.transpose()).getTable());
            });
        });
    }

    @Test
    public void minorShouldSucceed() {
        matrices.forEach(matrix -> {
            final int rowSize = matrix.rowSize();
            final int columnSize = matrix.columnSize();
            final Integer rowIndex = RandomUtils.nextInt(1, rowSize + 1);
            final Integer columnIndex = RandomUtils.nextInt(1, columnSize + 1);
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(rowSize - 1, columnSize - 1);
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (!rowKey.equals(rowIndex) && !columnKey.equals(columnIndex)) {
                    final Integer newRowIndex = rowKey.compareTo(rowIndex) > 0 ? rowKey - 1 : rowKey;
                    final Integer newColumnIndex = columnKey.compareTo(columnIndex) > 0 ? columnKey - 1 : columnKey;
                    builder.put(newRowIndex, newColumnIndex, cell.getValue());
                }
            });
            assertThat(matrix.minor(rowIndex, columnIndex).getTable()).isEqualTo(builder.build().getTable());
        });
    }
}
