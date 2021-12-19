package com.github.krisbanas.solutions;

import com.github.krisbanas.util.FileHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day19 {

    private final List<Probe> probeList = new ArrayList<>();
    private final List<Rotation> rotations = new ArrayList<>();
    private static final List<Integer> ANGLES = List.of(0, 90, 180, 270);
    private Probe mainProbe;

    public Day19() {
        createBeaconList();
        mainProbe = probeList.get(0);
        part1();
        part2();
    }


    public void part1() {

        final var newProbe = probeList.get(1);
        {
            final var permutedBeaconList = permuteBeaconList(newProbe.beaconList());
            final var candidates = permutedBeaconList.stream().map(this::calculateBeaconsForOneRotation).toList();

            // pick best rotated
            final var translatedBeacons = candidates.stream().max(Comparator.comparingLong(Candidate::count)).get().beacons();

            // Update mainProbe
            final var newBeaconList = Stream.of(translatedBeacons, mainProbe.beaconList()).flatMap(Collection::stream).distinct().toList();
            mainProbe = new Probe(0, newBeaconList);
        }


        System.out.println(mainProbe.beaconList().size());
    }

    private List<List<Beacon>> permuteBeaconList(List<Beacon> beaconList) {
        return null;
    }

    private Candidate calculateBeaconsForOneRotation(List<Beacon> beaconList) {

        // calculate translations
        final var vectorList = new ArrayList<Vector>();
        for (int i = 0; i < mainProbe.beaconList().size(); i++) {
            for (Beacon beacon : beaconList) {
                vectorList.add(createVector(i, beacon));
            }
        }

        // count translation
        final var countMap = vectorList.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
        final var translationVector = countMap.entrySet().stream().max(Comparator.comparingLong(Map.Entry::getValue)).get();

        // do a translation for 1 to find all
        return new Candidate(translationVector.getValue(), beaconList.stream().map(x -> translateBeacon(x, translationVector.getKey())).toList());
    }

    public void part2() {
    }

    private Beacon translateBeacon(Beacon beacon, Vector translationVector) {
        return new Beacon(
                beacon.x() - translationVector.x(),
                beacon.y() - translationVector.y(),
                beacon.z() - translationVector.z()
        );
    }

    private Vector createVector(int i, Beacon newBeacon) {
        return new Vector(
                newBeacon.x() - mainProbe.beaconList().get(i).x(),
                newBeacon.y() - mainProbe.beaconList().get(i).y(),
                newBeacon.z() - mainProbe.beaconList().get(i).z()
        );
    }


    private void createBeaconList() {
        final var beaconInputList = FileHelper.loadString("Day19Input.txt")
                .split(System.lineSeparator() + System.lineSeparator());

        for (String beaconInput : beaconInputList) {
            final var inputSplit = beaconInput.split(System.lineSeparator());
            int number = Integer.parseInt(inputSplit[0].split(" ")[2]);
            final var beaconList = Arrays.stream(inputSplit)
                    .skip(1)
                    .map(this::constructBeaconFromInput)
                    .toList();

            probeList.add(new Probe(number, beaconList));
        }
    }

    private Beacon constructBeaconFromInput(String s) {
        final var sSpLit = s.split(",");
        return new Beacon(Integer.parseInt(sSpLit[0]), Integer.parseInt(sSpLit[1]), 0); // TODO
    }
}

record Beacon(int x, int y, int z) {

}

/**
 * Ile trafien
 */
record Candidate(long count, List<Beacon> beacons) {

}

record Probe(int number, List<Beacon> beaconList) {
}

record Vector(int x, int y, int z) {

}

record Rotation(int rotX, int rotY, int rotZ) {

}