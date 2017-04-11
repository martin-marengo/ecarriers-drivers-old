package com.ecarriers.drivers.data.db.entities;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TripDB extends RealmObject {

    @PrimaryKey
    private long id;
    private String origin;
    private String destination;
    private String state;
    private String departureDate;
    private RealmList<ShipmentPublicationDB> shipmentPublications;

    public TripDB(){}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public RealmList<ShipmentPublicationDB> getShipmentPublications() {
        return shipmentPublications;
    }
    public void setShipmentPublications(RealmList<ShipmentPublicationDB> shipmentPublications) {
        this.shipmentPublications = shipmentPublications;
    }
}
