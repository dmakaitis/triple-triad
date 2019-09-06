package com.portkullis.tripletriad.engine.impl;

import java.util.Optional;

/**
 * Created by darius on 1/18/16.
 */
public class ValuedMove<M> {

    private final M move;
    private final double score;

    public ValuedMove(M move, double score) {
        this.move = move;
        this.score = score;
    }

    public Optional<M> getMove() {
        return Optional.ofNullable(move);
    }

    public double getScore() {
        return score;
    }

}
