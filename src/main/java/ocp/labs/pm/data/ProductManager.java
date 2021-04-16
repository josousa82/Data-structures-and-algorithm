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

import ocp.labs.pm.data.exceptions.ProductManagerException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/
public class ProductManager {

    private static final Logger logger = Logger.getLogger(ocp.labs.pm.data.ProductManager.class.getName());
    private ResourceBundle config = ResourceBundle.getBundle("config/config");

    private Path reportsFolder = Path.of(config.getString("reports.folder"));
    private Path productDataFolder = Path.of(config.getString("data.folder.products"));
    private Path reviewsDataFolder = Path.of(config.getString("data.folder.reviews"));

    private Path tempFolder = Path.of(config.getString("temp.folder"));

    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));

    private static Map<String, ResourceFormatter> formatters = Map.of(
            "en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
            "pt-PT", new ResourceFormatter(new Locale("pt", "PT")),
            "zh-CN", new ResourceFormatter(Locale.CHINA));

    private ResourceFormatter formatter;
    private Map<Product, List<Review>> products = new HashMap<>();

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
        loadAllData();
    }

    public static Set<String> getSupportedLocales(){
        return formatters.keySet();
    }

    // change languageTag to enum returning string tags to restrict inputs
    public void changeLocale(String languageTag){
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public Product findProductById(int id) throws ProductManagerException {
        return products.keySet()
                       .stream()
                       .filter(product -> product.getId() == id)
                       .findFirst()
                       .orElseThrow(() -> new ProductManagerException(String.format("Product with %d not found.", id)));
    }

    public void printProductReport(Product product) throws IOException {

        var reviews = products.get(product);
        var productFile = reportsFolder.resolve(
                MessageFormat.format(config.getString("report.file"), product.getId())
        );
        try(var out = new PrintWriter(new OutputStreamWriter(
                        Files.newOutputStream(productFile, StandardOpenOption.CREATE), StandardCharsets.UTF_8) {
        })) {
            out.append(formatter.formatProduct(product))
               .append(System.lineSeparator());

            if (reviews.isEmpty())
                out.append(formatter.getText("no.reviews"))
                   .append(System.lineSeparator());
            else
                out.append(reviews.stream()
                                   .sorted()
                                   .map(review -> formatter.formatReview(review)
                                                           .concat(System.lineSeparator()))
                                   .collect(Collectors.joining()));

        }
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter){
        var txt = new StringBuilder();
                products.keySet()
                        .stream()
                        .sorted(sorter)
                        .filter(filter)
                        .forEach(product -> txt.append(formatter.formatProduct(product)).append("\n"));

        out.println(txt);
    }

    public Product  createProduct(TypeOfProduct typeOfProduct, int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore){
        if(typeOfProduct.equals(TypeOfProduct.FOOD))
            return createFoodProduct(id, name, price, rating, bestBefore);
        else if(typeOfProduct.equals(TypeOfProduct.DRINK))
           return createDrinkProduct(id, name, price, rating, bestBefore);
        return null;
    }

    private Product createFoodProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    private Product createDrinkProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Drink(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }



    private void parseReview(String text)  {
        try {
            Object[] values = reviewFormat.parse(text);
            reviewProduct(Integer.parseInt(((String) values[0]).trim()),
                          Rateable.convert(Integer.parseInt(((String) values[1]).trim())),
                          ((String) values[2]).trim());

        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review: ".concat(text));
//            throw new ProductManagerException("Unable to parse review");
        }
    }

    public Review parseSingleReview(String text)  {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);
            review = new Review(Rateable.convert(Integer.parseInt(((String) values[0]).trim())),
                          ((String) values[1]).trim());
        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review: ".concat(text));
//            throw new ProductManagerException("Unable to parse review");
        }
        return review;
    }

    public List<Review> loadReviews(Product product) {

        List<Review> reviews = null;
        Path file = reviewsDataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));

        if(Files.notExists(file)) reviews = new ArrayList<>();
        else
            try(Stream<String> fileText = Files.lines(file, StandardCharsets.UTF_8)) {
                reviews =  fileText.map(this::parseSingleReview)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error loading reviews".concat(e.getMessage()));
            }
        return reviews;
    }

    private void parseProduct(String text){
        try{
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt(((String) values[1]).trim());
            String name = ((String) values[2]).trim();
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(((String) values[3]).trim()));
            Rating rating = Rateable.convert(Integer.parseInt(((String) values[4]).trim()));
            LocalDate bestBefore = LocalDate.parse(((String) values[5]).trim());
            Product product = null;
            switch ((String) values[0]) {
                case "D":
                     createProduct(TypeOfProduct.DRINK,id, name, price, rating,bestBefore);
                    break;
                case "F":
                    createProduct(TypeOfProduct.FOOD,id, name, price, rating,bestBefore);
                    break;
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product: ".concat(text), e.getStackTrace());

        }
    }

    public Product parseSingleProduct(String text){
        Product product = null;
        try{
            Object[] values = productFormat.parse(text);
            int id = Integer.parseInt(((String) values[1]).trim());
            String name = ((String) values[2]).trim();
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(((String) values[3]).trim()));
            Rating rating = Rateable.convert(Integer.parseInt(((String) values[4]).trim()));
            LocalDate bestBefore = LocalDate.parse(((String) values[5]).trim());

            switch ((String) values[0]) {
                case "D":
                    product = new Drink(id, name, price, rating,bestBefore );
                    break;
                case "F":
                    product = new Food(id, name, price, rating,bestBefore);
                    break;
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product: ".concat(text), e.getStackTrace());

        }
        return product;
    }

    private Product loadProduct(Path file){
        Supplier<Exception> exception = () -> new NoSuchElementException("Missing data in Product file with name ".concat(file.getFileName().toString()));
        Product product = null;
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)){
            product = parseSingleProduct(lines.findFirst().orElseThrow(exception));

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error loading the product {}", e.getMessage());
        }
        return product;
    }

    private void loadAllData(){
        try  {
            products =  Files.list(productDataFolder)
                    .filter(file -> file.getFileName().toString().startsWith("product"))
                    .map(this::loadProduct)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(product -> product, product -> loadReviews(product)));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading data {}", e.getMessage());
            e.getStackTrace();
        }
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProductById(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
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
        try {
            printProductReport(findProductById(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error printing the product report ".concat(e.getMessage()), e);
        }
    }

    public  Map<String, String> getDiscounts() {

        Function<Product, String> ratingAsStarsStringForEach = product -> product.getRating().getStars();
        ToDoubleFunction<Product> toDouble = product -> product.getDiscount().doubleValue();
        Function<Double, String> finisher = discount -> formatter.currencyFormat.format(discount);

        return products.keySet()
                .stream()
                .collect(Collectors.groupingBy(ratingAsStarsStringForEach,
                                            Collectors.collectingAndThen(
                                                    Collectors.summingDouble(toDouble), finisher)));
    }



    private static class ResourceFormatter {

        private Locale locale;
        private ResourceBundle resourceBundle;
        private DateTimeFormatter dateTimeFormatter;
        private NumberFormat currencyFormat;


        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            resourceBundle = ResourceBundle.getBundle("translations", locale);
            dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
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
