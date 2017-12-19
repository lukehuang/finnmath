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
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @param <B>
 *         The type of the real and imaginary part of the complex number
 * @param <S>
 *         The type of the complex number
 * @param <M>
 *         The type of the related matrix
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
abstract class AbstractComplexNumber<B, S, M> implements MathNumber<S, RealComplexNumber, BigDecimal> {
    /**
     * Default rounding mode
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * {@code real} part of this {@link AbstractComplexNumber}
     */
    protected final B real;

    /**
     * {@code imaginary} part of this {@link AbstractComplexNumber}
     */
    protected final B imaginary;

    protected AbstractComplexNumber(final B real, final B imaginary) {
        this.real = requireNonNull(real, "real");
        this.imaginary = requireNonNull(imaginary, "imaginary");
    }

    protected abstract RealComplexNumber divide(S divisor, RoundingMode roundingMode);

    protected abstract B absPow2();

    protected abstract S conjugate();

    protected abstract BigDecimal argument();

    protected abstract BigDecimal argument(int precision);

    protected abstract BigDecimal argument(final RoundingMode roundingMode);

    protected abstract BigDecimal argument(int precision, RoundingMode roundingMode);

    protected abstract BigDecimal argument(MathContext mathContext);

    protected abstract PolarForm polarForm();

    protected abstract PolarForm polarForm(int precision);

    protected abstract PolarForm polarForm(RoundingMode roundingMode);

    protected abstract PolarForm polarForm(int precision, RoundingMode roundingMode);

    protected abstract PolarForm polarForm(MathContext mathContext);

    protected abstract M matrix();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("real", real).add("imaginary", imaginary).toString();
    }

    public B getReal() {
        return real;
    }

    public B getImaginary() {
        return imaginary;
    }
}
