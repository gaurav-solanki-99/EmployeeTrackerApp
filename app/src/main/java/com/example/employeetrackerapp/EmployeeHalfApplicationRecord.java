package com.example.employeetrackerapp;

import java.io.Serializable;

public class EmployeeHalfApplicationRecord implements Serializable
{
    private int empId;
    private String empName;
    private String empDepartment;
    private String halfdaySubject;
    private String halfdayDescription;
    private String halddayDate;
    private String haldaytime;
    private String halfdayStatus;
    private String halfdayRemark;
    private String profile;
    private String AdminName;


    public EmployeeHalfApplicationRecord() {
    }

    public EmployeeHalfApplicationRecord(int empId, String empName, String empDepartment, String halfdaySubject, String halfdayDescription, String halddayDate, String haldaytime, String halfdayStatus) {
        this.empId = empId;
        this.empName = empName;
        this.empDepartment = empDepartment;
        this.halfdaySubject = halfdaySubject;
        this.halfdayDescription = halfdayDescription;
        this.halddayDate = halddayDate;
        this.haldaytime = haldaytime;
        this.halfdayStatus = halfdayStatus;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getHalfdayRemark() {
        return halfdayRemark;
    }

    public void setHalfdayRemark(String halfdayRemark) {
        this.halfdayRemark = halfdayRemark;
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

    public String getHalfdaySubject() {
        return halfdaySubject;
    }

    public void setHalfdaySubject(String halfdaySubject) {
        this.halfdaySubject = halfdaySubject;
    }

    public String getHalfdayDescription() {
        return halfdayDescription;
    }

    public void setHalfdayDescription(String halfdayDescription) {
        this.halfdayDescription = halfdayDescription;
    }

    public String getHalddayDate() {
        return halddayDate;
    }

    public void setHalddayDate(String halddayDate) {
        this.halddayDate = halddayDate;
    }

    public String getHaldaytime() {
        return haldaytime;
    }

    public void setHaldaytime(String haldaytime) {
        this.haldaytime = haldaytime;
    }

    public String getHalfdayStatus() {
        return halfdayStatus;
    }

    public void setHalfdayStatus(String halfdayStatus) {
        this.halfdayStatus = halfdayStatus;
    }
}
