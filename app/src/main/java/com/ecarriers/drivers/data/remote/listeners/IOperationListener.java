package com.ecarriers.drivers.data.remote.listeners;

import com.ecarriers.drivers.data.remote.responses.OperationResponse;

public interface IOperationListener {

    void onResponse(boolean success, OperationResponse response);
}
