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

package finnmath.number

import com.google.common.annotations.Beta
import java.math.BigInteger
import java.util.Optional
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

/**
 * An immutable implementation of a fraction which uses {@link BigInteger} as type for its numerator and denominator
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@EqualsHashCode
@ToString
final class Fraction implements MathNumber<Fraction, Fraction>, Comparable<Fraction> {
    /**
     * {@code 0} as {@link Fraction}
     */
    public static val ZERO = new Fraction(0BI, 1BI)

    /**
     * {@code 1} as {@link Fraction}
     */
    public static val ONE = new Fraction(1BI, 1BI)

    /**
     * {@code numerator} of this {@link Fraction} 
     */
    @Accessors
    val BigInteger numerator

    /**
     * {@code denominator} of this {@link Fraction} 
     */
    @Accessors
    val BigInteger denominator

    /**
     * Constructs a {@link Fraction} by the given numerator and denominator
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param numerator the numerator
     * @param denominator the denominator
     * @throws NullPointerException if {@code numerator == null}
     * @throws NullPointerException if {@code denominator == null}
     * @throws IllegalArgumentException if {@code denominator == 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    new(BigInteger numerator, BigInteger denominator) {
        this.numerator = requireNonNull(numerator, 'numerator')
        this.denominator = requireNonNull(denominator, 'denominator')
        checkArgument(denominator != 0BI, 'expected denominator != 0 but actual %s', denominator)
    }

    /**
     * Returns the sum of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param summand the summand
     * @return The sum
     * @throws NullPointerException if {@code summand == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    override add(Fraction summand) {
        requireNonNull(summand, 'summand')
        val newNumerator = summand.denominator * numerator + denominator * summand.numerator
        val newDenominator = denominator * summand.denominator
        new Fraction(newNumerator, newDenominator)
    }

    /**
     * Returns the difference of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param subtrahend the subtrahend
     * @return The difference
     * @throws NullPointerException  if {@code subtrahend == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    override subtract(Fraction subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        val newNumerator = subtrahend.denominator * numerator - denominator * subtrahend.numerator
        val newDenominator = denominator * subtrahend.denominator
        new Fraction(newNumerator, newDenominator)
    }

    /**
     * Returns the product of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param factor the factor
     * @return The product 
     * @throws NullPointerException if {@code factor == null}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    override multiply(Fraction factor) {
        requireNonNull(factor, 'factor')
        val newNumerator = numerator * factor.numerator
        val newDenominator = denominator * factor.denominator
        new Fraction(newNumerator, newDenominator)
    }

    /**
     * Return the quotient of this {@link Fraction} and the given one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param divisor the divisor
     * @return The quotient
     * @throws NullPointerException if {@code divisor.numerator == null}
     * @throws IllegalArgumentException if {@code divisor.numerator == 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #invertible
     * @see #normalize
     * @see #reduce
     * @see Optional#get
     */
    override divide(Fraction divisor) {
        requireNonNull(divisor, 'divisor')
        checkArgument(divisor.invertible, 'expected divisor to be invertible but actual %s', divisor)
        multiply(divisor.invert)
    }

    /**
     * Returns the power of this {@link Fraction} raised by the given exponent
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param exponent the exponent
     * @return The power
     * @throws IllegalArgumentException if {@code exponent < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    override pow(int exponent) {
        checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
        if (exponent > 1)
            return multiply(pow(exponent - 1))
        else if (exponent == 1)
            return this
        ONE
    }

    /**
     * Returns the negated {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @return The negated
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    override negate() {
        new Fraction(-numerator, denominator)
    }

    /**
     * Returns the inverted {@link Fraction} of this one if this {@link Fraction} is invertible
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @return The inverted
     * @throws IllegalStateException if {@code !invertible}
     * @since 1
     * @author Lars Tennstedt
     * @see #invertible
     * @see #normalize
     * @see #reduce
     */
    override invert() {
        checkState(invertible, 'expected to be invertible but actual %s', this)
        new Fraction(denominator, numerator)
    }

    /**
     * Returns if this {@link Fraction} is invertible
     * 
     * @return {@code true} if {@code numerator != 0}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     */
    override invertible() {
        numerator != 0BI
    }

    /**
     * Returns a string representation of this {@link Fraction}
     * 
     * @return The string representation
     * @since 1
     * @author Lars Tennstedt
     */
    override asString() {
        if (denominator < 0BI)
            return '''«numerator» / («denominator»)'''
        '''«numerator» / «denominator»'''
    }

    /**
     * Compares this {@link Fraction} to the given one and returns an int which indicates which one is less
     * 
     * @param other another {@link Fraction}
     * @return {@code -1} if {@code this < other}, {@code 1} if {@code this > other}, {@code 0} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThan
     * @see #greaterThan
     */
    override compareTo(Fraction other) {
        requireNonNull(other, 'other')
        if (lessThan(other))
            return -1
        if (greaterThan(other))
            return 1
        0
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean} which indicates which one is less
     * 
     * @param other another {@link Fraction}
     * @return {@code true} if {@code this <= other}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     */
    def lessThanOrEqualTo(Fraction other) {
        requireNonNull(other, 'other')
        val normalized = normalize
        val normalizedOther = other.normalize
        normalizedOther.denominator * normalized.numerator <= normalized.denominator * normalizedOther.numerator
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean} which indicates which one is 
     * greater
     * 
     * @param other another {@link Fraction}
     * @return {@code true} if {@code this >= other}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThanOrEqualTo
     * @see #equivalent
     */
    def greaterThanOrEqualTo(Fraction other) {
        requireNonNull(other, 'other')
        !lessThanOrEqualTo(other) || equivalent(other)
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean} which indicates which one is less
     * 
     * @param other another {@link Fraction}
     * @return {@code true} if {@code this < other}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #greaterThanOrEqualTo
     */
    def lessThan(Fraction other) {
        requireNonNull(other, 'other')
        !greaterThanOrEqualTo(other)
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean} which indicates which one is 
     * greater
     * 
     * @param other another {@link Fraction}
     * @return {@code true} if {@code this < other}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThanOrEqualTo
     */
    def greaterThan(Fraction other) {
        requireNonNull(other, 'other')
        !lessThanOrEqualTo(other)
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the minimum
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param other another {@link Fraction}
     * @return The minimum of this {@link Fraction} and the other one
     * @since 1
     * @author Lars Tennstedt
     * @see #greaterThan
     * @see #normalize
     * @see #reduce
     */
    def min(Fraction other) {
        requireNonNull(other, 'other')
        if (greaterThan(other))
            return other
        this
    }

    /**
     * Compares this {@link Fraction} to the given one and returns the maximum
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @param other another {@link Fraction}
     * @return The maximum of this {@link Fraction} and the other one
     * @since 1
     * @author Lars Tennstedt
     * @see #lessThan
     * @see #normalize
     * @see #reduce
     */
    def max(Fraction other) {
        requireNonNull(other, 'other')
        if (lessThan(other))
            return other
        this
    }

    /**
     * Returns the normalized {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced.
     * 
     * @return {@code new Fraction(-numerator.abs, denominator.abs)} if the {@code signum < 0}, {@code ZERO} if 
     * {@code signum == 0}, {@code this} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #signum
     * @see #abs
     * @see BigInteger#abs
     * @see #reduce
     */
    def normalize() {
        if (signum < 0)
            return new Fraction(-numerator.abs, denominator.abs)
        if (signum == 0)
            return ZERO
        if (numerator < 0BI)
            return abs
        this
    }

    /**
     * Returns the absolute {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not reduced and not normalized.
     * 
     * @return The absolute
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     * @see BigInteger#abs
     */
    def abs() {
        new Fraction(numerator.abs, denominator.abs)
    }

    /**
     * Returns the signum of this {@link Fraction}
     * 
     * @return The signum of this {@link Fraction}
     * @since 1
     * @author Lars Tennstedt
     * @see BigInteger#signum
     */
    def signum() {
        numerator.signum * denominator.signum
    }

    /**
     * Returns the reduced {@link Fraction} of this one
     * <p>
     * The returned {@link Fraction} is not normalized.
     * 
     * @return The reduced
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see BigInteger#gcd
     */
    def reduce() {
        val gcd = numerator.gcd(denominator)
        new Fraction(numerator / gcd, denominator / gcd)
    }

    /**
     * Compares this {@link Fraction} to the given one and returns a {@code boolean} which indicates if this 
     * {@link Fraction} is equivalent to the given one
     * 
     * @param other another {@link Fraction}
     * @return {@code true} if the {@code this} is equivalent to {@code other}, {@code false} otherwise
     * @since 1
     * @author Lars Tennstedt
     * @see #normalize
     * @see #reduce
     */
    def equivalent(Fraction other) {
        requireNonNull(other, 'other')
        normalize.reduce == other.normalize.reduce
    }
}
