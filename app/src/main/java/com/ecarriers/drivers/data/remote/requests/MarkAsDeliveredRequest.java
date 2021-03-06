package com.ecarriers.drivers.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkAsDeliveredRequest {

    @SerializedName("api_token")
    @Expose
    private String apiToken;

    @SerializedName("id")
    @Expose
    private long shipmentPublicationId;

    public MarkAsDeliveredRequest(String apiToken, long shipmentPublicationId) {
        this.apiToken = apiToken;
        this.shipmentPublicationId = shipmentPublicationId;
    }
}
