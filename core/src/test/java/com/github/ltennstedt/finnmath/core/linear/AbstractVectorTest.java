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
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class AbstractVectorTest {
    private final long bound = 10;
    private final int size = 4;
    private final int howMany = 10;
    private final MathRandom mathRandom = new MathRandom(7);
    private final List<BigIntegerVector> vectors = mathRandom.nextBigIntegerVectors(bound, size, howMany);
    private final List<BigIntegerVector> others = mathRandom.nextBigIntegerVectors(bound, size, howMany);
    private final List<BigIntegerVector> additionals = mathRandom.nextBigIntegerVectors(bound, size, howMany);
    private final List<BigInteger> scalars = mathRandom.nextBigIntegers(bound, howMany);
    private final BigIntegerVector zeroVector = Vectors.buildZeroBigIntegerVector(size);
    private final BigIntegerVector vectorDifferentSize =
        BigIntegerVector.builder(size + 1).putAll(BigInteger.ONE).build();
    private final SquareRootContext squareRootContext = AbstractVector.DEFAULT_SQUARE_ROOT_CONTEXT;
    private final String nullDescription = "null";
    private final String differentSizeDescription = "different size";
    private final String comparingToDescription = "compare";
    private final String equalsDescription = "equals";
    private final String zeroDescription = "zero";
    private final String positiveDefiniteDescription = "positive definite";
    private final String absolutelyHomogeneousDescription = "absolutely homogeneous";
    private final String subadditive = "subadditive";
    private final String otherMessage = "other";
    private final String differentSizeMessage = "expected equal sizes but actual 4 != 5";
    private final BigDecimal tolerance = new BigDecimal("0.001");
    private SoftAssertions softly;

    @Before
    public void setUp() {
        softly = new SoftAssertions();
    }

    @Test
    public void taxicabDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void taxicabDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.taxicabDistance(vectorDifferentSize)).as(differentSizeDescription)
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(differentSizeMessage);
    }

    @Test
    public void taxicabDistanceShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.taxicabDistance(other))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other).taxicabNorm())));
    }

    @Test
    public void taxicabDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.taxicabDistance(vector)).as(zeroDescription)
            .isEqualByComparingTo(BigInteger.ZERO));
    }

    @Test
    public void taxicabDistanceShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.taxicabDistance(other))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigInteger.ZERO)));
    }

    @Test
    public void taxicabDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigInteger actual = vector.scalarMultiply(scalar).taxicabDistance(other.scalarMultiply(scalar));
            final BigInteger expected = vector.taxicabDistance(other).multiply(scalar.abs());
            softly.assertThat(actual).as(comparingToDescription).isEqualByComparingTo(expected);
        })));
    }

    @Test
    public void taxicabDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigInteger triangleDistance = vector.taxicabDistance(other).add(other.taxicabDistance(additional));
            softly.assertThat(vector.taxicabDistance(additional)).as(subadditive).isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void euclideanDistancePow2NullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistancePow2(null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void euclideanDistancePow2DifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistancePow2(vectorDifferentSize)).as(differentSizeDescription)
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(differentSizeMessage);
    }

    @Test
    public void euclideanDistancePow2ShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistancePow2(other))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other).euclideanNormPow2())));
    }

    @Test
    public void euclideanDistancePow2SameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.euclideanDistancePow2(vector)).as(zeroDescription)
            .isEqualByComparingTo(BigInteger.ZERO));
    }

    @Test
    public void euclideanDistancePow2ShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistancePow2(other))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigInteger.ZERO)));
    }

    @Test
    public void euclideanDistancePow2ShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigInteger actual = vector.scalarMultiply(scalar).euclideanDistancePow2(other.scalarMultiply(scalar));
            final BigInteger expected = vector.euclideanDistancePow2(other).multiply(scalar.pow(2));
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isEqualByComparingTo(expected);
        })));
    }

    @Test
    public void euclideanDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void euclideanDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(vectorDifferentSize)).as(differentSizeDescription)
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(differentSizeMessage);
    }

    @Test
    public void euclideanDistanceShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistance(other))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other).euclideanNorm())));
    }

    @Test
    public void euclideanDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.euclideanDistance(vector)).as(zeroDescription)
            .isEqualByComparingTo(BigDecimal.ZERO));
    }

    @Test
    public void euclideanDistanceShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistance(other))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void euclideanDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigDecimal actual = vector.scalarMultiply(scalar).euclideanDistance(other.scalarMultiply(scalar));
            final BigDecimal expected = vector.euclideanDistance(other).multiply(new BigDecimal(scalar.abs()));
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isBetween(expected.subtract(tolerance),
                expected.add(tolerance));
        })));
    }

    @Test
    public void euclideanDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigDecimal triangleDistance =
                vector.euclideanDistance(other).add(other.euclideanDistance(additional));
            softly.assertThat(vector.euclideanDistance(additional)).as(subadditive)
                .isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void euclideanDistanceContextOtherNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, squareRootContext)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void euclideanDistanceContextContextNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("squareRootContext");
    }

    @Test
    public void euclideanDistanceContextDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(vectorDifferentSize, squareRootContext))
            .as(differentSizeDescription).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage(differentSizeMessage);
    }

    @Test
    public void euclideanDistanceContextShouldSucceed() {
        vectors.forEach(vector -> others.forEach(
            other -> softly.assertThat(vector.euclideanDistance(other, squareRootContext)).as(comparingToDescription)
                .isEqualByComparingTo(vector.subtract(other).euclideanNorm(squareRootContext))));
    }

    @Test
    public void euclideanDistanceContextSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.euclideanDistance(vector, squareRootContext))
            .as(zeroDescription).isEqualByComparingTo(BigDecimal.ZERO));
    }

    @Test
    public void euclideanDistanceContextShouldBePositiveDefinite() {
        vectors.forEach(
            vector -> others.forEach(other -> softly.assertThat(vector.euclideanDistance(other, squareRootContext))
                .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void euclideanDistanceContextShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigDecimal actual =
                vector.scalarMultiply(scalar).euclideanDistance(other.scalarMultiply(scalar), squareRootContext);
            final BigDecimal expected = vector.euclideanDistance(other, squareRootContext)
                .multiply(new BigDecimal(scalar.abs()), squareRootContext.getMathContext());
            softly.assertThat(actual).as(absolutelyHomogeneousDescription).isBetween(
                expected.subtract(tolerance, squareRootContext.getMathContext()),
                expected.add(tolerance, squareRootContext.getMathContext()));
        })));
    }

    @Test
    public void euclideanDistanceContextShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigDecimal triangleDistance = vector.euclideanDistance(other, squareRootContext)
                .add(other.euclideanDistance(additional, squareRootContext), squareRootContext.getMathContext());
            softly.assertThat(vector.euclideanDistance(additional, squareRootContext)).as(subadditive)
                .isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void maxDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void maxDistanceDifferentSizeShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.maxDistance(vectorDifferentSize)).as(differentSizeDescription)
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage(differentSizeMessage);
    }

    @Test
    public void maxDistanceShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.maxDistance(other))
            .as(comparingToDescription).isEqualByComparingTo(vector.subtract(other).maxNorm())));
    }

    @Test
    public void maxDistanceSameVectorShouldBeEqualToZero() {
        vectors.forEach(vector -> softly.assertThat(vector.maxDistance(vector)).as(zeroDescription)
            .isEqualByComparingTo(BigInteger.ZERO));
    }

    @Test
    public void maxDistanceShouldBePositiveDefinite() {
        vectors.forEach(vector -> others.forEach(other -> softly.assertThat(vector.maxDistance(other))
            .as(positiveDefiniteDescription).isGreaterThanOrEqualTo(BigInteger.ZERO)));
    }

    @Test
    public void maxDistanceShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(scalar -> {
            final BigInteger actual = vector.scalarMultiply(scalar).maxDistance(other.scalarMultiply(scalar));
            final BigInteger expected = vector.maxDistance(other).multiply(scalar.abs());
            softly.assertThat(actual).as(comparingToDescription).isEqualByComparingTo(expected);
        })));
    }

    @Test
    public void maxDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> additionals.forEach(additional -> {
            final BigInteger triangleDistance = vector.maxDistance(other).add(other.maxDistance(additional));
            softly.assertThat(vector.maxDistance(additional)).as(subadditive).isLessThanOrEqualTo(triangleDistance);
        })));
    }

    @Test
    public void elementNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.element(null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("index");
    }

    @Test
    public void elementInvalidIndexShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.element(0)).as("index").isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected index in [1, 4] but actual 0");
    }

    @Test
    public void elementShouldSucceed() {
        vectors.forEach(vector -> IntStream.rangeClosed(1, size).forEach(
            i -> softly.assertThat(vector.element(i)).as(equalsDescription).isEqualTo(vector.getMap().get(i))));
    }

    @Test
    public void entriesShouldSucceed() {
        vectors.forEach(
            vector -> softly.assertThat(vector.entries()).as(equalsDescription).isEqualTo(vector.getMap().entrySet()));
    }

    @Test
    public void elementsShouldSucceed() {
        vectors.forEach(
            vector -> softly.assertThat(vector.elements()).as(equalsDescription).isEqualTo(vector.getMap().values()));
    }

    @Test
    public void sizeShouldSucceed() {
        vectors.forEach(
            vector -> softly.assertThat(vector.size()).as(equalsDescription).isEqualTo(vector.getMap().size()));
    }

    @Test
    public void hashCodeEqualObjectsShouldHaveEqualHashCodes() {
        vectors.forEach(vector -> softly.assertThat(vector.hashCode()).as(equalsDescription)
            .isEqualTo(Vectors.copyOf(vector).hashCode()));
    }

    @Test
    public void hashCodeShouldSucceedd() {
        vectors.forEach(vector -> softly.assertThat(vector.hashCode()).as(equalsDescription)
            .isEqualTo(Objects.hash(vector.getMap())));
    }

    @Test
    @SuppressWarnings("SelfEquals")
    public void equalsSameObjectsShouldReturnTrue() {
        vectors.forEach(vector -> softly.assertThat(vector.equals(vector)).as(equalsDescription).isTrue());
    }

    @Test
    public void equalsNullShouldReturnFalse() {
        vectors.forEach(vector -> softly.assertThat(vector.equals(null)).as(equalsDescription).isFalse());
    }

    @Test
    public void equalsEqualObjectsShouldReturnTrue() {
        vectors
            .forEach(vector -> softly.assertThat(vector.equals(Vectors.copyOf(vector))).as(equalsDescription).isTrue());
    }

    @Test
    public void toStringShouldSucceed() {
        vectors.forEach(vector -> softly.assertThat(vector.toString()).as(equalsDescription)
            .isEqualTo(MoreObjects.toStringHelper(vector).add("map", vector.getMap()).toString()));
    }

    @After
    public void tearDown() {
        softly.assertAll();
    }
}
