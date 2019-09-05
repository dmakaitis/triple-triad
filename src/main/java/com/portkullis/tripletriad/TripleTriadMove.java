package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.MinMaxEngine;

/**
 * Created by darius on 1/18/16.
 */
public class TripleTriadMove {

    private final MinMaxEngine.GameState.Player player;
    private final int cardIndex;
    private final Location location;

    public TripleTriadMove(MinMaxEngine.GameState.Player player, int cardIndex, Location location) {
        this.player = player;
        this.cardIndex = cardIndex;

        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripleTriadMove that = (TripleTriadMove) o;

        if (cardIndex != that.cardIndex) return false;
        if (player != that.player) return false;
        return location == that.location;

    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + cardIndex;
        result = 31 * result + location.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TripleTriadMove{");
        sb.append("player=").append(player);
        sb.append(", cardIndex=").append(cardIndex);
        sb.append(", location=").append(location);
        sb.append('}');
        return sb.toString();
    }

    public MinMaxEngine.GameState.Player getPlayer() {
        return player;
    }

    public int getCardIndex() {
        return cardIndex;
    }


    public Location getLocation() {
        return location;
    }

}
