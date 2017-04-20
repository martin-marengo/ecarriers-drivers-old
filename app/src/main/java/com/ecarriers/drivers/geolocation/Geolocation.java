package com.ecarriers.drivers.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Geolocation implements LocationListener {

    private ILocationListener mListener = null;
    private Context mContext = null;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    //Singleton
    public static Geolocation geolocation;
    private boolean mKeepListening = false;
    private static final String TAG = "GEOLOCATION";
    private static final int TIME_OUT_MS = 10000;

    private Handler mHandler = null;
    private Runnable mUpdateUIRunnable = null;
    private Timer mListeningTimer = null;

    public static Geolocation getInstance(Context context, ILocationListener listener){
        if(geolocation == null){
            geolocation = new Geolocation(context, listener);
        }
        geolocation.mContext = context;
        geolocation.mListener = listener;
        return geolocation;
    }

    private Geolocation(Context context, ILocationListener listener) {
        this.mContext = context;
        mListener = listener;

        mHandler = new Handler();
        mUpdateUIRunnable = new Runnable() {
            public void run() {
                mListener.onLocationUpdated(false, null);
            }
        };

        startListeningForLocationUpdates();
    }

    private void startListeningForLocationUpdates(){
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            // Get the location manager
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = "";
            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // Begin listening for updates using mobile network or wifi
                locationProvider = LocationManager.NETWORK_PROVIDER;
            }else{
                // Or, use GPS location data:
                locationProvider = LocationManager.GPS_PROVIDER;
            }
            mLocationListener = this;
            mLocationManager.requestLocationUpdates(locationProvider, 0, 0, mLocationListener);
        }
    }

    public void getLastLocation(){
        Location finalLoc = getLastKnownLocation();
        if(finalLoc != null){
            GeolocationUtils.printLocation(finalLoc);
            mListener.onLocationUpdated(true, finalLoc);
            stopListeningUpdates();
        }else{
            if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                    (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
                Log.i(TAG, "Keep Listening");
                mKeepListening = true;
                // Sigue escuchando x segundos, y si no llega nada al onLocationChanged, tira que no encontró.
                startReturnTimer();
            }else{
                mListener.onLocationUpdated(false, null);
            }
        }
    }

    private void startReturnTimer(){
        mListeningTimer = new Timer();
        mListeningTimer.schedule(new TimerTask(){
            public void run() {
                if(mKeepListening) {
                    mKeepListening = false;
                    stopListeningUpdates();
                    asyncUpdateUI();
                }
            }
        }, TIME_OUT_MS);
    }

    private void asyncUpdateUI() {
        mHandler.post(mUpdateUIRunnable);
    }

    private Location getLastKnownLocation(){
        Location finalLoc = null;
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                if (mLocationManager == null) {
                    // Get the location manager
                    mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                }
                String provider = LocationManager.GPS_PROVIDER;
                Location location = mLocationManager.getLastKnownLocation(provider);

                if (location == null) {
                    provider = LocationManager.NETWORK_PROVIDER;
                    location = mLocationManager.getLastKnownLocation(provider);
                }
                if (location != null) {
                    finalLoc = location;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return finalLoc;
    }

    @Override
    public void onLocationChanged(Location location) {
        GeolocationUtils.printLocation(location);
        if(mKeepListening){
            mKeepListening = false;
            if(mListeningTimer != null) {
                mListeningTimer.cancel();
            }
            mListener.onLocationUpdated(true, location);
            stopListeningUpdates();
        }
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }
    @Override
    public void onProviderEnabled(String s) {
    }
    @Override
    public void onProviderDisabled(String s) {
    }

    public void stopListeningUpdates() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Remove the listener you previously added
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
