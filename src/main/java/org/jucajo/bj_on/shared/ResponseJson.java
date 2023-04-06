package org.jucajo.bj_on.shared;

public class ResponseJson {
    public static String error(int code, String message) {
        return "{\"error\": {\"code\": " + code + ",\"message\": \"" + message + "\"}   }";
    }
}
