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

import com.github.ltennstedt.finnmath.core.util.MathRandom;
import com.google.common.base.MoreObjects;
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
    private final BigIntegerVector zeroVector = Vectors.buildZeroBigIntegerVector(size);
    private final BigIntegerVector vectorDifferentSize =
        BigIntegerVector.builder(size + 1).putAll(BigInteger.ONE).build();
    private final String nullDescription = "null";
    private final String differentSizeDescription = "different size";
    private final String equalityDescription = "equality";
    private final String otherMessage = "other";
    private final String differentSizeMessage = "expected equal sizes but actual 4 != 5";
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
            .as(equalityDescription).isEqualTo(vector.subtract(other).taxicabNorm())));
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
            .as(equalityDescription).isEqualTo(vector.subtract(other).euclideanNormPow2())));
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
            .as(equalityDescription).isEqualTo(vector.subtract(other).euclideanNorm())));
    }

    @Test
    public void euclideanDistanceContextOtherNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(null, AbstractVector.DEFAULT_SQUARE_ROOT_CONTEXT))
            .as(nullDescription).isExactlyInstanceOf(NullPointerException.class).hasMessage(otherMessage);
    }

    @Test
    public void euclideanDistanceContextContextNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.euclideanDistance(zeroVector, null)).as(nullDescription)
            .isExactlyInstanceOf(NullPointerException.class).hasMessage("squareRootContext");
    }

    @Test
    public void euclideanDistanceContextDifferentSizeShouldThrowException() {
        assertThatThrownBy(
            () -> zeroVector.euclideanDistance(vectorDifferentSize, AbstractVector.DEFAULT_SQUARE_ROOT_CONTEXT))
                .as(differentSizeDescription).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(differentSizeMessage);
    }

    @Test
    public void euclideanDistanceContextShouldSucceed() {
        vectors.forEach(vector -> others.forEach(
            other -> softly.assertThat(vector.euclideanDistance(other, AbstractVector.DEFAULT_SQUARE_ROOT_CONTEXT))
                .as(equalityDescription)
                .isEqualTo(vector.subtract(other).euclideanNorm(AbstractVector.DEFAULT_SQUARE_ROOT_CONTEXT))));
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
            .as(equalityDescription).isEqualTo(vector.subtract(other).maxNorm())));
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
            i -> softly.assertThat(vector.element(i)).as(equalityDescription).isEqualTo(vector.getMap().get(i))));
    }

    @Test
    public void entriesShouldSucceed() {
        vectors.forEach(vector -> softly.assertThat(vector.entries()).as(equalityDescription)
            .isEqualTo(vector.getMap().entrySet()));
    }

    @Test
    public void elementsShouldSucceed() {
        vectors.forEach(
            vector -> softly.assertThat(vector.elements()).as(equalityDescription).isEqualTo(vector.getMap().values()));
    }

    @Test
    public void sizeShouldSucceed() {
        vectors.forEach(
            vector -> softly.assertThat(vector.size()).as(equalityDescription).isEqualTo(vector.getMap().size()));
    }

    @Test
    public void hashCodeEqualObjectsShouldHaveEqualHashCodes() {
        vectors.forEach(vector -> softly.assertThat(vector.hashCode()).as(equalityDescription)
            .isEqualTo(Vectors.copyOf(vector).hashCode()));
    }

    @Test
    public void hashCodeShouldSucceedd() {
        vectors.forEach(vector -> softly.assertThat(vector.hashCode()).as(equalityDescription)
            .isEqualTo(Objects.hash(vector.getMap())));
    }

    @Test
    @SuppressWarnings("SelfEquals")
    public void equalsSameObjectsShouldReturnTrue() {
        vectors.forEach(vector -> softly.assertThat(vector.equals(vector)).as(equalityDescription).isTrue());
    }

    @Test
    public void equalsNullShouldReturnFalse() {
        vectors.forEach(vector -> softly.assertThat(vector.equals(null)).as(equalityDescription).isFalse());
    }

    @Test
    public void equalsEqualObjectsShouldReturnTrue() {
        vectors.forEach(
            vector -> softly.assertThat(vector.equals(Vectors.copyOf(vector))).as(equalityDescription).isTrue());
    }

    @Test
    public void toStringShouldSucceed() {
        vectors.forEach(vector -> softly.assertThat(vector.toString()).as(equalityDescription)
            .isEqualTo(MoreObjects.toStringHelper(vector).add("map", vector.getMap()).toString()));
    }

    @After
    public void tearDown() {
        softly.assertAll();
    }
}
