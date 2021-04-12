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
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * {@code Product} class represents properties and behaviours of
 * <p>
 * product objects in the Product Management System
 * <br>
 * Each product has an id, name, and price
 * <br>
 * Each product can have a discount based on {@link DISCOUNT_RATE discount rate}
 *
 * @author oracle
 * @version 4.0
 */

// abstract classes don't have to implement the abstract methods from interface, however the concrete class
// that implement the abstract class will have to implement all method signatures either from the abstract
// class itself and interface that is implemented by the abstract class
public abstract class Product implements Rateable<Product> {

    /**
     * A constant that defines a
     * {@link java.math.BigDecimal BigDecimal} value of the discount rate
     * <br>
     * Discount rate is 10%
     */
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
    private final int id;
    private final String name;
    private final BigDecimal price;
    private final Rating rating;



     Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }


    /**
     * Calculates discount based on a product price and
     * {@link DISCOUNT_RATE discount rate}
     *
     * @return a {@link java.math.BigDecimal BigDecimal}
     * value of the discount
     */
    public BigDecimal getDiscount() {
        return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     *
     * @param newRating
     * @return factory method
     */
//    public abstract Product applyRating(Rating newRating);

    public LocalDateTime getBestBefore(){
        return LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public Rating getRating() {
        return rating;
    }


    @Override
    public String toString() {
        return  id + ", " + name + ", " + price + ", " +  this.getDiscount() + ", " + rating.getStars() + ", " + this.getBestBefore();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Product){
            final Product that = (Product) obj;
            return this.id == that.getId(); //&& Objects.equals(this.name, that.getName());
        }
       return false;
    }


}
