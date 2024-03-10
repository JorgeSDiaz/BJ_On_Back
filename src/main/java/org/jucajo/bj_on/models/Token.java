package org.jucajo.bj_on.models;

public class Token {
    private String color;
    private Integer x, y;

    public Token(Integer x, Integer y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
