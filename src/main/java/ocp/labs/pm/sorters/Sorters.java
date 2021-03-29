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

package ocp.labs.pm.sorters;

import ocp.labs.pm.data.Product;

import java.util.Comparator;

/**
 * Created by sousaJ on 27/03/2021
 * in package - ocp.labs.pm.sorters
 **/
public interface Sorters {
    Comparator<Product> sortByRating = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
    Comparator<Product> sortByPrice = Comparator.comparing(Product::getPrice);

}
