package com.portkullis.tripletriad.resource;

import com.portkullis.tripletriad.resource.impl.RngTableLoaderLargeImpl;

import java.util.List;

/**
 * Loads a sequence of random bytes to simulate the random number generator
 * used by Final Fantasy VIII
 */
public interface RngTableLoader {

    /**
     * Loads the RNG table.
     *
     * @return the RNG table.
     */
    List<Integer> loadRngTable();

    /**
     * Returns an instance of the RNG table loader.
     *
     * @return an instance of the RNG table loader.
     */
    static RngTableLoader getInstance() {
        return new RngTableLoaderLargeImpl();
    }

}
