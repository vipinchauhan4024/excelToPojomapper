package com.vcdeveloper.excelmapper.util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation is used on class, if Class object has to be mapped from Excel.
 * we can provide excel sheet name , by default sheetName will be Sheet1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface MapFromExcel {
	
	String sheetName() default "Sheet1";

}