package com.example.employeetrackerapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GCurrentDateTime {

    public static  String getCurrentTime() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(currentTime);

    }


    public  static String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(currentTime);

    }


    public static String getCurrentMonth() {
        String TodayMonth = "";
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.format(currentTime);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.format(currentTime);

        Calendar cal = Calendar.getInstance();

        int mounth = cal.get(Calendar.MONTH) + 1;

        switch (mounth) {
            case 1:
                TodayMonth = "January";
                break;
            case 2:
                TodayMonth = "Feburary";
                break;
            case 3:
                TodayMonth = "March";
                break;
            case 4:
                TodayMonth = "April";
                break;
            case 5:
                TodayMonth = "May";
                break;
            case 6:
                TodayMonth = "June";
                break;
            case 7:
                TodayMonth = "July";
                break;
            case 8:
                TodayMonth = "August";
                break;
            case 9:
                TodayMonth = "September";
                break;
            case 10:
                TodayMonth = "Octomber";
                break;
            case 11:
                TodayMonth = "November";
                break;
            case 12:
                TodayMonth = "December";
                break;

        }
        return TodayMonth;
    }
}
