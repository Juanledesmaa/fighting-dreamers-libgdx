package com.mygdx.game.Player;

public enum State {
    idle, walk, jumpUp, jumpFall, punch, land;

    public boolean ground () {
        return this == idle || this == walk || this == land;
    }

    public boolean air () {
        return !ground();
    }
}
