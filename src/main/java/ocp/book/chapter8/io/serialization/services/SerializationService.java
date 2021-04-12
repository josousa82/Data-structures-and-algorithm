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

package ocp.book.chapter8.io.serialization.services;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by sousaJ on 04/04/2021
 * in package - ocp.book.chapter8.io.serialization.services
 **/
public class SerializationService<T> {

    public static Logger logger = Logger.getLogger(SerializationService.class.getName());

    public String saveToFile(List<T> gorillas, File dataFile) {
        String hash = null;
        try(var byteArrayStream = new ByteArrayOutputStream(); var out = new ObjectOutputStream(
                            new BufferedOutputStream(
                                new FileOutputStream(dataFile)))){

            MessageDigest md = MessageDigest.getInstance("SHA-256"); // video 2 ch13 17m explained, read more about it

            for(var gorilla: gorillas){
                out.writeObject(gorilla);
            }hash = new BigInteger(1, md.digest(byteArrayStream.toByteArray())).toString(16);
           return hash;

        }catch (IOException | NoSuchAlgorithmException e){
            e.getStackTrace();
            Arrays.stream(e.getSuppressed()).forEach(t -> logger.severe(t::getMessage));
            return "";
        }
    }

    public List<T> readFromFile(File dataFile) {

        var list = new ArrayList<T>();
        try(var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(dataFile)))){

            while (true){
                var object = in.readObject();
                    list.add((T) object);
            }


        }catch (EOFException e){
            e.getStackTrace();
            logger.info("File has ended;");

        }catch (IOException | ClassNotFoundException e){
            e.getStackTrace();
            Arrays.stream(e.getSuppressed()).forEach(t -> logger.severe(t::getMessage));
        }
        return list;
    }
}
