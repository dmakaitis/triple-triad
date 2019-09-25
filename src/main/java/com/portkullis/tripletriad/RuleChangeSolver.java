package com.portkullis.tripletriad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class RuleChangeSolver {

    // Draw formula for fully charged Ultima DP in Shumi Village: (20 * (rnd + 128) / 512) + 1

    private static int SEED = 0;
    private static final List<RNGState> RNG_STATES = loadStatesLarge();
    private static final boolean QUEEN_IN_REGION = false;

    private static final List<Rule> LOCAL_RULES = asList(Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL);
    private static final List<Rule> FOREIGN_RULES = asList(Rule.OPEN, Rule.SAME, Rule.PLUS);

    public static void main(String[] args) {
        List<Integer> seeds = findSeeds(true, Rule.SAME_WALL);
        System.out.println(seeds);

        boolean search = true;
        int challengeCount = 0;
        while (search) {
            SEED = (QUEEN_IN_REGION ? 3 : 2) * (challengeCount + 1);
            Optional<RNGState> result = getResult(SEED);
            if (result.isPresent() && result.get().abolish) {
                search = false;
            } else {
                challengeCount++;
            }
        }
        System.out.println();

        SEED = 0;
        challengeNpcMix(challengeCount);
        printResult();
        System.out.println("Final seed value: " + SEED);

    }

    private static List<Integer> findSeeds(boolean abolish, Rule rule) {
        return IntStream.range(0, RNG_STATES.size() - 3).filter(seed -> {
            Optional<RNGState> result = getResult(seed);
            if (result.isPresent()) {
                return result.get().abolish == abolish && result.get().rule == rule;
            }
            return false;
        }).boxed().collect(toList());
    }

    private static Optional<RNGState> getResult(int seed) {
        for (int i = 0; i < 3; i++) {
            Rule rule = RNG_STATES.get(seed++).rule;
            if (canSpread(rule)) {
                return Optional.of(new RNGState(false, rule));
            }
        }
        if (RNG_STATES.get(seed).abolish) {
            Rule rule = RNG_STATES.get(seed - 1).rule;
            if (canAbolish(rule)) {
                return Optional.of(new RNGState(true, rule));
            }
        }
        return Optional.empty();
    }

    private static void printResult() {
        challengeNpcMix(1);
        for (int i = 0; i < 3; i++) {
            Rule rule = RNG_STATES.get(SEED++).rule;
            if (canSpread(rule)) {
                System.out.println("Spread " + rule);
                return;
            }
        }
        if (RNG_STATES.get(SEED).abolish) {
            Rule rule = RNG_STATES.get(SEED - 1).rule;
            if (canAbolish(rule)) {
                System.out.println("Abolish " + rule);
                return;
            }
        }
        System.out.println("No effect");
    }

    private static boolean canAbolish(Rule rule) {
        return LOCAL_RULES.contains(rule);// && !FOREIGN_RULES.contains(rule);
    }

    private static boolean canSpread(Rule rule) {
        return FOREIGN_RULES.contains(rule) && !LOCAL_RULES.contains(rule);
    }

    private static void challengeNpcMix(int i) {
        if (i == 0) return;
        System.out.println("Challenging NPC that is mixing rules " + i + " times...");
        SEED += (QUEEN_IN_REGION ? 3 : 2) * i;
    }

    private static List<RNGState> loadStates() {
        List<RNGState> states = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(RuleChangeSolver.class.getResourceAsStream("/ttseedtable.csv")))) {
            // Skip first line
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("[,]");
                int value = Integer.parseInt(values[1]);
                Rule rule = Rule.parseRule(values[2]);
                states.add(new RNGState(value >= 128, rule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return states;
    }

    private static List<RNGState> loadStatesLarge() {
        List<RNGState> states = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(RuleChangeSolver.class.getResourceAsStream("/ySbddd9a.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("[,]");
                int value = Integer.parseInt(line.substring(7, 10).trim());
                Rule rule = Rule.parseRule(line.substring(11));
                states.add(new RNGState(value < 128, rule));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return states;
    }

    private static enum Rule {
        OPEN,
        SUDDEN_DEATH,
        RANDOM,
        SAME,
        SAME_WALL,
        PLUS,
        ELEMENTAL;

        public static Rule parseRule(String value) {
            return Rule.valueOf(value.toUpperCase().replaceAll("[ ]", "_"));
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
