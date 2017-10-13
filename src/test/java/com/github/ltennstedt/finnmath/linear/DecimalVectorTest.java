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

package com.github.ltennstedt.finnmath.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.ltennstedt.finnmath.assertion.DecimalVectorAssert;
import com.github.ltennstedt.finnmath.linear.DecimalVector.DecimalVectorBuilder;
import com.github.ltennstedt.finnmath.util.MathRandom;
import com.github.ltennstedt.finnmath.util.SquareRootCalculator;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public final class DecimalVectorTest {
    private static final int size = RandomUtils.nextInt(3, 10);
    private static final int howMany = 10;
    private static final long bound = 10;
    private static final int scale = 2;
    private static final int differentSize = size + 1;
    private static final BigDecimal precision = BigDecimal.valueOf(0.00001);
    private static final RoundingMode roundingMode = RoundingMode.HALF_DOWN;
    private static final DecimalVector zeroVector = DecimalVector.builder(size)
            .putAll(BigDecimal.ZERO).build();
    private static final DecimalVector vectorWithAnotherSize = DecimalVector.builder(differentSize)
            .putAll(BigDecimal.ZERO).build();
    private static final List<DecimalVector> vectors = new ArrayList<>(howMany);
    private static final List<DecimalVector> others = new ArrayList<>(howMany);
    private static final List<DecimalVector> additionalOthers = new ArrayList<>(howMany);
    private static final List<BigDecimal> scalars = new ArrayList<>(howMany);
    private static final List<BigDecimal> otherScalars = new ArrayList<>(howMany);
    private final BigDecimal tolerance = BigDecimal.valueOf(0.001D);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom(7);
        for (int i = 0; i < howMany; i++) {
            vectors.add(mathRandom.nextDecimalVector(bound, scale, size));
            others.add(mathRandom.nextDecimalVector(bound, scale, size));
            additionalOthers.add(mathRandom.nextDecimalVector(bound, scale, size));
            scalars.add(mathRandom.nextDecimal(bound, scale));
            otherScalars.add(mathRandom.nextDecimal(bound, scale));
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
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().add(other.element(element.getKey())));
                });
                DecimalVectorAssert.assertThat(vector.add(other))
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                DecimalVectorAssert.assertThat(vector.add(other))
                        .isEqualToByBigDecimalComparator(other.add(vector));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    DecimalVectorAssert.assertThat(vector.add(other).add(additionalOther))
                            .isEqualToByBigDecimalComparator(
                                    vector.add(other.add(additionalOther)));
                });
            });
        });
    }

    @Test
    public void addZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.add(zeroVector))
                    .isEqualToByBigDecimalComparator(vector);
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
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().subtract(other.element(element.getKey())));
                });
                DecimalVectorAssert.assertThat(vector.subtract(other))
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.subtract(zeroVector))
                    .isEqualToByBigDecimalComparator(vector);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.subtract(vector))
                    .isEqualToByBigDecimalComparator(zeroVector);
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
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(scalar.multiply(element.getValue()));
                });
                DecimalVectorAssert.assertThat(vector.scalarMultiply(scalar))
                        .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    DecimalVectorAssert
                            .assertThat(vector.scalarMultiply(scalar.multiply(otherScalar)))
                            .isEqualToByBigDecimalComparator(
                                    vector.scalarMultiply(otherScalar).scalarMultiply(scalar));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoScalarsShouldBeDistributive() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    DecimalVectorAssert.assertThat(vector.scalarMultiply(scalar.add(otherScalar)))
                            .isEqualToByBigDecimalComparator(vector.scalarMultiply(scalar)
                                    .add(vector.scalarMultiply(otherScalar)));
                });
            });
        });
    }

    @Test
    public void addAndScalarMultiplyWithTwoVectorsShouldBeDistributive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                scalars.forEach(scalar -> {
                    DecimalVectorAssert.assertThat(vector.add(other).scalarMultiply(scalar))
                            .isEqualToByBigDecimalComparator(vector.scalarMultiply(scalar)
                                    .add(other.scalarMultiply(scalar)));
                });
            });
        });
    }

    @Test
    public void scalarMultiplyWithZeroShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.scalarMultiply(BigDecimal.ZERO))
                    .isEqualToByBigDecimalComparator(zeroVector);
        });
    }

    @Test
    public void scalarMultiplyWithOneShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.scalarMultiply(BigDecimal.ONE))
                    .isEqualToByBigDecimalComparator(vector);
        });
    }

    @Test
    public void negateShouldSucceed() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.negate()).isEqualToByBigDecimalComparator(
                    vector.scalarMultiply(BigDecimal.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        DecimalVectorAssert.assertThat(zeroVector.negate())
                .isEqualToByBigDecimalComparator(zeroVector);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.negate().negate())
                    .isEqualToByBigDecimalComparator(vector);
        });
    }

    @Test
    public void addNegatedShouldBeEqualToZeroMatrix() {
        vectors.forEach(vector -> {
            vector.add(vector.negate()).entries().forEach(element -> {
                assertThat(element.getValue().compareTo(BigDecimal.ZERO)).isEqualTo(0);
            });
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.scalarMultiply(BigDecimal.ONE.negate()))
                    .isEqualToByBigDecimalComparator(vector.negate());
        });
    }

    @Test
    public void taxicabNormShouldSucceed() {
        vectors.forEach(vector -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final BigDecimal element : vector.elements()) {
                expected = expected.add(element.abs());
            }
            assertThat(vector.taxicabNorm()).isEqualTo(expected);
        });
    }

    @Test
    public void taxicabNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.taxicabNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void taxicabNormShouldBePositive() {
        vectors.forEach(vector -> {
            assertThat(vector.taxicabNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void taxicabNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                assertThat(vector.scalarMultiply(scalar).taxicabNorm())
                        .isEqualTo(scalar.abs().multiply(vector.taxicabNorm()));
            });
        });
    }

    @Test
    public void taxicabNormShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.add(other).taxicabNorm())
                        .isLessThanOrEqualTo(vector.taxicabNorm().add(other.taxicabNorm()));
            });
        });
    }

    @Test
    public void taxicabDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.taxicabDistance(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void taxicabDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.taxicabDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void taxicabDistanceShouldBeSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.taxicabDistance(other))
                        .isEqualTo(vector.subtract(other).taxicabNorm());
            });
        });
    }

    @Test
    public void taxicabDistanceFromVectorToSelfShouldBeEqualToZero() {
        vectors.forEach(vector -> {
            assertThat(vector.taxicabDistance(vector)).isEqualByComparingTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void taxicabDistanceShouldBePositive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.taxicabDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO);
            });
        });
    }

    @Test
    public void taxicabDistanceShouldBeSymmetric() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.taxicabDistance(other)).isEqualTo(other.taxicabDistance(vector));
            });
        });
    }

    @Test
    public void taxicabDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    assertThat(vector.taxicabDistance(additionalOther)).isLessThanOrEqualTo(vector
                            .taxicabDistance(other).add(other.taxicabDistance(additionalOther)));
                });
            });
        });
    }

    @Test
    public void euclideanNormPow2ShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNormPow2()).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(vector.dotProduct(vector));
        });
    }

    @Test
    public void euclideanNormPow2ZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.euclideanNormPow2()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPow2NormShouldBeHomogeneous() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                assertThat(vector.scalarMultiply(scalar).euclideanNormPow2())
                        .isEqualByComparingTo(scalar.pow(2).multiply(vector.euclideanNormPow2()));
            });
        });
    }

    @Test
    public void euclideanNormShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm()).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(new SquareRootCalculator().sqrt(vector.euclideanNormPow2()));
        });
    }

    @Test
    public void euclideanNormShouldBePositive() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void euclideanNormShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.add(other).euclideanNorm()).isLessThanOrEqualTo(
                        vector.euclideanNorm().add(other.euclideanNorm()).add(tolerance));
            });
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
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void euclideanNormWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(precision)).isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(
                            new SquareRootCalculator(precision).sqrt(vector.euclideanNormPow2()));
        });
    }

    @Test
    public void euclideanNormScaleTooLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(-1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormWithScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(scale, roundingMode))
                    .isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(new SquareRootCalculator(scale, roundingMode)
                            .sqrt(vector.euclideanNormPow2()));
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
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ZERO);
    }

    @Test
    public void euclideanNormPrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(BigDecimal.ONE, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual %s", BigDecimal.ONE);
    }

    @Test
    public void euclideanNormScaleTooLowAndPrecisionAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanNorm(precision, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanNormWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanNorm(precision, scale, roundingMode))
                    .isExactlyInstanceOf(BigDecimal.class)
                    .isEqualTo(new SquareRootCalculator(precision, scale, roundingMode)
                            .sqrt(vector.euclideanNormPow2()));
        });
    }

    @Test
    public void dotProductNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.dotProduct(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void dotProductSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.dotProduct(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void dotProductShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                BigDecimal expected = BigDecimal.ZERO;
                for (final Entry<Integer, BigDecimal> entry : vector.entries()) {
                    expected = expected
                            .add(entry.getValue().multiply(other.element(entry.getKey())));
                }
                assertThat(vector.dotProduct(other)).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(expected);
            });
        });
    }

    @Test
    public void euclideanDistancePow2NullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistancePow2(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistancePow2SizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistancePow2(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistancePow2ShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(entry -> {
                    builder.put(entry.getValue().subtract(other.element(entry.getKey())));
                });
                assertThat(vector.euclideanDistancePow2(other))
                        .isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(builder.build().euclideanNormPow2());
            });
        });
    }

    @Test
    public void euclideanDistancePow2FromVectorToSelfShouldBeEqualToZero() {
        vectors.forEach(vector -> {
            assertThat(vector.euclideanDistancePow2(vector)).isEqualByComparingTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void euclideanDistancePow2AbsShouldBeSymmetric() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistancePow2(other).abs())
                        .isEqualTo(other.euclideanDistancePow2(vector).abs());
            });
        });
    }

    @Test
    public void euclideanDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other)).isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(new SquareRootCalculator()
                                .sqrt(vector.euclideanDistancePow2(other)));
            });
        });
    }

    @Test
    public void euclideanDistanceShouldBePositive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO);
            });
        });
    }

    @Test
    public void euclideanDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    assertThat(vector.euclideanDistance(additionalOther))
                            .isLessThanOrEqualTo(vector.euclideanDistance(other)
                                    .add(other.euclideanDistance(additionalOther)).add(tolerance));
                });
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithPrecisionShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, precision);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
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
            zeroVector.euclideanDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build(),
                    precision);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistancePrecisionTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, precision))
                        .isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(new SquareRootCalculator(precision)
                                .sqrt(vector.euclideanDistancePow2(other)));
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
    }

    @Test
    public void euclideanDistanceSizesNotEqualWithScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build(), scale,
                    roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, scale, roundingMode))
                        .isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(new SquareRootCalculator(scale, roundingMode)
                                .sqrt(vector.euclideanDistancePow2(other)));
            });
        });
    }

    @Test
    public void euclideanDistanceNullWithPrecisionAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(null, precision, scale, roundingMode);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("other");
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
            zeroVector.euclideanDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build(), precision,
                    scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void euclideanDistanceWithPrecisionTooLowAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ZERO, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 0");
    }

    @Test
    public void euclideanDistancePrecisionTooHighAndScaleAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, BigDecimal.ONE, scale, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected precision in (0, 1) but actual 1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleToLowAndRoundingModeShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.euclideanDistance(zeroVector, precision, -1, roundingMode);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected scale >= 0 but actual -1");
    }

    @Test
    public void euclideanDistanceWithPrecisionAndScaleAndRoundingModeShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.euclideanDistance(other, precision, scale, roundingMode))
                        .isExactlyInstanceOf(BigDecimal.class)
                        .isEqualTo(new SquareRootCalculator(precision, scale, roundingMode)
                                .sqrt(vector.euclideanDistancePow2(other)));
            });
        });
    }

    @Test
    public void maxNormShouldSucceed() {
        vectors.forEach(vector -> {
            BigDecimal expected = BigDecimal.ZERO;
            for (final BigDecimal element : vector.elements()) {
                expected = expected.max(element.abs());
            }
            assertThat(vector.maxNorm()).isEqualByComparingTo(expected);
        });
    }

    @Test
    public void maxNormZeroVectorShouldBeEqualToZero() {
        assertThat(zeroVector.maxNorm()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    public void maxNormShouldBePositive() {
        vectors.forEach(vector -> {
            assertThat(vector.maxNorm()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void maxNormShouldBeAbsolutelyHomogeneous() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                assertThat(vector.scalarMultiply(scalar).maxNorm())
                        .isEqualByComparingTo(scalar.abs().multiply(vector.maxNorm()));
            });
        });
    }

    @Test
    public void maxNormShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.add(other).maxNorm())
                        .isLessThanOrEqualTo(vector.maxNorm().add(other.maxNorm()));
            });
        });
    }

    @Test
    public void maxDistanceNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.maxDistance(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("vector");
    }

    @Test
    public void maxDistanceSizesNotEqualShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.maxDistance(
                    DecimalVector.builder(differentSize).putAll(BigDecimal.ZERO).build());
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected equal sizes but actual %s != %s", size, differentSize);
    }

    @Test
    public void maxDistanceShouldBeSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.maxDistance(other)).isEqualTo(vector.subtract(other).maxNorm());
            });
        });
    }

    @Test
    public void maxDistanceFromVectorToSelfShouldBeEqualToZero() {
        vectors.forEach(vector -> {
            assertThat(vector.maxDistance(vector)).isEqualByComparingTo(BigDecimal.ZERO);
        });
    }

    @Test
    public void maxDistanceShouldBePositive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.maxDistance(other)).isGreaterThanOrEqualTo(BigDecimal.ZERO);
            });
        });
    }

    @Test
    public void maxDistanceShouldBeSymmetric() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                assertThat(vector.maxDistance(other)).isEqualTo(other.maxDistance(vector));
            });
        });
    }

    @Test
    public void maxDistanceShouldBeSubadditive() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    assertThat(vector.maxDistance(additionalOther)).isLessThanOrEqualTo(
                            vector.maxDistance(other).add(other.maxDistance(additionalOther)));
                });
            });
        });
    }

    @Test
    public void elementNullShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.element(null);
        }).isExactlyInstanceOf(NullPointerException.class).hasMessage("index");
    }

    @Test
    public void elementIndexOutOfBoundShouldThrowException() {
        assertThatThrownBy(() -> {
            zeroVector.element(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected index in [1, %s] but actual 0", zeroVector.size());
    }

    @Test
    public void elementShouldSucceed() {
        vectors.forEach(vector -> {
            IntStream.rangeClosed(1, size).boxed().collect(Collectors.toList()).forEach(index -> {
                assertThat(vector.element(index)).isEqualTo(vector.getMap().get(index));
            });
        });
    }

    @Test
    public void entriesShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.entries()).isEqualTo(vector.getMap().entrySet());
        });
    }

    @Test
    public void elementsShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.elements()).isEqualTo(vector.getMap().values());
        });
    }

    @Test
    public void sizeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.size()).isEqualTo(vector.getMap().size());
        });
    }

    @Test
    public void builderSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalVector.builder(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.hashCode()).isExactlyInstanceOf(Integer.class)
                    .isEqualTo(Objects.hash(vector.getMap()));
        });
    }

    @Test
    public void equalsSelfShouldReturnTrue() {
        vectors.forEach(vector -> {
            assertThat(vector.equals(vector)).isTrue();
        });
    }

    @Test
    public void equalsNotDecimalVectorShouldReturnFalse() {
        assertThat(zeroVector.equals(new Object())).isFalse();
    }

    @Test
    public void equalsNotEqualShouldReturnFalse() {
        vectors.forEach(vector -> {
            final DecimalVectorBuilder builder = DecimalVector.builder(size);
            for (int i = 0; i < size; i++) {
                builder.put(BigDecimal.ONE);
            }
            assertThat(vector.equals(vector.add(builder.build()))).isFalse();
        });
    }

    @Test
    public void equalsEqualShouldReturnTrue() {
        vectors.forEach(vector -> {
            final DecimalVectorBuilder builder = DecimalVector.builder(size);
            vector.elements().forEach(element -> {
                builder.put(element);
            });
            assertThat(vector.equals(builder.build())).isTrue();
        });
    }

    @Test
    public void toStringShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.toString()).isEqualTo(
                    MoreObjects.toStringHelper(vector).add("map", vector.getMap()).toString());
        });
    }
}
