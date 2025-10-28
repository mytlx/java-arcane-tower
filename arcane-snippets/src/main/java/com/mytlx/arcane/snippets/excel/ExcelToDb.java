package com.mytlx.arcane.snippets.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-28 17:35:14
 */
@Slf4j
public class ExcelToDb {

    String inputFile = "E:\\TLX\\Documents\\project\\001_Java\\java-arcane-tower\\arcane-snippets\\src\\main\\resources\\明细科目码表整理1027.xlsx";
    String outputFile = "./arcane-snippets/excelToDb.sql";

    private final List<DataObject> excelDataList = new ArrayList<>();
    private Map<String, String> codeNameMap = new HashMap<>();
    private List<String> sqlList = new ArrayList<>();

    void main() {
        getFromExcel();
        completingData();
        transferSQL();
        writeToFile();

        log.info("===================== 处理完成");
    }

    public void getFromExcel() {
        try (FileInputStream file = new FileInputStream(inputFile);
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            int headerCnt = 2;
            int currentLineCnt = 0;

            for (Row row : sheet) {
                currentLineCnt++;

                // 跳过表头
                if (currentLineCnt <= headerCnt) {
                    continue;
                }

                DataObject dataObject = toDataObject(row, currentLineCnt - headerCnt);
                excelDataList.add(dataObject);
            }
        } catch (IOException e) {
            log.error("文件IO错误：{}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("执行出错：{}", e.getMessage(), e);
        }
        log.info("查询到{}条数据", excelDataList.size());

        codeNameMap = excelDataList.parallelStream()
                .collect(Collectors.toMap(DataObject::getCodeCode, o -> {
                    if (notBlank(o.getLevel1())) {
                        return o.getLevel1();
                    } else if (notBlank(o.getLevel2())) {
                        return o.getLevel2();
                    } else if (notBlank(o.getLevel3())) {
                        return o.getLevel3();
                    } else if (notBlank(o.getLevel4())) {
                        return o.getLevel4();
                    } else if (notBlank(o.getLevel5())) {
                        return o.getLevel5();
                    }
                    return "";
                }));
    }

    public DataObject toDataObject(Row row, int id) {
        DataObject dataObject = new DataObject();
        dataObject.setCodeId((long) id);
        dataObject.setDetailedAccounts(!notBlank(row.getCell(0).getStringCellValue().trim()) ? null : row.getCell(0).getStringCellValue().trim());
        dataObject.setCodeCode(!notBlank(row.getCell(1).getStringCellValue().trim()) ? null : row.getCell(1).getStringCellValue().trim());
        dataObject.setLevel1(!notBlank(row.getCell(2).getStringCellValue().trim()) ? null : row.getCell(2).getStringCellValue().trim());
        dataObject.setLevel2(!notBlank(row.getCell(3).getStringCellValue().trim()) ? null : row.getCell(3).getStringCellValue().trim());
        dataObject.setLevel3(!notBlank(row.getCell(4).getStringCellValue().trim()) ? null : row.getCell(4).getStringCellValue().trim());
        dataObject.setLevel4(!notBlank(row.getCell(5).getStringCellValue().trim()) ? null : row.getCell(5).getStringCellValue().trim());
        dataObject.setLevel5(!notBlank(row.getCell(6).getStringCellValue().trim()) ? null : row.getCell(6).getStringCellValue().trim());
        dataObject.setIsLast(false);
        if (row.getCell(7) != null && notBlank(row.getCell(7).getStringCellValue())) {
            dataObject.setIsLast(true);
        }

        if (dataObject.getCodeCode() == null) {
            log.error("数据有误，当前行：{}", id + 2);
            System.exit(1);
        }

        return dataObject;
    }

    public void completingData() {
        final int l1 = 4, l2 = 6, l3 = 8, l4 = 10, l5 = 12;
        int step = 2;
        for (DataObject dataObject : excelDataList) {
            if (dataObject.getCodeCName() == null) {
                dataObject.setCodeCName("");
            }
            if (dataObject.getCodeLName() == null) {
                dataObject.setCodeLName("");
            }
            if (dataObject.getCodeEName() == null) {
                dataObject.setCodeEName("");
            }

            String code = dataObject.getCodeCode();
            for (int i = l1; i <= l5 && i <= code.length(); i += step) {
                String currentPrefix = code.substring(0, i);
                String prefixName = codeNameMap.get(currentPrefix);
                String codeCName = dataObject.getCodeCName();
                // codeCName + "-" + prefixName
                String part1 = codeCName, part2 = "-", part3 = prefixName;
                if (!notBlank(codeCName)) {
                    part1 = "";
                    part2 = "";
                }
                if (!notBlank(prefixName)) {
                    part2 = "";
                    part3 = "";
                }
                String codeNameNew = part1 + part2 + part3;

                switch (i) {
                    case l1: {
                        dataObject.setLevel1(prefixName);
                        dataObject.setCodeCName(prefixName);
                        break;
                    }
                    case l2: {
                        dataObject.setLevel2(prefixName);
                        dataObject.setCodeCName(codeNameNew);
                        break;
                    }
                    case l3: {
                        dataObject.setLevel3(prefixName);
                        dataObject.setCodeCName(codeNameNew);
                        break;
                    }
                    case l4: {
                        dataObject.setLevel4(prefixName);
                        dataObject.setCodeCName(codeNameNew);
                        break;
                    }
                    case l5: {
                        dataObject.setLevel5(prefixName);
                        dataObject.setCodeCName(codeNameNew);
                        break;
                    }
                }
            }
        }
    }

    public void transferSQL() {
        sqlList = excelDataList.parallelStream()
                .map(o -> generateSql("t_temp_acc_conf_code_sc", o))
                .toList();
    }

    // INSERT INTO `t_temp_acc_conf_code_sc` (`code_id`, `detailed_accounts`, `code_code`,
    //         `level1`, `level2`, `level3`, `level4`, `level5`, `code_c_name`, `code_l_name`, `code_e_name`,
    //         `is_last`) VALUES (58, 'p2zowf9FnT', '06gUVgfWEX', 'SDEnaL0SY6', 'IlfnsPMbFO', 'GwymUxmT7C',
    // 'eLi6Bz22MO', 'FpzLF7jlrf', 'Deng Xiuying', 'Deng Xiuying', 'Deng Xiuying', 41);
    public String generateSql(String tableName, DataObject dataObject) {
        Field[] declaredFields = DataObject.class.getDeclaredFields();
        List<String> columnNames = Arrays.stream(declaredFields)
                .map(o -> "`" + camelToSnake(o.getName()) + "`")
                .toList();

        List<String> columnValues = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                Object fieldValue = declaredField.get(dataObject);
                columnValues.add(formatFieldValue(fieldValue));
            } catch (IllegalAccessException e) {
                log.error("反射访问字段失败: {} - {}", declaredField.getName(), e.getMessage());
            }
        }
        return String.format("INSERT INTO `%s` (%s) VALUES (%s);",
                tableName, String.join(", ", columnNames), String.join(", ", columnValues));

    }

    public String camelToSnake(String camelCaseName) {
        if (camelCaseName == null || camelCaseName.isEmpty()) {
            return "";
        }

        String snakeCase = camelCaseName.replaceAll("([A-Z])", "_$1").toLowerCase();

        if (snakeCase.startsWith("_")) {
            return snakeCase.substring(1);
        }

        return snakeCase;
    }

    public String formatFieldValue(Object fieldValue) {
        if (fieldValue == null) return null;
        Class<?> type = fieldValue.getClass();

        if (type.equals(String.class) || type.equals(Character.class)) {
            String replace = fieldValue.toString().replace("'", "''");
            return "'" + replace + "'";
        }

        if (type.isPrimitive() || Number.class.isAssignableFrom(type) || type.equals(Boolean.class)) {
            return fieldValue.toString();
        }

        return "'" + fieldValue.toString().replace("'", "''") + "'";
    }

    public void writeToFile() {
        try {
            Files.write(Paths.get(outputFile), sqlList);
            log.info("写入文件成功");
        } catch (IOException e) {
            log.error("写入文件失败", e);
        }
    }

    public boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
