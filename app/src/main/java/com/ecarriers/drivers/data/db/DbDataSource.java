package com.ecarriers.drivers.data.db;

import android.content.Context;

import com.ecarriers.drivers.models.ShipmentPublication;
import com.ecarriers.drivers.models.Trip;

import java.util.ArrayList;

public class DbDataSource {

    private Context context;

    public DbDataSource(Context context){
        this.context = context;
    }

    public boolean saveActiveTrips(ArrayList<Trip> trips){
        TripsDAO tripsDAO = new TripsDAO(context);

        return tripsDAO.insertOrUpdateTrips(trips);
    }

    public boolean deleteTrips(){
        TripsDAO tripsDAO = new TripsDAO(context);

        return tripsDAO.deleteTrips();
    }

    public ArrayList<Trip> getActiveTrips(){
        TripsDAO tripsDAO = new TripsDAO(context);

        return tripsDAO.getActiveTrips();
    }

    public Trip getActiveTrip(long id){
        TripsDAO tripsDAO = new TripsDAO(context);

        return tripsDAO.getActiveTrip(id);
    }

    public boolean updateTrip(Trip trip){
        TripsDAO tripsDAO = new TripsDAO(context);

        return tripsDAO.updateTrip(trip);
    }

    public boolean updateShipmentPublication(ShipmentPublication sp){
        ShipmentPublicationsDAO shipmentPublicationsDAO = new ShipmentPublicationsDAO(context);

        return shipmentPublicationsDAO.updateShipmentPublication(sp);
    }
}
