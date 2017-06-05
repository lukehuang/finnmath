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
import java.util.List
import mathrandom.MathRandom
import org.junit.BeforeClass
import org.junit.Test

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

class FractionTest {
    val static List<Fraction> fractions = newArrayList
    val static List<Fraction> others = newArrayList
    val static List<Fraction> invertibles = newArrayList

    @BeforeClass
    def static setUp() {
        val mathRandom = new MathRandom
        for (var i = 0; i < 100; i++) {
            fractions.add(mathRandom.createFraction(Integer.MAX_VALUE))
            others.add(mathRandom.createFraction(Integer.MAX_VALUE))
            invertibles.add(mathRandom.createFraction(Integer.MAX_VALUE))
        }
    }

    @Test(expected=IllegalArgumentException)
    def void newDenominatorIsZero() {
        new Fraction(BigInteger.ZERO, BigInteger.ZERO)
    }

    @Test
    def add() {
        fractions.forEach [
            val other = others.get(fractions.indexOf(it))
            val numerator = other.denominator * it.numerator + it.denominator * other.numerator
            val denominator = it.denominator * other.denominator
            assertThat(it.add(other), is(new Fraction(numerator, denominator)))
        ]
    }

    @Test
    def subtract() {
        fractions.forEach [
            val other = others.get(fractions.indexOf(it))
            val numerator = other.denominator * it.numerator - it.denominator * other.numerator
            val denominator = it.denominator * other.denominator
            assertThat(it.subtract(other), is(new Fraction(numerator, denominator)))
        ]
    }

    @Test
    def multiply() {
        fractions.forEach [
            val other = others.get(fractions.indexOf(it))
            val numerator = it.numerator * other.numerator
            val denominator = it.denominator * other.denominator
            assertThat(it.multiply(other), is(new Fraction(numerator, denominator)))
        ]
    }

    @Test
    def negate() {
        fractions.forEach [
            assertThat(it.negate, is(new Fraction(-numerator, denominator)))
        ]
    }

    @Test(expected=IllegalArgumentException)
    def void invertNumeratorIsZero() {
        Fraction.ZERO.invert
    }

    @Test
    def invert() {
        invertibles.forEach [
            assertThat(it.invert, is(new Fraction(denominator, numerator)))
        ]
    }

    @Test
    def abs() {
        fractions.forEach [
            assertThat(it.abs, is(new Fraction(numerator.abs, denominator.abs)))
        ]
    }

    @Test
    def signum() {
        fractions.forEach [
            assertThat(it.signum, is(numerator.signum * denominator.signum))
        ]
    }

    @Test
    def reduce() {
        fractions.forEach [
            val gcd = numerator.gcd(denominator)
            assertThat(it.reduce, is(new Fraction(numerator / gcd, denominator / gcd)))
        ]
    }

    @Test
    def equivalent() {
        fractions.forEach [
            val other = others.get(fractions.indexOf(it))
            assertThat(it.equivalent(other), is(reduce == other.reduce))
        ]
    }
}
