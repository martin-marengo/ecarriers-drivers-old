package com.ecarriers.drivers.utils;

public class Constants {

    /**
     * Network
     */

    // Production
    //public static final String ECARRIERS_BASE_URL = "http://ecarriers.herokuapp/apipie/";

    // Testing
    public static final String ECARRIERS_BASE_URL = "http://192.168.1.119:3000/api/v1/";

    public static final int HTTP_OK = 200;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_BAD_REQUEST = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;

    /**
     * Navigation
     * */

    public static final String KEY_TRIP_ID = "trip_id";

    /**
     * View
     * */

    public static final int DISTANCE_TO_TRIGGER_SYNC = 300; //in dpi

}
