package org.jucajo.bj_on.persistence;

public class GameControllerException extends Exception {
    public static final String GAME_FULL  = "GAME FULL";
    public static final String NOT_UPDATE_OTHER_BETS_OR_UPDATE_AGAIN = "YOU CANT UPDATE BETS OF THE OTHER PLAYER OR YOU ONLY BET FOR A ONE NUMBER";
    public static final String TIME_FINISHED = "THE TIME OF BET FINISHED";
    public static final String ON_GAME = "CONNECTION SUCCESFULL";
    public static final String REGISTER_BET = "BET REGISTERED";
    public GameControllerException(String message) {
        super(message);
    }
}
