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

import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberMatrix.SimpleComplexNumberMatrixBuilder;
import com.github.ltennstedt.finnmath.core.linear.SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder;
import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.junit.Test;

public final class SimpleComplexNumberMatrixTest {
    private final long bound = 10;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final SimpleComplexNumberMatrix zeroMatrixForAddition =
        SimpleComplexNumberMatrix.builder(rowSize, columnSize).putAll(SimpleComplexNumber.ZERO).build();
    private final SimpleComplexNumberMatrix zeroMatrixForMultiplication =
        SimpleComplexNumberMatrix.builder(columnSize, rowSize).putAll(SimpleComplexNumber.ZERO).build();
    private final SimpleComplexNumberMatrix zeroSquareMatrix =
        SimpleComplexNumberMatrix.builder(rowSize, rowSize).putAll(SimpleComplexNumber.ZERO).build();
    private final SimpleComplexNumberMatrix identityMatrix = Matrices.buildIdentitySimpleComplexNumberMatrix(size);
    private final List<Integer> range = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<SimpleComplexNumberMatrix> matrices =
        mathRandom.nextSimpleComplexNumberMatrices(bound, rowSize, columnSize, howMany);
    private final List<SimpleComplexNumberMatrix> squareMatrices =
        mathRandom.nextSimpleComplexNumberMatrices(bound, rowSize, rowSize, howMany);
    private final List<SimpleComplexNumberMatrix> othersForAddition =
        mathRandom.nextSimpleComplexNumberMatrices(bound, rowSize, columnSize, howMany);
    private final List<SimpleComplexNumberMatrix> additionalOthersForAddition =
        mathRandom.nextSimpleComplexNumberMatrices(bound, rowSize, columnSize, howMany);
    private final List<SimpleComplexNumberMatrix> othersForMultiplication =
        mathRandom.nextSimpleComplexNumberMatrices(bound, columnSize, columnSize, howMany);
    private final List<SimpleComplexNumberMatrix> additionalOthersForMultiplication =
        mathRandom.nextSimpleComplexNumberMatrices(bound, columnSize, columnSize, howMany);
    private final List<SimpleComplexNumberMatrix> fourByFourMatrices =
        mathRandom.nextSimpleComplexNumberMatrices(bound, 4, 4, howMany);
    private final List<SimpleComplexNumberMatrix> threeByThreeMatrices =
        mathRandom.nextSimpleComplexNumberMatrices(bound, 3, 3, howMany);
    private final List<SimpleComplexNumberMatrix> twoByTwoMatrices =
        mathRandom.nextSimpleComplexNumberMatrices(bound, 2, 2, howMany);
    private final List<SimpleComplexNumberMatrix> upperTriangularMatrices =
        mathRandom.nextUpperTriangularSimpleComplexNumberMatrices(bound, rowSize, howMany);
    private final List<SimpleComplexNumberMatrix> lowerTriangularMatrices =
        mathRandom.nextLowerTriangularSimpleComplexNumberMatrices(bound, rowSize, howMany);
    private final List<SimpleComplexNumberMatrix> triangularMatrices =
        mathRandom.nextTriangularSimpleComplexNumberMatrices(bound, rowSize, howMany);
    private final List<SimpleComplexNumberMatrix> diagonalMatrices =
        mathRandom.nextDiagonalSimpleComplexNumberMatrices(bound, rowSize, howMany);
    private final List<SimpleComplexNumberVector> vectors =
        mathRandom.nextSimpleComplexNumberVectors(bound, columnSize, howMany);
    private final List<SimpleComplexNumber> scalars = mathRandom.nextSimpleComplexNumbers(bound, howMany);
    private final List<SimpleComplexNumber> otherScalars = mathRandom.nextSimpleComplexNumbers(bound, howMany);
    private final SimpleComplexNumberMatrix nonSquareMatrix = matrices.get(0);
    private final BigDecimal tolerance = BigDecimal.valueOf(0.001D);

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");

    }

    @Test
    public void addRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final SimpleComplexNumberMatrix matrix = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberMatrix summand = Matrices.buildZeroSimpleComplexNumberMatrix(4, 5);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final SimpleComplexNumberMatrix matrix = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberMatrix summand = Matrices.buildZeroSimpleComplexNumberMatrix(5, 4);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void addShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(rowSize, columnSize);
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final SimpleComplexNumber expectedEntry = cell.getValue().add(other.element(rowIndex, columnIndex));
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
            final SimpleComplexNumberMatrix minuend = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberMatrix subtrahend = Matrices.buildZeroSimpleComplexNumberMatrix(4, 5);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final SimpleComplexNumberMatrix minuend = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberMatrix subtrahend = Matrices.buildZeroSimpleComplexNumberMatrix(5, 4);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void subtractShouldSucceed() {
        matrices.forEach(matrix -> othersForAddition.forEach(other -> {
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                final SimpleComplexNumber expectedEntry =
                    cell.getValue().subtract(other.element(rowIndex, columnIndex));
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
            final SimpleComplexNumberMatrix matrix = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberMatrix factor = Matrices.buildZeroSimpleComplexNumberMatrix(4, 5);
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == factor.rowSize but actual 5 != 4");

    }

    @Test
    public void multiplyShouldSucceed() {
        matrices.forEach(matrix -> othersForMultiplication.forEach(other -> {
            final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(rowSize, columnSize);
            matrix.rows().forEach((rowIndex, row) -> other.columns().forEach((otherColumnIndex, otherColumn) -> {
                SimpleComplexNumber element = SimpleComplexNumber.ZERO;
                for (final Entry<Integer, SimpleComplexNumber> rowEntry : row.entrySet()) {
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
            .forEach(cell -> assertThat(cell.getValue()).isEqualTo(SimpleComplexNumber.ZERO)));
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
            final SimpleComplexNumberMatrix matrix = Matrices.buildZeroSimpleComplexNumberMatrix(5, 5);
            final SimpleComplexNumberVector vector = Vectors.buildZeroSimpleComplexNumberVector(4);
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == vectorSize but actual 5 != 4");
    }

    @Test
    public void multiplyVectorShouldSucceed() {
        matrices.forEach(matrix -> vectors.forEach(vector -> {
            final SimpleComplexNumberVectorBuilder builder = SimpleComplexNumberVector.builder(matrix.rowSize());
            matrix.rows().values().forEach(row -> {
                final SimpleComplexNumber element = matrix.columnIndexes().stream()
                    .map(columnIndex -> row.get(columnIndex).multiply(vector.element(columnIndex)))
                    .reduce(SimpleComplexNumber::add).get();
                builder.put(element);
            });
            assertThat(matrix.multiplyVector(vector)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void multiplyZeroVectorShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> {
            final SimpleComplexNumberVector zeroVector = Vectors.buildZeroSimpleComplexNumberVector(columnSize);
            final SimpleComplexNumberVector expected = Vectors.buildZeroSimpleComplexNumberVector(rowSize);
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
            final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(rowSize, columnSize);
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
        matrices.forEach(
            matrix -> assertThat(matrix.scalarMultiply(SimpleComplexNumber.ZERO)).isEqualTo(zeroMatrixForAddition));
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(SimpleComplexNumber.ONE)).isEqualTo(matrix));
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
            matrix -> assertThat(matrix.negate()).isEqualTo(matrix.scalarMultiply(SimpleComplexNumber.ONE.negate())));
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
            .forEach(cell -> assertThat(cell.getValue()).isEqualTo(SimpleComplexNumber.ZERO)));
    }

    @Test
    public void multiplyNegativeIdentityMatrixShouldBeEqualToNegated() {
        squareMatrices.forEach(matrix -> {
            final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix
                .builder(matrix.rowSize(), matrix.columnSize()).putAll(SimpleComplexNumber.ZERO);
            matrix.rowIndexes().forEach(index -> builder.put(index, index, SimpleComplexNumber.ONE.negate()));
            assertThat(matrix.multiply(builder.build())).isEqualTo(matrix.negate());
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        matrices.forEach(
            matrix -> assertThat(matrix.scalarMultiply(SimpleComplexNumber.ONE.negate())).isEqualTo(matrix.negate()));
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> Matrices.buildZeroSimpleComplexNumberMatrix(2, 3).trace())
            .isExactlyInstanceOf(IllegalStateException.class).hasMessage("expected square matrix but actual 2 x 3");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            final SimpleComplexNumber expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(SimpleComplexNumber::add).get();
            assertThat(matrix.trace()).isEqualTo(expected);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace()).isEqualTo(SimpleComplexNumber.ZERO);
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
        assertThatThrownBy(() -> SimpleComplexNumberMatrix.builder(4, 5).putAll(SimpleComplexNumber.ZERO).build()
            .determinant().checkedGet()).isExactlyInstanceOf(MatrixNotSquareException.class)
                .hasMessage("expected square matrix but actual 4 x 5");
    }

    @Test
    public void determinantOfFourByFourMatricesShouldSucceed() {
        fourByFourMatrices.forEach(matrix -> {
            SimpleComplexNumber expected = SimpleComplexNumber.ZERO;
            for (final List<Integer> permutation : Collections2.permutations(matrix.rowIndexes())) {
                SimpleComplexNumber product = SimpleComplexNumber.ONE;
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
                expected = expected.add(SimpleComplexNumber.ONE.negate().pow(inversions).multiply(product));
            }
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfThreeByThreeMatricesShouldSucceed() {
        threeByThreeMatrices.forEach(matrix -> {
            final SimpleComplexNumber first =
                matrix.element(1, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(3, 3));
            final SimpleComplexNumber second =
                matrix.element(1, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(3, 1));
            final SimpleComplexNumber third =
                matrix.element(1, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(3, 2));
            final SimpleComplexNumber fourth =
                matrix.element(3, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(1, 3));
            final SimpleComplexNumber fifth =
                matrix.element(3, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(1, 1));
            final SimpleComplexNumber sixth =
                matrix.element(3, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(1, 2));
            final SimpleComplexNumber expected =
                first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final SimpleComplexNumber expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.determinant().get()).isEqualTo(SimpleComplexNumber.ZERO);
    }

    @Test
    public void determinatOfIdentityMatrixShouldBeEqualToOne() {
        assertThat(identityMatrix.determinant().get()).isEqualTo(SimpleComplexNumber.ONE);
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
            final SimpleComplexNumber expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(SimpleComplexNumber::multiply).get();
            assertThat(matrix.determinant().get()).isEqualTo(expected);
        });
    }

    @Test
    public void transposeShouldSucceed() {
        matrices.forEach(matrix -> {
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.columnSize(), matrix.rowSize());
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
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(rowSize - 1, columnSize - 1);
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
                .map(column -> column.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add))
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
                .map(column -> column.values().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add))
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
            final BigInteger expected =
                matrix.elements().stream().map(SimpleComplexNumber::absPow2).reduce(BigInteger::add).get();
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
                .isEqualTo(scalar.absPow2().multiply(matrix.frobeniusNormPow2()))));
    }

    @Test
    public void maxNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected =
                matrix.elements().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::max).get();
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
        assertThat(SimpleComplexNumberMatrix.builder(2, 3).putAll(SimpleComplexNumber.ZERO).build().square()).isFalse();
    }

    @Test
    public void squareOfSquareMatricesShouldBeTrue() {
        assertThat(squareMatrices).are(new Condition<>(SimpleComplexNumberMatrix::square, "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(matrix -> assertThat(matrix.square()).isEqualTo(matrix.rowSize() == matrix.columnSize()));
    }

    @Test
    public void triangularNotSquareShouldReturnFalse() {
        final SimpleComplexNumberMatrix matrix =
            SimpleComplexNumberMatrix.builder(4, 5).putAll(SimpleComplexNumber.ZERO).build();
        assertThat(matrix.triangular()).isFalse();
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleSimpleComplexNumber(bound));
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
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleSimpleComplexNumber(bound));
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
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleSimpleComplexNumber(bound));
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
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
                    builder.put(rowIndex, columnIndex, mathRandom.nextInvertibleSimpleComplexNumber(bound));
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
            final SimpleComplexNumber element = mathRandom.nextInvertibleSimpleComplexNumber(bound);
            final SimpleComplexNumberMatrixBuilder builder =
                SimpleComplexNumberMatrix.builder(rowSize, matrix.columnSize());
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
        final SimpleComplexNumberMatrix zeroMatrix =
            SimpleComplexNumberMatrix.builder(size, size).putAll(SimpleComplexNumber.ZERO).build();
        assertThat(zeroMatrix.invertible()).isFalse();
    }

    @Test
    public void invertibleIdShouldSucceed() {
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ONE);
            } else {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        final Integer index = RandomUtils.nextInt(1, 5);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(index) == 0 && columnIndex.compareTo(index) == 0) {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ONE.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ONE);
            } else {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
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
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final SimpleComplexNumber element = mathRandom.nextInvertibleSimpleComplexNumber(bound);
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
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final SimpleComplexNumber element = mathRandom.nextSimpleComplexNumber(bound);
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
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final SimpleComplexNumber element = mathRandom.nextInvertibleSimpleComplexNumber(bound);
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element);
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final SimpleComplexNumberMatrixBuilder builder = SimpleComplexNumberMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final SimpleComplexNumber element = mathRandom.nextSimpleComplexNumber(bound);
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, SimpleComplexNumber.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isTrue();
    }

    @Test
    public void builderRowSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> SimpleComplexNumberMatrix.builder(0, 1))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> SimpleComplexNumberMatrix.builder(1, 0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected columnSize > 0 but actual 0");
    }
}
