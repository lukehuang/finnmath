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
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @since 1
 * @author Lars Tennstedt
 * @see ImmutableMap
 */
@Beta
abstract class AbstractVector<E, V, N> {
    /**
     * The map holding the elements of this {@link AbstractVector}
     *
     * @since 1
     * @author Lars Tennstedt
     */
    protected final ImmutableMap<Integer, E> map;

    protected AbstractVector(final ImmutableMap<Integer, E> map) {
        this.map = map;
    }

    /**
     * Returns the element dependent on the given index
     *
     * @param index
     *            the index of the element
     * @return The element
     * @throws NullPointerException
     *             if {@code index == null}
     * @throws IllegalArgumentException
     *             if {@code !map.containsKey(index)}
     * @since 1
     * @author Lars Tennstedt
     * @see Map#containsKey
     */
    public E element(final Integer index) {
        requireNonNull(index, "index");
        checkArgument(map.containsKey(index), "expected index in [1, %s] but actual %s", map.size(), index);
        return map.get(index);
    }

    /**
     * Returns all elements of the underlying {@link Map} of this
     * {@link AbstractVector}
     *
     * @return The elements
     * @since 1
     * @author Lars Tennstedt
     * @see Map#entrySet
     */
    public ImmutableSet<Entry<Integer, E>> entries() {
        return map.entrySet();
    }

    /**
     * Returns all elements of this {@link AbstractVector}
     *
     * @return The elements
     * @since 1
     * @author Lars Tennstedt
     * @see Map#values
     */
    public ImmutableCollection<E> elements() {
        return map.values();
    }

    protected abstract V add(V summand);

    protected abstract V subtract(V subtrahend);

    protected abstract E dotProduct(V vector);

    protected abstract V scalarMultiply(E scalar);

    protected abstract V negate();

    protected abstract E euclideanNormPow2();

    protected abstract N euclideanNorm();

    protected abstract N euclideanNorm(BigDecimal precision);

    protected abstract N euclideanNorm(int scale, int roundingMode);

    protected abstract N euclideanNorm(BigDecimal precision, int scale, int roundingMode);

    protected abstract E euclideanDistancePow2(V vector);

    protected abstract N euclideanDistance(V vector);

    protected abstract N euclideanDistance(V vector, BigDecimal precision);

    protected abstract N euclideanDistance(V vector, int scale, int roundingMode);

    protected abstract N euclideanDistance(V vector, BigDecimal precision, int scale, int roundingMode);

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
