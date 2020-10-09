package edu.up.canastapartd;

public class Card {

    private int value;
    private char suit;
    private boolean knownCard;

    /**
    Constructor
     **/
    public Card(int v, char s) {
        value = v;
        suit = s;
        knownCard = false;
    }

    /**
    Copy constructor
     **/
    public Card (Card orig) {
        value = orig.value;
        suit = orig.suit;
        knownCard = orig.knownCard;
    }

    public int getValue() {
        return value;
    }
    public char getSuit() {
        return suit;
    }
}