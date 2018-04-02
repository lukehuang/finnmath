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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberMatrix.RealComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.RealComplexNumberVector.RealComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.number.RealComplexNumber;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.Test;

public final class RealComplexNumberMatrixTest {
    private final long bound = 10;
    private final int scale = 2;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final RealComplexNumberMatrix zeroMatrixForAddition =
        RealComplexNumberMatrix.builder(rowSize, columnSize).putAll(RealComplexNumber.ZERO).build();
    private final RealComplexNumberMatrix zeroMatrixForMultiplication =
        RealComplexNumberMatrix.builder(columnSize, rowSize).putAll(RealComplexNumber.ZERO).build();
    private final RealComplexNumberMatrix zeroSquareMatrix =
        RealComplexNumberMatrix.builder(rowSize, rowSize).putAll(RealComplexNumber.ZERO).build();
    private final RealComplexNumberMatrix identityMatrix = Matrices.buildIdentityRealComplexNumberMatrix(size);
    private final List<Integer> range = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<RealComplexNumberMatrix> matrices =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<RealComplexNumberMatrix> squareMatrices =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, rowSize, rowSize, howMany);
    private final List<RealComplexNumberMatrix> othersForAddition =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<RealComplexNumberMatrix> additionalOthersForAddition =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<RealComplexNumberMatrix> othersForMultiplication =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, columnSize, columnSize, howMany);
    private final List<RealComplexNumberMatrix> additionalOthersForMultiplication =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, columnSize, columnSize, howMany);
    private final List<RealComplexNumberMatrix> fourByFourMatrices =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, 4, 4, howMany);
    private final List<RealComplexNumberMatrix> threeByThreeMatrices =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, 3, 3, howMany);
    private final List<RealComplexNumberMatrix> twoByTwoMatrices =
        mathRandom.nextRealComplexNumberMatrices(bound, scale, 2, 2, howMany);
    private final List<RealComplexNumberMatrix> upperTriangularMatrices =
        mathRandom.nextUpperTriangularRealComplexNumberMatrices(bound, scale, rowSize, howMany);
    private final List<RealComplexNumberMatrix> lowerTriangularMatrices =
        mathRandom.nextLowerTriangularRealComplexNumberMatrices(bound, scale, rowSize, howMany);
    private final List<RealComplexNumberMatrix> triangularMatrices =
        mathRandom.nextTriangularRealComplexNumberMatrices(bound, scale, rowSize, howMany);
    private final List<RealComplexNumberMatrix> diagonalMatrices =
        mathRandom.nextDiagonalRealComplexNumberMatrices(bound, scale, rowSize, howMany);
    private final List<RealComplexNumberVector> vectors =
        mathRandom.nextRealComplexNumberVectors(bound, scale, columnSize, howMany);
    private final List<RealComplexNumber> scalars = mathRandom.nextRealComplexNumbers(bound, scale, howMany);
    private final List<RealComplexNumber> otherScalars = mathRandom.nextRealComplexNumbers(bound, scale, howMany);
    private final RealComplexNumberMatrix nonSquareMatrix = matrices.get(0);
    private final BigDecimal tolerance = BigDecimal.valueOf(0.001D);
    private final Condition<RealComplexNumber> equalToZero =
        new Condition<>(complexNumber -> complexNumber.isEqualToByComparingParts(RealComplexNumber.ZERO), "equal to 0");

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");

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
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final RealComplexNumber expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
                builder.put(rowIndex, columnIndex, expectedEntry);
            });
            assertThat(matrix.add(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void addShouldBeCommutative() {
        matrices.forEach(
            matrix -> othersForAddition.forEach(other -> assertThat(matrix.add(other)).isEqualTo(other.add(matrix))));
    }

    @Test
    public void addShouldBeAssociative() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> additionalOthersForAddition
            .forEach(additionalOther -> assertThat(matrix.add(other).add(additionalOther))
                .isEqualTo(matrix.add(other.add(additionalOther))))));
    }

    @Test
    public void addZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.add(zeroMatrixForAddition)).isEqualTo(matrix));

    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.subtract(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("subtrahend");

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
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final RealComplexNumberMatrixBuilder builder =
                RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final RealComplexNumber expectedEntry = cell.getValue().subtract(other.element(rowIndex, columnIndex));
                builder.put(rowIndex, columnIndex, expectedEntry);
            });
            assertThat(matrix.subtract(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void subtractZeroMatrixShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.subtract(zeroMatrixForAddition)).isEqualTo(matrix));
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.subtract(matrix).elements())
            .usingElementComparator(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR)
            .isEqualTo(zeroMatrixForAddition.elements()));
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForMultiplication.multiply(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");

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
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> {
            final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
            matrix.rows().forEach((rowIndex, row) -> other.columns().forEach((otherColumnIndex, otherColumn) -> {
                RealComplexNumber element = RealComplexNumber.ZERO;
                for (final Entry<Integer, RealComplexNumber> rowEntry : row.entrySet()) {
                    element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                }
                builder.put(rowIndex, otherColumnIndex, element);
            }));
            assertThat(matrix.multiply(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices
            .forEach(matrix -> assertThat(matrix.multiply(zeroMatrixForMultiplication).elements()).are(equalToZero));
    }

    @Test
    public void multiplyIdentityMatrixShouldBeEqualToSelf() {
        squareMatrices.forEach(matrix -> assertThat(matrix.multiply(identityMatrix)).isEqualTo(matrix));
    }

    @Test
    public void multiplyShouldBeAssociative() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> additionalOthersForMultiplication
            .forEach(additionalOthers -> assertThat(matrix.multiply(other).multiply(additionalOthers))
                .isEqualTo(matrix.multiply(other.multiply(additionalOthers))))));
    }

    @Test
    public void addAndMultiplyShouldBeDistributive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(
            other -> squareMatrices.forEach(additionalOther -> assertThat(matrix.multiply(other.add(additionalOther)))
                .isEqualTo(matrix.multiply(other).add(matrix.multiply(additionalOther))))));
    }

    @Test
    public void multiplyNullVectorShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.multiplyVector(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
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
        matrices.forEach(matrix -> vectors.forEach(vector -> {
            final RealComplexNumberVectorBuilder builder = RealComplexNumberVector.builder(matrix.rowSize());
            matrix.rows().forEach((rowIndex, row) -> {
                RealComplexNumber element = RealComplexNumber.ZERO;
                for (final Integer columnIndex : matrix.columnIndexes()) {
                    element = element.add(row.get(columnIndex).multiply(vector.element(columnIndex)));
                }
                builder.put(element);
            });
            assertThat(matrix.multiplyVector(vector)).isEqualTo(builder.build());
        }));
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
        assertThatThrownBy(() -> zeroMatrixForAddition.scalarMultiply(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> {
            final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(rowSize, columnSize);
            matrix.cells()
                .forEach(cell -> builder.put(cell.getRowKey(), cell.getColumnKey(), scalar.multiply(cell.getValue())));
            assertThat(matrix.scalarMultiply(scalar)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(matrix.scalarMultiply(scalar.multiply(otherScalar)))
                .isEqualTo(matrix.scalarMultiply(otherScalar).scalarMultiply(scalar)))));
    }

    @Test
    public void scalarMultiplyWithTwoMatricesShouldBeAssociative() {
        matrices.forEach(matrix -> othersForMultiplication
            .forEach(other -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).multiply(other))
                .isEqualTo(matrix.multiply(other).scalarMultiply(scalar)))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        matrices.forEach(matrix -> scalars.forEach(
            scalar -> otherScalars.forEach(otherScalar -> assertThat(matrix.scalarMultiply(scalar.add(otherScalar)))
                .isEqualTo(matrix.scalarMultiply(scalar).add(matrix.scalarMultiply(otherScalar))))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoMatricesShouldBeDistributive() {
        matrices.forEach(matrix -> othersForAddition
            .forEach(other -> scalars.forEach(scalar -> assertThat(matrix.add(other).scalarMultiply(scalar))
                .isEqualTo(matrix.scalarMultiply(scalar).add(other.scalarMultiply(scalar))))));
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(RealComplexNumber.ZERO).elements())
            .usingElementComparator(RealComplexNumber.REAL_COMPLEX_NUMBER_COMPARATOR)
            .isEqualTo(zeroMatrixForAddition.elements()));
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(RealComplexNumber.ONE)).isEqualTo(matrix));
    }

    @Test
    public void scalarMultiplyAndMultiplyVectorShouldBeCommutative() {
        matrices.forEach(matrix -> vectors.forEach(
            vector -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).multiplyVector(vector))
                .isEqualTo(matrix.multiplyVector(vector).scalarMultiply(scalar)))));
    }

    @Test
    public void negateShouldSucceed() {
        matrices.forEach(
            matrix -> assertThat(matrix.negate()).isEqualTo(matrix.scalarMultiply(RealComplexNumber.ONE.negate())));
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroMatrixForAddition.negate()).isEqualTo(zeroMatrixForAddition);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.negate().negate()).isEqualTo(matrix));
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.add(matrix.negate()).elements()).are(equalToZero));
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize()).putAll(RealComplexNumber.ZERO);
            matrix.rowIndexes().forEach(index -> builder.put(index, index, RealComplexNumber.ONE.negate()));
            assertThat(matrix.multiply(builder.build())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(
            matrix -> assertThat(matrix.scalarMultiply(RealComplexNumber.ONE.negate())).isEqualTo(matrix.negate()));
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.trace().checkedGet())
            .isExactlyInstanceOf(MatrixNotSquareException.class).hasMessage("expected square matrix but actual 4 x 5");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            final RealComplexNumber expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(RealComplexNumber::add).get();
            assertThat(matrix.trace().get()).isEqualTo(expected);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace().get()).isEqualTo(RealComplexNumber.ZERO);
    }

    @Test
    public void traceShouldBeAdditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).trace().get())
            .isEqualTo(matrix.trace().get().add(other.trace().get()))));
    }

    @Test
    public void traceShouldBeLinear() {
        squareMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).trace().get())
                .isEqualTo(scalar.multiply(matrix.trace().get()))));
    }

    @Test
    public void traceShouldBeIndependentOfTheOrderOfTheFactors() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(
            other -> assertThat(matrix.multiply(other).trace().get()).isEqualTo(other.multiply(matrix).trace().get())));
    }

    @Test
    public void traceShouldBeEqualToTheTraceOfTheTranspose() {
        squareMatrices.forEach(matrix -> assertThat(matrix.trace().get()).isEqualTo(matrix.transpose().trace().get()));
    }

    @Test
    public void determinatNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.determinant().checkedGet())
            .isExactlyInstanceOf(MatrixNotSquareException.class).hasMessage("expected square matrix but actual 4 x 5");
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
                        if (sigma.compareTo(permutation.get(j)) > 0) {
                            inversions++;
                        }
                    }
                    product = product.multiply(matrix.element(sigma, i + 1));
                }
                expected = expected.add(RealComplexNumber.ONE.negate().pow(inversions).multiply(product));
            }
            assertThat(matrix.determinant().get()).isEqualTo(expected);
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
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final RealComplexNumber expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.determinant().get()).isEqualTo(RealComplexNumber.ZERO);
    }

    @Test
    public void determinatOfIdentityMatrixShouldBeEqualToOne() {
        assertThat(identityMatrix.determinant().get()).isEqualTo(RealComplexNumber.ONE);
    }

    @Test
    public void determinatOfTransposeShouldBeEqualToDeterminant() {
        fourByFourMatrices.forEach(
            matrix -> assertThat(matrix.transpose().determinant().get()).isEqualTo(matrix.determinant().get()));
        threeByThreeMatrices.forEach(
            matrix -> assertThat(matrix.transpose().determinant().get()).isEqualTo(matrix.determinant().get()));
        twoByTwoMatrices.forEach(
            matrix -> assertThat(matrix.transpose().determinant().get()).isEqualTo(matrix.determinant().get()));
    }

    @Test
    public void determinatShouldBeMultiplicative() {
        fourByFourMatrices.forEach(
            matrix -> fourByFourMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant().get())
                .isEqualTo(matrix.determinant().get().multiply(other.determinant().get()))));
        threeByThreeMatrices.forEach(
            matrix -> threeByThreeMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant().get())
                .isEqualTo(matrix.determinant().get().multiply(other.determinant().get()))));
        twoByTwoMatrices
            .forEach(matrix -> twoByTwoMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant().get())
                .isEqualTo(matrix.determinant().get().multiply(other.determinant().get()))));
    }

    @Test
    public void determinatWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        fourByFourMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant().get())
                .isEqualTo(scalar.pow(matrix.rowSize()).multiply(matrix.determinant().get()))));
        threeByThreeMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant().get())
                .isEqualTo(scalar.pow(3).multiply(matrix.determinant().get()))));
        twoByTwoMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant().get())
                .isEqualTo(scalar.pow(2).multiply(matrix.determinant().get()))));
    }

    @Test
    public void determinatOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        triangularMatrices.forEach(matrix -> {
            final RealComplexNumber expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(RealComplexNumber::multiply).get();
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                RealComplexNumberMatrix.builder(matrix.columnSize(), matrix.rowSize());
            matrix.cells().forEach(cell -> builder.put(cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
            assertThat(matrix.transpose()).isEqualTo(builder.build());
        });
    }

    @Test
    public void transposeTwiceIsEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.transpose().transpose()).isEqualTo(matrix));
    }

    @Test
    public void transposeShouldBeAdditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(
            other -> assertThat(matrix.add(other).transpose()).isEqualTo(matrix.transpose().add(other.transpose()))));
    }

    @Test
    public void transposeShouldBeLinear() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).transpose())
            .isEqualTo(matrix.transpose().scalarMultiply(scalar))));
    }

    @Test
    public void transposeShouldBeMultiplicativeWithInversedOrder() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.multiply(other).transpose())
            .isEqualTo(other.transpose().multiply(matrix.transpose()))));
    }

    @Test
    public void minorRowIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(null, 1)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("rowIndex");
    }

    @Test
    public void minorColumnIndexNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(1, null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("columnIndex");
    }

    @Test
    public void minorRowIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(0, 1)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowIndex in [1, %s] but actual 0", zeroMatrixForAddition.rowSize());
    }

    @Test
    public void minorRowIndexTooHighShouldThrowException() {
        final Integer invalidRowIndex = zeroMatrixForAddition.rowSize() + 1;
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(invalidRowIndex, 1))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowIndex in [1, %s] but actual %s", zeroMatrixForAddition.rowSize(), invalidRowIndex);
    }

    @Test
    public void minorColumnIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(1, 0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnIndex in [1, %s] but actual 0", zeroMatrixForAddition.columnSize());
    }

    @Test
    public void minorColumnIndexTooHighShouldThrowException() {
        final Integer invalidColumnIndex = zeroMatrixForAddition.columnSize() + 1;
        assertThatThrownBy(() -> zeroMatrixForAddition.minor(1, invalidColumnIndex))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnIndex in [1, %s] but actual %s", zeroMatrixForAddition.columnSize(),
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
                if (rowKey.compareTo(rowIndex) != 0 && columnKey.compareTo(columnIndex) != 0) {
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
            final BigDecimal expected = matrix.columns().values().asList().stream()
                .map(column -> column.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add))
                .map(Optional::get).reduce(BigDecimal::max).get();
            assertThat(matrix.maxAbsColumnSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsColumnSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsColumnSumNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxAbsColumnSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsColumnSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void maxAbsColumnSumNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> {
            final BigDecimal expected = scalar.abs().multiply(matrix.maxAbsColumnSumNorm());
            assertThat(matrix.scalarMultiply(scalar).maxAbsColumnSumNorm()).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        }));
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSubadditive() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxAbsColumnSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsColumnSumNorm().add(other.maxAbsColumnSumNorm()).add(tolerance))));
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSubmultiplicative() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.multiply(other).maxAbsColumnSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsColumnSumNorm().multiply(other.maxAbsColumnSumNorm()))));
    }

    @Test
    public void maxAbsRowSumNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected = matrix.rows().values().asList().stream()
                .map(column -> column.values().stream().map(RealComplexNumber::abs).reduce(BigDecimal::add))
                .map(Optional::get).reduce(BigDecimal::max).get();
            assertThat(matrix.maxAbsRowSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsRowSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsRowSumNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxAbsRowSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsRowSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void maxAbsRowSumNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> {
            final BigDecimal expected = scalar.abs().multiply(matrix.maxAbsRowSumNorm());
            assertThat(matrix.scalarMultiply(scalar).maxAbsRowSumNorm()).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        }));
    }

    @Test
    public void maxAbsRowSumNormShouldBeSubadditive() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxAbsRowSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsRowSumNorm().add(other.maxAbsRowSumNorm()).add(tolerance))));
    }

    @Test
    public void maxAbsRowSumNormShouldBeSubmultiplicative() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.multiply(other).maxAbsRowSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsRowSumNorm().multiply(other.maxAbsRowSumNorm()))));
    }

    @Test
    public void frobeniusNormPow2ShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected =
                matrix.elements().stream().map(RealComplexNumber::absPow2).reduce(BigDecimal::add).get();
            assertThat(matrix.frobeniusNormPow2()).isEqualTo(expected);
        });
    }

    @Test
    public void frobeniusNormPow2OfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.frobeniusNormPow2()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormPow2ShouldBeHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).frobeniusNormPow2())
                .isEqualTo(scalar.absPow2().multiply(matrix.frobeniusNormPow2()))));
    }

    @Test
    public void maxNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected =
                matrix.elements().stream().map(RealComplexNumber::abs).reduce(BigDecimal::max).get();
            assertThat(matrix.maxNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxNorm()).isLessThan(tolerance);
    }

    @Test
    public void maxNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> {
            final BigDecimal expected = scalar.abs().multiply(matrix.maxNorm());
            assertThat(matrix.scalarMultiply(scalar).maxNorm()).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        }));
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxNorm())
            .isLessThanOrEqualTo(matrix.maxNorm().add(other.maxNorm()).add(tolerance))));
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(nonSquareMatrix.square()).isFalse();
    }

    @Test
    public void squareOfSquareMatricesShouldBeTrue() {
        assertThat(squareMatrices).are(new Condition<>(RealComplexNumberMatrix::square, "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(matrix -> assertThat(matrix.square()).isEqualTo(matrix.rowSize() == matrix.columnSize()));
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
                if (rowIndex.compareTo(columnIndex) > 0) {
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
                if (rowIndex.compareTo(columnIndex) < 0) {
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
        triangularMatrices.forEach(matrix -> assertThat(matrix.triangular()).isTrue());
    }

    @Test
    public void upperTriangularNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.upperTriangular()).isFalse();
    }

    @Test
    public void upperTriangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
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
        upperTriangularMatrices.forEach(matrix -> assertThat(matrix.upperTriangular()).isTrue());
    }

    @Test
    public void lowerTriangularNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.lowerTriangular()).isFalse();
    }

    @Test
    public void lowerTriangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final RealComplexNumberMatrixBuilder builder =
                RealComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
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
        lowerTriangularMatrices.forEach(matrix -> assertThat(matrix.lowerTriangular()).isTrue());
    }

    @Test
    public void diagonalNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.diagonal()).isFalse();
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
    public void diagonalShouldSucceed() {
        diagonalMatrices.forEach(matrix -> assertThat(matrix.diagonal()).isTrue());
    }

    @Test
    public void identityNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.identity()).isFalse();
    }

    @Test
    public void identityNotIdentityMatrixShouldReturnFalse() {
        assertThat(zeroSquareMatrix.identity()).isFalse();
    }

    @Test
    public void identityShouldSucceed() {
        assertThat(identityMatrix.identity()).isTrue();
    }

    @Test
    public void invertibleNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleZeroMatrixShouldReturnFalse() {
        assertThat(zeroSquareMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleIdShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ONE);
            } else {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        final Integer index = RandomUtils.nextInt(1, 5);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(index) == 0 && columnIndex.compareTo(index) == 0) {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ONE.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ONE);
            } else {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void symmetricNotquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.symmetric()).isFalse();
    }

    @Test
    public void symmetricNotSymmetricShouldReturnFalse() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final RealComplexNumber element = mathRandom.nextInvertibleRealComplexNumber(bound, scale);
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
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final RealComplexNumber element = mathRandom.nextRealComplexNumber(bound, scale);
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
        assertThat(nonSquareMatrix.skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricNotSymmetricShouldReturnFalse() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final RealComplexNumber element = mathRandom.nextInvertibleRealComplexNumber(bound, scale);
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element);
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final RealComplexNumberMatrixBuilder builder = RealComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final RealComplexNumber element = mathRandom.nextRealComplexNumber(bound, scale);
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, RealComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumberMatrix.builder(0, 1))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> RealComplexNumberMatrix.builder(1, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }
}
