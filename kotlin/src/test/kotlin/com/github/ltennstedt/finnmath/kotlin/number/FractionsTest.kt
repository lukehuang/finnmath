package com.github.ltennstedt.finnmath.kotlin.number

import assertk.assert
import assertk.assertions.isEqualTo
import com.github.ltennstedt.finnmath.core.number.Fraction
import com.github.ltennstedt.finnmath.core.util.MathRandom
import org.junit.Test

class FractionsTest {
    private val howMany = 10
    private val bound = 10L
    private val mathRandom = MathRandom(7)
    private val fractions = mathRandom.nextFractions(bound, howMany)
    private val others = mathRandom.nextFractions(bound, howMany)

    @Test
    fun plusShouldSucceed() {
        fractions.forEach {
            others.forEach { other ->
                val expectedNumerator = other.denominator.multiply(it.numerator).add(
                        it.denominator.multiply(other.numerator))
                val expectedDenominator = it.denominator.multiply(other.denominator)
                val expected = Fraction(expectedNumerator, expectedDenominator)
                assert(it + other).isEqualTo(expected)
            }
        }
    }
}
