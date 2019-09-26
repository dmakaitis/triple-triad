package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.BreadthFirstSearchEngine;
import com.portkullis.tripletriad.engine.RngSimulationEngine;
import com.portkullis.tripletriad.engine.model.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class RuleChangeSolver {

    // Draw formula for fully charged Ultima DP in Shumi Village: (20 * (rnd + 128) / 512) + 1
    private static final RngSimulationEngine rngSimulationEngine = RngSimulationEngine.getInstance();
    private static final BreadthFirstSearchEngine breadthFirstSearchEngine = BreadthFirstSearchEngine.getInstance();

    private static final Region QUEEN_IN_REGION = Region.GALBADIA;

    private static final Map<Region, List<Rule>> REGION_RULES = Map.of(
            Region.BALAMB, asList(Rule.OPEN),
            Region.GALBADIA, asList(Rule.OPEN),
            Region.TRABIA, asList(Rule.OPEN),
            Region.CENTRA, asList(Rule.OPEN, Rule.PLUS),
            Region.DOLLET, asList(Rule.OPEN),
            Region.FH, asList(Rule.OPEN),
            Region.LUNAR, asList(Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL),
            Region.ESTHAR, asList(Rule.OPEN)
    );

    private static final List<Rule> CARRIED_RULES = REGION_RULES.get(Region.CENTRA);

    private static final SearchEdge USE_DRAW_POINT = new UseDrawPoint();
    private static final SearchEdge CHALLENGE_THEN_REFUSE = new ChallengeThenRefuse();
    private static final SearchEdge CHALLENGE_THEN_ACCEPT = new ChallengeThenAccept();

    private static final List<SearchEdge> SEARCH_EDGE_LIST = asList(CHALLENGE_THEN_REFUSE, CHALLENGE_THEN_ACCEPT);

    public static void main(String[] args) {
        SearchNode rootNode = new SearchNode(CARRIED_RULES);
        EdgeGenerator edgeGenerator = new EdgeGenerator();
        Predicate<SearchNode> targetSpec = node -> node.terminal && node.abolish && node.rule != Rule.OPEN;

        List<SearchEdge> path = breadthFirstSearchEngine.findPath(rootNode, edgeGenerator, targetSpec, 20);

        SearchNode node = rootNode;
        if (path == null) {
            System.out.println("No path found");
        } else {
            int count = 0;
            SearchEdge action = null;
            for (SearchEdge edge : path) {
                node = edge.apply(node);
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
        System.out.println("Result: " + (node.abolish ? "Abolish " : "Spread ") + node.rule);
    }

    public enum Region {
        BALAMB(false),
        GALBADIA(false),
        TRABIA(false),
        CENTRA(false),
        DOLLET(false),
        FH(false),
        LUNAR(false),
        ESTHAR(false);

        private final boolean hasSafeDrawPoint;

        Region(boolean hasSafeDrawPoint) {
            this.hasSafeDrawPoint = hasSafeDrawPoint;
        }
    }

    static class SearchNode {

        private final Collection<Rule> carriedRules;
        private final Collection<Rule> regionRules;
        private final boolean queenInRegion;
        private final boolean regionHasSafeDrawPoint;
        private final int seed;
        private final boolean terminal;
        private final boolean abolish;
        private final Rule rule;

        SearchNode() {
            this(null, null, false, false, 0, false, false, null);
        }

        SearchNode(Collection<Rule> carriedRules) {
            this(carriedRules, null, false, false, 0, false, false, null);
        }

        SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint) {
            this(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, 0, false, false, null);
        }

        private SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint, int seed) {
            this(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, seed, false, false, null);
        }

        private SearchNode(SearchNode oldNode, int seed, boolean terminal, boolean abolish, Rule rule) {
            this(oldNode.carriedRules, oldNode.regionRules, oldNode.queenInRegion, oldNode.regionHasSafeDrawPoint, seed, terminal, abolish, rule);
        }

        private SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint, int seed, boolean terminal, boolean abolish, Rule rule) {
            this.carriedRules = carriedRules;
            this.regionRules = regionRules;
            this.queenInRegion = queenInRegion;
            this.regionHasSafeDrawPoint = regionHasSafeDrawPoint;
            this.seed = seed;
            this.terminal = terminal;
            this.abolish = abolish;
            this.rule = rule;
        }

        public SearchNode advanceSeed(int advanceCount) {
            return setSeed(seed + advanceCount);
        }

        public SearchNode setRegion(int newSeed, Collection<Rule> newRegionRules, boolean queenInNewRegion, boolean newRegionHasSafeDrawPoint) {
            return new SearchNode(carriedRules, newRegionRules, queenInNewRegion, newRegionHasSafeDrawPoint, newSeed, terminal, abolish, rule);
        }

        public SearchNode setSeed(int newSeed) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, terminal, abolish, rule);
        }

        public SearchNode setSpreadRule(int newSeed, Rule rule) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, true, false, rule);
        }

        public SearchNode setAbolishRule(int newSeed, Rule rule) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, true, true, rule);
        }

        public Collection<Rule> getCarriedRules() {
            return carriedRules;
        }

        public Collection<Rule> getRegionRules() {
            return regionRules;
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

    static class TravelTo implements SearchEdge {
        private final Region region;

        TravelTo(Region region) {
            this.region = region;
        }

        @Override
        public String toString() {
            return "Travel to " + region;
        }

        @Override
        public SearchNode apply(SearchNode node) {
            Collection<Rule> newRegionRules = REGION_RULES.get(region);
            return node.setRegion(0, newRegionRules, QUEEN_IN_REGION == region, false);
        }
    }

    static class ChallengeThenRefuse implements SearchEdge {
        @Override
        public String toString() {
            return "Challenge, then refuse";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            return node.advanceSeed(node.queenInRegion ? 3 : 2);
        }
    }

    static class UseDrawPoint implements SearchEdge {
        @Override
        public String toString() {
            return "Use draw point";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            return node.advanceSeed(1);
        }
    }

    static class ChallengeThenAccept implements SearchEdge {
        @Override
        public String toString() {
            return "Challenge, then accept";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            rngSimulationEngine.setSeed(node.seed + (node.queenInRegion ? 3 : 2));
            Rule rule = null;
            for (int i = 0; i < 3; i++) {
                rule = rngSimulationEngine.getNextRule();
                if (canSpread(node, rule)) {
                    return node.setSpreadRule(rngSimulationEngine.getSeed(), rule);
                }
            }
            if (rngSimulationEngine.getNextAbolishFlag() && canAbolish(node, rule)) {
                return node.setAbolishRule(rngSimulationEngine.getSeed(), rule);
            }

            // Extra stuff according to ForteGSOmega
//            if (rngSimulationEngine.getNextValue() <= 74) {
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
//            }

//            return node.setSeed(rngSimulationEngine.getSeed());
            return node.setSpreadRule(rngSimulationEngine.getSeed(), null);
        }

        private boolean canAbolish(SearchNode node, Rule rule) {
            return node.regionRules.contains(rule);
        }

        private boolean canSpread(SearchNode node, Rule rule) {
            return node.carriedRules.contains(rule) && !node.regionRules.contains(rule);
        }
    }


    private static class EdgeGenerator implements Function<SearchNode, Collection<SearchEdge>> {
        @Override
        public Collection<SearchEdge> apply(SearchNode node) {
            Collection<SearchEdge> edges;
            if (node.regionRules == null) {
                List<SearchEdge> newEdges = new ArrayList<>();
                for (Region r : Region.values()) {
                    if (!REGION_RULES.get(r).containsAll(node.carriedRules)) {
                        newEdges.add(new TravelTo(r));
                    }
                }
                edges = unmodifiableList(newEdges);
            } else {
                List<SearchEdge> newEdges = new ArrayList<>();
                if (node.regionHasSafeDrawPoint) {
                    newEdges.add(USE_DRAW_POINT);
                }
                newEdges.addAll(node.terminal ? emptyList() : SEARCH_EDGE_LIST);
                edges = unmodifiableList(newEdges);
            }
            return edges;
        }
    }

}
