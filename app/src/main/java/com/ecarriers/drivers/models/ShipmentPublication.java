package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShipmentPublication {

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

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("items")
    @Expose
    private ArrayList<Item> items;

    private long tripId;

    public enum ShipmentPublicationStates {

        STATUS_WAITING_PICKUP("waiting_pickup"),
        STATUS_BEING_SHIPPED("being_shipped"),
        STATUS_DELIVERED("delivered");

        private final String state;

        ShipmentPublicationStates(final String state) {
            this.state = state;
        }

        @Override
        public String toString() {
            return state;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
}
