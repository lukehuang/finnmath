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
import com.google.common.collect.ImmutableMap;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @since 1
 * @author Lars Tennstedt
 * @see ImmutableMap
 */
@Beta
abstract class Vector<E, V, N> {
    /**
     * The map holding the entries of this {@link Vector}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    protected final ImmutableMap<Integer, E> map;

    protected Vector(final ImmutableMap<Integer, E> map) {
        this.map = map;
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
        checkArgument(map.containsKey(index), "expected index in [1, %s] but actual %s", map.size(), index);
        return map.get(index);
    }

    abstract V add(V summand);

    abstract V subtract(V subtrahend);

    abstract E dotProduct(V vector);

    abstract V scalarMultiply(E scalar);

    abstract V negate();

    abstract E euclideanNormPow2();

    abstract N euclideanNorm();

    abstract N euclideanNorm(BigDecimal precision);

    abstract N euclideanNorm(int scale, int roundingMode);

    abstract N euclideanNorm(BigDecimal precision, int scale, int roundingMode);

    abstract E euclideanDistancePow2(V vector);

    abstract N euclideanDistance(V vector);

    abstract N euclideanDistance(V vector, BigDecimal precision);

    abstract N euclideanDistance(V vector, int scale, int roundingMode);

    abstract N euclideanDistance(V vector, BigDecimal precision, int scale, int roundingMode);

    /**
     * Returns the size of the underlying {@link Map}
     *
     * @return size the size
     * @since 1
     * @author Lars Tennstedt
     */
    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("map", map).toString();
    }

    public ImmutableMap<Integer, E> getMap() {
        return map;
    }
}
