package com.ecarriers.drivers.data.db.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ShipmentPublicationDB extends RealmObject {

    @PrimaryKey
    private long id;
    private String client;
    private String description;
    private String originAddress;
    private String destinationAddress;
    private String state;
    private RealmList<ItemDB> items;

    @Ignore
    private long tripId;

    public ShipmentPublicationDB(){}

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

    public long getTripId() {
        return tripId;
    }
    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public RealmList<ItemDB> getItems() {
        return items;
    }
    public void setItems(RealmList<ItemDB> items) {
        this.items = items;
    }
}
