package com.github.krisbanas.util;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileHelper {

    public static final String RESOURCES_ROOT = "src/main/resources/";

    @SneakyThrows
    public static List<String> loadStringList(String filename) {
        return Files.readAllLines(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath());
    }

    @SneakyThrows
    public static List<Integer> loadIntegerList(String filename) {
        return Files.readAllLines(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath())
                .stream().mapToInt(Integer::valueOf)
                .boxed()
                .toList();
    }

    @SneakyThrows
    public static String loadString(String filename) {
        return Files.readString(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath());
    }

    @SneakyThrows
    public static List<Integer> loadStringSeparatedIntegers(String filename) {
        return Arrays.stream(FileHelper.loadString(filename).split(","))
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());
    }
}
