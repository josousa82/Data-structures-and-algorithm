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

package ocp.book.chapter9.nio2paths;

import ocp.book.chapter9.nio2paths.services.FIleService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.System.out;

/**
 * Created by sousaJ on 06/04/2021
 * in package - ocp.book.chapter9.nio2paths
 **/
public class Main {

    private static Logger logger = Logger.getLogger(ocp.book.chapter9.nio2paths.Main.class.getName());
    private static FIleService fIleService = new FIleService();


    public static void main(String[] args) throws IOException {

//        subPathsExample();

        fIleService.printPathInformation(Path.of("zoo"));
        fIleService.printPathInformation(Path.of("/zoo/armadilllo/shells.txt"));
        fIleService.printPathInformation(Path.of("./armadillo/../shells.txt"));

        fIleService.printPathInformation(Path.of("/armadillo/test/path/shells.txt"));

        fIleService.copyFileAndReplace();
        fIleService.copyFileToNewDir();

        fIleService.copyFrom("www.google.com");
        fIleService.readFileWithBufferedReaderAndPrintToConsole("./fileCopyTest/file.txt");

        fIleService.writeToFileWithBufferedWriter("./fileCopyTest/file.txt" ,
                                      List.of("Test content 1","Test content 1","Test content 1", "Test content 1" ));
        fIleService.ls("./");

        var fileSize = fIleService.getPathFileSize("./", 2);
        out.println("fileSize = " + fileSize);

        fIleService.copyPath("./fileCopyTest", "./../fileCopyTest");

        fIleService.findFilesThatEndInAndMinSize("./", ".java", 10l );

        fIleService.readingFileWithLines("./fileCopyTest/textFile.txt").stream()
                   .filter(s -> !s.isBlank() || !s.isEmpty() ).forEach(out::println);

        fIleService.readFileWithLinesWithFilter("./fileCopyTest/textFile.txt", s -> s.startsWith("Lorem")).stream()
                   .forEach(out::println);
    }


}
