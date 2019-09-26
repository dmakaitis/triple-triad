package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.BreadthFirstSearchEngine;
import com.portkullis.tripletriad.engine.RngSimulationEngine;
import com.portkullis.tripletriad.engine.model.Rule;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class RuleChangeSolver {

    // Draw formula for fully charged Ultima DP in Shumi Village: (20 * (rnd + 128) / 512) + 1
    private static final RngSimulationEngine rngSimulationEngine = RngSimulationEngine.getInstance();
    private static final BreadthFirstSearchEngine breadthFirstSearchEngine = BreadthFirstSearchEngine.getInstance();

    private static final boolean QUEEN_IN_REGION = false;

    private static final List<Rule> LOCAL_RULES = asList(Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL);
    private static final List<Rule> FOREIGN_RULES = asList(Rule.OPEN, Rule.PLUS);

    private static final SearchEdge USE_DRAW_POINT = new UseDrawPoint();
    private static final SearchEdge CHALLENGE_THEN_REFUSE = new ChallengeThenRefuse();
    private static final SearchEdge CHALLENGE_THEN_ACCEPT = new ChallengeThenAccept(LOCAL_RULES, FOREIGN_RULES, QUEEN_IN_REGION);

    private static final List<SearchEdge> SEARCH_EDGE_LIST = asList(CHALLENGE_THEN_REFUSE, CHALLENGE_THEN_ACCEPT);

    public static void main(String[] args) {
        SearchNode rootNode = new SearchNode();
        EdgeGenerator edgeGenerator = new EdgeGenerator();
        Function<SearchNode, Boolean> targetSpec = node -> node.terminal && node.abolish && node.rule == Rule.SAME_WALL;

        List<SearchEdge> path = breadthFirstSearchEngine.findPath(rootNode, edgeGenerator, targetSpec, 20);

        if (path == null) {
            System.out.println("No path found");
        } else {
            int count = 0;
            SearchEdge action = null;
            for (SearchEdge edge : path) {
                if (!edge.equals(action)) {
                    if (action != null) {
                        System.out.println(count + " x " + action);
                    }
                    count = 1;
                    action = edge;
                } else {
                    count++;
                }
            }
            if (count > 0) {
                System.out.println(count + " x " + action);
            }
        }
    }

    static class SearchNode {

        private final int seed;
        private final boolean terminal;
        private final boolean abolish;
        private final Rule rule;

        SearchNode() {
            this(0, false, false, null);
        }

        private SearchNode(int seed) {
            this(seed, false, false, null);
        }

        private SearchNode(int seed, boolean terminal, boolean abolish, Rule rule) {
            this.seed = seed;
            this.terminal = terminal;
            this.abolish = abolish;
            this.rule = rule;
        }

        public int getSeed() {
            return seed;
        }

        public boolean isTerminal() {
            return terminal;
        }

        public boolean isAbolish() {
            return abolish;
        }

        public Rule getRule() {
            return rule;
        }

    }

    interface SearchEdge extends Function<SearchNode, SearchNode> {
    }

    static class ChallengeThenRefuse implements SearchEdge {
        @Override
        public String toString() {
            return "Challenge, then refuse";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            return new SearchNode(node.seed + (QUEEN_IN_REGION ? 3 : 2));
        }
    }

    static class UseDrawPoint implements SearchEdge {
        @Override
        public String toString() {
            return "Use draw point";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            return new SearchNode(node.seed + 1);
        }
    }

    static class ChallengeThenAccept implements SearchEdge {
        private final Collection<Rule> localRules;
        private final Collection<Rule> carriedRules;
        private final boolean queenInRegion;

        ChallengeThenAccept(Collection<Rule> localRules, Collection<Rule> carriedRules, boolean queenInRegion) {
            this.localRules = localRules;
            this.carriedRules = carriedRules;
            this.queenInRegion = queenInRegion;
        }

        @Override
        public String toString() {
            return "Challenge, then accept";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            rngSimulationEngine.setSeed(node.seed + (queenInRegion ? 3 : 2));
            Rule rule = null;
            for (int i = 0; i < 3; i++) {
                rule = rngSimulationEngine.getNextRule();
                if (canSpread(rule)) {
                    return new SearchNode(rngSimulationEngine.getSeed(), true, false, rule);
                }
            }
            if (rngSimulationEngine.getNextAbolishFlag() && canAbolish(rule)) {
                return new SearchNode(rngSimulationEngine.getSeed(), true, true, rule);
            }

            // Extra stuff according to ForteGSOmega
//            if (rngSimulationEngine.getNextValue() <= 74) {
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
//            }

            return new SearchNode(rngSimulationEngine.getSeed());
        }

        private boolean canAbolish(Rule rule) {
            return localRules.contains(rule);
        }

        private boolean canSpread(Rule rule) {
            return carriedRules.contains(rule) && !localRules.contains(rule);
        }
    }


    private static class EdgeGenerator implements Function<SearchNode, Collection<SearchEdge>> {
        @Override
        public Collection<SearchEdge> apply(SearchNode node) {
            return node.terminal ? emptyList() : SEARCH_EDGE_LIST;
        }
    }

//    public static void main(String[] args) {
//        boolean search = true;
//        int challengeCount = 0;
//        while (search) {
//            rngSimulationEngine.reset();
//
//            for (int i = 0; i < challengeCount; i++) {
//                challengeAndDecline();
//            }
//
//            Optional<RNGState> result = challengeAndGetResult();
//            if (result.isPresent() && result.get().abolish) {
//                search = false;
//            } else {
//                challengeCount++;
//            }
//        }
//        System.out.println();
//
//        rngSimulationEngine.reset();
//
//        challengeNpcMix(challengeCount);
//        printResult();
//    }
//
//    private static void challengeAndDecline() {
//        rngSimulationEngine.getNextValue();
//        rngSimulationEngine.getNextValue();
//        if (QUEEN_IN_REGION) {
//            rngSimulationEngine.getNextValue();
//        }
//    }
//

//
//    private static void printResult() {
//        Optional<RNGState> result = challengeAndGetResult();
//        if (result.isPresent()) {
//            System.out.println((result.get().abolish ? "Abolish " : "Spread ") + result.get().rule);
//        } else {
//            System.out.println("No effect");
//        }
//    }
//

//
//    private static void challengeNpcMix(int i) {
//        if (i == 0) return;
//        System.out.println("Challenging NPC that is mixing rules " + i + " times...");
//        for (int r = 0; r < i; r++) {
//            challengeAndDecline();
//        }
//    }

//    private static class RNGState {
//        final boolean abolish;
//        final Rule rule;
//
//        private RNGState(boolean abolish, Rule rule) {
//            this.abolish = abolish;
//            this.rule = rule;
//        }
//
//        @Override
//        public String toString() {
//            return new StringJoiner(", ", RNGState.class.getSimpleName() + "[", "]")
//                    .add("abolish=" + abolish)
//                    .add("rule=" + rule)
//                    .toString();
//        }
//    }

}
