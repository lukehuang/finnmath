package mathmyday.lib.linear

import com.google.common.annotations.Beta

@Beta
interface Vector<V, E> {
    def V negate()

    def E abs()

    def int size()
}
