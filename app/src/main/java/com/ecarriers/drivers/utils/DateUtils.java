package com.ecarriers.drivers.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //formato api del campo real start (2016-05-29 15:30:00-03:00)
    //El formato de la time zone logrado con la ZZ es -0300, pero funciona igual... Ver estos links:
    //https://developer.android.com/reference/java/text/SimpleDateFormat.html
    //https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
    public static final String FORMATO_FECHA_API = "yyyy-MM-dd HH:mm:ssZZ";

    public static final String FORMATO_FECHA_API_SHORT = "yyyy-MM-dd";
    public static final String FORMATO_FECHA_FUNCION = "EE dd";
    public static final String FORMATO_FECHA_LARGA_FUNCION = "EEEE dd/MM";
    public static final String FORMATO_FECHA_CORTA_FUNCION = "dd/MM";

    public static final String FORMATO_FECHA_FUNCION_COMPRA = "dd/MM/yyyy HH:mm";

    public static final String FORMATO_FECHA_DEFAULT = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_LARGO = "EEEE dd/MM/yyyy";

    public static final String FORMATO_FECHA_NOTIF = "dd MMM yyyy, HH:mm";
    public static final String FORMATO_FECHA_LARGA = "dd MMM yyyy";

    public static Locale getSpanishLocale(){
        return new Locale("es", "ES");
    }

    public static String calendarToStringApiShort(Calendar cal){
        String fechaStr = "";
        if (cal != null) {
            DateFormat df = new SimpleDateFormat(FORMATO_FECHA_API_SHORT, getSpanishLocale());
            fechaStr = df.format(cal.getTime());
        }
        return fechaStr;
    }

    public static String formatoApiShortToFormatoFuncion(String fechaFormatoApi){

        if(fechaFormatoApi != null) {
            if (fechaFormatoApi.equals("")) {
                return "";
            } else {

                boolean error = false;

                SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_API_SHORT, getSpanishLocale());
                Calendar fecha = null;
                try {
                    fecha = Calendar.getInstance();
                    fecha.setTime(formatter.parse(fechaFormatoApi));

                } catch (ParseException e) {
                    e.printStackTrace();
                    error = true;
                }

                if (error) {
                    return "";
                } else {
                    String fechaStr = "";
                    if (fecha != null) {
                        DateFormat df = new SimpleDateFormat(FORMATO_FECHA_FUNCION, getSpanishLocale());
                        fechaStr = df.format(fecha.getTime());
                    }
                    return fechaStr;
                }
            }
        }else {
            return "";
        }
    }

    public static Calendar stringApiToCalendar(String fechaFormatoApi) {
        Calendar fecha = null;
        if (fechaFormatoApi.equals("")) {
            return null;
        } else {
            boolean error = false;
            SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_API, getSpanishLocale());
            try {
                fecha = Calendar.getInstance();
                fecha.setTime(formatter.parse(fechaFormatoApi));

            } catch (ParseException e) {
                e.printStackTrace();
                error = true;
            }
        }
        return fecha;
    }

    public static String stringDefaultToFormatoFechaFuncion(String fechaFormatoApi){

        if(fechaFormatoApi.equals("")){
            return "";
        }else {

            boolean error = false;

            SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DEFAULT, getSpanishLocale());
            Calendar fecha = null;
            try {
                fecha = Calendar.getInstance();
                fecha.setTime(formatter.parse(fechaFormatoApi));

            } catch (ParseException e) {
                e.printStackTrace();
                error = true;
            }

            if(error){
                return "";
            }else{
                String fechaStr = "";
                if (fecha != null) {
                    DateFormat df = new SimpleDateFormat(FORMATO_FECHA_LARGA_FUNCION, getSpanishLocale());
                    fechaStr = df.format(fecha.getTime());
                }
                return fechaStr;
            }
        }
    }

    public static String stringDefaultToFormatoFechaCortaFuncion(String fechaFormatoApi){

        if(fechaFormatoApi.equals("")){
            return "";
        }else {

            boolean error = false;

            SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_DEFAULT, getSpanishLocale());
            Calendar fecha = null;
            try {
                fecha = Calendar.getInstance();
                fecha.setTime(formatter.parse(fechaFormatoApi));

            } catch (ParseException e) {
                e.printStackTrace();
                error = true;
            }

            if(error){
                return "";
            }else{
                String fechaStr = "";
                if (fecha != null) {
                    DateFormat df = new SimpleDateFormat(FORMATO_FECHA_CORTA_FUNCION, getSpanishLocale());
                    fechaStr = df.format(fecha.getTime());
                }
                return fechaStr;
            }
        }
    }

    public static Calendar stringFuncionCompraToCalendar(String fechaFuncionCompra) {
        Calendar fecha = null;
        if (fechaFuncionCompra.equals("")) {
            return null;
        } else {
            boolean error = false;
            SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_FUNCION_COMPRA, getSpanishLocale());
            try {
                fecha = Calendar.getInstance();
                fecha.setTime(formatter.parse(fechaFuncionCompra));

            } catch (ParseException e) {
                e.printStackTrace();
                error = true;
            }
        }
        return fecha;
    }

    public static Date calendarToDate(Calendar cal){
        Date fecha = cal.getTime();
        return fecha;
    }

    public static Calendar stringApiShortToCalendar(String fechaFormatoApiShort) {
        Calendar fecha = null;
        if (fechaFormatoApiShort.equals("")) {
            return null;
        } else {
            boolean error = false;
            SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_FECHA_API_SHORT, getSpanishLocale());
            try {
                fecha = Calendar.getInstance();
                fecha.setTime(formatter.parse(fechaFormatoApiShort));

            } catch (ParseException e) {
                e.printStackTrace();
                error = true;
            }
        }
        return fecha;
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String calendarToString(Calendar cal){
        String fechaStr = "";
        if (cal != null) {
            DateFormat df = new SimpleDateFormat(FORMATO_FECHA_DEFAULT, getSpanishLocale());
            fechaStr = df.format(cal.getTime());
        }
        return fechaStr;
    }

    public static String calendarToStringApi(Calendar cal){
        String fechaStr = "";
        if (cal != null) {
            DateFormat df = new SimpleDateFormat(FORMATO_FECHA_API, getSpanishLocale());
            fechaStr = df.format(cal.getTime());
        }
        return fechaStr;
    }

    public static String calendarToStringNotificacion(Calendar cal){
        String fechaStr = "";
        if (cal != null) {
            DateFormat df = new SimpleDateFormat(FORMATO_FECHA_NOTIF, getSpanishLocale());
            fechaStr = df.format(cal.getTime());
        }
        return fechaStr;
    }

    public static String calendarToStringLargo(Calendar cal){
        String fechaStr = "";
        if (cal != null) {
            DateFormat df = new SimpleDateFormat(FORMATO_FECHA_LARGA, getSpanishLocale());
            fechaStr = df.format(cal.getTime());
        }
        return fechaStr;
    }
}

/*
*
* public static String timeZoneCode(Funcion funcionCualquiera){
        //por defecto la time zone de argentina
        String code = "GMT-03:00";
        try {
            String realStart = funcionCualquiera.getRealStart();
            String rawCode = realStart.substring(realStart.length() - 6, realStart.length());
            code = "GMT" + rawCode;
        }catch(Exception e){
            e.printStackTrace();
        }

        return code;
    }

    public static Calendar getNowInTimeZone(String timeZoneCode){
        TimeZone tz = TimeZone.getTimeZone(timeZoneCode);
        Log.i("CINEMA TIMEZONE", tz.getID() + " - " + tz.getDisplayName());
        Calendar cinemaCal = Calendar.getInstance();
        cinemaCal.setTimeZone(tz);

        return cinemaCal;
    }*/