package com.github.krisbanas.util;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHelper {

    public static final String RESOURCES_ROOT = "src/main/resources/";

    @SneakyThrows
    public static List<String> loadFileAsStringList(String filename) {
        return Files.readAllLines(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath());
    }

    @SneakyThrows
    public static List<Integer> loadFileAsIntegerList(String filename) {
        return Files.readAllLines(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath())
                .stream().mapToInt(Integer::valueOf)
                .boxed()
                .toList();
    }

    @SneakyThrows
    public static String loadFileAsString(String filename) {
        return Files.readString(Paths.get(RESOURCES_ROOT + filename).toAbsolutePath());
    }
}
