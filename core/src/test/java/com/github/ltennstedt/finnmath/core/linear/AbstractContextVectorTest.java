/*
 * Copyright 2018 Lars Tennstedt
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.core.sqrt.SquareRootContext;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class AbstractContextVectorTest {
    private final long bound = 10;
    private final int scale = 2;
    private final int size = 4;
    private final int howMany = 10;
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigDecimalVector> vectors = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimalVector> others = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimalVector> additionals = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimal> scalars = mathRandom.nextBigDecimals(bound, scale, howMany);
    private final BigDecimalVector zeroVector = Vectors.buildZeroBigDecimalVector(size);
    private final BigDecimalVector vectorDifferentSize = mathRandom.nextBigDecimalVector(bound, scale, size + 1);
    private final MathContext mathContext = MathContext.DECIMAL128;
    private final SquareRootContext squareRootContext = AbstractContextVector.DEFAULT_SQUARE_ROOT_CONTEXT;
    private final String nullDescription = "null";
    private final String differentSizeDescription = "different size";
    private final String comparingToDescription = "compare";
    private final String zeroDescription = "zero";
    private final String positiveDefiniteDescription = "positive definite";
    private final String absolutelyHomogeneousDescription = "absolutely homogeneous";
    private final String subadditive = "subadditive";
    private final String otherMessage = "other";
    private final String mathContextMessage = "mathContext";
    private final String squareRootContextMessage = "squareRootContext";
    private final String differentSizeMessage = "expected equal sizes but actual 4 != 5";
    private final BigDecimal tolerance = new BigDecimal("0.001");
    private SoftAssertions softly;

    @Before
    public void setUp() {
        softly = new SoftAssertions();
    }

    @Test
    public void taxicabDistanceOtherNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(null, mathContext)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void taxicabDistanceContextNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(zeroVector, null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(mathContextMessage);
    }

    @Test
    public void taxicabDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(vectorDifferentSize, mathContext))
            .as(differentSizeDescription).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(differentSizeMessage);
    }

    @Test
    public void taxicabDistanceShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.taxicabDistance(other, mathContext))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other, mathContext).taxicabNorm())));
    }

    @Test
    public void taxicabDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.taxicabDistance(vector, mathContext)).as(zeroDescription)
            .isEqualByComparingTo(BigDecimal.ZERO));
    }

    @Test
    public void taxicabDistanceShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.taxicabDistance(other, mathContext))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void taxicabDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigDecimal actual = vector.scalarMultiply(scalar, mathContext)
                .taxicabDistance(other.scalarMultiply(scalar, mathContext), mathContext);
            final BigDecimal expected =
                vector.taxicabDistance(other, mathContext).multiply(scalar.abs(mathContext), mathContext);
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isEqualByComparingTo(expected);
        })));
    }

    @Test
    public void taxicabDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigDecimal triangleDistance = vector.taxicabDistance(other, mathContext)
                .add(other.taxicabDistance(additional, mathContext), mathContext);
            softly.assertThat(vector.taxicabDistance(additional, mathContext)).as(subadditive)
                .isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void euclideanDistanceOtherNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, squareRootContext)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void euclideanDistanceContextNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(squareRootContextMessage);
    }

    @Test
    public void euclideanDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(vectorDifferentSize, squareRootContext))
            .as(differentSizeDescription).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(differentSizeMessage);
    }

    @Test
    public void euclideanDistanceShouldSucceed() {
        vectors.forEach(
            vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistance(other, squareRootContext))
                .as(comparingToDescription).isEqualByComparingTo(
                    vector.subtract(other, squareRootContext.getMathContext()).euclideanNorm(squareRootContext))));
    }

    @Test
    public void euclideanDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.euclideanDistance(vector, squareRootContext))
            .as(zeroDescription).isBetween(tolerance.negate(), tolerance));
    }

    @Test
    public void euclideanDistanceShouldBePositiveDefinite() {
        vectors.forEach(
            vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistance(other, squareRootContext))
                .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void euclideanDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigDecimal actual = vector.scalarMultiply(scalar, mathContext)
                .euclideanDistance(other.scalarMultiply(scalar, mathContext), squareRootContext);
            final BigDecimal expected =
                vector.euclideanDistance(other, squareRootContext).multiply(scalar.abs(mathContext), mathContext);
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isBetween(
                expected.subtract(tolerance, squareRootContext.getMathContext()),
                expected.add(tolerance, squareRootContext.getMathContext()));
        })));
    }

    @Test
    public void euclideanDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigDecimal triangleDistance = vector.euclideanDistance(other, squareRootContext)
                .add(other.euclideanDistance(additional, squareRootContext), mathContext);
            softly.assertThat(vector.euclideanDistance(additional, squareRootContext)).as(subadditive)
                .isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void maxDistanceOtherNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(null, mathContext)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void maxDistanceContextNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(zeroVector, null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(mathContextMessage);
    }

    @Test
    public void maxDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(vectorDifferentSize, mathContext)).as(differentSizeDescription)
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(differentSizeMessage);
    }

    @Test
    public void maxDistanceShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.maxDistance(other, mathContext))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other, mathContext).maxNorm())));
    }

    @Test
    public void maxDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.maxDistance(vector, mathContext)).as(zeroDescription)
            .isEqualByComparingTo(BigDecimal.ZERO));
    }

    @Test
    public void maxDistanceShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.maxDistance(other, mathContext))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void maxDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigDecimal actual = vector.scalarMultiply(scalar, mathContext)
                .maxDistance(other.scalarMultiply(scalar, mathContext), mathContext);
            final BigDecimal expected =
                vector.maxDistance(other, mathContext).multiply(scalar.abs(mathContext), mathContext);
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isEqualByComparingTo(expected);
        })));
    }

    @Test
    public void maxDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigDecimal triangleDistance =
                vector.maxDistance(other, mathContext).add(other.maxDistance(additional, mathContext), mathContext);
            softly.assertThat(vector.maxDistance(additional, mathContext)).as(subadditive)
                .isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @After
    public void tearDown() {
        softly.assertAll();
    }
}
