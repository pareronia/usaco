package com.github.pareronia.usaco._572;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class BreedCountingTest extends TestBase<BreedCounting> {

    protected BreedCountingTest() {
        super(BreedCounting.class);
    }

	@Override
	protected Function<int[], Integer> numberOfLinesPerTestCase() {
		return t -> t[0] + t[1] + 1;
	}
}
