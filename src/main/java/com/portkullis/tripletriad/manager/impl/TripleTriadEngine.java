package com.portkullis.tripletriad.manager.impl;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.model.Card;
import com.portkullis.tripletriad.manager.model.Location;
import com.portkullis.tripletriad.manager.model.OwnedCard;
import com.portkullis.tripletriad.manager.model.TripleTriadMove;

import java.util.ArrayList;
import java.util.List;
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
        List<Card> newRed = new ArrayList<>(state.getRedCards());
        List<Card> newBlue = new ArrayList<>(state.getBlueCards());
        OwnedCard[] newBoard = new OwnedCard[9];
        for (int i = 0; i < 9; i++) {
            newBoard[i] = state.getBoard()[i];
        }

        Card selectedCard;
        if (state.getTurn() == MinMaxEngine.GameState.Player.MINIMIZING) {
            selectedCard = newRed.remove(move.getCardIndex());
        } else {
            selectedCard = newBlue.remove(move.getCardIndex());
        }
        OwnedCard newCard = new OwnedCard(state.getTurn(), selectedCard);
        newBoard[move.getLocation().getBoardIndex()] = newCard;

        // Now, do the card flipping...
        flipCards(state, move, newBoard, newCard);

        return new TripleTriadGameState(newTurn, newBlue, newRed, newBoard);
    }

    private void flipCards(TripleTriadGameState state, TripleTriadMove move, OwnedCard[] newBoard, OwnedCard newCard) {
        Location location = move.getLocation();

        // Check up
        location.getUp().ifPresent(compareLocation -> {
            OwnedCard compare = newBoard[compareLocation.getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getUp() > compare.getCard().getDown()) {
                newBoard[compareLocation.getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        });

        // Check down
        location.getDown().ifPresent(compareLocation -> {
            OwnedCard compare = newBoard[compareLocation.getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getDown() > compare.getCard().getUp()) {
                newBoard[compareLocation.getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        });

        // Check left
        location.getLeft().ifPresent(compareLocation -> {
            OwnedCard compare = newBoard[compareLocation.getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getLeft() > compare.getCard().getRight()) {
                newBoard[compareLocation.getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        });

        // Check right
        location.getRight().ifPresent(compareLocation -> {
            OwnedCard compare = newBoard[compareLocation.getBoardIndex()];
            if (compare != null && compare.getPlayer() != newCard.getPlayer() && newCard.getCard().getRight() > compare.getCard().getLeft()) {
                newBoard[compareLocation.getBoardIndex()] = new OwnedCard(state.getTurn(), compare.getCard());
            }
        });
    }

}
