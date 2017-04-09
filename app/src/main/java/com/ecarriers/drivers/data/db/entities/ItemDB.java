package com.ecarriers.drivers.data.db.entities;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class ItemDB extends RealmObject {

    private String description;
    private int quantity;

    @Ignore
    private long shipmentPublicationId;

    public ItemDB(){}

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
