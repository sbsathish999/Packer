package com.mobiquity.packer;

import com.mobiquity.packer.exception.APIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackerTest {
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
    public void testPack() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "8 : (1,15.3,€34) (2,3.8,€18) (5,1.5,€12)";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        String packedItems = Packer.pack(tempFile.getAbsolutePath());
        assertEquals("2,5", packedItems);
    }

    @Test
    public void testPackInvalidFormat() throws IOException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "8 : (1,15.3,€34) (2,3.8) (5,1.5,€12)"; // Missing price for the second item
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        assertThrows(APIException.class, () -> Packer.pack(tempFile.getAbsolutePath()));
    }

    @Test
    public void testPackWithNoItems() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "10 :";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        assertThrows(APIException.class, () -> Packer.pack(tempFile.getAbsolutePath()));
    }

    @Test
    public void testPackWithMultiplePackages() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "8 : (1,15.3,€34) (2,3.8,€18) (5,1.5,€12)\n" +
                "5 : (1,1.0,€10) (2,2.0,€20) (3,3.0,€30)";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        String packedItems = Packer.pack(tempFile.getAbsolutePath());
        assertEquals("2,5\n2,3", packedItems);
    }

    @Test
    public void testPackWithLargeWeightLimit() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "1000 : (1,100.0,€500) (2,200.0,€1000) (3,300.0,€1500)";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        String packedItems = Packer.pack(tempFile.getAbsolutePath());
        assertEquals("1,2,3", packedItems);
    }

    @Test
    public void testPackWithZeroWeightLimit() throws IOException, APIException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "0 : (1,1.0,€10) (2,2.0,€20) (3,3.0,€30)";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        String packedItems = Packer.pack(tempFile.getAbsolutePath());
        assertEquals("-", packedItems);
    }

    @Test
    public void testPackWithInvalidInputFormat() throws IOException {
        File tempFile = new File(tempDir.toFile(), "test.txt");
        String content = "10 : (1,15.3,€34) (2,3.8) (5,1.5,€12)";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(content);
        }
        assertThrows(APIException.class, () -> Packer.pack(tempFile.getAbsolutePath()));
    }
}
