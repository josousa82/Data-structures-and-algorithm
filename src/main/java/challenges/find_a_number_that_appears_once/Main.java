package challenges.find_a_number_that_appears_once;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.List.of;

/**
 * Created by sousaJ on 17/10/2020
 * in package - challenges.find_a_number_that_appears_once
 **/

@Slf4j
public class Main {
    public static void main(String[] args) {
//        log.info("teste log");

        System.out.println("test");
        findUniqueNumberInArray( new int[]{1,2,3,4,1,2,4,3,5});

    }

    public static int findUniqueNumberInArray(int[] numbers){
        Map<Integer, Long> numbersCount = Arrays.stream(numbers)
                .boxed()
                .collect(Collectors.groupingBy( n -> n, Collectors.counting()));
        numbersCount.forEach((k, v) -> System.out.println("k = " + k + "value = " + v));
        return Math.toIntExact(Collections.min(numbersCount.entrySet(), Map.Entry.comparingByValue()).getKey());
    }
}
