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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/
public class ProductManager {

    private static Map<String, ResourceFormatter> formatters = Map.of(
            "en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
            "pt-PT", new ResourceFormatter(new Locale("pt", "PT")),
            "zh-CN", new ResourceFormatter(Locale.CHINA));

    private ResourceFormatter formatter;
    private HashMap<Product, List<Review>> products = new HashMap<>();

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    // change languageTag to enum returning string tags to restrict inputs
    public void changeLocale(String languageTag){
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public Product findProductById(int id) {
        return products.keySet()
                       .stream()
                       .filter(product -> product.getId() == id)
                       .findFirst()
                       .orElseGet(null);
    }

    public void printProductReport(Product product) {
        var txt = new StringBuilder();
        var reviews = products.get(product);

        txt.append(formatter.formatProduct(product)).append("\n");

        if (reviews.isEmpty())
            txt.append(formatter.getText("no.reviews")).append("\n");
        else
            txt.append(reviews.stream()
                              .sorted()
                              .map(review -> formatter.formatReview(review).concat("\n"))
                              .collect(Collectors.joining())
            );

//        Collections.sort(reviews);
          // this solution is no good to work in parallel because it may corrupt the origin stream
//        reviews.forEach(review -> txt.append(formatter.formatReview(review)).append("\n"));

        System.out.println(txt);
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter){
        var txt = new StringBuilder();
                products.keySet()
                        .stream()
                        .sorted(sorter)
                        .filter(filter)
                        .forEach(product -> txt.append(formatter.formatProduct(product)).append("\n"));

        System.out.println(txt);
    }

    public Product  createProduct(TypeOfProduct typeOfProduct, int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore){
        if(typeOfProduct.equals(TypeOfProduct.FOOD))
            return createFoodProduct(id, name, price, rating, bestBefore);
        else if(typeOfProduct.equals(TypeOfProduct.DRINK))
           return createDrinkProduct(id, name, price, rating, bestBefore);
        return null;
    }

    private Product createFoodProduct(int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    private Product createDrinkProduct(int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore) {
        Product product = new Drink(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        return reviewProduct(findProductById(id), rating, comments);
    }

    // A more generic alternative  design of this app could  have used a type Rateable instead of product
    // for both instance variable and method argument (for example an abstract class could contain the implementation)
    // to enable the app to create reviews of any other objects that implement the Rateable interface
    public Product reviewProduct(Product product, Rating rating, String comments) {

        List<Review> reviews = products.get(product); //extract the list
        products.remove(product, reviews);            // remove it from products
        reviews.add(new Review(rating, comments));    // Add new review to the list

      product = product.applyRating(                 // apply the rating to product which is the list of reviews
                  Rateable.convert(
                          (int) Math.round(
                                    reviews.stream()
                                           .mapToInt(r -> r.getRating().ordinal())
                                           .average()
                                           .orElse(0))));

        products.put(product, reviews); // add product and the review to the Map
        return product;
    }

    public void printProductReport(int id) {
        printProductReport(findProductById(id));
    }

    public  Map<String, String> getDiscounts() {

        Function<Product, String> ratingAsStarsStringForEach = product -> product.getRating().getStars();
        ToDoubleFunction<Product> toDouble = product -> product.getDiscount().doubleValue();



        return products.keySet()
                .stream()
                .collect(Collectors.groupingBy(ratingAsStarsStringForEach,
                                            Collectors.collectingAndThen(
                                                    Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                                                       discount -> formatter.currencyFormat.format(discount))));
    }

    private static class ResourceFormatter {

        private Locale locale;
        private ResourceBundle resourceBundle;
        private DateTimeFormatter dateTimeFormatter;
        private NumberFormat currencyFormat;

        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            resourceBundle = ResourceBundle.getBundle("translations", locale);
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(locale);
            currencyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(
                    resourceBundle.getString("product"),
                    product.getName(),
                    currencyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateTimeFormatter.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(
                    resourceBundle.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resourceBundle.getString(key);
        }


    }
}
