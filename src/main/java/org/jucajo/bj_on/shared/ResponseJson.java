package org.jucajo.bj_on.shared;

public class ResponseJson {
    public static String error(int code, String message) {
        return "{\"code\": " + code + ",\"message\": \"" + message + "\"}";
    }
}
