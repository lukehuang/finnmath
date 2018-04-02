package com.github.ltennstedt.finnmath.core.linear;

import com.google.common.annotations.Beta;

/**
 * {@link Exception} if the {@link AbstractMatrix} has to be square but is not
 *
 * @author Lars Tennstedt
 * @since 1
 */
@Beta
public final class MatrixNotSquareException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MatrixNotSquareException() {
        super();
    }

    /**
     * Constructor
     *
     * @param message
     *            message
     */
    public MatrixNotSquareException(final String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public MatrixNotSquareException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     *
     * @param cause
     *            cause
     */
    public MatrixNotSquareException(final Throwable cause) {
        super(cause);
    }
}
