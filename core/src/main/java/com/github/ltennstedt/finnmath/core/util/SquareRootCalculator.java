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
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.github.ltennstedt.finnmath.core.number.ScientificNotation;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.math.BigIntegerMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation for calculating square roots of {@link BigInteger
 * BigIntegers} and {@link BigDecimal BigDecimals}
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SquareRootCalculator {
    /**
     * Default abort criterion
     *
     * @since 1
     */
    public static final BigDecimal DEFAULT_ABORT_CRITERION = BigDecimal.valueOf(0.0000000001);

    /**
     * Default rounding mode
     *
     * @since 1
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private static final Logger log = LoggerFactory.getLogger(SquareRootCalculator.class);

    private SquareRootCalculator() {
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer), DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final BigDecimal abortCriterion) {
        requireNonNull(integer, "integer");
        requireNonNull(abortCriterion, "abortCriterion");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return sqrt(new BigDecimal(integer), abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @param roundingMode
     *            rounding mode
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final RoundingMode roundingMode) {
        requireNonNull(integer, "integer");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer), DEFAULT_ABORT_CRITERION, roundingMode);
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @param roundingMode
     *            rounding mode
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final BigDecimal abortCriterion,
        final RoundingMode roundingMode) {
        requireNonNull(integer, "integer");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return sqrt(new BigDecimal(integer), abortCriterion, roundingMode);
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @param mathContext
     *            math context
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final MathContext mathContext) {
        requireNonNull(integer, "integer");
        requireNonNull(mathContext, "mathContext");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer), DEFAULT_ABORT_CRITERION, mathContext);
    }

    /**
     * Returns the square root of the given {@link BigInteger}
     *
     * @param integer
     *            the integer whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @param mathContext
     *            math context
     * @return The square root of the given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #sqrt(BigDecimal)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final BigDecimal abortCriterion,
        final MathContext mathContext) {
        requireNonNull(integer, "integer");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(mathContext, "mathContext");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        return sqrt(new BigDecimal(integer), abortCriterion, mathContext);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal) {
        requireNonNull(decimal, "decimal");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        final BigDecimal scaled = decimal.scale() < DEFAULT_ABORT_CRITERION.scale()
            ? decimal.setScale(DEFAULT_ABORT_CRITERION.scale(), DEFAULT_ROUNDING_MODE) : decimal;
        return heronsMethod(scaled, DEFAULT_ABORT_CRITERION, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final BigDecimal abortCriterion) {
        requireNonNull(decimal, "decimal");
        requireNonNull(abortCriterion, "abortCriterion");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        final BigDecimal scaled = decimal.scale() < abortCriterion.scale()
            ? decimal.setScale(abortCriterion.scale(), DEFAULT_ROUNDING_MODE) : decimal;
        return heronsMethod(scaled, abortCriterion, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @param roundingMode
     *            rounding mode
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final RoundingMode roundingMode) {
        requireNonNull(decimal, "decimal");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        final BigDecimal scaled = decimal.scale() < DEFAULT_ABORT_CRITERION.scale()
            ? decimal.setScale(DEFAULT_ABORT_CRITERION.scale(), roundingMode) : decimal;
        return heronsMethod(scaled, DEFAULT_ABORT_CRITERION, roundingMode);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @param roundingMode
     *            rounding mode
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code roundingMode == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final BigDecimal abortCriterion,
        final RoundingMode roundingMode) {
        requireNonNull(decimal, "decimal");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(roundingMode, "roundingMode");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        final BigDecimal scaled =
            decimal.scale() < abortCriterion.scale() ? decimal.setScale(abortCriterion.scale(), roundingMode) : decimal;
        return heronsMethod(scaled, abortCriterion, roundingMode);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @param mathContext
     *            math context
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final MathContext mathContext) {
        requireNonNull(decimal, "decimal");
        requireNonNull(mathContext, "mathContext");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        final BigDecimal scaled = decimal.scale() < DEFAULT_ABORT_CRITERION.scale()
            ? decimal.setScale(DEFAULT_ABORT_CRITERION.scale(), mathContext.getRoundingMode()) : decimal;
        return heronsMethod(scaled, DEFAULT_ABORT_CRITERION, mathContext);
    }

    /**
     * Returns the square root of the given {@link BigDecimal}
     *
     * @param decimal
     *            the decimal number whose square root is to be calculated
     * @param abortCriterion
     *            abort criterion
     * @param mathContext
     *            math context
     * @return The square root of the given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code abortCriterion == null}
     * @throws NullPointerException
     *             if {@code mathContext == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @throws IllegalArgumentException
     *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
     * @see #heronsMethod
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final BigDecimal abortCriterion,
        final MathContext mathContext) {
        requireNonNull(decimal, "decimal");
        requireNonNull(abortCriterion, "abortCriterion");
        requireNonNull(mathContext, "mathContext");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
        final BigDecimal scaled = decimal.scale() < abortCriterion.scale()
            ? decimal.setScale(abortCriterion.scale(), mathContext.getRoundingMode()) : decimal;
        return heronsMethod(scaled, abortCriterion, mathContext);
    }

    /**
     * Returns the square root of the given {@link BigInteger} which has to be a
     * perfect square
     *
     * @param integer
     *            the perfect square whose square root is to be calculated
     * @return The square root of the given perfect square
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @throws IllegalArgumentException
     *             if {@code !perfectSquare}
     * @see #perfectSquare
     * @see BigIntegerMath#sqrt
     * @since 1
     */
    public static BigInteger sqrtOfPerfectSquare(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        checkArgument(perfectSquare(integer), "expected perfect square but actual %s", integer);
        return BigIntegerMath.sqrt(integer, RoundingMode.UNNECESSARY);
    }

    /**
     * Returns if the given {@link BigInteger} is a perfect square
     *
     * @param integer
     *            the integer which should be checked
     * @return {@code true} if the integer is a perfect square, {@code false}
     *         otherwise
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @since 1
     */
    public static boolean perfectSquare(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger odd = BigInteger.ONE; sum.compareTo(integer) < 0; odd = odd.add(BigInteger.valueOf(2))) {
            sum = sum.add(odd);
        }
        return sum.equals(integer);
    }

    private static BigDecimal heronsMethod(final BigDecimal decimal, final BigDecimal abortCriterion,
        final RoundingMode roundingMode) {
        assert decimal != null;
        assert abortCriterion != null;
        assert roundingMode != null;
        log.debug("calculating square root for {} with precision = {}", decimal.toPlainString(),
            abortCriterion.toPlainString());
        BigDecimal predecessor = decimal.add(BigDecimal.ONE).divide(BigDecimal.valueOf(2), roundingMode);
        log.debug("seed value = {}", predecessor.toPlainString());
        BigDecimal successor = calculateSuccessor(predecessor, decimal, abortCriterion, roundingMode);
        long iterations = 1;
        while (successor.subtract(predecessor).abs().compareTo(abortCriterion) > 0) {
            log.debug("|successor - predecessor| = {}", successor.subtract(predecessor).abs().toPlainString());
            predecessor = successor;
            successor = calculateSuccessor(successor, decimal, abortCriterion, roundingMode);
            iterations++;
        }
        log.debug("terminated after {} iterations", iterations);
        log.debug("sqrt({}) = {}", decimal.toPlainString(), successor.toPlainString());
        return successor;
    }

    private static BigDecimal heronsMethod(final BigDecimal decimal, final BigDecimal abortCriterion,
        final MathContext mathContext) {
        assert decimal != null;
        assert abortCriterion != null;
        assert mathContext != null;
        log.debug("calculating square root for {} with precision = {}", decimal.toPlainString(),
            abortCriterion.toPlainString());
        BigDecimal predecessor = decimal.add(BigDecimal.ONE, mathContext).divide(BigDecimal.valueOf(2), mathContext);
        log.debug("seed value = {}", predecessor.toPlainString());
        BigDecimal successor = calculateSuccessor(predecessor, decimal, abortCriterion, mathContext);
        long iterations = 1;
        while (successor.subtract(predecessor).abs().compareTo(abortCriterion) > 0) {
            log.debug("|successor - predecessor| = {}", successor.subtract(predecessor).abs().toPlainString());
            predecessor = successor;
            successor = calculateSuccessor(successor, decimal, abortCriterion, mathContext);
            iterations++;
        }
        log.debug("terminated after {} iterations", iterations);
        log.debug("sqrt({}) = {}", decimal.toPlainString(), successor.toPlainString());
        return successor;
    }

    private static BigDecimal calculateSuccessor(final BigDecimal predecessor, final BigDecimal decimal,
        final BigDecimal abortCriterion, final RoundingMode roundingMode) {
        assert predecessor != null;
        assert decimal != null;
        assert abortCriterion != null;
        assert roundingMode != null;
        log.debug("iteration");
        log.debug("predecessor = {}", predecessor.toPlainString());
        final BigDecimal divisor = BigDecimal.valueOf(2).multiply(predecessor);
        final BigDecimal successor = divisor.compareTo(BigDecimal.ZERO) != 0
            ? predecessor.pow(2).add(decimal).divide(divisor, roundingMode) : abortCriterion;
        log.debug("successor = {}", successor.toPlainString());
        return successor.setScale(abortCriterion.scale(), roundingMode);
    }

    private static BigDecimal calculateSuccessor(final BigDecimal predecessor, final BigDecimal decimal,
        final BigDecimal abortCriterion, final MathContext mathContext) {
        assert predecessor != null;
        assert decimal != null;
        assert abortCriterion != null;
        assert mathContext != null;
        log.debug("iteration");
        log.debug("predecessor = {}", predecessor.toPlainString());
        final BigDecimal divisor = BigDecimal.valueOf(2).multiply(predecessor, mathContext);
        final BigDecimal successor = divisor.compareTo(BigDecimal.ZERO) != 0
            ? predecessor.pow(2, mathContext).add(decimal, mathContext).divide(divisor, mathContext) : abortCriterion;
        log.debug("successor = {}", successor.toPlainString());
        return successor;
    }

    @VisibleForTesting
    static ScientificNotation scientificNotationForSqrt(final BigDecimal decimal, final RoundingMode roundingMode) {
        assert decimal != null;
        assert roundingMode != null;
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

    @VisibleForTesting
    static ScientificNotation scientificNotationForSqrt(final BigDecimal decimal, final MathContext mathContext) {
        assert decimal != null;
        assert mathContext != null;
        log.debug("calculating scientific notification for {}", decimal.toPlainString());
        BigDecimal coefficient = decimal;
        int exponent = 0;
        log.debug("coefficient = {}", coefficient.toPlainString());
        log.debug("exponent = {}", exponent);
        while (coefficient.compareTo(BigDecimal.valueOf(100)) > -1) {
            log.debug("iteration for scientific notification");
            coefficient = coefficient.divide(BigDecimal.valueOf(100), mathContext);
            exponent = addExact(exponent, 2);
            log.debug("coefficient = {}", coefficient.toPlainString());
            log.debug("exponent = {}", exponent);
        }
        return new ScientificNotation(coefficient, exponent);
    }
}
