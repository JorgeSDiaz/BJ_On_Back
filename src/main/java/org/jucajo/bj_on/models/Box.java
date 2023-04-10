package org.jucajo.bj_on.models;

public class Box {
    private String id;

    private Bet bet;

    public Box(String card, Bet bet){
        this.id = card;
        this.bet = bet;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String newCard){
        this.id = newCard;
    }

    public Bet getBet(){
        return this.bet;
    }

    public void setBet(Bet newBet){
        this.bet = newBet;
    }

    
}
