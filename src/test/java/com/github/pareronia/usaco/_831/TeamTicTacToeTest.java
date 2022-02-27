package com.github.pareronia.usaco._831;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class TeamTicTacToeTest extends TestBase<TeamTicTacToe> {

    protected TeamTicTacToeTest() {
        super(TeamTicTacToe.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> 3;
    }

    @Override
    protected int numberOfOutputLinesPerTestCase() {
        return 2;
    }
}
