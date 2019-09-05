package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Created by darius on 1/20/16.
 */
public class TripleTriadEngine implements BiFunction<TripleTriadGameState, TripleTriadMove, TripleTriadGameState> {

    @Override
    public TripleTriadGameState apply(TripleTriadGameState state, TripleTriadMove move) {
        if (!state.getLegalMoves().contains(move)) {
            throw new IllegalArgumentException("Illegal move!");
        }

        MinMaxEngine.GameState.Player newTurn = state.getTurn() == MinMaxEngine.GameState.Player.MINIMIZING ? MinMaxEngine.GameState.Player.MAXIMIZING : MinMaxEngine.GameState.Player.MINIMIZING;
        List<Card> newRed = new ArrayList<Card>(state.getRedCards());
        List<Card> newBlue = new ArrayList<Card>(state.getBlueCards());
        OwnedCard[] newBoard = new OwnedCard[9];
        for (int i = 0; i < 9; i++) {
            newBoard[i] = state.getBoard()[i];
        }

        Card selectedCard = null;
        if (state.getTurn() == MinMaxEngine.GameState.Player.MINIMIZING) {
            selectedCard = newRed.remove(move.getCardIndex());
        } else {
            selectedCard = newBlue.remove(move.getCardIndex());
        }
        OwnedCard newCard = new OwnedCard(state.getTurn(), selectedCard);
        newBoard[move.getLocation().getBoardIndex()] = newCard;

        // Now, do the card flipping...
        Location location = move.getLocation();

        // Check up
        Optional<Location> compareLocation = location.getUp();
        if (compareLocation.isPresent()) {
            OwnedCard compare = newBoard[compareLocation.get().getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getUp() > compare.getCard().getDown()) {
                newBoard[compareLocation.get().getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        }

        // Check down
        compareLocation = location.getDown();
        if (compareLocation.isPresent()) {
            OwnedCard compare = newBoard[compareLocation.get().getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getDown() > compare.getCard().getUp()) {
                newBoard[compareLocation.get().getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        }

        // Check left
        compareLocation = location.getLeft();
        if (compareLocation.isPresent()) {
            OwnedCard compare = newBoard[compareLocation.get().getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getLeft() > compare.getCard().getRight()) {
                newBoard[compareLocation.get().getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        }

        // Check right
        compareLocation = location.getRight();
        if (compareLocation.isPresent()) {
            OwnedCard compare = newBoard[compareLocation.get().getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getRight() > compare.getCard().getLeft()) {
                newBoard[compareLocation.get().getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        }

        return new TripleTriadGameState(newTurn, newBlue, newRed, newBoard);
    }

}
