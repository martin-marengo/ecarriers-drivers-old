package com.ecarriers.drivers.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAsDrivingRequest {

    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @SerializedName("id")
    @Expose
    private long tripId;

    public MarkAsDrivingRequest(String apiToken, long tripId) {
        this.apiToken = apiToken;
        this.tripId = tripId;
    }
}
