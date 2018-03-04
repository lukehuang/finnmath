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

package com.github.ltennstedt.finnmath.core.number;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents the polar form of a complex number number which uses
 * {@link BigDecimal} as type for its coordinates
 * <p>
 * complexNumber = r * (cos(phi) + i * sin(phi))
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class PolarForm {
    /**
     * Default precision
     *
     * @since 1
     */
    public static final int DEFAULT_PRECISION = 100;

    /**
     * Radial coordinate
     *
     * @since 1
     */
    private final BigDecimal radial;

    /**
     * Angular coordinate
     *
     * @since 1
     */
    private final BigDecimal angular;

    /**
     * Constructs a {@link PolarForm} from the given coordinates
     *
     * @param radial
     *            the radial coordinate
     * @param angular
     *            the angular coordinate
     * @throws NullPointerException
     *             if {@code radial == null}
     * @throws NullPointerException
     *             if {@code angular == null}
     * @since 1
     */
    public PolarForm(final BigDecimal radial, final BigDecimal angular) {
        this.radial = requireNonNull(radial, "radial");
        this.angular = requireNonNull(angular, "angular");
    }

    /**
     * Returns the correspondig complex number of this polar form
     *
     * @return The complex number
     * @since 1
     */
    public RealComplexNumber complexNumber() {
        return complexNumer(BigFloat.context(DEFAULT_PRECISION));
    }

    /**
     * Returns the correspondig complex number of this polar form
     *
     * @param precision
     *            The precision
     * @return The complex number
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @since 1
     */
    public RealComplexNumber complexNumber(final int precision) {
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        return complexNumer(BigFloat.context(new MathContext(precision)));
    }

    /**
     * Returns the correspondig complex number of this polar form
     *
     * @param roundingMode
     *            The rounding mode
     * @return The complex number
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @since 1
     */
    public RealComplexNumber complexNumber(final RoundingMode roundingMode) {
        requireNonNull(roundingMode, "roundingMode");
        return complexNumer(BigFloat.context(new MathContext(DEFAULT_PRECISION, roundingMode)));
    }

    /**
     * Returns the correspondig complex number of this polar form
     *
     * @param precision
     *            The precision
     * @param roundingMode
     *            The rounding mode
     * @return The complex number
     * @throws IllegalArgumentException
     *             if {@code precision < 0}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @since 1
     */
    public RealComplexNumber complexNumber(final int precision, final RoundingMode roundingMode) {
        checkArgument(precision > -1, "expected precision > -1 but actual %s", precision);
        requireNonNull(roundingMode, "roundingMode");
        return complexNumer(BigFloat.context(new MathContext(precision, roundingMode)));
    }

    /**
     * Returns the correspondig complex number of this polar form considering the
     * {@link MathContext}
     *
     * @param mathContext
     *            The math context
     * @return The complex number
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @since 1
     */
    public RealComplexNumber complexNumber(final MathContext mathContext) {
        requireNonNull(mathContext, "mathContext");
        return complexNumer(BigFloat.context(mathContext));
    }

    private RealComplexNumber complexNumer(final Context context) {
        final BigDecimal real = radial.multiply(BigFloat.cos(context.valueOf(angular)).toBigDecimal());
        final BigDecimal imaginary = radial.multiply(BigFloat.sin(context.valueOf(angular)).toBigDecimal());
        return RealComplexNumber.of(real, imaginary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(radial, angular);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PolarForm)) {
            return false;
        }
        final PolarForm other = (PolarForm) object;
        return radial.equals(other.getRadial()) && angular.equals(other.getAngular());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("radial", radial).add("angular", angular).toString();
    }

    public BigDecimal getRadial() {
        return radial;
    }

    public BigDecimal getAngular() {
        return angular;
    }
}
