package com.vcdeveloper.excelmapper.util.excel;

import java.io.InputStream;
import java.util.List;

/**
 * Interface for ExcelToPojoMapperBean<T>
 * */
public interface ExcelToPojoMapper<T> {
    
    public List<T> getAllObjects(Class<T> pojoClass) throws Exception;
    
    public List<T> getAllObjectsByRowCount(Class<T> pojoClass, int totalRowsTobeMapped) throws Exception;

    public T getSingleObject(Class<T> pojoClass, int rowNum) throws Exception;

    public void setInputStream(InputStream inputStream);
}
