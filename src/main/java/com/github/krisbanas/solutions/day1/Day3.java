package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 {

    private static final String INPUT = "Day3Input.txt";
    private final List<String> inputList;

    public Day3() {
        inputList = FileHelper.loadFileAsStringList(INPUT);
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

        final var gammaDec = Integer.parseInt(gamma.toString(), 2);
        final var epsilonDec = Integer.parseInt(epsilon.toString(), 2);

        return gammaDec * epsilonDec;
    }

    public int part2() {
        var oxygen = "";
        var co2 = "";

        oxygen += getOxy(true);
        co2 += getOxy(false);

        final var oxy = Integer.parseInt(oxygen, 2);
        final var co = Integer.parseInt(co2, 2);

        return oxy * co;
    }

    private String getOxy(boolean most) {
        int oneCounter;
        int mostCommon;
        final var workingList = new ArrayList<>(inputList);
        for (int j = 0; j < workingList.get(0).length(); j++) {
            oneCounter = 0;
            for (final var entry : workingList) {
                oneCounter += Integer.parseInt(entry.substring(j, j + 1));
            }
            if (most) {
                if (oneCounter == workingList.size() / 2.) mostCommon = 1;
                else {
                    mostCommon = oneCounter > workingList.size() / 2 ? 1 : 0;
                }
            } else {
                if (oneCounter == workingList.size() / 2.) mostCommon = 0;
                else {
                    mostCommon = oneCounter < workingList.size() / 2. ? 1 : 0;
                }
            }
            int finalJ = j;
            int finalMostCommon = mostCommon;
            workingList.removeIf(x -> Integer.parseInt(x.substring(finalJ, finalJ + 1)) != (finalMostCommon));
            if (workingList.size() == 1) break;
        }
        return workingList.get(0);
    }
}
