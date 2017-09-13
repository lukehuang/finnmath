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

import finnmath.linear.BigIntMatrix.BigIntMatrixBuilder;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import finnmath.util.MathRandom;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.BeforeClass;
import org.junit.Test;

public final class BigIntMatrixTest {
    private static BigIntMatrix zeroMatrixForAddition;
    private static BigIntMatrix zeroMatrixForMultiplication;
    private static BigIntMatrix identityMatrix;
    private static final int howMany = 10;
    private static final List<BigIntMatrix> matrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> squarematrices = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> othersForAddition = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> additionalOthersForAddition = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> othersForMultiplication = new ArrayList<>(howMany);
    private static final List<BigIntMatrix> additionalOthersForMultiplication = new ArrayList<>(howMany);
    private static final List<BigIntVector> vectors = new ArrayList<>(howMany);
    private static final List<BigInteger> scalars = new ArrayList<>(howMany);
    private static final List<BigInteger> otherScalars = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUp() {
        final MathRandom mathRandom = new MathRandom();
        final long bound = 10;
        final int rowSize = RandomUtils.nextInt(2, 10);
        final int columnSize = RandomUtils.nextInt(2, 10);
        final int columnSizeForOthers = RandomUtils.nextInt(2, 10);
        final int columnSizeForAdditionalOthers = RandomUtils.nextInt(2, 10);
        zeroMatrixForAddition = BigIntMatrix.builder(rowSize, columnSize).putAll(BigInteger.ZERO).build();
        zeroMatrixForMultiplication = BigIntMatrix.builder(columnSize, rowSize).putAll(BigInteger.ZERO).build();
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
            squarematrices.add(mathRandom.nextBigIntMatrix(bound, rowSize, rowSize));
            othersForAddition.add(mathRandom.nextBigIntMatrix(bound, rowSize, columnSize));
            additionalOthersForAddition.add(mathRandom.nextBigIntMatrix(bound, rowSize, columnSize));
            othersForMultiplication.add(mathRandom.nextBigIntMatrix(bound, columnSize, columnSizeForOthers));
            additionalOthersForMultiplication
                    .add(mathRandom.nextBigIntMatrix(bound, columnSizeForOthers, columnSizeForAdditionalOthers));
            vectors.add(mathRandom.nextBigIntVector(bound, columnSize));
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
    public void addShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigInteger expectedEntry = cell.getValue().add(other.entry(rowIndex, columnIndex));
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
        matrices.forEach(it -> {
            assertThat(it.add(zeroMatrixForAddition)).isEqualTo(it);
        });

    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForAddition.subtract(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("subtrahend");

    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final BigInteger expectedEntry = cell.getValue().subtract(other.entry(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.subtract(other)).isExactlyInstanceOf(BigIntMatrix.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(it -> {
            assertThat(it.subtract(zeroMatrixForAddition)).isEqualTo(it);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach(it -> {
            assertThat(it.subtract(it)).isEqualTo(zeroMatrixForAddition);
        });
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroMatrixForMultiplication.multiply(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");

    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize(), other.columnSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    other.columns().forEach((otherColumnIndex, otherColumn) -> {
                        BigInteger entry = BigInteger.ZERO;
                        for (final Entry<Integer, BigInteger> rowEntry : row.entrySet()) {
                            entry = entry.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                        }
                        builder.put(rowIndex, otherColumnIndex, entry);
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
        squarematrices.forEach(it -> {
            assertThat(it.multiply(identityMatrix)).isEqualTo(it);
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
        squarematrices.forEach(matrix -> {
            squarematrices.forEach(other -> {
                squarematrices.forEach(additionalOther -> {
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
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(matrix.rowSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    BigInteger entry = BigInteger.ZERO;
                    for (final Integer columnIndex : matrix.columnIndexes()) {
                        entry = entry.add(row.get(columnIndex).multiply(vector.entry(columnIndex)));
                    }
                    builder.put(entry);
                });
                assertThat(matrix.multiplyVector(vector)).isExactlyInstanceOf(BigIntVector.class)
                        .isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(it -> {
            final BigIntVector zeroVector = BigIntVector.builder(it.columnSize()).putAll(BigInteger.ZERO).build();
            final BigIntVector expected = BigIntVector.builder(it.rowSize()).putAll(BigInteger.ZERO).build();
            assertThat(it.multiplyVector(zeroVector)).isEqualTo(expected);
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
        matrices.forEach(it -> {
            assertThat(it.scalarMultiply(BigInteger.ZERO)).isEqualTo(zeroMatrixForAddition);
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(it -> {
            assertThat(it.scalarMultiply(BigInteger.ONE)).isEqualTo(it);
        });
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(it -> {
            assertThat(it.negate()).isExactlyInstanceOf(BigIntMatrix.class)
                    .isEqualTo(it.scalarMultiply(BigInteger.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate()).isEqualTo(zeroMatrixForAddition);
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
        squarematrices.forEach(matrix -> {
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
        matrices.forEach(it -> {
            assertThat(it.scalarMultiply(BigInteger.ONE.negate())).isEqualTo(it.negate());
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
        squarematrices.forEach(it -> {
            BigInteger expected = BigInteger.ZERO;
            for (final Integer index : it.rowIndexes()) {
                expected = expected.add(it.entry(index, index));
            }
            assertThat(it.trace()).isExactlyInstanceOf(BigInteger.class).isEqualTo(expected);
        });
    }

    @Test
    public void detNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build().det();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(BigIntMatrix.builder(2, 3).putAll(BigInteger.ZERO).build().square()).isFalse();
    }

    @Test
    public void squareOfSquareMatricesShouldBeTrue() {
        assertThat(squarematrices).are(new Condition<>(matrix -> matrix.square(), "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(it -> {
            assertThat(it.square()).isEqualTo(it.rowSize() == it.columnSize());
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
            final Integer rowIndex = RandomUtils.nextInt(1, matrix.rowSize() + 1);
            final Integer columnIndex = RandomUtils.nextInt(1, matrix.columnSize() + 1);
            final BigIntMatrixBuilder builder = BigIntMatrix.builder(matrix.rowSize() - 1, matrix.columnSize() - 1);
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
}
