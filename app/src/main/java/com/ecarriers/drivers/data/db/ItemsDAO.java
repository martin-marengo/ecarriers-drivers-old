package com.ecarriers.drivers.data.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ecarriers.drivers.data.db.entities.ItemDB;
import com.ecarriers.drivers.models.Item;

class ItemsDAO extends RealmDAO {

    ItemsDAO(Context context){
        super(context);
    }

    // MAPPERS

    static ItemDB mapToEntity(@NonNull final Item item){
        ItemDB dbItem = new ItemDB();

        dbItem.setDescription(item.getDescription());
        dbItem.setQuantity(item.getQuantity());

        return dbItem;
    }

    static Item mapToModel(@NonNull final ItemDB dbItem, long shipmentPublicationId){
        Item item = new Item();

        item.setDescription(dbItem.getDescription());
        item.setQuantity(dbItem.getQuantity());
        item.setShipmentPublicationId(shipmentPublicationId);

        return item;
    }
}
