package mathmyday.lib.linear

import com.google.common.annotations.Beta

@Beta
interface MatrixBuilder<T> {
    def T build()
}
