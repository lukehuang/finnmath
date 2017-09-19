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
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.Builder;

/**
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
abstract class AbstractVectorBuilder<E, V, B> implements Builder<V> {
    /**
     * The map holding the elements of this {@link AbstractVectorBuilder}
     */
    protected final Map<Integer, E> map = new HashMap<>();

    /**
     * The size of the map
     */
    protected final int size;

    /**
     * Constructs a {@link AbstractVectorBuilder} from the given size
     *
     * @param size
     *            the size
     * @throws IllegalArgumentException
     *             if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    protected AbstractVectorBuilder(final int size) {
        checkArgument(size > 0, "expected size > 0 but actual %s", size);
        this.size = size;
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
