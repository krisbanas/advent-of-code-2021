package com.github.krisbanas.solutions.day1;

import com.github.krisbanas.util.FileHelper;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day9 {

    private final Map<Coordinate, Integer> map;

    public Day9() {
        map = readMap();
        part2(map);
    }

    public static void part2(Map<Coordinate, Integer> map) {
        System.out.println(map.keySet().stream().filter(c -> isLowPoint(c, map)).map(c -> findBasinOf(c, map))
                .distinct().sorted().limit(3).mapToInt(Basin::size).reduce(1, (a, v) -> a * v));
    }

    private static boolean isLowPoint(Coordinate coord, Map<Coordinate, Integer> map) {
        return coord.getAdjacentPoints().stream().filter(map::containsKey).allMatch(adj -> map.get(coord) < map.get(adj));
    }

    private Map<Coordinate, Integer> readMap() {
        var lines = FileHelper.loadStringList("Day9Input.txt");
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                map.put(new Coordinate(x, y), Integer.parseInt("" + line.charAt(x)));
            }
        }
        return map;
    }

    private static Basin findBasinOf(Coordinate lowPoint, Map<Coordinate, Integer> map) {
        Set<Coordinate> seen = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        Basin basin = new Basin();
        seen.add(lowPoint);
        queue.add(lowPoint);
        basin.add(lowPoint);
        while (!queue.isEmpty()) {
            var next = queue.poll();
            next.getAdjacentPoints().forEach(n -> {
                if (map.containsKey(n) && !seen.contains(n) && map.get(n) < 9) {
                    seen.add(n);
                    queue.add(n);
                    basin.add(n);
                }
            });
        }
        return basin;
    }

    private static record Coordinate(int x, int y) {
        List<Coordinate> getAdjacentPoints() {
            return List.of(new Coordinate(x - 1, y), new Coordinate(x + 1, y), new Coordinate(x, y - 1), new Coordinate(x, y + 1));
        }
    }

    private static final class Basin extends HashSet<Coordinate> implements Comparable<Basin> {

        private static final Comparator<Basin> comparator = Comparator.comparing(Basin::size, Comparator.reverseOrder());

        @Override
        public boolean equals(Object o) {
            if (o instanceof Basin b) {
                return b.size() == size() && b.containsAll(this);
            }
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            return stream().mapToInt(Coordinate::hashCode).sum();
        }

        @Override
        public int compareTo(Basin o) {
            return comparator.compare(this, o);
        }
    }
}
