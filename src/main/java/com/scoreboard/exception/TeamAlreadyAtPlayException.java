package com.scoreboard.exception;

import java.util.List;

public class TeamAlreadyAtPlayException extends RuntimeException {
    public TeamAlreadyAtPlayException(List<String> teamNames) {
        super(String.format("Team/Teams %s already at play", teamNames));
    }
}
