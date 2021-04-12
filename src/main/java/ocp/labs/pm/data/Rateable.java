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

import java.io.Serializable;

/**
 * Created by sousaJ on 20/03/2021
 * in package - ocp.labs.pm.data
 **/

// Functional interfaces only compile with one abstract method
// Can contain (like all interfaces), static, private and default methods
// Can contain static final variables
// Remember that if there is a class impplements more than one interface
// and default method are declared in the interfaces, with the same signature,
// the concrete class will have to override the default method implementation
// this is s tricky case to remember. Diamond problem, the method definition
// will be ambigous, because the compiler wont know which implementation to
// to use, and wont compile unless an implementation of the method is provided
// by the concrete class.

@FunctionalInterface
public interface Rateable<T> extends Serializable {
    public static final Rating DEFAULT_RATING = Rating.NOT_RATED;

    public static Rating convert(int stars){
        return (stars >= 0 && stars <= 5)?Rating.values()[stars]: DEFAULT_RATING;
    }

    public abstract T applyRating(Rating rating);

    default Rating getRating(){
        return DEFAULT_RATING;
    }

    public default T applyRating(int value){
        return applyRating(Rateable.convert(value));
    }


}
