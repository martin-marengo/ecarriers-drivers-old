package com.ecarriers.drivers.interfaces;

import com.ecarriers.drivers.models.Trip;

public interface ITripClick {

    void onTripClick(int position, Trip trip);
    void onStartTripClick(int position, Trip trip);
}
