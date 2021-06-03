package com.example.employeetrackerapp;

public class EmployeLeavesApplicationRecord
{
    int empId;
    String empName;
    String empDepartment;
    String leaveSuject;
    String leaveDescription;
    String leaveStartDate;
    String leaveEndDate;
    String leaveStatus;

    public EmployeLeavesApplicationRecord() {
    }

    public EmployeLeavesApplicationRecord(int empId, String empName, String empDepartment, String leaveSuject, String leaveDescription, String leaveStartDate, String leaveEndDate, String leaveStatus) {
        this.empId = empId;
        this.empName = empName;
        this.empDepartment = empDepartment;
        this.leaveSuject = leaveSuject;
        this.leaveDescription = leaveDescription;
        this.leaveStartDate = leaveStartDate;
        this.leaveEndDate = leaveEndDate;
        this.leaveStatus = leaveStatus;
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

    public String getLeaveSuject() {
        return leaveSuject;
    }

    public void setLeaveSuject(String leaveSuject) {
        this.leaveSuject = leaveSuject;
    }

    public String getLeaveDescription() {
        return leaveDescription;
    }

    public void setLeaveDescription(String leaveDescription) {
        this.leaveDescription = leaveDescription;
    }

    public String getLeaveStartDate() {
        return leaveStartDate;
    }

    public void setLeaveStartDate(String leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public String getLeaveEndDate() {
        return leaveEndDate;
    }

    public void setLeaveEndDate(String leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }
}
