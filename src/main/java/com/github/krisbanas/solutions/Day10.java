package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Day10 {

    private final List<String> inputList;
    Stack<String> nawiasStack;
    List<Stack<String>> stacksToEvaluate = new ArrayList<>();

    public Day10() {
        inputList = FileHelper.loadStringList("Day10Input.txt");
        part2();
    }

    public void part1() {
        // I think I broke this part
        int counter = 0;

        for (var entry : inputList) {
            nawiasStack = new Stack<>();
            for (int i = 0; i < entry.length() - 1; i++) {
                var nextNawias = entry.substring(i, i + 1);
                System.out.println("Nawias na pozycji i: " + nextNawias);
                if (isOpeningBracket(nextNawias)) {
                    nawiasStack.push(getClosingBracketFor(nextNawias));
                } else {
                    if (nawiasStack.isEmpty() || !nextNawias.equals(nawiasStack.pop())) {
                        continue;
                    } else {
                        stacksToEvaluate.add((Stack<String>) nawiasStack.clone());
                    }

                }
            }
        }
        System.out.println(counter);
    }

    public void part2() {
        outer:
        for (var entry : inputList) {
            nawiasStack = new Stack<>();
            for (int i = 0; i < entry.length(); i++) {
                var nextNawias = entry.substring(i, i + 1);
                if (isOpeningBracket(nextNawias)) {
                    nawiasStack.push(getClosingBracketFor(nextNawias));
                } else { //closing bracket
                    if (nawiasStack.isEmpty()) continue outer; // nothing to close, corrupted!
                    if (!nextNawias.equals(nawiasStack.pop())) continue outer;// corrupted!
                }
            }
            if (!nawiasStack.isEmpty()) { // nothing to close it with
                stacksToEvaluate.add((Stack<String>) nawiasStack.clone());
            }
        }

        System.out.println(countStackValue());
    }

    private long countStackValue() {
        List<Long> scores = new ArrayList<>();
        for (var entry : stacksToEvaluate) {
            long counter = 0;

            while (!entry.isEmpty()) {
                counter *= 5L;
                counter += value(entry.pop());
            }
            scores.add(counter);
        }
        Collections.sort(scores);
        return scores.get(scores.size() / 2);
    }

    private int value(String pop) {
        return switch (pop) {
            case ")" -> 1;
            case "]" -> 2;
            case "}" -> 3;
            case ">" -> 4;
            default -> throw new IllegalArgumentException("spok?");
        };
    }

    private int getScoreFor(String nextBracket) {
        return switch (nextBracket) {
            case "(", ")" -> 3;
            case "[", "]" -> 57;
            case "{", "}" -> 1197;
            case "<", ">" -> 25137;
            default -> throw new IllegalArgumentException("spok?");
        };
    }

    private String getClosingBracketFor(String nextBracket) {
        return switch (nextBracket) {
            case "(" -> ")";
            case "[" -> "]";
            case "{" -> "}";
            case "<" -> ">";
            default -> throw new IllegalArgumentException("wut?");
        };
    }

    private boolean isOpeningBracket(String nextBracket) {
        return nextBracket.equals("(") || nextBracket.equals("[") || nextBracket.equals("<") || nextBracket.equals("{");
    }
}
