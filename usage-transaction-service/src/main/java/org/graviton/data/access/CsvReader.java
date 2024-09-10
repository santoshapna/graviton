package org.graviton.data.access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CsvReader {
    /**
     * remove comment lines and return stream of lines
     * @param filePath path of CSV file
     * @return stream of lines
     */
    public Stream<String> readCSV(String filePath) throws IOException {
        Path path = Path.of(filePath);
        try{
            Stream<String> lines = Files.lines(path);
            return lines.filter(line -> !line.startsWith("#"));
        } catch (IOException e) {
            //log and throw
            throw e;
        }
    }
}
