package mathmyday.lib.number

import com.google.common.annotations.Beta
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkNotNull

@Beta
@Data
final class NumberAndSqrt<T> {
  T number
  T sqrt

  new(T number, T sqrt) {
    this.number = checkNotNull(number, "The number is not allowed to be null but is %s.", number)
    this.sqrt = checkNotNull(sqrt, "The square root is not allowed to be null but is %s.", sqrt)
  }
}
