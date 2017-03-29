package com.ecarriers.drivers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Item extends RealmObject {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    public Item(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }
}
