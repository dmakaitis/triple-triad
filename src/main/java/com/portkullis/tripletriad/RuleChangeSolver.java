package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.RngSimulationEngine;
import com.portkullis.tripletriad.engine.model.Rule;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import static java.util.Arrays.asList;

public class RuleChangeSolver {

    // Draw formula for fully charged Ultima DP in Shumi Village: (20 * (rnd + 128) / 512) + 1
    private static final RngSimulationEngine rngSimulationEngine = RngSimulationEngine.getInstance();


    private static final boolean QUEEN_IN_REGION = false;

    private static final List<Rule> LOCAL_RULES = asList(Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL);
    private static final List<Rule> FOREIGN_RULES = asList(Rule.OPEN, Rule.SAME, Rule.PLUS);

    public static void main(String[] args) {
        boolean search = true;
        int challengeCount = 0;
        while (search) {
            rngSimulationEngine.reset();

            for (int i = 0; i < challengeCount; i++) {
                challengeAndDecline();
            }

            Optional<RNGState> result = challengeAndGetResult();
            if (result.isPresent() && result.get().abolish) {
                search = false;
            } else {
                challengeCount++;
            }
        }
        System.out.println();

        rngSimulationEngine.reset();

        challengeNpcMix(challengeCount);
        printResult();
    }

    private static void challengeAndDecline() {
        rngSimulationEngine.getNextValue();
        rngSimulationEngine.getNextValue();
        if (QUEEN_IN_REGION) {
            rngSimulationEngine.getNextValue();
        }
    }

    private static Optional<RNGState> challengeAndGetResult() {
        Rule rule = null;
        for (int i = 0; i < 3; i++) {
            rule = rngSimulationEngine.getNextRule();
            if (canSpread(rule)) {
                return Optional.of(new RNGState(false, rule));
            }
        }
        if (rngSimulationEngine.getNextAbolishFlag() && canAbolish(rule)) {
            return Optional.of(new RNGState(true, rule));
        }
        return Optional.empty();
    }

    private static void printResult() {
        Optional<RNGState> result = challengeAndGetResult();
        if (result.isPresent()) {
            System.out.println((result.get().abolish ? "Abolish " : "Spread ") + result.get().rule);
        } else {
            System.out.println("No effect");
        }
    }

    private static boolean canAbolish(Rule rule) {
        return LOCAL_RULES.contains(rule);
    }

    private static boolean canSpread(Rule rule) {
        return FOREIGN_RULES.contains(rule) && !LOCAL_RULES.contains(rule);
    }

    private static void challengeNpcMix(int i) {
        if (i == 0) return;
        System.out.println("Challenging NPC that is mixing rules " + i + " times...");
        for (int r = 0; r < i; r++) {
            challengeAndDecline();
        }
    }

    private static class RNGState {
        final boolean abolish;
        final Rule rule;

        private RNGState(boolean abolish, Rule rule) {
            this.abolish = abolish;
            this.rule = rule;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", RNGState.class.getSimpleName() + "[", "]")
                    .add("abolish=" + abolish)
                    .add("rule=" + rule)
                    .toString();
        }
    }

}
