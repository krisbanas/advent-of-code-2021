package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

    List<Vent> inputList;
    int[][] marker = new int[1000][];

    public Day5() {
        inputList = FileHelper.loadFileAsStringList("Day5Input.txt").stream()
                .map(Vent::new)
                .toList();
        for (int i = 0; i < 1000; i++) {
            marker[i] = new int[1000];
        }
    }

    public int part1() {
        var straightList = inputList.stream().filter(Vent::isStraight).collect(Collectors.toList());
        processStraight(straightList);
        return countCollisions();
    }

    public int part2() {
        var straightList = inputList.stream().filter(Vent::isStraight).collect(Collectors.toList());
        var nonStraightList = inputList.stream().filter(x -> !x.isStraight()).collect(Collectors.toList());
        processStraight(straightList);
        processNonStraight(nonStraightList);
        return countCollisions();
    }

    private void processStraight(List<Vent> straightList) {
        for (var entry : straightList) {
            if (entry.startX == entry.endX) {
                var pointer = entry.startY;
                while (pointer != entry.endY) {
                    marker[entry.startX][pointer]++;
                    if (entry.startY < entry.endY) pointer++;
                    else pointer--;
                }
                marker[entry.startX][entry.endY]++;
            } else {
                var pointer = entry.startX;
                while (pointer != entry.endX) {
                    marker[pointer][entry.startY]++;
                    if (entry.startX < entry.endX) pointer++;
                    else pointer--;
                }
                marker[entry.endX][entry.startY]++;
            }
        }
    }

    private void processNonStraight(List<Vent> nonStraightList) {
        for (var entry : nonStraightList) {
            var endToTheLeft = entry.endX > entry.startX;
            var endDown = entry.endY > entry.startY;

            var pointerX = entry.startX;
            var pointerY = entry.startY;

            while (entry.endX != pointerX) {
                marker[pointerX][pointerY]++;
                pointerX = endToTheLeft ? ++pointerX : --pointerX;
                pointerY = endDown ? ++pointerY : --pointerY;
            }
            marker[pointerX][pointerY]++;
        }
    }

    private int countCollisions() {
        var counter = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (marker[i][j] > 1) counter++;
            }
        }
        return counter;
    }
}

class Vent {

    int startX;
    int startY;
    int endX;
    int endY;

    public Vent(String x) {
        var split = x.split(" -> ");
        startX = Integer.parseInt(split[0].split(",")[0]);
        startY = Integer.parseInt(split[0].split(",")[1]);
        endX = Integer.parseInt(split[1].split(",")[0]);
        endY = Integer.parseInt(split[1].split(",")[1]);
    }

    public boolean isStraight() {
        return startX == endX || startY == endY;
    }
}