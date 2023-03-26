package org.jucajo.bj_on.persistence;

public class UserServiceException extends Exception{
    public static final String NOT_FOUND = "USER NOT FOUND";
    public static final String ALREADY_EXISTS = "USER ALREADY EXISTS";
    public UserServiceException(String message) {
        super(message);
    }
}
