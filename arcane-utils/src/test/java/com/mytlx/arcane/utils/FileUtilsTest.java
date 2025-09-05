package com.mytlx.arcane.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-03 23:02:03
 */
class FileUtilsTest {

    @Test
    void getWorkingDir() {
        System.out.println(FileUtils.getWorkingDir());
    }

    @Test
    void getModuleFile() {
        File file = FileUtils.getModuleFile("arcane-utils", "testFile.txt");
        if (file.exists()) {
            System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        }
    }

    @Test
    void getResourceFile() {
    }

    @Test
    void getFileInWorkingDir() {
    }
}