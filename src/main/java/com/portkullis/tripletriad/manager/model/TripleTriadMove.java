package com.portkullis.tripletriad.manager.model;

import com.portkullis.tripletriad.engine.MinMaxEngine;

import java.util.Objects;
import java.util.StringJoiner;

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
        return getCardIndex() == that.getCardIndex() &&
                getPlayer() == that.getPlayer() &&
                getLocation() == that.getLocation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer(), getCardIndex(), getLocation());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TripleTriadMove.class.getSimpleName() + "[", "]")
                .add("player=" + player)
                .add("cardIndex=" + cardIndex)
                .add("location=" + location)
                .toString();
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
