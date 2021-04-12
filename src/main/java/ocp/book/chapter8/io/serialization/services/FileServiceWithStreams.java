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
 * Created by sousaJ on 04/04/2021
 * in package - ocp.book.chapter8.io.serialization.services
 **/
public class FileServiceWithStreams {
    private static Logger logger = Logger.getLogger(FileServiceWithStreams.class.getName());


    public static void copyFileAsBinaryData(File src, File dest) {

        createFile(dest);
        try(var in = new FileInputStream(src);
            var out = new FileOutputStream(dest)){

            int b;
            while ((b = in.read()) != -1){
                out.write(b);
            }

        }catch (IOException e){
            ioStreamException(e);
        }
    }

    public static void copyFileWithBuffer(File src, File dest){

        createFile(dest);

        try(var in = new BufferedInputStream(new FileInputStream(src));
            var out = new BufferedOutputStream(new FileOutputStream(dest))){

            var buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0 ){
                out.write(buffer, 0, lengthRead);
                out.flush();
            }

        }catch (IOException e){
            ioStreamException(e);
        }
    }

    // make method return file length offset in bytes so that
    //  if the file exist, the text is appended and do not override
    // the existent content. (in case of text files of course)
    private static void createFile(File dest) {
        if(!dest.exists()){
            try {
                dest.createNewFile();
                logger.log(Level.INFO,"File was created in path {0}", dest.getPath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            logger.log(Level.INFO,"File already existed in path {0}", dest.getPath());

        }
    }

    private static void ioStreamException(IOException e) {
        logger.log(Level.SEVERE, "IO Exception {0}", e.getMessage());
        Arrays.stream(e.getSuppressed()).forEach(throwable -> logger.info(throwable.getMessage()));
    }
}
