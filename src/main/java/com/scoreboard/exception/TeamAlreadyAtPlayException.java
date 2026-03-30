package com.scoreboard.exception;

public class TeamAlreadyAtPlayException extends RuntimeException {
    public TeamAlreadyAtPlayException(String teamName) {
        super(String.format("Team %s already at play", teamName));
    }
}
