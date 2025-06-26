package com.vcdeveloper.excelmapper.util.excel;


import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.lang.reflect.Method;
import java.util.Date;

public class CellValueMapper {

    public static Object mapCellToExpectedType(XSSFCell cell, Class<?> targetType) {
        if (cell == null) return null;

        try {
            if (targetType == String.class) {
                return getStringValue(cell);

            } else if (targetType == Integer.class) {
                return (int) cell.getNumericCellValue();

            } else if (targetType == Double.class) {
                return cell.getNumericCellValue();

            } else if (targetType == Date.class) {
                return cell.getDateCellValue();

            } else if (targetType == Boolean.class) {
                return getBoolean(cell);
            }

        } catch (Exception e) {
            // Optionally log or rethrow
        }

        return null;
    }

    public static void setPrimitiveOrEnum(Object obj, XSSFCell cell, Method setter) throws Exception {
        Class<?> type = setter.getParameterTypes()[0];

        if (type == boolean.class) {
            Boolean val = getBoolean(cell);
            if (val != null) setter.invoke(obj, val);

        } else if (type == int.class) {
            setter.invoke(obj, (int) cell.getNumericCellValue());

        } else if (type == long.class) {
            setter.invoke(obj, (long) cell.getNumericCellValue());

        } else if (type == short.class) {
            setter.invoke(obj, (short) cell.getNumericCellValue());

        } else if (type.isEnum()) {
            for (Object o : type.getEnumConstants()) {
                if (o.toString().equalsIgnoreCase(getStringValue(cell))) {
                    setter.invoke(obj, o);
                    break;
                }
            }
        }
    }

    private static Boolean getBoolean(XSSFCell cell) {
        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            int val = (int) cell.getNumericCellValue();
            return val == 1;
        }
        return null;
    }

    private static String getStringValue(XSSFCell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return Integer.toString((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
