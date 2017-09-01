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

package finnmath.linear

import com.google.common.annotations.Beta
import java.util.HashMap
import java.util.Map
import org.apache.commons.lang3.builder.Builder
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@ToString
abstract class VectorBuilder<E, V, B> implements Builder<V> {
    /**
     * The map holding the entries of this {@link VectorBuilder}
     * 
     * @since 1
     * @author Lars Tennstedt
     */
    @Accessors
    protected val Map<Integer, E> map = new HashMap

    /**
     * The size of the resulting {@link Vector}
     */
    @Accessors
    val int size

    /**
     * Constructs a {@link VectorBuilder} from the given size
     * 
     * @param size the size
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    new(int size) {
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        this.size = size
    }

    /**
     * Returns the entry dependent on the given index
     * 
     * @param index the index of the entry
     * @return The entry
     * @throws NullPointerException if {@code index == null}
     * @throws IllegalArgumentException if {@code !map.containsKey(index)}
     * @since 1
     * @author Lars Tennstedt
     * @see Map#containsKey
     */
    def entry(Integer index) {
        requireNonNull(index, 'index')
        checkArgument(0 < index && index <= size, 'expected index in [1, %s] but actual %s', size, index)
        map.get(index)
    }

    /**
     * Puts the given entry on the first free index and returns {@code this}
     * 
     * @param entry the entry
     * @return {@code this}
     * @throws NullPointerException if {@code entry == null}
     * @throws IllegalStateException if {@code map.size == size}
     * @since 1
     * @author Lars Tennstedt
     */
    def put(E entry) {
        requireNonNull(entry, 'entry')
        val index = map.size + 1
        checkState(map.size < size, 'expected index in [1, %s] but actual %s', size, index)
        map.put(index, entry)
        this
    }

    /**
     * Puts the given entry on the given index and returns {@code this}
     * 
     * @param index the index
     * @param entry the entry
     * @return {@code this}
     * @throws NullPointerException if {@code index == null}
     * @throws NullPointerException if {@code entry == null}
     * @throws IllegalArgumentException if {@code index <= 0 || size < index}
     * @since 1
     * @author Lars Tennstedt
     */
    def put(Integer index, E entry) {
        requireNonNull(index, 'index')
        requireNonNull(entry, 'entry')
        checkArgument(0 < index && index <= size, 'expected index in [1, %s] but actual %s', size, index)
        map.put(index, entry)
        this
    }

    /**
     * Puts the given entry on all indices and returns {@code this}
     * 
     * @param entry the entry
     * @return {@code this}
     * @throws NullPointerException if {@code entry == null}
     * @since 1
     * @author Lars Tennstedt
     */
    def putAll(E entry) {
        requireNonNull(entry, 'entry')
        (1 .. size).forEach [
            map.put(it, entry)
        ]
        this
    }
}
