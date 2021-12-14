package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 {

    private final List<Fish> fishList;

    public Day6() {
        var tmp = Arrays.stream(FileHelper.loadString("Day6Input.txt").trim().split(","))
                .map(Fish::new)
                .toList();
        fishList = new ArrayList<>(tmp);
    }

    public int part1() {
        for (int i = 0; i < 256; i++) {
            System.out.println("Days passed: " + i);
            passDay();
        }

        return fishList.size();
    }

    private void passDay() {
        List<Fish> toAdd = new ArrayList<>();
        for (int i = 0; i < fishList.size(); i++) {
            final var fish = fishList.get(i);
            var num = fish.getNum();
            if (num == 0) toAdd.add(new Fish("8"));
            fish.setNum(num == 0 ? 6 : num - 1);
        }
        fishList.addAll(toAdd);
    }

    public long part2() {
        Map<Integer, Long> numOfFishWithDayToBreed = new HashMap<>();
        for (int i = 0; i <= 8; i++) {
            numOfFishWithDayToBreed.put(i,0L);
        }

        fishList.forEach(x -> {
            var xd = numOfFishWithDayToBreed.get(x.getNum());
            numOfFishWithDayToBreed.put(x.getNum(), xd + 1L);
        });

        for (int i = 0; i < 256; i++) {
            incrementDay(numOfFishWithDayToBreed);
        }

        return numOfFishWithDayToBreed.values().stream().reduce(0L, Long::sum);
    }

    private void incrementDay(Map<Integer, Long> numOfFishWithDayToBreed) {
        long cache =  numOfFishWithDayToBreed.get(8);
        long current = 0;
        for (int i = 8; i >= 1; i--) {
            if (i == 1) {
                numOfFishWithDayToBreed.put(6, numOfFishWithDayToBreed.get(0) + numOfFishWithDayToBreed.get(6));
            }
            current = cache;
            cache =  numOfFishWithDayToBreed.get(i-1);
            numOfFishWithDayToBreed.put(i-1, current);
        }
        numOfFishWithDayToBreed.put(8, cache);
    }
}

class Fish {
    private int num;

    public Fish(String num) {
        this.num = Integer.parseInt(num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
