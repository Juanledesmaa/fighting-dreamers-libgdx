package com.mygdx.game.Player;

public enum State {
    idle, walk, jumpUp, jumpFall;

    public boolean ground () {
        return this == idle || this == walk;
    }

    public boolean air () {
        return !ground();
    }
}
