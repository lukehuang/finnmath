package finnmath.asserts;

import finnmath.linear.DecimalVector;
import org.assertj.core.api.AbstractAssert;

public final class DecimalVectorAssert extends AbstractAssert<DecimalVectorAssert, DecimalVector> {

    private DecimalVectorAssert(final DecimalVector actual) {
        super(actual, DecimalVectorAssert.class);
    }

    public static DecimalVectorAssert assertThat(final DecimalVector actual) {
        return new DecimalVectorAssert(actual);
    }

    public DecimalVectorAssert isEqualToByBigDecimalComparator(final DecimalVector expected) {
        isNotNull();
        actual.entries().forEach(entry -> {
            final int compareTo = entry.getValue().compareTo(expected.entry(entry.getKey()));
            if (compareTo != 0) {
                failWithMessage("expected compareTo == 0 but actual %s", compareTo);
            }
        });
        return this;
    }
}
