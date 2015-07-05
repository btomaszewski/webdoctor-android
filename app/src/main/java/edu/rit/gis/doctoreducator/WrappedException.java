package edu.rit.gis.doctoreducator;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A RuntimeException which wraps a normal exception.
 */
public class WrappedException extends RuntimeException {

    private Exception mCause;

    public WrappedException(Exception cause) {
        super(cause);
        mCause = cause;
    }

    @Override
    public Throwable getCause() {
        return mCause.getCause();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return mCause.getStackTrace();
    }
}
