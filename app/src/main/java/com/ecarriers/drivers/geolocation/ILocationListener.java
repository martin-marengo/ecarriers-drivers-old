package com.ecarriers.drivers.geolocation;

import android.location.Location;

public interface ILocationListener {

    void onLocationUpdated(boolean exito, Location location);
}
