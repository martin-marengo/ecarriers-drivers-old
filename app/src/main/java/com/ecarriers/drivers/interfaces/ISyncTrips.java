package com.ecarriers.drivers.interfaces;

import com.ecarriers.drivers.remote.pojos.TripsResponse;

public interface ISyncTrips {

    void onResponseButacas(boolean exito, TripsResponse response);
}
