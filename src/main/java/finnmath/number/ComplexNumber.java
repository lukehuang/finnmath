/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2017, Lars Tennstedt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package finnmath.number;

import static java.util.Objects.requireNonNull;

import com.google.common.base.MoreObjects;
import java.math.BigDecimal;

/**
 * @since 1
 * @author Lars Tennstedt
 */
abstract class ComplexNumber<B, S, R, M> implements MathNumber<S, R, BigDecimal> {
    /**
     * {@code real} part of this {@link ComplexNumber}
     */
    protected final B real;

    /**
     * {@code imaginary} part of this {@link ComplexNumber}
     */
    protected final B imaginary;

    protected ComplexNumber(final B real, final B imaginary) {
        this.real = requireNonNull(real, "real");
        this.imaginary = requireNonNull(imaginary, "imaginary");
    }

    abstract B absPow2();

    abstract S conjugate();

    abstract M matrix();

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
