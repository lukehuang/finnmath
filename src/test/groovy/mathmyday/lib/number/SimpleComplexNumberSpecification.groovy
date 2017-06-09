package mathmyday.lib.number

import mathmyday.lib.util.MathRandom
import spock.lang.Shared
import spock.lang.Specification

class SimpleComplexNumberSpecification extends Specification {
	@Shared
	List<SimpleComplexNumber> complexNumbers

	@Shared
	List<SimpleComplexNumber> others

	def setupSpec() {
		def size = 100
		def mathRandom = new MathRandom()
		complexNumbers = mathRandom.createSimpleComplexNumbers(Integer.MAX_VALUE, size)
		others = mathRandom.createSimpleComplexNumbers(Integer.MAX_VALUE, size)
	}

	def add() {
		expect:
		complexNumber.add(other) == new SimpleComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real + other.real
		imaginary = complexNumber.imaginary + other.imaginary
	}

	def subtract() {
		expect:
		complexNumber.subtract(other) == new SimpleComplexNumber(real, imaginary)

		where:
		complexNumber << complexNumbers
		other << others
		real = complexNumber.real - other.real
		imaginary = complexNumber.imaginary - other.imaginary
	}

	def multiply() {
		expect:
		complexNumber.multiply(other) == new SimpleComplexNumber(real, imaginary)

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
		complexNumber.pow(0) == SimpleComplexNumber.ONE

		where:
		complexNumber << complexNumbers
	}

	def negate() {
		expect:
		complexNumber.negate() == new SimpleComplexNumber(-complexNumber.real, -complexNumber.imaginary)

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
		complexNumber.conjugate() == new SimpleComplexNumber(complexNumber.real, -complexNumber.imaginary)

		where:
		complexNumber << complexNumbers
	}
}
