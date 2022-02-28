package com.github.pareronia.usaco._783;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class BlockedBillboardIITest extends TestBase<BlockedBillboardII> {

    protected BlockedBillboardIITest() {
        super(BlockedBillboardII.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> 2;
    }
}
