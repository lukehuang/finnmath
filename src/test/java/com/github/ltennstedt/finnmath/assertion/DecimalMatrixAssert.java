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

package com.github.ltennstedt.finnmath.assertion;

import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.linear.DecimalMatrix;
import org.assertj.core.api.AbstractAssert;

public final class DecimalMatrixAssert extends AbstractAssert<DecimalMatrixAssert, DecimalMatrix> {
    private DecimalMatrixAssert(final DecimalMatrix actual) {
        super(actual, DecimalMatrixAssert.class);
    }

    public static DecimalMatrixAssert assertThat(final DecimalMatrix actual) {
        requireNonNull(actual, "actual");
        return new DecimalMatrixAssert(actual);
    }

    public DecimalMatrixAssert isEqualToByBigDecimalComparator(final DecimalMatrix expected) {
        isNotNull();
        actual.cells().forEach(cell -> {
            final int compareTo = cell.getValue()
                    .compareTo(expected.element(cell.getRowKey(), cell.getColumnKey()));
            if (compareTo != 0) {
                failWithMessage("expected compareTo == 0 but actual %s", compareTo);
            }
        });
        return this;
    }
}
