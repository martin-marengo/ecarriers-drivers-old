package com.ecarriers.drivers.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportLocationRequest {

    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @SerializedName("trip_id")
    @Expose
    private long tripId;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    public ReportLocationRequest(String apiToken, long tripId, double lat, double lng) {
        this.apiToken = apiToken;
        this.tripId = tripId;
        this.lat = lat;
        this.lng = lng;
    }
}
