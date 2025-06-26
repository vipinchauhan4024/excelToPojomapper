package com.vcdeveloper.excelmapper.util.excel;

@MapFromExcel(sheetName = "Employee")
public class Employee {

    private int id;

    private String name;

    private String department;

    private boolean isOnprobation;

    @SkipMapping
    private static long serialVersionUID;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isOnprobation() {
        return isOnprobation;
    }

    public void setOnprobation(boolean onprobation) {
        isOnprobation = onprobation;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", isOnprobation=" + isOnprobation +
                '}';
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}

