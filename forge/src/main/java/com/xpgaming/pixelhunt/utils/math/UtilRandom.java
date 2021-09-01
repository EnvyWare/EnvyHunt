package com.xpgaming.pixelhunt.utils.math;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UtilRandom {

    public static <T> T getRandomElementExcluding(T[] array, T... exclude) {
        T randomElement = getRandomElement(array);
        List<T> exclusions = Arrays.asList(exclude);

        while (exclusions.contains(randomElement)) {
            randomElement = getRandomElement(array);
        }

        return randomElement;
    }

    public static <T> T getRandomElement(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }

    public static <T> T getRandomElement(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }

    public static int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(max + 1 - min) + min;
    }
}
