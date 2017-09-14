package finnmath.asserts;

import finnmath.linear.DecimalMatrix;
import org.assertj.core.api.AbstractAssert;

public final class DecimalMatrixAssert extends AbstractAssert<DecimalMatrixAssert, DecimalMatrix> {

    private DecimalMatrixAssert(final DecimalMatrix actual) {
        super(actual, DecimalMatrixAssert.class);
    }

    public static DecimalMatrixAssert assertThat(final DecimalMatrix actual) {
        return new DecimalMatrixAssert(actual);
    }

    public DecimalMatrixAssert isEqualToByBigDecimalComparator(final DecimalMatrix expected) {
        isNotNull();
        actual.cells().forEach(cell -> {
            final int compareTo = cell.getValue().compareTo(expected.entry(cell.getRowKey(), cell.getColumnKey()));
            if (compareTo != 0) {
                failWithMessage("expected compareTo == 0 but actual %s", compareTo);
            }
        });
        return this;
    }
}
