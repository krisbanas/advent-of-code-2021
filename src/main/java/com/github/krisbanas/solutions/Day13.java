package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 {

    private Set<Coordinate> points;
    private List<Instruction> instructions;
    private int xSize;
    private int ySize;

    public Day13() {
        var file = FileHelper.loadString("Day13Input.txt");
        var splitTable = Arrays.stream(file.split(System.lineSeparator()))
                .collect(Collectors.partitioningBy(x -> x.length() < 13));

        points = splitTable.get(true).stream()
                .filter(entry -> !entry.isBlank())
                .map(x -> x.split(","))
                .map(x -> new Coordinate(Integer.parseInt(x[0]), Integer.parseInt(x[1])))
                .collect(Collectors.toSet());

        instructions = splitTable.get(false).stream()
                .map(x -> x.replaceAll("fold along", ""))
                .map(x -> x.split("="))
                .map(x -> new Instruction(x[0].trim(), Integer.parseInt(x[1])))
                .toList();

        xSize = points.stream().max(Comparator.comparingInt(coordinate -> coordinate.x)).get().x;
        ySize = points.stream().max(Comparator.comparingInt(coordinate -> coordinate.y)).get().y;

        part1();
        part2();
    }

    public void part1() {
        fold(0);
        final var numberOfDots = points.size();
        System.out.println("Dots: " + numberOfDots);
    }

    public void part2() {
        for (int i = 1; i < instructions.size(); i++) { // folded once already in part1
            fold(i);
        }
        printPoints();
    }

    private void fold(int i) {
        final var instruction = instructions.get(i);

        points = points.stream()
                .map(point -> foldPoint(point, instruction))
                .collect(Collectors.toSet());

        xSize = instruction.axis.equals("x") ? xSize / 2 : xSize;
        ySize = instruction.axis.equals("y") ? ySize / 2 : ySize;
    }

    private Coordinate foldPoint(Coordinate point, Instruction instruction) {
        return instruction.axis.equals("x") ? foldPointX(point, instruction.value) : foldPointY(point, instruction.value);
    }

    private Coordinate foldPointX(Coordinate point, int xAxis) {
        final var distance = point.x - xAxis;
        if (point.x >= xAxis) return new Coordinate(xAxis - distance, point.y);
        else return point;
    }

    private Coordinate foldPointY(Coordinate point, int yAxis) {
        final var distance = point.y - yAxis;
        if (point.y >= yAxis) return new Coordinate(point.x, yAxis - distance);
        else return point;
    }

    private void printPoints() {
        for (int i = 0; i <= ySize; i++) {
            for (int j = 0; j <= xSize; j++) {
                if (points.contains(new Coordinate(j, i))) System.out.print("#");
                else System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static record Coordinate(int x, int y) {
    }

    private static record Instruction(String axis, int value) {
    }
}
