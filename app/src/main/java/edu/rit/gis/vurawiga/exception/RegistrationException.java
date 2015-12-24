package edu.rit.gis.vurawiga.exception;

/**
 * Thrown when registration fails.
 */
public class RegistrationException extends Exception {

    public RegistrationException() {
        super();
    }

    public RegistrationException(String detailMessage) {
        super(detailMessage);
    }

    public RegistrationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RegistrationException(Throwable throwable) {
        super(throwable);
    }
}
