package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day12 {

    private final List<List<String>> paths = new ArrayList<>();
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();
    private final String lastVertex = "end";

    public Day12() {
        var file = FileHelper.loadString("Day12Input.txt");

        //1. Create vertices
        Arrays.stream(file.split("\r\n"))
                .flatMap(x -> Arrays.stream(x.trim().split("-")))
                .distinct()
                .forEach(vertex -> adjacencyList.put(vertex, new ArrayList<>()));

        // 2. Create Edges
        Arrays.stream(file.split("\r\n"))
                .map(x -> x.trim().split("-"))
                .map(x -> new Edge(x[0], x[1]))
                .flatMap(edge -> Stream.of(edge, edge.reverseEdge()))
                .filter(x -> !x.end.equals("start"))
                .forEach(edge -> adjacencyList.get(edge.start).add(edge));

//        dijkstra();
        part2();
    }

    public void part1() {
        visit("start", new ArrayList<>(), new ArrayList<>());
        System.out.println(paths.size());
    }

    public void part2() {
        visit2("start", new ArrayList<>(), new ArrayList<>());
        System.out.println(paths.size());
    }

    private void visit2(String vertex, List<String> path, List<Edge> visitedEdges) {
        path.add(vertex);
        if (vertex.equals(lastVertex)) {
            paths.add(path);
            return;
        }

        for (final var elem : adjacencyList.get(vertex)) {
            var newEdges = new ArrayList<>(visitedEdges);
            if (tryingToVisitSmallCaveMoreThanTwice(elem, path)) continue;
            newEdges.add(elem);
            visit2(elem.end, new ArrayList<>(path), newEdges);
        }
    }

    private void visit(String vertex, List<String> path, List<Edge> visitedEdges) {
        path.add(vertex);
        if (vertex.equals(lastVertex)) {
            paths.add(path);
            return;
        }

        for (final var elem : adjacencyList.get(vertex)) {
            var newEdges = new ArrayList<>(visitedEdges);
            if (newEdges.contains(elem) || tryingToVisitSmallCaveAgain(elem, path)) continue;
            newEdges.add(elem);
            visit(elem.end, new ArrayList<>(path), newEdges);
        }
    }

    private boolean tryingToVisitSmallCaveAgain(Edge elem, List<String> path) {
        var cave = elem.end;
        if (cave.toUpperCase().equals(cave)) return false;
        return path.contains(cave);
    }

    private boolean tryingToVisitSmallCaveMoreThanTwice(Edge elem, List<String> path) {
        var cave = elem.end;
        if (cave.toUpperCase().equals(cave)) return false;
        var maxVisitedCave = path.stream()
                .filter(x -> x.toLowerCase().equals(x))
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .values()
                .stream()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        if (maxVisitedCave.getOrDefault(3L, 0L) > 0 || maxVisitedCave.getOrDefault(2L, 0L) > 1) return true;
        return false;
    }

    private record Edge(String start, String end) {
        public Edge reverseEdge() {
            return new Edge(end, start);
        }
    }
}
