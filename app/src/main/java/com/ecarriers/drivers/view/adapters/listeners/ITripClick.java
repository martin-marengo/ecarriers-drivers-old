package com.ecarriers.drivers.view.adapters.listeners;

import com.ecarriers.drivers.models.Trip;

public interface ITripClick {

    void onTripClick(int position, Trip trip);
    void onStartTripClick(int position, Trip trip);
}
