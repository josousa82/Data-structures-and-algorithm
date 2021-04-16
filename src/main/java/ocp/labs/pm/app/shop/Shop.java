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
import ocp.labs.pm.data.exceptions.ProductManagerException;
import ocp.labs.pm.sorters.Sorters;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

import static java.lang.System.out;

/**
 * {@code} class represents an application that manages products
 *
 * @author oracle
 * @version 4.0
 */
public class Shop {

    public static void main(String[] args) throws ProductManagerException {
        var pt = "pt-PT";
        var en = "en-GB";
        ProductManager pm = new ProductManager(en);

        pm.printProductReport(101);
        pm.printProductReport(42);
        pm.printProductReport(102);
        pm.printProductReport(103);

        pm.changeLocale(pt);

        pm.printProductReport(104);
        pm.printProductReport(105);

        pm.changeLocale(en);
        pm.printProductReport(106);
        pm.printProductReport(107);

        Comparator<Product> sortByRating = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
        Comparator<Product> sortByPrice = Comparator.comparing(Product::getPrice);

        pm.printProducts(ProductFilters.filterAllDrink.and(ProductFilters.priceBelowTwo), sortByRating.thenComparing(sortByPrice));
        pm.printProducts(ProductFilters.filterAllFood, Sorters.sortByRating);
        pm.printProducts(ProductFilters.priceBelowTwo,  Sorters.sortByPrice.reversed());

        pm.getDiscounts().forEach((rating, discount) -> out.println(rating + "\t" + discount));

        pm.createProduct(TypeOfProduct.DRINK, 108, "Whisky", BigDecimal.valueOf(53.57), Rating.NOT_RATED, LocalDate.of(2020, 6,30));
        pm.reviewProduct(108, Rating.FOUR_STAR, "Nice hot cup of Whisky.");
        pm.reviewProduct(108, Rating.TWO_STAR, "Could be nicer");
        pm.reviewProduct(108, Rating.FIVE_STAR, "Just a cup of Whisky.");
        pm.reviewProduct(108, Rating.FOUR_STAR, "Bad cup of Whisky.");
        pm.printProductReport(108);

        pm.printProducts(Objects::nonNull, (o1, o2) -> o2.getId() - o1.getId());

//        pm.parseProduct("F,101,Tea,153.57,0"); error write a test to this code
//        pm.parseProduct("F,101,Tea,153.57,0,2021-04-30");
//        pm.parseReview("101,5,Nice hot cup of tea.");
//        pm.parseReview("101,2,Could be nicer");
//        pm.parseReview("101,4,Just a cup of tea.");
//        pm.parseReview("101,2,Bad cup of tea.");


//
//        pm.parseProduct("F,103,Cake, 55.67,0,2021-04-30");
//        pm.printProductReport(103);
//        pm.changeLocale(pt);


//        pm.parseProduct("D, 102, Coffee, 1.99,4, 2021-04-15");
//        pm.parseReview("102,4, Very good");
//        pm.parseReview("102,3, Fair");
//        pm.parseReview("102,1, Very Bad");
//        pm.parseReview("102,0,");


//        pm.parseProduct( "F, 107,Cake,1.99,4,2021-07-20");
//        pm.parseReview("107, 4, good");
//        pm.parseReview("107, 1, bad");
//        pm.parseReview("107, 1, Very Bad");
//        pm.parseReview("107, 5, Very good");




//       pm.parseProduct("F,104,Cake Red Velvet,4.99,5,2021-04-18");
//       pm.parseReview("104, 1, Not too good");
//       pm.parseReview("104, 1, Bad enough");
//       pm.parseReview("104, 1, Really bad");
//       pm.parseReview("104, 3, Not too bad");
//        pm.printProductReport(104);


//        pm.parseProduct("F,105, Chocolate, 3.99, 5,2021-05-13");
//        pm.parseReview("105, 1, Not too good");
//        pm.parseReview("105, 1, Really bad");
//        pm.printProductReport(105);

//        pm.parseProduct("D,106,Coca-Cola, 2.79, 4,2021-04-10");
//        pm.parseReview("106, 5, Tasty");
//        pm.parseReview("106, 5, Really good");
//        pm.printProductReport(106);

//        Comparator<Product> sortByRating = (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal();
//        Comparator<Product> sortByPrice = Comparator.comparing(Product::getPrice);
//
//        pm.printProducts(ProductFilters.filterAllDrink.and(ProductFilters.priceBelowTwo), sortByRating.thenComparing(sortByPrice));
//        pm.printProducts(ProductFilters.filterAllFood, Sorters.sortByRating);
////        pm.printProducts(ProductFilters.priceBelowTwo,  Sorters.sortByPrice.reversed());
//
//
//        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));




    }

    private static void printProduct(Product p1) {
        String formatPattern = "id: {0} - Product: {1} with price {2} and a discount of {3} has the final price of {4}.\nProduct Rating: {5}";
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        String messageFormatted = MessageFormat.format(formatPattern, p1.getId(), p1.getName(), currencyFormat.format(p1.getPrice()), currencyFormat.format(p1.getDiscount()), currencyFormat.format((p1.getPrice().subtract(p1.getDiscount()))), p1.getRating().getStars());
        out.println(messageFormatted);
    }
}
