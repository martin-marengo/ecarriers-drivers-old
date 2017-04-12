package com.ecarriers.drivers.data.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecarriers.drivers.data.db.entities.ItemDB;
import com.ecarriers.drivers.data.db.entities.ShipmentPublicationDB;
import com.ecarriers.drivers.models.Item;
import com.ecarriers.drivers.models.ShipmentPublication;

import java.util.ArrayList;

import io.realm.RealmList;

class ShipmentPublicationsDAO extends RealmDAO {

    ShipmentPublicationsDAO(Context context){
        super(context);
    }

    // MAPPERS

    static ShipmentPublicationDB mapToEntity(@NonNull final ShipmentPublication shipmentPublication){
        ShipmentPublicationDB dbShipmentPublication = new ShipmentPublicationDB();

        dbShipmentPublication.setId(shipmentPublication.getId());
        dbShipmentPublication.setClient(shipmentPublication.getClient());
        dbShipmentPublication.setDescription(shipmentPublication.getDescription());
        dbShipmentPublication.setOriginAddress(shipmentPublication.getOriginAddress());
        dbShipmentPublication.setDestinationAddress(shipmentPublication.getDestinationAddress());
        dbShipmentPublication.setState(shipmentPublication.getState());

        if(shipmentPublication.getItems() != null) {
            RealmList<ItemDB> itemDBs = new RealmList<>();
            for(Item item : shipmentPublication.getItems()){
                ItemDB itemDB = ItemsDAO.mapToEntity(item);
                itemDBs.add(itemDB);
            }
            dbShipmentPublication.setItems(itemDBs);
        }

        return dbShipmentPublication;
    }

    static ShipmentPublication mapToModel(@NonNull final ShipmentPublicationDB dbShipmentPublication, long tripId){
        ShipmentPublication shipmentPublication = new ShipmentPublication();

        shipmentPublication.setId(dbShipmentPublication.getId());
        shipmentPublication.setClient(dbShipmentPublication.getClient());
        shipmentPublication.setDescription(dbShipmentPublication.getDescription());
        shipmentPublication.setOriginAddress(dbShipmentPublication.getOriginAddress());
        shipmentPublication.setDestinationAddress(dbShipmentPublication.getDestinationAddress());
        shipmentPublication.setState(dbShipmentPublication.getState());
        shipmentPublication.setTripId(tripId);

        if(dbShipmentPublication.getItems() != null){
            ArrayList<Item> items = new ArrayList<>();
            for(ItemDB dbItem : dbShipmentPublication.getItems()){
                Item item = ItemsDAO.mapToModel(dbItem, shipmentPublication.getId());
                items.add(item);
            }
            shipmentPublication.setItems(items);
        }

        return shipmentPublication;
    }
}
