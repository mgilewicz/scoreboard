package com.scoreboard;

import com.scoreboard.exception.SameTeamOnBothSidesException;
import com.scoreboard.exception.TeamAlreadyAtPlayException;
import com.scoreboard.exception.TeamNameInvalidException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScoreBoardImpl implements ScoreBoard {
    private record MatchKey(String homeTeam, String awayTeam) {}

    private final Map<MatchKey, Match> matches = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Match startGame(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isBlank())
            throw new TeamNameInvalidException(homeTeam);
        if (awayTeam == null || awayTeam.isBlank())
            throw new TeamNameInvalidException(awayTeam);
        if (homeTeam.equals(awayTeam)) throw new SameTeamOnBothSidesException();

        Set<String> activeTeams = matches.values().stream()
                .flatMap(match -> Stream.of(match.homeTeam(), match.awayTeam()))
                .collect(Collectors.toSet());

        if (activeTeams.contains(homeTeam)) throw new TeamAlreadyAtPlayException(homeTeam);
        if (activeTeams.contains(awayTeam)) throw new TeamAlreadyAtPlayException(awayTeam);

        Match match = new Match(homeTeam, awayTeam, 0, 0, sequence++);
        matches.put(new MatchKey(homeTeam, awayTeam), match);

        return match;
    }

    @Override
    public void finishGame(String homeTeam, String awayTeam) {

    }

    @Override
    public Match updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        return null;
    }

    @Override
    public List<Match> getSummary() {
        return matches.values().stream()
                .sorted(Comparator.comparingInt(Match::totalScore)
                        .thenComparing(Match::sequence)
                        .reversed())
                .toList();
    }
}
