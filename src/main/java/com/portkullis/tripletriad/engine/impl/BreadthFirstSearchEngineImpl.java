package com.portkullis.tripletriad.engine.impl;

import com.portkullis.tripletriad.engine.BreadthFirstSearchEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

public class BreadthFirstSearchEngineImpl implements BreadthFirstSearchEngine {

    private final IntConsumer depthListener;

    public BreadthFirstSearchEngineImpl(IntConsumer depthListener) {
        this.depthListener = depthListener;
    }

    @Override
    public <N, E extends Function<N, N>> List<E> findPath(N startNode, Function<N, Collection<E>> edgeProvider, Predicate<N> targetSpecification, int searchDepth) {
        Deque<Branch<N, E>> searchBranches = new LinkedList<>();
        searchBranches.add(new Branch<>(startNode));
        Branch result = null;

        while (!searchBranches.isEmpty()) {
            Branch<N, E> branch = searchBranches.poll();
            depthListener.accept(branch.size);
            if (targetSpecification.test(branch.endNode)) {
                result = branch;
                searchBranches.clear();
            } else {
                if (branch.size < searchDepth) {
                    Collection<E> newEdges = edgeProvider.apply(branch.endNode);
                    for (E edge : newEdges) {
                        N newNode = edge.apply(branch.endNode);
                        searchBranches.add(new Branch<>(branch, edge, newNode));
                    }
                }
            }
        }

        return result == null ? null : result.toList();
    }

    private static class Branch<N, E> {

        private final N endNode;
        private final Branch<N, E> previous;
        private final E lastEdge;
        private final int size;

        private Branch(N endNode) {
            this(null, null, endNode);
        }

        private Branch(Branch<N, E> previous, E lastEdge, N endNode) {
            this.endNode = endNode;
            this.previous = previous;
            this.lastEdge = lastEdge;
            this.size = previous == null ? 0 : previous.size + 1;
        }

        private List<E> toList() {
            List<E> previousPath;
            if (previous == null) {
                previousPath = new ArrayList<>();
            } else {
                previousPath = previous.toList();
            }
            if (lastEdge != null) {
                previousPath.add(lastEdge);
            }
            return previousPath;
        }
    }

}
