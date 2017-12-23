package com.github.ltennstedt.finnmath.kotlin.number

import com.github.ltennstedt.finnmath.core.number.Fraction

operator fun Fraction.plus(fraction: Fraction) = add(fraction)

operator fun Fraction.minus(fraction: Fraction) = subtract(fraction)

operator fun Fraction.times(fraction: Fraction) = multiply(fraction)

operator fun Fraction.div(fraction: Fraction) = divide(fraction)

operator fun Fraction.unaryMinus() = negate()

operator fun Fraction.inc() = add(Fraction.ONE)

operator fun Fraction.dec() = subtract(Fraction.ONE)
