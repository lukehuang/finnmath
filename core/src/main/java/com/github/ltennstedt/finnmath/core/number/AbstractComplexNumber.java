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

import com.github.ltennstedt.finnmath.core.linear.AbstractMatrix;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Base class for complex numbers
 *
 * @param <B>
 *            The type of the real and imaginary part of the complex number
 * @param <S>
 *            The type of the complex number
 * @param <M>
 *            The type of the related matrix
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public abstract class AbstractComplexNumber<B, S extends AbstractComplexNumber<B, S, M>,
    M extends AbstractMatrix<B, ?, M, B, B>> implements MathNumber<S, RealComplexNumber, BigDecimal> {
    /**
     * Default rounding mode
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * {@code real} part of this {@link AbstractComplexNumber}
     *
     * @since 1
     */
    protected final B real;

    /**
     * {@code imaginary} part of this {@link AbstractComplexNumber}
     *
     * @since 1
     */
    protected final B imaginary;

    protected AbstractComplexNumber(final B real, final B imaginary) {
        this.real = requireNonNull(real, "real");
        this.imaginary = requireNonNull(imaginary, "imaginary");
    }

    /**
     * Returns the quotient of this {@link AbstractComplexNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code divisor == 0}
     * @see #invertible
     * @since 1
     */
    @Override
    public final RealComplexNumber divide(final S divisor) {
        requireNonNull(divisor, "divisor");
        checkArgument(divisor.invertible(), "expected divisor to be invertible but actual %s", divisor);
        return divide(divisor, DEFAULT_ROUNDING_MODE);
    }

    protected abstract RealComplexNumber divide(S divisor, RoundingMode roundingMode);

    protected abstract B absPow2();

    protected abstract S conjugate();

    protected abstract BigDecimal argument();

    protected abstract BigDecimal argument(int precision);

    protected abstract BigDecimal argument(RoundingMode roundingMode);

    protected abstract BigDecimal argument(int precision, RoundingMode roundingMode);

    protected abstract BigDecimal argument(MathContext mathContext);

    protected abstract PolarForm polarForm();

    protected abstract PolarForm polarForm(int precision);

    protected abstract PolarForm polarForm(RoundingMode roundingMode);

    protected abstract PolarForm polarForm(int precision, RoundingMode roundingMode);

    protected abstract PolarForm polarForm(MathContext mathContext);

    protected abstract M matrix();

    @Override
    public final int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AbstractComplexNumber)) {
            return false;
        }
        final AbstractComplexNumber<?, ?, ?> other = (AbstractComplexNumber<?, ?, ?>) object;
        return real.equals(other.getReal()) && imaginary.equals(other.getImaginary());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this).add("real", real).add("imaginary", imaginary).toString();
    }

    public final B getReal() {
        return real;
    }

    public final B getImaginary() {
        return imaginary;
    }
}
