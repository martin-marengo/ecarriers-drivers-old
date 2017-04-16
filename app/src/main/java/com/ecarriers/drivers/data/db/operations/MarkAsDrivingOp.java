package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarkAsDrivingOp extends RealmObject {

    public static final int OPERATION_TYPE = 1;

    @PrimaryKey
    private long timestamp;
    private long tripId;
    private boolean sync;

    public MarkAsDrivingOp(){
        sync = false;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
