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

package ocp.book.chapter9.nio2paths.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.nio.file.Files.createFile;

/**
 * Created by sousaJ on 08/04/2021
 * in package - ocp.book.chapter9.nio2paths.services
 **/
public class FIleService {

    private Logger logger = Logger.getLogger(ocp.book.chapter9.nio2paths.services.FIleService.class.getName());

    public static void copyFileAndReplace() {
        Path src = Paths.get("./fileCopyTest/test.txt");
        Path dest = Path.of("./newDirectory/");
        try {
            Files.createDirectory(Path.of("./fileCopyTest"));
            Files.createFile(Path.of("./fileCopyTest/test.txt"));
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e ){
            err.println("An exception was thrown: \n" + e.getMessage());
        }
    }

    public void ls(String dir) {
        try (Stream<Path> s = Files.list(Path.of(dir))) {
            s.forEach(out::println);
        } catch (IOException e) {
            e.printStackTrace();
            Arrays.stream(e.getSuppressed()).forEach(t -> out.println(t.getMessage()));
        }
    }

    public void copyPath(String src, String tgt){

        // problem with recursive copy, when target is a sub directory of the src
        // copyPath("./", "./testCopy") will create nested test copy dirs, probably up to
        // Integer.MAX_VALUE depth
        try {
            var source = Path.of(src);
            var target = Path.of(tgt);

            //  throws exception if file/directory already exists , replicate behaviour and check
            // if same happens with directory exists
            Files.copy(source, target); // shallow copy of the dir / file

            if(Files.isDirectory(source))  // if is a directory, recursive copy of sub folders
                try (Stream<Path> s = Files.list(source)){   //  when using streams the JVM wont follow sym links
                    s.forEach(path -> {
                        copyPath(path.toString(), target.resolve(path.getFileName()).toString());
                    } );
                }

        } catch (IOException e) {
            e.printStackTrace();
            Arrays.stream(e.getSuppressed()).forEach(t -> System.out.println("t.getMessage() = " + t.getMessage()));
        }
    }

    public long getPathFileSize(String source, int maxDepth){
        try(var s = Files.walk(Path.of(source), maxDepth, FileVisitOption.FOLLOW_LINKS)){
            return s.parallel()
                    .filter(p -> !Files.isDirectory(p))
                    .mapToLong(this::getSize)
                    .sum();
        }catch (IOException e){
            e.printStackTrace();
            Arrays.stream(e.getSuppressed())
                  .forEach(t -> out.println("t.getMessage() = " + t.getMessage()));
        }
        return 0L;
    }

    private long getSize(Path p) {
        try{
            return Files.size(p);
        }catch (IOException io){
            io.getMessage();
            Arrays.stream(io.getSuppressed())
                  .forEach(t-> err.println(t.getMessage()));
        }
        return 0L;
    }

    public void findFilesThatEndInAndMinSize(String dir, String matcherEnd, long minSize){
        Path path = Paths.get(dir);
        try( var s = Files.find(path, 10,
                                (p, attributes) -> attributes.isRegularFile() &&
                p.toString().endsWith(matcherEnd) && attributes.size() > minSize)){
            s.forEach(System.out::println);

        }catch (IOException io){
            io.getMessage();
            Arrays.stream(io.getSuppressed())
                  .forEach(t-> System.err.println(t.getMessage()));
        }
    }

    public List<String> readingFileWithLines(String file) throws FileNotFoundException {
        List<String> text = new ArrayList<>();
        Path path = Paths.get(file);
        if (!path.toFile().isFile()){
            throw new IllegalArgumentException("Path given is not for a file");
        }else if(!path.toFile().exists()){
            throw  new FileNotFoundException("File doesn't exist in the given directory");
        }else {
            try(var s = Files.lines(path)){
                s.forEach(text::add);
            }catch (IOException io){

            }
        }
        return text;
    }

    public List<String> readFileWithLinesWithFilter(String file, Predicate<String> filter) {

        try {
            return readingFileWithLines(file).stream()
                                      .filter(filter)
                                      .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    public void copyFileToNewDir() {
        try (var is = new FileInputStream(Paths.get("./fileCopyTest/test.txt").toFile())){

            Path path = Paths.get("./fileCopyTest/file.txt");

            if(!path.toFile().exists()){
                logger.info("A new file was created");
                Files.copy(is, path);

            }else {

                logger.info("The file was replaced");
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            }

            // reads file as input stream and print to the console
            Files.copy(path, out);

        }catch (IOException e){
            out.println("e.getMessage() = " + e.getMessage());
            Arrays.stream(e.getSuppressed()).forEach(t -> out.println("t.getMessage() = " + t.getMessage()));
        }
    }

    public void writeToFileWithBufferedWriter(String url, List<String> list){

        try(var bw = Files.newBufferedWriter(Paths.get(url))){
            for (var line : list){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readFileWithBufferedReaderAndPrintToConsole(String  url) throws IOException {

        try(var br = Files.newBufferedReader(Paths.get(url))){
            String currentLine =null;
            while ((currentLine = br.readLine()) != null){
                if(!currentLine.isBlank())
                    out.println("currentLine = " + currentLine);
            }
        }
    }

    public  void copyFrom(String url){
        try(var is = new FileInputStream(url)){
            Path path = Paths.get("./fileCopyTest/fileURL.txt");
            Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e){
            out.println("e.getMessage() = " + e.getMessage());
            Arrays.stream(e.getSuppressed()).forEach(t -> out.println("t.getMessage() = " + t.getMessage()));
        }
    }

    public void manipulatingDirectoriesAndFiles() throws IOException {
        //Create directories
        Files.createDirectory(Path.of("./testeV/rtest/test"));

        //Creates file
        createFile(Path.of("./test.txt"));

        createFile(Path.of("./fileCopyTest/test.txt"));
    }
    public void copyFile(Path source, Path target) throws IOException {
        Files.move(source, target, LinkOption.NOFOLLOW_LINKS, StandardCopyOption.ATOMIC_MOVE);
    }

    public void subPathsExample(){
        var p  = Paths.get("/mammal/omnivore/racoon.image");
        out.println("p = " + p);

        for (int i = 0; i < p.getNameCount() ; i++) {
            out.println(" Element " + i + " is: " + p.getName(i));
        }

        //  throws IllegalArgumentException for invalid index, even for outOfBounds
        out.println("p.subpath(0,3) = " + p.subpath(0, 3));
        out.println("p.subpath(1,2) = " + p.subpath(1,2));
        out.println("p.subpath(1,3) = " + p.subpath(1, 3));

    }

    public void printPathInformation(Path path){
        out.println("Filename is: " + path.getFileName());
        out.println("      Root is: " + path.getRoot());

        Path currentParent = path;

        while ((currentParent = currentParent.getParent()) != null){
            out.println("   Current parent is: " + currentParent);
        }
    }



}
