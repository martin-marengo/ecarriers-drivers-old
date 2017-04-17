package com.ecarriers.drivers.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

public class GeolocationUtils {

    public static final double INVALID_GEO_VALUES = 1000;
    private static final String TAG = "GEOLOCATION";

    public static boolean locationPermissionGranted(Context context){
        boolean hasPermission = false;
        int hasLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            hasPermission = true;
        }
        return hasPermission;
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
