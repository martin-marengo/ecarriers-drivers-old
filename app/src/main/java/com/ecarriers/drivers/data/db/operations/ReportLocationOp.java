package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReportLocationOp extends RealmObject {

    public static final int OPERATION_TYPE = 5;

    @PrimaryKey
    private long timestamp;
    private long tripId;
    private double lat;
    private double lng;
    private boolean sync;

    public ReportLocationOp(){
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
