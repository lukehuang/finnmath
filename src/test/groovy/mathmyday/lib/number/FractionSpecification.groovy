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

import mathmyday.lib.util.MathRandom
import spock.lang.Shared
import spock.lang.Specification

class FractionSpecification extends Specification {
	@Shared
	List<Fraction> fractions

	@Shared
	List<Fraction> others

	@Shared
	List<Fraction> invertibles

	def setupSpec() {
		def size = 100
		def mathRandom = new MathRandom()
		fractions = mathRandom.createFractions(Integer.MAX_VALUE, size)
		others = mathRandom.createFractions(Integer.MAX_VALUE, size)
		invertibles = mathRandom.createInvertibleFractions(Integer.MAX_VALUE, size)
	}

	def newDenominatorIsZero() {
		when:
		new Fraction(0, 0)

		then:
		thrown(IllegalArgumentException)
	}

	def add() {
		expect:
		fraction.add(other) == new Fraction(numerator, denominator)

		where:
		fraction << fractions
		other << others
		numerator = other.denominator * fraction.numerator + fraction.denominator * other.numerator
		denominator = fraction.denominator * other.denominator
	}

	def subtract() {
		expect:
		fraction.subtract(other) == new Fraction(numerator, denominator)

		where:
		fraction << fractions
		other << others
		numerator = other.denominator * fraction.numerator - fraction.denominator * other.numerator
		denominator = fraction.denominator * other.denominator
	}

	def multiply() {
		expect:
		fraction.multiply(other) == new Fraction(numerator, denominator)

		where:
		fraction << fractions
		other << others
		numerator = fraction.numerator * other.numerator
		denominator = fraction.denominator * other.denominator
	}

	def pow() {
		expect:
		fraction.pow(3) == fraction.multiply(fraction.multiply(fraction))
		fraction.pow(2) == fraction.multiply(fraction)
		fraction.pow(1) == fraction
		fraction.pow(0) == Fraction.ONE

		where:
		fraction << fractions
	}

	def negate() {
		expect:
		fraction.negate() == new Fraction(-fraction.numerator, fraction.denominator)

		where:
		fraction << fractions
	}

	def invertNumeratorIsZero() {
		when:
		Fraction.ZERO.invert()

		then:
		thrown(ArithmeticException)
	}

	def invert() {
		expect:
		fraction.invert() == new Fraction(fraction.denominator, fraction.numerator)

		where:
		fraction << fractions
	}

	def abs() {
		expect:
		fraction.abs() == new Fraction(fraction.numerator.abs(), fraction.denominator.abs())

		where:
		fraction << fractions
	}

	def signum() {
		expect:
		fraction.signum() == fraction.numerator.signum * fraction.denominator.signum

		where:
		fraction << fractions
	}

	def reduce() {
		expect:
		fraction.reduce() == new Fraction(numerator, denominator)

		where:
		fraction << fractions
		gcd = fraction.numerator.gcd(fraction.denominator)
		numerator = (fraction.numerator / gcd).toBigInteger()
		denominator = (fraction.denominator / gcd).toBigInteger()
	}

	def equivalent() {
		expect:
		fraction.equivalent(other) == (fraction.reduce() == other.reduce())

		where:
		fraction << fractions
		other << others
	}
}
