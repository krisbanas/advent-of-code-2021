package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    private static final String INPUT = "Day2Input.txt";
    private final List<Command> commandList;

    private int xPosition;
    private int yPosition;
    private int aim;

    public Day2() {
        commandList = FileHelper.loadFileAsStringList(INPUT).stream()
                .map(x -> Arrays.stream(x.split(" ")).toList())
                .map(x -> new Command(Direction.valueOf(x.get(0).toUpperCase()), Integer.parseInt(x.get(1))))
                .collect(Collectors.toList());
    }

    public int part1() {
        for (final var command : commandList) {
            switch (command.direction) {
                case UP -> xPosition -= command.value();
                case DOWN -> xPosition += command.value();
                case FORWARD -> yPosition += command.value();
            }
        }
        return yPosition * xPosition;
    }

    public int part2() {
        for (final var command : commandList) {
            switch (command.direction) {
                case UP -> aim -= command.value();
                case DOWN -> aim += command.value();
                case FORWARD -> {
                    yPosition += command.value();
                    xPosition += command.value() * aim;
                }
            }
        }
        return yPosition * xPosition;
    }

    private record Command(Direction direction, int value) {
    }

    private enum Direction {FORWARD, UP, DOWN}
}
