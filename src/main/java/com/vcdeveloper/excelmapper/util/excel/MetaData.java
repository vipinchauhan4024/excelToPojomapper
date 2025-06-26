package com.vcdeveloper.excelmapper.util.excel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class is to hold MetaData info for POJO class which has to be mapped from Excel.  
 */
public class MetaData {
    private Field feild;
    private Method setterMethod;
    private Class<?> pojoClass;
    private int classLevel;
    
    
    public MetaData(Field feild, Method setterMethod, Class<?> pojoClass, int classLevel) {
        super();
        this.feild = feild;
        this.setterMethod = setterMethod;
        this.pojoClass = pojoClass;
        this.classLevel = classLevel;
    }
    public Field getFeild() {
        return feild;
    }
    public void setFeild(Field feild) {
        this.feild = feild;
    }
    public Method getSetterMethod() {
        return setterMethod;
    }
    public void setSetterMethod(Method setterMethod) {
        this.setterMethod = setterMethod;
    }
    public Class<?> getPojoClass() {
        return pojoClass;
    }
    public void setPojoClass(Class<?> pojoClass) {
        this.pojoClass = pojoClass;
    }
    public int getClassLevel() {
        return classLevel;
    }
    public void setClassLevel(int classLevel) {
        this.classLevel = classLevel;
    }
    
    
}
