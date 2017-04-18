package com.ecarriers.drivers.data.remote.listeners;

import com.ecarriers.drivers.data.remote.responses.TripsResponse;

public interface ITripsListener {

    void onResponse(boolean success, TripsResponse response);
}
