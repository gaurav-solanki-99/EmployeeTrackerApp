package com.example.employeetrackerapp;

public class Halfday {

    String halfdayMonth;
    String halfdayDate;
    String halfdayReason;
    String halfdayStatus;

    public Halfday() {
    }

    public Halfday(String halfdayMonth, String halfdayDate, String halfdayReason, String halfdayStatus) {
        this.halfdayMonth = halfdayMonth;
        this.halfdayDate = halfdayDate;
        this.halfdayReason = halfdayReason;
        this.halfdayStatus = halfdayStatus;
    }

    public String getHalfdayMonth() {
        return halfdayMonth;
    }

    public void setHalfdayMonth(String halfdayMonth) {
        this.halfdayMonth = halfdayMonth;
    }

    public String getHalfdayDate() {
        return halfdayDate;
    }

    public void setHalfdayDate(String halfdayDate) {
        this.halfdayDate = halfdayDate;
    }

    public String getHalfdayReason() {
        return halfdayReason;
    }

    public void setHalfdayReason(String halfdayReason) {
        this.halfdayReason = halfdayReason;
    }

    public String getHalfdayStatus() {
        return halfdayStatus;
    }

    public void setHalfdayStatus(String halfdayStatus) {
        this.halfdayStatus = halfdayStatus;
    }
}
