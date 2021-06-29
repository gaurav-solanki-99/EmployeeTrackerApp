package com.example.employeetrackerapp;

import java.io.Serializable;

public class Halfday implements Serializable
{

    String halfdayMonth;
    String halfdayDate;
    String halfdayReason;
    String halfdayStatus;
    String halfdatTime;
    String halfdayDescription;

    public String getHalfdatTime() {
        return halfdatTime;
    }

    public void setHalfdatTime(String halfdatTime) {
        this.halfdatTime = halfdatTime;
    }

    public String getHalfdayDescription() {
        return halfdayDescription;
    }

    public void setHalfdayDescription(String halfdayDescription) {
        this.halfdayDescription = halfdayDescription;
    }

    public Halfday() {
    }

    public Halfday(String halfdayMonth, String halfdayDate, String halfdayReason, String halfdayStatus,String halfdatTime,
            String halfdayDescription) {
        this.halfdayMonth = halfdayMonth;
        this.halfdayDate = halfdayDate;
        this.halfdayReason = halfdayReason;
        this.halfdayStatus = halfdayStatus;
        this.halfdatTime=halfdatTime;
        this.halfdayDescription=halfdayDescription;
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
