package com.scoreboard.exception;

public class SameTeamOnBothSidesException extends RuntimeException {
    public SameTeamOnBothSidesException() {
        super("Home team cannot be the same as away team");
    }
}
