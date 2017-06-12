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

class RealComplexNumberSpecification extends Specification {
	@Shared
	RealComplexNumber ZERO = RealComplexNumber.ZERO

	@Shared
	List<RealComplexNumber> complexNumbers

	@Shared
	List<RealComplexNumber> others

	def setupSpec() {
		def size = 100
		def mathRandom = new MathRandom()
		complexNumbers = mathRandom.createRealComplexNumbers(Integer.MAX_VALUE, size)
		others = mathRandom.createRealComplexNumbers(Integer.MAX_VALUE, size)
	}

	def newRealNull() {
		when:
		new RealComplexNumber(null, 0)

		then:
		thrown(NullPointerException)
	}

	def newImaginaryNull() {
		when:
		new RealComplexNumber(0, null)

		then:
		thrown(NullPointerException)
	}

	def addNull() {
		when:
		ZERO.add(null)

		then:
		thrown(NullPointerException)
	}

	def add() {
		expect:
		complexNumber.add(other) == new RealComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real + other.real
		imaginary = complexNumber.imaginary + other.imaginary
	}

	def subtractNull() {
		when:
		ZERO.subtract(null)

		then:
		thrown(NullPointerException)
	}

	def subtract() {
		expect:
		complexNumber.subtract(other) == new RealComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real - other.real
		imaginary = complexNumber.imaginary - other.imaginary
	}

	def multiplyNull() {
		when:
		ZERO.multiply(null)

		then:
		thrown(NullPointerException)
	}

	def multiply() {
		expect:
		complexNumber.multiply(other) == new RealComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real * other.real - complexNumber.imaginary * other.imaginary
		imaginary = complexNumber.imaginary * other.real + complexNumber.real * other.imaginary
	}

	def pow() {
		expect:
		complexNumber.pow(3) == complexNumber.multiply(complexNumber.multiply(complexNumber))
		complexNumber.pow(2) == complexNumber.multiply(complexNumber)
		complexNumber.pow(1) == complexNumber
		complexNumber.pow(0) == RealComplexNumber.ONE

		where:
		complexNumber << complexNumbers
	}

	def negate() {
		expect:
		complexNumber.negate() == new RealComplexNumber(-complexNumber.real, -complexNumber.imaginary)

		where:
		complexNumber << complexNumbers
	}

	def absPow2() {
		expect:
		complexNumber.absPow2() == complexNumber.real**2 + complexNumber.imaginary**2

		where:
		complexNumber << complexNumbers
	}

	def conjugate() {
		expect:
		complexNumber.conjugate() == new RealComplexNumber(complexNumber.real, -complexNumber.imaginary)

		where:
		complexNumber << complexNumbers
	}
}
