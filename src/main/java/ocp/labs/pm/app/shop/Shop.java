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

package ocp.labs.pm.app.shop;

import ocp.labs.pm.data.Product;
import ocp.labs.pm.data.ProductFilters;
import ocp.labs.pm.data.ProductManager;
import ocp.labs.pm.data.exceptions.ProductManagerException;
import ocp.labs.pm.sorters.Sorters;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static java.lang.System.out;

/**
 * {@code} class represents an application that manages products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {

    public static void main(String[] args) throws ProductManagerException, IOException {
        var pt = "pt-PT";
        var en = "en-GB";
        ProductManager pm = new ProductManager(en);

        pm.printProducts(ProductFilters.nonNull, ProductFilters.sortById);
        pm.printProducts(ProductFilters.filterAllDrink.and(ProductFilters.priceBelowTwo), Sorters.sortByRating.thenComparing(Sorters.sortByPrice));
        pm.printProducts(ProductFilters.filterAllFood, Sorters.sortByRating);
        pm.printProducts(ProductFilters.priceBelowTwo,  Sorters.sortByPrice.reversed());

        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));

    }

    private static void printProduct(Product p1) {
        String formatPattern = "id: {0} - Product: {1} with price {2} and a discount of {3} has the final price of {4}.\nProduct Rating: {5}";
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        String messageFormatted = MessageFormat.format(formatPattern, p1.getId(), p1.getName(), currencyFormat.format(p1.getPrice()), currencyFormat.format(p1.getDiscount()), currencyFormat.format((p1.getPrice().subtract(p1.getDiscount()))), p1.getRating().getStars());
        out.println(messageFormatted);
    }
}
