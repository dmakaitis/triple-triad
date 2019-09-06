package com.portkullis.tripletriad.manager.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.model.Card;
import com.portkullis.tripletriad.manager.model.Location;
import com.portkullis.tripletriad.manager.model.OwnedCard;
import com.portkullis.tripletriad.manager.model.TripleTriadMove;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by darius on 1/18/16.
 */
public class TripleTriadGameState implements MinMaxEngine.GameState<TripleTriadMove> {

    private final Player turn;
    private final List<Card> blueCards;
    private final List<Card> redCards;

    private final OwnedCard[] board;

    public TripleTriadGameState(Player turn, List<Card> blueCards, List<Card> redCards) {
        this(turn, blueCards, redCards, new OwnedCard[9]);
    }

    public TripleTriadGameState(Player turn, List<Card> blueCards, List<Card> redCards, OwnedCard[] board) {
        if (board.length != 9) {
            throw new IllegalArgumentException("The board must have nine slots");
        }
        if (blueCards.size() > 5) {
            throw new IllegalArgumentException("The blue player can only hold up to five cards");
        }
        if (redCards.size() > 5) {
            throw new IllegalArgumentException("The red player can only hold up to five cards");
        }

        // Make sure we have 10 cards...
        int count = blueCards.size() + redCards.size();
        for (OwnedCard c : board) {
            if (c != null) {
                count++;
            }
        }
        if (count > 10) {
            throw new IllegalArgumentException("A game may only have up to ten cards");
        }

        this.turn = turn;
        this.blueCards = blueCards;
        this.redCards = redCards;
        this.board = board;
    }

    @Override
    public Player getTurn() {
        return turn;
    }

    public List<Card> getBlueCards() {
        return blueCards;
    }

    public List<Card> getRedCards() {
        return redCards;
    }

    public OwnedCard[] getBoard() {
        return board;
    }

    @Override
    public boolean isGameOver() {
        return getEmptyLocations().isEmpty();
    }

    @Override
    public Collection<TripleTriadMove> getLegalMoves() {
        Set<TripleTriadMove> moves = new HashSet<>();
        Collection<Card> cards = turn == Player.MAXIMIZING ? blueCards : redCards;
        Set<Location> emptySquares = getEmptyLocations();
        for (int i = 0; i < cards.size(); i++) {
            for (Location l : emptySquares) {
                moves.add(new TripleTriadMove(turn, i, l));
            }
        }
        return moves;
    }

    private Set<Location> getEmptyLocations() {
        Set<Location> locations = new HashSet<>();
        for (Location l : Location.values()) {
            if (board[l.getBoardIndex()] == null) {
                locations.add(l);
            }
        }
        return locations;
    }

}
