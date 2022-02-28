package com.github.pareronia.usaco._663;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class SquarePastureTest extends TestBase<SquarePasture> {

    protected SquarePastureTest() {
        super(SquarePasture.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> 2;
    }
}
