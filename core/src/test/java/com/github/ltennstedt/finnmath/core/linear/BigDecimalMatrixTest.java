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

import com.github.ltennstedt.finnmath.core.linear.BigDecimalMatrix.BigDecimalMatrixBuilder;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.collect.Collections2;
import com.google.common.collect.Table.Cell;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Condition;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.Ignore;
import org.junit.Test;

public final class BigDecimalMatrixTest {
    private final long bound = 10;
    private final int scale = 2;
    private final int rowSize = 4;
    private final int columnSize = 5;
    private final int size = rowSize;
    private final int howMany = 10;
    private final BigDecimalMatrix zeroMatrixForAddition =
        BigDecimalMatrix.builder(rowSize, columnSize).putAll(BigDecimal.ZERO).build();
    private final BigDecimalMatrix zeroMatrixForMultiplication =
        BigDecimalMatrix.builder(columnSize, rowSize).putAll(BigDecimal.ZERO).build();
    private final BigDecimalMatrix zeroSquareMatrix =
        BigDecimalMatrix.builder(rowSize, rowSize).putAll(BigDecimal.ZERO).build();
    private final BigDecimalMatrix identityMatrix = Matrices.buildIdentityBigDecimalMatrix(size);
    private final List<Integer> range = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigDecimalMatrix> matrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, columnSize, howMany);
    private final List<BigDecimalMatrix> squareMatrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, rowSize, rowSize, howMany);
    private final List<BigDecimalMatrix> fourByFourMatrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, 4, 4, howMany);
    private final List<BigDecimalMatrix> threeByThreeMatrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, 3, 3, howMany);
    private final List<BigDecimalMatrix> twoByTwoMatrices =
        mathRandom.nextBigDecimalMatrices(bound, scale, 2, 2, howMany);
    private final List<BigDecimalMatrix> upperTriangularMatrices =
        mathRandom.nextUpperTriangularBigDecimalMatrices(bound, scale, rowSize, howMany);
    private final List<BigDecimalMatrix> lowerTriangularMatrices =
        mathRandom.nextLowerTriangularBigDecimalMatrices(bound, scale, rowSize, howMany);
    private final List<BigDecimalMatrix> triangularMatrices =
        mathRandom.nextTriangularBigDecimalMatrices(bound, scale, rowSize, howMany);
    private final List<BigDecimalMatrix> diagonalMatrices =
        mathRandom.nextDiagonalBigDecimalMatrices(bound, scale, rowSize, howMany);
    private final List<BigDecimal> scalars = mathRandom.nextBigDecimals(bound, scale, howMany);
    private final BigDecimalMatrix nonSquareMatrix = matrices.get(0);

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");

    }

    @Test
    public void addRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix matrix = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalMatrix summand = Matrices.buildZeroBigDecimalMatrix(4, 5);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");
    }

    @Test
    public void addColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix matrix = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalMatrix summand = Matrices.buildZeroBigDecimalMatrix(5, 4);
            matrix.add(summand);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.subtract(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("subtrahend");

    }

    @Test
    public void subtractRowSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix minuend = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalMatrix subtrahend = Matrices.buildZeroBigDecimalMatrix(4, 5);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal row sizes but actual 5 != 4");

    }

    @Test
    public void subtractColumnSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix minuend = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalMatrix subtrahend = Matrices.buildZeroBigDecimalMatrix(5, 4);
            minuend.subtract(subtrahend);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal column sizes but actual 5 != 4");

    }

    @Test
    public void subtractSelfShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.subtract(matrix).getTable().values())
            .usingElementComparator(BigDecimalComparator.BIG_DECIMAL_COMPARATOR)
            .isEqualTo(zeroMatrixForAddition.getTable().values()));
    }

    @Test
    public void multiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForMultiplication.multiply(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("factor");

    }

    @Test
    public void multiplyColumnSizeNotEqualToFactorRowSizeShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix matrix = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalMatrix factor = Matrices.buildZeroBigDecimalMatrix(4, 5);
            matrix.multiply(factor);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == factor.rowSize but actual 5 != 4");

    }

    @Test
    public void muliplyZeroMatrixShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> matrix.multiply(zeroMatrixForMultiplication).cells()
            .forEach(cell -> assertThat(cell.getValue()).isEqualByComparingTo(BigDecimal.ZERO)));
    }

    @Test
    public void multiplyNullVectorShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.multiplyVector(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void multiplyVectorColumnSizeNotEqualToVectorSizeShouldThrowException() {
        assertThatThrownBy(() -> {
            final BigDecimalMatrix matrix = Matrices.buildZeroBigDecimalMatrix(5, 5);
            final BigDecimalVector vector = Vectors.buildZeroBigDecimalVector(4);
            matrix.multiplyVector(vector);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize == vectorSize but actual 5 != 4");
    }

    @Test
    public void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroMatrixForAddition.scalarMultiply(null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.scalarMultiply(BigDecimal.ZERO).getTable().values())
            .usingElementComparator(BigDecimalComparator.BIG_DECIMAL_COMPARATOR)
            .isEqualTo(zeroMatrixForAddition.getTable().values()));
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        matrices.forEach(matrix -> assertThat(matrix.add(matrix.negate()).getTable().values())
            .usingElementComparator(BigDecimalComparator.BIG_DECIMAL_COMPARATOR)
            .isEqualTo(zeroMatrixForAddition.getTable().values()));
    }

    @Test
    public void traceNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.trace()).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("expected square matrix but was a 4x5 matrix");
    }

    @Test
    public void traceShouldSucceed() {
        squareMatrices.forEach(matrix -> {
            final BigDecimal expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(BigDecimal::add).get();
            assertThat(matrix.trace().compareTo(expected)).isEqualTo(0);
        });
    }

    @Test
    public void traceOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.trace().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }

    @Test
    public void traceShouldBeAdditive() {
        squareMatrices.forEach(matrix -> squareMatrices.forEach(
            other -> assertThat(matrix.add(other).trace().compareTo(matrix.trace().add(other.trace()))).isEqualTo(0)));
    }

    @Test
    public void traceShouldBeLinear() {
        squareMatrices.forEach(matrix -> scalars.forEach(
            scalar -> assertThat(matrix.scalarMultiply(scalar).trace().compareTo(scalar.multiply(matrix.trace())))
                .isEqualTo(0)));
    }

    @Test
    public void traceShouldBeIndependentOfTheOrderOfTheFactors() {
        squareMatrices.forEach(matrix -> squareMatrices
            .forEach(other -> assertThat(matrix.multiply(other).trace().compareTo(other.multiply(matrix).trace()))
                .isEqualTo(0)));
    }

    @Test
    public void traceShouldBeEqualToTheTraceOfTheTranspose() {
        squareMatrices.forEach(matrix -> assertThat(matrix.trace().compareTo(matrix.transpose().trace())).isEqualTo(0));
    }

    @Test
    public void determinatNotSquareShouldThrowException() {
        assertThatThrownBy(() -> nonSquareMatrix.determinant()).isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("expected square matrix but was a 4x5 matrix");
    }

    @Test
    public void determinantOfFourByFourMatricesShouldSucceed() {
        fourByFourMatrices.forEach(matrix -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final List<Integer> permutation : Collections2.permutations(matrix.rowIndexes())) {
                BigDecimal product = BigDecimal.ONE;
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
                expected = expected.add(BigDecimal.ONE.negate().pow(inversions).multiply(product));
            }
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfThreeByThreeMatricesShouldSucceed() {
        threeByThreeMatrices.forEach(matrix -> {
            final BigDecimal first = matrix.element(1, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(3, 3));
            final BigDecimal second =
                matrix.element(1, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(3, 1));
            final BigDecimal third = matrix.element(1, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(3, 2));
            final BigDecimal fourth =
                matrix.element(3, 1).multiply(matrix.element(2, 2)).multiply(matrix.element(1, 3));
            final BigDecimal fifth = matrix.element(3, 2).multiply(matrix.element(2, 3)).multiply(matrix.element(1, 1));
            final BigDecimal sixth = matrix.element(3, 3).multiply(matrix.element(2, 1)).multiply(matrix.element(1, 2));
            final BigDecimal expected = first.add(second).add(third).subtract(fourth).subtract(fifth).subtract(sixth);
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfTwoByTwoMatricesShouldSucceed() {
        twoByTwoMatrices.forEach(matrix -> {
            final BigDecimal expected = matrix.element(1, 1).multiply(matrix.element(2, 2))
                .subtract(matrix.element(1, 2).multiply(matrix.element(2, 1)));
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
    }

    @Test
    public void determinatOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.determinant()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void determinatOfIdentityMatrixShouldBeEqualToOne() {
        assertThat(identityMatrix.determinant()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    public void determinatOfTransposeShouldBeEqualToDeterminant() {
        fourByFourMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualByComparingTo(matrix.determinant()));
        threeByThreeMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualByComparingTo(matrix.determinant()));
        twoByTwoMatrices
            .forEach(matrix -> assertThat(matrix.transpose().determinant()).isEqualByComparingTo(matrix.determinant()));
    }

    @Test
    public void determinatShouldBeMultiplicative() {
        fourByFourMatrices
            .forEach(matrix -> fourByFourMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualByComparingTo(matrix.determinant().multiply(other.determinant()))));
        threeByThreeMatrices
            .forEach(matrix -> threeByThreeMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualByComparingTo(matrix.determinant().multiply(other.determinant()))));
        twoByTwoMatrices
            .forEach(matrix -> twoByTwoMatrices.forEach(other -> assertThat(matrix.multiply(other).determinant())
                .isEqualByComparingTo(matrix.determinant().multiply(other.determinant()))));
    }

    @Test
    public void determinatWithScalarShouldBeEqualToPowOfScalarMultipliedWithDet() {
        fourByFourMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualByComparingTo(scalar.pow(matrix.rowSize()).multiply(matrix.determinant()))));
        threeByThreeMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualByComparingTo(scalar.pow(3).multiply(matrix.determinant()))));
        twoByTwoMatrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).determinant())
                .isEqualByComparingTo(scalar.pow(2).multiply(matrix.determinant()))));
    }

    @Test
    public void determinatOfTriangularMatricesShouldBeEqualToProductOfTheDiagonalEntries() {
        triangularMatrices.forEach(matrix -> {
            final BigDecimal expected =
                matrix.cells().stream().filter(cell -> cell.getRowKey().compareTo(cell.getColumnKey()) == 0)
                    .map(Cell::getValue).reduce(BigDecimal::multiply).get();
            assertThat(matrix.determinant()).isEqualTo(expected);
        });
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
    public void maxAbsColumnSumNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected = matrix.columns().values().asList().stream()
                .map(column -> column.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add)).map(Optional::get)
                .reduce(BigDecimal::max).get();
            assertThat(matrix.maxAbsColumnSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsColumnSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsColumnSumNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void maxAbsColumnSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsColumnSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Ignore
    @Test
    public void maxAbsColumnSumNormShouldBeAbsolutelyHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxAbsColumnSumNorm())
                .isEqualByComparingTo(scalar.abs().multiply(matrix.maxAbsColumnSumNorm()))));
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
            final BigDecimal expected = matrix.rows().values().asList().stream()
                .map(column -> column.values().stream().map(BigDecimal::abs).reduce(BigDecimal::add)).map(Optional::get)
                .reduce(BigDecimal::max).get();
            assertThat(matrix.maxAbsRowSumNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxAbsRowSumNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxAbsRowSumNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void maxAbsRowSumNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxAbsRowSumNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Ignore
    @Test
    public void maxAbsRowSumNormShouldBeAbsolutelyHomogeneous() {
        matrices
            .forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxAbsRowSumNorm())
                .isEqualByComparingTo(scalar.abs().multiply(matrix.maxAbsRowSumNorm()))));
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
            final BigDecimal expected =
                matrix.elements().stream().map(element -> element.pow(2)).reduce(BigDecimal::add).get();
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
                .isEqualTo(scalar.pow(2).multiply(matrix.frobeniusNormPow2()))));
    }

    @Test
    public void maxNormShouldBeSucceed() {
        matrices.forEach(matrix -> {
            final BigDecimal expected = matrix.elements().stream().map(BigDecimal::abs).reduce(BigDecimal::max).get();
            assertThat(matrix.maxNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxNormOfZeroMatrixShouldBeEqualToZero() {
        assertThat(zeroSquareMatrix.maxNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void maxNormShouldBePositiveValued() {
        matrices.forEach(matrix -> assertThat(matrix.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Ignore
    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        matrices.forEach(matrix -> scalars.forEach(scalar -> assertThat(matrix.scalarMultiply(scalar).maxNorm())
            .isEqualByComparingTo(scalar.abs().multiply(matrix.maxNorm()))));
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
        assertThat(squareMatrices).are(new Condition<>(BigDecimalMatrix::square, "square"));
    }

    @Test
    public void squareShouldBePredictable() {
        matrices.forEach(matrix -> assertThat(matrix.square()).isEqualTo(matrix.rowSize() == matrix.columnSize()));
    }

    @Test
    public void triangularNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.triangular()).isFalse();
    }

    @Test
    public void triangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
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
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
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
        triangularMatrices.forEach(matrix -> assertThat(matrix.triangular()).isTrue());
    }

    @Test
    public void upperTriangularNotSquareShouldReturnFalse() {
        assertThat(nonSquareMatrix.upperTriangular()).isFalse();
    }

    @Test
    public void upperTriangularNotUpperTriangularShouldReturnFalse() {
        upperTriangularMatrices.forEach(matrix -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) > 0) {
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
        upperTriangularMatrices.forEach(matrix -> assertThat(matrix.upperTriangular()).isTrue());
    }

    @Test
    public void lowerTriangularNotSquareShouldReturnFalse() {
        final BigDecimalMatrix matrix = Matrices.buildZeroBigDecimalMatrix(4, 5);
        assertThat(matrix.lowerTriangular()).isFalse();
    }

    @Test
    public void lowerTriangularNotLowerTriangularShouldReturnFalse() {
        lowerTriangularMatrices.forEach(matrix -> {
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(matrix.rowSize(), matrix.columnSize());
            matrix.cells().forEach(cell -> {
                final Integer rowIndex = cell.getRowKey();
                final Integer columnIndex = cell.getColumnKey();
                if (rowIndex.compareTo(columnIndex) < 0) {
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
            final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
            final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(rowSize, matrix.columnSize());
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
    public void identityNotSquareMatrixShouldReturnFalse() {
        assertThat(nonSquareMatrix.identity()).isFalse();
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
    public void invertibleIdentityMatrixShouldSucceed() {
        assertThat(identityMatrix.invertible()).isTrue();
    }

    @Test
    public void invertibleMatrixWithDetEqualToMinusOneShouldSucceed() {
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        final Integer index = RandomUtils.nextInt(1, 5);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(index) == 0 && columnIndex.compareTo(index) == 0) {
                builder.put(rowIndex, columnIndex, BigDecimal.ONE.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigDecimal.ONE);
            } else {
                builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
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
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
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
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
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
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(1, bound));
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element);
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
            }
        }));
        assertThat(builder.build().skewSymmetric()).isFalse();
    }

    @Test
    public void skewSymmetricShouldSucceed() {
        final BigDecimalMatrixBuilder builder = BigDecimalMatrix.builder(size, size);
        range.forEach(rowIndex -> range.forEach(columnIndex -> {
            if (rowIndex.compareTo(columnIndex) < 0) {
                final BigDecimal element = BigDecimal.valueOf(RandomUtils.nextLong(0, bound));
                builder.put(rowIndex, columnIndex, element);
                builder.put(columnIndex, rowIndex, element.negate());
            } else if (rowIndex.compareTo(columnIndex) == 0) {
                builder.put(rowIndex, columnIndex, BigDecimal.ZERO);
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
        matrices.forEach(matrix -> range.forEach(rowIndex -> IntStream.rangeClosed(1, columnSize).boxed()
            .collect(Collectors.toList()).forEach(columnIndex -> assertThat(matrix.element(rowIndex, columnIndex))
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
        matrices.forEach(matrix -> IntStream.rangeClosed(1, 4).boxed().collect(Collectors.toList())
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
        matrices.forEach(matrix -> IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList()).forEach(
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
        assertThatThrownBy(() -> BigDecimalMatrix.builder(0, 1)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected rowSize > 0 but actual 0");
    }

    @Test
    public void builderColumnSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> BigDecimalMatrix.builder(1, 0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected columnSize > 0 but actual 0");
    }
}
