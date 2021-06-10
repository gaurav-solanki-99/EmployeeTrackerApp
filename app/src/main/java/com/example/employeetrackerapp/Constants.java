package com.example.employeetrackerapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {

    public static String formateDate(String date){
        String returnDate="";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final Date dateObj = sdf.parse(date);
            System.out.println(new SimpleDateFormat("hh:mm a").format(dateObj));
            returnDate = new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
return returnDate;

    }
}
