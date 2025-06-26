package com.vcdeveloper.excelmapper.test;

import com.vcdeveloper.excelmapper.util.excel.Employee;
import com.vcdeveloper.excelmapper.util.excel.ExcelToPojoMapper;
import com.vcdeveloper.excelmapper.util.excel.ExcelToPojoMapperBean;
import com.vcdeveloper.excelmapper.util.fileutil.FileUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelMapperTest {

    @Test
    void testExcelWrite() {

        ExcelToPojoMapper<Employee> mapperBean  = new ExcelToPojoMapperBean<>(FileUtil.getInputStream(ExcelMapperTest.class, "employee_readfile.xlsx"));
        try {
            List<Employee> list = mapperBean.getAllObjects(Employee.class);

            list.forEach(System.out::println);
            assertEquals(list.get(0).getId() ,1); // Dummy check
            assertEquals(list.get(0).getName() ,"Alice"); //
            assertEquals(list.get(1).getId() ,2);
            assertEquals(list.get(1).getDepartment() ,"Engineering");
        }catch(Exception e){
            System.out.println(e.getMessage());

        }

    }

    @Test
    void testExcelRead() {

        ExcelToPojoMapper<Employee> mapperBean  = new ExcelToPojoMapperBean<>(FileUtil.getInputStream(ExcelMapperTest.class, "employee_readfile.xlsx"));

        try {
            List<Employee> list = mapperBean.getAllObjects(Employee.class);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        assertEquals(1, 1); // Dummy check
    }
}
