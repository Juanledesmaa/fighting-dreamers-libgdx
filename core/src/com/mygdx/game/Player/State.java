package com.mygdx.game.Player;

public enum State {
    idle, walk, jumpUp, jumpFall, punch;

    public boolean ground () {
        return this == idle || this == walk;
    }

    public boolean air () {
        return !ground();
    }
}
