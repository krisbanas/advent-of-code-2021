package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.List;

public class Day1 {

    private static final String INPUT = "Day1Input.txt";
    private static int counter = 0;
    private final List<Integer> measurementList;

    public Day1() {
        measurementList = FileHelper.loadIntegerList(INPUT);
        part1();
        part2();
    }

    public void part1() {
        for (int i = 1; i < measurementList.size(); i++) {
            if (measurementList.get(i - 1) < measurementList.get(i)) counter++;
        }
        System.out.println(counter);
    }

    public void part2() {
        counter = 0;
        for (int i = 3; i < measurementList.size(); i++) {
            int left = measurementList.get(i - 3);
            int right = measurementList.get(i);
            if (left < right) counter++;
        }
        System.out.println(counter);
    }
}
