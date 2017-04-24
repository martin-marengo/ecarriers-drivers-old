package com.ecarriers.drivers.data.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecarriers.drivers.data.db.entities.ShipmentPublicationDB;
import com.ecarriers.drivers.data.db.entities.TripDB;
import com.ecarriers.drivers.models.ShipmentPublication;
import com.ecarriers.drivers.models.Trip;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

class TripsDAO extends RealmDAO {

    TripsDAO(Context context) {
        super(context);
    }

    boolean insertOrUpdateTrips(@NonNull final ArrayList<Trip> trips) {
        boolean success = true;

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ArrayList<TripDB> dbTrips = new ArrayList<>();
                    for (Trip trip : trips) {
                        TripDB dbTrip = TripsDAO.mapToEntity(trip);
                        dbTrips.add(dbTrip);
                    }

                    realm.copyToRealmOrUpdate(dbTrips);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    boolean deleteTrips(){
        boolean success = true;

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmResults<TripDB> trips = realm.where(TripDB.class).findAll();
                    trips.deleteAllFromRealm();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    ArrayList<Trip> getActiveTrips(){
        ArrayList<Trip> tripList = new ArrayList<>();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {

            // Before getting all trips, delete the finished ones.
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<TripDB> finishedTrips = realm.where(TripDB.class)
                            .equalTo(TripDB.STATE, Trip.TripStates.STATUS_FINISHED.toString())
                            .findAll();
                    finishedTrips.deleteAllFromRealm();
                }
            });

            final RealmResults<TripDB> trips = realm.where(TripDB.class).findAll();
            if (trips != null) {
                for (TripDB dbTrip : trips) {
                    Trip trip = TripsDAO.mapToModel(dbTrip);
                    tripList.add(trip);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return tripList;
    }

    Trip getActiveTrip(long id){
        Trip trip = new Trip();

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            final TripDB dbTrip = realm.where(TripDB.class).equalTo(TripDB.ID, id).findFirst();
            if (dbTrip != null) {
                trip = TripsDAO.mapToModel(dbTrip);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return trip;
    }

    boolean updateTrip(@NonNull final Trip trip) {
        boolean success = true;

        // Initialize Realm
        Realm.init(context);
        // Get a Realm instance for this thread
        Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    TripDB dbTrip = TripsDAO.mapToEntity(trip);
                    realm.copyToRealmOrUpdate(dbTrip);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    // MAPPERS

    private static TripDB mapToEntity(@NonNull final Trip trip){
        TripDB dbTrip = new TripDB();

        dbTrip.setId(trip.getId());
        dbTrip.setOrigin(trip.getOrigin());
        dbTrip.setDestination(trip.getDestination());
        dbTrip.setDepartureDate(trip.getDepartureDate());
        dbTrip.setState(trip.getState());

        if(trip.getShipmentPublications() != null){
            RealmList<ShipmentPublicationDB> dbShipmentPublications = new RealmList<>();
            for(ShipmentPublication sp : trip.getShipmentPublications()){
                ShipmentPublicationDB dbSp = ShipmentPublicationsDAO.mapToEntity(sp);
                dbShipmentPublications.add(dbSp);
            }
            dbTrip.setShipmentPublications(dbShipmentPublications);
        }

        return dbTrip;
    }

    private static Trip mapToModel(@NonNull final TripDB dbTrip){
        Trip trip = new Trip();

        trip.setId(dbTrip.getId());
        trip.setOrigin(dbTrip.getOrigin());
        trip.setDestination(dbTrip.getDestination());
        trip.setDepartureDate(dbTrip.getDepartureDate());
        trip.setState(dbTrip.getState());

        if(dbTrip.getShipmentPublications() != null){
            ArrayList<ShipmentPublication> shipmentPublications = new ArrayList<>();
            for(ShipmentPublicationDB dbSp : dbTrip.getShipmentPublications()){
                ShipmentPublication sp = ShipmentPublicationsDAO.mapToModel(dbSp, trip.getId());
                shipmentPublications.add(sp);
            }
            trip.setShipmentPublications(shipmentPublications);
        }

        return trip;
    }
}
