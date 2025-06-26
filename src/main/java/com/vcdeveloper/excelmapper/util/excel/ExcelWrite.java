package com.vcdeveloper.excelmapper.util.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelWrite {/*

    public static <T> void write(List<T> dataList, OutputStream out) throws Exception {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty.");
        }

        Class<?> clazz = dataList.get(0).getClass();

        // Get sheet name from annotation
        ExcelSheet sheetAnnotation = clazz.getAnnotation(ExcelSheet.class);
        String sheetName = (sheetAnnotation != null) ? sheetAnnotation.value() : "Sheet1";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        // Use LinkedHashMap to maintain order
        Map<Field, String> fieldColumnMap = new LinkedHashMap<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelIgnore.class)) continue;

            ExcelColumn col = field.getAnnotation(ExcelColumn.class);
            String columnName = (col != null) ? col.value() : field.getName();
            field.setAccessible(true);
            fieldColumnMap.put(field, columnName);
        }

        // Create header row
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;
        for (String columnName : fieldColumnMap.values()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(columnName);
        }

        // Write data rows
        int rowIndex = 1;
        for (T item : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;

            for (Field field : fieldColumnMap.keySet()) {
                Object value = field.get(item);
                Cell cell = row.createCell(cellIndex++);
                if (value != null) {
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        }

        // Auto size columns
        for (int i = 0; i < fieldColumnMap.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(out);
        workbook.close();
    }
*/

}
