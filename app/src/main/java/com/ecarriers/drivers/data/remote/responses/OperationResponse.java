package com.ecarriers.drivers.data.remote.responses;

import com.google.gson.annotations.SerializedName;

public class OperationResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

}
