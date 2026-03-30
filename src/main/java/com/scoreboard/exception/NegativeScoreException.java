package com.scoreboard.exception;

public class NegativeScoreException extends RuntimeException {
    public NegativeScoreException(int score) {
        super(String.format("Score: %s is invalid. Please, provide a correct non-negative score", score));
    }
}
