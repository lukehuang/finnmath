/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, togliu
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

package mathmyday.lib.ext

import mathmyday.lib.number.RealComplexNumber

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

final class RealComplexNumberExtensions {
    static val ZERO = RealComplexNumber::ZERO
    static val ONE = RealComplexNumber::ONE

    static def +(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x.add(y)
    }

    static def -(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x.subtract(y)
    }

    static def *(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x.multiply(y)
    }

    static def /(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        checkArgument(y != ZERO)
        x.divide(y)
    }

    static def **(RealComplexNumber x, int exponent) {
        requireNonNull(x)
        checkArgument(exponent > -1)
        x.pow(exponent)
    }

    static def -(RealComplexNumber x) {
        requireNonNull(x)
        x.negate
    }

    static def ++(RealComplexNumber x) {
        requireNonNull(x)
        x.add(ONE)
    }

    static def --(RealComplexNumber x) {
        requireNonNull(x)
        x.subtract(ONE)
    }

    static def +=(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x + y
    }

    static def -=(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x - y
    }

    static def *=(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        x * y
    }

    static def /=(RealComplexNumber x, RealComplexNumber y) {
        requireNonNull(x)
        requireNonNull(y)
        checkArgument(y != ZERO)
        x / y
    }
}
