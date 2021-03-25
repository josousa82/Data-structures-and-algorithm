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
import java.util.*;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/
public class ProductManager {

    private Locale locale;
    private ResourceBundle resourceBundle;
    private DateTimeFormatter dateTimeFormatter;
    private NumberFormat currencyFormat;

//    private Product product;
//    private Review[] reviews = new Review[5];
    private HashMap<Product, List<Review>> products = new HashMap<>();


    public ProductManager(Locale l) {
        this.locale = l;
        resourceBundle = ResourceBundle.getBundle("resources", locale);
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(locale);
        currencyFormat = NumberFormat.getCurrencyInstance(locale);

    }

    public Product findProductById(int id){
        Set<Product> productsSet = products.keySet();
        Product result = null;

        for (Product p : productsSet){
            if(p.getId() == id){
                result = p;
                break;
            }
        }
        return result;
    }

    public void printProductReport(int id){
        printProductReport(findProductById(id));
    }

    public void printProductReport(Product product) {
        StringBuilder txt = new StringBuilder();
        List<Review> reviews = products.get(product);

        txt.append(MessageFormat.format(resourceBundle.getString("product"), product.getName(),
                                        currencyFormat.format(product.getPrice()), product.getRating().getStars(),
                                        dateTimeFormatter.format(product.getBestBefore()))).append("\n");

        if(reviews.isEmpty()) txt.append(resourceBundle.getString("no.reviews")).append("\n");

        Collections.sort(reviews);

        reviews.forEach(review -> txt.append(MessageFormat.format(resourceBundle.getString("review"),
                                                                  review.getRating().getStars(),
                                                                  review.getComments())).append("\n"));
//        for (Review review : reviews) {
//            txt.append(MessageFormat.format(resourceBundle.getString("review"),
//                                            review.getRating().getStars(),
//                                            review.getComments())).append("\n");
//        }

        System.out.println(txt);
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore) {

        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments){
        return reviewProduct(findProductById(id), rating, comments);
    }
    // A more generic alternative  design of this app could  have used a type Rateable instead of product
    // for both instance variable and method argument (for example an abstract class could contain the implementation)
    // to enable the app to create reviews of any other objects that implement the Rateable interface
    public Product reviewProduct(Product product, Rating rating, String comments) {

        List<Review> reviews = products.get(product);
        if(products.remove(product, reviews)){

        }
        reviews.add(new Review(rating, comments));

        int sum = 0;

//      sum = reviews.stream().mapToInt(value -> value.getRating().ordinal()).sum();

        for(Review review : reviews){
            sum += review.getRating().ordinal();
        }

        product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
        products.put(product, reviews);
        return product;
    }
}
