package mathmyday.lib.number

import com.google.common.annotations.Beta
import org.eclipse.xtend.lib.annotations.Data

import static java.util.Objects.requireNonNull

@Beta
@Data
final class NumberAndSqrt<T> {
    T number
    T sqrt

    new(T number, T sqrt) {
        this.number = requireNonNull(number)
        this.sqrt = requireNonNull(sqrt)
    }
}
