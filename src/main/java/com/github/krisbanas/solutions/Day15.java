package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Day15 {

    private final Map<Coordinate, Integer> riskMap;
    private Map<Coordinate, List<Edge>> graph1 = new HashMap<>();
    private Map<Coordinate, Integer> distanceList1 = new HashMap();

    private Map<Coordinate, Integer> riskMap2;
    private Map<Coordinate, List<Edge>> graph2 = new HashMap<>();
    private Map<Coordinate, Integer> distanceList2 = new HashMap();

    public static final int PROBLEM_SIZE = 100;

    public Day15() {
        riskMap = readMap1();
        part2();
    }

    public void dijkstra() {
        PriorityQueue<Vertex> verticesToVisit = new PriorityQueue<>(Comparator.comparingInt(v -> v.distanceFromStart));
        verticesToVisit.offer(new Vertex(new Coordinate(0, 0), 0));

        while (!verticesToVisit.isEmpty()) {
            Vertex vertex = verticesToVisit.poll();
            Coordinate vertexCoordinate = vertex.coordinate;
            int distance = vertex.distanceFromStart;

            if (distanceList1.containsKey(vertexCoordinate)) continue;
            distanceList1.put(vertexCoordinate, distance);

            if (graph1.containsKey(vertexCoordinate)) {
                for (Edge edge : graph1.get(vertexCoordinate)) {
                    Coordinate endCoordinate = edge.end;
                    int distanceToEndNode = edge.length;
                    if (!distanceList1.containsKey(endCoordinate)) {
                        verticesToVisit.offer(new Vertex(endCoordinate, distance + distanceToEndNode));
                    }
                }
            }
        }
        System.out.println(distanceList1.get(new Coordinate(PROBLEM_SIZE - 1, PROBLEM_SIZE - 1)));
    }

    public void part2() {
        riskMap2 = readMap2();
        fillAdjacencyList2();
        dijkstra2();
    }


    public void dijkstra2() {
        PriorityQueue<Vertex> verticesToVisit = new PriorityQueue<>(Comparator.comparingInt(v -> v.distanceFromStart));
        verticesToVisit.offer(new Vertex(new Coordinate(0, 0), 0));

        while (!verticesToVisit.isEmpty()) {
            Vertex vertex = verticesToVisit.poll();
            Coordinate vertexCoordinate = vertex.coordinate;
            int distance = vertex.distanceFromStart;

            if (distanceList2.containsKey(vertexCoordinate)) continue;
            distanceList2.put(vertexCoordinate, distance);

            if (graph2.containsKey(vertexCoordinate)) {
                for (Edge edge : graph2.get(vertexCoordinate)) {
                    Coordinate endCoordinate = edge.end;
                    int distanceToEndNode = edge.length;
                    if (!distanceList2.containsKey(endCoordinate)) {
                        verticesToVisit.offer(new Vertex(endCoordinate, distance + distanceToEndNode));
                    }
                }
            }
        }
        System.out.println(distanceList2.get(new Coordinate(PROBLEM_SIZE * 5 - 1, PROBLEM_SIZE * 5 - 1)));
    }

    private Map<Coordinate, Integer> readMap2() {
        riskMap2 = readMap1();
        for (Coordinate coordinate : riskMap.keySet()) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (i == 0 && j == 0) continue;
                    var currentVal = riskMap.get(coordinate);
                    currentVal += (j + i);
                    if (currentVal >= 10) currentVal -= 9;
                    riskMap2.put(new Coordinate(coordinate.x + i * PROBLEM_SIZE, coordinate.y + j * PROBLEM_SIZE), currentVal);
                }
            }
        }
        return riskMap2;
    }

    private void fillAdjacencyList2() {
        for (int x = 0; x < PROBLEM_SIZE * 5; x++) {
            for (int y = 0; y < PROBLEM_SIZE * 5; y++) {
                final var coordinate = new Coordinate(x, y);
                final var adjacent = coordinate.getAdjacent();
                final var edges = adjacent.stream().map(c -> new Edge(coordinate, c, riskMap2.get(c))).toList();
                graph2.put(coordinate, edges);
            }
        }
    }

    private void fillAdjacencyList1() {
        for (int x = 0; x < PROBLEM_SIZE; x++) {
            for (int y = 0; y < PROBLEM_SIZE; y++) {
                final var coordinate = new Coordinate(x, y);
                final var adjacent = coordinate.getAdjacent();
                final var edges = adjacent.stream().map(c -> new Edge(coordinate, c, riskMap2.get(c))).toList();
                graph2.put(coordinate, edges);
            }
        }
    }

    private Map<Coordinate, Integer> readMap1() {
        final var riskMap = new HashMap<Coordinate, Integer>();
        var lines = FileHelper.loadStringList("Day15Input.txt");
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                riskMap.put(new Coordinate(x, y), Integer.parseInt("" + line.charAt(x)));
            }
        }
        return riskMap;
    }

    private static record Coordinate(int x, int y) {
        List<Coordinate> getAdjacent() {
            List<Coordinate> adjacent = new ArrayList<>();
            if (x - 1 >= 0) adjacent.add(new Coordinate(x - 1, y));
            if (x + 1 < PROBLEM_SIZE * 5) adjacent.add(new Coordinate(x + 1, y));
            if (y - 1 >= 0) adjacent.add(new Coordinate(x, y - 1));
            if (y + 1 < PROBLEM_SIZE * 5) adjacent.add(new Coordinate(x, y + 1));
            return adjacent;
        }
    }

    private static record Edge(Coordinate start, Coordinate end, int length) {
    }

    private static record Vertex(Coordinate coordinate, int distanceFromStart) {
    }
}
