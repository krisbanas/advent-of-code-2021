package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day8 {

    private final List<String> inputList;
    private final ArrayList<String> displayTen;
    private final ArrayList<String> displayOutput;

    public Day8() {
        inputList = FileHelper.loadStringList("Day8Input.txt");
        displayTen = new ArrayList<>();
        displayOutput = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i = i + 2) {
            displayTen.add(inputList.get(i));
            displayOutput.add(inputList.get(i + 1));
        }
    }

    public long part1() {
        return displayOutput.stream()
                .map(x -> x.split(" "))
                .flatMap(Arrays::stream)
                .filter(x -> (x.length() == 2 || x.length() == 3 || x.length() == 4 || x.length() == 7))
                .count();
    }

    public int part2() {
        int counter = 0;
        for (int i = 0; i < displayOutput.size(); i++) {
            System.out.println("i: " + i);
            final var value = calculateNumberForEntryIndex(i);
            System.out.println("value: " + value);
            counter += value;

        }
        return counter;
    }

    private int calculateNumberForEntryIndex(int index) {
        Map<String, Integer> matchStrings = new HashMap<>();
        Map<String, String> cableMapping = findCableMapping(index);
        findCableMapping(index);
        createMatchStrings(matchStrings, cableMapping);

        final var toTestLine = displayOutput.get(index);
        final var toSumList = Arrays.stream(toTestLine.split(" ")).map(a ->
                matchStrings.entrySet().stream().filter(x -> equalsMatchString(a, x)).findAny().get().getValue()
        ).toList();

        return toSumList.get(0) * 1000 + toSumList.get(1) * 100 + toSumList.get(2) * 10 + toSumList.get(3);
    }

    private void createMatchStrings(Map<String, Integer> matchStrings, Map<String, String> cableMapping) {
        final var zeroString = cableMapping.get("a") + cableMapping.get("b") + cableMapping.get("c") + cableMapping.get("e") + cableMapping.get("f") + cableMapping.get("g");
        final var oneString = cableMapping.get("c") + cableMapping.get("f");
        final var twoString = cableMapping.get("a") + cableMapping.get("c") + cableMapping.get("d") + cableMapping.get("e") + cableMapping.get("g");
        final var threeString = cableMapping.get("a") + cableMapping.get("c") + cableMapping.get("d") + cableMapping.get("f") + cableMapping.get("g");
        final var fourString = cableMapping.get("b") + cableMapping.get("c") + cableMapping.get("d") + cableMapping.get("f");
        final var fiveString = cableMapping.get("a") + cableMapping.get("b") + cableMapping.get("d") + cableMapping.get("f") + cableMapping.get("g");
        final var sixString = cableMapping.get("a") + cableMapping.get("b") + cableMapping.get("d") + cableMapping.get("e") + cableMapping.get("f") + cableMapping.get("g");
        final var sevenString = cableMapping.get("a") + cableMapping.get("c") + cableMapping.get("f");
        final var eightString = cableMapping.get("a") + cableMapping.get("b") + cableMapping.get("c") + cableMapping.get("d") + cableMapping.get("e") + cableMapping.get("f") + cableMapping.get("g");
        final var nineString = cableMapping.get("a") + cableMapping.get("b") + cableMapping.get("c") + cableMapping.get("d") + cableMapping.get("f") + cableMapping.get("g");
        matchStrings.put(zeroString, 0);
        matchStrings.put(oneString, 1);
        matchStrings.put(twoString, 2);
        matchStrings.put(threeString, 3);
        matchStrings.put(fourString, 4);
        matchStrings.put(fiveString, 5);
        matchStrings.put(sixString, 6);
        matchStrings.put(sevenString, 7);
        matchStrings.put(eightString, 8);
        matchStrings.put(nineString, 9);
    }

    private boolean equalsMatchString(String toTest, Map.Entry<String, Integer> x) {
        var key = x.getKey();
        key = sort(key);
        toTest = sort(toTest);

        return key.equals(toTest);
    }

    private String sort(String key) {
        char[] charArray = key.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    private Map<String, String> findCableMapping(int index) {
        Map<String, String> cables = new HashMap<>();
        final var entry = displayTen.get(index).substring(0, displayTen.get(index).length() - 1).strip();
        final var entryList = Arrays.stream(entry.split(" ")).toList();

        // find a
        final var seven = findEntryWithLength(entryList, 3);
        final var one = findEntryWithLength(entryList, 2);
        final var a = seven.replace(one.substring(0, 1), "").replace(one.substring(1, 2), "");
        cables.put("a", a);

        // find b, e, f
        final var occurrenceList = Arrays.stream(entry.split("")).filter(x -> !x.isBlank()).collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        cables.put("e", occurrenceList.entrySet().stream().filter(x -> x.getValue() == 4).toList().get(0).getKey());
        cables.put("b", occurrenceList.entrySet().stream().filter(x -> x.getValue() == 6).toList().get(0).getKey());
        cables.put("f", occurrenceList.entrySet().stream().filter(x -> x.getValue() == 9).toList().get(0).getKey());

        // find c
        cables.put("c", one.replace(cables.get("f"), ""));

        // find d
        var four = entryList.stream().filter(x -> x.length() == 4).findAny().get();
        var d = four.replace(one.substring(0, 1), "").replace(one.substring(1, 2), "").replace(cables.get("b"), "");
        cables.put("d", d);

        //find g
        var eight = entryList.stream().filter(x -> x.length() == 7).findAny().get();
        var g = eight
                .replace(cables.get("a"), "")
                .replace(cables.get("b"), "")
                .replace(cables.get("c"), "")
                .replace(cables.get("d"), "")
                .replace(cables.get("e"), "")
                .replace(cables.get("f"), "");

        cables.put("g", g);
        return cables;
    }

    private String findEntryWithLength(List<String> entryList, int len) {
        return entryList.stream().filter(x -> x.length() == len).findAny().get();
    }
}
