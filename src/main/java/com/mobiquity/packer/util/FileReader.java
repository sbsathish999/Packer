package com.mobiquity.packer.util;

import com.mobiquity.packer.exception.APIException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileReader {
    public static byte[] loadFile(String absolutePath) throws IOException {
        // Create a new File object from the absolute path
        File file = new File(absolutePath);

        // Get the file's path as a Path object
        Path filePath = file.toPath();

        // Read the file's contents into a byte array using the Files class
        byte[] fileBytes = Files.readAllBytes(filePath);

        return fileBytes;
    }

    public static Stream<String> loadFileContentStream(String filePath) throws APIException {
        try {
            byte[] fileContent = FileReader.loadFile(filePath);
            if (fileContent == null || fileContent.length == 0) {
                throw new APIException("No content found");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent)));
            return reader.lines();
        } catch (Throwable e) {
            throw new APIException("Error reading the input file: " + e.getMessage());
        }
    }
}
