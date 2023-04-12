package org.jucajo.bj_on.models;

public class Box {
    private String id, owner;
    private Token token;

    public Box(String id, String owner, Token token) {
        this.id = id;
        this.owner = owner;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
