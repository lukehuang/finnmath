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

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.Builder;

/**
 * @param <E>
 *         The type of the elements of the matrix
 * @param <M>
 *         The type of the matrix
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
abstract class AbstractMatrixBuilder<E, M> implements Builder<M> {
    /**
     * The table holding the elements of this {@link AbstractVectorBuilder}
     *
     * @since 1
     */
    protected final Table<Integer, Integer, E> table;

    protected AbstractMatrixBuilder(final int rowSize, final int columnSize) {
        final List<Integer> rowIndexes = IntStream.rangeClosed(1, rowSize).boxed().collect(Collectors.toList());
        final List<Integer> columnIndexes = IntStream.rangeClosed(1, columnSize).boxed().collect(Collectors.toList());
        table = ArrayTable.create(rowIndexes, columnIndexes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("table", table).toString();
    }

    public Table<Integer, Integer, E> getTable() {
        return table;
    }
}