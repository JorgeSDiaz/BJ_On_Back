package org.jucajo.bj_on.models;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class User {
    @Id
    private String userId;
    @Indexed(unique = true)
    private String name;
    private Integer coins;
    @CreatedDate
    private final LocalDate createdAt;

    public User(String name, Integer coins) {
        this.name = name;
        this.coins = coins;
        this.createdAt = LocalDate.now();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoins() {
        return this.coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public LocalDate getCreationDate() {
        return this.createdAt;
    }
}
