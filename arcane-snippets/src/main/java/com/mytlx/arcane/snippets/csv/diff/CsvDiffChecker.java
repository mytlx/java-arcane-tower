package com.mytlx.arcane.snippets.csv.diff;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * ID比对，比对csv文件中两列id的差异，用于对账
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-04 13:16:16
 */
@Slf4j
public class CsvDiffChecker {

    public static void main(String[] args) throws Exception {
        check();
    }


    public static void check() throws Exception {
        // 改成你的路径
        String inputFile = "C:/Users/TLX/Desktop/test.csv";
        String onlyInCol1File = "only_in_col1.txt";
        String onlyInCol2File = "only_in_col2.txt";

        Set<String> c1Set = new HashSet<>();
        Set<String> c2Set = new HashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
            String line;
            int headerCnt = 1;
            int currentLineCnt = 0;

            while ((line = reader.readLine()) != null) {
                currentLineCnt++;
                // 跳过表头
                if (headerCnt >= currentLineCnt) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length <= 2 && parts.length >= 1) {
                    String column1 = parts[0].trim();
                    String column2 = parts.length < 2 ? "" : parts[1].trim();

                    if (!column1.isBlank()) {
                        c1Set.add(column1);
                    }

                    if (!column2.isBlank()) {
                        c2Set.add(column2);
                    }
                } else {
                    log.error("数据格式有误，line：{}, data: {}", currentLineCnt, line);
                }
            }

        } catch (Exception e) {
            log.error("执行出错：{}", e.getMessage(), e);
        }

        // 计算差集，需要排序可以换成 TreeSet
        Set<String> onlyInCol1Set = new HashSet<>(c1Set);
        onlyInCol1Set.removeAll(c2Set);

        Set<String> onlyInCol2Set = new HashSet<>(c2Set);
        onlyInCol2Set.removeAll(c1Set);

        writeToFile(onlyInCol1Set, onlyInCol1File);
        writeToFile(onlyInCol2Set, onlyInCol2File);

        System.out.println("对比完成，结果已写入文件：");
        System.out.println("仅在第一列中的：" + onlyInCol1File);
        System.out.println("仅在第二列中的：" + onlyInCol2File);
    }

    private static void writeToFile(Set<String> data, String filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }


}
