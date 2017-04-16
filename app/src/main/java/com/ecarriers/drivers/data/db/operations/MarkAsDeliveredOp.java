package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;

public class MarkAsDeliveredOp extends RealmObject {

    private int id;
    private long shipmentPublicationId;
    private boolean sync;

    public MarkAsDeliveredOp(){
        sync = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getShipmentPublicationId() {
        return shipmentPublicationId;
    }

    public void setShipmentPublicationId(long shipmentPublicationId) {
        this.shipmentPublicationId = shipmentPublicationId;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync() {
        this.sync = true;
    }
}
