package com.vcdeveloper.excelmapper.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vcdeveloper.excelmapper.util.exceptions.ExcelMapperException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.ss.usermodel.CellType;



/**
 * This is generic Class which will map Excel data to a Simple POJO calss of Type T, constructor  need
 * a file Input stream.
 * Pojo class should be annotated by @MapFromExcel(sheetName ="Testpojo") sheetName default value is "sheet1"
 * Propery which are not required to be mapped can be annotated by  @SkipMapping
 *
 * Column name in excel should match the property name in POJO class.
 * @param <T>
 *
 */
public class ExcelToPojoMapperBean<T> implements ExcelToPojoMapper<T> {

    private InputStream inputStream;
    private ExcelRead excelRead;



    public ExcelToPojoMapperBean(InputStream inputStream) {
        super();

        this.inputStream = inputStream;
    }


    @Override
    public List<T> getAllObjectsByRowCount(Class<T> pojoClass, int totalRowsToBeMapped) throws Exception {
        ensureExcelReadInitialized(pojoClass);
        int totalRows = excelRead.getTotalRows();

        if (totalRowsToBeMapped > totalRows) {
            throw new ExcelMapperException("Requested rows (" + totalRowsToBeMapped +
                    ") exceed total available rows (" + totalRows + ")");
        }

        return parseAllRows(pojoClass, 1, totalRowsToBeMapped);
    }


    /**
     * Maps all rows from excel to List<T> Pojo
     *
     * {@inheritDoc}
     */
    @Override
    public List<T> getAllObjects(Class<T> pojoClass) {
        ensureExcelReadInitialized(pojoClass);
        int totalRows = excelRead.getTotalRows();
        return parseAllRows(pojoClass, 1, totalRows); // start from row 1
    }


    /**
     * Maps a row from excel to a single object of  T
     * {@inheritDoc}
     */
    @Override
    public T getSingleObject(Class<T> pojoClass, int rowNum) throws Exception {
        ensureExcelReadInitialized(pojoClass);

        T obj = pojoClass.getDeclaredConstructor().newInstance(); // cleaner than reflection helper
        List<XSSFCell> cells = excelRead.getMatchingCells(rowNum);

        for (XSSFCell cell : cells) {
            if (cell != null) {
                mapValuesToPojo(cell, obj, excelRead.getCellPojoMetadataMap());
            }
        }

        return obj;
    }


    private void ensureExcelReadInitialized(Class<T> pojoClass) {
        if (excelRead == null) {
            try {
                String sheetName = PojoMetaDataReader.getSheetName(pojoClass);
                excelRead = new ExcelRead(inputStream, sheetName, pojoClass);
            } catch (IOException e) {
                throw new ExcelMapperException("Error initializing ExcelRead", e);
            }
        }
    }


    private List<T> parseAllRows(Class<T> pojoClass, int startRow, int endRow) {
        List<T> list = new ArrayList<>();

        for (int i = startRow; i < endRow; i++) {
            try {
                T obj = getSingleObject(pojoClass, i);
                list.add(obj);
            } catch (Exception e) {
                throw new ExcelMapperException("Error parsing row " + i, e);
            }
        }

        return list;
    }

    private void mapValuesToPojo(XSSFCell cell, T obj, Map<Integer, MetaData> map) throws Exception {
        MetaData metaData = map.get(cell.getColumnIndex());
        if (metaData == null) return;

        Method setter = metaData.getSetterMethod();
        if (setter == null) return;

        Class<?> targetType = setter.getParameterTypes()[0];
        Object value = CellValueMapper.mapCellToExpectedType(cell, targetType);

        if (value != null) {
            setter.invoke(obj, value);
        } else {
            CellValueMapper.setPrimitiveOrEnum(obj, cell, setter);
        }
    }



    private void setPrimitiveDataOrEnum(T obj, XSSFCell cell, Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            InstantiationException, NoSuchMethodException, SecurityException {
        Class<?> classType = m.getParameterTypes()[0];

        if (classType.getName().equals("boolean")) {
            setBoolean(obj, cell, m);
        } else if (classType.getName().equals("int")) {
            setInt(obj, cell, m);
        } else if (classType.getName().equals("long")) {
            setLong(obj, cell, m);
        } else if (classType.getName().equals("short")) {
            setShort(obj, cell, m);
        } else if (classType.isEnum()) {
            for (Object o : classType.getEnumConstants()) {
                if (o.toString().equals(getStringValue(cell))) {
                    m.invoke(obj, o);
                }
            }
        }

    }

    private void setInt(T obj, XSSFCell cell, Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        int i = (int) cell.getNumericCellValue();
        m.invoke(obj, i);

    }

    private void setLong(T obj, XSSFCell cell, Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        long l = (long) cell.getNumericCellValue();
        m.invoke(obj, l);

    }

    private void setShort(T obj, XSSFCell cell, Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        short s = (short) cell.getNumericCellValue();
        m.invoke(obj, s);

    }


    private void setBoolean(T obj, XSSFCell cell, Method m)
            throws IllegalAccessException, InvocationTargetException {

        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return;
        }

        Boolean value = null;
        CellType type = cell.getCellType();

        switch (type) {
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;

            case NUMERIC:
                int intVal = (int) cell.getNumericCellValue();
                value = intVal == 1;
                break;

            case STRING:
                String text = cell.getStringCellValue().trim().toLowerCase();
                if (text.equals("true") || text.equals("yes") || text.equals("1")) {
                    value = true;
                } else if (text.equals("false") || text.equals("no") || text.equals("0")) {
                    value = false;
                }
                break;

            default:
                // Do nothing for other cell types (e.g., FORMULA, ERROR)
                break;
        }

        if (value != null) {
            m.invoke(obj, value);
        }
    }



    private String getStringValue(XSSFCell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();

            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());

            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getCellFormula(); // Or evaluate it via FormulaEvaluator

            default:
                return "";
        }
    }


    private Object getMappingTypeObject(XSSFCell cell, Method m) {
        Class<?> classType = m.getParameterTypes()[0];
        if (classType.isAssignableFrom(String.class)) {
            return getStringValue(cell);

        } else if (classType.isAssignableFrom(Integer.class)) {
            return (int) cell.getNumericCellValue();
        } else if (classType.isAssignableFrom(Double.class)) {
            return cell.getNumericCellValue();
        } else if (classType.isAssignableFrom(Date.class)) {
            return cell.getDateCellValue();
        } else if (classType.isAssignableFrom(Boolean.class)) {
            return cell.getBooleanCellValue();
        }
        return null;
    }


    @Override
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;

    }

    public InputStream getInputStream () {
        return inputStream;
    }


}
