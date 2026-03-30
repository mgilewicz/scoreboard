package com.scoreboard;

import com.scoreboard.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class ScoreBoardTest {
    private ScoreBoard board;

    @BeforeEach
    void setUp() {
        board = new ScoreBoardImpl();
    }

    @Test
    void startGame_returnsMatchWithInitialScore() {
        Match match = board.startGame("Mexico", "Canada");
        assertThat(match.homeTeam()).isEqualTo("Mexico");
        assertThat(match.homeScore()).isZero();
        assertThat(match.awayTeam()).isEqualTo("Canada");
        assertThat(match.awayScore()).isZero();
    }

    @Test
    void startGame_makesMatchAppearInSummary() {
        board.startGame("Mexico", "Canada");

        assertThat(board.getSummary())
                .hasSize(1)
                .extracting(Match::homeTeam, Match::awayTeam, Match::homeScore, Match::awayScore)
                .containsExactly(tuple("Mexico", "Canada", 0, 0));
    }

    @Test
    void startGame_allowsMatchesBetweenDifferentTeams() {
        board.startGame("Mexico", "Canada");
        board.startGame("Germany", "France");

        assertThat(board.getSummary()).hasSize(2);
    }

    @Test
    void startGame_doesNotAllowMatch_sameTeamOnBothSides() {
        assertThatExceptionOfType(SameTeamOnBothSidesException.class).isThrownBy(() -> board.startGame("Mexico", "Mexico"));
    }

    @Test
    void startGame_doesNotAllowMatch_blankHomeTeam() {
        assertThatExceptionOfType(TeamNameInvalidException.class).isThrownBy(() -> board.startGame("", "Canada"));
    }

    @Test
    void startGame_doesNotAllowMatch_blankAwayTeam() {
        assertThatExceptionOfType(TeamNameInvalidException.class).isThrownBy(() -> board.startGame("Mexico", ""));
    }

    @Test
    void startGame_doesNotAllowMatch_homeTeamAlreadyAtPlay() {
        board.startGame("Mexico", "Canada");
        assertThatExceptionOfType(TeamAlreadyAtPlayException.class).isThrownBy(() -> board.startGame("Mexico", "Germany"));
    }

    @Test
    void startGame_doesNotAllowMatch_awayTeamAlreadyAtPlay() {
        board.startGame("Mexico", "Canada");
        assertThatExceptionOfType(TeamAlreadyAtPlayException.class).isThrownBy(() -> board.startGame("Canada", "Germany"));
    }

    @Test
    void startGame_doesNotAllowMatch_bothTeamsAlreadyAtPlay() {
        board.startGame("Mexico", "Canada");
        assertThatExceptionOfType(TeamAlreadyAtPlayException.class).isThrownBy(() -> board.startGame("Mexico", "Canada"));
    }

    @Test
    void finishGame_removesMatchFromBoard() {
        board.startGame("Mexico", "Canada");
        board.finishGame("Mexico", "Canada");

        assertThat(board.getSummary()).hasSize(0);
    }

    @Test
    void finishGame_matchNotFound_throwsAnException() {
        assertThatExceptionOfType(MatchNotFoundException.class).isThrownBy(() -> board.finishGame("Mexico", "Canada"));
    }

    @Test
    void updateScore_updateScoresCorrectly() {
        board.startGame("Mexico", "Canada");
        // our scout made a mistake
        board.updateScore("Mexico", "Canada", 11, 11);
        // our scout corrects the mistake by providing new absolute scores
        Match updatedMatch = board.updateScore("Mexico", "Canada", 5, 3);

        assertThat(updatedMatch.homeScore()).isEqualTo(5);
        assertThat(updatedMatch.awayScore()).isEqualTo(10);
        assertThat(board.getSummary())
                .hasSize(1)
                .extracting(Match::homeTeam, Match::awayTeam, Match::homeScore, Match::awayScore)
                .containsExactly(tuple("Mexico", "Canada", 5, 10));
    }

    @Test
    void updateScore_matchNotFound_throwsAnException() {
        assertThatExceptionOfType(MatchNotFoundException.class).isThrownBy(() -> board.updateScore("Mexico", "Canada", 0, 0));
    }

    @Test
    void updateScore_negativeHomeScore_throwsAnException() {
        assertThatExceptionOfType(NegativeScoreException.class).isThrownBy(() -> board.updateScore("Mexico", "Canada", -1, 0));
    }

    @Test
    void updateScore_negativeAwayScore_throwsAnException() {
        assertThatExceptionOfType(NegativeScoreException.class).isThrownBy(() -> board.updateScore("Mexico", "Canada", 0, -1));
    }

    @Test
    void getSummary_emptyBoard() {
        assertThat(board.getSummary()).isEmpty();
    }

    @Test
    void getSummary_orderedByTotalScoreDescThenByStartingTimeDesc() {
        board.startGame("Mexico", "Canada");
        board.startGame("Spain", "Brazil");
        board.startGame("Germany", "France");
        board.startGame("Uruguay", "Italy");
        board.startGame("Argentina", "Australia");

        board.updateScore("Mexico", "Canada", 0, 5);
        board.updateScore("Spain", "Brazil", 10, 2);
        board.updateScore("Germany", "France", 2, 2);
        board.updateScore("Uruguay", "Italy", 6, 6);
        board.updateScore("Argentina", "Australia", 3, 1);

        assertThat(board.getSummary())
                .hasSize(5)
                .extracting(Match::homeTeam, Match::awayTeam, Match::homeScore, Match::awayScore)
                .containsExactly(
                        tuple("Uruguay", "Italy", 6, 6), // Same score as Spain vs Brazil however this match was added most recently
                        tuple("Spain", "Brazil", 10, 2),
                        tuple("Mexico", "Canada", 0, 5),
                        tuple("Argentina", "Australia", 3, 1),
                        tuple("Germany", "France", 2, 2)
                );
    }
}
