package com.ecarriers.drivers.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static final String API_DATE_FORMAT = "yyyy-MM-dd";
    public static final String VISUAL_DATE_TIME_FORMAT = "dd/MM/yyyy";
    public static final String LONG_DATE_FORMAT = "dd MMMM, yyyy";

    public static Locale getSpanishLocale(){
        return new Locale("es", "ES");
    }

    public static String apiToVisual(String apiDate){

        if(apiDate != null) {
            if (apiDate.equals("")) {
                return "";
            } else {

                boolean error = false;

                SimpleDateFormat formatter = new SimpleDateFormat(API_DATE_FORMAT, getSpanishLocale());
                Calendar calendar = null;
                try {
                    calendar = Calendar.getInstance();
                    calendar.setTime(formatter.parse(apiDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                    error = true;
                }

                if (error) {
                    return "";
                } else {
                    String visual = "";
                    if (calendar != null) {
                        DateFormat df = new SimpleDateFormat(VISUAL_DATE_TIME_FORMAT, getSpanishLocale());
                        visual = df.format(calendar.getTime());
                    }
                    return visual;
                }
            }
        }else {
            return "";
        }
    }

    public static String apiToLongDate(String apiDate){

        if(apiDate != null) {
            if (apiDate.equals("")) {
                return "";
            } else {

                boolean error = false;

                SimpleDateFormat formatter = new SimpleDateFormat(API_DATE_FORMAT, getSpanishLocale());
                Calendar calendar = null;
                try {
                    calendar = Calendar.getInstance();
                    calendar.setTime(formatter.parse(apiDate));

                } catch (ParseException e) {
                    e.printStackTrace();
                    error = true;
                }

                if (error) {
                    return "";
                } else {
                    String visual = "";
                    if (calendar != null) {
                        DateFormat df = new SimpleDateFormat(LONG_DATE_FORMAT, getSpanishLocale());
                        visual = df.format(calendar.getTime());
                    }
                    return visual;
                }
            }
        }else {
            return "";
        }
    }
}
