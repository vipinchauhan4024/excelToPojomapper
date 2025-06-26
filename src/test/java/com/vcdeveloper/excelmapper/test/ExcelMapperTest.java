package com.vcdeveloper.excelmapper.test;

import com.vcdeveloper.excelmapper.util.excel.*;
import com.vcdeveloper.excelmapper.util.fileutil.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExcelMapperTest {

    @Test
    void testExcelRead() {

        ExcelToPojoMapper<Employee> mapperBean  = new ExcelToPojoMapperBeanold<>(FileUtil.getInputStream(ExcelMapperTest.class, "employee_readfile.xlsx"));
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
    void testExcelWrite() throws Exception {

         ExcelWriter writer = new ExcelWriterBean();

        List<Employee> employees = Arrays.asList(
                createEmployee(1, "Vipin", "HR", true),
                createEmployee(2, "Bob", "Engineering", false)
        );


        ByteArrayOutputStream out = new ByteArrayOutputStream();

        writer.write(employees, out);
        try (FileOutputStream fos = new FileOutputStream("Excel_writer_Test.xlsx")) {
            out.writeTo(fos); // write byte array to file
            out.flush();
            out.close();

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private Employee createEmployee(int id, String name, String dept, boolean probation) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        e.setDepartment(dept);
        e.setOnprobation(probation);
        return e;
    }
}
