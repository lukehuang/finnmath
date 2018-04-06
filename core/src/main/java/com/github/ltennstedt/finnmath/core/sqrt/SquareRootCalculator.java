/*
 * Copyright 2017 Lars Tennstedt
 *
 * Licensed under Apache License, Version 2.0 ("License");
 * you may not use this file except in compliance with License.
 * You may obtain a copy of License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See License for specific language governing permissions and
 * limitations under License.
 */

package com.github.ltennstedt.finnmath.core.sqrt;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import com.google.common.annotations.Beta;
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
     * Default {@link SquareRootContext}
     *
     * @since 1
     */
    public static final SquareRootContext DEFAULT_SQUARE_ROOT_CONTEXT = SquareRootContext.builder().build();

    private static final Logger log = LoggerFactory.getLogger(SquareRootCalculator.class);

    private SquareRootCalculator() {
    }

    /**
     * Returns square root of given {@link BigInteger}
     *
     * @param integer
     *            integer whose square root is to be calculated
     * @return square root of given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @see #sqrt(BigDecimal, SquareRootContext)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer), DEFAULT_SQUARE_ROOT_CONTEXT);
    }

    /**
     * Returns square root of given {@link BigInteger}
     *
     * @param integer
     *            integer whose square root is to be calculated
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return Square root of given integer
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @see #sqrt(BigDecimal, SquareRootContext)
     * @since 1
     */
    public static BigDecimal sqrt(final BigInteger integer, final SquareRootContext squareRootContext) {
        requireNonNull(integer, "integer");
        requireNonNull(squareRootContext, "squareRootContext");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        return sqrt(new BigDecimal(integer), squareRootContext);
    }

    /**
     * Returns square root of given {@link BigDecimal}
     *
     * @param decimal
     *            decimal number whose square root is to be calculated
     * @return square root of given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @see #heronsMethod(BigDecimal, SquareRootContext)
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal) {
        requireNonNull(decimal, "decimal");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        return heronsMethod(decimal, DEFAULT_SQUARE_ROOT_CONTEXT);
    }

    /**
     * Returns square root of given {@link BigDecimal}
     *
     * @param decimal
     *            decimal number whose square root is to be calculated
     * @param squareRootContext
     *            {@link SquareRootContext}
     * @return Square root of given decimal
     * @throws NullPointerException
     *             if {@code decimal == null}
     * @throws NullPointerException
     *             if {@code squareRootContext == null}
     * @throws IllegalArgumentException
     *             if {@code decimal < 0}
     * @see #heronsMethod(BigDecimal, SquareRootContext)
     * @since 1
     */
    public static BigDecimal sqrt(final BigDecimal decimal, final SquareRootContext squareRootContext) {
        requireNonNull(decimal, "decimal");
        requireNonNull(squareRootContext, "squareRootContext");
        checkArgument(decimal.compareTo(BigDecimal.ZERO) > -1, "expected decimal >= 0 but actual %s", decimal);
        return heronsMethod(decimal, squareRootContext);
    }

    /**
     * Returns square root of given {@link BigInteger} which has to be a perfect
     * square
     *
     * @param integer
     *            perfect square whose square root is to be calculated
     * @return square root of given perfect square
     * @throws NullPointerException
     *             if {@code integer == null}
     * @throws IllegalArgumentException
     *             if {@code integer < 0}
     * @throws IllegalArgumentException
     *             if {@code !perfectSquare}
     * @see #perfectSquare
     * @since 1
     */
    public static BigInteger sqrtOfPerfectSquare(final BigInteger integer) {
        requireNonNull(integer, "integer");
        checkArgument(integer.compareTo(BigInteger.ZERO) > -1, "expected integer >= 0 but actual %s", integer);
        checkArgument(perfectSquare(integer), "expected perfect square but actual %s", integer);
        return BigIntegerMath.sqrt(integer, RoundingMode.UNNECESSARY);
    }

    /**
     * Returns if given {@link BigInteger} is a perfect square
     *
     * @param integer
     *            integer which should be checked
     * @return {@code true} if integer is a perfect square, {@code false} otherwise
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
        return sum.compareTo(integer) == 0;
    }

    private static BigDecimal heronsMethod(final BigDecimal decimal, final SquareRootContext squareRootContext) {
        assert decimal != null;
        assert squareRootContext != null;
        final BigDecimal abortCriterion = squareRootContext.getAbortCriterion();
        log.debug("calculating square root for {} with precision = {}", decimal.toPlainString(),
            abortCriterion.toPlainString());
        final MathContext mathContext = squareRootContext.getMathContext();
        final RoundingMode roundingMode = mathContext.getRoundingMode();
        final BigDecimal scaled = decimal.setScale(squareRootContext.getInitalScale(), roundingMode);
        BigDecimal predecessor = scaled.add(BigDecimal.ONE, mathContext).divide(BigDecimal.valueOf(2), mathContext);
        log.debug("seed value = {}", predecessor.toPlainString());
        BigDecimal successor = calculateSuccessor(predecessor, scaled, squareRootContext);
        long iterations = 1;
        while (successor.subtract(predecessor).abs().compareTo(abortCriterion) > 0
            && iterations <= squareRootContext.getMaxIterations()) {
            log.debug("|successor - predecessor| = {}", successor.subtract(predecessor).abs().toPlainString());
            predecessor = successor;
            successor = calculateSuccessor(successor, scaled, squareRootContext);
            iterations++;
        }
        log.debug("terminated after {} iterations", iterations);
        log.debug("sqrt({}) = {}", decimal.toPlainString(), successor.toPlainString());
        return successor;
    }

    private static BigDecimal calculateSuccessor(final BigDecimal predecessor, final BigDecimal decimal,
        final SquareRootContext squareRootContext) {
        assert predecessor != null;
        assert decimal != null;
        assert squareRootContext != null;
        log.debug("iteration");
        log.debug("predecessor = {}", predecessor.toPlainString());
        final MathContext mathContext = squareRootContext.getMathContext();
        final BigDecimal divisor = BigDecimal.valueOf(2).multiply(predecessor, mathContext);
        final BigDecimal successor = divisor.compareTo(BigDecimal.ZERO) != 0
            ? predecessor.pow(2, mathContext).add(decimal, mathContext).divide(divisor, mathContext)
            : squareRootContext.getAbortCriterion();
        log.debug("successor = {}", successor.toPlainString());
        return successor;
    }
}
