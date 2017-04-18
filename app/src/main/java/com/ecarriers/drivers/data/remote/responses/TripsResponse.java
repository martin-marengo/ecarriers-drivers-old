package com.ecarriers.drivers.data.remote.responses;

import com.ecarriers.drivers.models.Trip;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TripsResponse {

    @SerializedName("trips")
    @Expose
    private ArrayList<Trip> trips;

    public ArrayList<Trip> getTrips() {
        return trips;
    }
}
