package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 {

    private final List<Integer> numbersCalled;
    private final List<Board> boards;

    public Day4() {
        List<String> inputList = FileHelper.loadFileAsStringList("Day4Input.txt");
        numbersCalled = Arrays.stream(inputList.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        final var boardsTmp = Arrays.stream(FileHelper.loadFileAsString("Day4Input.txt").split("\r\n\r\n")).toList();

        List<List<List<Integer>>> boardsInt = new ArrayList<>();
        for (int j = 1; j < boardsTmp.size(); j++) {
            String entry = boardsTmp.get(j);
            final var array = entry.split("\r\n");
            List<List<Integer>> numList = new ArrayList<>();

            for (final String arrayEntry : array) {
                List<Integer> tmp = Arrays.stream(arrayEntry.split(" ")).filter(x -> !x.isBlank()).map(Integer::valueOf).toList();
                numList.add(tmp);
            }

            boardsInt.add(numList);
        }

        boards = boardsInt.stream().map(Board::new).toList();
    }

    public int part1() {
        for (final Integer numberCalled : numbersCalled) {
            boards.forEach(x -> x.markCalledNumber(numberCalled));
            int bingoVal = boards.stream()
                    .filter(Board::isBingo)
                    .map(board -> board.calculateBingoVal(numberCalled))
                    .findAny()
                    .orElse(Integer.MIN_VALUE);
            if (bingoVal != Integer.MIN_VALUE) return bingoVal;
        }
        return 0;
    }

    public int part2() {
        boolean[] won = new boolean[boards.size()];
        for (final Integer numberCalled : numbersCalled) {
            boards.forEach(x -> x.markCalledNumber(numberCalled));
            for (int i = 0; i < boards.size(); i++) {
                final var board = boards.get(i);
                if (board.isBingo()) won[i] = true;
                if (allWon(won)) return board.calculateBingoVal(numberCalled);
            }
        }
        return 0;
    }

    private boolean allWon(boolean[] won) {
        for (boolean b : won) {
            if (!b) return false;
        }
        return true;
    }

    private static class Board {

        private final int[][] boardValues = new int[5][];
        private final boolean[][] marked = new boolean[5][];

        public Board(List<List<Integer>> boardValues) {
            for (int i = 0; i < 5; i++) {
                final var intArray = boardValues.get(i);
                int[] newArray = new int[5];
                for (int j = 0; j < intArray.size(); j++) {
                    newArray[j] = intArray.get(j);
                }
                this.boardValues[i] = newArray;
            }
            for (int i = 0; i < 5; i++) {
                marked[i] = new boolean[5];
            }
        }

        public void markCalledNumber(int num) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (num == boardValues[i][j]) marked[i][j] = true;
                }
            }
        }

        public boolean isBingo() {
            // check rows
            for (int i = 0; i < 5; i++) {
                if (marked[i][0] && marked[i][1] && marked[i][2] && marked[i][3] && marked[i][4]) return true;
                if (marked[0][i] && marked[1][i] && marked[2][i] && marked[3][i] && marked[4][i]) return true;
            }
            return false;
        }

        public int calculateBingoVal(int numberCalled) {
            int counter = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!marked[i][j]) counter += boardValues[i][j];
                }
            }
            return counter * numberCalled;
        }
    }
}
