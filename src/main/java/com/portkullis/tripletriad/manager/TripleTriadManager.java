package com.portkullis.tripletriad.manager;

import com.portkullis.tripletriad.engine.MinMaxEngine;
import com.portkullis.tripletriad.manager.impl.TripleTriadManagerImpl;
import com.portkullis.tripletriad.manager.model.Card;
import com.portkullis.tripletriad.manager.model.TripleTriadMove;
import com.portkullis.tripletriad.manager.model.TripleTriadState;

import java.util.List;

public interface TripleTriadManager {

    TripleTriadState startNewGame(List<Card> blueCards, List<Card> redCards, MinMaxEngine.GameState.Player firstPlayer);

    TripleTriadState applyMove(TripleTriadState currentState, TripleTriadMove move);

    static TripleTriadManager getInstance() {
        return new TripleTriadManagerImpl();
    }

}
