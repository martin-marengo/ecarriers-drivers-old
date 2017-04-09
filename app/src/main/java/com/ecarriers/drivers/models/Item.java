package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Item extends RealmObject {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    private long shipmentPublicationId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getShipmentPublicationId() {
        return shipmentPublicationId;
    }

    public void setShipmentPublicationId(long shipmentPublicationId) {
        this.shipmentPublicationId = shipmentPublicationId;
    }
}
