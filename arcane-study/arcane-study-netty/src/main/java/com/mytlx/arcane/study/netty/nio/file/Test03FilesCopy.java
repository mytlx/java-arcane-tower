package com.mytlx.arcane.study.netty.nio.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-04 1:09:54
 */
public class Test03FilesCopy {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String source = "D:\\ProgramLanguage\\Java\\jdk-19.0.1";
        String target = "D:\\ProgramLanguage\\Java\\jdk-19.0.1temp";

        // Files.walk(Paths.get(source)).forEach(path -> {
        //     try {
        //         String targetName = path.toString().replace(source, target);
        //         if (Files.isDirectory(path)) {
        //             // 是目录
        //             Files.createDirectory(Paths.get(targetName));
        //         } else if (Files.isRegularFile(path)) {
        //             // 是普通文件
        //             Files.copy(path, Paths.get(targetName));
        //         }
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // });
        // long end = System.currentTimeMillis();
        // System.out.println(end - start);

    }
}
