package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;
import static java.lang.System.out;

public class Day20 {

    private static Map<Coordinate, Character> map = new HashMap<>();
    private static String encoder;
    private static final int STARTING_SIZE = 100;
    private static final int OFFSET = 75;
    private static int MIN = -OFFSET;
    private static int MAX = STARTING_SIZE + OFFSET;

    public Day20() {
        final var inputString = FileHelper.loadString("Day20Input.txt");
        encoder = inputString.split(lineSeparator() + lineSeparator())[0].replaceAll(lineSeparator(), "");
        var inputRaw = inputString.split(lineSeparator() + lineSeparator())[1];
        map = readMapFromString(inputRaw);
        part1();
    }

    public void part1() {
//        printPoints();
        out.println("part 1 works");

        for (int k = 0; k < 50; k++) {
            out.println("iteration: " + k);
            Map<Coordinate, Character> newMap = new HashMap<>();
            MAX++;
            MIN--;
            for (int i = MIN; i < MAX; i++) {
                for (int j = MIN; j < MAX; j++) {
                    List<Character> chars = new Coordinate(i, j).getGrid().stream()
                            .map(coordinate -> getForCoordinate(coordinate))
                            .toList();

                    // Find encoding character
                    var binaryString = chars.stream().map(character -> character == '#' ? "1" : "0").collect(Collectors.joining());
                    var encodingCharacter = encoder.charAt(Integer.parseInt(binaryString, 2));

                    newMap.put(new Coordinate(i, j), encodingCharacter);
                }
            }

            map = newMap;
//            printPoints();
        }

        out.println(map.entrySet().stream()
                .filter(x -> (x.getKey().x >= MIN + OFFSET && x.getKey().y >= MIN + OFFSET && x.getKey().x < MAX - OFFSET && x.getKey().y < MAX - OFFSET))
                .map(Map.Entry::getValue)
                .filter(x -> x == '#')
                .count());
    }

    private Character getForCoordinate(Coordinate coordinate) {
        return map.getOrDefault(coordinate, '.');
    }

    private record Coordinate(int x, int y) {
        List<Coordinate> getGrid() {
            return List.of(
                    new Coordinate(x - 1, y - 1), new Coordinate(x - 1, y), new Coordinate(x - 1, y + 1), // upper row
                    new Coordinate(x, y - 1), new Coordinate(x, y), new Coordinate(x, y + 1), // middle row
                    new Coordinate(x + 1, y - 1), new Coordinate(x + 1, y), new Coordinate(x + 1, y + 1) // lower row
            );
        }
    }

    private static Map<Coordinate, Character> readMapFromString(String input) {
        var map = new HashMap<Coordinate, Character>();
        var lines = Arrays.stream(input.split(lineSeparator())).toList();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                final var key = new Coordinate(y, x);
                map.put(key, line.charAt(x));
            }
        }
        return map;
    }

    private void printPoints() {
        for (int i = MIN; i < MAX; i++) {
            for (int j = MIN; j < MAX; j++) {
                if (map.containsKey(new Coordinate(i, j))) out.print(map.get(new Coordinate(i, j)));
                else System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }
}