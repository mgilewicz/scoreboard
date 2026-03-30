package com.scoreboard;

import java.util.List;

public interface ScoreBoard {
    /**
     *  When a game starts, it should capture (being initial score 0-0)
     * @throws com.scoreboard.exception.TeamNameInvalidException when team name is blank
     * @throws com.scoreboard.exception.TeamAlreadyAtPlayException when team is already registered in the game board
     * @throws com.scoreboard.exception.SameTeamOnBothSidesException when home team equals away team
     */
    Match startGame(String homeTeam, String awayTeam);

    /**
     * It will remove a match from the scoreboard.
     * @throws com.scoreboard.exception.MatchNotFoundException when match between given homeTeam and awayTeam is not found
     */
    void finishGame(String homeTeam, String awayTeam);

    /**
     * Updates a game score (absolute scores).
     */
    Match updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);

    /**
     * Get a summary of games by total score. Those games with the same total score
     * will be returned ordered by the most recently added to our system.
     */
    List<Match> getSummary();
}
