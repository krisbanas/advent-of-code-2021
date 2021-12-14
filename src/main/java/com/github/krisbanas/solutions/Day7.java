package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7 {

    private final List<Integer> inputList;
    private final Map<Integer, Integer> distanceMap = new HashMap<>();

    public Day7() {
        inputList = FileHelper.loadStringSeparatedIntegers("Day7Input.txt");
    }

    public int part1() {
        var min = inputList.get(inputList.size() / 2 - 15);
        var max = inputList.get(inputList.size() / 2 + 15);

        for (int point = min; point < max; point++) {
            distanceMap.put(point, calculateDistance(point));
        }

        return distanceMap.values().stream().min(Integer::compareTo).get();
    }

    private Integer calculateDistance(int point) {
        return inputList.stream().mapToInt(x -> Math.abs(point - x)).sum();
    }


    public int part2() {
        for (int point = 0; point < inputList.size(); point++) {
            distanceMap.put(point, calculateDistance2(point));
        }

        return distanceMap.values().stream().min(Integer::compareTo).get();
    }

    private Integer calculateDistance2(int point) {
        return inputList.stream()
                .mapToInt(x -> countArithmetics(Math.abs(point - x)))
                .sum();
    }

    private int countArithmetics(int distance) {
        return ((distance+1) * distance) / 2;
    }
}
