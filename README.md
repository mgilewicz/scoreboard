# Football World Cup Score Board

A simple library for Live Football World Cup Score Board that shows matches and scores.

## Implementation plan
Note: each commit will be pushed directly to master branch for the sake of simplicity.

1. Initial commit with project + java setup & simple github workflow to run tests on commit.
2. TDD: provide the interface and any simple data structures required to define the contract 
followed by the test suite demonstrating the intended behavior.
3. Implement simple, in-memory version of the score board.
4. Improve the implementation to be concurrency-safe, proven by a simple concurrency unit test.

## Design decisions & assumptions
### Business assumptions
1. Score updates accept absolute values instead of incremental ones.
2. Match cannot be started when home team equals away team.
3. Match cannot be started if home or away team names are invalid (e.g. blank).
4. Match cannot be started when home or/and away team are already playing.
5. Attempt to finish a non-existent match throws an exception.
6. Attempt to update a non-existent match throws an exception.
7. Attempt to update a match with negative score throws an exception.
8. Match total score is calculated as `homeScore + awayScore`.

### Technical decisions
1. For the sake of simplicity `updateScore` function accepts `homeTeam` and `awayTeam` Strings. In the production scenario I'd suggest using an idempotent `matchId`.
