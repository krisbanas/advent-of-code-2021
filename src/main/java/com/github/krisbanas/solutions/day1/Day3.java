package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

    private final List<String> inputList;

    public Day3() {
        inputList = FileHelper.loadStringList("Day3Input.txt");
    }

    public int part1() {
        final var frequencyList = IntStream.range(0, inputList.get(0).length())
                .mapToObj(i -> inputList.stream()
                        .map(x -> x.charAt(i))
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                ).collect(Collectors.toList());

        final var gamma = new StringBuilder();
        final var epsilon = new StringBuilder();

        for (int i = 0; i < inputList.get(0).length(); i++) {
            if (frequencyList.get(i).get('1') > frequencyList.get(i).get('0')) {
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }

        final var gammaDecimal = Integer.parseInt(gamma.toString(), 2);
        final var epsilonDecimal = Integer.parseInt(epsilon.toString(), 2);

        return gammaDecimal * epsilonDecimal;
    }

    public int part2() {
        final var oxygen = getReadingValue(Gas.OXYGEN);
        final var carbon = getReadingValue(Gas.CARBON);

        final var oxygenDecimal = Integer.parseInt(oxygen, 2);
        final var carbonDecimal = Integer.parseInt(carbon, 2);

        return oxygenDecimal * carbonDecimal;
    }

    private String getReadingValue(Gas gas) {
        final var workingList = new ArrayList<>(inputList);
        for (int j = 0; j < workingList.get(0).length(); j++) {
            final var mostCommon = findMostCommonValue(workingList, j, gas);
            int finalJ = j;
            workingList.removeIf(x -> Integer.parseInt(String.valueOf(x.charAt(finalJ))) != (mostCommon));
            if (workingList.size() == 1) break;
        }
        return workingList.get(0);
    }

    private int findMostCommonValue(ArrayList<String> workingList, int index, Gas gas) {
        final var oneCounter = workingList.stream().mapToInt(entry -> Integer.parseInt(entry.substring(index, index + 1))).sum();
        if (oneCounter == workingList.size() / 2.) return gas.getMostCommon();
        return oneCounter > workingList.size() / 2 ? gas.getMostCommon() : gas.getLeastCommon();
    }

    private enum Gas {
        OXYGEN(1, 0), CARBON(0, 1);

        private final int mostCommon;

        private final int leastCommon;

        Gas(int mostCommon, int leastCommon) {
            this.mostCommon = mostCommon;
            this.leastCommon = leastCommon;
        }

        public int getMostCommon() {
            return mostCommon;
        }

        public int getLeastCommon() {
            return leastCommon;
        }
    }
}
