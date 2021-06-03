package com.example.employeetrackerapp;

public class EmployeeWorkingDetails
{
    private int empId;
    private String empName;
    private String empDepartment;
    private String date;
    private String mounth;
    private String startTime;
    private String EndTime;
    private String dayStatus;
    private String breakStartTime;
    private String breakEndTme;
    private String breakHours;


    public EmployeeWorkingDetails()
    {

    }


    public EmployeeWorkingDetails(int empId, String empName, String empDepartment, String date, String mounth, String startTime, String endTime, String dayStatus, String breakStartTime, String breakEndTme, String breakHours) {
        this.empId = empId;
        this.empName = empName;
        this.empDepartment = empDepartment;
        this.date = date;
        this.mounth = mounth;
        this.startTime = startTime;
        EndTime = endTime;
        this.dayStatus = dayStatus;
        this.breakStartTime = breakStartTime;
        this.breakEndTme = breakEndTme;
        this.breakHours = breakHours;
    }

    public String getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(String breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public String getBreakEndTme() {
        return breakEndTme;
    }

    public void setBreakEndTme(String breakEndTme) {
        this.breakEndTme = breakEndTme;
    }

    public String getBreakHours() {
        return breakHours;
    }

    public void setBreakHours(String breakHours) {
        this.breakHours = breakHours;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMounth() {
        return mounth;
    }

    public void setMounth(String mounth) {
        this.mounth = mounth;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(String dayStatus) {
        this.dayStatus = dayStatus;
    }


}
