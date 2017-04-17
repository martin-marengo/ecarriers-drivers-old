package com.ecarriers.drivers.utils;

import android.location.Location;

public interface ILocationListener {

    void onLocationUpdated(boolean exito, Location location);
}
