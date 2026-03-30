package com.scoreboard.exception;

public class MatchNotFoundException extends RuntimeException {
    public MatchNotFoundException(String homeTeam, String awayTeam) {
        super(String.format("A match between %s and %s is not started", homeTeam, awayTeam));
    }
}
