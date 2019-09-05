package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.List;
import java.util.function.BiFunction;

public class TripleTriadMoveHeuristic implements BiFunction<TripleTriadGameState, TripleTriadMove, Integer> {

    @Override
    public Integer apply(TripleTriadGameState state, TripleTriadMove move) {
        List<Card> cards = move.getPlayer() == MinMaxEngine.GameState.Player.MAXIMIZING ? state.getBlueCards() : state.getRedCards();
        Card card = cards.get(move.getCardIndex());

        int score = 0;

        switch (move.getLocation()) {
            case TOPLEFT:
            case TOP:
            case TOPRIGHT:
                score += 4 * card.getDown();
                break;
            case LEFT:
            case CENTER:
            case RIGHT:
                score += card.getUp() + card.getDown();
                break;
            case BOTTOMLEFT:
            case BOTTOM:
            case BOTTOMRIGHT:
                score += 4 * card.getUp();
                break;
            default:
                break;
        }

        switch (move.getLocation()) {
            case TOPLEFT:
            case LEFT:
            case BOTTOMLEFT:
                score += 4 * card.getRight();
                break;
            case TOP:
            case CENTER:
            case BOTTOM:
                score += card.getLeft() + card.getRight();
                break;
            case TOPRIGHT:
            case RIGHT:
            case BOTTOMRIGHT:
                score += 4 * card.getLeft();
                break;
        }

        return score;
    }

}
