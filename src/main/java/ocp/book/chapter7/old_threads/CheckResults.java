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

package ocp.book.chapter7.old_threads;

/**
 * Created by sousaJ on 10/04/2021
 * in package - ocp.book.chapter7.old_threads
 **/
public class CheckResults {

    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < 500; i++) {
                CheckResults.counter++;
                System.out.println("i = " + i);
            }
        }).start();

        while (CheckResults.counter < 100) {
            System.out.println("Not reached yet");
            Thread.sleep(1000);
        }

        System.out.println("Reached!");
    }
}
