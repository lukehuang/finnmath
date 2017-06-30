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

package mathmyday.lib.number

import com.google.common.annotations.Beta
import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument
import static com.google.common.base.Preconditions.checkState
import static java.util.Objects.requireNonNull

@Beta
@Data
final class Fraction implements MathNumber<Fraction, Fraction> {
    public static val ZERO = new Fraction(0BI, 1BI)
    public static val ONE = new Fraction(1BI, 1BI)
    BigInteger numerator
    BigInteger denominator

    new(BigInteger numerator, BigInteger denominator) {
        checkArgument(denominator != 0BI, 'expected denominator != 0 but actual %s', denominator)
        this.numerator = requireNonNull(numerator, 'numerator')
        this.denominator = requireNonNull(denominator, 'denominator')
    }

    override add(Fraction summand) {
        requireNonNull(summand, 'summand')
        val newNumerator = summand.denominator * numerator + denominator * summand.numerator
        val newDenominator = denominator * summand.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override subtract(Fraction subtrahend) {
        requireNonNull(subtrahend, 'subtrahend')
        val newNumerator = subtrahend.denominator * numerator - denominator * subtrahend.numerator
        val newDenominator = denominator * subtrahend.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override multiply(Fraction factor) {
        requireNonNull(factor, 'factor')
        val newNumerator = numerator * factor.numerator
        val newDenominator = denominator * factor.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override divide(Fraction divisor) {
        requireNonNull(divisor, 'divisor')
        checkArgument(divisor.numerator != 0BI, 'expected divisor.numerator != 0 but actual %s', divisor.numerator)
        multiply(divisor.invert)
    }

    override negate() {
        new Fraction(-numerator, denominator)
    }

    override pow(int exponent) {
        checkArgument(exponent > -1, 'expected exponent > -1 but actual %s', exponent)
        if (exponent > 1)
            return multiply(pow(exponent - 1))
        else if (exponent == 1)
            return this
        ONE
    }

    override asString() {
        if (denominator < 0BI)
            return '''«numerator» / («denominator»)'''
        '''«numerator» / «denominator»'''
    }

    def invertible() {
        numerator != 0BI
    }

    def invert() {
        checkState(invertible, 'expected numerator != 0 but actual %s', numerator)
        new Fraction(denominator, numerator)
    }

    def lessThanOrEqualTo(Fraction other) {
        requireNonNull(other, 'other')
        val normalized = normalize
        val normalizedOther = other.normalize
        normalizedOther.denominator * normalized.numerator <= normalized.denominator * normalizedOther.numerator
    }

    def greaterThanOrEqualTo(Fraction other) {
        requireNonNull(other, 'other')
        !lessThanOrEqualTo(other) || equivalent(other)
    }

    def lessThan(Fraction other) {
        requireNonNull(other, 'other')
        !greaterThanOrEqualTo(other)
    }

    def greaterThan(Fraction other) {
        requireNonNull(other, 'other')
        !lessThanOrEqualTo(other)
    }

    def min(Fraction other) {
        requireNonNull(other, 'other')
        if (greaterThan(other))
            return other
        this
    }

    def max(Fraction other) {
        requireNonNull(other, 'other')
        if (lessThan(other))
            return other
        this
    }

    def normalize() {
        if (signum < 0)
            return new Fraction(-numerator.abs, denominator.abs)
        if (signum == 0)
            return ZERO
        if (numerator < 0BI)
            return new Fraction(numerator.abs, denominator.abs)
        this
    }

    def abs() {
        new Fraction(numerator.abs, denominator.abs)
    }

    def signum() {
        numerator.signum * denominator.signum
    }

    def reduce() {
        val gcd = numerator.gcd(denominator)
        new Fraction(numerator / gcd, denominator / gcd)
    }

    def equivalent(Fraction other) {
        requireNonNull(other, 'other')
        normalize.reduce == other.normalize.reduce
    }
}
