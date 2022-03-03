package com.github.pareronia.usaco._568;

import java.util.function.Function;

import com.github.pareronia.usaco.TestBase;

class SpeedingTicketTest extends TestBase<SpeedingTicket> {

    protected SpeedingTicketTest() {
        super(SpeedingTicket.class);
    }

    @Override
    protected Function<int[], Integer> numberOfLinesPerTestCase() {
        return t -> t[0] + t[1] + 1;
    }
}
