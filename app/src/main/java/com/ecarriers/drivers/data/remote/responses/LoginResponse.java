package com.ecarriers.drivers.data.remote.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("api_token")
    private String token;

    public String getToken() {
        return token;
    }
}
