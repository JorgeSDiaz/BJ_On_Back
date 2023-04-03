package org.jucajo.bj_on.models;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;



public class Game {


    private String id;

    private LocalDate createdAt;

    private List<User> players;


    private Box box;

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Game(String id, LocalDate createdAt, List<User> players, Box box) {
        this.id = id;
        this.createdAt = createdAt;
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }
}
