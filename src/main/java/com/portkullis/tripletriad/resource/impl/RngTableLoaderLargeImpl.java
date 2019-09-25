package com.portkullis.tripletriad.resource.impl;

import com.portkullis.tripletriad.RuleChangeSolver;
import com.portkullis.tripletriad.resource.RngTableLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RngTableLoaderLargeImpl implements RngTableLoader {

    @Override
    public List<Integer> loadRngTable() {
        List<Integer> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(RuleChangeSolver.class.getResourceAsStream("/ySbddd9a.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                values.add(Integer.parseInt(line.substring(7, 10).trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load RNG table", e);
        }
        return values;
    }

}
