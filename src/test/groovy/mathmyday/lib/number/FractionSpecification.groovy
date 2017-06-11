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

	def addNull() {
		when:
		Fraction.ZERO.add(null)

		then:
		thrown(NullPointerException)
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

	def subtractNull() {
		when:
		Fraction.ZERO.subtract(null)

		then:
		thrown(NullPointerException)
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

	def multiplyNull() {
		when:
		Fraction.ZERO.multiply(null)

		then:
		thrown(NullPointerException)
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

	def negate() {
		expect:
		fraction.negate() == new Fraction(-fraction.numerator, fraction.denominator)

		where:
		fraction << fractions
	}

	def powNegativeExponent() {
		when:
		Fraction.ZERO.pow(-1)

		then:
		thrown(IllegalArgumentException)
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

	def divideNull() {
		when:
		Fraction.ZERO.divide(null)

		then:
		thrown(NullPointerException)
	}

	def divide() {
		expect:
		fraction.divide(invertible) == new Fraction(numerator, denominator)

		where:
		fraction << fractions
		invertible << invertibles
		numerator = fraction.numerator * invertible.denominator
		denominator = fraction.denominator * invertible.numerator
	}

	def invertNumeratorIsZero() {
		when:
		Fraction.ZERO.invert()

		then:
		thrown(IllegalStateException)
	}

	def invert() {
		expect:
		fraction.invert() == new Fraction(fraction.denominator, fraction.numerator)

		where:
		fraction << fractions
	}

	def minNull() {
		when:
		Fraction.ZERO.min(null)

		then:
		thrown(NullPointerException)
	}

	def min() {
		expect:
		fraction.min(maximum) == fraction
		fraction.min(minimum) == minimum
		fraction.min(fraction) == fraction

		where:
		fraction << fractions
		minimum = fraction.subtract(Fraction.ONE)
		maximum = fraction.add(Fraction.ONE)
	}

	def maxNull() {
		when:
		Fraction.ZERO.max(null)

		then:
		thrown(NullPointerException)
	}

	def max() {
		expect:
		fraction.max(minimum) == fraction
		fraction.max(maximum) == maximum
		fraction.max(fraction) == fraction

		where:
		fraction << fractions
		maximum = fraction.add(Fraction.ONE)
		minimum = fraction.subtract(Fraction.ONE)
	}

	def lowerThanNull() {
		when:
		Fraction.ZERO.lowerThan(null)

		then:
		thrown(NullPointerException)
	}

	def lowerThan() {
		expect:
		fraction.lowerThan(greater) == true
		fraction.lowerThan(lower) == false
		fraction.lowerThan(fraction) == true

		where:
		fraction << fractions
		greater = fraction.add(Fraction.ONE)
		lower = fraction.subtract(Fraction.ONE)
	}

	def greaterThanNull() {
		when:
		Fraction.ZERO.greaterThan(null)

		then:
		thrown(NullPointerException)
	}

	def greaterThan() {
		expect:
		fraction.greaterThan(lower) == true
		fraction.greaterThan(greater) == false
		fraction.greaterThan(fraction) == true

		where:
		fraction << fractions
		lower = fraction.subtract(Fraction.ONE)
		greater = fraction.add(Fraction.ONE)
	}

	def strictLowerThanNull() {
		when:
		Fraction.ZERO.strictLowerThan(null)

		then:
		thrown(NullPointerException)
	}

	def strictLowerThan() {
		expect:
		fraction.strictLowerThan(greater) == true
		fraction.strictLowerThan(lower) == false
		fraction.strictLowerThan(fraction) == false

		where:
		fraction << fractions
		greater = fraction.add(Fraction.ONE)
		lower = fraction.subtract(Fraction.ONE)
	}

	def strictGreaterThanNull() {
		when:
		Fraction.ZERO.strictGreaterThan(null)

		then:
		thrown(NullPointerException)
	}

	def strictGreaterThan() {
		expect:
		fraction.strictGreaterThan(lower) == true
		fraction.strictGreaterThan(greater) == false
		fraction.strictGreaterThan(fraction) == false

		where:
		fraction << fractions
		lower = fraction.subtract(Fraction.ONE)
		greater = fraction.add(Fraction.ONE)
	}

	def normalize() {
		if (signum < 0)
			return new Fraction(-numerator.abs, denominator.abs)
		if (signum == 0)
			return ZERO
		if (numerator.signum < 0)
			return new Fraction(numerator.abs, denominator.abs)
		this
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

	def quivalentNull() {
		when:
		Fraction.ZERO.equivalent(null)

		then:
		thrown(NullPointerException)
	}

	def equivalent() {
		expect:
		fraction.equivalent(other) == (fraction.reduce() == other.reduce())

		where:
		fraction << fractions
		other << others
	}
}
