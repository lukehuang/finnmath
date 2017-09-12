/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2017, Lars Tennstedt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABDLITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABDLITY, WHETHER IN CONTRACT, STRICT LIABDLITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBDLITY OF SUCH DAMAGE.
 */

package finnmath.linear;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.Builder;

/**
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
abstract class VectorBuilder<E, V, B> implements Builder<V> {
    /**
     * The map holding the entries of this {@link VectorBuilder}
     */
    protected final Map<Integer, E> map = new HashMap<>();

    /**
     * The size of the map
     */
    protected final int size;

    /**
     * Constructs a {@link VectorBuilder} from the given size
     *
     * @param size
     *            the size
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    protected VectorBuilder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        this.size = size;
    }

    /**
     * Returns the entry dependent on the given index
     *
     * @param index
     *            the index of the entry
     * @return The entry
     * @throws NullPointerException
     *             if {@code index == null}
     * @throws IllegalArgumentException
     *             if {@code !map.containsKey(index)}
     * @since 1
     * @author Lars Tennstedt
     * @see Map#containsKey
     */
    public E entry(final Integer index) {
        requireNonNull(index, "index");
        checkArgument((0 < index) && (index <= size), "expected index in [1, %s] but actual %s", size, index);
        return map.get(index);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).add("size", size).toString();
    }

    public Map<Integer, E> getMap() {
        return map;
    }

    public int getSize() {
        return size;
    }
}
