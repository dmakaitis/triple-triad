package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;

/**
 * Created by darius on 1/18/16.
 */
public class OwnedCard {

    private final MinMaxEngine.GameState.Player player;
    private final Card card;

    public OwnedCard(MinMaxEngine.GameState.Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    public MinMaxEngine.GameState.Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }

}
