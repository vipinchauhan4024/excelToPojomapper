package com.vcdeveloper.excelmapper.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * class is used by ExcelBeanMapper.
 */
 class ExcelRead {
    private  XSSFWorkbook workBook;
    private XSSFSheet sheet;
    private Map<Integer, MetaData> cellPojoMetadata =  new HashMap<Integer, MetaData>();
    private Map<Integer, Method>  cellIndexPojoSetterMap = new HashMap<Integer, Method>();

    public ExcelRead() {
        super();
    }
    /**
     * Creates new instance with initializing cellPojoMetadata, cellIndexPojoSetterMap.
     * @param in
     * @param sheetName
     * @param pojoClass
     * @throws IOException
     */


    protected ExcelRead(InputStream in, String sheetName,
            Class<?> pojoClass) throws IOException {
        workBook =  new XSSFWorkbook(in);
        sheet = workBook.getSheet(sheetName);
        init(pojoClass);
    }


    private void init(Class<?> pojoClass) {
        List<Field> pojoFeilds = PojoMetaDataReader.getAllfields(pojoClass);
        List<Method> pojoMethods = PojoMetaDataReader.getAllSetterMethods(pojoClass);
        XSSFRow headers = sheet.getRow(0);
        int totalHeaders = headers.getLastCellNum();
        XSSFCell cell;
        String header;

        for (int i = 0; i < totalHeaders; i++) {
            cell = headers.getCell(i);
            header = cell.getStringCellValue();
            int classLevelIndex = 0;
            findMatchingProperties(pojoFeilds, pojoMethods, header, i, pojoClass, classLevelIndex);

        }
    }
    
    private boolean findMatchingProperties(List<Field> pojoFeilds, List<Method> pojoMethods, String header, int colIndex, Class<?> pojoClass,
            int classLevelIndex) {
        boolean matchingPropertyFound = false;
        for (Field field : pojoFeilds) {
            if (header.equals(field.getName())) {
                MetaData m = new MetaData(field, null, pojoClass, classLevelIndex);
                cellPojoMetadata.put(colIndex, m);
                putInCellIndexSetterMethodMap(field, colIndex, pojoMethods, m);
                matchingPropertyFound = true;
                break;
            }
        }
        if (!matchingPropertyFound && pojoClass.getSuperclass() != null) {
            classLevelIndex++;
            findMatchingProperties(PojoMetaDataReader.getAllfields(pojoClass.getSuperclass()),
                                   PojoMetaDataReader.getAllSetterMethods(pojoClass.getSuperclass()), header, colIndex, pojoClass.getSuperclass(),
                                   classLevelIndex);
        }
        
        return matchingPropertyFound;
    }
   

    private void putInCellIndexSetterMethodMap(Field field, int i, List<Method> pojoMethods, MetaData m) {
        if (pojoMethods != null) {
            pojoMethods.forEach((method) -> addMappingSetterMethod(field, i, method, m));
           
        }
    }


    private void addMappingSetterMethod(Field field, int i, Method method, MetaData m) {
        String fieldName = field.getName();
        boolean isBooleanfield = field.getName().startsWith("is");
        String fieldNameFromMethod = ExcelUtil.toProperty(ExcelUtil.SET.length(), method.getName());
        if (isBooleanfield) {
            fieldNameFromMethod =  ExcelUtil.toPropertyForBoolean(ExcelUtil.SET.length(), method.getName());
        }
        if (fieldName.equals(fieldNameFromMethod)) {
            m.setSetterMethod(method);
            cellIndexPojoSetterMap.put(i, method);
        } 
         
    }

 

    /**
     * Get the List<XSSFCell> of  matching excel column  with Class property.
     * @param rowNum
     * @return
     */
    public List<XSSFCell> getMatchingCells(int rowNum) {
        List<XSSFCell> cells = new ArrayList<XSSFCell>();
        XSSFRow row = sheet.getRow(rowNum);
        short totalCells = row.getLastCellNum();
        XSSFCell cell;
        for (int i = 0; i < totalCells; i++) {
            if (cellPojoMetadata.get(i) != null) {
                cell = row.getCell(i);
                cells.add(cell);
            }
        }
        return cells;
    }
    
    public XSSFRow getRow(int rowNum) {
        return sheet.getRow(rowNum);
    }
    
    /**
     * give a map for excel cell index and MetaData.
     * @see MetaData
     * @return
     */
    public Map<Integer, MetaData>  getCellPojoMetadataMap() {
        return cellPojoMetadata;
    }
    
    /**
     * give a map for excel cell index and setter Method.
     * @return
     */
    public Map<Integer, Method>  getCellIndexPojoSetterMap() {
        return cellIndexPojoSetterMap;
    }
    
    public int getTotalRows() {
      return  sheet.getLastRowNum();
    }
}
