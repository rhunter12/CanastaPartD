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
        player1 = new CanastaPlayer(orig.player1);
        player2 = new CanastaPlayer(orig.player2);
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
        if (!(checkValidMeld(p))) {
            return false;
        }
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().get(i).getValue() == selectedCard) {
                discardPile.add(p.getHand().remove(i));
                selectedCard = -1;
                return true;
            }
        }
        return false;
    }

    /**
     * Takes two cards from deck; checks if it is a red three and
     * handles it accordingly
     * @param p
     */
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

    /**
     * Adds a selected card to the player's meld
     * @param p
     * @return
     */
    public boolean meldCard(CanastaPlayer p) {
        int pos = searchHand(p, selectedCard);;

        switch (selectedCard) {
            case -1:
                return false;
            case 1:
                p.getPlayerMoves().add(1);
                p.getMeldedAce().add(p.getHand().remove(pos));
                break;
            case 2:
                p.getPlayerMoves().add(2);
                p.getMeldedWild().add(p.getHand().remove(pos));
                break;
            case 3:
                p.getPlayerMoves().add(3);
                p.getMelded3().add(p.getHand().remove(pos));
                break;
            case 4:
                p.getPlayerMoves().add(4);
                p.getMelded4().add(p.getHand().remove(pos));
                break;
            case 5:
                p.getPlayerMoves().add(5);
                p.getMelded5().add(p.getHand().remove(pos));
                break;
            case 6:
                p.getPlayerMoves().add(6);
                p.getMelded6().add(p.getHand().remove(pos));
                break;
            case 7:
                p.getPlayerMoves().add(7);
                p.getMelded7().add(p.getHand().remove(pos));
                break;
            case 8:
                p.getPlayerMoves().add(8);
                p.getMelded8().add(p.getHand().remove(pos));
                break;
            case 9:
                p.getPlayerMoves().add(9);
                p.getMelded9().add(p.getHand().remove(pos));
                break;
            case 10:
                p.getPlayerMoves().add(10);
                p.getMelded10().add(p.getHand().remove(pos));
                break;
            case 11:
                p.getPlayerMoves().add(11);
                p.getMeldedJack().add(p.getHand().remove(pos));
                break;
            case 12:
                p.getPlayerMoves().add(12);
                p.getMeldedQueen().add(p.getHand().remove(pos));
                break;
            case 13:
                p.getPlayerMoves().add(13);
                p.getMeldedKing().add(p.getHand().remove(pos));
                break;
        }
        return true;
    }

    /**
     * Searches hand for selected card and returns index
     * @param p
     * @param n
     * @return
     */
    public int searchHand(CanastaPlayer p, int n) {
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.getHand().get(i).getValue() == n) {
                return i;
            }
        }
        return -1;
    }

    public int countWildCards(ArrayList<Card> cards, int value) {
        int wildCount = 0;

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getValue() != value) {
                wildCount++;
            }
        }
        return wildCount;
    }

    public boolean checkValidMeld(CanastaPlayer p) {
        if (!((p.getMeldedAce().size() >= 3 && countWildCards(p.getMeldedAce(), 1) <= p.getMeldedAce().size()/2) || p.getMeldedAce().size() == 0)) {
            return false;
        }
        if (!((p.getMelded4().size() >= 3 && countWildCards(p.getMelded4(), 4) <= p.getMelded4().size()/2) || p.getMelded4().size() == 0)) {
            return false;
        }
        if (!((p.getMelded5().size() >= 3 && countWildCards(p.getMelded5(), 5) <= p.getMelded5().size()/2) || p.getMelded5().size() == 0)) {
            return false;
        }
        if (!((p.getMelded6().size() >= 3 && countWildCards(p.getMelded6(), 6) <= p.getMelded6().size()/2) || p.getMelded6().size() == 0)) {
            return false;
        }
        if (!((p.getMelded7().size() >= 3 && countWildCards(p.getMelded7(), 7) <= p.getMelded7().size()/2) || p.getMelded7().size() == 0)) {
            return false;
        }
        if (!((p.getMelded8().size() >= 3 && countWildCards(p.getMelded8(), 8) <= p.getMelded8().size()/2) || p.getMelded8().size() == 0)) {
            return false;
        }
        if (!((p.getMelded9().size() >= 3 && countWildCards(p.getMelded9(), 9) <= p.getMelded9().size()/2) || p.getMelded9().size() == 0)) {
            return false;
        }
        if (!((p.getMelded10().size() >= 3 && countWildCards(p.getMelded10(), 10) <= p.getMelded10().size()/2) || p.getMelded10().size() == 0)) {
            return false;
        }
        if (!((p.getMeldedJack().size() >= 3 && countWildCards(p.getMeldedJack(), 11) <= p.getMeldedJack().size()/2) || p.getMeldedJack().size() == 0)) {
            return false;
        }
        if (!((p.getMeldedQueen().size() >= 3 && countWildCards(p.getMeldedQueen(), 12) <= p.getMeldedQueen().size()/2) || p.getMeldedQueen().size() == 0)) {
            return false;
        }
        if (!((p.getMeldedKing().size() >= 3 && countWildCards(p.getMeldedKing(), 13) <= p.getMeldedKing().size()/2) || p.getMeldedKing().size() == 0)) {
            return false;
        }

        if (!(p.getMeldedWild().size() == 0 || p.getMeldedWild().size() >= 3)) {
            return false;
        }
        return true;
    }

    public boolean undo(CanastaPlayer p) {
        if(p.getPlayerMoves().size() == 0) {
            return false;
        }
        int value = p.getPlayerMoves().get(p.getPlayerMoves().size()-1);

        switch (value) {
            case 1:
                p.getHand().add(p.getMeldedAce().remove(p.getMeldedAce().size()-1));
                break;
            case 3:
                p.getHand().add(p.getMelded3().remove(p.getMelded3().size()-1));
                break;
            case 4:
                p.getHand().add(p.getMelded4().remove(p.getMelded4().size()-1));
                break;
            case 5:
                p.getHand().add(p.getMelded5().remove(p.getMelded5().size()-1));
                break;
            case 6:
                p.getHand().add(p.getMelded6().remove(p.getMelded6().size()-1));
                break;
            case 7:
                p.getHand().add(p.getMelded7().remove(p.getMelded7().size()-1));
                break;
            case 8:
                p.getHand().add(p.getMelded8().remove(p.getMelded8().size()-1));
                break;
            case 9:
                p.getHand().add(p.getMelded9().remove(p.getMelded9().size()-1));
                break;
            case 10:
                p.getHand().add(p.getMelded10().remove(p.getMelded10().size()-1));
                break;
            case 11:
                p.getHand().add(p.getMeldedJack().remove(p.getMeldedJack().size()-1));
                break;
            case 12:
                p.getHand().add(p.getMeldedQueen().remove(p.getMeldedQueen().size()-1));
                break;
            case 13:
                p.getHand().add(p.getMeldedKing().remove(p.getMeldedKing().size()-1));
                break;
        }

        p.getPlayerMoves().remove(p.getPlayerMoves().size()-1);
        return true;
    }

    /**
     * Selects card
     * @param p
     * @param card
     * @return
     */
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

    public String toString() {
        return null;
    }


    @Override
    public void onClick(View view) {
        CanastaGameState firstInstance = new CanastaGameState();
        firstInstance.start();
        CanastaGameState secondInstance = new CanastaGameState(firstInstance);

        //building pre-defined hand for testing
        Card addedCard = new Card(5,'H');
        firstInstance.player1.getHand().add(addedCard);
        firstInstance.player1.getHand().add(addedCard);
        firstInstance.player1.getHand().add(addedCard);
        firstInstance.player1.getHand().add(addedCard);

        firstInstance.drawFromDeck(player1);
        firstInstance.selectCard(player1,5);
        firstInstance.meldCard(player1);
        firstInstance.meldCard(player1);
        firstInstance.undo(player1);
        firstInstance.meldCard(player1);
        firstInstance.meldCard(player1);
        firstInstance.addToDiscard(player1);

        CanastaGameState thirdInstance = new CanastaGameState();
        CanastaGameState fourthInstance = new CanastaGameState(thirdInstance);

        secondInstance.toString();
        fourthInstance.toString();
    }




    //accessors
    public int getPlayer1Score() {
        return player1Score;
    }
    public int getPlayer2Score() {
        return player2Score;
    }



}
