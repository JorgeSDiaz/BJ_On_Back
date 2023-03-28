package org.jucajo.bj_on.persistence;

public class UserException extends Exception {
    public static final String OUT_OF_BOUNDS = "MINIMUM 0, MAXIMUM 9,999,999";
    public UserException(String message) {
        super(message);
    }
}
