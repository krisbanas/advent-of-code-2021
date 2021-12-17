package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day18 {

    List<SnailfishNumber> snailfishNumbers;
    List<Value> valuesInOrder = new ArrayList<>();
    private int minY;
    private int maxY;

    private SnailfishNumber added;

    public Day18() {
        snailfishNumbers = readInput();
        part1();
        part2();
    }

    public void part1() {
        var res = snailfishNumbers.stream().reduce(this::addNumbers);

        System.out.println("works.");
    }

    public void part2() {
    }

    private SnailfishNumber addNumbers(SnailfishNumber left, SnailfishNumber right) {
        var added = new Pair(left, right);
        this.added = added;
        while (true) {
            recreateNumberOrder(added);
            immediateExplosion = false;
            addToLeft = 0;
            addToRight = 0;
            explosionDone = false;
            splitDone = false;
            explode(added, 0);
            System.out.println(valuesInOrder);
            if (explosionDone) {
                recreateNumberOrder(added);
                continue;
            }
//            recreateNumberOrder(added);
            split(added);
            if (splitDone) continue;
            break;
        }
//        System.out.println(valuesInOrder);
        return added;
    }

    public static int addToLeft;
    public static int addToRight;
    public static boolean immediateExplosion;
    public static boolean explosionDone;
    public static boolean splitDone;

    private void explode(SnailfishNumber number, int nestingLevel) {
        // Base cases
        if (explosionDone || number instanceof Value)
            return;
        if (nestingLevel == 4 && number instanceof Pair p) {
            addToLeft = ((Value) p.left).value;
            addToRight = ((Value) p.right).value;
            immediateExplosion = true;
            explosionDone = true;
            return;
        }

        var pair = (Pair) number;
        explode(pair.left, nestingLevel + 1);
        if (immediateExplosion && nestingLevel == 3) {
            immediateExplosion = false;
            addValuesFrom(pair.left);
            pair.left = new Value(0);
            return;
        }

        explode(pair.right, nestingLevel + 1);
        if (immediateExplosion && nestingLevel == 3) {
            immediateExplosion = false;
            addValuesFrom(pair.right);
            pair.right = new Value(0);
        }
    }

    private void split(SnailfishNumber added) {
        if (splitDone) return;
        if (added instanceof Value) return;
        var addedPair = (Pair) added;

        split(addedPair.left);
        if (addedPair.left instanceof Value value && value.value > 9) {
            splitDone = true;
            addedPair.left = createSplitPairFrom(value.value);
        } else if (addedPair.right instanceof Value value && value.value > 9) {
            splitDone = true;
            addedPair.right = createSplitPairFrom(value.value);
            return;
        }
        split(addedPair.right);
    }

    private Pair createSplitPairFrom(int value) {
        return new Pair(new Value(value / 2), new Value((value + 1) / 2));
    }

    private void addValuesFrom(SnailfishNumber pair) {
        var left = (Value) ((Pair) pair).left;
        for (int i = 0; i < valuesInOrder.size(); i++) {
            if (valuesInOrder.get(i) == left && i > 0)
                valuesInOrder.get(i - 1).setValue(valuesInOrder.get(i - 1).value + addToLeft);
        }

        var right = (Value) ((Pair) pair).right;
        for (int i = 0; i < valuesInOrder.size(); i++) {
            if (valuesInOrder.get(i) == right && i < valuesInOrder.size() - 1) {
                valuesInOrder.get(i + 1).setValue(valuesInOrder.get(i + 1).value + addToRight);
                break;
            }
        }
    }

    private List<SnailfishNumber> readInput() {
        final var inputList = FileHelper.loadStringList("Day18Input.txt");
        return inputList.stream()
                .map(this::createSnailfishNumber)
                .toList();
    }

    private SnailfishNumber createSnailfishNumber(String entry) {
        if (entry.length() == 1) return new Value(Integer.parseInt(entry));

        int pointer = findPositionOfComma(entry);

        var leftSubstring = entry.substring(1, pointer);
        var rightSubstring = entry.substring(pointer + 1, entry.length() - 1);

        return new Pair(createSnailfishNumber(leftSubstring), createSnailfishNumber(rightSubstring));
    }

    private void recreateNumberOrder(Pair added) {
        valuesInOrder.clear();
        visit(added);
    }

    private void visit(SnailfishNumber number) {
        if (number instanceof Value value) valuesInOrder.add(value);
        else {
            var pair = (Pair) number;
            visit(pair.left);
            visit(pair.right);
        }
    }

    private int findPositionOfComma(String entry) {
        var nestingLevel = 0;
        var pointer = 0;
        while (pointer != entry.length()) {
            if (entry.charAt(pointer) == '[') nestingLevel++;
            if (entry.charAt(pointer) == ']') nestingLevel--;
            if (entry.charAt(pointer) == ',') {
                if (nestingLevel == 1) break;
            }
            pointer++;
        }
        return pointer;
    }

    private interface SnailfishNumber {
    }

    private static final class Pair implements SnailfishNumber {
        public SnailfishNumber left;
        public SnailfishNumber right;

        private Pair(SnailfishNumber left, SnailfishNumber right) {
            this.left = left;
            this.right = right;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Pair) obj;
            return Objects.equals(this.left, that.left) &&
                    Objects.equals(this.right, that.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }

        @Override
        public String toString() {
            return "Pair[" +
                    "left=" + left + ", " +
                    "right=" + right + ']';
        }

    }

    private static final class Value implements SnailfishNumber {
        private int value;

        private Value(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Value) obj;
            return this.value == that.value;
        }

        @Override
        public String toString() {
            return " " + value;
        }

    }

    private static record OperationResult(boolean changed, SnailfishNumber number) {
    }
}