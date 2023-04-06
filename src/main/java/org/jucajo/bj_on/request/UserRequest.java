package org.jucajo.bj_on.request;

public class UserRequest {
    private String name;
    private Integer coins;

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
