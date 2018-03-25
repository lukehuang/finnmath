package com.github.ltennstedt.finnmath.core.sqrt;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.exclusiveBetween;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import org.apache.commons.lang3.builder.Builder;

/**
 * Value class holding properties for the {@link SquareRootCalculator}
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class SquareRootContext {
    private final BigDecimal abortCriterion;
    private final int maxIerations;
    private final int initalScale;
    private final MathContext mathContext;

    private SquareRootContext(final BigDecimal abortCriterion, final int maxIerations, final int initalScale,
        final MathContext mathContext) {
        assert abortCriterion != null;
        assert BigDecimal.ZERO.compareTo(abortCriterion) < 0 && abortCriterion.compareTo(BigDecimal.ONE) < 0;
        assert maxIerations > 0;
        assert initalScale > -1;
        assert mathContext != null;
        this.abortCriterion = abortCriterion;
        this.maxIerations = maxIerations;
        this.initalScale = initalScale;
        this.mathContext = mathContext;
    }

    public static SquareRootContextBuilder builder() {
        return new SquareRootContextBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(abortCriterion, maxIerations, initalScale, mathContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SquareRootContext)) {
            return false;
        }
        final SquareRootContext other = (SquareRootContext) object;
        return abortCriterion.equals(other.getAbortCriterion()) && maxIerations == other.getMaxIerations()
            && initalScale == other.getInitalScale() && mathContext.equals(other.getMathContext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("abortCriterion", abortCriterion).add("maxIerations", maxIerations)
            .add("initalScale", initalScale).add("mathContext", mathContext).toString();
    }

    public BigDecimal getAbortCriterion() {
        return abortCriterion;
    }

    public int getMaxIerations() {
        return maxIerations;
    }

    public int getInitalScale() {
        return initalScale;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    /**
     * {@link Builder} for a {@link SquareRootContext}
     *
     * @author Lars Tennstedt
     * @since 1
     */
    @Beta
    public static final class SquareRootContextBuilder implements Builder<SquareRootContext> {
        private BigDecimal _abortCriterion = new BigDecimal("0.0000000001");
        private int _maxIerations = 10;
        private int _initalScale = 10;
        private MathContext _mathContext = MathContext.DECIMAL128;

        /**
         * Sets the abort criterion and returns {@code this}
         *
         * @param abortCriterion
         *            abort criterion
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code abortCriterion == null}
         * @throws IllegalArgumentException
         *             if {@code abortCriterion <= 0 || 1 <= abortCriterion}
         * @since 1
         */
        public SquareRootContextBuilder abortCriterion(final BigDecimal abortCriterion) {
            _abortCriterion = requireNonNull(abortCriterion, "abortCriterion");
            exclusiveBetween(BigDecimal.ZERO, BigDecimal.ONE, abortCriterion);
            return this;
        }

        /**
         * Sets the maximal number of iterations and returns {@code this}
         *
         * @param maxIerations
         *            maximal number of iterations
         * @return {@code this}
         * @throws IllegalArgumentException
         *             if {@code maxIterations < 1}
         * @since 1
         */
        public SquareRootContextBuilder maxIerations(final int maxIerations) {
            checkArgument(maxIerations > 0, "expected maxIterations > 0 but actual %s", maxIerations);
            _maxIerations = maxIerations;
            return this;
        }

        /**
         * Sets the inital scale and returns {@code this}
         *
         * @param initalScale
         *            initial scale
         * @return {@code this}
         * @throws IllegalArgumentException
         *             if {@code scale < 0}
         * @since 1
         */
        public SquareRootContextBuilder initalScale(final int initalScale) {
            checkArgument(initalScale > -1, "expected initScale > -1 but actual %s", initalScale);
            _initalScale = initalScale;
            return this;
        }

        /**
         * Sets the {@link MathContext} and returns {@code this}
         *
         * @param mathContext
         *            {@link MathContext}
         * @return {@code this}
         * @throws NullPointerException
         *             if {@code mathContext == null}
         * @since 1
         */
        public SquareRootContextBuilder mathContext(final MathContext mathContext) {
            _mathContext = requireNonNull(mathContext, "mathContext");
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SquareRootContext build() {
            return new SquareRootContext(_abortCriterion, _maxIerations, _initalScale, _mathContext);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("_abortCriterion", _abortCriterion)
                .add("_maxIerations", _maxIerations).add("_initalScale", _initalScale).add("_mathContext", _mathContext)
                .toString();
        }

        public BigDecimal getAbortCriterion() {
            return _abortCriterion;
        }

        public int getMaxIerations() {
            return _maxIerations;
        }

        public int getInitalScale() {
            return _initalScale;
        }

        public MathContext getMathContext() {
            return _mathContext;
        }
    }
}
