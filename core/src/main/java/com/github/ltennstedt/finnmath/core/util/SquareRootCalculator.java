/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ltennstedt.finnmath.core.util;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.addExact;
import static java.util.Objects.requireNonNull;

import com.github.ltennstedt.finnmath.core.number.ScientificNotation;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation for calculating square roots of {@link BigInteger BigIntegers} and {@link BigDecimal BigDecimals}
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SquareRootCalculator {
    /**
     * Default precision
     *
     * @since 1
     */
    public static final BigDecimal DEFAULT_PRECISION = BigDecimal.valueOf(0.0000000001);

    /**
     * Default scale
     *
     * @since 1
     */
    public static final int DEFAULT_SCALE = 10;

    /**
     * Default rounding mode
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private static Logger log = LoggerFactory.getLogger(SquareRootCalculator.class);

    /**
     * Precision
     *
     * @since 1
     */
    private final BigDecimal precision;

    /**
     * Scale
     *
     * @since 1
     */
    private final int scale;

    /**
     * Rounding mode
     *
     * @since 1
     */
    private final RoundingMode roundingMode;

    /**
     * Constructs a {@link SquareRootCalculator} from defaults only
     *
     * @since 1
     */
    public SquareRootCalculator() {
        precision = DEFAULT_PRECISION;
        scale = DEFAULT_SCALE;
        roundingMode = DEFAULT_ROUNDING_MODE;
    }

    /**
     * Constructs a {@link SquareRootCalculator} from a given precision
     *
     * @param precision
     *         the precision for the termination condition
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @since 1
     */
    public SquareRootCalculator(final BigDecimal precision) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        this.precision = precision;
        scale = DEFAULT_SCALE;
        roundingMode = DEFAULT_ROUNDING_MODE;
    }

    /**
     * Constructs a {@link SquareRootCalculator} from a given scale and rounding mode
     *
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @since 1
     */
    public SquareRootCalculator(final int scale, final RoundingMode roundingMode) {
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        precision = DEFAULT_PRECISION;
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    /**
     * Constructs a {@link SquareRootCalculator} from a given precision, scale and rounding mode
     *
     * @param precision
     *         the precision for the termination condition
     * @param scale
     *         the scale to be set on the result
     * @param roundingMode
     *         the rounding mode to be used during the setting of the scale of the result
     * @throws NullPointerException
     *         if {@code precision == null}
     * @throws IllegalArgumentException
     *         if {@code precision <= 0 || 1 <= precision}
     * @throws IllegalArgumentException
     *         if {@code scale < 0}
     * @since 1
     */
    public SquareRootCalculator(final BigDecimal precision, final int scale, final RoundingMode roundingMode) {
        requireNonNull(precision, "precision");
        checkArgument((BigDecimal.ZERO.compareTo(precision) < 0) && (precision.compareTo(BigDecimal.ONE) < 0),
                "expected precision in (0, 1) but actual %s", precision);
        checkArgument(scale >= 0, "expected scale >= 0 but actual %s", scale);
        this.precision = precision;
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *         the integer whose square root is to be calculated
     * @return The square root of the given integer
     * @throws NullPointerException
     *         if {@code integer == null}
     * @throws IllegalArgumentException
     *         if {@code integer < 0}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public BigDecimal sqrt(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer));
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *         the decimal number whose square root is to be calculated
     * @return The square root of the given decimal
     * @throws NullPointerException
     *         if {@code decimal == null}
     * @throws IllegalArgumentException
     *         if {@code decimal < 0}
     * @see #heronsMethod
     * @since 1
     */
    public BigDecimal sqrt(final BigDecimal decimal) {
        requireNonNull(decimal, "decimal");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        return heronsMethod(decimal).setScale(scale, roundingMode);
    }

    /**
     * Returns the square root of the given {@link BigInteger} which has to be a perfect square
     *
     * @param integer
     *         the perfect square whose square root is to be calculated
     * @return The square root of the given perfect square
     * @throws NullPointerException
     *         if {@code integer == null}
     * @throws IllegalArgumentException
     *         if {@code integer < 0}
     * @throws IllegalArgumentException
     *         if {@code !perfectSquare}
     * @see #perfectSquare
     * @see BigIntegerMath#sqrt
     * @since 1
     */
    public BigInteger sqrtOfPerfectSquare(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        checkArgument(perfectSquare(integer), "expected perfect square but actual %s", integer);
        return BigIntegerMath.sqrt(integer, RoundingMode.UNNECESSARY);
    }

    /**
     * Returns if the given {@link BigInteger} is a perfect square
     *
     * @param integer
     *         the integer which should be checked
     * @return {@code true} if the integer is a perfect square, {@code false} otherwise
     * @throws NullPointerException
     *         if {@code integer == null}
     * @throws IllegalArgumentException
     *         if {@code integer < 0}
     * @since 1
     */
    public boolean perfectSquare(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger odd = BigInteger.ONE; sum.compareTo(integer) < 0; odd = odd.add(BigInteger.valueOf(2))) {
            sum = sum.add(odd);
        }
        return sum.equals(integer);
    }

    private BigDecimal heronsMethod(final BigDecimal decimal) {
        log.debug("calculating square root for {} with precision = {}", decimal.toPlainString(),
                precision.toPlainString());
        BigDecimal predecessor = seedValue(decimal);
        log.debug("seed value = {}", predecessor.toPlainString());
        BigDecimal successor = calculateSuccessor(predecessor, decimal);
        long iterations = 1;
        while (successor.subtract(predecessor).abs().compareTo(precision) > -1) {
            log.debug("|successor - predecessor| = {}", successor.subtract(predecessor).abs().toPlainString());
            predecessor = successor;
            successor = calculateSuccessor(successor, decimal);
            iterations++;
        }
        log.debug("terminated after {} iterations", iterations);
        log.debug("sqrt({}) = {}", decimal.toPlainString(), successor.toPlainString());
        return successor;
    }

    private BigDecimal calculateSuccessor(final BigDecimal predecessor, final BigDecimal decimal) {
        log.debug("iteration");
        log.debug("predecessor = {}", predecessor.toPlainString());
        final BigDecimal divisor = BigDecimal.valueOf(2).multiply(predecessor);
        final BigDecimal successor = divisor.compareTo(BigDecimal.ZERO) != 0 ?
                predecessor.pow(2).add(decimal).divide(divisor, scale, roundingMode) : DEFAULT_PRECISION;
        log.debug("successor = {}", successor.toPlainString());
        return successor;
    }

    private BigDecimal seedValue(final BigDecimal decimal) {
        final ScientificNotation scientificNotation = scientificNotationForSqrt(decimal);
        log.debug("Scientific notation of {} is {}.", decimal.toPlainString(), scientificNotation.asString());
        if (scientificNotation.getCoefficient().compareTo(BigDecimal.TEN) > -1) {
            return BigDecimal.valueOf(6).multiply(BigDecimal.TEN.pow(scientificNotation.getExponent() / 2));
        }
        return BigDecimal.valueOf(2).multiply(BigDecimal.TEN.pow(scientificNotation.getExponent() / 2));
    }

    @VisibleForTesting
    ScientificNotation scientificNotationForSqrt(final BigDecimal decimal) {
        log.debug("calculating scientific notification for {}", decimal.toPlainString());
        BigDecimal coefficient = decimal;
        int exponent = 0;
        log.debug("coefficient = {}", coefficient.toPlainString());
        log.debug("exponent = {}", exponent);
        while (coefficient.compareTo(BigDecimal.valueOf(100)) > -1) {
            log.debug("iteration for scientific notification");
            coefficient = coefficient.divide(BigDecimal.valueOf(100), roundingMode);
            exponent = addExact(exponent, 2);
            log.debug("coefficient = {}", coefficient.toPlainString());
            log.debug("exponent = {}", exponent);
        }
        return new ScientificNotation(coefficient, exponent);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("precision", precision).add("scale", scale)
                .add("roundingMode", roundingMode).toString();
    }

    public BigDecimal getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }
}
