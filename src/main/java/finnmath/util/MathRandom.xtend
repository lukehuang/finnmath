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
     * @param bound {@code long}
     * @return long {@code long}
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
     * @param bound {@code long}
     * @return long {@code long}
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
     * @param bound {@code long}
     * @return long {@code long}
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
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return long array {@code long[]}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextPositiveLong
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (i : 0 ..< howMany)
            ints.set(i, nextPositiveLong(bound))
        ints
    }

    /**
     * Returns an array of the length of {@code howMany} containing negative {@code long longs}
     * 
     * @param bound  {@code long}
     * @param howMany  {@code int}
     * @return long array  {@code long[]}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextNegativeLong
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (i : 0 ..< howMany)
            ints.set(i, nextNegativeLong(bound))
        ints
    }

    /**
     * Returns an array of the length of {@code howMany} containing {@code long longs}
     * 
     * @param bound {@code long}
     * @param howMany  {@code int}
     * @return long array  {@code long[]}
     * @throws IllegalArgumentException if {@code  bound < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
     */
    def nextLongs(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val ints = newLongArrayOfSize(howMany)
        for (i : 0 ..< howMany)
            ints.set(i, nextLong(bound))
        ints
    }

    /**
     * Returns a positive {@link BigDecimal} of a given {@code scale} bounded below by {@code 0} (inclusive) and above  
     * by {@code bound} (exclusive)
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @see #nextPositiveDecimal
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @see #nextNegativeDecimal
     * @see #nextInvertibleDecimal
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return BigDecimal {@link BigDecimal}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextPositiveDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextPositiveDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link BigDecimal BigDecimals}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextNegativeDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextNegativeDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigDecimal BigDecimals}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing positive {@link BigDecimal BigDecimals} which 
     * are invertible
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertiblePositiveDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertiblePositiveDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextInvertiblePositiveDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link BigDecimal BigDecimals} which 
     * are invertible
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleNegativeDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleNegativeDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextInvertibleNegativeDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@code List} of the size of {@code howMany} containing {@link BigDecimal BigDecimals} which are 
     * invertible
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleDecimals(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<BigDecimal> decimals = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            decimals += nextInvertibleDecimal(bound, scale)
        decimals
    }

    /**
     * Returns a {@link Fraction} whose {@code numerator} is bounded below by {@code 0} (inclusive) and above by 
     * {@code bound} (exclusive) and whose {@code denominator} is bounded below {@code 1} (inclusive) and {@code bound} 
     * (exclusive)
     * 
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
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
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
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
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
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
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextPositiveFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextPositiveFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (i : 0 ..< howMany)
            fractions += nextPositiveFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link Fraction Fractions}
     * 
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextNegativeFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextNegativeFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<Fraction> fractions = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            fractions += nextNegativeFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link Fraction Fractions}
     * 
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val List<Fraction> fractions = new ArrayList(howMany)
        for (i : 0 ..< howMany)
            fractions += nextFraction(bound)
        fractions
    }

    /**
     * Returns a positive {@link Fraction} which is invertible
     * 
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @see #nextPositiveFraction
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @see #nextNegativeFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleNegativeFraction(long bound) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        nextInvertiblePositiveFraction(bound).negate
    }

    /**
     * Returns a {@link Fraction} which is invertible
     * 
     * @param bound {@code long}
     * @return Fraction {@link Fraction}
     * @throws IllegalArgumentException if {@code bound < 2}
     * @see #nextFraction
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertiblePositiveFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertiblePositiveFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (i : 0 ..< howMany)
            fractions += nextInvertiblePositiveFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing negative {@link Fraction Fractions} which are 
     * invertible
     * 
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleNegativeFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleNegativeFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (i : 0 ..< howMany)
            fractions += nextInvertibleNegativeFraction(bound)
        fractions
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link Fraction Fractions} which are 
     * invertible
     * 
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleFraction
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleFractions(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val fractions = new ArrayList<Fraction>(howMany) as List<Fraction>
        for (i : 0 ..< howMany)
            fractions += nextInvertibleFraction(bound)
        fractions
    }

    /**
     * Returns a {@link SimpleComplexNumber} whose {@code real} and {@code imaginary} part are bounded below by 
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound {@code long}
     * @return SimpleComplexNumber {@link SimpleComplexNumber}
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
     * @param bound {@code long}
     * @return SimpleComplexNumber {@link SimpleComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @see #nextSimpleComplexNumber
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextSimpleComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    def nextSimpleComplexNumbers(long bound, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
        for (i : 0 ..< howMany)
            complexNumbers += nextSimpleComplexNumber(bound)
        complexNumbers
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing 
     * {@link SimpleComplexNumber SimpleComplexNumbers} which are invertible
     * 
     * @param bound {@code long}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleSimpleComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleSimpleComplexNumbers(long bound, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<SimpleComplexNumber>(howMany) as List<SimpleComplexNumber>
        for (i : 0 ..< howMany)
            complexNumbers += nextInvertibleSimpleComplexNumber(bound)
        complexNumbers
    }

    /**
     * Returns a {@link RealComplexNumber} whose {@code real} and {@code imaginary} part are bounded below by 
     * {@code -bound} (exclusive) and above by {@code bound} (exclusive)
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @return RealComplexNumber {@link RealComplexNumber}
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @return RealComplexNumber {@link RealComplexNumber}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @see #nextRealComplexNumber
     * @since 1
     * @author Lars Tennstedt
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
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextRealComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    def nextRealComplexNumbers(long bound, int scale, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
        for (i : 0 ..< howMany)
            complexNumbers += nextRealComplexNumber(bound, scale)
        complexNumbers
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link RealComplexNumber RealComplexNumbers} 
     * which are invertible
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextInvertibleRealComplexNumber
     * @since 1
     * @author Lars Tennstedt
     */
    def nextInvertibleRealComplexNumbers(long bound, int scale, int howMany) {
        checkArgument(bound > 1, 'expected bound > 1 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val complexNumbers = new ArrayList<RealComplexNumber>(howMany) as List<RealComplexNumber>
        for (i : 0 ..< howMany)
            complexNumbers += nextInvertibleRealComplexNumber(bound, scale)
        complexNumbers
    }

    /**
     * Returns a {@link BigIntVector}
     * 
     * @param bound {@code long}
     * @param size {@code int}
     * @return BigIntVector {@link BigIntVector} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
     */
    def nextBigIntVector(long bound, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = BigIntVector::builder(size)
        for (index : 1 .. size)
            builder.put(BigInteger::valueOf(nextLong(bound)))
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigIntVector BigIntVectors}
     * 
     * @param bound {@code long}
     * @param size {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code  bound < 2}
     * @throws IllegalArgumentException if {@code howMany < 2}
     * @see #nextBigIntVector
     * @since 1
     * @author Lars Tennstedt
     */
    def nextBigIntVectors(long bound, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val vectors = new ArrayList<BigIntVector>(howMany) as List<BigIntVector>
        for (i : 1 .. howMany)
            vectors += nextBigIntVector(bound, size)
        vectors
    }

    /**
     * Returns a {@link BigIntMatrix}
     * 
     * @param bound {@code long}
     * @param rowSize {@code int}
     * @param columnSize {@code int}
     * @return BigIntMatrix {@link BigIntMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @see #nextLong
     * @since 1
     * @author Lars Tennstedt
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
     * Returns a {@link List} of the size of {@code howMany} containing {@link BigIntMatrix BigIntMatrices}
     * 
     * @param bound {@code long}
     * @param rowSize {@code int}
     * @param columnSize {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @see #nextBigIntMatrix
     * @since 1
     * @author Lars Tennstedt
     */
    def nextBigIntMatrices(long bound, int rowSize, int columnSize, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<BigIntMatrix>(howMany) as List<BigIntMatrix>
        for (i : 1 .. howMany)
            matrices += nextBigIntMatrix(bound, rowSize, columnSize)
        matrices
    }

    /**
     * Returns a {@link DecimalVector}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param size {@code int}
     * @return DecimalVector {@link DecimalVector} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code size < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
     */
    def nextDecimalVector(long bound, int scale, int size) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        val builder = DecimalVector::builder(size)
        for (index : 1 .. size)
            builder.put(nextDecimal(bound, scale))
        builder.build
    }

    /**
     * Returns a {@link List} of the size of {@code howMany} containing {@link DecimalVector DecimalVectors}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param size {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @see #nextDecimalVector
     * @since 1
     * @author Lars Tennstedt
     */
    def nextDecimalVectors(long bound, int scale, int size, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(size > 0, 'expected size > 0 but actual %s', size)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val vectors = new ArrayList<DecimalVector>(howMany) as List<DecimalVector>
        for (i : 1 .. howMany)
            vectors += nextDecimalVector(bound, scale, size)
        vectors
    }

    /**
     * Returns a {@link DecimalMatrix}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param rowSize {@code int}
     * @param columnSize {@code int}
     * @return DecimalMatrix {@link DecimalMatrix} 
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @see #nextDecimal
     * @since 1
     * @author Lars Tennstedt
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
     * Returns a {@link List} of the size of {@code howMany} containing {@link DecimalMatrix DecimalMatrices}
     * 
     * @param bound {@code long}
     * @param scale {@code int}
     * @param rowSize {@code int}
     * @param columnSize {@code int}
     * @param howMany {@code int}
     * @return List {@link List}
     * @throws IllegalArgumentException if {@code bound < 1}
     * @throws IllegalArgumentException if {@code scale < 1}
     * @throws IllegalArgumentException if {@code rowSize < 1}
     * @throws IllegalArgumentException if {@code columnSize < 1}
     * @throws IllegalArgumentException if {@code howMany < 1}
     * @see #nextDecimalMatrix
     * @since 1
     * @author Lars Tennstedt
     */
    def nextDecimalMatrices(long bound, int scale, int rowSize, int columnSize, int howMany) {
        checkArgument(bound > 0, 'expected bound > 0 but actual %s', bound)
        checkArgument(scale > 0, 'expected scale > 0 but actual %s', scale)
        checkArgument(rowSize > 0, 'expected rowSize > 0 but actual %s', rowSize)
        checkArgument(columnSize > 0, 'expected columnSize > 0 but actual %s', columnSize)
        checkArgument(howMany > 1, 'expected howMany > 1 but actual %s', howMany)
        val matrices = new ArrayList<DecimalMatrix>(howMany) as List<DecimalMatrix>
        for (i : 1 .. howMany)
            matrices += nextDecimalMatrix(bound, scale, rowSize, columnSize)
        matrices
    }
}
