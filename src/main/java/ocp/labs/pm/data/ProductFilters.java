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

package ocp.labs.pm.data;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by sousaJ on 29/03/2021
 * in package - ocp.labs.pm.data
 **/
public interface ProductFilters {

    Predicate<Product> filterAllDrink = product -> product instanceof Drink;
    Predicate<Product> filterAllFood = product -> product instanceof Food;
    Predicate<Product> priceBelowTwo = product -> product.getPrice().floatValue() < 2;
    Predicate<Product> nonNull = Objects::nonNull;
    Comparator<Product> sortById = (o1, o2) -> o2.getId() - o1.getId();

}
