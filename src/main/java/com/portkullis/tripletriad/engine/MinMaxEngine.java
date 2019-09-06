package com.portkullis.tripletriad.engine;

import com.portkullis.tripletriad.engine.impl.MinMax;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Interface for a min-max game move evaluator.
 *
 * @param <T> the game state type.
 * @param <M> the move type.
 */
public interface MinMaxEngine<T extends MinMaxEngine.GameState<M>, M> {

    /**
     * Finds the best move for the current player given the game state and maximum search depth.
     *
     * @param state the game state.
     * @param depth the maximum search depth in moves.
     * @return the best move that was found for the current player.
     */
    M findMove(T state, int depth);

    /**
     * Returns an instance of the min-max engine.
     *
     * @param moveApplier a function that applies a move to a game state, returning a new game state.
     * @param evaluator   the evaluator to use to calculate the value of a game state.
     * @param <S>         the game state type.
     * @param <N>         the move type.
     * @return an instance of the min-max engine.
     */
    static <S extends GameState<N>, N> MinMaxEngine<S, N> getInstance(BiFunction<S, N, S> moveApplier, Function<S, Double> evaluator) {
        return new MinMax<>(moveApplier, evaluator);
    }

    /**
     * Returns an instance of the min-max engine.
     *
     * @param moveApplier a function that applies a move to a game state, returning a new game state.
     * @param evaluator   the evaluator to use to calculate the value of a game state.
     * @param heuristic   a heuristic for estimating the relative value of moves give a game state.
     * @param <S>         the game state type.
     * @param <N>         the move type.
     * @return an instance of the min-max engine.
     */
    static <S extends GameState<N>, N> MinMaxEngine<S, N> getInstance(BiFunction<S, N, S> moveApplier, Function<S, Double> evaluator, BiFunction<S, N, Double> heuristic) {
        return new MinMax<>(moveApplier, evaluator, heuristic);
    }

    /**
     * Game state interface.
     *
     * @param <M> the move type.
     */
    interface GameState<M> {

        /**
         * Returns which player's turn it is.
         *
         * @return the player who's turn it is.
         */
        Player getTurn();

        /**
         * Returns {@code true} if the game is over.
         *
         * @return {@code true} if the game is over; {@code false} otherwise.
         */
        boolean isGameOver();

        /**
         * Returns a collection of legal moves for the current player.
         *
         * @return a collection of legal moves for the current player.
         */
        Collection<M> getLegalMoves();

        /**
         * Enumeration of players.
         */
        enum Player {
            MAXIMIZING, MINIMIZING;
        }

    }

}
