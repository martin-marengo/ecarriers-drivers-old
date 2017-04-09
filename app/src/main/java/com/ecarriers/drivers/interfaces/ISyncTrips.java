package com.ecarriers.drivers.interfaces;

import com.ecarriers.drivers.data.remote.pojos.TripsResponse;

public interface ISyncTrips {

    void onResponse(boolean success, TripsResponse response);
}
