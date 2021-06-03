package com.example.employeetrackerapp;

public class EmployeeSalaryStatus
{
    private int empId;
    private String empName;
    private String empDepartment;
    private String month;
    private int totalleaves;
    private int totalattendence;
    private int totalhalfdays;
    private float monthsalary;


    public EmployeeSalaryStatus()
    {

    }

    public EmployeeSalaryStatus(int empId, String empName, String empDepartment, String month, int totalleaves, int totalattendence, int totalhalfdays, float monthsalary) {
        this.empId = empId;
        this.empName = empName;
        this.empDepartment = empDepartment;
        this.month = month;
        this.totalleaves = totalleaves;
        this.totalattendence = totalattendence;
        this.totalhalfdays = totalhalfdays;
        this.monthsalary = monthsalary;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getTotalleaves() {
        return totalleaves;
    }

    public void setTotalleaves(int totalleaves) {
        this.totalleaves = totalleaves;
    }

    public int getTotalattendence() {
        return totalattendence;
    }

    public void setTotalattendence(int totalattendence) {
        this.totalattendence = totalattendence;
    }

    public int getTotalhalfdays() {
        return totalhalfdays;
    }

    public void setTotalhalfdays(int totalhalfdays) {
        this.totalhalfdays = totalhalfdays;
    }

    public float getMonthsalary() {
        return monthsalary;
    }

    public void setMonthsalary(float monthsalary) {
        this.monthsalary = monthsalary;
    }
}
