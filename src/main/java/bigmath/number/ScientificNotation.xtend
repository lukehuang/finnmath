package bigmath.number

import com.google.common.annotations.Beta
import java.math.BigDecimal
import org.eclipse.xtend.lib.annotations.Data

@Beta
@Data
final class ScientificNotation {
  BigDecimal coefficient
  int exponent
}
