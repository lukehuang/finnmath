package mathmyday.lib.number

import mathmyday.lib.util.MathRandom
import spock.lang.Shared
import spock.lang.Specification

class RealComplexNumberSpecification extends Specification {
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

	def add() {
		expect:
		complexNumber.add(other) == new RealComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real + other.real
		imaginary = complexNumber.imaginary + other.imaginary
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
