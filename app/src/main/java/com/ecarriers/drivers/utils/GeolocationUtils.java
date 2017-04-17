package com.ecarriers.drivers.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.text.DecimalFormat;

public class GeolocationUtils {

    public static final double INVALID_GEO_VALUES = 1000;
    private static final String TAG = "GEOLOCATION";

    public static double getDistanciaKm(double latUser, double lonUser, double latTarget, double lonTarget){
        int Radius = 6371; // radius of earth in Km
        double lat1 = latTarget;
        double lat2 = latUser;
        double lon1 = lonTarget;
        double lon2 = lonUser;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        /*Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);*/
        return kmInDec;
    }

    public static void printLocation(double lat, double lon){
        String geoloc = "LAT: " + String.valueOf(lat) + " - LON: " + String.valueOf(lon);
        Log.i(TAG, geoloc);
    }

    public static void printLocation(Location location){
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        String geoloc = "LAT: " + String.valueOf(lat) + " - LON: " + String.valueOf(lon);
        Log.i(TAG, geoloc);
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                //Toast.makeText(context,"Ubicacion 1",Toast.LENGTH_LONG).show();
                return false;
            }

            //Toast.makeText(context,"Ubicacion 2",Toast.LENGTH_LONG).show();
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            //Toast.makeText(context,"Ubicacion 3",Toast.LENGTH_LONG).show();
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static boolean isGpsEnabled(Context context){

        final LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
        if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER)){
            return false;
        }
        return true;
    }
}
