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

import ocp.labs.pm.data.*;
import ocp.labs.pm.sorters.Sorters;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;

/**
 * {@code} class represents an application that manages products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {

    public static void main(String[] args) {
        var pt = "pt-PT";
        var en = "en-GB";
        ProductManager pm = new ProductManager(en);

        pm.createProduct(TypeOfProduct.DRINK, 101, "Tea", BigDecimal.valueOf(153.57), Rating.NOT_RATED, LocalDateTime.now());
        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea.");
        pm.reviewProduct(101, Rating.TWO_STAR, "Could be nicer");
        pm.reviewProduct(101, Rating.FIVE_STAR, "Just a cup of tea.");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Bad cup of tea.");
//        pm.printProductReport(101);

//        pm.changeLocale(pt);


        pm.createProduct(TypeOfProduct.DRINK, 102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR, LocalDateTime.now());
        pm.reviewProduct(102, Rating.FOUR_STAR, "Very good");
        pm.reviewProduct(102, Rating.THREE_STAR, "Fair");
        pm.reviewProduct(102, Rating.ONE_STAR, "Very Bad");
        pm.reviewProduct(102, Rating.NOT_RATED, "");
//        pm.printProductReport(102);

//        pm.changeLocale(en);
        pm.createProduct( TypeOfProduct.FOOD, 103, "Cake", BigDecimal.valueOf(1.99), Rating.FOUR_STAR, LocalDateTime.now().plusDays(1));
        pm.reviewProduct(103, Rating.FOUR_STAR, "good");
        pm.reviewProduct(103, Rating.ONE_STAR, "bad");
        pm.reviewProduct(103, Rating.ONE_STAR, "Very Bad");
        pm.reviewProduct(103, Rating.FIVE_STAR, "Very good");
//        pm.printProductReport(103);


       pm.createProduct(TypeOfProduct.FOOD, 104, "Cake Red Velvet", BigDecimal.valueOf(4.99), Rating.FIVE_STAR, LocalDateTime.now().plusDays(1));
       pm.reviewProduct(104, Rating.ONE_STAR, "Not too good");
       pm.reviewProduct(104, Rating.ONE_STAR, "Bad enough");
       pm.reviewProduct(104, Rating.ONE_STAR, "Really bad");
       pm.reviewProduct(104, Rating.THREE_STAR, "Not too bad");

        pm.createProduct(TypeOfProduct.FOOD,105, "Chocolate", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDateTime.now().plusDays(2));
        pm.reviewProduct(105, Rating.ONE_STAR, "Not too good");
        pm.reviewProduct(105, Rating.ONE_STAR, "Really bad");

        pm.createProduct(TypeOfProduct.DRINK,106, "Coca-Cola", BigDecimal.valueOf(2.79), Rating.FOUR_STAR, LocalDateTime.now().plusDays(2));
        pm.reviewProduct(106, Rating.FIVE_STAR, "Tasty");
        pm.reviewProduct(106, Rating.FIVE_STAR, "Really good");

        Comparator<Product> sortByRating = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        Comparator<Product> sortByPrice = Comparator.comparing(Product::getPrice);

        pm.printProducts(ProductFilters.filterAllDrink.and(ProductFilters.priceBelowTwo), sortByRating.thenComparing(sortByPrice));
        pm.printProducts(ProductFilters.filterAllFood,  Sorters.sortByRating.reversed());
        pm.printProducts(ProductFilters.priceBelowTwo,  Sorters.sortByPrice.reversed());



        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));





    }

    private static void printProduct(Product p1) {
        String formatPattern = "id: {0} - Product: {1} with price {2} and a discount of {3} has the final price of {4}.\nProduct Rating: {5}";
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        String messageFormatted = MessageFormat.format(formatPattern, p1.getId(), p1.getName(), currencyFormat.format(p1.getPrice()), currencyFormat.format(p1.getDiscount()), currencyFormat.format((p1.getPrice().subtract(p1.getDiscount()))), p1.getRating().getStars());
        System.out.println(messageFormatted);
    }
}
