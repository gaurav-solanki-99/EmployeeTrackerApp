package com.example.employeetrackerapp;

public class EmployeLeavesStatus
{
    private int empId;
    private String empName;
    private String empDepartment;
    private int annualLeaves;
    private int monthLeaves;
    private int weekLeaves;


    public EmployeLeavesStatus()
    {}

    public EmployeLeavesStatus(int empId, String empName, String empDepartment, int annualLeaves, int monthLeaves, int weekLeaves) {
        this.empId = empId;
        this.empName = empName;
        this.empDepartment = empDepartment;
        this.annualLeaves = annualLeaves;
        this.monthLeaves = monthLeaves;
        this.weekLeaves = weekLeaves;
    }


    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(String empDepartment) {
        this.empDepartment = empDepartment;
    }

    public int getAnnualLeaves() {
        return annualLeaves;
    }

    public void setAnnualLeaves(int annualLeaves) {
        this.annualLeaves = annualLeaves;
    }

    public int getMonthLeaves() {
        return monthLeaves;
    }

    public void setMonthLeaves(int monthLeaves) {
        this.monthLeaves = monthLeaves;
    }

    public int getWeekLeaves() {
        return weekLeaves;
    }

    public void setWeekLeaves(int weekLeaves) {
        this.weekLeaves = weekLeaves;
    }
}
