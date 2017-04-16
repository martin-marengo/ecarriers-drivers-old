package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ReportLocationOp extends RealmObject {

    public static final int OPERATION_TYPE = 5;
    public static final String TAG = "report_location_op";

    @PrimaryKey
    private long timestamp;
    private long tripId;
    private float lat;
    private float lng;
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

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
