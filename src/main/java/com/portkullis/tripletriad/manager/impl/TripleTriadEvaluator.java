package com.portkullis.tripletriad.manager.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.function.ToDoubleFunction;

/**
 * Created by darius on 1/20/16.
 */
public class TripleTriadEvaluator implements ToDoubleFunction<TripleTriadGameState> {

    @Override
    public double applyAsDouble(TripleTriadGameState state) {
        int score = state.getBlueCards().size() - state.getRedCards().size();
        for (int i = 0; i < 9; i++) {
            if (state.getBoard()[i] != null) {
                score += state.getBoard()[i].getPlayer() == MinMaxEngine.GameState.Player.MAXIMIZING ? 1 : -1;
            }
        }
        return score;
    }

}
