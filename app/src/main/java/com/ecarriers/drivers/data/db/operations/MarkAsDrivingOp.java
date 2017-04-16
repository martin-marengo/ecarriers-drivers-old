package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;

public class MarkAsDrivingOp extends RealmObject {

    private int id;
    private long tripId;
    private boolean sync;

    public MarkAsDrivingOp(){
        sync = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setSync() {
        this.sync = true;
    }
}
