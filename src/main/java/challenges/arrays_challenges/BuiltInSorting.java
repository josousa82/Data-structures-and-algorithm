/*
 * Copyright (c) 2021 sousaJ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package challenges.arrays_challenges;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by sousaJ on 22/03/2021
 * in package - challenges.arrays_challenges
 **/
public class BuiltInSorting {

    public static void main(String[] args) {
        Melon[] melons = {
                Melon.of("yellow", BigDecimal.valueOf(2)),
                Melon.of("red",  BigDecimal.valueOf(1)),
                Melon.of("yellow", BigDecimal.valueOf(1.25)),
                Melon.of("yellow", BigDecimal.valueOf(4)),
                Melon.of("yellow", BigDecimal.valueOf(10)),
        };
//        Comparator<Melon> compareByWeight = (o1, o2) -> Integer.compare(o1.getWeight(), o2.getWeight());

        CustomSort<Melon> sorted = new CustomSort();

        System.out.println("sorted 1: ");
        Arrays.stream(melons).forEach(melon -> {
            System.out.printf("melon.getType() = %s melon.getWeight()  = %s ", melon.getType(), melon.getWeight() );
        });

//        Arrays.sort(melons, compareByWeight);

        System.out.println("sorted: ");

        Arrays.stream(melons).forEach(melon -> {
            System.out.printf("melon.getType() = %s melon.getWeight()  = %s ", melon.getType(), melon.getWeight() );
        });

    }



}

class CustomSort<T> {

    public void sort(T [] array, Comparator<T> comparator){
        Arrays.sort(array, comparator);
    }
}

@Setter
@Getter
@Builder
class Melon {
    private final String type;
    private final BigDecimal weight;

    private Melon(String type, BigDecimal weight) {
        this.type = type;
        this.weight = weight;
    }

    public static Melon of(String type, BigDecimal weight){
        return new Melon(type, weight);
    }
}