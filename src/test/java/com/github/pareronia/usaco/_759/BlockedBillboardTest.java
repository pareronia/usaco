package com.github.pareronia.usaco._759;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class BlockedBillboardTest extends TestBase<BlockedBillboard> {

	protected BlockedBillboardTest() {
		super(BlockedBillboard.class);
	}

	@Override
	protected Function<int[], Integer> numberOfLinesPerTestCase() {
		return t -> 3;
	}
}
