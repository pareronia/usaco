package com.github.pareronia.usaco._1229;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class AlchemyTest extends TestBase<Alchemy> {

    protected AlchemyTest() {
        super(Alchemy.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> t[0] + 1 + 2;
    }
}
