package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.List;

public class Day1 {

    private static final String INPUT = "Day1Input.txt";
    private static int counter = 0;
    private final List<Integer> measurementList;

    public Day1() {
        measurementList = FileHelper.loadFileAsIntegerList(INPUT);
    }

    public int part1() {
        for (int i = 1; i < measurementList.size(); i++) {
            if (measurementList.get(i - 1) < measurementList.get(i)) counter++;
        }
        return counter;
    }

    public int part2() {
        for (int i = 3; i < measurementList.size(); i++) {
            int left = measurementList.get(i - 3) + measurementList.get(i - 2) + measurementList.get(i - 1);
            int right = measurementList.get(i - 2) + measurementList.get(i - 1) + measurementList.get(i);
            if (left < right) counter++;
        }
        return counter;
    }
}
