package com.example.employeetrackerapp;

import java.io.Serializable;

public class Leaves implements Serializable
{
    String leavesMonth;
    String leavesDate;
    String leavesReason;
    String leavesStatus;
    String leaveStartDate;
    String leaveEndDtae;
    String leaveDescription;
    String AdminName;

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String adminName) {
        AdminName = adminName;
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

    public String getLeaveEndDtae() {
        return leaveEndDtae;
    }

    public void setLeaveEndDtae(String leaveEndDtae) {
        this.leaveEndDtae = leaveEndDtae;
    }

    public Leaves(){}

    public Leaves(String leavesMonth, String leavesDate, String leavesReason, String leavesStatus,String leaveStartDate,String leaveEndDtae,String leaveDescription,String AdminName) {
        this.leavesMonth = leavesMonth;
        this.leavesDate = leavesDate;
        this.leavesReason = leavesReason;
        this.leavesStatus = leavesStatus;
        this.leaveStartDate=leaveStartDate;
        this.leaveEndDtae = leaveEndDtae;
        this.leaveDescription=leaveDescription;
       this.AdminName=AdminName;
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
