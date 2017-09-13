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
