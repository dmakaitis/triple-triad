package com.portkullis.tripletriad;

import com.portkullis.tripletriad.engine.BreadthFirstSearchEngine;
import com.portkullis.tripletriad.engine.RngSimulationEngine;
import com.portkullis.tripletriad.engine.model.Rule;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class RuleChangeSolver {

    // Draw formula for fully charged Ultima DP in Shumi Village: (20 * (rnd + 128) / 512) + 1
    private static final DepthPrinter depthListener = new DepthPrinter();
    private static final RngSimulationEngine rngSimulationEngine = RngSimulationEngine.getInstance();
    private static final BreadthFirstSearchEngine breadthFirstSearchEngine = BreadthFirstSearchEngine.getInstance(depthListener);

    private static final Region QUEEN_IN_REGION = Region.GALBADIA;

    private static final Map<Region, List<Rule>> REGION_RULES = Map.of(
            Region.BALAMB, asList(Rule.OPEN, Rule.SUDDEN_DEATH),
            Region.GALBADIA, asList(Rule.OPEN, Rule.SAME, Rule.PLUS),
            Region.TRABIA, asList(Rule.OPEN),
            Region.CENTRA, asList(Rule.OPEN),
            Region.DOLLET, asList(Rule.OPEN, Rule.SAME),
            Region.FH, asList(Rule.OPEN),
            Region.LUNAR, asList(Rule.OPEN, Rule.SUDDEN_DEATH, Rule.SAME, Rule.SAME_WALL, Rule.ELEMENTAL),
            Region.ESTHAR, asList(Rule.OPEN, Rule.ELEMENTAL)
    );

    private static final SearchEdge USE_DRAW_POINT = new UseDrawPoint();
    private static final SearchEdge CHALLENGE_THEN_REFUSE = new ChallengeThenRefuse();
    private static final SearchEdge CHALLENGE_THEN_ACCEPT = new ChallengeThenAccept();

    private static final List<SearchEdge> SEARCH_EDGE_LIST = unmodifiableList(asList(CHALLENGE_THEN_REFUSE, CHALLENGE_THEN_ACCEPT));

    public static void main(String[] args) {
        SearchNode rootNode = new SearchNode();
        EdgeGenerator edgeGenerator = new EdgeGenerator();
        Predicate<SearchNode> targetSpec = node -> node.terminal && node.abolish && node.rule != Rule.OPEN;

        List<SearchEdge> path = breadthFirstSearchEngine.findPath(rootNode, edgeGenerator, targetSpec, 21);

        depthListener.printUpdate();

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
        BALAMB(true),
        GALBADIA(true),
        TRABIA(true),
        CENTRA(true),
        DOLLET(true),
        FH(true),
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
        private final boolean almostDoneMixing;
        private final boolean terminal;
        private final boolean abolish;
        private final Rule rule;

        SearchNode() {
            this(null, null, false, false, 0, false, false, false, null);
        }

        SearchNode(Collection<Rule> carriedRules) {
            this(carriedRules, null, false, false, 0, false, false, false, null);
        }

        SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint) {
            this(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, 0, false, false, false, null);
        }

        private SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint, int seed) {
            this(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, seed, false, false, false, null);
        }

        private SearchNode(SearchNode oldNode, int seed, boolean terminal, boolean abolish, Rule rule) {
            this(oldNode.carriedRules, oldNode.regionRules, oldNode.queenInRegion, oldNode.regionHasSafeDrawPoint, seed, false, terminal, abolish, rule);
        }

        private SearchNode(Collection<Rule> carriedRules, Collection<Rule> regionRules, boolean queenInRegion, boolean regionHasSafeDrawPoint, int seed, boolean almostDoneMixing, boolean terminal, boolean abolish, Rule rule) {
            this.carriedRules = carriedRules;
            this.regionRules = regionRules;
            this.queenInRegion = queenInRegion;
            this.regionHasSafeDrawPoint = regionHasSafeDrawPoint;
            this.seed = seed;
            this.almostDoneMixing = almostDoneMixing;
            this.terminal = terminal;
            this.abolish = abolish;
            this.rule = rule;
        }

        public SearchNode carryRules(Collection<Rule> newCarriedRules) {
            return new SearchNode(newCarriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, seed, almostDoneMixing, terminal, abolish, rule);
        }

        public SearchNode advanceSeed(int advanceCount) {
            return setSeed(seed + advanceCount);
        }

        public SearchNode setRegion(int newSeed, Collection<Rule> newRegionRules, boolean queenInNewRegion, boolean newRegionHasSafeDrawPoint) {
            return new SearchNode(carriedRules, newRegionRules, queenInNewRegion, newRegionHasSafeDrawPoint, newSeed, almostDoneMixing, terminal, abolish, rule);
        }

        public SearchNode setSeed(int newSeed) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, almostDoneMixing, terminal, abolish, rule);
        }

        public SearchNode setSeedAlmostDoneMixing(int newSeed) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, true, almostDoneMixing, abolish, rule);
        }

        public SearchNode setSpreadRule(int newSeed, Rule rule) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, almostDoneMixing, true, false, rule);
        }

        public SearchNode setAbolishRule(int newSeed, Rule rule) {
            return new SearchNode(carriedRules, regionRules, queenInRegion, regionHasSafeDrawPoint, newSeed, almostDoneMixing, true, true, rule);
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

    static class ClaimRules implements SearchEdge {
        private final Region region;

        ClaimRules(Region region) {
            this.region = region;
        }

        @Override
        public String toString() {
            return "Carry rules from " + region;
        }

        @Override
        public SearchNode apply(SearchNode node) {
            return node.carryRules(REGION_RULES.get(region));
        }
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
            SearchNode newNode = node.setRegion(0, newRegionRules, QUEEN_IN_REGION == region, region.hasSafeDrawPoint);
            if (newNode.regionRules.containsAll(newNode.carriedRules)) {
                newNode = newNode.setSpreadRule(0, null);
            }
            return newNode;
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

    static class ChallengeThenRefuse implements SearchEdge {
        @Override
        public String toString() {
            return "Challenge, then refuse";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            rngSimulationEngine.setSeed(node.seed);
            boolean doneMixing = rngSimulationEngine.getNextDoneMixingFlag();
            int rnd2 = rngSimulationEngine.getNextValue();
            if (node.queenInRegion) {
                int rnd3 = rngSimulationEngine.getNextValue();
            }
            SearchNode newNode;
            if (doneMixing) {
                newNode = node.setSeedAlmostDoneMixing(rngSimulationEngine.getSeed());
            } else {
                newNode = node.setSeed(rngSimulationEngine.getSeed());
            }
            return newNode;
        }
    }

    static class ChallengeThenAccept extends ChallengeThenRefuse {
        @Override
        public String toString() {
            return "Challenge, then accept";
        }

        @Override
        public SearchNode apply(SearchNode node) {
            SearchNode challengeNode = super.apply(node);

            SearchNode result;
            if (challengeNode.terminal) {
                result = challengeNode;
            } else {
                rngSimulationEngine.setSeed(challengeNode.seed);
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
                if (rngSimulationEngine.getNextValue() <= 74) {
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
//                rngSimulationEngine.getNextValue();
                }

//                result = node.setSeed(rngSimulationEngine.getSeed());
                result = node.setSpreadRule(rngSimulationEngine.getSeed(), null);
            }
            return result;
        }

        private boolean canAbolish(SearchNode node, Rule rule) {
            return node.regionRules.contains(rule);
        }

        private boolean canSpread(SearchNode node, Rule rule) {
            return node.carriedRules.contains(rule) && !node.regionRules.contains(rule);
        }
    }

    private static class EdgeGenerator implements Function<SearchNode, Collection<SearchEdge>> {
        private final List<SearchEdge> searchListWithDraw;

        private EdgeGenerator() {
            List<SearchEdge> list = new ArrayList<>();
            list.add(USE_DRAW_POINT);
            list.addAll(SEARCH_EDGE_LIST);
            searchListWithDraw = unmodifiableList(list);
        }

        @Override
        public Collection<SearchEdge> apply(SearchNode node) {
            Collection<SearchEdge> edges;
            if (node.terminal) {
                edges = emptyList();
            } else {
                if (node.carriedRules == null) {
                    List<SearchEdge> newEdges = new ArrayList<>();
                    for (Region r : Region.values()) {
                        newEdges.add(new ClaimRules(r));
                    }
                    edges = unmodifiableList(newEdges);
                } else if (node.regionRules == null) {
                    List<SearchEdge> newEdges = new ArrayList<>();
                    for (Region r : Region.values()) {
                        if (!REGION_RULES.get(r).containsAll(node.carriedRules)) {
                            newEdges.add(new TravelTo(r));
                        }
                    }
                    edges = unmodifiableList(newEdges);
                } else {
                    if (node.regionHasSafeDrawPoint) {
                        edges = searchListWithDraw;
                    } else {
                        edges = SEARCH_EDGE_LIST;
                    }
                }
            }
            return edges;
        }
    }

    private static class DepthPrinter implements IntConsumer {

        private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.US);

        private final long startTime = new Date().getTime();
        private int lastDepth = Integer.MIN_VALUE;
        private long nodeCount = 0;

        @Override
        public void accept(int value) {
            ++nodeCount;
            if ((value > lastDepth) || (nodeCount % 10000000 == 0)) {
                lastDepth = value;
                printUpdate();
            }
        }

        public void printUpdate() {
            Date now = new Date();
            System.out.println(now + " (" + NUMBER_FORMAT.format(now.getTime() - startTime) + "ms) Search depth: " + lastDepth + ", Node count: " + NUMBER_FORMAT.format(nodeCount));
        }
    }
}
