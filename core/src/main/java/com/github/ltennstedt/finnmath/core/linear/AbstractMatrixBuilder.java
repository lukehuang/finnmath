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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.Builder;

/**
 * Base class for matrix builders
 *
 * @param <E>
 *            The type of the elements of the matrix
 * @param <M>
 *            The type of the matrix
 * @param <B>
 *            The type of the builder
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public abstract class AbstractMatrixBuilder<E, M extends AbstractMatrix<E, ?, M, ?, ?>,
    B extends AbstractMatrixBuilder<E, M, B>> implements Builder<M> {
    /**
     * The table holding the elements of this {@link AbstractVectorBuilder}
     *
     * @since 1
     */
    protected final Table<Integer, Integer, E> table;

    protected AbstractMatrixBuilder(final int rowSize, final int columnSize) {
        checkArgument(rowSize > 0, "expected rowSize > 0 but actual %s", rowSize);
        checkArgument(columnSize > 0, "expected columnSize > 0 but actual %s", columnSize);
        final List<Integer> rowIndexes = IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList());
        final List<Integer> columnIndexes = IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList());
        table = ArrayTable.create(rowIndexes, columnIndexes);
    }

    /**
     * Puts the given element on the {@link Table} dependent on the given row and
     * column index
     *
     * @param rowIndex
     *            thr row index
     * @param columnIndex
     *            the column index
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code rowIndex == null}
     * @throws NullPointerException
     *             if {@code columnIndex == null}
     * @throws NullPointerException
     *             if {@code element == null}
     * @throws IllegalArgumentException
     *             if {@code rowIndex < 1 || rowSize < rowIndex}
     * @throws IllegalArgumentException
     *             if {@code columnIndex < 1 || columnSize < columnIndex}
     * @since 1
     */
    public final B put(final Integer rowIndex, final Integer columnIndex, final E element) {
        requireNonNull(element, "element");
        requireNonNull(rowIndex, "rowIndex");
        requireNonNull(columnIndex, "columnIndex");
        checkArgument(table.rowKeySet().contains(rowIndex), "expected rowIndex in [1, %s] but actual %s",
            table.rowKeySet().size(), rowIndex);
        checkArgument(table.columnKeySet().contains(columnIndex), "expected columnIndex in [1, %s] but actual %s",
            table.columnKeySet().size(), columnIndex);
        table.put(rowIndex, columnIndex, element);

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    /**
     * Puts the given element on the {@link Table} on all indexes which contains
     * {@code null} values
     *
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code element == null}
     * @since 1
     */
    public final B nullsToElement(final E element) {
        requireNonNull(element, "element");
        table.cellSet().forEach(cell -> {
            if (cell.getValue() == null) {
                table.put(cell.getRowKey(), cell.getColumnKey(), element);
            }
        });

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    /**
     * Puts the given element on all indices and returns {@code this}
     *
     * @param element
     *            the element
     * @return {@code this}
     * @throws NullPointerException
     *             if {@code element == null}
     * @since 1
     */
    public final B putAll(final E element) {
        requireNonNull(element, "element");
        table.rowKeySet()
            .forEach(rowKey -> table.columnKeySet().forEach(columnKey -> table.put(rowKey, columnKey, element)));

        @SuppressWarnings("unchecked")
        final B builder = (B) this;

        return builder;
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("table", table).toString();
    }

    public final Table<Integer, Integer, E> getTable() {
        return table;
    }
}
