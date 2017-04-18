package com.ecarriers.drivers.data.remote.listeners;

import com.ecarriers.drivers.data.remote.responses.LoginResponse;

public interface ILoginListener {

    void onResponse(boolean success, LoginResponse response);
}
