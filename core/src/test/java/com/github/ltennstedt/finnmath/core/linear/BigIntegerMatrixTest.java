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

import com.github.ltennstedt.finnmath.core.linear.BigIntegerMatrix.BigIntegerMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.BigIntegerVector.BigIntegerVectorBuilder;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.Test;

public final class BigIntegerMatrixTest {
    private final long bound = 10;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final BigIntegerMatrix zeroMatrixForAddition =
        BigIntegerMatrix.builder(rowSize, columnSize).putAll(BigInteger.ZERO).build();
    private final BigIntegerMatrix zeroMatrixForMultiplication =
        BigIntegerMatrix.builder(columnSize, rowSize).putAll(BigInteger.ZERO).build();
    private final BigIntegerMatrix zeroSquareMatrix =
        BigIntegerMatrix.builder(rowSize, rowSize).putAll(BigInteger.ZERO).build();
    private final BigIntegerMatrix identityMatrix = Matrices.buildIdentityBigIntegerMatrix(size);
    private final List<Integer> range = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigIntegerMatrix> matrices =
        mathRandom.nextBigIntegerMatrices(bound, rowSize, columnSize, howMany);
    private final List<BigIntegerMatrix> squareMatrices =
        mathRandom.nextBigIntegerMatrices(bound, rowSize, rowSize, howMany);
    private final List<BigIntegerMatrix> othersForAddition =
        mathRandom.nextBigIntegerMatrices(bound, rowSize, columnSize, howMany);
    private final List<BigIntegerMatrix> additionalOthersForAddition =
        mathRandom.nextBigIntegerMatrices(bound, rowSize, columnSize, howMany);
    private final List<BigIntegerMatrix> othersForMultiplication =
        mathRandom.nextBigIntegerMatrices(bound, columnSize, columnSize, howMany);
    private final List<BigIntegerMatrix> additionalOthersForMultiplication =
        mathRandom.nextBigIntegerMatrices(bound, columnSize, columnSize, howMany);
    private final List<BigIntegerMatrix> fourByFourMatrices = mathRandom.nextBigIntegerMatrices(bound, 4, 4, howMany);
    private final List<BigIntegerMatrix> threeByThreeMatrices = mathRandom.nextBigIntegerMatrices(bound, 3, 3, howMany);
    private final List<BigIntegerMatrix> twoByTwoMatrices = mathRandom.nextBigIntegerMatrices(bound, 2, 2, howMany);
    private final List<BigIntegerMatrix> upperTriangularMatrices =
        mathRandom.nextUpperTriangularBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerMatrix> lowerTriangularMatrices =
        mathRandom.nextLowerTriangularBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerMatrix> triangularMatrices =
        mathRandom.nextTriangularBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerMatrix> diagonalMatrices =
        mathRandom.nextDiagonalBigIntegerMatrices(bound, rowSize, howMany);
    private final List<BigIntegerVector> vectors = mathRandom.nextBigIntegerVectors(bound, columnSize, howMany);
    private final List<BigInteger> scalars = mathRandom.nextBigIntegers(bound, howMany);
    private final List<BigInteger> otherScalars = mathRandom.nextBigIntegers(bound, howMany);
    private final BigIntegerMatrix nonSquareMatrix = matrices.get(0);
    private final List<Integer> rowRange = IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList());
    private final List<Integer> columnRange = IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList());
    private final BigDecimal tolerance = BigDecimal.valueOf(0.001D);

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");

    }

    @Test
    public void addRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntegerMatrix matrix = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerMatrix summand = Matrices.buildZeroBigIntegerMatrix(4, 5);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntegerMatrix matrix = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerMatrix summand = Matrices.buildZeroBigIntegerMatrix(5, 4);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize, columnSize);
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final BigInteger expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
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
            final BigIntegerMatrix minuend = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerMatrix subtrahend = Matrices.buildZeroBigIntegerMatrix(4, 5);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntegerMatrix minuend = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerMatrix subtrahend = Matrices.buildZeroBigIntegerMatrix(5, 4);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final BigInteger expectedEntry = cell.getValue().subtract(other.element(rowIndex, columnIndex));
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
        matrices.forEach(matrix -> assertThat(matrix.subtract(matrix)).isEqualTo(zeroMatrixForAddition));
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForMultiplication.multiply(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");

    }

    @Test
    public void multiplyColumnSizeNotEqualToFactorRowSizeShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigIntegerMatrix matrix = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerMatrix factor = Matrices.buildZeroBigIntegerMatrix(4, 5);
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == factor.rowSize but actual 5 != 4");

    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize, columnSize);
            matrix.rows().forEach((rowIndex, row) -> other.columns().forEach((otherColumnIndex, otherColumn) -> {
                BigInteger element = BigInteger.ZERO;
                for (final Entry<Integer, BigInteger> rowEntry : row.entrySet()) {
                    element = element.add(rowEntry.getValue().multiply(otherColumn.get(rowEntry.getKey())));
                }
                builder.put(rowIndex, otherColumnIndex, element);
            }));
            assertThat(matrix.multiply(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> matrix.multiply(zeroMatrixForMultiplication).cells()
            .forEach(cell -> assertThat(cell.getValue()).isEqualTo(BigInteger.ZERO)));
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
            final BigIntegerMatrix matrix = Matrices.buildZeroBigIntegerMatrix(5, 5);
            final BigIntegerVector vector = Vectors.buildZeroBigIntegerVector(4);
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == vectorSize but actual 5 != 4");
    }

    @Test
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> vectors.forEach(vector -> {
            final BigIntegerVectorBuilder builder = BigIntegerVector.builder(matrix.rowSize());
            matrix.rows().values().forEach(row -> {
                final BigInteger element = matrix.columnIndexes().stream()
                    .map(columnIndex -> row.get(columnIndex).multiply(vector.element(columnIndex)))
                    .reduce(BigInteger::add).get();
                builder.put(element);
            });
            assertThat(matrix.multiplyVector(vector)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            final BigIntegerVector zeroVector = Vectors.buildZeroBigIntegerVector(columnSize);
            final BigIntegerVector expected = Vectors.buildZeroBigIntegerVector(rowSize);
            assertThat(matrix.multiplyVector(zeroVector)).isEqualTo(expected);
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
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize, columnSize);
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
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(BigInteger.ZERO)).isEqualTo(zeroMatrixForAddition));
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(BigInteger.ONE)).isEqualTo(matrix));
    }

    @Test
    public void scalarMultiplyAndMultiplyVectorShouldBeCommutative() {
        matrices.forEach(matrix -> vectors.forEach(
            vector -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).multiplyVector(vector))
                .isEqualTo(matrix.multiplyVector(vector).scalarMultiply(scalar)))));
    }

    @Test
    public void negateShouldSucceed() {
        matrices
            .forEach(matrix -> assertThat(matrix.negate()).isEqualTo(matrix.scalarMultiply(BigInteger.ONE.negate())));
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
        matrices.forEach(matrix -> matrix.add(matrix.negate()).cells()
            .forEach(cell -> assertThat(cell.getValue()).isEqualTo(BigInteger.ZERO)));
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder =
                BigIntegerMatrix.builder(matrix.rowSize(), matrix.columnSize()).putAll(BigInteger.ZERO);
            matrix.rowIndexes().forEach(index -> builder.put(index, index, BigInteger.ONE.negate()));
            assertThat(matrix.multiply(builder.build())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices
            .forEach(matrix -> assertThat(matrix.scalarMultiply(BigInteger.ONE.negate())).isEqualTo(matrix.negate()));
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.trace()).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("expected square matrix but actual 4 x 5");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            final BigInteger expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(BigInteger::add).get();
            assertThat(matrix.trace()).isEqualTo(expected);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void traceShouldBeAdditive() {
        squareMatrices.forEach(matrix -> squareMatrices
            .forEach(other -> assertThat(matrix.add(other).trace()).isEqualTo(matrix.trace().add(other.trace()))));
    }

    @Test
    public void traceShouldBeLinear() {
        squareMatrices.forEach(matrix -> scalars.forEach(
            scalar -> assertThat(matrix.scalarMultiply(scalar).trace()).isEqualTo(scalar.multiply(matrix.trace()))));
    }

    @Test
    public void traceShouldBeIndependentOfTheOrderOfTheFactors() {
        squareMatrices.forEach(matrix -> squareMatrices
            .forEach(other -> assertThat(matrix.multiply(other).trace()).isEqualTo(other.multiply(matrix).trace())));
    }

    @Test
    public void traceShouldBeEqualToTheTraceOfTheTranspose() {
        squareMatrices.forEach(matrix -> assertThat(matrix.trace()).isEqualTo(matrix.transpose().trace()));
    }

    @Test
    public void determinatNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.determinant()).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("expected square matrix but actual 4 x 5");
    }

    @Test
    public void determinantOfFourByFourMatricesShouldSucceed() {
        fourByFourMatrices.forEach(matrix -> {
            BigInteger expected = BigInteger.ZERO;
            for (final List<Integer> permutation : Collections2.permutations(matrix.rowIndexes())) {
                BigInteger product = BigInteger.ONE;
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
                expected = expected.add(BigInteger.ONE.negate().pow(inversions).multiply(product));
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfThreeByThreeMatricesShouldSucceed() {
        threeByThreeMatrices.forEach(matrix -> {
            final BigInteger first = matrix.element(1, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(3, 3));
            final BigInteger second =
                matrix.element(1, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(3, 1));
            final BigInteger third = matrix.element(1, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(3, 2));
            final BigInteger fourth =
                matrix.element(3, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(1, 3));
            final BigInteger fifth = matrix.element(3, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(1, 1));
            final BigInteger sixth = matrix.element(3, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(1, 2));
            final BigInteger expected = first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final BigInteger expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.determinant()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void determinatOfIdentityMatrixShouldBeEqualToOne() {
        assertThat(identityMatrix.determinant()).isEqualTo(BigInteger.ONE);
    }

    @Test
    public void determinatOfTransposeShouldBeEqualToDeterminant() {
        fourByFourMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant()));
        threeByThreeMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant()));
        twoByTwoMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualTo(matrix.determinant()));
    }

    @Test
    public void determinatShouldBeMultiplicative() {
        fourByFourMatrices
            .forEach(matrix -> fourByFourMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualTo(matrix.determinant().multiply(other.determinant()))));
        threeByThreeMatrices
            .forEach(matrix -> threeByThreeMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualTo(matrix.determinant().multiply(other.determinant()))));
        twoByTwoMatrices
            .forEach(matrix -> twoByTwoMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualTo(matrix.determinant().multiply(other.determinant()))));
    }

    @Test
    public void determinatWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        fourByFourMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualTo(scalar.pow(matrix.rowSize()).multiply(matrix.determinant()))));
        threeByThreeMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualTo(scalar.pow(3).multiply(matrix.determinant()))));
        twoByTwoMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualTo(scalar.pow(2).multiply(matrix.determinant()))));
    }

    @Test
    public void determinatOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        triangularMatrices.forEach(matrix -> {
            final BigInteger expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(BigInteger::multiply).get();
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(matrix.columnSize(), matrix.rowSize());
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
            final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(rowSize - 1, columnSize - 1);
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
            final BigInteger expected = matrix.columns().values().asList().stream()
                .map(column -> column.values().stream().map(BigInteger::abs).reduce(BigInteger::add)).map(Optional::get)
                .reduce(BigInteger::max).get();
            assertThat(matrix.maxAbsColumnSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsColumnSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsColumnSumNorm()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void maxAbsColumnSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsColumnSumNorm()).isGreaterThanOrEqualTo(BigInteger.ZERO));
    }

    @Test
    public void maxAbsColumnSumNormShouldBeAbsolutelyHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxAbsColumnSumNorm())
                .isEqualTo(scalar.abs().multiply(matrix.maxAbsColumnSumNorm()))));
    }

    @Test
    public void maxAbsColumnSumNormShouldBeSubadditive() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxAbsColumnSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsColumnSumNorm().add(other.maxAbsColumnSumNorm()))));
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
            final BigInteger expected = matrix.rows().values().asList().stream()
                .map(row -> row.values().stream().map(BigInteger::abs).reduce(BigInteger::add)).map(Optional::get)
                .reduce(BigInteger::max).get();
            assertThat(matrix.maxAbsRowSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsRowSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsRowSumNorm()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void maxAbsRowSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsRowSumNorm()).isGreaterThanOrEqualTo(BigInteger.ZERO));
    }

    @Test
    public void maxAbsRowSumNormShouldBeAbsolutelyHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxAbsRowSumNorm())
                .isEqualTo(scalar.abs().multiply(matrix.maxAbsRowSumNorm()))));
    }

    @Test
    public void maxAbsRowSumNormShouldBeSubadditive() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxAbsRowSumNorm())
                .isLessThanOrEqualTo(matrix.maxAbsRowSumNorm().add(other.maxAbsRowSumNorm()))));
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
            final BigInteger expected =
                matrix.elements().stream().map(element -> element.pow(2)).reduce(BigInteger::add).get();
            assertThat(matrix.frobeniusNormPow2()).isEqualTo(expected);
        });
    }

    @Test
    public void frobeniusNormPow2OfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.frobeniusNormPow2()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void frobeniusNormPow2ShouldBeHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).frobeniusNormPow2())
                .isEqualTo(scalar.pow(2).multiply(matrix.frobeniusNormPow2()))));
    }

    @Test
    public void frobeniusNormShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.frobeniusNorm())
            .isEqualByComparingTo(new SquareRootCalculator().sqrt(matrix.frobeniusNormPow2())));
    }

    @Test
    public void frobeniusNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.frobeniusNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void frobeniusNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).frobeniusNorm())
            .isLessThanOrEqualTo(matrix.frobeniusNorm().add(other.frobeniusNorm()).add(tolerance))));
    }

    @Test
    public void frobeniusNormShouldBeSubmultiplicative() {
        squareMatrices
            .forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.multiply(other).frobeniusNorm())
                .isLessThanOrEqualTo(matrix.frobeniusNorm().multiply(other.frobeniusNorm()).add(tolerance))));
    }

    @Test
    public void frobeniusNormWithPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("precision");
    }

    @Test
    public void frobeniusNormWithPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(BigDecimal.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormWithPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(BigDecimal.ONE))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void frobeniusNormWithPrecisionShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION))
            .isLessThanOrEqualTo(new SquareRootCalculator(SquareRootCalculator.DEFAULT_PRECISION)
                .sqrt(matrix.frobeniusNormPow2()).add(tolerance)));
    }

    @Test
    public void frobeniusNormWithRoundingModeAndScaleTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(-1, RoundingMode.HALF_EVEN))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void frobeniusNormWithScaleAndRoundingModeShouldSucceed() {
        matrices.forEach(matrix -> assertThat(matrix.frobeniusNorm(2, RoundingMode.HALF_EVEN)).isEqualByComparingTo(
            new SquareRootCalculator(2, RoundingMode.HALF_EVEN).sqrt(matrix.frobeniusNormPow2())));
    }

    @Test
    public void frobeniusNormWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(null, 2, RoundingMode.HALF_EVEN))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void frobeniusNormWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(BigDecimal.ZERO, 2, RoundingMode.HALF_EVEN))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void frobeniusNormWithPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroSquareMatrix.frobeniusNorm(BigDecimal.ONE, 2, RoundingMode.HALF_EVEN))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void frobeniusNormWithPrecisionAndScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(
            () -> zeroSquareMatrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION, -1, RoundingMode.HALF_EVEN))
                .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void frobeniusNormWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        matrices.forEach(matrix -> assertThat(
            matrix.frobeniusNorm(SquareRootCalculator.DEFAULT_PRECISION, 2, RoundingMode.HALF_EVEN))
                .isEqualTo(new SquareRootCalculator(SquareRootCalculator.DEFAULT_PRECISION, 2, RoundingMode.HALF_EVEN)
                    .sqrt(matrix.frobeniusNormPow2())));
    }

    @Test
    public void maxNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigInteger expected = matrix.elements().stream().map(BigInteger::abs).reduce(BigInteger::max).get();
            assertThat(matrix.maxNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxNorm()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void maxNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxNorm()).isGreaterThanOrEqualTo(BigInteger.ZERO));
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxNorm())
            .isEqualTo(scalar.abs().multiply(matrix.maxNorm()))));
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(other -> assertThat(matrix.add(other).maxNorm())
            .isLessThanOrEqualTo(matrix.maxNorm().add(other.maxNorm()))));
    }

    @Test
    public void squareOfNonSquareMatrixShouldBeFalse() {
        assertThat(nonSquareMatrix.square()).isFalse();
    }

    @Test
    public void squareOfSquareMatricesShouldBeTrue() {
        assertThat(squareMatrices).are(new Condition<>(BigIntegerMatrix::square, "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(matrix -> assertThat(matrix.square()).isEqualTo(matrix.rowSize() == matrix.columnSize()));
    }

    @Test
    public void triangularNotSquareShouldReturnFalse() {
        final BigIntegerMatrix matrix = BigIntegerMatrix.builder(4, 5).putAll(BigInteger.ZERO).build();
        assertThat(matrix.triangular()).isFalse();
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
    public void diagonalShouldSucceed() {
        diagonalMatrices.forEach(matrix -> assertThat(matrix.diagonal()).isTrue());
    }

    @Test
    public void identityNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.identity()).isFalse();
    }

    @Test
    public void identityZeroMatrixShouldReturnFalse() {
        assertThat(Matrices.buildZeroBigIntegerMatrix(size, size).identity()).isFalse();
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
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigInteger.ONE);
            } else {
                builder.put(rowIndex, columnIndex, BigInteger.ZERO);
            }
        }));
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final BigIntegerMatrixBuilder builder = BigIntegerMatrix.builder(size, size);
        final Integer index = RandomUtils.nextInt(1, 5);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(index) == 0 && columnIndex.compareTo(index) == 0) {
                builder.put(rowIndex, columnIndex, BigInteger.ONE.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigInteger.ONE);
            } else {
                builder.put(rowIndex, columnIndex, BigInteger.ZERO);
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
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> BigIntegerMatrix.builder(0, 1)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> BigIntegerMatrix.builder(1, 0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize > 0 but actual 0");
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
    public void equalsNotSimpleComplexNumberShouldReturnFalse() {
        assertThat(zeroMatrixForAddition.equals(new Object())).isFalse();
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
