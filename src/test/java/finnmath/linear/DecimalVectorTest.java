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
import finnmath.assertion.DecimalVectorAssert;
import finnmath.linear.DecimalVector.DecimalVectorBuilder;
import finnmath.util.MathRandom;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public final class DecimalVectorTest {
    private static final int size = RandomUtils.nextInt(3, 10);
    private static final int howMany = 10;
    private static final long bound = 10;
    private static final int scale = 2;
    private static final int anotherSize = size + 1;
    private static final DecimalVector zeroVector = DecimalVector.builder(size).putAll(BigDecimal.ZERO).build();
    private static final DecimalVector vectorWithAnotherSize = DecimalVector.builder(anotherSize)
                    .putAll(BigDecimal.ZERO).build();
    private static final List<DecimalVector> vectors = new ArrayList<>(howMany);
    private static final List<DecimalVector> others = new ArrayList<>(howMany);
    private static final List<DecimalVector> additionalOthers = new ArrayList<>(howMany);
    private static final List<BigDecimal> scalars = new ArrayList<>(howMany);
    private static final List<BigDecimal> otherScalars = new ArrayList<>(howMany);

    @BeforeClass
    public static void setUpClass() {
        final MathRandom mathRandom = new MathRandom();
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
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                        size, anotherSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().add(other.element(element.getKey())));
                });
                DecimalVectorAssert.assertThat(vector.add(other)).isExactlyInstanceOf(DecimalVector.class)
                                .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void addShouldBeCommutative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                DecimalVectorAssert.assertThat(vector.add(other)).isEqualToByBigDecimalComparator(other.add(vector));
            });
        });
    }

    @Test
    public void addShouldBeAssociative() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                additionalOthers.forEach(additionalOther -> {
                    DecimalVectorAssert.assertThat(vector.add(other).add(additionalOther))
                                    .isEqualToByBigDecimalComparator(vector.add(other.add(additionalOther)));
                });
            });
        });
    }

    @Test
    public void addZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.add(zeroVector)).isEqualToByBigDecimalComparator(vector);
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
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected equal sizes but actual %s != %s",
                        size, anotherSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final DecimalVectorBuilder builder = DecimalVector.builder(size);
                vector.entries().forEach(element -> {
                    builder.put(element.getValue().subtract(other.element(element.getKey())));
                });
                DecimalVectorAssert.assertThat(vector.subtract(other)).isExactlyInstanceOf(DecimalVector.class)
                                .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void subtractZeroVectorShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.subtract(zeroVector)).isEqualToByBigDecimalComparator(vector);
        });
    }

    @Test
    public void subtractSelfShouldBeEqualToZeroVector() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.subtract(vector)).isEqualToByBigDecimalComparator(zeroVector);
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
                DecimalVectorAssert.assertThat(vector.scalarMultiply(scalar)).isExactlyInstanceOf(DecimalVector.class)
                                .isEqualToByBigDecimalComparator(builder.build());
            });
        });
    }

    @Test
    public void scalarMultiplyWithTwoScalarsShouldBeAssociative() {
        vectors.forEach(vector -> {
            scalars.forEach(scalar -> {
                otherScalars.forEach(otherScalar -> {
                    DecimalVectorAssert.assertThat(vector.scalarMultiply(scalar.multiply(otherScalar)))
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
                                    .isEqualToByBigDecimalComparator(
                                                    vector.scalarMultiply(scalar).add(other.scalarMultiply(scalar)));
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
            DecimalVectorAssert.assertThat(vector.negate()).isExactlyInstanceOf(DecimalVector.class)
                            .isEqualToByBigDecimalComparator(vector.scalarMultiply(BigDecimal.ONE.negate()));
        });
    }

    @Test
    public void negateZeroMatrixShouldBeEqualToSelf() {
        DecimalVectorAssert.assertThat(zeroVector.negate()).isEqualToByBigDecimalComparator(zeroVector);
    }

    @Test
    public void negateTwiceShouldBeEqualToSelf() {
        vectors.forEach(vector -> {
            DecimalVectorAssert.assertThat(vector.negate().negate()).isEqualToByBigDecimalComparator(vector);
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
    public void builderRowIndexTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalMatrix.builder(0, 1);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected row size > 0 but actual 0");
    }

    @Test
    public void builderSizeTooLowShouldThrowException() {
        assertThatThrownBy(() -> {
            DecimalVector.builder(0);
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("expected size > 0 but actual 0");
    }

    @Test
    public void hashCodeShouldSucceed() {
        vectors.forEach(vector -> {
            assertThat(vector.hashCode()).isExactlyInstanceOf(Integer.class).isEqualTo(Objects.hash(vector.getMap()));
        });
    }

    @Test
    public void equalsNotSimpleComplexNumberShouldReturnFalse() {
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
            assertThat(vector.toString()).isExactlyInstanceOf(String.class)
                            .isEqualTo(MoreObjects.toStringHelper(vector).add("map", vector.getMap()).toString());
        });
    }
}
