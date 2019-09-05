package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.function.Function;

/**
 * Created by darius on 1/20/16.
 */
public class TripleTriadEvaluator implements Function<TripleTriadGameState, Double> {

    @Override
    public Double apply(TripleTriadGameState state) {
        int score = state.getBlueCards().size() - state.getRedCards().size();
        for (int i = 0; i < 9; i++) {
            if (state.getBoard()[i] != null) {
                score += state.getBoard()[i].getPlayer() == MinMaxEngine.GameState.Player.MAXIMIZING ? 1 : -1;
            }
        }
        return Double.valueOf(score);
    }

}
