package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;

public class ReportLocationOp extends RealmObject {

    private int id;
    private long tripId;
    private double lat;
    private double lng;
    private boolean sync;

    public ReportLocationOp(){
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

    public void setSync() {
        this.sync = true;
    }
}
