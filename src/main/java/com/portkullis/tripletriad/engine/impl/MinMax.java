package com.portkullis.tripletriad.engine.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToDoubleFunction;

import static java.util.stream.Collectors.toList;

/**
 * Created by darius on 1/18/16.
 */
public class MinMax<T extends MinMaxEngine.GameState<M>, M> implements MinMaxEngine<T, M> {

    private static final Logger LOG = LoggerFactory.getLogger(MinMax.class);

    private final BiFunction<T, M, T> engine;
    private final ToDoubleFunction<T> evaluator;
    private final BiFunction<T, M, Double> moveHeuristic;

    private long evaluatedMoves;

    public MinMax(BiFunction<T, M, T> engine, ToDoubleFunction<T> evaluator) {
        this.engine = engine;
        this.evaluator = evaluator;
        this.moveHeuristic = new DefaultMoveHeuristic();
    }

    public MinMax(BiFunction<T, M, T> engine, ToDoubleFunction<T> evaluator, BiFunction<T, M, Double> moveHeuristic) {
        this.engine = engine;
        this.evaluator = evaluator;
        this.moveHeuristic = moveHeuristic;
    }

    public M findMove(T state, int depth) {
        Date start = new Date();
        evaluatedMoves = 0;
        M move = findMove(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, state.getTurn()).getMove().orElseThrow(() -> new RuntimeException("No move found"));
        Date end = new Date();
        LOG.info("Evaluated {} moves in {}ms", evaluatedMoves, end.getTime() - start.getTime());
        return move;
    }

    private ValuedMove<M> findMove(T state, int depth, double alpha, double beta, GameState.Player player) {
        List<M> moves = state.getLegalMoves().stream()
                .sorted(Comparator.comparingDouble(a -> -moveHeuristic.apply(state, a)))
                .collect(toList());
        if (depth == 0 || moves.isEmpty()) {
            evaluatedMoves++;
            return new ValuedMove<>(null, evaluator.applyAsDouble(state));
        }

        ValuedMove<M> bestMove;
        if (player == GameState.Player.MAXIMIZING) {
            bestMove = findMoveAlpha(state, depth, alpha, beta, moves);
        } else {
            bestMove = findMoveBeta(state, depth, alpha, beta, moves);
        }

        return bestMove;
    }

    private ValuedMove<M> findMoveAlpha(T state, int depth, double alpha, double beta, List<M> moves) {
        ValuedMove<M> bestMove;
        bestMove = new ValuedMove<>(null, Double.NEGATIVE_INFINITY);
        for (M m : moves) {
            double value = evaluateMove(m, state, depth, alpha, beta, GameState.Player.MINIMIZING).getScore();
            if (value > bestMove.getScore()) {
                bestMove = new ValuedMove<>(m, value);
            }

            alpha = Math.max(alpha, value);

            if (alpha >= beta) {
                break;
            }
        }
        return bestMove;
    }

    private ValuedMove<M> findMoveBeta(T state, int depth, double alpha, double beta, List<M> moves) {
        ValuedMove<M> bestMove;
        bestMove = new ValuedMove<>(null, Double.POSITIVE_INFINITY);
        for (M m : moves) {
            double value = evaluateMove(m, state, depth, alpha, beta, GameState.Player.MAXIMIZING).getScore();
            if (value < bestMove.getScore()) {
                bestMove = new ValuedMove<>(m, value);
            }

            beta = Math.min(beta, value);

            if (alpha >= beta) {
                break;
            }
        }
        return bestMove;
    }

    private ValuedMove evaluateMove(M move, T state, int depth, double alpha, double beta, GameState.Player player) {
        LOG.debug("Evaluating move: {}", move);
        T gameState = engine.apply(state, move);
        return findMove(gameState, depth - 1, alpha, beta, player);
    }

    private class DefaultMoveHeuristic implements BiFunction<T, M, Double> {
        @Override
        public Double apply(T t, M m) {
            double value = evaluator.applyAsDouble(engine.apply(t, m));
            return t.getTurn() == GameState.Player.MAXIMIZING ? value : -value;
        }
    }

}
