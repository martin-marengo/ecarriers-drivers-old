package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Trip {

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

    @SerializedName("shipment_publications")
    @Expose
    private ArrayList<ShipmentPublication> shipmentPublications;

    public enum TripStates {

        STATUS_HAVE_TO_FETCH_ITEMS("have_to_pick_up_items"),
        STATUS_DRIVING("driving"),
        STATUS_FINISHED("finished");

        private final String state;

        TripStates(final String state) {
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

    public ArrayList<ShipmentPublication> getShipmentPublications() {
        return shipmentPublications;
    }

    public void setShipmentPublications(ArrayList<ShipmentPublication> shipmentPublications) {
        this.shipmentPublications = shipmentPublications;
    }
}
