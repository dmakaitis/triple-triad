package com.portkullis.tripletriad.manager.model;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.Collection;
import java.util.List;

public class TripleTriadState {

    private final MinMaxEngine.GameState.Player turn;
    private final List<Card> blueCards;
    private final List<Card> redCards;

    private final OwnedCard[] board;

    private final Collection<TripleTriadMove> legalMoves;

    private final boolean gameOver;
    private final double score;

    public TripleTriadState(MinMaxEngine.GameState.Player turn, List<Card> blueCards, List<Card> redCards, OwnedCard[] board, Collection<TripleTriadMove> legalMoves, boolean gameOver, double score) {
        this.turn = turn;
        this.blueCards = blueCards;
        this.redCards = redCards;
        this.board = board;
        this.legalMoves = legalMoves;
        this.gameOver = gameOver;
        this.score = score;
    }

    public MinMaxEngine.GameState.Player getTurn() {
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

    public Collection<TripleTriadMove> getLegalMoves() {
        return legalMoves;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public double getScore() {
        return score;
    }

}
