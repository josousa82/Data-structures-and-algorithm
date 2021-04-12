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
 * Created by sousaJ on 05/04/2021
 * in package - ocp.book.chapter8.io.serialization.objects
 **/
public class Chimpanzee implements Serializable {
    private static final long serialUID = 2L;
    private static char type = 'C';
    private transient String name;
    private transient int age = 10;

    {
        this.age = 14;
        System.out.println("instance initializer");
    }

    public Chimpanzee() {
        this.name = "Unknown";
        this.age = 12;
        this.type = 'Q'; // attention to this.. may throw a compile  error due to the this keyword, although type
        System.out.println("NoArgs constructor initialization ");
    }
    public Chimpanzee(String name, int age, char type) {
        this.name = name;
        this.age = age;
        this.type = type;
        System.out.println(" All args constructor initialization ");
    }

    public static char getType() {
        return type;
    }

    public static void setType(char type) {
        Chimpanzee.type = type;
    }

    public String getName() {
        return name;
    }

    public Chimpanzee setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Chimpanzee setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chimpanzee)) return false;
        Chimpanzee that = (Chimpanzee) o;
        return age == that.age && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return '[' + "name=" + name + ", age=" + age + ", type=" + type + ']';
    }
}
