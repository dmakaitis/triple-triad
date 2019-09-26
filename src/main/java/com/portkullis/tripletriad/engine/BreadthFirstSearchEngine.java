package com.portkullis.tripletriad.engine;

import com.portkullis.tripletriad.engine.impl.BreadthFirstSearchEngineImpl;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface for an engine that performs a breadth-first search over a problem space.
 */
public interface BreadthFirstSearchEngine {

    /**
     * Attempts to find a path from the start node to a target node.
     *
     * @param startNode           the start node.
     * @param edgeProvider        a function that takes a node and returns a collection of edges that originate from that node.
     * @param targetSpecification a function that takes a node and returns true if that is the target node.
     * @param searchDepth         the maximum depth that will be search in the problem space.
     * @return the list of edges that connect the start node to the target node.
     */
    <N, E extends Function<N, N>> List<E> findPath(N startNode, Function<N, Collection<E>> edgeProvider, Predicate<N> targetSpecification, int searchDepth);

    /**
     * Returns an instance of the breadth first search engine.
     *
     * @return an instance of the breadth first search engine.
     */
    static BreadthFirstSearchEngine getInstance() {
        return new BreadthFirstSearchEngineImpl();
    }

}
