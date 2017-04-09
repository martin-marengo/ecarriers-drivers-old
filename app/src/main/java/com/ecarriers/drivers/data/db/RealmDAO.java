package com.ecarriers.drivers.data.db;

import android.content.Context;

abstract class RealmDAO {

    protected Context context;

    RealmDAO(Context context){
        this.context = context;
    }
}
