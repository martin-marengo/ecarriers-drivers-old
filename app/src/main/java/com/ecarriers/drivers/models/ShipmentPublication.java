package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ShipmentPublication extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("client")
    @Expose
    private String client;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("originAddress")
    @Expose
    private String originAddress;

    @SerializedName("destinationAddress")
    @Expose
    private String destinationAddress;

    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    @SerializedName("state")
    @Expose
    private String state;

    public ShipmentPublication(long id, String client, String description, String originAddress,
                               String destinationAddress, ArrayList<Item> items, String state) {
        this.id = id;
        this.client = client;
        this.description = description;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.items = items;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public String getClient() {
        return client;
    }

    public String getDescription() {
        return description;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getState() {
        return state;
    }
}
