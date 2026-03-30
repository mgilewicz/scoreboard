package com.scoreboard;

/**
 * Represents a live football match state
 * @param sequence used only for summary ordering. Ideally it should not be mapped to a dto returned by the public API
 */
public record Match(
        String homeTeam,
        String awayTeam,
        int homeScore,
        int awayScore,
        long sequence
) {
}
