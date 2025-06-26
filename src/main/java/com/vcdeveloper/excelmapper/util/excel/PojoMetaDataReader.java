package com.vcdeveloper.excelmapper.util.excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * This Class is for Reading Meta Data from Class through cutom annotation.
 */
public class PojoMetaDataReader {

    private PojoMetaDataReader() {

    }

    public static String getSheetName(Class<?> pojoClass) {
        if (pojoClass.isAnnotationPresent(MapFromExcel.class)) {
            MapFromExcel anotation = pojoClass.getAnnotation(MapFromExcel.class);
            return anotation.sheetName();
        }
        return null;
    }

    public static List<Field> getAllfields(Class<?> pojoClass) {
        List<Field> mapfields = new ArrayList<Field>();
        Field[] fields = pojoClass.getDeclaredFields();
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {

                if (!skipMapping(fields[i])) {
                    mapfields.add(fields[i]);
                }
            }
        }
        return mapfields;
    }

    public static List<Method> getAllSetterMethods(Class<?> pojoClass) {
        Method[] methods = pojoClass.getMethods();
        List<Method> list = new ArrayList<Method>();
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 1 && method.getName().startsWith(ExcelUtil.SET)
                    && Modifier.isPublic(method.getModifiers())) {
                list.add(method);
            }
        }
        return list;
    }

    private static boolean skipMapping(Field field) {
        boolean annotationPresent = field.isAnnotationPresent(SkipMapping.class);
        if (annotationPresent) {
            return field.getAnnotation(SkipMapping.class).skipMapping();
        }
        return false;
    }

    public static Object getObject(Class<?> objType) {
        Object outputObject = null;
        if (isInstantiable(objType) && hasDefaultConstructor(objType)) {
            try {
                outputObject = objType.getConstructor().newInstance();

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("Failed to instantiate verified class " + objType, e);
            }
        }
        return outputObject;

    }

    private static boolean isInstantiable(Class<?> clazz) {
        return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }

    private static boolean hasDefaultConstructor(Class<?> clazz) {
        try {
            clazz.getConstructor();
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
