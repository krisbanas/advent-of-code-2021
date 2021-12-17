package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17 {

    Set<Coordinate> target;
    private int minY;
    private int maxY;

    public Day17() {
        target = readInput();
        part1();
        part2();
    }

    public void part1() {
        var yMax = Integer.MIN_VALUE;
        var yVelocity = 0;
        for (int i = -200; i < 200; i++) {
            yVelocity++;
            var states = calculateThrow(new State(Coordinate.START_COORDINATE, new Velocity(0, yVelocity)));
            if (isInTargetVertically(states.get(states.size() - 1))) {
                yMax = Math.max(yMax, states.stream().mapToInt(x -> x.coordinate.y).max().getAsInt());
            }
        }
        System.out.println(yMax);
    }

    private boolean isInTargetVertically(State state) {
        return state.coordinate.y >= minY && state.coordinate.y <= maxY;
    }

    private boolean isInTarget(List<State> states) {
        return states.stream().anyMatch(state -> target.contains(state.coordinate));
    }

    public void part2() {
        var yVelocity = -200;
        var xVelocity = -200;
        Set<Velocity> trajectories = new HashSet<>();
        for (int i = -200; i < 200; i++) {
            yVelocity++;
            for (int j = -200; j < 200; j++) {
                xVelocity++;
                var states = calculateThrow(new State(Coordinate.START_COORDINATE, new Velocity(xVelocity, yVelocity)));
                if (isInTarget(states)) {
                    trajectories.add(new Velocity(xVelocity, yVelocity));
                }
            }
            xVelocity = 0;
        }
        System.out.println(trajectories.size());
    }

    private List<State> calculateThrow(State initialState) {
        var state = initialState;
        List<State> stateList = new ArrayList<>();
        while (state.coordinate.y >= minY) {
            stateList.add(state);
            var newX = state.coordinate.x + state.velocity.Vx;
            var newY = state.coordinate.y + state.velocity.Vy;
            var newVx = Math.max(state.velocity.Vx - 1, 0);
            var newVy = state.velocity.Vy - 1;
            state = new State(new Coordinate(newX, newY), new Velocity(newVx, newVy));
        }
        return stateList;
    }

    private Set<Coordinate> readInput() {
        final var inputString = FileHelper.loadString("Day17Input.txt");
        var xDescription = inputString.split(", ")[0].substring(15);
        var yDescription = inputString.split(", ")[1].substring(2);

        var xMin = Integer.parseInt(xDescription.split("\\.\\.")[0]);
        var xMax = Integer.parseInt(xDescription.split("\\.\\.")[1]);
        var yMin = Integer.parseInt(yDescription.split("\\.\\.")[0]);
        var yMax = Integer.parseInt(yDescription.split("\\.\\.")[1]);

        var targetSet = new HashSet<Coordinate>();
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMin; j <= yMax; j++) {
                targetSet.add(new Coordinate(i, j));
            }
        }

        minY = yMin;
        maxY = yMax;

        return targetSet;
    }

    private static record Coordinate(int x, int y) {
        public static final Coordinate START_COORDINATE = new Coordinate(0, 0);
    }

    private static record Velocity(int Vx, int Vy) {

    }

    private static record State(Coordinate coordinate, Velocity velocity) {
    }
}
