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
import com.google.common.annotations.VisibleForTesting
import com.google.common.math.BigIntegerMath
import finnmath.number.ScientificNotation
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import org.slf4j.LoggerFactory

import static com.google.common.base.Preconditions.checkArgument
import static java.lang.Math.addExact
import static java.util.Objects.requireNonNull

/**
 * An implementation for calculating square roots of {@link BigInteger BigIntegers} and 
 * {@link BigDecimal BigDecimals}
 * 
 * @since 1
 * @author Lars Tennstedt
 */
@Beta
final class SquareRootCalculator {
    /**
     * Default precision
     */
    public static val BigDecimal DEFAULT_PRECISION = 0.0000000001BD

    /**
     * Default scale
     */
    public static val int DEFAULT_SCALE = 10

    /**
     * Default rounding mode
     */
    public static val int DEFAULT_ROUNDING_MODE = BigDecimal::ROUND_HALF_UP

    static val log = LoggerFactory::getLogger(SquareRootCalculator)

    private new() {
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     * 
     * @param integer the integer whose square root is to be calculated
     * @return The square root of the given integer
     * @throws NullPointerException if {@code integer == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #sqrt(BigDecimal, BigDecimal, int, int)
     */
    static def sqrt(BigInteger integer) {
        requireNonNull(integer, 'integer')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        sqrt(new BigDecimal(integer), DEFAULT_PRECISION, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     * 
     * @param decimal the decimal number whose square root is to be calculated
     * @return The square root of the given decimal
     * @throws NullPointerException if {@code decimal == null}
     * @throws IllegalArgumentException if {@code decimal < 0}
     * @since 1
     * @author Lars Tennstedt
     * @see #heronsMethod
     */
    static def sqrt(BigDecimal decimal) {
        requireNonNull(decimal, 'decimal')
        checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
        heronsMethod(decimal, DEFAULT_PRECISION).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     * 
     * @param integer the integer whose square root is to be calculated
     * @param precision the precision for the termination condition
     * @return The square root of the given integer
     * @throws NullPointerException if {@code integer == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #sqrt(BigDecimal, BigDecimal, int, int)
     */
    static def sqrt(BigInteger integer, BigDecimal precision) {
        requireNonNull(integer, 'integer')
        requireNonNull(precision, 'precision')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
        sqrt(new BigDecimal(integer), precision, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     * 
     * @param decimal the decimal number whose square root is to be calculated
     * @param precision the precision for the termination condition
     * @return The square root of the given decimal
     * @throws NullPointerException if {@code decimal == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code decimal < 0}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @since 1
     * @author Lars Tennstedt
     * @see #heronsMethod
     */
    static def sqrt(BigDecimal decimal, BigDecimal precision) {
        requireNonNull(decimal, 'decimal')
        requireNonNull(precision, 'precision')
        checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
        heronsMethod(decimal, precision).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING_MODE)
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     * 
     * @param integer the integer whose square root is to be calculated
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result 
     * @return The square root of the given integer
     * @throws NullPointerException if {@code integer == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #sqrt(BigDecimal, BigDecimal, int, int)
     * @see BigDecimal#setScale(int, int)
     */
    static def sqrt(BigInteger integer, int scale, int roundingMode) {
        requireNonNull(integer, 'integer')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s',
            roundingMode)
        sqrt(new BigDecimal(integer), DEFAULT_PRECISION, scale, roundingMode)
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     * 
     * @param decimal the decimal number whose square root is to be calculated
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The square root of the given decimal
     * @throws NullPointerException if {@code decimal == null}
     * @throws IllegalArgumentException if {@code decimal < 0}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #heronsMethod
     * @see BigDecimal#setScale(int, int)
     */
    static def sqrt(BigDecimal decimal, int scale, int roundingMode) {
        requireNonNull(decimal, 'decimal')
        checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s',
            roundingMode)
        heronsMethod(decimal, DEFAULT_PRECISION).setScale(scale, roundingMode)
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     * 
     * @param integer the integer whose square root is to be calculated
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The square root of the given integer
     * @throws NullPointerException if {@code integer == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #sqrt(BigDecimal, BigDecimal, int, int)
     * @see BigDecimal#setScale(int, int)
     */
    static def sqrt(BigInteger integer, BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(integer, 'integer')
        requireNonNull(precision, 'precision')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s',
            roundingMode)
        sqrt(new BigDecimal(integer), precision, scale, roundingMode)
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     * 
     * @param decimal the decimal number whose square root is to be calculated
     * @param precision the precision for the termination condition
     * @param scale the scale to be set on the result
     * @param roundingMode the rounding mode to be used during the setting of the scale of the result
     * @return The square root of the given decimal
     * @throws NullPointerException if {@code decimal == null}
     * @throws NullPointerException if {@code precision == null}
     * @throws IllegalArgumentException if {@code decimal < 0}
     * @throws IllegalArgumentException if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException if {@code scale < 0}
     * @throws IllegalArgumentException if {@code roundingMode < 0 || 7 < roundingMode}
     * @since 1
     * @author Lars Tennstedt
     * @see #heronsMethod
     * @see BigDecimal#setScale(int, int)
     */
    static def sqrt(BigDecimal decimal, BigDecimal precision, int scale, int roundingMode) {
        requireNonNull(decimal, 'decimal')
        requireNonNull(precision, 'precision')
        checkArgument(decimal >= 0BD, 'expected decimal >= 0 but actual %s', decimal)
        checkArgument(0BD < precision && precision < 1BD, 'expected precision in (0, 1) but actual %s', precision)
        checkArgument(scale >= 0, 'expected scale >= 0 but actual %s', scale)
        checkArgument(0 <= roundingMode && roundingMode <= 7, 'expected roundingMode in [0, 7] but actual %s',
            roundingMode)
        heronsMethod(decimal, precision).setScale(scale, roundingMode)
    }

    /**
     * Returns the square root of the given {@link BigInteger} which has to be a perfect square
     * 
     * @param integer the perfect square whose square root is to be calculated
     * @return The square root of the given perfect square
     * @throws NullPointerException if {@code integer == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @throws IllegalArgumentException if {@code !perfectSquare}
     * @see #perfectSquare
     * @see BigIntegerMath#sqrt
     * @since 1
     * @author Lars Tennstedt
     */
    static def sqrtOfPerfectSquare(BigInteger integer) {
        requireNonNull(integer, 'integer')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        checkArgument(perfectSquare(integer), 'expected perfect square but actual %s', integer)
        BigIntegerMath::sqrt(integer, RoundingMode.UNNECESSARY)
    }

    /**
     * Returns if the given {@link BigInteger} is a perfect square
     * 
     * @param integer the integer which should be checked
     * @return {@code true} if the integer is a perfect square, {@code false} otherwise
     * @throws NullPointerException if {@code integer == null}
     * @throws IllegalArgumentException if {@code integer < 0}
     * @since 1
     * @author Lars Tennstedt
     */
    static def perfectSquare(BigInteger integer) {
        requireNonNull(integer, 'integer')
        checkArgument(integer >= 0BI, 'expected integer >= 0 but actual %s', integer)
        var sum = 0BI
        for (var odd = 1BI; sum < integer; odd += 2BI)
            sum += odd
        sum == integer
    }

    private static def heronsMethod(BigDecimal decimal, BigDecimal precision) {
        log.debug('calculating square root for {} with precision = {}', decimal.toPlainString, precision.toPlainString)
        var predecessor = seedValue(decimal)
        log.debug('seed value = {}', predecessor.toPlainString)
        var successor = calculateSuccessor(predecessor, decimal)
        var iterations = 1
        while ((successor - predecessor).abs >= precision) {
            log.debug('|successor - predecessor| = {}', (successor - predecessor).abs.toPlainString)
            predecessor = successor
            successor = calculateSuccessor(successor, decimal)
            iterations++
        }
        log.debug('terminated after {} iterations', iterations)
        log.debug('sqrt({}) = {}', decimal.toPlainString, successor.toPlainString)
        successor
    }

    private static def calculateSuccessor(BigDecimal predecessor, BigDecimal decimal) {
        log.debug('iteration')
        log.debug('predecessor = {}', predecessor.toPlainString)
        val successor = (predecessor ** 2 + decimal) / (2BD * predecessor)
        log.debug('successor = {}', successor.toPlainString)
        successor
    }

    private static def seedValue(BigDecimal decimal) {
        val it = scientificNotationForSqrt(decimal)
        log.debug('Scientific notation of {} is {}.', decimal.toPlainString, asString)
        if (coefficient >= 10BD)
            return 6BD * 10BD ** (exponent / 2)
        2BD * 10BD ** (exponent / 2)
    }

    @VisibleForTesting
    package static def scientificNotationForSqrt(BigDecimal decimal) {
        log.debug('calculating scientific notification for {}', decimal.toPlainString)
        var coefficient = decimal
        var exponent = 0
        log.debug('coefficient = {}', coefficient.toPlainString)
        log.debug('exponent = {}', exponent)
        while (coefficient >= 100BD) {
            log.debug('iteration for scientific notification')
            coefficient /= 100BD
            exponent = addExact(exponent, 2)
            log.debug('coefficient = {}', coefficient.toPlainString)
            log.debug('exponent = {}', exponent)
        }
        new ScientificNotation(coefficient, exponent)
    }
}
