package com.ecarriers.drivers.view.adapters.listeners;

import com.ecarriers.drivers.models.ShipmentPublication;

public interface IShipmentPublicationClick {

    void onShipmentPublicationClick(int position, ShipmentPublication item);
    void onChangeStateClick(int position, ShipmentPublication item);
}
