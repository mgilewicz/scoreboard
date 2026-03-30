package com.scoreboard;

import com.scoreboard.exception.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScoreBoardImpl implements ScoreBoard {
    private record MatchKey(String homeTeam, String awayTeam) {
    }

    private final ConcurrentHashMap<MatchKey, Match> matches = new ConcurrentHashMap<>();
    private final AtomicLong sequenceGenerator = new AtomicLong(0);
    private final Object startFinishMonitor = new Object();

    @Override
    public Match startGame(String homeTeam, String awayTeam) {
        if (homeTeam == null || homeTeam.isBlank())
            throw new TeamNameInvalidException(homeTeam);
        if (awayTeam == null || awayTeam.isBlank())
            throw new TeamNameInvalidException(awayTeam);
        if (homeTeam.equals(awayTeam)) throw new SameTeamOnBothSidesException();

        synchronized (startFinishMonitor) {
            Set<String> activeTeams = matches.values().stream()
                    .flatMap(match -> Stream.of(match.homeTeam(), match.awayTeam()))
                    .collect(Collectors.toSet());

            if (activeTeams.contains(homeTeam)) throw new TeamAlreadyAtPlayException(homeTeam);
            if (activeTeams.contains(awayTeam)) throw new TeamAlreadyAtPlayException(awayTeam);

            Match match = new Match(homeTeam, awayTeam, 0, 0, sequenceGenerator.getAndIncrement());
            matches.put(new MatchKey(homeTeam, awayTeam), match);

            return match;
        }
    }

    @Override
    public void finishGame(String homeTeam, String awayTeam) {
        synchronized (startFinishMonitor) {
            Match removed = matches.remove(new MatchKey(homeTeam, awayTeam));

            if (removed == null) throw new MatchNotFoundException(homeTeam, awayTeam);
        }
    }

    @Override
    public Match updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        if (homeScore < 0) throw new NegativeScoreException(homeScore);
        if (awayScore < 0) throw new NegativeScoreException(awayScore);
        MatchKey matchKey = new MatchKey(homeTeam, awayTeam);

        return matches.compute(matchKey, (key, existingMatch) -> {
                    if (existingMatch == null) throw new MatchNotFoundException(homeTeam, awayTeam);
                    return new Match(existingMatch.homeTeam(), existingMatch.awayTeam(), homeScore, awayScore, existingMatch.sequence());
                }
        );
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
