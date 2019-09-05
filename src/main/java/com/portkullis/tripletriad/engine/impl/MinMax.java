package com.portkullis.tripletriad.engine.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by darius on 1/18/16.
 */
public class MinMax<T extends MinMaxEngine.GameState<M>, M> implements MinMaxEngine<T, M> {

    private final BiFunction<T, M, T> engine;
    private final Function<T, Double> evaluator;
    private final boolean printStatus;

    private long evaluatedMoves;

    public MinMax(BiFunction<T, M, T> engine, Function<T, Double> evaluator, boolean printStatus) {
        this.engine = engine;
        this.evaluator = evaluator;
        this.printStatus = printStatus;
    }

    public M findMove(T state, int depth) {
        evaluatedMoves = 0;
        M move = findMove(state, depth, null, null, state.getTurn(), printStatus).getMove();
        System.out.println("Evaluated " + evaluatedMoves + " moves");
        return move;
    }

    private ValuedMove<M> findMove(T state, int depth, Double alpha, Double beta, GameState.Player player, boolean printStatus) {
        Collection<M> moves = state.getLegalMoves();
        if (depth == 0 || moves.isEmpty()) {
            evaluatedMoves++;
            return new ValuedMove(null, evaluator.apply(state));
        }

        ValuedMove bestMove = null;
        if (player == GameState.Player.MAXIMIZING) {
            for (M m : moves) {
                if (printStatus) {
                    System.out.println("Evaluating move: " + m);
                }
                T gameState = engine.apply(state, m);
                ValuedMove vm = findMove(gameState, depth - 1, alpha, beta, GameState.Player.MINIMIZING, false);
                if (bestMove == null || vm.getScore() > bestMove.getScore()) {
                    bestMove = new ValuedMove(m, vm.getScore());
                }
                if (alpha == null || vm.getScore() > alpha) {
                    alpha = vm.getScore();
                }
                if (beta != null && beta <= alpha) {
                    break;
                }
            }
        } else {
            for (M m : moves) {
                if (printStatus) {
                    System.out.println("Evaluating move: " + m);
                }
                T gameState = engine.apply(state, m);
                ValuedMove vm = findMove(gameState, depth - 1, alpha, beta, GameState.Player.MAXIMIZING, false);
                if (bestMove == null || vm.getScore() < bestMove.getScore()) {
                    bestMove = new ValuedMove(m, vm.getScore());
                }
                if (beta == null || vm.getScore() < beta) {
                    beta = vm.getScore();
                }
                if (alpha != null && beta <= alpha) {
                    break;
                }
            }
        }

        return bestMove;
    }

}
