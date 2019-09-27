package com.portkullis.tripletriad.engine.impl;

import com.portkullis.tripletriad.engine.BreadthFirstSearchEngine;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;

public class BreadthFirstSearchEngineImpl implements BreadthFirstSearchEngine {

    @Override
    public <N, E extends Function<N, N>> List<E> findPath(N startNode, Function<N, Collection<E>> edgeProvider, Predicate<N> targetSpecification, int searchDepth) {
        Deque<Branch<N, E>> searchBranches = new LinkedList<>();
        searchBranches.add(new Branch<>(startNode, emptyList()));
        Branch result = null;

        while (!searchBranches.isEmpty()) {
            Branch<N, E> branch = searchBranches.poll();
            if (targetSpecification.test(branch.endNode)) {
                result = branch;
                searchBranches.clear();
            } else {
                if (branch.path.size() < searchDepth) {
                    Collection<E> newEdges = edgeProvider.apply(branch.endNode);
                    for (E edge : newEdges) {
                        List<E> newPath = new ArrayList<>(branch.path);
                        newPath.add(edge);
                        N newNode = edge.apply(branch.endNode);
                        searchBranches.add(new Branch<>(newNode, newPath));
                    }
                }
            }
        }

        return result == null ? null : result.path;
    }

    private static class Branch<N, E> {

        private final N endNode;
        private final List<E> path;

        private Branch(N endNode, List<E> path) {
            this.endNode = endNode;
            this.path = path;
        }

    }

}
