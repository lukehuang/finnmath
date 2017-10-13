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

package com.github.ltennstedt.finnmath.number;

import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents the scientific notation for a decimal number which uses {@link BigDecimal} as type for
 * its coefficient and {@code int} as type for its exponent
 * <p>
 * decimal = coefficient * 10^exponent
 *
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
public final class ScientificNotation {
  /**
   * Coefficient of this scientific notation
   */
  private final BigDecimal coefficient;

  /**
   * Exponent of this scientific notation
   */
  private final int exponent;

  /**
   * Constructs a {@link ScientificNotation} from the given coefficient and exponent
   *
   * @param coefficient
   *          the coefficient
   * @param exponent
   *          the exponent
   * @throws NullPointerException
   *           if {@code coefficient == null}
   * @since 1
   * @author Lars Tennstedt
   */
  public ScientificNotation(final BigDecimal coefficient, final int exponent) {
    this.coefficient = requireNonNull(coefficient, "coefficient");
    this.exponent = exponent;
  }

  /**
   * Returns a string representation of this {@link ScientificNotation}
   *
   * @return The string representation
   * @since 1
   * @author Lars Tennstedt
   */
  public String asString() {
    if (coefficient.compareTo(BigDecimal.ZERO) == 0) {
      return "0";
    }
    if (exponent < 0) {
      return new StringBuilder(coefficient.toPlainString()).append(" * 10**(").append(exponent)
          .append(")").toString();
    }
    if (exponent == 0) {
      return coefficient.toPlainString();
    }
    if (exponent == 1) {
      return new StringBuilder(coefficient.toPlainString()).append(" * 10").toString();
    }
    return new StringBuilder(coefficient.toPlainString()).append(" * 10**").append(exponent)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(coefficient, exponent);
  }

  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof ScientificNotation)) {
      return false;
    }
    final ScientificNotation other = (ScientificNotation) object;
    return coefficient.equals(other.getCoefficient()) && (exponent == other.getExponent());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("coefficient", coefficient)
        .add("exponent", exponent).toString();
  }

  public BigDecimal getCoefficient() {
    return coefficient;
  }

  public int getExponent() {
    return exponent;
  }
}
