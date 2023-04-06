package org.jucajo.bj_on.models;

import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("users")
public class User {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private Integer coins;
    @CreatedDate
    private LocalDate createdAt;

    public User(String name, Integer coins) {
        this.name = name;
        this.coins = coins;
        this.createdAt = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public LocalDate getCreationDate() {
        return createdAt;
    }

    public void setCreationDate(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
