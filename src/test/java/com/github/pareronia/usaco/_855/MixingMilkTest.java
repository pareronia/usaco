package com.github.pareronia.usaco._855;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class MixingMilkTest extends TestBase<MixingMilk> {

    protected MixingMilkTest() {
        super(MixingMilk.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> 3;
    }

    @Override
    protected int numberOfOutputLinesPerTestCase() {
        return 3;
    }
}
