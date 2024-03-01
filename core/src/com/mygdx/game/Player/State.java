package com.mygdx.game.Player;

public enum State {
    idle, walk, jumpUp, jumpFall, punch, land;

    public enum punchCombo {
        NONE, // No combo
        PUNCH_1,
        PUNCH_2,
        PUNCH_3
    }

    public boolean ground () {
        return this == idle || this == walk || this == land;
    }

    public boolean air () {
        return !ground();
    }
}
