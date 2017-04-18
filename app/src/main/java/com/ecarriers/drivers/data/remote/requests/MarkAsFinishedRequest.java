package com.ecarriers.drivers.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAsFinishedRequest {

    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @SerializedName("id")
    @Expose
    private long tripId;

    public MarkAsFinishedRequest(String apiToken, long tripId) {
        this.apiToken = apiToken;
        this.tripId = tripId;
    }
}
