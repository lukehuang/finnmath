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

package mathrandom

import java.math.BigDecimal
import java.util.Random
import org.eclipse.xtend.lib.annotations.ToString
import java.util.ArrayList
import java.util.List
import org.apache.commons.math3.fraction.Fraction
import org.apache.commons.math3.complex.Complex

@ToString
class MathRandom {
    val random = new Random

    def createPositiveInt(int bound) {
        random.nextInt(bound)
    }

    def createNegativeInt(int bound) {
        -random.nextInt(bound)
    }

    def createInt(int bound) {
        if (random.nextBoolean)
            return createNegativeInt(bound)
        createPositiveInt(bound)
    }

    def createPositiveInts(int bound, int howMany) {
        var ints = newArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, createPositiveInt(bound))
        ints
    }

    def createNegativeInts(int bound, int howMany) {
        var ints = newArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, createNegativeInt(bound))
        ints
    }

    def createInts(int bound, int howMany) {
        var ints = newArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, createInt(bound))
        ints
    }

    def createPositiveDecimal(int bound, int scale) {
        val decimal = createDecimal(bound, scale)
        if (decimal < BigDecimal.ZERO)
            return -decimal
        decimal
    }

    def createNegativeDecimal(int bound, int scale) {
        val decimal = createDecimal(bound, scale)
        if (decimal > BigDecimal.ZERO)
            return -decimal
        decimal
    }

    def createDecimal(int bound, int scale) {
        val decimal = BigDecimal.valueOf(random.nextDouble)
        keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal.ROUND_HALF_UP)
    }

    def keepDecimalInBound(BigDecimal decimal, int bound) {
        var value = decimal
        val decimalBound = BigDecimal.valueOf(bound)
        if (value >= BigDecimal.ZERO)
            while (value >= decimalBound)
                value -= decimalBound
        else
            while (value.abs >= decimalBound)
                value += decimalBound
        value
    }

    def createPositiveDecimals(int bound, int scale, int howMany) {
        var List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals.add(createPositiveDecimal(bound, scale))
        decimals
    }

    def createNegativeDecimals(int bound, int scale, int howMany) {
        var List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals.add(createNegativeDecimal(bound, scale))
        decimals
    }

    def createDecimals(int bound, int scale, int howMany) {
        var List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals.add(createDecimal(bound, scale))
        decimals
    }

    def createPositiveFraction(int bound) {
        new Fraction(random.nextInt(bound), random.nextInt(bound - 1) + 1)
    }

    def createNegativeFraction(int bound) {
        new Fraction(random.nextInt(bound), random.nextInt(bound - 1) + 1).negate
    }

    def createFraction(int bound) {
        if (random.nextBoolean)
            return createNegativeFraction(bound)
        createPositiveFraction(bound)
    }

    def createPositiveFractions(int bound, int howMany) {
        val List<Fraction> fractions = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            fractions.add(createPositiveFraction(bound))
        fractions
    }

    def createNegativeFractions(int bound, int howMany) {
        val List<Fraction> fractions = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            fractions.add(createNegativeFraction(bound))
        fractions
    }

    def createFractions(int bound, int howMany) {
        val List<Fraction> fractions = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            fractions.add(createFraction(bound))
        fractions
    }

    def createComplexNumber(int bound) {
        new Complex(createInt(bound), createInt(bound))
    }
    
    def createComplexNumbers(int bound, int howMany) {
        var List<Complex> complexes = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            complexes.add(createComplexNumber(bound))
        complexes
    }
}
