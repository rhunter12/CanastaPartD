package edu.up.canastapartd;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;


public class CanastaGameState implements Button.OnClickListener {

    // instance variables
    ArrayList<Card> deck = new ArrayList<>();
    ArrayList<Card> discardPile = new ArrayList<>();
    private int player1Score; //player 1 is the human
    private int player2Score; //player 2 is the AI
    CanastaPlayer player1;
    CanastaPlayer player2;
    int playerTurnID;
    int selectedCard = -1;

    /**
     * Constructor
     */
    public CanastaGameState() {
        deck = null;
        discardPile = null;
        player1Score = 0;
        player2Score = 0;
        player1 = null;
        player2 = null;
        playerTurnID = 0;
    }

    /**
     * Copy constructor
     * @param orig
     */
    public CanastaGameState(CanastaGameState orig) {
        for (Card c: orig.deck) {
            this.deck.add(new Card(c));
        }
        for (Card c: orig.discardPile) {
            this.discardPile.add(new Card(c));
        }
        player1Score = orig.player1Score;
        player2Score = orig.player2Score;
        player1 = orig.player1;
        player2 = orig.player2;
        playerTurnID = orig.playerTurnID;
    }

    /**
     * Builds deck and shuffles
     */
    public void buildDeck() {
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 14; j++) {
                deck.add(new Card(j,'H'));
                deck.add(new Card(j,'S'));
                deck.add(new Card(j,'D'));
                deck.add(new Card(j,'C'));
            }
            deck.add(new Card(0,'W'));
            deck.add(new Card(0,'W'));
        }
        Collections.shuffle(deck);
    }

    /**
     * Deals card to players, adds one to discard pile
     * @return
     */
    public boolean deal() {
        for (int i = 0; i < 15; i++) {
            player1.getHand().add(deck.remove(0));
            player2.getHand().add(deck.remove(0));
        }
        while (deck.get(0).getValue() == 3 && (deck.get(0).getSuit() == 'H' || deck.get(0).getSuit() == 'D')) {
            Collections.shuffle(deck);
        }
        discardPile.add(deck.remove(0));
        return true;
    }

    /**
     * Searches through hand for selected card to move to
     * discard pile
     * @param p
     * @return
     */
    public boolean addToDiscard(CanastaPlayer p) {
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().get(i).getValue() == selectedCard) {
                discardPile.add(p.getHand().remove(i));
                selectedCard = -1;
                return true;
            }
        }
        return false;
    }

    public void drawFromDeck(CanastaPlayer p) {
        p.getHand().add(deck.remove(0));
        p.getHand().add(deck.remove(0));
        removeRedThree(p);
    }

    /**
     * Removes red three from hand and replaces it with something else
     * @param p
     * @return
     */
    public void removeRedThree(CanastaPlayer p) {
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().get(0).getValue() == 3 && (p.getHand().get(0).getSuit() == 'H' || p.getHand().get(0).getSuit() == 'D')) {
                p.getHand().remove(i);
                p.getHand().add(deck.remove(0));
                i = 0;  //resets loop if a red three has been found. Checks if new card is a red three
                p.setScore(p.getScore() + 100);
            }
        }
    }

    public boolean meldCard(CanastaPlayer p) {
        int pos = searchHand(p, selectedCard);;

        switch (selectedCard) {
            case -1:
                return false;
            case 1:
                p.getMeldedAce().add(p.getHand().remove(pos));
                break;
        }
        return true;
    }

    public int searchHand(CanastaPlayer p, int n) {
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().get(i).getValue() == n) {
                return i;
            }
        }
        return -1;
    }

    public boolean checkValidMeld() {
        return false;
    }

    public boolean undo() {
        return false;
    }

    public boolean selectCard(CanastaPlayer p, int card) {
        if (playerTurnID == p.getPlayerNum()) {
            selectedCard = card;
            return true;
        }
        return false;
    }


    /**
     * Init players, call build deck
     * @return
     */
    public boolean start() {
        player1 = new CanastaPlayer(1);
        player2 = new CanastaPlayer(2);

        buildDeck();
        deal();
        return true;
    }


    @Override
    public void onClick(View view) {

    }




    //accessors
    public int getPlayer1Score() {
        return player1Score;
    }
    public int getPlayer2Score() {
        return player2Score;
    }



}
