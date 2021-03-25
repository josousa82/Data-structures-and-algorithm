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
import ocp.labs.pm.data.ProductManager;
import ocp.labs.pm.data.Rating;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * {@code} class represents an application that manages products
 * @version 4.0
 *  @author oracle
 */
public class Shop {

    public static void main(String[] args) {

        ProductManager pm  = new ProductManager(Locale.UK);

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(153.57), Rating.NOT_RATED);
        pm.printProductReport(p1);
        pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea.");
        pm.reviewProduct(p1, Rating.TWO_STAR, "Could be nicer");
        pm.reviewProduct(p1, Rating.FIVE_STAR, "Just a cup of tea.");
        pm.reviewProduct(p1, Rating.FOUR_STAR, "Bad cup of tea.");
        pm.printProductReport(p1);

        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
        p2 = pm.reviewProduct(p2, Rating.FOUR_STAR, "Very good");
        p2 = pm.reviewProduct(p2, Rating.THREE_STAR, "Fair");
        p2 = pm.reviewProduct(p2, Rating.ONE_STAR, "Very Bad");
        p2 = pm.reviewProduct(p2, Rating.NOT_RATED, "");
        pm.printProductReport(p2);

        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(1.99), Rating.FOUR_STAR, LocalDateTime.now().plusDays(2));
        p3 = pm.reviewProduct(p3, Rating.FOUR_STAR, "good");
        p3 = pm.reviewProduct(p3, Rating.ONE_STAR, "bad");
        p3 = pm.reviewProduct(p3, Rating.ONE_STAR, "Very Bad");
        p3 = pm.reviewProduct(p3, Rating.FIVE_STAR, "Very good");
        pm.printProductReport(p3);



//        Product p4 = pm.createProduct( 104,  "Cake Red Velvet", BigDecimal.valueOf(4.99), Rating.FIVE_STAR, LocalDate.now());
//        Product p5 = p3.applyRating(Rating.THREE_STAR);
//        Product p6 = pm.createProduct(106, "Chocolate", BigDecimal.valueOf(3.99), Rating.FIVE_STAR);
//        Product p7 = pm.createProduct( 106,  "Chocolate", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
//        Product p8 = p4.applyRating(Rating.FIVE_STAR);
//        Product p9 = p1.applyRating(Rating.TWO_STAR);
//        System.out.println("p6.equals(p7) = " + p6.equals(p7));

//        System.out.println("p1.bestBefore() = " + p1.getBestBefore());
//        System.out.println("p3.bestBefore() = " + p7.getBestBefore());

        // because besBefore is specific to food
        // commented because bestBefore is defined in the abstract product and is inherited by all subtypes
//        if(p3 instanceof Food){
//            System.out.println("((Food) p3).getBestBefore() = " + ((Food) p3).getBestBefore());
//        }


//        System.out.println("p1 = " + p1);
//        System.out.println("p2 = " + p2);
//        System.out.println("p3 = " + p3);
//        System.out.println("p4 = " + p4);
//        System.out.println("p5 = " + p5);
//        System.out.println("p6 = " + p6);
//        System.out.println("p7 = " + p7);
//        System.out.println("p8 = " + p8);
//        System.out.println("p8 = " + p9);



//        printProduct(p1);
//        printProduct(p2);
//        printProduct(p3);
//        printProduct(p4);
//        printProduct(p5);



    }

    private static void printProduct(Product p1) {
        String formatPattern = "id: {0} - Product: {1} with price {2} and a discount of {3} has the final price of {4}.\nProduct Rating: {5}";
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.UK);
        String messageFormatted = MessageFormat.format(formatPattern, p1.getId(), p1.getName(),
                                                       currencyFormat.format(p1.getPrice()),
                                                       currencyFormat.format(p1.getDiscount()),
                                                       currencyFormat.format((p1.getPrice().subtract(p1.getDiscount()))), p1.getRating().getStars());
        System.out.println(messageFormatted);
    }
}
