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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sousaJ on 05/04/2021
 * in package - ocp.book.chapter8.io.serialization.services
 **/
public class FileServiceWithReadWrite {


    private static Logger logger = Logger.getLogger(FileServiceWithReadWrite.class.getName());


    public static void copyTextFile(File src, File dest) {

        if (doesFileExist(src, dest)) return;

        try(var reader = new FileReader(src);
            var writer = new FileWriter(dest)){

          int b;
          while ((b = reader.read()) != -1){
              writer.write(b);
          }

        }catch (IOException   e){
            e.getStackTrace();
            Arrays.stream(e.getSuppressed())
                  .forEach(throwable -> logger.log(Level.SEVERE, "Suppressed Exception in copyTextFile: {0}", throwable.getMessage()));
        }
    }

    public static void copyTextFileWithBuffer(File src, File dest){

        if (doesFileExist(src, dest)) return;

        try(var reader = new BufferedReader(new FileReader(src));
            var writer = new BufferedWriter(new FileWriter(dest))){
            String s;
            while((s= reader.readLine()) != null){
                writer.write(s);
                writer.newLine();
            }
        }catch (IOException e){
            e.getStackTrace();
            Arrays.stream(e.getSuppressed())
                  .forEach(throwable -> logger.log(Level.SEVERE, "Suppressed Exception in copyTextFile: {0}", throwable.getMessage()));
        }
    }



    private static boolean doesFileExist(File src, File dest) {
        if(!src.exists()){
            logger.log(Level.WARNING, "Source file doesn''t exists is path: {0}", src.getAbsolutePath());
            return true;
        }

        if(!dest.exists()){
            try {
                dest.createNewFile();
                String[] info = {src.getAbsolutePath(), src.getName()};
                logger.log(Level.INFO, "Destination file doesn''t exists is path,: {0}. File with name {1} was created.", info);
            }catch (IOException e){
                e.getStackTrace();
            }
        }
        return false;
    }

}
