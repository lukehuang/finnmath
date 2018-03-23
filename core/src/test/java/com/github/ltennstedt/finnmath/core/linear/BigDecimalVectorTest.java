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

import com.github.ltennstedt.finnmath.core.linear.BigDecimalVector.BigDecimalVectorBuilder;
import com.github.ltennstedt.finnmath.core.util.MathRandom;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.Test;

public final class BigDecimalVectorTest {
    private final int size = 4;
    private final int howMany = 10;
    private final long bound = 10;
    private final int scale = 2;
    private final int differentSize = size + 1;
    private final MathRandom mathRandom = new MathRandom(7);
    private final BigDecimalVector zeroVector = Vectors.buildZeroBigDecimalVector(size);
    private final BigDecimalVector vectorWithAnotherSize = Vectors.buildZeroBigDecimalVector(differentSize);
    private final List<Integer> range = IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList());
    private final List<BigDecimalVector> vectors = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimalVector> others = mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimalVector> additionalOthers =
        mathRandom.nextBigDecimalVectors(bound, scale, size, howMany);
    private final List<BigDecimal> scalars = mathRandom.nextBigDecimals(bound, scale, howMany);
    private final List<BigDecimal> otherScalars = mathRandom.nextBigDecimals(bound, scale, howMany);

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.add(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("summand");
    }

    @Test
    public void addSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.add(vectorWithAnotherSize))
            .isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final BigDecimalVectorBuilder builder = BigDecimalVector.builder(size);
            vector.entries().forEach(element -> builder.put(element.getValue().add(other.element(element.getKey()))));
            assertThat(vector.add(other).getMap()).isEqualToComparingFieldByField(builder.build().getMap());
        }));
    }

    @Test
    public void addShouldBeCommutative() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).getMap())
            .isEqualToComparingFieldByField(other.add(vector).getMap())));
    }

    @Test
    public void addShouldBeAssociative() {
        vectors.forEach(vector -> others.forEach(other -> additionalOthers
            .forEach(additionalOther -> assertThat(vector.add(other).add(additionalOther).getMap())
                .usingComparatorForFields(BigDecimalComparator.BIG_DECIMAL_COMPARATOR)
                .isEqualToComparingFieldByField(vector.add(other.add(additionalOther)).getMap()))));
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
            .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final BigDecimalVectorBuilder builder = BigDecimalVector.builder(size);
            vector.entries()
                .forEach(element -> builder.put(element.getValue().subtract(other.element(element.getKey()))));
            assertThat(vector.subtract(other).getMap()).isEqualToComparingFieldByField(builder.build().getMap());
        }));
    }

    @Test
    public void subtractZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.subtract(zeroVector)).isEqualTo(vector));
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> {
            final Map<Integer, BigDecimal> actual = vector.subtract(vector).getMap();
            final Map<Integer, BigDecimal> expected = zeroVector.getMap();
            range.forEach(index -> assertThat(actual.get(index)).isEqualByComparingTo(expected.get(index)));
        });
    }

    @Test
    public void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.scalarMultiply(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        vectors.forEach(vector -> scalars.forEach(scalar -> {
            final BigDecimalVectorBuilder builder = BigDecimalVector.builder(size);
            vector.entries().forEach(element -> builder.put(scalar.multiply(element.getValue())));
            assertThat(vector.scalarMultiply(scalar).getMap()).isEqualToComparingFieldByField(builder.build().getMap());
        }));
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        vectors.forEach(vector -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(vector.scalarMultiply(scalar.multiply(otherScalar)).getMap())
                .usingComparatorForFields(BigDecimalComparator.BIG_DECIMAL_COMPARATOR)
                .isEqualToComparingFieldByField(vector.scalarMultiply(otherScalar).scalarMultiply(scalar).getMap()))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        vectors.forEach(vector -> scalars.forEach(scalar -> otherScalars
            .forEach(otherScalar -> assertThat(vector.scalarMultiply(scalar.add(otherScalar)).getMap())
                .usingComparatorForFields(BigDecimalComparator.BIG_DECIMAL_COMPARATOR).isEqualToComparingFieldByField(
                    vector.scalarMultiply(scalar).add(vector.scalarMultiply(otherScalar)).getMap()))));
    }

    @Test
    public void addAndScalarMultiplyWithTwoVectorsShouldBeDistributive() {
        vectors.forEach(vector -> others.forEach(other -> scalars.forEach(
            scalar -> assertThat(vector.add(other).scalarMultiply(scalar).getMap()).isEqualToComparingFieldByField(
                vector.scalarMultiply(scalar).add(other.scalarMultiply(scalar)).getMap()))));
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> {
            final Map<Integer, BigDecimal> actual = vector.scalarMultiply(BigDecimal.ZERO).getMap();
            final Map<Integer, BigDecimal> expected = zeroVector.getMap();
            range.forEach(index -> assertThat(actual.get(index)).isEqualByComparingTo(expected.get(index)));
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.scalarMultiply(BigDecimal.ONE)).isEqualTo(vector));
    }

    @Test
    public void negateShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.negate().getMap())
            .isEqualToComparingFieldByField(vector.scalarMultiply(BigDecimal.ONE.negate()).getMap()));
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroVector.negate().getMap()).isEqualTo(zeroVector.getMap());
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        vectors.forEach(vector -> assertThat(vector.negate().negate()).isEqualTo(vector));
    }

    @Test
    public void addNegatedShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> {
            final Map<Integer, BigDecimal> actual = vector.add(vector.negate()).getMap();
            final Map<Integer, BigDecimal> expected = zeroVector.getMap();
            range.forEach(index -> assertThat(actual.get(index)).isEqualByComparingTo(expected.get(index)));
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        vectors.forEach(vector -> assertThat(vector.scalarMultiply(BigDecimal.ONE.negate()).getMap())
            .isEqualToComparingFieldByField(vector.negate().getMap()));
    }

    @Test
    public void taxicabNormShouldSucceed() {
        vectors.forEach(vector -> {
            final BigDecimal expected = vector.elements().stream().map(BigDecimal::abs).reduce(BigDecimal::add).get();
            assertThat(vector.taxicabNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void taxicabNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.taxicabNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void taxicabNormShouldBePositive() {
        vectors.forEach(vector -> assertThat(vector.taxicabNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void taxicabNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> scalars.forEach(scalar -> assertThat(vector.scalarMultiply(scalar).taxicabNorm())
            .isEqualTo(scalar.abs().multiply(vector.taxicabNorm()))));
    }

    @Test
    public void taxicabNormShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).taxicabNorm())
            .isLessThanOrEqualTo(vector.taxicabNorm().add(other.taxicabNorm()))));
    }

    @Test
    public void euclideanNormPow2ShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.euclideanNormPow2()).isEqualTo(vector.dotProduct(vector)));
    }

    @Test
    public void euclideanNormPow2ZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.euclideanNormPow2()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPow2NormShouldBeHomogeneous() {
        vectors
            .forEach(vector -> scalars.forEach(scalar -> assertThat(vector.scalarMultiply(scalar).euclideanNormPow2())
                .isEqualByComparingTo(scalar.pow(2).multiply(vector.euclideanNormPow2()))));
    }

    @Test
    public void dotProductNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.dotProduct(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("other");
    }

    @Test
    public void dotProductSizesNotEqualShouldThrowException() {
        assertThatThrownBy(
            () -> zeroVector.dotProduct(BigDecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void dotProductShouldSucceed() {
        vectors.forEach(vector -> others.forEach(other -> {
            final BigDecimal expected = vector.entries().stream()
                .map(entry -> entry.getValue().multiply(other.element(entry.getKey()))).reduce(BigDecimal::add).get();
            assertThat(vector.dotProduct(other)).isEqualTo(expected);
        }));
    }

    @Test
    public void maxNormShouldSucceed() {
        vectors.forEach(vector -> {
            final BigDecimal expected = vector.elements().stream().map(BigDecimal::abs).reduce(BigDecimal::max).get();
            assertThat(vector.maxNorm()).isEqualByComparingTo(expected);
        });
    }

    @Test
    public void maxNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.maxNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void maxNormShouldBePositive() {
        vectors.forEach(vector -> assertThat(vector.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO));
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> scalars.forEach(scalar -> assertThat(vector.scalarMultiply(scalar).maxNorm())
            .isEqualByComparingTo(scalar.abs().multiply(vector.maxNorm()))));
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        vectors.forEach(vector -> others.forEach(other -> assertThat(vector.add(other).maxNorm())
            .isLessThanOrEqualTo(vector.maxNorm().add(other.maxNorm()))));
    }

    @Test
    public void elementNullShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.element(null)).isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("index");
    }

    @Test
    public void elementIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> zeroVector.element(0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected index in [1, %s] but actual 0", zeroVector.size());
    }

    @Test
    public void elementShouldSucceed() {
        vectors.forEach(vector -> IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList())
            .forEach(index -> assertThat(vector.element(index)).isEqualTo(vector.getMap().get(index))));
    }

    @Test
    public void entriesShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.entries()).isEqualTo(vector.getMap().entrySet()));
    }

    @Test
    public void elementsShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.elements()).isEqualTo(vector.getMap().values()));
    }

    @Test
    public void sizeShouldSucceed() {
        vectors.forEach(vector -> assertThat(vector.size()).isEqualTo(vector.getMap().size()));
    }

    @Test
    public void builderSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> BigDecimalVector.builder(0)).isExactlyInstanceOf(IllegalArgumentException.class)
            .hasMessage("expected size > 0 but actual 0");
    }
}
