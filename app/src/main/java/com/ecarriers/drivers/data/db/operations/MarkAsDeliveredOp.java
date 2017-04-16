package com.ecarriers.drivers.data.db.operations;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MarkAsDeliveredOp extends RealmObject {

    public static final int OPERATION_TYPE = 4;

    @PrimaryKey
    private long timestamp;
    private long shipmentPublicationId;
    private boolean sync;

    public MarkAsDeliveredOp(){
        sync = false;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
