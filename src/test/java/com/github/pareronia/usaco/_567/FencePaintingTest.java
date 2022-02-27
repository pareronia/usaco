package com.github.pareronia.usaco._567;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class FencePaintingTest extends TestBase<FencePainting> {

    protected FencePaintingTest() {
        super(FencePainting.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> 2;
    }
}
