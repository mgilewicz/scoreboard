package com.scoreboard.exception;

public class TeamNameInvalidException extends RuntimeException {
    public TeamNameInvalidException(String teamName) {
        super(String.format("Team name: %s is invalid", teamName));
    }
}
