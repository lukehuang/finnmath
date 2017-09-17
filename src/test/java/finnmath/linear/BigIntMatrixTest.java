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
import finnmath.linear.BigIntMatrix.BigIntMatrixBuilder;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import finnmath.util.MathRandom;
import java.math.BigInteger;
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

public final class BigIntMatrixTest {
    private static BigIntMatrix zeroMatrixForAddition;
    private static BigIntMatrix zeroMatrixForMultiplication;
    private static BigIntMatrix zeroSquareMatrix;
    private static BigIntMatrix identityMatrix;
    private static final int howMany = 10;
    private static final MathRandom mathRandom = new MathRandom();
    private static final long bound = 10;
    private static final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> squareMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> othersForAddition = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> additionalOthersForAddition = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> othersForMultiplication = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> additionalOthersForMultiplication = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> threeByThreeMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> twoByTwoMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> oneByOneMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> upperTriangularMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> lowerTriangularMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> triangularMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> threeByThreeTriangularMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> twoByTwoTriangularMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> diagonalMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> symmetricMatrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> skewSymmetricMatrices = new ArrayList<>(howMany);
    private static final List<BigIntVector> vectors = new ArrayList<>(howMany);
    private static final List<BigInteger> scalars = new ArrayList<>(howMany);
    private static final List<BigInteger> otherScalars = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUp() {
        final int rowSize = RandomUtils.nextInt(3, 10);
        final int columnSize = RandomUtils.nextInt(3, 10);
        final int columnSizeForOthers = RandomUtils.nextInt(3, 10);
        final int columnSizeForAdditionalOthers = RandomUtils.nextInt(3, 10);
        zeroMatrixForAddition = BigIntMatrix.builder(rowSize, columnSize).putAll(BigInteger.ZERO).build();
        zeroMatrixForMultiplication = BigIntMatrix.builder(columnSize, rowSize).putAll(BigInteger.ZERO).build();
        zeroSquareMatrix = BigIntMatrix.builder(rowSize, rowSize).putAll(BigInteger.ZERO).build();
        final BigIntMatrixBuilder identityMatrixBuilder = BigIntMatrix.builder(rowSize, rowSize);
        IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    identityMatrixBuilder.put(rowIndex, columnIndex, BigInteger.ONE);
                } else {
                    identityMatrixBuilder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        identityMatrix = identityMatrixBuilder.build();
        for (int i = 0; i < howMany; i++) {
            matrices.add(mathRandom.nextBigIntMatrix(bound, rowSize, columnSize));
            squareMatrices.add(mathRandom.nextBigIntMatrix(bound, rowSize, rowSize));
            othersForAddition.add(mathRandom.nextBigIntMatrix(bound, rowSize, columnSize));
            additionalOthersForAddition.add(mathRandom.nextBigIntMatrix(bound, rowSize, columnSize));
            othersForMultiplication.add(mathRandom.nextBigIntMatrix(bound, columnSize, columnSizeForOthers));
            additionalOthersForMultiplication
                    .add(mathRandom.nextBigIntMatrix(bound, columnSizeForOthers, columnSizeForAdditionalOthers));
            threeByThreeMatrices.add(mathRandom.nextBigIntMatrix(bound, 3, 3));
            twoByTwoMatrices.add(mathRandom.nextBigIntMatrix(bound, 2, 2));
            oneByOneMatrices.add(mathRandom.nextBigIntMatrix(bound, 1, 1));
            vectors.add(mathRandom.nextBigIntVector(bound, columnSize));
            upperTriangularMatrices.add(mathRandom.nextUpperTriangularBigIntMatrix(bound, rowSize));
            lowerTriangularMatrices.add(mathRandom.nextLowerTriangularBigIntMatrix(bound, rowSize));
            triangularMatrices.add(mathRandom.nextTriangularBigIntMatrix(bound, rowSize));
            threeByThreeTriangularMatrices.add(mathRandom.nextTriangularBigIntMatrix(bound, 3));
            twoByTwoTriangularMatrices.add(mathRandom.nextTriangularBigIntMatrix(bound, 2));
            diagonalMatrices.add(mathRandom.nextDiagonalBigIntMatrix(bound, rowSize));
            symmetricMatrices.add(mathRandom.nextSymmetricBigIntMatrix(bound, rowSize));
            skewSymmetricMatrices.add(mathRandom.nextSkewSymmetricBigIntMatrix(bound, rowSize));
            scalars.add(BigInteger.valueOf(mathRandom.nextLong(bound)));
            otherScalars.add(BigInteger.valueOf(mathRandom.nextLong(bound)));
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
            final BigIntMatrix matrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntMatrix summand = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 3 != 2");

    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntMatrix matrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntMatrix summand = BigIntMatrix.builder(3, 2).putAll(BigInteger.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 3 != 2");

    }

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigInteger expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.add(other)).isExactlyInstanceOf(BigIntMatrix.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                assertThat(matrix.add(other)).isEqualTo(other.add(matrix));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                additionalOthersForAddition.forEach(additionalOthers -> {
                    assertThat(matrix.add(other).add(additionalOthers))
                            .isEqualTo(matrix.add(other.add(additionalOthers)));
                });
            });
        });
    }

    @Test
    public void addZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.add(zeroMatrixForAddition)).isEqualTo(matrix);
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
            final BigIntMatrix minuend = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntMatrix subtrahend = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 3 != 2");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntMatrix minuend = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntMatrix subtrahend = BigIntMatrix.builder(3, 2).putAll(BigInteger.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 3 != 2");

    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigInteger expectedEntry = cell.getValue().subtract(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.subtract(other)).isExactlyInstanceOf(BigIntMatrix.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.subtract(zeroMatrixForAddition)).isEqualTo(matrix);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            assertThat(matrix.subtract(matrix)).isEqualTo(zeroMatrixForAddition);
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
            final BigIntMatrix matrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntMatrix factor = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == factor.rowSize but actual 3 != 2");

    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), other.columnSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    other.columns().forEach((otherColumnIndex, otherColumn) -> {
                        BigInteger element = BigInteger.ZERO;
                        for (final Entry<Integer, BigInteger> rowEntry : row.entrySet()) {
                            element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                        }
                        builder.put(rowIndex, otherColumnIndex, element);
                    });
                });
                assertThat(matrix.multiply(other)).isExactlyInstanceOf(BigIntMatrix.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            matrix.multiply(zeroMatrixForMultiplication).cells().forEach(cell -> {
                assertThat(cell.getValue()).isEqualTo(BigInteger.ZERO);
            });
        });
    }

    @Test
    public void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.multiply(identityMatrix)).isEqualTo(matrix);
        });
    }

    @Test
    public void multiplyShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                additionalOthersForMultiplication.forEach(additionalOthers -> {
                    assertThat(matrix.multiply(other).multiply(additionalOthers))
                            .isEqualTo(matrix.multiply(other.multiply(additionalOthers)));
                });
            });
        });
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                squareMatrices.forEach(additionalOther -> {
                    assertThat(matrix.multiply(other.add(additionalOther)))
                            .isEqualTo(matrix.multiply(other).add(matrix.multiply(additionalOther)));
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
            final BigIntMatrix matrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
            final BigIntVector vector = BigIntVector.builder(2).putAll(BigInteger.ZERO).build();
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == vectorSize but actual 3 != 2");
    }

    @Test
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(matrix.rowSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    BigInteger element = BigInteger.ZERO;
                    for (final Integer columnIndex : matrix.columnIndexes()) {
                        element = element.add(row.get(columnIndex).multiply(vector.element(columnIndex)));
                    }
                    builder.put(element);
                });
                assertThat(matrix.multiplyVector(vector)).isExactlyInstanceOf(BigIntVector.class)
                        .isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            final BigIntVector zeroVector = BigIntVector.builder(matrix.columnSize()).putAll(BigInteger.ZERO).build();
            final BigIntVector expected = BigIntVector.builder(matrix.rowSize()).putAll(BigInteger.ZERO).build();
            assertThat(matrix.multiplyVector(zeroVector)).isEqualTo(expected);
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
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
                });
                assertThat(matrix.scalarMultiply(scalar)).isExactlyInstanceOf(BigIntMatrix.class)
                        .isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(matrix.scalarMultiply(scalar.multiply(otherScalar)))
                            .isEqualTo(matrix.scalarMultiply(otherScalar).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                scalars.forEach(scalar -> {
                    assertThat(matrix.scalarMultiply(scalar).multiply(other))
                            .isEqualTo(matrix.multiply(other).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(matrix.scalarMultiply(scalar.add(otherScalar)))
                            .isEqualTo(matrix.scalarMultiply(scalar).add(matrix.scalarMultiply(otherScalar)));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                scalars.forEach(scalar -> {
                    assertThat(matrix.add(other).scalarMultiply(scalar))
                            .isEqualTo(matrix.scalarMultiply(scalar).add(other.scalarMultiply(scalar)));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(BigInteger.ZERO)).isEqualTo(zeroMatrixForAddition);
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(BigInteger.ONE)).isEqualTo(matrix);
        });
    }

    @Test
    public void scalarMultiplyAndMultiplyVectorShouldBeCommutative() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                scalars.forEach(scalar -> {
                    assertThat(matrix.scalarMultiply(scalar).multiplyVector(vector))
                            .isEqualTo(matrix.multiplyVector(vector).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.negate()).isExactlyInstanceOf(BigIntMatrix.class)
                    .isEqualTo(matrix.scalarMultiply(BigInteger.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate()).isEqualTo(zeroMatrixForAddition);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.negate().negate()).isEqualTo(matrix);
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            matrix.add(matrix.negate()).cells().forEach(cell -> {
                assertThat(cell.getValue()).isEqualTo(BigInteger.ZERO);
            });
        });
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize())
                    .putAll(BigInteger.ZERO);
            matrix.rowIndexes().forEach(index -> {
                builder.put(index, index, BigInteger.ONE.negate());
            });
            assertThat(matrix.multiply(builder.build())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(BigInteger.ONE.negate())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build().trace();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            BigInteger expected = BigInteger.ZERO;
            for (final Integer index : matrix.rowIndexes()) {
                expected = expected.add(matrix.element(index, index));
            }
            assertThat(matrix.trace()).isExactlyInstanceOf(BigInteger.class).isEqualTo(expected);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void traceShouldBeAdditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).trace()).isEqualTo(matrix.trace().add(other.trace()));
            });
        });
    }

    @Test
    public void traceShouldBeLinear() {
        squareMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).trace()).isEqualTo(scalar.multiply(matrix.trace()));
            });
        });
    }

    @Test
    public void traceShouldBeIndependentOfTheOrderOfTheFactors() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).trace()).isEqualTo(other.multiply(matrix).trace());
            });
        });
    }

    @Test
    public void traceShouldBeEqualToTheTraceOfTheTranspose() {
        squareMatrices.forEach(matrix -> {
            assertThat(matrix.trace()).isEqualTo(matrix.transpose().trace());
        });
    }

    @Test
    public void detNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void detRowSizeTooLargeShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(4, 3).putAll(BigInteger.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected row size < 4 but actual 4");
    }

    @Test
    public void detColumnSizeTooLargeShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(3, 4).putAll(BigInteger.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected column size < 4 but actual 4");
    }

    @Test
    public void detOfThreeByThreeMatricesShouldSucceed() {
        threeByThreeMatrices.forEach(matrix -> {
            final BigInteger firstSummand = matrix.element(1, 1).multiply(matrix.element(2, 2))
                    .multiply(matrix.element(3, 3));
            final BigInteger secondSummand = matrix.element(1, 2).multiply(matrix.element(2, 3))
                    .multiply(matrix.element(3, 1));
            final BigInteger thirdSummand = matrix.element(1, 3).multiply(matrix.element(2, 1))
                    .multiply(matrix.element(3, 2));
            final BigInteger fourthSummand = matrix.element(3, 1).multiply(matrix.element(2, 2))
                    .multiply(matrix.element(1, 3)).negate();
            final BigInteger fifthSummand = matrix.element(3, 2).multiply(matrix.element(2, 3))
                    .multiply(matrix.element(1, 1)).negate();
            final BigInteger sixthSummand = matrix.element(3, 3).multiply(matrix.element(2, 1))
                    .multiply(matrix.element(1, 2)).negate();
            final BigInteger expected = firstSummand.add(secondSummand).add(thirdSummand).add(fourthSummand)
                    .add(fifthSummand).add(sixthSummand);
            assertThat(matrix.determinant()).isExactlyInstanceOf(BigInteger.class).isEqualTo(expected);
        });
    }

    @Test
    public void detOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final BigInteger expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                    .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant()).isExactlyInstanceOf(BigInteger.class).isEqualTo(expected);
        });
    }

    @Test
    public void detOfOneByOneMatricesShouldSucceed() {
        oneByOneMatrices.forEach(matrix -> {
            assertThat(matrix.determinant()).isExactlyInstanceOf(BigInteger.class).isEqualTo(matrix.element(1, 1));
        });
    }

    @Test
    public void detOfZeroMatrixShouldBeEqualToZero() {
        final BigIntMatrix zeroThreeByThreeMatrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
        final BigIntMatrix zeroTwoByTwoMatrix = BigIntMatrix.builder(2, 2).putAll(BigInteger.ZERO).build();
        final BigIntMatrix zeroOneByOneMatrix = BigIntMatrix.builder(1, 1).put(1, 1, BigInteger.ZERO).build();
        assertThat(zeroThreeByThreeMatrix.determinant()).isEqualTo(BigInteger.ZERO);
        assertThat(zeroTwoByTwoMatrix.determinant()).isEqualTo(BigInteger.ZERO);
        assertThat(zeroOneByOneMatrix.determinant()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void detOfIdentityMatrixShouldBeEqualToOne() {
        final BigIntMatrixBuilder identityThreeByThreeMatrixBuilder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    identityThreeByThreeMatrixBuilder.put(rowIndex, columnIndex, BigInteger.ONE);
                } else {
                    identityThreeByThreeMatrixBuilder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        final BigIntMatrix identityThreeByThreeMatrix = identityThreeByThreeMatrixBuilder.build();
        final BigIntMatrix identityTwoByTwoMatrix = BigIntMatrix.builder(2, 2).put(1, 1, BigInteger.ONE)
                .put(1, 2, BigInteger.ZERO).put(2, 1, BigInteger.ZERO).put(2, 2, BigInteger.ONE).build();
        final BigIntMatrix identityOneByOneMatrix = BigIntMatrix.builder(1, 1).put(1, 1, BigInteger.ONE).build();
        assertThat(identityThreeByThreeMatrix.determinant()).isEqualTo(identityTwoByTwoMatrix.determinant())
                .isEqualTo(identityOneByOneMatrix.determinant()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void detOfTransposeShouldBeEqualToDet() {
        threeByThreeMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
        twoByTwoMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
        oneByOneMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
    }

    @Test
    public void detShouldBeMultiplicative() {
        threeByThreeMatrices.forEach(matrix -> {
            threeByThreeMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).determinant())
                        .isEqualTo(matrix.determinant().multiply(other.determinant()));
            });
        });
        twoByTwoMatrices.forEach(matrix -> {
            twoByTwoMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).determinant())
                        .isEqualTo(matrix.determinant().multiply(other.determinant()));
            });
        });
        oneByOneMatrices.forEach(matrix -> {
            oneByOneMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).determinant())
                        .isEqualTo(matrix.determinant().multiply(other.determinant()));
            });
        });
    }

    @Test
    public void detWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        threeByThreeMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant())
                        .isEqualTo(scalar.pow(3).multiply(matrix.determinant()));
            });
        });
        twoByTwoMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant())
                        .isEqualTo(scalar.pow(2).multiply(matrix.determinant()));
            });
        });
        oneByOneMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant())
                        .isEqualTo(scalar.multiply(matrix.determinant()));
            });
        });
    }

    @Test
    public void detOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        threeByThreeTriangularMatrices.forEach(matrix -> {
            BigInteger expected = BigInteger.ONE;
            for (final Cell<Integer, Integer, BigInteger> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
        twoByTwoTriangularMatrices.forEach(matrix -> {
            BigInteger expected = BigInteger.ONE;
            for (final Cell<Integer, Integer, BigInteger> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
        oneByOneMatrices.forEach(matrix -> {
            BigInteger expected = BigInteger.ONE;
            for (final Cell<Integer, Integer, BigInteger> cell : matrix.cells()) {
                if (cell.getRowKey().equals(cell.getColumnKey())) {
                    expected = expected.multiply(cell.getValue());
                }
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.columnSize(), matrix.rowSize());
            matrix.cells().forEach(cell -> {
                builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
            });
            assertThat(matrix.transpose()).isEqualTo(builder.build());
        });
    }

    @Test
    public void transposeTwiceIsEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.transpose().transpose()).isEqualTo(matrix);
        });
    }

    @Test
    public void transposeShouldBeAdditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).transpose()).isEqualTo(matrix.transpose().add(other.transpose()));
            });
        });
    }

    @Test
    public void transposeShouldBeLinear() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).transpose())
                        .isEqualTo(matrix.transpose().scalarMultiply(scalar));
            });
        });
    }

    @Test
    public void transposeShouldBeMultiplicativeWithInversedOrder() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).transpose())
                        .isEqualTo(other.transpose().multiply(matrix.transpose()));
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
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row index in [1, %s] but actual 0",
                zeroMatrixForAddition.rowSize());
    }

    @Test
    public void minorRowIndexTooHighShouldThrowException() {
        final Integer invalidRowIndex = zeroMatrixForAddition.rowSize() + 1;
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(invalidRowIndex, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row index in [1, %s] but actual %s",
                zeroMatrixForAddition.rowSize(), invalidRowIndex);
    }

    @Test
    public void minorColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected column index in [1, %s] but actual 0", zeroMatrixForAddition.columnSize());
    }

    @Test
    public void minorColumnIndexTooHighShouldThrowException() {
        final Integer invalidColumnIndex = zeroMatrixForAddition.columnSize() + 1;
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.minor(1, invalidColumnIndex);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(
                "expected column index in [1, %s] but actual %s", zeroMatrixForAddition.columnSize(),
                invalidColumnIndex);
    }

    @Test
    public void minorShouldSucceed() {
        matrices.forEach(matrix -> {
            final int rowSize = matrix.rowSize();
            final int columnSize = matrix.columnSize();
            final Integer rowIndex = RandomUtils.nextInt(1, rowSize + 1);
            final Integer columnIndex = RandomUtils.nextInt(1, columnSize + 1);
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(rowSize - 1, columnSize - 1);
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (!rowKey.equals(rowIndex) && !columnKey.equals(columnIndex)) {
                    final Integer newRowIndex = rowKey > rowIndex ? rowKey - 1 : rowKey;
                    final Integer newColumnIndex = columnKey > columnIndex ? columnKey - 1 : columnKey;
                    builder.put(newRowIndex, newColumnIndex, cell.getValue());
                }
            });
            assertThat(matrix.minor(rowIndex, columnIndex)).isExactlyInstanceOf(BigIntMatrix.class)
                    .isEqualTo(builder.build());
        });
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build().square()).isFalse();
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.triangular()).isFalse();
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
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
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(RandomUtils.nextLong(1, bound)));
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.upperTriangular()).isFalse();
    }

    @Test
    public void upperTriangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(RandomUtils.nextLong(1, bound)));
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.lowerTriangular()).isFalse();
    }

    @Test
    public void lowerTriangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, BigInteger.valueOf(RandomUtils.nextLong(1, bound)));
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.diagonal()).isFalse();
    }

    @Test
    public void diagonalNotNotDiagonalShouldReturnFalse() {
        diagonalMatrices.forEach(matrix -> {
            final int rowSize = matrix.rowSize();
            final Integer rowIndex = RandomUtils.nextInt(2, rowSize);
            final Integer columnIndex = RandomUtils.nextInt(1, rowIndex);
            final int factor = new Random().nextBoolean() ? -1 : 1;
            final BigInteger element = BigInteger.valueOf(factor * RandomUtils.nextLong(1, bound));
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(rowSize, matrix.columnSize());
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
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
        final BigIntMatrix matrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.invertible()).isFalse();
    }

    @Test
    public void invertibleZeroMatrixShouldReturnFalse() {
        final BigIntMatrix zeroMatrix = BigIntMatrix.builder(3, 3).putAll(BigInteger.ZERO).build();
        assertThat(zeroMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleIdShouldSucceed() {
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final int rowSize = 3;
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(rowSize, 3);
        final Integer index = RandomUtils.nextInt(1, rowSize + 1);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex.equals(index) && columnIndex.equals(index)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ONE.negate());
                } else if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void symmetricNotquareShouldReturnFalse() {
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.symmetric()).isFalse();
    }

    @Test
    public void symmetricNotSymmetricShouldReturnFalse() {
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
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
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
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
        final BigIntMatrix matrix = BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build();
        assertThat(matrix.skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricNotSymmetricShouldReturnFalse() {
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(1, bound));
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final BigIntMatrixBuilder builder = BigIntMatrix.builder(3, 3);
        IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
            IntStream.rangeClosed(1, 3).boxed().collect(Collectors.toList()).forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final BigInteger element = BigInteger.valueOf(RandomUtils.nextLong(0, bound));
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, BigInteger.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row size > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected column size > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.hashCode()).isExactlyInstanceOf(Integer.class).isEqualTo(Objects.hash(matrix.getTable()));
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
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
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
