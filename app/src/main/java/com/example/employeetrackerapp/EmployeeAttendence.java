package com.example.employeetrackerapp;

public class EmployeeAttendence {

    private String workday;
    private String workdate;
    private String intime;
    private String outtime;

    private String daystatus;


    public EmployeeAttendence()
    {}

    public String getDaystatus() {
        return daystatus;
    }

    public void setDaystatus(String daystatus) {
        this.daystatus = daystatus;
    }

    public EmployeeAttendence(String workday, String workdate, String intime, String outtime, String daystatus) {
        this.workday = workday;
        this.workdate = workdate;
        this.intime = intime;
        this.outtime = outtime;
        this.daystatus = daystatus;
    }

    public String getWorkday() {
        return workday;
    }

    public void setWorkday(String workday) {
        this.workday = workday;
    }

    public String getWorkdate() {
        return workdate;
    }

    public void setWorkdate(String workdate) {
        this.workdate = workdate;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }
}
