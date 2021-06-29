package com.example.employeetrackerapp;

import java.io.Serializable;

public class EmployeeRecord implements Serializable
{
    int empid;
    String empName;
    String empEmail;
    String empPhone;
    String empAdress;
    String empDOB;
    String empDepartment;
    String empDateOFjoining;
    String empDateOFresign;
    String empPassword;
    String empMember;
    String empBloodGroup;
    String empAdharNo;
    String empProfile;
    String adharFrount;
    String adharBack;
    String adressLine1;
    String adressLine2;
    String city;
    String state;
    String position;
    String fid;
    String empEmailPersonal;

    public String getEmpEmailPersonal()
    {
        return empEmailPersonal;
    }

    public void setEmpEmailPersonal(String empEmailPersonal) {
        this.empEmailPersonal = empEmailPersonal;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getAdharFrount() {
        return adharFrount;
    }

    public void setAdharFrount(String adharFrount) {
        this.adharFrount = adharFrount;
    }

    public String getAdharBack() {
        return adharBack;
    }

    public void setAdharBack(String adharBack) {
        this.adharBack = adharBack;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public void setAdressLine1(String adressLine1) {
        this.adressLine1 = adressLine1;
    }

    public String getAdressLine2() {
        return adressLine2;
    }

    public void setAdressLine2(String adressLine2) {
        this.adressLine2 = adressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmpProfile() {
        return empProfile;
    }

    public void setEmpProfile(String empProfile) {
        this.empProfile = empProfile;
    }

    public String getEmpMember() {
        return empMember;
    }

    public void setEmpMember(String empMember) {
        this.empMember = empMember;
    }

    public String getEmpBloodGroup() {
        return empBloodGroup;
    }

    public void setEmpBloodGroup(String empBloodGroup) {
        this.empBloodGroup = empBloodGroup;
    }

    public String getEmpAdharNo() {
        return empAdharNo;
    }

    public void setEmpAdharNo(String empAdharNo) {
        this.empAdharNo = empAdharNo;
    }

    public EmployeeRecord()
    {

    }

    public EmployeeRecord(int empid, String empName, String empEmail, String empPhone, String empAdress, String empDOB, String empDepartment, String empDateOFjoining, String empDateOFresign, String empPassword, String empMember) {
        this.empid = empid;
        this.empName = empName;
        this.empEmail = empEmail;
        this.empPhone = empPhone;
        this.empAdress = empAdress;
        this.empDOB = empDOB;
        this.empDepartment = empDepartment;
        this.empDateOFjoining = empDateOFjoining;
        this.empDateOFresign = empDateOFresign;
        this.empPassword = empPassword;
        this.empMember = empMember;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public int getEmpid() {
        return empid;
    }

    public void setEmpid(int empid) {
        this.empid = empid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpPhone() {
        return empPhone;
    }

    public void setEmpPhone(String empPhone) {
        this.empPhone = empPhone;
    }

    public String getEmpAdress() {
        return empAdress;
    }

    public void setEmpAdress(String empAdress) {
        this.empAdress = empAdress;
    }

    public String getEmpDOB() {
        return empDOB;
    }

    public void setEmpDOB(String empDOB) {
        this.empDOB = empDOB;
    }

    public String getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(String empDepartment) {
        this.empDepartment = empDepartment;
    }

    public String getEmpDateOFjoining() {
        return empDateOFjoining;
    }

    public void setEmpDateOFjoining(String empDateOFjoining) {
        this.empDateOFjoining = empDateOFjoining;
    }

    public String getEmpDateOFresign() {
        return empDateOFresign;
    }

    public void setEmpDateOFresign(String empDateOFresign) {
        this.empDateOFresign = empDateOFresign;
    }
}
