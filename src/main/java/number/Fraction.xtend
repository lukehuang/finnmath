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

package number

import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Data

import static com.google.common.base.Preconditions.checkArgument

@Data
class Fraction implements MathNumber<Fraction> {
    public static val ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE)
    public static val ONE = new Fraction(BigInteger.ONE, BigInteger.ONE)
    BigInteger numerator
    BigInteger denominator

    new(BigInteger numerator, BigInteger denominator) {
        checkArgument(denominator != BigInteger.ZERO, "denominator = 0")
        this.numerator = numerator
        this.denominator = denominator
    }

    override add(Fraction summand) {
        val newNumerator = summand.denominator * numerator + denominator * summand.numerator
        val newDenominator = denominator * summand.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override subtract(Fraction subtrahend) {
        val newNumerator = subtrahend.denominator * numerator - denominator * subtrahend.numerator
        val newDenominator = denominator * subtrahend.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override multiply(Fraction factor) {
        val newNumerator = numerator * factor.numerator
        val newDenominator = denominator * factor.denominator
        new Fraction(newNumerator, newDenominator)
    }

    override negate() {
        new Fraction(-numerator, denominator)
    }

    override pow(int exponent) {
        checkArgument(exponent >= 0, "exponent < 0")
        if (exponent > 1)
            return this.multiply(pow(exponent - 1))
        else if (exponent == 1)
            return this
        ZERO
    }

    override asString() {
        if (denominator < BigInteger.ZERO)
            return '''«numerator» / («denominator»)'''
        '''«numerator» / «denominator»'''
    }

    def divide(Fraction divisor) {
        this.multiply(divisor.invert)
    }

    def invert() {
        checkArgument(numerator != BigInteger.ZERO, "numerator = 0")
        new Fraction(denominator, numerator)
    }

    def min(Fraction other) {
        val newNumerator = other.denominator * numerator
        val newOtherNumerator = denominator * other.numerator
        if (newOtherNumerator < newNumerator)
            return other
        this
    }

    def max(Fraction other) {
        val newNumerator = other.denominator * numerator
        val newOtherNumerator = denominator * other.numerator
        if (newOtherNumerator > newNumerator)
            return other
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
        other.reduce == this.reduce
    }
}
