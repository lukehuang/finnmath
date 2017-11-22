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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.linear.RealComplexNumberMatrix.RealComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.linear.RealComplexNumberVector.RealComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.util.MathRandom;
import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.BeforeClass;
import org.junit.Test;

public final class RealComplexNumberMatrixTest {
    private static final int rowSize = 4;
    private static final int columnSize = 5;
    private static final int size = rowSize;
    private static RealComplexNumberMatrix zeroMatrixForAddition =
            RealComplexNumberMatrix.builder(rowSize, columnSize).putAll(RealComplexNumber.ZERO).build();;
    private static RealComplexNumberMatrix zeroMatrixForMultiplication =
            RealComplexNumberMatrix.builder(columnSize, rowSize).putAll(RealComplexNumber.ZERO).build();
    private static RealComplexNumberMatrix zeroSquareMatrix =
            RealComplexNumberMatrix.builder(rowSize, rowSize).putAll(RealComplexNumber.ZERO).build();
    private static final int howMany = 10;
    private static final MathRandom mathRandom = new MathRandom(7);
    private static final long bound = 10;
    private static final int scale = 2;
    private static final List<RealComplexNumberMatrix> matrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> squareMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> othersForAddition = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> additionalOthersForAddition = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> othersForMultiplication = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> additionalOthersForMultiplication = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> fourByFourMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> threeByThreeMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> twoByTwoMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> upperTriangularMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> lowerTriangularMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> triangularMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> threeByThreeTriangularMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> twoByTwoTriangularMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> diagonalMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> symmetricMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberMatrix> skewSymmetricMatrices = new ArrayList<>(howMany);
    private static final List<RealComplexNumberVector> vectors = new ArrayList<>(howMany);
    private static final List<RealComplexNumber> scalars = new ArrayList<>(howMany);
    private static final List<RealComplexNumber> otherScalars = new ArrayList<>(howMany);
    private static final List<Integer> rowRange =
            IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList());
    private static final List<Integer> columnRange =
            IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList());
    private static final List<Integer> range = rowRange;
    private static final BigDecimal tolerance = BigDecimal.valueOf(0.001D);
    private static final Condition<RealComplexNumber> equalToZero =
            new Condition<>(complexNumber -> complexNumber.isEqualToByComparingParts(RealComplexNumber.ZERO), "= 0");
    private static RealComplexNumberMatrix identityMatrix;

    @BeforeClass
    public static void setUp() {
        final RealComplexNumberMatrixBuilder identityMatrixBuilder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    identityMatrixBuilder.put(rowIndex, columnIndex, RealComplexNumber.ONE);
                } else {
                    identityMatrixBuilder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            });
        });
        identityMatrix = identityMatrixBuilder.build();
        for (int i = 0; i < howMany; i++) {
            matrices.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, rowSize, columnSize));
            squareMatrices.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, rowSize, rowSize));
            othersForAddition.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, rowSize, columnSize));
            additionalOthersForAddition.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, rowSize, columnSize));
            othersForMultiplication.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, columnSize, columnSize));
            additionalOthersForMultiplication
                    .add(mathRandom.nextRealComplexNumberMatrix(bound, scale, columnSize, columnSize));
            fourByFourMatrices.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, 4, 4));
            threeByThreeMatrices.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, 3, 3));
            twoByTwoMatrices.add(mathRandom.nextRealComplexNumberMatrix(bound, scale, 2, 2));
            vectors.add(mathRandom.nextRealComplexNumberVector(bound, scale, columnSize));
            upperTriangularMatrices.add(mathRandom.nextUpperTriangularRealComplexNumberMatrix(bound, scale, rowSize));
            lowerTriangularMatrices.add(mathRandom.nextLowerTriangularRealComplexNumberMatrix(bound, scale, rowSize));
            triangularMatrices.add(mathRandom.nextTriangularRealComplexNumberMatrix(bound, scale, rowSize));
            threeByThreeTriangularMatrices.add(mathRandom.nextTriangularRealComplexNumberMatrix(bound, scale, 3));
            twoByTwoTriangularMatrices.add(mathRandom.nextTriangularRealComplexNumberMatrix(bound, scale, 2));
            diagonalMatrices.add(mathRandom.nextDiagonalRealComplexNumberMatrix(bound, scale, rowSize));
            symmetricMatrices.add(mathRandom.nextSymmetricRealComplexNumberMatrix(bound, scale, rowSize));
            skewSymmetricMatrices.add(mathRandom.nextSkewSymmetricRealComplexNumberMatrix(bound, scale, rowSize));
            scalars.add(mathRandom.nextRealComplexNumber(bound, scale));
            otherScalars.add(mathRandom.nextRealComplexNumber(bound, scale));
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
            final RealComplexNumberMatrix matrix =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberMatrix summand =
                    RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final RealComplexNumberMatrix matrix =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberMatrix summand =
                    RealComplexNumberMatrix.builder(5, 4).putAll(RealComplexNumber.ZERO).build();
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final RealComplexNumber expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.add(other)).isEqualTo(builder.build());
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
                additionalOthersForAddition.forEach(additionalOther -> {
                    assertThat(matrix.add(other).add(additionalOther))
                            .isEqualTo(matrix.add(other.add(additionalOther)));
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
            final RealComplexNumberMatrix minuend =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberMatrix subtrahend =
                    RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final RealComplexNumberMatrix minuend =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberMatrix subtrahend =
                    RealComplexNumberMatrix.builder(5, 4).putAll(RealComplexNumber.ZERO).build();
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForAddition.forEach(other -> {
                final RealComplexNumberMatrixBuilder builder =
                        RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
                matrix.cells().forEach(cell -> {
                    final Integer rowIndex = cell.getRowKey();
                    final Integer columnIndex = cell.getColumnKey();
                    final RealComplexNumber expectedEntry =
                            cell.getValue().subtract(other.element(rowIndex, columnIndex));
                    builder.put(rowIndex, columnIndex, expectedEntry);
                });
                assertThat(matrix.subtract(other)).isEqualTo(builder.build());
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
            assertThat(matrix.subtract(matrix).elements())
                    .usingElementComparator(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR)
                    .isEqualTo(zeroMatrixForAddition.elements());
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
            final RealComplexNumberMatrix matrix =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberMatrix factor =
                    RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == factor.rowSize but actual 5 != 4");

    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> {
            othersForMultiplication.forEach(other -> {
                final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
                matrix.rows().forEach((rowIndex, row) -> {
                    other.columns().forEach((otherColumnIndex, otherColumn) -> {
                        RealComplexNumber element = RealComplexNumber.ZERO;
                        for (final Entry<Integer, RealComplexNumber> rowEntry : row.entrySet()) {
                            element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                        }
                        builder.put(rowIndex, otherColumnIndex, element);
                    });
                });
                assertThat(matrix.multiply(other)).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            assertThat(matrix.multiply(zeroMatrixForMultiplication).elements()).are(equalToZero);
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
            final RealComplexNumberMatrix matrix =
                    RealComplexNumberMatrix.builder(5, 5).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberVector vector =
                    RealComplexNumberVector.builder(4).putAll(RealComplexNumber.ZERO).build();
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected columnSize == vectorSize but actual 5 != 4");
    }

    @Test
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> {
            vectors.forEach(vector -> {
                final RealComplexNumberVectorBuilder builder = RealComplexNumberVector.builder(matrix.rowSize());
                matrix.rows().forEach((rowIndex, row) -> {
                    RealComplexNumber element = RealComplexNumber.ZERO;
                    for (final Integer columnIndex : matrix.columnIndexes()) {
                        element = element.add(row.get(columnIndex).multiply(vector.element(columnIndex)));
                    }
                    builder.put(element);
                });
                assertThat(matrix.multiplyVector(vector)).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            final RealComplexNumberVector zeroVector =
                    RealComplexNumberVector.builder(matrix.columnSize()).putAll(RealComplexNumber.ZERO).build();
            final RealComplexNumberVector expected =
                    RealComplexNumberVector.builder(matrix.rowSize()).putAll(RealComplexNumber.ZERO).build();
            assertThat(matrix.multiplyVector(zeroVector).elements())
                    .usingElementComparator(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR)
                    .isEqualTo(expected.elements());
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
                final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
                matrix.cells().forEach(cell -> {
                    builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue()));
                });
                assertThat(matrix.scalarMultiply(scalar)).isEqualTo(builder.build());
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
            assertThat(matrix.scalarMultiply(RealComplexNumber.ZERO).elements())
                    .usingElementComparator(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR)
                    .isEqualTo(zeroMatrixForAddition.elements());
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(RealComplexNumber.ONE)).isEqualTo(matrix);
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
            assertThat(matrix.negate()).isEqualTo(matrix.scalarMultiply(RealComplexNumber.ONE.negate()));
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
            assertThat(matrix.add(matrix.negate()).elements()).are(equalToZero);
        });
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix
                    .builder(matrix.rowSize(), matrix.columnSize()).putAll(RealComplexNumber.ZERO);
            matrix.rowIndexes().forEach(index -> {
                builder.put(index, index, RealComplexNumber.ONE.negate());
            });
            assertThat(matrix.multiply(builder.build())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(matrix -> {
            assertThat(matrix.scalarMultiply(RealComplexNumber.ONE.negate())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            RealComplexNumberMatrix.builder(2, 3).putAll(RealComplexNumber.ZERO).build().trace();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            RealComplexNumber expected = RealComplexNumber.ZERO;
            for (final Integer index : matrix.rowIndexes()) {
                expected = expected.add(matrix.element(index, index));
            }
            assertThat(matrix.trace()).isEqualTo(expected);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace()).isEqualTo(RealComplexNumber.ZERO);
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
    public void determinatNotSquareShouldThrowException() {
        assertThatThrownBy(() -> {
            RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build().determinant();
        }).isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 4 x 5");
    }

    @Test
    public void determinantOfFourByFourMatricesShouldSucceed() {
        fourByFourMatrices.forEach(matrix -> {
            RealComplexNumber expected = RealComplexNumber.ZERO;
            for (final List<Integer> permutation : Collections2.permutations(matrix.rowIndexes())) {
                RealComplexNumber product = RealComplexNumber.ONE;
                int inversions = 0;
                for (int i = 0; i < 4; i++) {
                    final Integer sigma = permutation.get(i);
                    for (int j = i + 1; j < 4; j++) {
                        if (sigma > permutation.get(j)) {
                            inversions++;
                        }
                    }
                    product = product.multiply(matrix.element(sigma, i + 1));
                }
                expected = expected.add(RealComplexNumber.ONE.negate().pow(inversions).multiply(product));
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfThreeByThreeMatricesShouldSucceed() {
        threeByThreeMatrices.forEach(matrix -> {
            final RealComplexNumber first =
                    matrix.element(1, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(3, 3));
            final RealComplexNumber second =
                    matrix.element(1, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(3, 1));
            final RealComplexNumber third =
                    matrix.element(1, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(3, 2));
            final RealComplexNumber fourth =
                    matrix.element(3, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(1, 3));
            final RealComplexNumber fifth =
                    matrix.element(3, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(1, 1));
            final RealComplexNumber sixth =
                    matrix.element(3, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(1, 2));
            final RealComplexNumber expected =
                    first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final RealComplexNumber expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                    .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.determinant()).isEqualTo(RealComplexNumber.ZERO);
    }

    @Test
    public void determinatOfIdentityMatrixShouldBeEqualToOne() {
        assertThat(identityMatrix.determinant()).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void determinatOfTransposeShouldBeEqualToDeterminant() {
        fourByFourMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
        threeByThreeMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
        twoByTwoMatrices.forEach(matrix -> {
            assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant());
        });
    }

    @Test
    public void determinatShouldBeMultiplicative() {
        fourByFourMatrices.forEach(matrix -> {
            fourByFourMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).determinant())
                        .isEqualTo(matrix.determinant().multiply(other.determinant()));
            });
        });
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
    }

    @Test
    public void determinatWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        fourByFourMatrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).determinant())
                        .isEqualTo(scalar.pow(matrix.rowSize()).multiply(matrix.determinant()));
            });
        });
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
    }

    @Test
    public void determinatOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        triangularMatrices.forEach(matrix -> {
            RealComplexNumber expected = RealComplexNumber.ONE;
            for (final Cell<Integer, Integer, RealComplexNumber> cell : matrix.cells()) {
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
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.columnSize(), matrix.rowSize());
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
            final Integer rowIndex = RandomUtils.nextInt(1, rowSize + 1);
            final Integer columnIndex = RandomUtils.nextInt(1, columnSize + 1);
            final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize - 1, columnSize - 1);
            matrix.cells().forEach(cell -> {
                final Integer rowKey = cell.getRowKey();
                final Integer columnKey = cell.getColumnKey();
                if (!rowKey.equals(rowIndex) && !columnKey.equals(columnIndex)) {
                    final Integer newRowIndex = rowKey.compareTo(rowIndex) > 0 ? rowKey - 1 : rowKey;
                    final Integer newColumnIndex = columnKey.compareTo(columnIndex) > 0 ? columnKey - 1 : columnKey;
                    builder.put(newRowIndex, newColumnIndex, cell.getValue());
                }
            });
            assertThat(matrix.minor(rowIndex, columnIndex)).isEqualTo(builder.build());
        });
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final Map<Integer, RealComplexNumber> column : matrix.columns().values().asList()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (final RealComplexNumber element : column.values()) {
                    sum = sum.add(element.abs());
                }
                expected = expected.max(sum);
            }
            assertThat(matrix.maxAbsColumnSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsColumnSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsColumnSumNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxAbsColumnSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> {
            assertThat(matrix.maxAbsColumnSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void maxAbsColumnSumNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                final BigDecimal expected = scalar.abs().multiply(matrix.maxAbsColumnSumNorm());
                assertThat(matrix.scalarMultiply(scalar).maxAbsColumnSumNorm()).isBetween(expected.subtract(tolerance),
                        expected.add(tolerance));
            });
        });
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).maxAbsColumnSumNorm()).isLessThanOrEqualTo(
                        matrix.maxAbsColumnSumNorm().add(other.maxAbsColumnSumNorm()).add(tolerance));
            });
        });
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSubmultiplicative() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).maxAbsColumnSumNorm())
                        .isLessThanOrEqualTo(matrix.maxAbsColumnSumNorm().multiply(other.maxAbsColumnSumNorm()));
            });
        });
    }

    @Test
    public void maxAbsRowSumNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final Map<Integer, RealComplexNumber> row : matrix.rows().values().asList()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (final RealComplexNumber element : row.values()) {
                    sum = sum.add(element.abs());
                }
                expected = expected.max(sum);
            }
            assertThat(matrix.maxAbsRowSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsRowSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsRowSumNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxAbsRowSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> {
            assertThat(matrix.maxAbsRowSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void maxAbsRowSumNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                final BigDecimal expected = scalar.abs().multiply(matrix.maxAbsRowSumNorm());
                assertThat(matrix.scalarMultiply(scalar).maxAbsRowSumNorm()).isBetween(expected.subtract(tolerance),
                        expected.add(tolerance));
            });
        });
    }

    @Test
    public void maxAbsRowSumNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).maxAbsRowSumNorm())
                        .isLessThanOrEqualTo(matrix.maxAbsRowSumNorm().add(other.maxAbsRowSumNorm()).add(tolerance));
            });
        });
    }

    @Test
    public void maxAbsRowSumNormShouldBeSubmultiplicative() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).maxAbsRowSumNorm())
                        .isLessThanOrEqualTo(matrix.maxAbsRowSumNorm().multiply(other.maxAbsRowSumNorm()));
            });
        });
    }

    @Test
    public void frobeniusNormPow2ShouldBeSucceed() {
        matrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final RealComplexNumber element : matrix.elements()) {
                expected = expected.add(element.absPow2());
            }
            assertThat(matrix.frobeniusNormPow2()).isEqualTo(expected);
        });
    }

    @Test
    public void frobeniusNormPow2OfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.frobeniusNormPow2()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormPow2ShouldBeHomogeneous() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                assertThat(matrix.scalarMultiply(scalar).frobeniusNormPow2())
                        .isEqualTo(scalar.absPow2().multiply(matrix.frobeniusNormPow2()));
            });
        });
    }

    @Test
    public void frobeniusNormShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.frobeniusNorm())
                    .isEqualByComparingTo(new SquareRootCalculator().sqrt(matrix.frobeniusNormPow2()));
        });
    }

    @Test
    public void frobeniusNormShouldBePositiveValued() {
        matrices.forEach(matrix -> {
            assertThat(matrix.frobeniusNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void frobeniusNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).frobeniusNorm())
                        .isLessThanOrEqualTo(matrix.frobeniusNorm().add(other.frobeniusNorm()).add(tolerance));
            });
        });
    }

    @Test
    public void frobeniusNormShouldBeSubmultiplicative() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.multiply(other).frobeniusNorm())
                        .isLessThanOrEqualTo(matrix.frobeniusNorm().multiply(other.frobeniusNorm()).add(tolerance));
            });
        });
    }

    @Test
    public void frobeniusNormWithPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void frobeniusNormWithPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormWithPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ONE);
    }

    @Test
    public void frobeniusNormWithPrecisionShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION))
                    .isLessThanOrEqualTo(new SquareRootCalculator(SquareRootCalculator.DEFAULT_PRECISION)
                            .sqrt(matrix.frobeniusNormPow2()).add(tolerance));
        });
    }

    @Test
    public void frobeniusNormWithRoundingModeAndScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(-1, RoundingMode.HALF_EVEN);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void frobeniusNormWithScaleAndRoundingModeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.frobeniusNorm(2, RoundingMode.HALF_EVEN)).isEqualByComparingTo(
                    new SquareRootCalculator(2, RoundingMode.HALF_EVEN).sqrt(matrix.frobeniusNormPow2()));
        });
    }

    @Test
    public void frobeniusNormWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(null, 2, RoundingMode.HALF_EVEN);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void frobeniusNormWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(BigDecimal.ZERO, 2, RoundingMode.HALF_EVEN);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(BigDecimal.ONE, 2, RoundingMode.HALF_EVEN);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ONE);
    }

    @Test
    public void frobeniusNormWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION, -1, RoundingMode.HALF_EVEN);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void frobeniusNormWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION, 2, RoundingMode.HALF_EVEN))
                    .isEqualTo(
                            new SquareRootCalculator(SquareRootCalculator.DEFAULT_PRECISION, 2, RoundingMode.HALF_EVEN)
                                    .sqrt(matrix.frobeniusNormPow2()));
        });
    }

    @Test
    public void maxNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final RealComplexNumber element : matrix.elements()) {
                expected = expected.max(element.abs());
            }
            assertThat(matrix.maxNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxNormShouldBePositiveValued() {
        matrices.forEach(matrix -> {
            assertThat(matrix.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> {
            scalars.forEach(scalar -> {
                final BigDecimal expected = scalar.abs().multiply(matrix.maxNorm());
                assertThat(matrix.scalarMultiply(scalar).maxNorm()).isBetween(expected.subtract(tolerance),
                        expected.add(tolerance));
            });
        });
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> {
            squareMatrices.forEach(other -> {
                assertThat(matrix.add(other).maxNorm())
                        .isLessThanOrEqualTo(matrix.maxNorm().add(other.maxNorm()).add(tolerance));
            });
        });
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(RealComplexNumberMatrix.builder(2, 3).putAll(RealComplexNumber.ZERO).build().square()).isFalse();
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.triangular()).isFalse();
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleRealComplexNumber(bound, scale));
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
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleRealComplexNumber(bound, scale));
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.upperTriangular()).isFalse();
    }

    @Test
    public void upperTriangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex > columnIndex) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleRealComplexNumber(bound, scale));
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.lowerTriangular()).isFalse();
    }

    @Test
    public void lowerTriangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleRealComplexNumber(bound, scale));
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.diagonal()).isFalse();
    }

    @Test
    public void diagonalNotNotDiagonalShouldReturnFalse() {
        diagonalMatrices.forEach(matrix -> {
            final Integer rowIndex = RandomUtils.nextInt(2, rowSize);
            final Integer columnIndex = RandomUtils.nextInt(1, rowIndex);
            final RealComplexNumber element = mathRandom.nextInvertibleRealComplexNumber(bound, scale);
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(rowSize, matrix.columnSize());
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.identity()).isFalse();
    }

    @Test
    public void identityNotDiagonalShouldReturnFalse() {
        assertThat(RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build().identity()).isFalse();
    }

    @Test
    public void identityNotIdentityMatrixShouldReturnFalse() {
        assertThat(RealComplexNumberMatrix.builder(4, 4).putAll(RealComplexNumber.ZERO).build().identity()).isFalse();
    }

    @Test
    public void identityShouldSucceed() {
        assertThat(identityMatrix.identity()).isTrue();
    }

    @Test
    public void invertibleNotSquareShouldReturnFalse() {
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 4).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.invertible()).isFalse();
    }

    @Test
    public void invertibleZeroMatrixShouldReturnFalse() {
        final RealComplexNumberMatrix zeroMatrix =
                RealComplexNumberMatrix.builder(4, 4).putAll(RealComplexNumber.ZERO).build();
        assertThat(zeroMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleIdShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        final Integer index = RandomUtils.nextInt(1, 5);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                if (rowIndex.equals(index) && columnIndex.equals(index)) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ONE.negate());
                } else if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ONE);
                } else {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            });
        });
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void symmetricNotquareShouldReturnFalse() {
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.symmetric()).isFalse();
    }

    @Test
    public void symmetricNotSymmetricShouldReturnFalse() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                final RealComplexNumber element = mathRandom.nextInvertibleRealComplexNumber(bound, scale);
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
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                final RealComplexNumber element = mathRandom.nextRealComplexNumber(bound, scale);
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
        final RealComplexNumberMatrix matrix =
                RealComplexNumberMatrix.builder(4, 5).putAll(RealComplexNumber.ZERO).build();
        assertThat(matrix.skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricNotSymmetricShouldReturnFalse() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final RealComplexNumber element = mathRandom.nextInvertibleRealComplexNumber(bound, scale);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element);
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(4, 4);
        range.forEach(rowIndex -> {
            range.forEach(columnIndex -> {
                if (rowIndex < columnIndex) {
                    final RealComplexNumber element = mathRandom.nextRealComplexNumber(bound, scale);
                    builder.put(rowIndex, columnIndex, element);
                    builder.put(columnIndex, rowIndex, element.negate());
                }
                if (rowIndex.equals(columnIndex)) {
                    builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
                }
            });
        });
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void rowIndexesShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.rowIndexes()).isEqualTo(matrix.getTable().rowKeySet());
        });
    }

    @Test
    public void columnIndexesShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.columnIndexes()).isEqualTo(matrix.getTable().columnKeySet());
        });
    }

    @Test
    public void elementRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.element(null, 1);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("rowIndex");
    }

    @Test
    public void elementRowIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.element(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row index in [1, %s] but actual 0",
                zeroSquareMatrix.rowSize());
    }

    @Test
    public void elementColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.element(1, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("columnIndex");
    }

    @Test
    public void elementColumnIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.element(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected column index in [1, %s] but actual 0", zeroSquareMatrix.columnSize());
    }

    @Test
    public void elementShouldSucceed() {
        matrices.forEach(matrix -> {
            rowRange.forEach(rowIndex -> {
                columnRange.forEach(columnIndex -> {
                    assertThat(matrix.element(rowIndex, columnIndex))
                            .isEqualTo(matrix.getTable().get(rowIndex, columnIndex));
                });
            });
        });
    }

    @Test
    public void cellsShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.cells()).isEqualTo(matrix.getTable().cellSet());
        });
    }

    @Test
    public void rowRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.row(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("rowIndex");
    }

    @Test
    public void rowRowIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.row(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row index in [1, %s] but actual 0",
                zeroSquareMatrix.rowSize());
    }

    @Test
    public void rowShouldSucceed() {
        matrices.forEach(matrix -> {
            IntStream.rangeClosed(1, matrix.rowSize()).boxed().collect(Collectors.toList()).forEach(rowIndex -> {
                assertThat(matrix.row(rowIndex)).isEqualTo(matrix.getTable().row(rowIndex));
            });
        });
    }

    @Test
    public void columnColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.column(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("columnIndex");
    }

    @Test
    public void columnColumnIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroSquareMatrix.column(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected column index in [1, %s] but actual 0", zeroSquareMatrix.columnSize());
    }

    @Test
    public void columnShouldSucceed() {
        matrices.forEach(matrix -> {
            columnRange.forEach(columnIndex -> {
                assertThat(matrix.column(columnIndex)).isEqualTo(matrix.getTable().column(columnIndex));
            });
        });
    }

    @Test
    public void rowsShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.rows()).isEqualTo(matrix.getTable().rowMap());
        });
    }

    @Test
    public void columnsShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.columns()).isEqualTo(matrix.getTable().columnMap());
        });
    }

    @Test
    public void elementsShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.elements()).isEqualTo(matrix.getTable().values());
        });
    }

    @Test
    public void sizeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.size()).isEqualTo(Long.valueOf(matrix.getTable().rowKeySet().size())
                    * Long.valueOf(matrix.getTable().columnKeySet().size()));
        });
    }

    @Test
    public void rowSizeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.rowSize()).isEqualTo(matrix.getTable().rowKeySet().size());
        });
    }

    @Test
    public void columnSizeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.columnSize()).isEqualTo(matrix.getTable().columnKeySet().size());
        });
    }

    @Test
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            RealComplexNumberMatrix.builder(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            RealComplexNumberMatrix.builder(1, 0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.hashCode()).isEqualTo(Objects.hash(matrix.getTable()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        matrices.forEach(matrix -> {
            assertThat(matrix.equals(matrix)).isTrue();
        });
    }

    @Test
    public void equalsNotRealComplexNumberShouldReturnFalse() {
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
            final RealComplexNumberMatrixBuilder builder =
                    RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                builder.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
            });
            assertThat(matrix.equals(builder.build())).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        matrices.forEach(matrix -> {
            assertThat(matrix.toString())
                    .isEqualTo(MoreObjects.toStringHelper(matrix).add("table", matrix.getTable()).toString());
        });
    }
}
