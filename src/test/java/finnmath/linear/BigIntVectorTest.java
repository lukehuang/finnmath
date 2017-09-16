package finnmath.linear;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finnmath.linear.BigIntVector.BigIntVectorBuilder;
import finnmath.util.MathRandom;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public final class BigIntVectorTest {
    private static final int size = RandomUtils.nextInt(3, 10);
    private static final int howMany = 10;
    private static final long bound = 10;
    private static final int anotherSize = size + 1;
    private static final BigIntVector zeroVector = BigIntVector.builder(size).putAll(BigInteger.ZERO).build();
    private static final BigIntVector vectorWithAnotherSize = BigIntVector.builder(anotherSize).putAll(BigInteger.ZERO)
            .build();
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
                size, anotherSize);
    }

    @Test
    public void addShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(entry -> {
                    builder.put(entry.getValue().add(other.entry(entry.getKey())));
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
                size, anotherSize);
    }

    @Test
    public void subtractShouldSucceed() {
        vectors.forEach(vector -> {
            others.forEach(other -> {
                final BigIntVectorBuilder builder = BigIntVector.builder(size);
                vector.entries().forEach(entry -> {
                    builder.put(entry.getValue().subtract(other.entry(entry.getKey())));
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
                vector.entries().forEach(entry -> {
                    builder.put(scalar.multiply(entry.getValue()));
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
            vector.add(vector.negate()).entries().forEach(entry -> {
                assertThat(entry.getValue()).isEqualTo(BigInteger.ZERO);
            });
        });
    }

    @Test
    public void scalarMultiplyWithMinusOneShouldBeEqualToNegated() {
        vectors.forEach(vector -> {
            assertThat(vector.scalarMultiply(BigInteger.ONE.negate())).isEqualTo(vector.negate());
        });
    }
}
