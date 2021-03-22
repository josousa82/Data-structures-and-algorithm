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

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/
public class ProductManager {

    private Locale locale;
    private ResourceBundle resourceBundle;
    private DateTimeFormatter dateTimeFormatter;
    private NumberFormat currencyFormat;

    private Product product;
    private Review[] reviews = new Review[5];


    public ProductManager(Locale l) {
        this.locale = l;
        resourceBundle = ResourceBundle.getBundle("resources", locale);
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(locale);
        currencyFormat = NumberFormat.getCurrencyInstance(locale);

    }

    public void printProductReport() {
        StringBuilder txt = new StringBuilder();
        txt.append(MessageFormat.format(resourceBundle.getString("product"), product.getName(),
                                        currencyFormat.format(product.getPrice()), product.getRating().getStars(),
                                        dateTimeFormatter.format(product.getBestBefore()))).append("\n");
        if(reviews[0] == null) txt.append(resourceBundle.getString("no.reviews")).append("\n");

        for (Review review : reviews) {
            if (review == null) {
                break;
            }
            txt.append(MessageFormat.format(resourceBundle.getString("review"), review.getRating().getStars(), review.getComments())).append("\n");
        }

        System.out.println(txt);
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore) {
        product = new Food(id, name, price, rating, bestBefore);
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        product = new Drink(id, name, price, rating);
        return product;
    }

    // A more generic alternative  design of this app could  have used a type Rateable instead of product
    // for both instance variable and method argument (for example an abstract class could contain the implementation)
    // to enable the app to create reviews of any other objects that implement the Rateable interface
    public Product reviewProduct(Product product, Rating rating, String comments) {

        if (reviews[reviews.length - 1] != null) {
            reviews = Arrays.copyOf(reviews, reviews.length + 5);
        }
        int sum = 0, i = 0;
        boolean reviewed = false;

        while (i < reviews.length && !reviewed) {
            if (reviews[i] == null) {
                reviews[i] = new Review(rating, comments);
                reviewed = true;
            }
            sum += reviews[i].getRating().ordinal();
            i++;
        }

        this.product = product.applyRating(Rateable.convert(Math.round((float) sum / i)));
        return this.product;
    }
}
