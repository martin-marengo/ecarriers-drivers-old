package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    @SerializedName("status")
    @Expose
    private String state;

    @SerializedName("departure_date")
    @Expose
    private String departureDate;

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

    public static void sort(ArrayList<Trip> list){
        final Integer driving = 1;
        final Integer pending = 2;
        final Integer finished = 3;

        Collections.sort(list, new Comparator<Trip>() {
            @Override
            public int compare(Trip t1, Trip t2) {
                Integer t1v, t2v;

                if (t1.getState().equals(TripStates.STATUS_DRIVING.toString())){
                    t1v = driving;
                } else {
                    if (t1.getState().equals(TripStates.STATUS_HAVE_TO_FETCH_ITEMS.toString())){
                        t1v = pending;
                    }else {
                        t1v = finished;
                    }
                }

                if (t2.getState().equals(TripStates.STATUS_DRIVING.toString())){
                    t2v = driving;
                } else {
                    if (t2.getState().equals(TripStates.STATUS_HAVE_TO_FETCH_ITEMS.toString())){
                        t2v = pending;
                    }else {
                        t2v = finished;
                    }
                }

                return t1v.compareTo(t2v);
            }
        });
    }

    public boolean canFinish(){
        boolean canFinish = false;
        if(getState().equals(TripStates.STATUS_DRIVING.toString())){
            if(getShipmentPublications() != null && !getShipmentPublications().isEmpty()){
                boolean allDelivered = true;
                for(ShipmentPublication sp : getShipmentPublications()){
                    if (!sp.getState().equals(ShipmentPublication.States.STATUS_DELIVERED.toString())){
                        allDelivered = false;
                    }
                }
                if(allDelivered){
                    canFinish = true;
                }
            }
        }

        return canFinish;
    }

    public boolean canStartTrip(){
        if(this.getState().equals(TripStates.STATUS_HAVE_TO_FETCH_ITEMS.toString())){
            return true;
        }else{
            return false;
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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public ArrayList<ShipmentPublication> getShipmentPublications() {
        return shipmentPublications;
    }

    public void setShipmentPublications(ArrayList<ShipmentPublication> shipmentPublications) {
        this.shipmentPublications = shipmentPublications;
    }
}
