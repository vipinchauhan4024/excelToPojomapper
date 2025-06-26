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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;


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
public class ExcelToPojoMapperBeanold<T> implements ExcelToPojoMapper<T> {

    private InputStream inputStream;
    private ExcelRead excelRead;
    
    

    public ExcelToPojoMapperBeanold(InputStream inputStream) {
        super();
      
        this.inputStream = inputStream;
    }
    
    
    @Override
    public List<T> getAllObjectsByRowCount(Class<T> pojoClass, int totalRowsTobeMapped) throws Exception {
        List<T> list = new ArrayList<T>();
        if (excelRead == null) {
            try {
                excelRead = new ExcelRead(inputStream, PojoMetaDataReader.getSheetName(pojoClass), pojoClass);
            } catch (IOException e) {
                throw new ExcelMapperException("problem in reading Inpfut stream ******** ", e);
            }

        }
        int totalRows = excelRead.getTotalRows();
        if (totalRows < totalRowsTobeMapped) {
            throw new ExcelMapperException("Number of Rows passes more that total rows");
        }
        for (int i = 1; i < totalRowsTobeMapped; i++) {
            try {
                list.add(getSingleObject(pojoClass, i));
            } catch (Exception e) {
                throw new ExcelMapperException("Exception occured while reading  row number ******** " + i, e);
            }

        }
        return list;
    }

    /**
     * Maps all rows from excel to List<T> Pojo
     * 
     * {@inheritDoc}
     */
    @Override
    public List<T> getAllObjects(Class<T> pojoClass)  {
        List<T> list = new ArrayList<T>();
        if (excelRead == null) {
            try {
                excelRead = new ExcelRead(inputStream, PojoMetaDataReader.getSheetName(pojoClass), pojoClass);
            } catch (IOException e) {
                throw new ExcelMapperException("problem in reading Inpfut stream ******** " , e);
            }

        }
        int rows = excelRead.getTotalRows();
        for (int i = 1; i < rows; i++) {
            try {
                list.add(getSingleObject(pojoClass, i));
            } catch (Exception e) {
                throw new ExcelMapperException("Exception occured while reading  row number ******** " + i, e);
            }

        }
        return list;

    }

    /**
     * Maps a row from excel to a single object of  T
     * {@inheritDoc}
     */
    @Override
    public T getSingleObject(Class<T> pojoClass, int rowNum) throws Exception {
        @SuppressWarnings("unchecked")
        T obj = (T) PojoMetaDataReader.getObject(pojoClass);
        if (excelRead == null) {
            excelRead = new ExcelRead(inputStream, PojoMetaDataReader.getSheetName(pojoClass), pojoClass);
        }

        List<XSSFCell> cells = excelRead.getMatchingCells(rowNum);
       
        for (XSSFCell cell : cells) {
            if(cell == null) { // if cell is blank or empty
                continue;
            }
            mapValuesToPojo(cell, obj, excelRead.getCellPojoMetadataMap());
        }
        return obj;
    }

    private void mapValuesToPojo(XSSFCell cell, T obj, Map<Integer, MetaData> map) throws Exception {
      
        MetaData metaData = map.get(cell.getColumnIndex());
        Method m = metaData != null ?  metaData.getSetterMethod() : null;
        if (m == null) {
            return;
        }
        Object parm = getMappingTypeObject(cell, m);
        if (parm == null) {
            setPrimitiveDataOrEnum(obj, cell, m);
        } else {
            m.invoke(obj, parm);
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

    private void setBoolean(T obj, XSSFCell cell, Method m) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Boolean b = null;
        if (cell.getCellType() == CellType.BOOLEAN) {
            b = cell.getBooleanCellValue();
        } else if ((cell.getCellType() == CellType.NUMERIC)) {
            Integer x = (int) cell.getNumericCellValue();
            if (x != null && x == 0) {
                b = false;
            } else if (x != null && x == 1) {
                b = true;
            }
        }
        if (b != null) {
            m.invoke(obj, b);
        } 
    }

    private String getStringValue(XSSFCell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return new Integer((int) cell.getNumericCellValue()).toString();
        } else {
            return cell.getStringCellValue();
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
