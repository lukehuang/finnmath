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

import com.google.common.annotations.Beta;

/**
 * @param <S>
 *            The type of the number
 * @param <T>
 *            The type for quotients of the number
 * @param <A>
 *            The type of the absolute value of the number
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public interface MathNumber<S, T, A> {
    S add(S summand);

    S subtract(S subtrahend);

    S multiply(S factor);

    T divide(S divisor);

    S pow(int exponent);

    S negate();

    T invert();

    boolean invertible();

    A abs();
}
