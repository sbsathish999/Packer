package com.mobiquity.packer.util;

import com.mobiquity.packer.exception.APIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileReaderTest {
    @TempDir
    public Path tempDir;

//    @BeforeEach
//    public void setUp() throws IOException {
//        tempDir = Files.createTempDirectory("my-test");
//    }
//
//    @AfterEach
//    public void tearDown() throws IOException {
//        Files.walk(tempDir)
//                .sorted(Comparator.reverseOrder())
//                .map(Path::toFile)
//                .forEach(File::delete);
//    }

    @Test
    public void testLoadFile() throws IOException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "Hello, world!";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        byte[] fileBytes = FileReader.loadFile(tempFile.getAbsolutePath());
        String expectedContent = new String(content.getBytes(), StandardCharsets.UTF_8);
        String actualContent = new String(fileBytes, StandardCharsets.UTF_8);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    public void testLoadFileContentStream() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "Line 1\nLine 2\nLine 3";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        Stream<String> fileContentStream = FileReader.loadFileContentStream(tempFile.getAbsolutePath());
        String collectedContent = fileContentStream.collect(Collectors.joining("\n"));
        assertEquals(content, collectedContent);
    }

    @Test
    public void testLoadFileContentStreamEmptyFile() {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        assertThrows(APIException.class, () -> FileReader.loadFileContentStream(tempFile.getAbsolutePath()));
    }

    @Test
    public void testLoadFileContentStreamInvalidFile() {
        assertThrows(APIException.class, () -> FileReader.loadFileContentStream("nonexistent.txt"));
    }
}
