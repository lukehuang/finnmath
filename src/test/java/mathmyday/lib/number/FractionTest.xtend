package mathmyday.lib.number

import java.util.List
import mathmyday.lib.util.MathRandom
import org.junit.BeforeClass
import org.junit.Test

import static org.assertj.core.api.Assertions.assertThat
import static org.assertj.core.api.Assertions.assertThatThrownBy
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

final class FractionTest {
    static val ZERO = Fraction::ZERO
    static val ONE = Fraction::ONE
    static var List<Fraction> fractions
    static var List<Fraction> others
    static var List<Fraction> invertibles

    @BeforeClass
    static def void setUpClass() {
        val it = new MathRandom
        val howMany = 10
        val bound = 10
        fractions = createFractions(bound, howMany)
        others = createFractions(bound, howMany)
        invertibles = createInvertibleFractions(bound, howMany)
    }

    @Test
    def void newNumeratorNullShouldThrowException() {
        assertThatThrownBy[
            new Fraction(null, 1BI)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void newDenominatorNullShouldThrowException() {
        assertThatThrownBy[
            new Fraction(0BI, null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void newDenominatorZeroShouldThrowException() {
        assertThatThrownBy[
            new Fraction(0BI, 0BI)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void addNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.add(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testAdd() {
        fractions.forEach [
            others.forEach [ other |
                val expectedNumerator = other.denominator * numerator + denominator * other.numerator
                val expectedDenominator = denominator * other.denominator
                assertThat(add(other)).isEqualTo(new Fraction(expectedNumerator, expectedDenominator))
            ]
        ]
    }

    @Test
    def void subtractNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.subtract(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testSubtract() {
        fractions.forEach [
            others.forEach [ other |
                val expectedNumerator = other.denominator * numerator - denominator * other.numerator
                val expectedDenominator = denominator * other.denominator
                assertThat(subtract(other)).isEqualTo(new Fraction(expectedNumerator, expectedDenominator))
            ]
        ]
    }

    @Test
    def void multiplyNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.multiply(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testMultiply() {
        fractions.forEach [
            others.forEach [ other |
                val expectedNumerator = numerator * other.numerator
                val expectedDenominator = denominator * other.denominator
                assertThat(multiply(other)).isEqualTo(new Fraction(expectedNumerator, expectedDenominator))
            ]
        ]
    }

    @Test
    def void divideNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.divide(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void divideZeroShouldThrowException() {
        assertThatThrownBy[
            ZERO.divide(ZERO)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void testDivide() {
        fractions.forEach [
            invertibles.forEach [ invertible |
                assertThat(divide(invertible)).isEqualTo(multiply(invertible.invert))
            ]
        ]
    }

    @Test
    def void testNegate() {
        fractions.forEach [
            assertThat(negate).isEqualTo(new Fraction(-numerator, denominator))
        ]
    }

    @Test
    def void powNegativeExponentShouldThrowException() {
        assertThatThrownBy[
            ZERO.pow(-1)
        ].isExactlyInstanceOf(IllegalArgumentException)
    }

    @Test
    def void testPow() {
        fractions.forEach [
            assertThat(pow(3)).isEqualTo(multiply(multiply(it)))
            assertThat(pow(2)).isEqualTo(multiply(it))
            assertThat(pow(1)).isSameAs(it)
            assertThat(pow(0)).isSameAs(ONE)
        ]
        assertThat(ONE.pow(3)).isEqualTo(ONE)
        assertThat(ZERO.pow(0)).isEqualTo(ONE)
    }

    @Test
    def void invertZeroShouldThrowException() {
        assertThatThrownBy[
            ZERO.invert
        ].isExactlyInstanceOf(IllegalStateException)
    }

    @Test
    def void testInvert() {
        invertibles.forEach [
            assertThat(invert).isEqualTo(new Fraction(denominator, numerator))
        ]
    }

    @Test
    def void lessThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.lessThanOrEqualTo(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testLessThanOrEqualTo() {
        fractions.forEach [
            val greater = add(ONE)
            val lower = subtract(ONE)
            assertTrue(lessThanOrEqualTo(greater))
            assertFalse(lessThanOrEqualTo(lower))
            assertTrue(lessThanOrEqualTo(it))
        ]
    }

    @Test
    def void greaterThanOrEqualToNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.greaterThanOrEqualTo(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testGreaterThanOrEqualTo() {
        fractions.forEach [
            val lower = subtract(ONE)
            val greater = add(ONE)
            assertTrue(greaterThanOrEqualTo(lower))
            assertFalse(greaterThanOrEqualTo(greater))
            assertTrue(greaterThanOrEqualTo(it))
        ]
    }

    @Test
    def void lessThanNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.lessThan(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testLessThan() {
        fractions.forEach [
            val greater = add(ONE)
            val lower = subtract(ONE)
            assertTrue(lessThan(greater))
            assertFalse(lessThan(lower))
            assertFalse(lessThan(it))
        ]
    }

    @Test
    def void greaterThanNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.greaterThan(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testGreaterThan() {
        fractions.forEach [
            val lower = subtract(ONE)
            val greater = add(ONE)
            assertTrue(greaterThan(lower))
            assertFalse(greaterThan(greater))
            assertFalse(greaterThan(it))
        ]
    }

    @Test
    def void minNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.min(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testMin() {
        fractions.forEach [
            val maximum = add(ONE)
            val minimum = subtract(ONE)
            assertThat(min(maximum)).isSameAs(it)
            assertThat(min(minimum)).isSameAs(minimum)
            assertThat(min(it)).isSameAs(it)
        ]
    }

    @Test
    def void maxNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.max(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testMax() {
        fractions.forEach [
            val minimum = subtract(ONE)
            val maximum = add(ONE)
            assertThat(max(minimum)).isSameAs(it)
            assertThat(max(maximum)).isSameAs(maximum)
            assertThat(max(it)).isSameAs(it)
        ]
    }

    @Test
    def void testNormalize() {
        val mathRandom = new MathRandom
        val bound = 10
        val howMany = 10
        mathRandom.createInvertiblePositiveFractions(bound, howMany).forEach [
            val expectedForNegativeSignum = new Fraction(-numerator, denominator)
            assertThat(new Fraction(numerator, -denominator).normalize).isEqualTo(expectedForNegativeSignum)
            assertThat(new Fraction(-numerator, denominator).normalize).isEqualTo(expectedForNegativeSignum)
            assertThat(new Fraction(0BI, denominator).normalize).isEqualTo(ZERO)
            assertThat(new Fraction(-numerator, -denominator).normalize).isEqualTo(it)
            val normalized = normalize
            assertThat(normalized.normalize).isSameAs(normalized)
        ]
    }

    @Test
    def void testAbs() {
        fractions.forEach [
            assertThat(abs).isEqualTo(new Fraction(numerator.abs, denominator.abs))
        ]
    }

    @Test
    def void testSignum() {
        fractions.forEach [
            assertEquals(signum, numerator.signum * denominator.signum)
        ]
    }

    @Test
    def void testReduce() {
        fractions.forEach [
            val gcd = numerator.gcd(denominator)
            assertThat(reduce).isEqualTo(new Fraction(numerator / gcd, denominator / gcd))
        ]
    }

    @Test
    def void equivalentNullShouldThrowException() {
        assertThatThrownBy[
            ZERO.equivalent(null)
        ].isExactlyInstanceOf(NullPointerException)
    }

    @Test
    def void testEquivalent() {
        fractions.forEach [
            others.forEach [ other |
                assertEquals(equivalent(other), reduce == other.reduce)
            ]
        ]
    }
}
