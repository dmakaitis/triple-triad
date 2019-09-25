package com.portkullis.tripletriad.engine.impl;

import com.portkullis.tripletriad.engine.RngSimulationEngine;
import com.portkullis.tripletriad.engine.model.Rule;
import com.portkullis.tripletriad.resource.RngTableLoader;

import java.util.List;

/**
 * Implementation of the PS FF8 random number generator simulator.
 */
public class RngSimulationEngineImpl implements RngSimulationEngine {

    private static final Rule[] RULE_LOOKUP = {
            Rule.OPEN,          // 0
            Rule.SAME,          // 32
            Rule.PLUS,          // 64
            Rule.RANDOM,        // 96
            Rule.SUDDEN_DEATH,  // 128
            Rule.OPEN,          // 160
            Rule.SAME_WALL,     // 192
            Rule.ELEMENTAL      // 224
    };

    private final RngTableLoader rngTableLoader = RngTableLoader.getInstance();
    private final List<Integer> randomValues;

    private int seed = 0;

    /**
     * Constructs the engine.
     */
    public RngSimulationEngineImpl() {
        randomValues = rngTableLoader.loadRngTable();
    }

    @Override
    public void reset() {
        seed = 0;
    }

    @Override
    public int getNextValue() {
        return randomValues.get(seed++);
    }

    @Override
    public Rule getNextRule() {
        return RULE_LOOKUP[getNextValue() >> 5];
    }

    @Override
    public boolean getNextAbolishFlag() {
        return getNextValue() < 128;
    }

}
