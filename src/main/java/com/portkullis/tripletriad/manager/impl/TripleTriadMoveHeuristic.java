package com.portkullis.tripletriad.manager.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.model.Card;
import com.portkullis.tripletriad.manager.model.TripleTriadMove;

import java.util.List;
import java.util.function.BiFunction;

public class TripleTriadMoveHeuristic implements BiFunction<TripleTriadGameState, TripleTriadMove, Double> {

    private static final double EDGE_VALUE = 4.0;

    @Override
    public Double apply(TripleTriadGameState state, TripleTriadMove move) {
        List<Card> cards = move.getPlayer() == MinMaxEngine.GameState.Player.MAXIMIZING ? state.getBlueCards() : state.getRedCards();
        Card card = cards.get(move.getCardIndex());

        double score = 0.0;

        switch (move.getLocation()) {
            case TOPLEFT:
            case TOP:
            case TOPRIGHT:
                score += EDGE_VALUE * card.getDown();
                break;
            case LEFT:
            case CENTER:
            case RIGHT:
                score += card.getUp() + card.getDown();
                break;
            case BOTTOMLEFT:
            case BOTTOM:
            case BOTTOMRIGHT:
                score += EDGE_VALUE * card.getUp();
                break;
            default:
                break;
        }

        switch (move.getLocation()) {
            case TOPLEFT:
            case LEFT:
            case BOTTOMLEFT:
                score += EDGE_VALUE * card.getRight();
                break;
            case TOP:
            case CENTER:
            case BOTTOM:
                score += card.getLeft() + card.getRight();
                break;
            case TOPRIGHT:
            case RIGHT:
            case BOTTOMRIGHT:
                score += EDGE_VALUE * card.getLeft();
                break;
        }

        return score;
    }

}
