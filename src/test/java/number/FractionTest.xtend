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
        fractions.forEach[
            assertThat(it.abs, is(new Fraction(numerator.abs, denominator.abs)))
        ]
    }
    
    @Test
    def signum() {
        fractions.forEach[
            assertThat(it.signum, is(numerator.signum * denominator.signum))
        ]
    }
    
    @Test
    def reduce() {
        fractions.forEach[
            val gcd = numerator.gcd(denominator)
            assertThat(it.reduce, is(new Fraction(numerator / gcd, denominator / gcd)))
        ]
    }
    
    @Test
    def equivalent() {
        fractions.forEach[
            val other = others.get(fractions.indexOf(it))
            assertThat(it.equivalent(other), is(reduce == other.reduce))
        ]
    }
}
