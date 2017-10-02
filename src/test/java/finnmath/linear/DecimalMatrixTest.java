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

package finnmath.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Table.Cell;
import finnmath.assertion.DecimalMatrixAssert;
import finnmath.assertion.DecimalVectorAssert;
import finnmath.linear.DecimalMatrix.DecimalMatrixBuilder;
import finnmath.linear.DecimalVector.DecimalVectorBuilder;
import finnmath.util.MathRandom;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.BeforeClass;
import org.junit.Test;

public final class DecimalMatrixTest {
    private static DecimalMatrix zeroMatrixForAddition;
    private static DecimalMatrix zeroMatrixForMultiplication;
    private static DecimalMatrix zeroSquareMatrix;
    private static DecimalMatrix identityMatrix;
    private static final int howMany = 10;
    private static final MathRandom mathRandom = new MathRandom();
    private static final long bound = 10;
    private static final int scale = 2;
    private static final List<DecimalMatrix> matrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> squareMatrices = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> othersForAddition = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> additionalOthersForAddition = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> othersForMultiplication = new ArrayList<>(howMany);
    private static final List<DecimalMatrix> additionalOthersForMultiplication = new ArrayList<>(howMany);
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
        zeroMatrixForMultiplication = DecimalMatrix.builder(columnSize, rowSize).putAll(BigDecimal.ZERO).build();
        zeroSquareMatrix = DecimalMatrix.builder(rowSize, rowSize).putAll(BigDecimal.ZERO).build();
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
            additionalOthersForMultiplication.add(
                    mathRandom.nextDecimalMatrix(bound, scale, columnSizeForOthers, columnSizeForAdditionalOthers));
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
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.add(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("summand");

    }

    @Test
    public void addRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix matrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalMatrix summand = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 3 != 2");

    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix matrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalMatrix summand = DecimalMatrix.builder(3, 2).putAll(BigDecimal.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 3 != 2");

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
                DecimalMatrixAssert.assertThat(matrix.add(other)).isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                DecimalMatrixAssert.assertThat(matrix.add(other)).isEqualToByBigDecimalComparator(other.add(matrix));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                additionalOthersForAddition.forEach(additionalOthers -> {
                    DecimalMatrixAssert.assertThat(matrix.add(other).add(additionalOthers))
                            .isEqualToByBigDecimalComparator(matrix.add(other.add(additionalOthers)));
                });
            });
        });
    }

    @Test
    public void addZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.add(zeroMatrixForAddition)).isEqualToByBigDecimalComparator(matrix);
        });

    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.subtract(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("subtrahend");

    }

    @Test
    public void subtractRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix minuend = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalMatrix subtrahend = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 3 != 2");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix minuend = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalMatrix subtrahend = DecimalMatrix.builder(3, 2).putAll(BigDecimal.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 3 != 2");

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
                DecimalMatrixAssert.assertThat(matrix.subtract(other)).isExactlyInstanceOf(DecimalMatrix.class)
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.subtract(zeroMatrixForAddition))
                    .isEqualToByBigDecimalComparator(matrix);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.subtract(matrix))
                    .isEqualToByBigDecimalComparator(zeroMatrixForAddition);
        });
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForMultiplication.multiply(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");

    }

    @Test
    public void multiplyColumnSizeNotEqualToFactorRowSizeShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix matrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalMatrix factor = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == factor.rowSize but actual 3 != 2");

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
                DecimalMatrixAssert.assertThat(matrix.multiply(other)).isExactlyInstanceOf(DecimalMatrix.class)
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            matrix.multiply(zeroMatrixForMultiplication).cells().forEach(cell -> {
                assertThat(cell.getValue().compareTo(BigDecimal.ZERO)).isEqualTo(0);
            });
        });
    }

    @Test
    public void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squareMatrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.multiply(identityMatrix)).isEqualToByBigDecimalComparator(matrix);
        });
    }

    @Test
    public void multiplyShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                additionalOthersForMultiplication.forEach(additionalOthers -> {
                    DecimalMatrixAssert.assertThat(matrix.multiply(other).multiply(additionalOthers))
                            .isEqualToByBigDecimalComparator(matrix.multiply(other.multiply(additionalOthers)));
                });
            });
        });
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                squareMatrices.forEach(additionalOther -> {
                    DecimalMatrixAssert.assertThat(matrix.multiply(other.add(additionalOther)))
                            .isEqualToByBigDecimalComparator(
                                    matrix.multiply(other).add(matrix.multiply(additionalOther)));
                });
            });
        });
    }

    @Test
    public void multiplyNullVectorShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.multiplyVector(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void multiplyVectorColumnSizeNotEqualToVectorSizeShouldThrowException() {
        assertThatThrownBy(() -> {
            final DecimalMatrix matrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
            final DecimalVector vector = DecimalVector.builder(2).putAll(BigDecimal.ZERO).build();
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == vectorSize but actual 3 != 2");
    }

    @Test
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(matrix.rowSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    BigDecimal element = BigDecimal.ZERO;
                    for (final Integer columnIndex : matrix.columnIndexes()) {
                        element = element.add(row.get(columnIndex).multiply(vector.element(columnIndex)));
                    }
                    builder.put(element);
                });
                DecimalVectorAssert.assertThat(matrix.multiplyVector(vector))
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            final DecimalVector zeroVector = DecimalVector.builder(matrix.columnSize()).putAll(BigDecimal.ZERO).build();
            final DecimalVector expected = DecimalVector.builder(matrix.rowSize()).putAll(BigDecimal.ZERO).build();
            DecimalVectorAssert.assertThat(matrix.multiplyVector(zeroVector)).isEqualToByBigDecimalComparator(expected);
        });

    }

    @Test
    public void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.scalarMultiply(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
                });
                DecimalMatrixAssert.assertThat(matrix.scalarMultiply(scalar)).isExactlyInstanceOf(DecimalMatrix.class)
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    DecimalMatrixAssert.assertThat(matrix.scalarMultiply(scalar.multiply(otherScalar)))
                            .isEqualToByBigDecimalComparator(matrix.scalarMultiply(otherScalar).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                scalars.forEach(scalar -> {
                    DecimalMatrixAssert.assertThat(matrix.scalarMultiply(scalar).multiply(other))
                            .isEqualToByBigDecimalComparator(matrix.multiply(other).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    DecimalMatrixAssert.assertThat(matrix.scalarMultiply(scalar.add(otherScalar)))
                            .isEqualToByBigDecimalComparator(
                                    matrix.scalarMultiply(scalar).add(matrix.scalarMultiply(otherScalar)));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                scalars.forEach(scalar -> {
                    DecimalMatrixAssert.assertThat(matrix.add(other).scalarMultiply(scalar))
                            .isEqualToByBigDecimalComparator(
                                    matrix.scalarMultiply(scalar).add(other.scalarMultiply(scalar)));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.scalarMultiply(BigDecimal.ZERO))
                    .isEqualToByBigDecimalComparator(zeroMatrixForAddition);
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.scalarMultiply(BigDecimal.ONE))
                    .isEqualToByBigDecimalComparator(matrix);
        });
    }

    @Test
    public void scalarMultiplyAndMultiplyVectorShouldBeCommutative() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                scalars.forEach(scalar -> {
                    DecimalVectorAssert.assertThat(matrix.scalarMultiply(scalar).multiplyVector(vector))
                            .isEqualToByBigDecimalComparator(matrix.multiplyVector(vector).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.negate()).isExactlyInstanceOf(DecimalMatrix.class)
                    .isEqualToByBigDecimalComparator(matrix.scalarMultiply(BigDecimal.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        DecimalMatrixAssert.assertThat(zeroMatrixForAddition.negate())
                .isEqualToByBigDecimalComparator(zeroMatrixForAddition);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.negate().negate()).isEqualToByBigDecimalComparator(matrix);
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            matrix.add(matrix.negate()).cells().forEach(cell -> {
                assertThat(cell.getValue().compareTo(BigDecimal.ZERO)).isEqualTo(0);
            });
        });
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize())
                    .putAll(BigDecimal.ZERO);
            matrix.rowIndexes().forEach(index -> {
                builder.put(index, index, BigDecimal.ONE.negate());
            });
            DecimalMatrixAssert.assertThat(matrix.multiply(builder.build()))
                    .isEqualToByBigDecimalComparator(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.scalarMultiply(BigDecimal.ONE.negate()))
                    .isEqualToByBigDecimalComparator(matrix.negate());
        });
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build().trace();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final Integer index : matrix.rowIndexes()) {
                expected = expected.add(matrix.element(index, index));
            }
            assertThat(matrix.trace().compareTo(expected)).isEqualTo(0);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void traceShouldBeAdditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).trace().compareTo(matrix.trace().add(other.trace()))).isEqualTo(0);
            });
        });
    }

    @Test
    public void traceShouldBeLinear() {
        squareMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).trace().compareTo(scalar.multiply(matrix.trace())))
                        .isEqualTo(0);
            });
        });
    }

    @Test
    public void traceShouldBeIndependentOfTheOrderOfTheFactors() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).trace().compareTo(other.multiply(matrix).trace())).isEqualTo(0);
            });
        });
    }

    @Test
    public void traceShouldBeEqualToTheTraceOfTheTranspose() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.trace().compareTo(matrix.transpose().trace())).isEqualTo(0);
        });
    }

    @Test
    public void detNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void detRowSizeTooLargeShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(4, 3).putAll(BigDecimal.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected rowSize < 4 but actual 4");
    }

    @Test
    public void detColumnSizeTooLargeShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(3, 4).putAll(BigDecimal.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected columnSize < 4 but actual 4");
    }

    @Test
    public void detOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final BigDecimal expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                    .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant().compareTo(expected)).isEqualTo(0);
        });
    }

    @Test
    public void detOfOneByOneMatricesShouldSucceed() {
        oneByOneMatrices.forEach(matrix -> {
            assertThat(matrix.determinant().compareTo(matrix.element(1, 1))).isEqualTo(0);
        });
    }

    @Test
    public void detOfZeroMatrixShouldBeEqualToZero() {
        final DecimalMatrix zeroThreeByThreeMatrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
        final DecimalMatrix zeroTwoByTwoMatrix = DecimalMatrix.builder(2, 2).putAll(BigDecimal.ZERO).build();
        final DecimalMatrix zeroOneByOneMatrix = DecimalMatrix.builder(1, 1).put(1, 1, BigDecimal.ZERO).build();
        assertThat(zeroThreeByThreeMatrix.determinant().compareTo(BigDecimal.ZERO)).isEqualTo(0);
        assertThat(zeroTwoByTwoMatrix.determinant().compareTo(BigDecimal.ZERO)).isEqualTo(0);
        assertThat(zeroOneByOneMatrix.determinant().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void detOfIdentityMatrixShouldBeEqualToOne() {
        final DecimalMatrixBuilder identityThreeByThreeMatrixBuilder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    identityThreeByThreeMatrixBuilder.put(rowIndex, columnIndex, BigDecimal.ONE);
                } else {
                    identityThreeByThreeMatrixBuilder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        final DecimalMatrix identityThreeByThreeMatrix = identityThreeByThreeMatrixBuilder.build();
        final DecimalMatrix identityTwoByTwoMatrix = DecimalMatrix.builder(2, 2).put(1, 1, BigDecimal.ONE)
                .put(1, 2, BigDecimal.ZERO).put(2, 1, BigDecimal.ZERO).put(2, 2, BigDecimal.ONE).build();
        final DecimalMatrix identityOneByOneMatrix = DecimalMatrix.builder(1, 1).put(1, 1, BigDecimal.ONE).build();
        assertThat(identityThreeByThreeMatrix.determinant().compareTo(BigDecimal.ONE)).isEqualTo(0);
        assertThat(identityTwoByTwoMatrix.determinant().compareTo(BigDecimal.ONE)).isEqualTo(0);
        assertThat(identityOneByOneMatrix.determinant().compareTo(BigDecimal.ONE)).isEqualTo(0);
    }

    @Test
    public void detWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        threeByThreeMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant()
                        .compareTo(scalar.pow(3).multiply(matrix.determinant()))).isEqualTo(0);
            });
        });
        twoByTwoMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant()
                        .compareTo(scalar.pow(2).multiply(matrix.determinant()))).isEqualTo(0);
            });
        });
        oneByOneMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant().compareTo(scalar.multiply(matrix.determinant())))
                        .isEqualTo(0);
            });
        });
    }

    @Test
    public void detOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        threeByThreeTriangularMatrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ONE;
            for (final Cell<Integer, Integer, BigDecimal> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant().compareTo(expected)).isEqualTo(0);
        });
        twoByTwoTriangularMatrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ONE;
            for (final Cell<Integer, Integer, BigDecimal> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant().compareTo(expected)).isEqualTo(0);
        });
        oneByOneMatrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ONE;
            for (final Cell<Integer, Integer, BigDecimal> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant().compareTo(expected)).isEqualTo(0);
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.columnSize(), matrix.rowSize());
            matrix.cells().forEach(cell -> {
                builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            });
            DecimalMatrixAssert.assertThat(matrix.transpose()).isEqualToByBigDecimalComparator(builder.build());
        });
    }

    @Test
    public void transposeTwiceIsEqualToSelf() {
        matrices.forEach(matrix -> {
            DecimalMatrixAssert.assertThat(matrix.transpose().transpose()).isEqualToByBigDecimalComparator(matrix);
        });
    }

    @Test
    public void transposeShouldBeAdditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                DecimalMatrixAssert.assertThat(matrix.add(other).transpose())
                        .isEqualToByBigDecimalComparator(matrix.transpose().add(other.transpose()));
            });
        });
    }

    @Test
    public void transposeShouldBeLinear() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                DecimalMatrixAssert.assertThat(matrix.scalarMultiply(scalar).transpose())
                        .isEqualToByBigDecimalComparator(matrix.transpose().scalarMultiply(scalar));
            });
        });
    }

    @Test
    public void transposeShouldBeMultiplicativeWithInversedOrder() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                DecimalMatrixAssert.assertThat(matrix.multiply(other).transpose())
                        .isEqualToByBigDecimalComparator(other.transpose().multiply(matrix.transpose()));
            });
        });
    }

    @Test
    public void minorRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(null, 1);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("rowIndex");
    }

    @Test
    public void minorColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(1, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("columnIndex");
    }

    @Test
    public void minorRowIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowIndex in [1, %s] but actual 0",
                zeroMatrixForAddition.rowSize());
    }

    @Test
    public void minorRowIndexTooHighShouldThrowException() {
        final Integer invalidRowIndex = zeroMatrixForAddition.rowSize() + 1;
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(invalidRowIndex, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowIndex in [1, %s] but actual %s",
                zeroMatrixForAddition.rowSize(), invalidRowIndex);
    }

    @Test
    public void minorColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnIndex in [1, %s] but actual 0", zeroMatrixForAddition.columnSize());
    }

    @Test
    public void minorColumnIndexTooHighShouldThrowException() {
        final Integer invalidColumnIndex = zeroMatrixForAddition.columnSize() + 1;
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(1, invalidColumnIndex);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(
                "expected columnIndex in [1, %s] but actual %s", zeroMatrixForAddition.columnSize(),
                invalidColumnIndex);
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
                    final Integer newRowIndex = rowKey > rowIndex ? rowKey - 1 : rowKey;
                    final Integer newColumnIndex = columnKey > columnIndex ? columnKey - 1 : columnKey;
                    builder.put(newRowIndex, newColumnIndex, cell.getValue());
                }
            });
            DecimalMatrixAssert.assertThat(matrix.minor(rowIndex, columnIndex)).isExactlyInstanceOf(DecimalMatrix.class)
                    .isEqualToByBigDecimalComparator(builder.build());
        });
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build().square()).isFalse();
    }

    @Test
    public void squareOfSquareMatricesShouldBeTrue() {
        assertThat(squareMatrices).are(new Condition<>(matrix -> matrix.square(), "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(matrix -> {
            assertThat(matrix.square()).isEqualTo(matrix.rowSize() == matrix.columnSize());
        });
    }

    @Test
    public void triangularNotSquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.triangular()).isFalse();
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
                    builder.put(rowIndex, columnIndex, BigDecimal.valueOf(RandomUtils.nextLong(1, bound)));
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
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, BigDecimal.valueOf(RandomUtils.nextLong(1, bound)));
                } else {
                    builder.put(rowIndex, columnIndex, cell.getValue());
                }
            });
            assertThat(builder.build().triangular()).isFalse();
        });
    }

    @Test
    public void triangularShouldSucceed() {
        triangularMatrices.forEach(matrix -> {
            assertThat(matrix.triangular()).isTrue();
        });
    }

    @Test
    public void upperTriangularNotSquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.upperTriangular()).isFalse();
    }

    @Test
    public void upperTriangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
                    builder.put(rowIndex, columnIndex, BigDecimal.valueOf(RandomUtils.nextLong(1, bound)));
                } else {
                    builder.put(rowIndex, columnIndex, cell.getValue());
                }
            });
            assertThat(builder.build().upperTriangular()).isFalse();
        });
    }

    @Test
    public void upperTriangularShouldSucceed() {
        upperTriangularMatrices.forEach(matrix -> {
            assertThat(matrix.upperTriangular()).isTrue();
        });
    }

    @Test
    public void lowerTriangularNotSquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.lowerTriangular()).isFalse();
    }

    @Test
    public void lowerTriangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, BigDecimal.valueOf(RandomUtils.nextLong(1, bound)));
                } else {
                    builder.put(rowIndex, columnIndex, cell.getValue());
                }
            });
            assertThat(builder.build().lowerTriangular()).isFalse();
        });
    }

    @Test
    public void lowerTriangularShouldSucceed() {
        lowerTriangularMatrices.forEach(matrix -> {
            assertThat(matrix.lowerTriangular()).isTrue();
        });
    }

    @Test
    public void diagonalNotSquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.diagonal()).isFalse();
    }

    @Test
    public void diagonalNotNotDiagonalShouldReturnFalse() {
        diagonalMatrices.forEach(matrix -> {
            final int rowSize = matrix.rowSize();
            final Integer rowIndex = RandomUtils.nextInt(2, rowSize);
            final Integer columnIndex = RandomUtils.nextInt(1, rowIndex);
            final int factor = new Random().nextBoolean() ? -1 : 1;
            final BigDecimal element = BigDecimal.valueOf(factor * RandomUtils.nextLong(1, bound));
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(rowSize, matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (rowKey.equals(rowIndex) && columnKey.equals(columnIndex)) {
                    builder.put(rowKey, columnKey, element);
                } else {
                    builder.put(rowKey, columnKey, cell.getValue());
                }
            });
            assertThat(builder.build().diagonal()).isFalse();
        });
    }

    @Test
    public void diagonalShouldSucceed() {
        diagonalMatrices.forEach(matrix -> {
            assertThat(matrix.diagonal()).isTrue();
        });
    }

    @Test
    public void identityNotDiagonalhouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.identity()).isFalse();
    }

    @Test
    public void identityNotIdShouldReturnFalse() {
        assertThat(zeroMatrixForAddition.identity()).isFalse();
    }

    @Test
    public void identityShouldSucceed() {
        assertThat(identityMatrix.identity()).isTrue();
    }

    @Test
    public void invertibleNotSquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.invertible()).isFalse();
    }

    @Test
    public void invertibleZeroMatrixShouldReturnFalse() {
        final DecimalMatrix zeroMatrix = DecimalMatrix.builder(3, 3).putAll(BigDecimal.ZERO).build();
        assertThat(zeroMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleIdShouldSucceed() {
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final int rowSize = 3;
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(rowSize, 3);
        final Integer index = RandomUtils.nextInt(1, rowSize + 1);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(index) && columnIndex.equals(index)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ONE.negate());
                } else if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void symmetricNotquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.symmetric()).isFalse();
    }

    @Test
    public void symmetricNotSymmetricShouldReturnFalse() {
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, element);
                }
            });
        });
        assertThat(builder.build().symmetric()).isFalse();
    }

    @Test
    public void symmetricShouldSucceed() {
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, element);
                }
            });
        });
        assertThat(builder.build().symmetric()).isTrue();
    }

    @Test
    public void skewSymmetricNotquareShouldReturnFalse() {
        final DecimalMatrix matrix = DecimalMatrix.builder(2, 3).putAll(BigDecimal.ZERO).build();
        assertThat(matrix.skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricNotSymmetricShouldReturnFalse() {
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final DecimalMatrixBuilder builder = DecimalMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.hashCode()).isExactlyInstanceOf(Integer.class).isEqualTo(Objects.hash(matrix.getTable()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        matrices.forEach(matrix -> {
            assertThat(matrix.equals(matrix)).isTrue();
        });
    }

    @Test
    public void equalsNotSimpleComplexNumberShouldReturnFalse() {
        assertThat(zeroMatrixForAddition.equals(new Object())).isFalse();
    }

    @Test
    public void equalsNotEqualShouldReturnFalse() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.equals(matrix.add(identityMatrix))).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        matrices.forEach(matrix -> {
            final DecimalMatrixBuilder builder = DecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                builder.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            });
            assertThat(matrix.equals(builder.build())).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.toString()).isExactlyInstanceOf(String.class)
                    .isEqualTo(MoreObjects.toStringHelper(matrix).add("table", matrix.getTable()).toString());
        });
    }
}
