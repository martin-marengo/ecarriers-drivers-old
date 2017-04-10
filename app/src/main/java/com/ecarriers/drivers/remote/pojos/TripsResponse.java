package com.ecarriers.drivers.remote.pojos;

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
