package com.example.employeetrackerapp;

public class Leaves
{
    String leavesMonth;
    String leavesDate;
    String leavesReason;
    String leavesStatus;

    public Leaves(){}

    public Leaves(String leavesMonth, String leavesDate, String leavesReason, String leavesStatus) {
        this.leavesMonth = leavesMonth;
        this.leavesDate = leavesDate;
        this.leavesReason = leavesReason;
        this.leavesStatus = leavesStatus;
    }

    public String getLeavesMonth() {
        return leavesMonth;
    }

    public void setLeavesMonth(String leavesMonth) {
        this.leavesMonth = leavesMonth;
    }

    public String getLeavesDate() {
        return leavesDate;
    }

    public void setLeavesDate(String leavesDate) {
        this.leavesDate = leavesDate;
    }

    public String getLeavesReason() {
        return leavesReason;
    }

    public void setLeavesReason(String leavesReason) {
        this.leavesReason = leavesReason;
    }

    public String getLeavesStatus() {
        return leavesStatus;
    }

    public void setLeavesStatus(String leavesStatus) {
        this.leavesStatus = leavesStatus;
    }
}
