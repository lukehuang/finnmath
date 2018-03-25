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
    /**
     * Returns the sum of this {@link MathNumber} and the given one
     *
     * @param summand
     *            the summand
     * @return The sum
     * @since 1
     */
    S add(S summand);

    /**
     * Returns the difference of this {@link MathNumber} and the given one
     *
     * @param subtrahend
     *            the subtrahend
     * @return The difference
     * @since 1
     */
    S subtract(S subtrahend);

    /**
     * Returns the product of this {@link MathNumber} and the given one
     *
     * @param factor
     *            the factor
     * @return The product
     * @since 1
     */
    S multiply(S factor);

    /**
     * Return the quotient of this {@link MathNumber} and the given one
     *
     * @param divisor
     *            the divisor
     * @return The quotient
     * @throws NullPointerException
     *             if {@code divisor == null}
     * @throws IllegalArgumentException
     *             if {@code !divisor.invertible}
     * @since 1
     */
    T divide(S divisor);

    /**
     * Returns the power of this {@link MathNumber} raised by the given exponent
     *
     * @param exponent
     *            the exponent
     * @return The power
     * @throws IllegalArgumentException
     *             if {@code exponent < 0}
     * @since 1
     */
    S pow(int exponent);

    /**
     * Returns the negated {@link MathNumber} of this one
     *
     * @return The negated
     * @since 1
     */
    S negate();

    /**
     * Returns the inverted {@link MathNumber} of this one
     *
     * @return The inverted
     * @throws IllegalStateException
     *             if {@code numerator == 0}
     * @since 1
     */
    T invert();

    /**
     * Returns if this {@link MathNumber} is invertible
     *
     * @return {@code true} if {@code numerator != 0}, {@code false} otherwise
     * @since 1
     */
    boolean invertible();

    /**
     * Returns the absolute {@link MathNumber} of this one
     *
     * @return The absolute
     * @since 1
     */
    A abs();
}
