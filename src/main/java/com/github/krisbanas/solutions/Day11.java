package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Day11 {

    private static Map<Coordinate, Octopus> octopusMap;
    private static final Set<Coordinate> flashedThisRound = new HashSet<>();

    public Day11() {
        octopusMap = readMap();

        part1();
        part2();
    }

    public void part1() {
        int flashCounter = 0;
        for (int i = 0; i < 100; i++) {
            for (var octopus : octopusMap.values()) {
                octopus.energize();
            }
            flashCounter += flashedThisRound.size();
            flashedThisRound.clear();
        }
        System.out.println(flashCounter);
    }

    public void part2() {
        int iterationCounter = 0;
        while (true) {
            iterationCounter++;
            for (var octopus : octopusMap.values()) {
                octopus.energize();
            }
            if (flashedThisRound.size() == 100) {
                System.out.println(iterationCounter);
                break;
            }
            flashedThisRound.clear();
        }
    }

    private static class Octopus {

        private final Coordinate coordinate;
        private int energyValue;

        public Octopus(Coordinate coordinate, int initialValue) {
            this.coordinate = coordinate;
            this.energyValue = initialValue;
        }

        public void energize() {
            if (flashedThisRound.contains(coordinate)) return;
            energyValue++;
            if (energyValue > 9) flash();
        }

        private void flash() {
            flashedThisRound.add(coordinate);
            energyValue = 0;
            coordinate.getAdjacentPoints().stream()
                    .map(x -> octopusMap.get(x))
                    .filter(Objects::nonNull)
                    .forEach(Octopus::energize);
        }
    }

    private static record Coordinate(int x, int y) {
        List<Coordinate> getAdjacentPoints() {
            return List.of(
                    new Coordinate(x - 1, y + 1), new Coordinate(x, y + 1), new Coordinate(x + 1, y + 1), // upper row
                    new Coordinate(x - 1, y), new Coordinate(x + 1, y), // middle row
                    new Coordinate(x - 1, y - 1), new Coordinate(x, y - 1), new Coordinate(x + 1, y - 1) // lower row
            );
        }
    }

    private Map<Coordinate, Octopus> readMap() {
        Map<Coordinate, Octopus> map = new HashMap<>();
        var lines = FileHelper.loadStringList("Day11Input.txt");
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                final var key = new Coordinate(x, y);
                map.put(key, new Octopus(key, Integer.parseInt("" + line.charAt(x))));
            }
        }
        return map;
    }
}
