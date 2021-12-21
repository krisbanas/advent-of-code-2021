package com.github.krisbanas.solutions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Day21 {

    private final Map<State, long[]> memoTable = new HashMap<>();

    public Day21() {
        part1();
        part2();
    }

    public void part1() {
        Dice dice = new DeterministicDice();
        final var track = new Track(8, 6);
        while (true) {
            final var playerOneRoll = dice.roll();
            track.movePlayerOne(playerOneRoll);
            if (track.isGameWon()) break;

            final var playerTwoRoll = dice.roll();
            track.movePlayerTwo(playerTwoRoll);
            if (track.isGameWon()) break;
        }
        System.out.println("Part 1: " + Math.min(track.getPlayerOnePoints(), track.getPlayerTwoPoints()) * dice.getRollCount());
    }

    public void part2() {
        final var result = countWinFromState(new State(8, 6, 0, 0));
        System.out.println("Part 2: " + Math.max(result[0], result[1]));
    }

    // I did not figure it out by myself, I'm not smart enough. Based on solution by: https://github.com/jonathanpaulson
    private long[] countWinFromState(State state) {
        if (state.p1Points >= 21) return new long[]{1, 0};
        if (state.p2Points >= 21) return new long[]{0, 1};
        if (memoTable.containsKey(state)) return (memoTable.get(state));

        long[] resultForAll = new long[2];
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    var newPlayerOnePosition = (state.p1Position + i + j + k - 1) % 10 + 1;
                    var newPlayerOnePoints = state.p1Points + newPlayerOnePosition;

                    final var newState = new State(state.p2Position, newPlayerOnePosition, state.p2Points, newPlayerOnePoints);
                    long[] result = countWinFromState(newState);
                    resultForAll[0] = resultForAll[0] + result[1];
                    resultForAll[1] = resultForAll[1] + result[0];
                    memoTable.put(newState, result);
                }
            }
        }
        return resultForAll;
    }

    private record State(int p1Position, int p2Position, int p1Points, int p2Points) {
    }

    private interface Dice {
        int getRollCount();

        int roll();
    }

    private static class DeterministicDice implements Dice {

        private int rollCounter = 0;

        @Override
        public int getRollCount() {
            return rollCounter;
        }

        @Override
        public int roll() {
            return (++rollCounter % 100) + (++rollCounter % 100) + (++rollCounter % 100);
        }
    }

    @Getter
    private static class Track {

        private int playerOnePosition;
        private int playerTwoPosition;

        private int playerOnePoints;
        private int playerTwoPoints;

        public Track(int playerOneStart, int playerTwoStart) {
            this.playerOnePosition = playerOneStart;
            this.playerTwoPosition = playerTwoStart;
        }

        public void movePlayerOne(int fields) {
            playerOnePosition = (playerOnePosition + fields);
            playerOnePosition = (playerOnePosition - 1) % 10 + 1;
            playerOnePoints += playerOnePosition;
        }

        public void movePlayerTwo(int fields) {
            playerTwoPosition = (playerTwoPosition + fields);
            playerTwoPosition = (playerTwoPosition - 1) % 10 + 1;
            playerTwoPoints += playerTwoPosition;
        }

        public boolean isGameWon() {
            return playerOnePoints >= 1000 || playerTwoPoints >= 1000;
        }
    }
}