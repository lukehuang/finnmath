package number

import java.math.BigInteger
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.EqualsHashCode
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument

@EqualsHashCode
@ToString
@Accessors
class Fraction {
    static val ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE)
    static val ONE = new Fraction(BigInteger.ONE, BigInteger.ONE)
    final BigInteger numerator
    final BigInteger denominator

    new(BigInteger numerator, BigInteger denominator) {
        checkArgument(denominator != BigInteger.ZERO, "denominator = 0")
        this.numerator = numerator
        this.denominator = denominator
    }

    def reduce() {
        var gcd = numerator.gcd(denominator)
        new Fraction(numerator / gcd, numerator / gcd)
    }

    def add(Fraction summand) {
        val newNumerator = summand.getDenominator * numerator + denominator * summand.getNumerator
        val newDenominator = denominator * summand.getDenominator
        new Fraction(newNumerator, newDenominator)
    }

    def subtract(Fraction subtrahend) {
        val newNumerator = subtrahend.getDenominator * numerator - denominator * subtrahend.getNumerator
        val newDenominator = denominator * subtrahend.getDenominator
        new Fraction(newNumerator, newDenominator)
    }

    def multiply(Fraction factor) {
        val newNumerator = numerator * factor.getNumerator
        val newDenominator = denominator * factor.getDenominator
        new Fraction(newNumerator, newDenominator)
    }

    def divide(Fraction divisor) {
        val newNumerator = numerator * divisor.getDenominator
        val newDenominator = denominator * divisor.getNumerator
        new Fraction(newNumerator, newDenominator)
    }

    def abs() {
        new Fraction(numerator.abs, denominator.abs)
    }

    def negate() {
        new Fraction(numerator.negate, denominator)
    }

    def signum() {
        numerator.signum * denominator.signum
    }

    def min(Fraction other) {
        val newNumerator = other.getDenominator * numerator
        val newOtherNumerator = denominator * other.getNumerator
        if (newOtherNumerator < newNumerator)
            return other
        this
    }

    def max(Fraction other) {
        val newNumerator = other.getDenominator * numerator
        val newOtherNumerator = denominator * other.getNumerator
        if (newOtherNumerator > newNumerator)
            return other
        this
    }

    def equivalent(Fraction other) {
        other.reduce == this.reduce
    }
}
