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

package ocp.book.chapter8.io.serialization.app;

import ocp.book.chapter8.io.serialization.objects.Chimpanzee;
import ocp.book.chapter8.io.serialization.objects.Gorilla;
import ocp.book.chapter8.io.serialization.services.SerializationService;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by sousaJ on 04/04/2021
 * in package - ocp.book.chapter8.io.serialization.app
 **/
public class Main {

    private static final SerializationService serializationService = new SerializationService();

    public static void main(String[] args) throws IOException, URISyntaxException {
        // this method is using try with resources which closes the stream
        // however System.in and out are System streams and shoulnd't be closed
        // because they wont be available for the rest of hte program.
        // running this method before the the rest of the code will cause an IOException
        // the JVM manages the System resources for us
        //Remmber the exception is thrown by the next callers and not the System methods..
        // system methods use checkError() and do not throw any exception

//        consoleInputExample();
        var gorillas = new ArrayList<Gorilla>();
        gorillas.add(new Gorilla("Grodd", 5, false));
        gorillas.add(new Gorilla("Ismahel", 8, true));
        File datafile = new File("gorillas2.data");

        String hash = serializationService.saveToFile(gorillas, datafile);
        System.out.println("hash = " + hash);

        var gorillasFromDisk = serializationService.readFromFile(datafile);
        System.out.println("gorillasFromDisk = " + gorillasFromDisk);

        printChimpanzee();

        System.out.format("test string : %s %ntest decimal: %d%n%n", "string", 5);

        System.out.println("type your name:\n");
        var reader  = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("reader.readLine() = " + reader.readLine());

//        consoleInputExample();

        // Remember: console class is a singleton, it is only accessible from factory methods
        // The exam may try to trick in instantiate the class
        Console console = System.console();
        if(console != null){
            String userInput = console.readLine();
            console.writer().println("You entered:" + userInput);
        }else {
            System.err.println("Console not available.");
        }
        
//        Path p5 = Paths.get(new URI("http://www.wiley.com"));
//        System.out.println("p5.toString() = " + p5.toString());
    }

    private static void consoleInputExample() {
        try(var reader  = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("type your name:\n");
            System.out.println("reader.readLine() = " + reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printChimpanzee(){
        var chimpanzees = new ArrayList<Chimpanzee>();
        chimpanzees.add(new Chimpanzee("Ham", 2, 'A'));
        chimpanzees.add(new Chimpanzee("Enos", 4, 'B'));

        var dataFile = new File("chimpanzee.data");

        serializationService.saveToFile(chimpanzees, dataFile);
        var chimpanzeesFromDisk = serializationService.readFromFile(dataFile);

        System.out.println("chimpanzeesFromDisk = " + chimpanzeesFromDisk);
    }
}
