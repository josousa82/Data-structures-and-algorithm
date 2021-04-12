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

package ocp.book.chapter8.io.serialization.objects;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by sousaJ on 04/04/2021
 * in package - ocp.book.chapter8.io.serialization.objects
 **/
public class Gorilla implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private Boolean friendly;
    private transient String favoriteFood;

    public Gorilla(String name, int age, Boolean friendly) {
        this.name = name;
        this.age = age;
        this.friendly = friendly;
    }

    public String getName() {
        return name;
    }

    public Gorilla setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Gorilla setAge(int age) {
        this.age = age;
        return this;
    }

    public Boolean getFriendly() {
        return friendly;
    }

    public Gorilla setFriendly(Boolean friendly) {
        this.friendly = friendly;
        return this;
    }

    public String getFavoriteFood() {
        return favoriteFood;
    }

    public Gorilla setFavoriteFood(String favoriteFood) {
        this.favoriteFood = favoriteFood;
        return this;
    }

    @Override
    public String toString() {
        return "[" + "name=" + name  + ", age=" + age + ", friendly=" + friendly + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gorilla)) return false;
        Gorilla gorilla = (Gorilla) o;
        return age == gorilla.age && Objects.equals(name, gorilla.name) && Objects.equals(friendly, gorilla.friendly) && Objects.equals(favoriteFood, gorilla.favoriteFood);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, friendly, favoriteFood);
    }
}
