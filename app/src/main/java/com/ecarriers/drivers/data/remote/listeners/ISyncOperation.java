package com.ecarriers.drivers.data.remote.listeners;

import com.ecarriers.drivers.data.remote.pojos.OperationResponse;

public interface ISyncOperation {

    void onResponse(boolean success, OperationResponse response);
}
