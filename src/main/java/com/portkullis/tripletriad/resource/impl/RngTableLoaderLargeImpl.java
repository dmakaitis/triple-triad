package com.portkullis.tripletriad.resource.impl;

import com.portkullis.tripletriad.RuleChangeSolver;
import com.portkullis.tripletriad.resource.RngTableLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RngTableLoaderLargeImpl implements RngTableLoader {

    // Next seed = (1103515245 * seed + 12345) % 4294967296
    // random value = ((seed / 65535) % 32768) % 256)

    @Override
    public List<Integer> loadRngTable() {
        List<Integer> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(RuleChangeSolver.class.getResourceAsStream("/ySbddd9a.txt")))) {
//            long seed = 1;
            String line;
            while ((line = br.readLine()) != null) {
//                seed = (1103515245L * seed + 12345L) & 0xffffffffL;
//                int value = (int)((seed / 65536) & 0x000000ff);
                values.add(Integer.parseInt(line.substring(7, 10).trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load RNG table", e);
        }
        return values;
    }

}
