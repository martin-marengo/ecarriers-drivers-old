package com.ecarriers.drivers.data.remote.listeners;

public interface IAsyncResponse {

    void onResponse(boolean success, String key);
}
