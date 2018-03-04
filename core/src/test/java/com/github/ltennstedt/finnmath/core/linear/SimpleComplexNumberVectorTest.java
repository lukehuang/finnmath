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

import com.github.ltennstedt.finnmath.core.number.SimpleComplexNumber;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.github.ltennstedt.finnmath.core.util.SquareRootCalculator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import org.junit.Test;

public final class SimpleComplexNumberVectorTest {
    private final int size = 4;
    private final int howMany = 10;
    private final long bound = 10;
    private final int differentSize = size + 1;
    private final int scale = 2;
    private final BigDecimal precision = BigDecimal.valueOf(0.00001);
    private final MathRandom mathRandom = new MathRandom(7);
    private final RoundingMode roundingMode = RoundingMode.HALF_DOWN;
    private final SimpleComplexNumberVector zeroVector =
        SimpleComplexNumberVector.builder(size).putAll(SimpleComplexNumber.ZERO).build();
    private final SimpleComplexNumberVector vectorWithAnotherSize =
        SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build();
    private final List<SimpleComplexNumberVector> vectors =
        mathRandom.nextSimpleComplexNumberVectors(bound, size, howMany);
    private final List<SimpleComplexNumberVector> others =
        mathRandom.nextSimpleComplexNumberVectors(bound, size, howMany);
    private final List<SimpleComplexNumberVector> additionalOthers =
        mathRandom.nextSimpleComplexNumberVectors(bound, size, howMany);
    private final List<SimpleComplexNumber> scalars = mathRandom.nextSimpleComplexNumbers(bound, howMany);
    private final List<SimpleComplexNumber> otherScalars = mathRandom.nextSimpleComplexNumbers(bound, howMany);
    private final BigDecimal tolerance = BigDecimal.valueOf(0.001D);
    private final BigDecimal negatedTolerance = tolerance.negate();

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");
    }

    @Test
    public void addSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.add(vectorWithAnotherSize))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("equal sizes expected but actual %s != %s", size, differentSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder builder =
                SimpleComplexNumberVector.builder(size);
            vector.entries().forEach(element -> builder.put(element.getValue().add(other.element(element.getKey()))));
            assertThat(vector.add(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void addShouldBeCommutative() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other)).isEqualTo(other.add(vector))));
    }

    @Test
    public void addShouldBeAssociative() {
        vectors.forEach(vector -> others.forEach(
            other -> additionalOthers.forEach(additionalOther -> assertThat(vector.add(other).add(additionalOther))
                .isEqualTo(vector.add(other.add(additionalOther))))));
    }

    @Test
    public void addZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.add(zeroVector)).isEqualTo(vector));
    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.subtract(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("subtrahend");
    }

    @Test
    public void subtractSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.subtract(vectorWithAnotherSize))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("equal sizes expected but actual %s != %s", size, differentSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder builder =
                SimpleComplexNumberVector.builder(size);
            vector.entries()
                .forEach(element -> builder.put(element.getValue().subtract(other.element(element.getKey()))));
            assertThat(vector.subtract(other)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void subtractZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.subtract(zeroVector)).isEqualTo(vector));
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> assertThat(vector.subtract(vector)).isEqualTo(zeroVector));
    }

    @Test
    public void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.scalarMultiply(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        vectors.forEach(vector -> scalars.forEach(scalar -> {
            final SimpleComplexNumberVector.SimpleComplexNumberVectorBuilder builder =
                SimpleComplexNumberVector.builder(size);
            vector.entries().forEach(element -> builder.put(scalar.multiply(element.getValue())));
            assertThat(vector.scalarMultiply(scalar)).isEqualTo(builder.build());
        }));
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        vectors.forEach(vector -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(vector.scalarMultiply(scalar.multiply(otherScalar)))
                .isEqualTo(vector.scalarMultiply(otherScalar).scalarMultiply(scalar)))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        vectors.forEach(vector -> scalars.forEach(
            scalar -> otherScalars.forEach(otherScalar -> assertThat(vector.scalarMultiply(scalar.add(otherScalar)))
                .isEqualTo(vector.scalarMultiply(scalar).add(vector.scalarMultiply(otherScalar))))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoVectorsShouldBeDistributive() {
        vectors.forEach(vector -> others
            .forEach(other -> scalars.forEach(scalar -> assertThat(vector.add(other).scalarMultiply(scalar))
                .isEqualTo(vector.scalarMultiply(scalar).add(other.scalarMultiply(scalar))))));
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> assertThat(vector.scalarMultiply(SimpleComplexNumber.ZERO)).isEqualTo(zeroVector));
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.scalarMultiply(SimpleComplexNumber.ONE)).isEqualTo(vector));
    }

    @Test
    public void negateShouldSucceed() {
        vectors.forEach(
            vector -> assertThat(vector.negate()).isEqualTo(vector.scalarMultiply(SimpleComplexNumber.ONE.negate())));
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroVector.negate()).isEqualTo(zeroVector);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.negate().negate()).isEqualTo(vector));
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> vector.add(vector.negate()).entries()
            .forEach(element -> assertThat(element.getValue()).isEqualTo(SimpleComplexNumber.ZERO)));
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        vectors.forEach(
            vector -> assertThat(vector.scalarMultiply(SimpleComplexNumber.ONE.negate())).isEqualTo(vector.negate()));
    }

    @Test
    public void taxicabNormShouldSucceed() {
        vectors.forEach(vector -> {
            final BigDecimal expected =
                vector.elements().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::add).get();
            assertThat(vector.taxicabNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void taxicabNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.taxicabNorm()).isBetween(negatedTolerance, tolerance);
    }

    @Test
    public void taxicabNormShouldBePositive() {
        vectors.forEach(vector -> assertThat(vector.taxicabNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void taxicabNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> scalars.forEach(scalar -> {
            final BigDecimal expected = scalar.abs().multiply(vector.taxicabNorm());
            assertThat(vector.scalarMultiply(scalar).taxicabNorm()).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        }));
    }

    @Test
    public void taxicabNormShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).taxicabNorm())
            .isLessThanOrEqualTo(vector.taxicabNorm().add(other.taxicabNorm()))));
    }

    @Test
    public void taxicabDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void taxicabDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector
            .taxicabDistance(SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void taxicabDistanceShouldBeSucceed() {
        vectors.forEach(vector -> others.forEach(
            other -> assertThat(vector.taxicabDistance(other)).isEqualTo(vector.subtract(other).taxicabNorm())));
    }

    @Test
    public void taxicabDistanceFromVectorToSelfShouldBeEqualToZero() {
        vectors.forEach(vector -> assertThat(vector.taxicabDistance(vector)).isBetween(negatedTolerance, tolerance));
    }

    @Test
    public void taxicabDistanceShouldBePositive() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.taxicabDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void taxicabDistanceShouldBeSymmetric() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.taxicabDistance(other)).isEqualTo(other.taxicabDistance(vector))));
    }

    @Test
    public void taxicabDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(
            other -> additionalOthers.forEach(additionalOther -> assertThat(vector.taxicabDistance(additionalOther))
                .isLessThanOrEqualTo(vector.taxicabDistance(other).add(other.taxicabDistance(additionalOther))))));
    }

    @Test
    public void euclideanNormPow2ShouldSucceed() {
        vectors.forEach(vector -> {
            final BigInteger expected =
                vector.elements().stream().map(SimpleComplexNumber::absPow2).reduce(BigInteger::add).get();
            assertThat(vector.euclideanNormPow2()).isEqualTo(expected);
        });
    }

    @Test
    public void euclideanNormPow2ZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.euclideanNormPow2()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    public void euclideanNormPow2NormShouldBeHomogeneous() {
        vectors
            .forEach(vector -> scalars.forEach(scalar -> assertThat(vector.scalarMultiply(scalar).euclideanNormPow2())
                .isEqualTo(scalar.absPow2().multiply(vector.euclideanNormPow2()))));
    }

    @Test
    public void euclideanNormShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.euclideanNorm()).isExactlyInstanceOf(BigDecimal.class)
            .isEqualTo(new SquareRootCalculator().sqrt(vector.euclideanNormPow2())));
    }

    @Test
    public void euclideanNormShouldBePositive() {
        vectors.forEach(vector -> assertThat(vector.euclideanNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void euclideanNormShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).euclideanNorm())
            .isLessThanOrEqualTo(vector.euclideanNorm().add(other.euclideanNorm()).add(tolerance))));
    }

    @Test
    public void euclideanNormPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("precision");
    }

    @Test
    public void euclideanNormPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(BigDecimal.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(BigDecimal.ONE))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void euclideanNormWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.euclideanNorm(precision)).isExactlyInstanceOf(BigDecimal.class)
            .isEqualTo(new SquareRootCalculator(precision).sqrt(vector.euclideanNormPow2())));
    }

    @Test
    public void euclideanNormScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(-1, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormWithScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(
            vector -> assertThat(vector.euclideanNorm(scale, roundingMode)).isExactlyInstanceOf(BigDecimal.class)
                .isEqualTo(new SquareRootCalculator(scale, roundingMode).sqrt(vector.euclideanNormPow2())));
    }

    @Test
    public void euclideanNormPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(null, scale, roundingMode))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanNormPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(BigDecimal.ZERO, scale, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(BigDecimal.ONE, scale, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void euclideanNormScaleTooLowAndPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanNorm(precision, -1, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.euclideanNorm(precision, scale, roundingMode))
            .isExactlyInstanceOf(BigDecimal.class)
            .isEqualTo(new SquareRootCalculator(precision, scale, roundingMode).sqrt(vector.euclideanNormPow2())));
    }

    @Test
    public void dotProductNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.dotProduct(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void dotProductSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector
            .dotProduct(SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void dotProductShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final SimpleComplexNumber expected =
                vector.entries().stream().map(entry -> entry.getValue().multiply(other.element(entry.getKey())))
                    .reduce(SimpleComplexNumber::add).get();
            assertThat(vector.dotProduct(other)).isEqualTo(expected);
        }));
    }

    @Test
    public void euclideanDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void euclideanDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(
            SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceShouldSucceed() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.euclideanDistance(other)).isExactlyInstanceOf(BigDecimal.class)
                .isEqualByComparingTo(new SquareRootCalculator().sqrt(vector.euclideanDistancePow2(other)))));
    }

    @Test
    public void euclideanDistanceShouldBePositive() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.euclideanDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void euclideanDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionalOthers
            .forEach(additionalOther -> assertThat(vector.euclideanDistance(additionalOther)).isLessThanOrEqualTo(
                vector.euclideanDistance(other).add(other.euclideanDistance(additionalOther)).add(tolerance)))));
    }

    @Test
    public void euclideanDistanceNullWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, precision))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistancePrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, null))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(
            SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build(), precision))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistancePrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> others.forEach(
            other -> assertThat(vector.euclideanDistance(other, precision)).isExactlyInstanceOf(BigDecimal.class)
                .isEqualTo(new SquareRootCalculator(precision).sqrt(vector.euclideanDistancePow2(other)))));
    }

    @Test
    public void euclideanDistanceNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, scale, roundingMode))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(
            SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build(), scale,
            roundingMode)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, -1, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithScaleAndRoundingModeShouldSucceed() {
        vectors
            .forEach(vector -> others.forEach(other -> assertThat(vector.euclideanDistance(other, scale, roundingMode))
                .isExactlyInstanceOf(BigDecimal.class)
                .isEqualTo(new SquareRootCalculator(scale, roundingMode).sqrt(vector.euclideanDistancePow2(other)))));
    }

    @Test
    public void euclideanDistanceNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, precision, scale, roundingMode))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistanceWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, null, scale, roundingMode))
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(
            SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build(), precision, scale,
            roundingMode)).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO, scale, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE, scale, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, precision, -1, roundingMode))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.euclideanDistance(other, precision, scale, roundingMode))
                .isExactlyInstanceOf(BigDecimal.class)
                .isEqualTo(new SquareRootCalculator(precision, scale, roundingMode)
                    .sqrt(vector.euclideanDistancePow2(other)))));
    }

    @Test
    public void maxNormShouldSucceed() {
        vectors.forEach(vector -> {
            final BigDecimal expected =
                vector.elements().stream().map(SimpleComplexNumber::abs).reduce(BigDecimal::max).get();
            assertThat(vector.maxNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void maxNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.maxNorm()).isBetween(negatedTolerance, tolerance);
    }

    @Test
    public void maxNormShouldBePositive() {
        vectors.forEach(vector -> assertThat(vector.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> scalars.forEach(scalar -> {
            final BigDecimal expected = scalar.abs().multiply(vector.maxNorm());
            assertThat(vector.scalarMultiply(scalar).maxNorm()).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        }));
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).maxNorm())
            .isLessThanOrEqualTo(vector.maxNorm().add(other.maxNorm()).add(tolerance))));
    }

    @Test
    public void maxDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void maxDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector
            .maxDistance(SimpleComplexNumberVector.builder(differentSize).putAll(SimpleComplexNumber.ZERO).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void maxDistanceShouldBeSucceed() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.maxDistance(other)).isEqualTo(vector.subtract(other).maxNorm())));
    }

    @Test
    public void maxDistanceFromVectorToSelfShouldBeEqualToZero() {
        vectors.forEach(vector -> assertThat(vector.maxDistance(vector)).isBetween(negatedTolerance, tolerance));
    }

    @Test
    public void maxDistanceShouldBePositive() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.maxDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void maxDistanceShouldBeSymmetric() {
        vectors.forEach(vector -> others
            .forEach(other -> assertThat(vector.maxDistance(other)).isEqualTo(other.maxDistance(vector))));
    }

    @Test
    public void maxDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(
            other -> additionalOthers.forEach(additionalOther -> assertThat(vector.maxDistance(additionalOther))
                .isLessThanOrEqualTo(vector.maxDistance(other).add(other.maxDistance(additionalOther))))));
    }

    @Test
    public void builderSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> SimpleComplexNumberVector.builder(0))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }
}
