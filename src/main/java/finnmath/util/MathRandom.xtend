/*
 * BSD 2-Clause License
 * 
 * Copyright (c) 2017, Lars Tennstedt
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

package finnmath.util

import com.google.common.annotations.Beta
import finnmath.linear.BigIntMatrix
import finnmath.linear.BigIntVector
import finnmath.linear.DecimalMatrix
import finnmath.linear.DecimalVector
import finnmath.number.Fraction
import finnmath.number.RealComplexNumber
import finnmath.number.SimpleComplexNumber
import java.math.BigDecimal
import java.math.BigInteger
import java.util.ArrayList
import java.util.List
import java.util.Random
import org.apache.commons.lang3.RandomUtils
import org.eclipse.xtend.lib.annotations.ToString

import static com.google.common.base.Preconditions.checkArgument
import static java.util.Objects.requireNonNull

/**
 * A pseudo random generator for {@code long}, {@link BigDecimal}, {@link Fraction}, {@link SimpleComplexNumber}, 
 * {@link RealComplexNumber}, {@link BigIntVector}, {@link DecimalVector}, {@link BigIntMatrix} and 
 * {@link DecimalMatrix}
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
@ToString
final class MathRandom {
    val random = new Random

    /**
     * Returns a positive {@code long} bounded below by {@code 0} (inclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveLong(long bound) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        RandomUtils::nextLong(0, bound)
    }

    /**
     * Returns a negative {@code long} bounded below by {@code -bound} (exclusive) and above by {@code 0} (inclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeLong(long bound) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        (-1) * RandomUtils::nextLong(0, bound)
    }

    /**
     * Returns a {@code long} bounded below by {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@code long}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextLong(long bound) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        if (random.nextBoolean)
            return nextNegativeLong(bound)
        nextPositiveLong(bound)
    }

    /**
     * Returns an array of the length of {@code howMany} containing positive {@code long longs}
     * 
     * @param bound the bound
     * @param howMany the length of the resulting array
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextPositiveLong
     */
    def nextPositiveLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, nextPositiveLong(bound))
        ints
    }

    /**
     * Returns an array of the length of {@code howMany} containing negative {@code long longs}
     * 
     * @param bound  {@code long}
     * @param howMany  {@code int}
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextNegativeLong
     */
    def nextNegativeLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, nextNegativeLong(bound))
        ints
    }

    /**
     * Returns an array of the length of {@code howMany} containing {@code long longs}
     * 
     * @param bound the bound
     * @param howMany the length of the resulting array
     * @return An array of pseudo random {@code long longs}
     * @throws IllegalArgumentException if {@code  bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     */
    def nextLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (var i = 0; i < howMany; i++)
            ints.set(i, nextLong(bound))
        ints
    }

    /**
     * Returns a positive {@link BigDecimal} of a given {@code scale} bounded below by {@code 0} (inclusive) and above  
     * by {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveDecimal(long bound, int scale) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = nextDecimal(bound, scale)
        if (decimal < 0BD)
            return -decimal
        decimal
    }

    /**
     * Returns a negative {@link BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and  
     * above by {@code 0} (inclusive)
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeDecimal(long bound, int scale) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = nextDecimal(bound, scale)
        if (decimal > 0BD)
            return -decimal
        decimal
    }

    /**
     * Returns a {@link BigDecimal} of a given {@code scale} bounded below by {@code -bound} (exclusive) and above by  
     * {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextDecimal(long bound, int scale) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = BigDecimal::valueOf(RandomUtils::nextLong(0, bound))
        keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal::ROUND_HALF_UP)
    }

    /**
     * Returns a positive {@link BigDecimal} which is invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextPositiveDecimal
     */
    def nextInvertiblePositiveDecimal(long bound, int scale) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = nextInvertibleDecimal(bound, scale)
        if (decimal < 0BD)
            return -decimal
        decimal
    }

    /**
     * Returns a negative {@link BigDecimal} which is invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextNegativeDecimal
     * @see #nextInvertibleDecimal
     */
    def nextInvertibleNegativeDecimal(long bound, int scale) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = nextInvertibleDecimal(bound, scale)
        if (decimal > 0BD)
            return -decimal
        decimal
    }

    /**
     * Returns a {@link BigDecimal} which is invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     */
    def nextInvertibleDecimal(long bound, int scale) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val decimal = BigDecimal::valueOf(RandomUtils::nextLong(1, bound))
        keepDecimalInBound(decimal, bound).setScale(scale, BigDecimal::ROUND_HALF_UP)
    }

    private def keepDecimalInBound(BigDecimal decimal, long bound) {
        requireNonNull(decimal, 'decimal')
        checkArgument(bound > 0, 'expected > 0 but actual %s', bound)
        var it = decimal
        val decimalBound = BigDecimal::valueOf(bound)
        if (it >= 0BD)
            while (it >= decimalBound)
                it -= decimalBound
        else
            while (abs >= decimalBound)
                it += decimalBound
        it
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive {@link BigDecimal BigDecimals}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextPositiveDecimal
     */
    def nextPositiveDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextPositiveDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link BigDecimal BigDecimals}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextNegativeDecimal
     */
    def nextNegativeDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextNegativeDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigDecimal BigDecimals}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     */
    def nextDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive {@link BigDecimal BigDecimals} which 
     * are invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertiblePositiveDecimal
     */
    def nextInvertiblePositiveDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextInvertiblePositiveDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link BigDecimal BigDecimals} which 
     * are invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleNegativeDecimal
     */
    def nextInvertibleNegativeDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextInvertibleNegativeDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@code List} of the size of {@code howMany} containing {@link BigDecimal BigDecimals} which are 
     * invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigDecimal BigDecimals}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleDecimal
     */
    def nextInvertibleDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            decimals += nextInvertibleDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by {@code 0} (inclusive) and above by 
     * {@code bound} (exclusive) and whose {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound} 
     * (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        val numerator = BigInteger::valueOf(RandomUtils::nextLong(0, bound))
        val denominator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
        new Fraction(numerator, denominator)
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by {@code -bound} (exclusive) and above by  
     * {@code 0} (inclusive) and whose {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound}  
     * (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        nextPositiveFraction(bound).negate
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by {@code -bound} (exclusive) and above by  
     * {@code bound} (exclusive) and whose {@code denominator} is bounded below {@code -bound} (exclusive) and  
     * {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        if (random.nextBoolean)
            return nextNegativeFraction(bound)
        nextPositiveFraction(bound)
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive {@link Fraction Fractions}
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions} 
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextPositiveFraction
     */
    def nextPositiveFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (var i = 0; i < howMany; i++)
            fractions += nextPositiveFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link Fraction Fractions}
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextNegativeFraction
     */
    def nextNegativeFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<Fraction> fractions = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            fractions += nextNegativeFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link Fraction Fractions}
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextFraction
     */
    def nextFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<Fraction> fractions = new ArrayList(howMany)
        for (var i = 0; i < howMany; i++)
            fractions += nextFraction(bound)
        fractions
    }

    /**
     * Returns a positive {@link Fraction} which is invertible
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextPositiveFraction
     */
    def nextInvertiblePositiveFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        val numerator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
        val denominator = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
        new Fraction(numerator, denominator)
    }

    /**
     * Returns a negative {@link Fraction} which is invertible
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextNegativeFraction
     */
    def nextInvertibleNegativeFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        nextInvertiblePositiveFraction(bound).negate
    }

    /**
     * Returns a {@link Fraction} which is invertible
     * 
     * @param bound the bound
     * @return A pseudo random {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextFraction
     */
    def nextInvertibleFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        if (random.nextBoolean)
            return nextInvertibleNegativeFraction(bound)
        nextInvertiblePositiveFraction(bound)
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive {@link Fraction Fractions} which are 
     * invertible
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertiblePositiveFraction
     */
    def nextInvertiblePositiveFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (var i = 0; i < howMany; i++)
            fractions += nextInvertiblePositiveFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link Fraction Fractions} which are 
     * invertible
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleNegativeFraction
     */
    def nextInvertibleNegativeFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (var i = 0; i < howMany; i++)
            fractions += nextInvertibleNegativeFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link Fraction Fractions} which are 
     * invertible
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link Fraction Fractions}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleFraction
     */
    def nextInvertibleFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (var i = 0; i < howMany; i++)
            fractions += nextInvertibleFraction(bound)
        fractions
    }

    /**
     * Returns a {@link SimpleComplexNumber} whose {@code real} and {@code imaginary} part are bounded below by 
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextSimpleComplexNumber(long bound) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        val real = BigInteger::valueOf(nextLong(bound))
        val imaginary = BigInteger::valueOf(nextLong(bound))
        new SimpleComplexNumber(real, imaginary)
    }

    /**
     * Returns a {@link SimpleComplexNumber} which is invertible
     * 
     * @param bound the bound
     * @return A pseudo random {@link SimpleComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSimpleComplexNumber
     */
    def nextInvertibleSimpleComplexNumber(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        val nonZeroPart = BigInteger::valueOf(RandomUtils::nextLong(1, bound))
        val possibleZeroPart = if (random.nextBoolean) RandomUtils::nextLong(1, bound) else nextLong(bound)
        if (random.nextBoolean)
            return new SimpleComplexNumber(BigInteger::valueOf(possibleZeroPart), nonZeroPart)
        new SimpleComplexNumber(nonZeroPart, BigInteger::valueOf(possibleZeroPart))
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing 
     * {@link SimpleComplexNumber SimpleComplexNumbers}
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A List {@link List} of pseudo random {@link SimpleComplexNumber SimpleComplexNumbers}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSimpleComplexNumber
     */
    def nextSimpleComplexNumbers(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
        for (var i = 0; i < howMany; i++)
            complexNumbers += nextSimpleComplexNumber(bound)
        complexNumbers
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing 
     * {@link SimpleComplexNumber SimpleComplexNumbers} which are invertible
     * 
     * @param bound the bound
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link SimpleComplexNumber SimpleComplexNumbers} 
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleSimpleComplexNumber
     */
    def nextInvertibleSimpleComplexNumbers(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
        for (var i = 0; i < howMany; i++)
            complexNumbers += nextInvertibleSimpleComplexNumber(bound)
        complexNumbers
    }

    /**
     * Returns a {@link RealComplexNumber} whose {@code real} and {@code imaginary} part are bounded below by 
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link RealComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     */
    def nextRealComplexNumber(long bound, int scale) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val real = nextDecimal(bound, scale)
        val imaginary = nextDecimal(bound, scale)
        new RealComplexNumber(real, imaginary)
    }

    /**
     * Returns a {@link RealComplexNumber} which is invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @return A pseudo random {@link RealComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextRealComplexNumber
     */
    def nextInvertibleRealComplexNumber(long bound, int scale) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        val nonZeroPart = nextInvertibleDecimal(bound, scale)
        val possibleZeroPart = if (random.nextBoolean)
                nextInvertibleDecimal(bound, scale)
            else
                nextDecimal(bound, scale)
        if (random.nextBoolean)
            return new RealComplexNumber(possibleZeroPart, nonZeroPart)
        new RealComplexNumber(nonZeroPart, possibleZeroPart)
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link RealComplexNumber RealComplexNumbers}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumber RealComplexNumbers}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextRealComplexNumber
     */
    def nextRealComplexNumbers(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
        for (var i = 0; i < howMany; i++)
            complexNumbers += nextRealComplexNumber(bound, scale)
        complexNumbers
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link RealComplexNumber RealComplexNumbers} 
     * which are invertible
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link RealComplexNumber RealComplexNumbers}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextInvertibleRealComplexNumber
     */
    def nextInvertibleRealComplexNumbers(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
        for (var i = 0; i < howMany; i++)
            complexNumbers += nextInvertibleRealComplexNumber(bound, scale)
        complexNumbers
    }

    /**
     * Returns a {@link BigIntVector}
     * 
     * @param bound the bound
     * @param size the size of the resulting {@link BigIntVector}  
     * @return A pseudo random {@link BigIntVector} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     */
    def nextBigIntVector(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntVector::builder(size)
        for (var i = 0; i < size; i++)
            builder.put(BigInteger::valueOf(nextLong(bound)))
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigIntVector BigIntVectors}
     * 
     * @param bound the bound
     * @param size the sizes of the resulting {@link BigIntVector BigIntVectors}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntVector BigIntVectors}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextBigIntVector
     */
    def nextBigIntVectors(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val vectors = new ArrayList<BigIntVector>(howMany) as List<BigIntVector>
        for (var i = 0; i < howMany; i++)
            vectors += nextBigIntVector(bound, size)
        vectors
    }

    /**
     * Returns a {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param rowSize the row size of the resulting {@link BigIntMatrix}
     * @param columnSize the column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     */
    def nextBigIntMatrix(long bound, int rowSize, int columnSize) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        val builder = BigIntMatrix::builder(rowSize, columnSize)
        (1 .. rowSize).forEach [ rowIndex |
            (1 .. columnSize).forEach [ columnIndex |
                builder.put(rowIndex, columnIndex, BigInteger::valueOf(nextLong(bound)))
            ]
        ]
        builder.build
    }

    /**
     * Returns an upper triangular {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of resulting {@link BigIntMatrix}
     * @return A pseudo random upper triangular {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     * @see BigIntMatrix#upperTriangular
     */
    def nextUpperTriangularBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex <= columnIndex)
                    builder.put(rowIndex, columnIndex, BigInteger::valueOf(nextLong(bound)))
                else
                    builder.put(rowIndex, columnIndex, 0BI)
            ]
        ]
        builder.build
    }

    /**
     * Returns an lower triangular {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random lower triangular {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLong
     * @see BigIntMatrix#lowerTriangular
     */
    def nextLowerTriangularBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex >= columnIndex)
                    builder.put(rowIndex, columnIndex, BigInteger::valueOf(nextLong(bound)))
                else
                    builder.put(rowIndex, columnIndex, 0BI)
            ]
        ]
        builder.build
    }

    /**
     * Returns a triangular {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random triangular {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularBigIntMatrix
     * @see #nextLowerTriangularBigIntMatrix
     * @see BigIntMatrix#triangular
     */
    def nextTriangularBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        if (random.nextBoolean)
            return nextLowerTriangularBigIntMatrix(bound, size)
        nextUpperTriangularBigIntMatrix(bound, size)
    }

    /**
     * Returns a diagonal {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random diagonal {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#diagonal
     */
    def nextDiagonalBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex == columnIndex)
                    builder.put(rowIndex, columnIndex, BigInteger::valueOf(nextLong(bound)))
                else
                    builder.put(rowIndex, columnIndex, 0BI)
            ]
        ]
        builder.build
    }

    /**
     * Returns a symmetric {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random symmetric {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#symmetric
     */
    def nextSymmetricBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                val entry = BigInteger::valueOf(nextLong(bound))
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, entry)
                    builder.put(columnIndex, rowIndex, entry)
                } else
                    builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    /**
     * Returns a skew-symmetric {@link BigIntMatrix}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix}
     * @return A pseudo random skew-symmetric {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see BigIntMatrix#skewSymmetric
     */
    def nextSkewSymmetricBigIntMatrix(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                val entry = BigInteger::valueOf(nextLong(bound))
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, entry)
                    builder.put(columnIndex, rowIndex, -entry)
                } else
                    builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param rowSize the row size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param columnSize the column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextBigIntMatrix
     */
    def nextBigIntMatrices(long bound, int rowSize, int columnSize, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextBigIntMatrix(bound, rowSize, columnSize)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper triangular 
     * {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularBigIntMatrix
     */
    def nextUpperTriangularBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextUpperTriangularBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower triangular 
     * {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLowerTriangularBigIntMatrix
     */
    def nextLowerTriangularBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextLowerTriangularBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextTriangularBigIntMatrix
     */
    def nextTriangularBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextTriangularBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDiagonalBigIntMatrix
     */
    def nextDiagonalBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextDiagonalBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSymmetricBigIntMatrix
     */
    def nextSymmetricBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextSymmetricBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing skew-symmetric 
     * {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound the bound
     * @param size the row and column size of the resulting {@link BigIntMatrix BigIntMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric {@link BigIntMatrix BigIntMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSkewSymmetricBigIntMatrix
     */
    def nextSkewSymmetricBigIntMatrices(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextSkewSymmetricBigIntMatrix(bound, size)
        matrices
    }

    /**
     * Returns a {@link DecimalVector}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size the size of the resulting {@link DecimalVector}
     * @return A speudo random {@link DecimalVector} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     */
    def nextDecimalVector(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalVector::builder(size)
        for (var i = 0; i < size; i++)
            builder.put(nextDecimal(bound, scale))
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link DecimalVector DecimalVectors}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param size the size of the resulting {@link DecimalVector DecimalVectors}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link DecimalVector DecimalVectors}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimalVector
     */
    def nextDecimalVectors(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val vectors = new ArrayList<DecimalVector>(howMany) as List<DecimalVector>
        for (var i = 0; i < howMany; i++)
            vectors += nextDecimalVector(bound, scale, size)
        vectors
    }

    /**
     * Returns a {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale to be set on the {@link BigDecimal BigDecimals}
     * @param rowSize the row size of the resulting {@link DecimalMatrix}
     * @param columnSize the column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     */
    def nextDecimalMatrix(long bound, int scale, int rowSize, int columnSize) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        val builder = DecimalMatrix::builder(rowSize, columnSize)
        (1 .. rowSize).forEach [ rowIndex |
            (1 .. columnSize).forEach [ columnIndex |
                builder.put(rowIndex, columnIndex, nextDecimal(bound, scale))
            ]
        ]
        builder.build
    }

    /**
     * Returns an upper triangular {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of resulting {@link DecimalMatrix}
     * @return A pseudo random upper triangular {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     * @see DecimalMatrix#upperTriangular
     */
    def nextUpperTriangularDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex <= columnIndex)
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale))
                else
                    builder.put(rowIndex, columnIndex, 0BD)
            ]
        ]
        builder.build
    }

    /**
     * Returns an lower triangular {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random lower triangular {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimal
     * @see DecimalMatrix#lowerTriangular
     */
    def nextLowerTriangularDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex >= columnIndex)
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale))
                else
                    builder.put(rowIndex, columnIndex, 0BD)
            ]
        ]
        builder.build
    }

    /**
     * Returns a triangular {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random triangular {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularDecimalMatrix
     * @see #nextLowerTriangularDecimalMatrix
     * @see DecimalMatrix#triangular
     */
    def nextTriangularDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        if (random.nextBoolean)
            return nextLowerTriangularDecimalMatrix(bound, scale, size)
        nextUpperTriangularDecimalMatrix(bound, scale, size)
    }

    /**
     * Returns a diagonal {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random diagonal {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#diagonal
     */
    def nextDiagonalDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                if (rowIndex == columnIndex)
                    builder.put(rowIndex, columnIndex, nextDecimal(bound, scale))
                else
                    builder.put(rowIndex, columnIndex, 0BD)
            ]
        ]
        builder.build
    }

    /**
     * Returns a symmetric {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random symmetric {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#symmetric
     */
    def nextSymmetricDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                val entry = nextDecimal(bound, scale)
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, entry)
                    builder.put(columnIndex, rowIndex, entry)
                } else
                    builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    /**
     * Returns a skew-symmetric {@link DecimalMatrix}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix}
     * @return A pseudo random skew-symmetric {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see DecimalMatrix#skewSymmetric
     */
    def nextSkewSymmetricDecimalMatrix(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalMatrix::builder(size, size)
        (1 .. size).forEach [ rowIndex |
            (1 .. size).forEach [ columnIndex |
                val entry = nextDecimal(bound, scale)
                if (rowIndex < columnIndex) {
                    builder.put(rowIndex, columnIndex, entry)
                    builder.put(columnIndex, rowIndex, -entry)
                } else
                    builder.put(rowIndex, columnIndex, entry)
            ]
        ]
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param rowSize the row size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param columnSize the column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDecimalMatrix
     */
    def nextDecimalMatrices(long bound, int scale, int rowSize, int columnSize, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextDecimalMatrix(bound, scale, rowSize, columnSize)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing upper triangular 
     * {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random upper triangular {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextUpperTriangularDecimalMatrix
     */
    def nextUpperTriangularDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextUpperTriangularDecimalMatrix(bound, scale, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing lower triangular 
     * {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random lower triangular {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextLowerTriangularDecimalMatrix
     */
    def nextLowerTriangularDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextLowerTriangularDecimalMatrix(bound, scale, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing triangular {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random triangular {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextTriangularDecimalMatrix
     */
    def nextTriangularDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextTriangularDecimalMatrix(bound, scale, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing diagonal {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random diagonal {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextDiagonalDecimalMatrix
     */
    def nextDiagonalDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextDiagonalDecimalMatrix(bound, scale, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing symmetric {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random symmetric {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSymmetricDecimalMatrix
     */
    def nextSymmetricDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextSymmetricDecimalMatrix(bound, scale, size)
        matrices
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing skew-symmetric 
     * {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound the bound
     * @param scale the scale
     * @param size the row and column size of the resulting {@link DecimalMatrix DecimalMatrices}
     * @param howMany the size of the resulting {@link List}
     * @return A {@link List} of pseudo random skew-symmetric {@link DecimalMatrix DecimalMatrices}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @since 1
     * @author Lars Tennstedt
     * @see #nextSkewSymmetricDecimalMatrix
     */
    def nextSkewSymmetricDecimalMatrices(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (var i = 0; i < howMany; i++)
            matrices += nextSkewSymmetricDecimalMatrix(bound, scale, size)
        matrices
    }
}
