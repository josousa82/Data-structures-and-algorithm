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
import java.time.LocalDateTime;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/
public final class Food extends Product {

    private LocalDateTime bestBefore;

    Food(int id, String name, BigDecimal price, Rating rating, LocalDateTime bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    /**
     * Get the value of date for hte Product
     *
     * @return the value of bestBefore
     */
    public LocalDateTime getBestBefore() {
        return bestBefore;
    }

    @Override
    public BigDecimal getDiscount() {
        return (bestBefore.isEqual(LocalDateTime.now().plusDays(1)))?super.getDiscount():BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + bestBefore ;
    }
}
