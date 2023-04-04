package org.jucajo.bj_on.persistence;

public class GameControllerException extends Exception {
    public static final String gameFull  = "GAME FULL";
    public GameControllerException(String message) {
        super(message);
    }
}
