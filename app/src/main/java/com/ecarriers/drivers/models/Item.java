package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Item {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    private long shipmentPublicationId;

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

    @Override
    public String toString() {
        return getDescription() + " (" + String.valueOf(getQuantity()) + ")";
    }

    public static String[] getArrayFromList(ArrayList<Item> items){
        ArrayList<String> itemsList = new ArrayList<>();
        for(Item item : items){
            itemsList.add(item.toString());
        }
        String[] itemsArray = new String[itemsList.size()];
        itemsArray = itemsList.toArray(itemsArray);
        return itemsArray;
    }
}
