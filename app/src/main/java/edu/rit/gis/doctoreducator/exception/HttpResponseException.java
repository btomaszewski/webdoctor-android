package edu.rit.gis.doctoreducator.exception;

import java.io.IOException;

/**
 * Thrown when an HTTP code is not 200.
 */
public class HttpResponseException extends IOException {

    /** Response code which caused the error. */
    private int mCode;

    public HttpResponseException(int code) {
        super();
        mCode = code;
    }

    public int getErrorCode() {
        return mCode;
    }
}
