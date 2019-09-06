package com.portkullis.tripletriad.manager.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.TripleTriadManager;
import com.portkullis.tripletriad.manager.model.Card;
import com.portkullis.tripletriad.manager.model.TripleTriadMove;
import com.portkullis.tripletriad.manager.model.TripleTriadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToDoubleFunction;

public class TripleTriadManagerImpl implements TripleTriadManager {

    private static final Logger LOG = LoggerFactory.getLogger(TripleTriadManagerImpl.class);

    private final BiFunction<TripleTriadGameState, TripleTriadMove, TripleTriadGameState> engine = new TripleTriadEngine();
    private final ToDoubleFunction<TripleTriadGameState> evaluator = new TripleTriadEvaluator();

    private final MinMaxEngine<TripleTriadGameState, TripleTriadMove> minMax = MinMaxEngine.getInstance(engine, evaluator);

    @Override
    public TripleTriadState startNewGame(List<Card> blueCards, List<Card> redCards, MinMaxEngine.GameState.Player firstPlayer) {
        return mapState(doComputerMoves(new TripleTriadGameState(firstPlayer, blueCards, redCards)));
    }

    @Override
    public TripleTriadState applyMove(TripleTriadState currentState, TripleTriadMove move) {
        return mapState(doComputerMoves(engine.apply(mapState(currentState), move)));
    }

    private TripleTriadGameState doComputerMoves(TripleTriadGameState state) {
        TripleTriadGameState newState;

        if (state.isGameOver() || state.getTurn() == MinMaxEngine.GameState.Player.MINIMIZING) {
            newState = state;
        } else {
            TripleTriadMove move = minMax.findMove(state, 1000);
            LOG.info("Selected move: {}", move);
            newState = doComputerMoves(engine.apply(state, move));
        }

        return newState;
    }

    private TripleTriadState mapState(TripleTriadGameState state) {
        return new TripleTriadState(state.getTurn(), state.getBlueCards(), state.getRedCards(), state.getBoard(), state.getLegalMoves(), state.isGameOver(), evaluator.applyAsDouble(state));
    }

    private TripleTriadGameState mapState(TripleTriadState state) {
        return new TripleTriadGameState(state.getTurn(), state.getBlueCards(), state.getRedCards(), state.getBoard());
    }

}
