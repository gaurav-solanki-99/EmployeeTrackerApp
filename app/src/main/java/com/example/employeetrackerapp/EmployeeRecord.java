package com.example.employeetrackerapp;

public class EmployeeRecord
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

    public String getEmpMember() {
        return empMember;
    }

    public void setEmpMember(String empMember) {
        this.empMember = empMember;
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
