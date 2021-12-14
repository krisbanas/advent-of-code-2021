package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {

    private String polymer;
    private Map<String, String> substitutionDict = new HashMap<>();

    public Day14() {
        final var input = FileHelper.loadStringList("Day14Input.txt");
        polymer = input.get(0).trim();

        for (int i = 2; i < input.size(); i++) {
            final var entry = input.get(i);
            var entrySplit = entry.split("->");
            substitutionDict.put(entrySplit[0].trim(), entrySplit[1].trim());
        }

//        part1();
        part2();
    }

    public void part1() {
        for (int i = 0; i < 10; i++) {
            System.out.println("step: " + i);
            polymerizeOnce();
        }
        final var frequencyMap = polymer.chars().mapToObj(String::valueOf).collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        final var max = frequencyMap.values().stream().max(Long::compareTo).get();
        final var min = frequencyMap.values().stream().min(Long::compareTo).get();
        System.out.println(max - min);
    }

    private void polymerizeOnce() {
        final var sb = new StringBuilder();
        sb.append(polymer.charAt(0));
        for (int i = 0; i < polymer.length() - 1; i++) {
            var ruleResult = substitutionDict.get(polymer.substring(i, i + 2));
            sb.append(ruleResult);
            sb.append(polymer.charAt(i + 1));
        }
        polymer = sb.toString();
    }

    public void part2() {
        Map<String, Long> polymerPairs = new HashMap<>();
        Map<String, Long> finalPolymerPairs = polymerPairs;
        substitutionDict.keySet().forEach(key -> finalPolymerPairs.put(key, 0L));
        for (int i = 0; i < polymer.length() - 1; i++) {
            final var onePoly = polymer.substring(i, i + 2);
            polymerPairs.compute(onePoly, (k, v) -> v + 1L);
        }

//        Map<String, Long> newPolymerPairs = new HashMap<>();
//        polymerPairs.keySet().forEach(key -> newPolymerPairs.put(key, 0L));

        for (int i = 0; i < 40; i++) {
            Map<String, Long> newPolymerPairs = new HashMap<>();
            polymerPairs.keySet().forEach(key -> newPolymerPairs.put(key, 0L));
            for (var rule : polymerPairs.keySet()) {
                var toPend = substitutionDict.get(rule);
                var currentCount = polymerPairs.getOrDefault(rule, 0L);
                newPolymerPairs.compute(rule.substring(0, 1) + toPend, (k, v) -> v + currentCount);
                newPolymerPairs.compute(toPend + rule.substring(1, 2), (k, v) -> v + currentCount);
            }
            polymerPairs = newPolymerPairs;
        }

        Map<String, Long> frequencyMap = polymerPairs.keySet().stream()
                .flatMap(x -> Arrays.stream(x.split("")))
                .distinct()
                .collect(Collectors.toMap(key -> key, key -> 0L));

        Map<String, Long> finalPolymerPairs1 = polymerPairs;
        polymerPairs.keySet().forEach(
                key -> {
                    var one = key.substring(0, 1);
                    var two = key.substring(1, 2);
                    frequencyMap.compute(one, (k, v) -> v + finalPolymerPairs1.get(key));
                    frequencyMap.compute(two, (k, v) -> v + finalPolymerPairs1.get(key));
                }
        );

        var sumList = frequencyMap.values().stream().map(x -> (x + 1) / 2).toList();

        final var max = sumList.stream().max(Long::compareTo).get();
        final var min = sumList.stream().min(Long::compareTo).get();
        System.out.println(max - min);
        System.out.println("gfdgjh");
    }
}
