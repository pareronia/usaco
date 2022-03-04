package com.github.pareronia.usaco._1205;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class BlocksTest extends TestBase<Blocks> {

    protected BlocksTest() {
        super(Blocks.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> t[0] + 5;
    }
}
