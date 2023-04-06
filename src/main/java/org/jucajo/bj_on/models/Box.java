package org.jucajo.bj_on.models;

public class Box {
    private String card;

    private Bet bet;

    public Box(String card, Bet bet){
        this.card = card;
        this.bet = bet;
    }

    public String getCard(){
        return this.card;
    }

    public void setCard(String newCard){
        this.card = newCard;
    }

    public Bet getBet(){
        return this.bet;
    }

    public void setBet(Bet newBet){
        this.bet = newBet;
    }

    
}
