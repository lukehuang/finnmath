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

package finnmath.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.base.MoreObjects;
import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import finnmath.util.MathRandom;
import finnmath.util.SquareRootCalculator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public final class BigIntVectorTest {
    private static final int size = RandomUtils.nextInt(3, 10);
    private static final int howMany = 10;
    private static final long bound = 10;
    private static final int differentSize = size + 1;
    private static final BigDecimal precision = BigDecimal.valueOf(0.00001);
    private static final int scale = 7;
    private static final int roundingMode = BigDecimal.ROUND_HALF_DOWN;
    private static final BigIntVector zeroVector = BigIntVector.builder(size).putAll(BigInteger.ZERO).build();
    private static final BigIntVector vectorWithAnotherSize = BigIntVector.builder(differentSize)
            .putAll(BigInteger.ZERO).build();
    private static final List<BigIntVector> vectors = new ArrayList<>(howMany);
    private static final List<BigIntVector> others = new ArrayList<>(howMany);
    private static final List<BigIntVector> additionalOthers = new ArrayList<>(howMany);
    private static final List<BigInteger> scalars = new ArrayList<>(howMany);
    private static final List<BigInteger> otherScalars = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom();
        for (int i = 0; i < howMany; i++) {
            vectors.add(mathRandom.nextBigIntVector(bound, size));
            others.add(mathRandom.nextBigIntVector(bound, size));
            additionalOthers.add(mathRandom.nextBigIntVector(bound, size));
            scalars.add(BigInteger.valueOf(RandomUtils.nextLong(0, bound)));
            otherScalars.add(BigInteger.valueOf(RandomUtils.nextLong(0, bound)));
        }
    }

    @Test
    public void addNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.add(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("summand");
    }

    @Test
    public void addSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.add(vectorWithAnotherSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("equal sizes expected but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().add(other.element(element.getKey())));
                });
                assertThat(vector.add(other)).isExactlyInstanceOf(BigIntVector.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.add(other)).isEqualTo(other.add(vector));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    assertThat(vector.add(other).add(additionalOther))
                            .isEqualTo(vector.add(other.add(additionalOther)));
                });
            });
        });
    }

    @Test
    public void addZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            assertThat(vector.add(zeroVector)).isEqualTo(vector);
        });
    }

    @Test
    public void subtractNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.subtract(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("subtrahend");
    }

    @Test
    public void subtractSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.subtract(vectorWithAnotherSize);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("equal sizes expected but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().subtract(other.element(element.getKey())));
                });
                assertThat(vector.subtract(other)).isExactlyInstanceOf(BigIntVector.class).isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            assertThat(vector.subtract(zeroVector)).isEqualTo(vector);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> {
            assertThat(vector.subtract(vector)).isEqualTo(zeroVector);
        });
    }

    @Test
    public void scalarMultiplyNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.scalarMultiply(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("scalar");
    }

    @Test
    public void scalarMultiplyShouldSucceed() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(scalar.multiply(element.getValue()));
                });
                assertThat(vector.scalarMultiply(scalar)).isExactlyInstanceOf(BigIntVector.class)
                        .isEqualTo(builder.build());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(vector.scalarMultiply(scalar.multiply(otherScalar)))
                            .isEqualTo(vector.scalarMultiply(otherScalar).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    assertThat(vector.scalarMultiply(scalar.add(otherScalar)))
                            .isEqualTo(vector.scalarMultiply(scalar).add(vector.scalarMultiply(otherScalar)));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoVectorsShouldBeDistributive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                scalars.forEach(scalar -> {
                    assertThat(vector.add(other).scalarMultiply(scalar))
                            .isEqualTo(vector.scalarMultiply(scalar).add(other.scalarMultiply(scalar)));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> {
            assertThat(vector.scalarMultiply(BigInteger.ZERO)).isEqualTo(zeroVector);
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            assertThat(vector.scalarMultiply(BigInteger.ONE)).isEqualTo(vector);
        });
    }

    @Test
    public void negateShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.negate()).isExactlyInstanceOf(BigIntVector.class)
                    .isEqualTo(vector.scalarMultiply(BigInteger.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        assertThat(zeroVector.negate()).isEqualTo(zeroVector);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            assertThat(vector.negate().negate()).isEqualTo(vector);
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> {
            vector.add(vector.negate()).entries().forEach(element -> {
                assertThat(element.getValue()).isEqualTo(BigInteger.ZERO);
            });
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        vectors.forEach(vector -> {
            assertThat(vector.scalarMultiply(BigInteger.ONE.negate())).isEqualTo(vector.negate());
        });
    }

    @Test
    public void euclideanNormPow2ShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNormPow2()).isExactlyInstanceOf(BigInteger.class)
                    .isEqualTo(vector.dotProduct(vector));
        });
    }

    @Test
    public void euclideanNormShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm()).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanNormPow2()));
        });
    }

    @Test
    public void euclideanNormPrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanNormPrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ONE);
    }

    @Test
    public void euclideanNormWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(precision)).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanNormPow2(), precision));
        });
    }

    @Test
    public void euclideanNormScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(-1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormRoundingModeTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(scale, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void euclideanNormRoundingModeTooHighAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(scale, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void euclideanNormWithScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(scale, roundingMode)).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanNormPow2(), scale, roundingMode));
        });
    }

    @Test
    public void euclideanNormPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(null, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanNormPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ZERO, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ONE, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual %s",
                BigDecimal.ONE);
    }

    @Test
    public void euclideanNormScaleTooLowAndPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(precision, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormRoundingModeTooLowAndPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(precision, scale, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void euclideanNormRoundingModeTooHighAndPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(precision, scale, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void euclideanNormWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(precision, scale, roundingMode)).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanNormPow2(), precision, scale, roundingMode));
        });
    }

    @Test
    public void dotProductNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.dotProduct(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void dotProductSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.dotProduct(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void dotProductShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                BigInteger expected = BigInteger.ZERO;
                for (final Entry<Integer, BigInteger> entry : vector.entries()) {
                    expected = expected.add(entry.getValue().multiply(other.element(entry.getKey())));
                }
                assertThat(vector.dotProduct(other)).isExactlyInstanceOf(BigInteger.class).isEqualTo(expected);
            });
        });
    }

    @Test
    public void euclideanDistancePow2NullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistancePow2(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void euclideanDistancePow2SizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistancePow2(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void euclideanDistancePow2ShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(entry -> {
                    builder.put(entry.getValue().subtract(other.element(entry.getKey())));
                });
                assertThat(vector.euclideanDistancePow2(other)).isExactlyInstanceOf(BigInteger.class)
                        .isEqualTo(builder.build().euclideanNormPow2());
            });
        });
    }

    @Test
    public void euclideanDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void euclideanDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void euclideanDistanceShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other)).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanDistancePow2(other)));
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, precision);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void euclideanDistancePrecisionNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build(),
                    precision);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void euclideanDistancePrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, precision)).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanDistancePow2(other), precision));
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build(), scale,
                    roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void euclideanDistanceScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithScaleAndRoundingTooLowModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, scale, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void euclideanDistanceWithScaleAndRoundingTooHighModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, scale, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void euclideanDistanceWithScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, scale, roundingMode)).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(SquareRootCalculator.sqrt(vector.euclideanDistancePow2(other), scale, roundingMode));
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, precision, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void euclideanDistanceWithPrecisionNullAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, null, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("precision");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(BigIntVector.builder(differentSize).putAll(BigInteger.ZERO).build(), precision,
                    scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                size, differentSize);
    }

    @Test
    public void euclideanDistanceWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, precision, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleAndRoundingTooLowModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, precision, scale, -1);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual -1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleAndRoundingTooHighModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, precision, scale, 8);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected roundingMode in [0, 7] but actual 8");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, precision, scale, roundingMode))
                        .isExactlyInstanceOf(BigDecimal.class).isEqualTo(SquareRootCalculator
                                .sqrt(vector.euclideanDistancePow2(other), precision, scale, roundingMode));
            });
        });
    }

    @Test
    public void builderSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            BigIntVector.builder(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.hashCode()).isExactlyInstanceOf(Integer.class).isEqualTo(Objects.hash(vector.getMap()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        vectors.forEach(vector -> {
            assertThat(vector.equals(vector)).isTrue();
        });
    }

    @Test
    public void equalsNotBigIntVectorShouldReturnFalse() {
        assertThat(zeroVector.equals(new Object())).isFalse();
    }

    @Test
    public void equalsNotEqualShouldReturnFalse() {
        vectors.forEach(vector -> {
            final BigIntVectorBuilder builder = BigIntVector.builder(size);
            for (int i = 0; i < size; i++) {
                builder.put(BigInteger.ONE);
            }
            assertThat(vector.equals(vector.add(builder.build()))).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        vectors.forEach(vector -> {
            final BigIntVectorBuilder builder = BigIntVector.builder(size);
            vector.elements().forEach(element -> {
                builder.put(element);
            });
            assertThat(vector.equals(builder.build())).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.toString()).isExactlyInstanceOf(String.class)
                    .isEqualTo(MoreObjects.toStringHelper(vector).add("map", vector.getMap()).toString());
        });
    }
}
