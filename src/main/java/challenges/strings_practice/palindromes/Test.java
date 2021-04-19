package challenges.strings_practice.palindromes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by sousaJ on 06/03/2021
 * in package - challenges.strings_practice.palindromes
 **/
public class Test {


    String global = "111";

    public static void main(String[] args) {
        var list = new ArrayList<String>();
        list.add("a");
        list.add("t");
        list.add("b");
        list.add("c");
        list.add("z");
        list.forEach(System.out::println);
        list.sort(String::compareTo);
        list.forEach(System.out::println);

        list.removeIf(a -> a.equals("z"));
        list.forEach(System.out::println);

        BigDecimal bd = BigDecimal.valueOf(12.7222);
        bd = bd.add(BigDecimal.valueOf(2)).setScale(2, RoundingMode.HALF_UP);
        System.out.println("Big Decimal : " + bd);
        Test ct = new Test();
        System.out.print(ct.parse("333"));

        java.time.temporal.ChronoField.values();



    }

        public int parse(String arg) {
            var value = 0;
            try {
                String global = arg;
                value = Integer.parseInt(global);
            } catch (Exception e) {
                System.out.println(e.getClass());
            }
            System.out.print(global + " " + value + " ");
            return value;
        }
    }

