package number

import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Data

@Data
class SimpleComplexNumber {
    static val ZERO = new SimpleComplexNumber(BigInteger.ZERO, BigInteger.ZERO)
    static val ONE = new SimpleComplexNumber(BigInteger.ONE, BigInteger.ZERO)
    BigInteger real
    BigInteger imaginary

    def add(SimpleComplexNumber summand) {
        new SimpleComplexNumber(real + summand.getReal, imaginary + summand.getImaginary)
    }

    def subtract(SimpleComplexNumber subtrahend) {
        new SimpleComplexNumber(real - subtrahend.getReal, imaginary - subtrahend.getImaginary)
    }

    def multiply(SimpleComplexNumber factor) {
        val newReal = real * factor.getReal - imaginary * factor.getImaginary
        val newImaginary = real * factor.getImaginary + imaginary * factor.getReal
        new SimpleComplexNumber(newReal, newImaginary)
    }

    def divide(SimpleComplexNumber divisor) {
    }

    def absPow2() {
        real.pow(2) + imaginary.pow(2)
    }

    def conjugate() {
        new SimpleComplexNumber(real, -imaginary)
    }
    
    def negate() {
        new SimpleComplexNumber(-real, -imaginary)
    }
}
