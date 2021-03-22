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

/**
 * Created by sousaJ on 18/03/2021
 * in package - ocp.labs.pm.app.shop
 **/

/*
*
* */
public enum Rating {
    NOT_RATED( "\u2606\u2606\u2606\u2606\u2606"),
    ONE_STAR(  "\u2605\u2606\u2606\u2606\u2606"),
    TWO_STAR(  "\u2605\u2605\u2606\u2606\u2606"),
    THREE_STAR("\u2605\u2605\u2605\u2606\u2606"),
    FOUR_STAR( "\u2605\u2605\u2605\u2605\u2606"),
    FIVE_STAR( "\u2605\u2605\u2605\u2605\u2605");

    private String stars;

    Rating(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }
}
