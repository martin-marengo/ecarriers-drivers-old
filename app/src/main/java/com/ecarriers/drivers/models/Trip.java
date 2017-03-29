package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Trip extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("origin")
    @Expose
    private String origin;

    @SerializedName("destination")
    @Expose
    private String destination;

    @SerializedName("state")
    @Expose
    private String state;

    public Trip(long id, String origin, String destination, String state){
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getState() {
        return state;
    }
}
