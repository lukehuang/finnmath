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

package mathmyday.lib.util

import spock.lang.Shared
import spock.lang.Specification

class MathRandomSpecification extends Specification {
	@Shared
	int howMany = 100

	@Shared
	int[] bounds = new int[howMany]

	@Shared
	int[] scales = new int[howMany]

	@Shared
	int[] sizes = new int[howMany]

	@Shared
	MathRandom mathRandom = new MathRandom()

	def setupSpec() {
		def random = new Random()
		for (i in 0..howMany - 1) {
			bounds[i] = random.nextInt(Integer.MAX_VALUE)
			scales[i] = random.nextInt(10)
			sizes[i] = i + 1
		}
	}

	def createPositiveInt() {
		expect:
		0 <= integer
		integer < bound

		where:
		bound << bounds
		integer = mathRandom.createPositiveInt(bound)
	}

	def createNegativeInt() {
		expect:
		-bound < integer
		integer <= 0

		where:
		bound << bounds
		integer = mathRandom.createNegativeInt(bound)
	}

	def createInt() {
		expect:
		-bound < integer
		integer < bound

		where:
		bound << bounds
		integer = mathRandom.createInt(bound)
	}


	def createPositiveInts() {
		expect:
		ints.length == size

		where:
		size << sizes
		ints = mathRandom.createPositiveInts(1, size)
	}

	def createNegativeInts() {
		expect:
		ints.length == size

		where:
		size << sizes
		ints = mathRandom.createNegativeInts(1, size)
	}

	def createInts() {
		expect:
		ints.length == size

		where:
		size << sizes
		ints = mathRandom.createInts(1, size)
	}

	def createPositiveDecimal() {
		expect:
		0 <= decimal
		decimal < bound
		decimal.scale == scale

		where:
		bound << bounds
		scale << scales
		decimal = mathRandom.createPositiveDecimal(bound, scale)
	}

	def createNegativeDecimal() {
		expect:
		-bound < decimal
		decimal <= 0
		decimal.scale == scale

		where:
		bound << bounds
		scale << scales
		decimal = mathRandom.createNegativeDecimal(bound, scale)
	}

	def createDecimal() {
		expect:
		-bound < decimal
		decimal < bound
		decimal.scale == scale

		where:
		bound << bounds
		scale << scales
		decimal = mathRandom.createDecimal(bound, scale)
	}

	def createPositiveDecimals() {
		expect:
		decimals.size == size

		where:
		size << sizes
		decimals = mathRandom.createPositiveDecimals(1, 1, size)
	}

	def createNegativeDecimals() {
		expect:
		decimals.size == size

		where:
		size << sizes
		decimals = mathRandom.createNegativeDecimals(1, 1, size)
	}

	def createDecimals() {
		expect:
		decimals.size == size

		where:
		size << sizes
		decimals = mathRandom.createDecimals(1, 1, size)
	}

	def createPositiveFraction() {
		expect:
		0 <= fraction.numerator
		fraction.numerator < bound
		0 < fraction.denominator
		fraction.denominator < bound

		where:
		bound << bounds
		fraction = mathRandom.createPositiveFraction(bound)
	}

	def createNegativeFraction() {
		expect:
		-bound < fraction.numerator
		fraction.numerator <= 0
		0 < fraction.denominator
		fraction.denominator < bound

		where:
		bound << bounds
		fraction = mathRandom.createNegativeFraction(bound)
	}

	def createFraction() {
		expect:
		-bound < fraction.numerator
		fraction.numerator < bound
		-bound < fraction.denominator
		fraction.denominator < bound
		fraction.denominator != 0

		where:
		bound << bounds
		fraction = mathRandom.createFraction(bound)
	}

	def createPositiveFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createPositiveFractions(2, size)
	}

	def createNegativeFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createNegativeFractions(2, size)
	}

	def createFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createFractions(2, size)
	}

	def createInvertiblePositiveFraction() {
		expect:
		0 < fraction.numerator
		fraction.numerator < bound
		0 < fraction.denominator
		fraction.denominator < bound

		where:
		bound << bounds
		fraction = mathRandom.createInvertiblePositiveFraction(bound)
	}

	def createInvertibleNegativeFraction() {
		expect:
		-bound < fraction.numerator
		fraction.numerator < 0
		0 < fraction.denominator
		fraction.denominator < bound

		where:
		bound << bounds
		fraction = mathRandom.createInvertibleNegativeFraction(bound)
	}

	def createInvertibleFraction() {
		expect:
		-bound < fraction.numerator
		fraction.numerator < bound
		-bound < fraction.denominator
		fraction.denominator < bound
		fraction.denominator != 0

		where:
		bound << bounds
		fraction = mathRandom.createInvertibleFraction(bound)
	}

	def createInvertiblePositiveFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createInvertiblePositiveFractions(2, size)
	}

	def createInvertibleNegativeFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createInvertibleNegativeFractions(2, size)
	}

	def createInvertibleFractions() {
		expect:
		fractions.size ==  size

		where:
		size << sizes
		fractions = mathRandom.createInvertibleFractions(2, size)
	}

	def createSimpleComplexNumber() {
		expect:
		-bound < complexNumber.real
		complexNumber.real < bound
		-bound < complexNumber.imaginary
		complexNumber.imaginary < bound

		where:
		bound << bounds
		complexNumber = mathRandom.createSimpleComplexNumber(bound)
	}

	def createSimpleComplexNumbers() {
		expect:
		complexNumbers.size ==  size

		where:
		size << sizes
		complexNumbers = mathRandom.createSimpleComplexNumbers(1, size)
	}

	def createRealComplexNumber() {
		expect:
		-bound < complexNumber.real
		complexNumber.real < bound
		-bound < complexNumber.imaginary
		complexNumber.imaginary < bound

		where:
		bound << bounds
		complexNumber = mathRandom.createRealComplexNumber(bound)
	}

	def createRealComplexNumbers() {
		expect:
		complexNumbers.size ==  size

		where:
		size << sizes
		complexNumbers = mathRandom.createRealComplexNumbers(1, size)
	}
}
